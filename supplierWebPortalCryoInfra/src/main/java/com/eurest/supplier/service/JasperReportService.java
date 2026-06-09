package com.eurest.supplier.service;

import com.eurest.supplier.model.Report;
import com.eurest.supplier.model.ReportDataSource;
import com.eurest.supplier.model.ReportExportOption;
import com.eurest.supplier.model.ReportParameter;
import com.eurest.supplier.model.ReportParameterMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

@Service("jasperReportService")
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    private String getReportDir() {
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = java.net.URLDecoder.decode(path, "UTF-8");
            return fullPath + "jasperReports";
        } catch (Exception e) {
            throw new RuntimeException("Error resolviendo el directorio de reportes en classpath", e);
        }
    }

    public java.sql.Connection getDataSourceConnection() throws Exception {
        return dataSource.getConnection();
    }

    public byte[] runReport(Report report, String format, Map<String, String> rawParams) throws Exception {
        boolean isDynamic = (report.getFile() == null || report.getFile().trim().isEmpty());
        String reportDir = getReportDir();
        File reportFile = null;

        if (!isDynamic) {
            reportFile = new File(reportDir, report.getFile());
            if (!reportFile.exists()) {
                String jrxmlName = report.getFile().replace(".jasper", ".jrxml");
                File jrxmlFile = new File(reportDir, jrxmlName);
                if (jrxmlFile.exists()) {
                    JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), reportFile.getAbsolutePath());
                } else if (report.getQuery() != null && !report.getQuery().trim().isEmpty()) {
                    // Fallback to dynamic if physical files don't exist but query is provided
                    isDynamic = true;
                } else {
                    throw new RuntimeException("No se encontró el archivo del reporte en: " + reportFile.getAbsolutePath());
                }
            }
        }

        Connection conn = null;
        boolean isExternalConn = false;

        try {
            if (report.getDataSource() != null) {
                ReportDataSource ds = report.getDataSource();
                if (ds.getJndi() != null && ds.getJndi()) {
                    javax.naming.InitialContext ctx = null;
                    try {
                        ctx = new javax.naming.InitialContext();
                        DataSource jndiDs = (DataSource) ctx.lookup(ds.getUrl());
                        conn = jndiDs.getConnection();
                    } finally {
                        if (ctx != null) {
                            ctx.close();
                        }
                    }
                } else {
                    Class.forName(ds.getDriverClassName());
                    conn = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
                }
                isExternalConn = true;
            } else {
                conn = dataSource.getConnection();
            }

            Map<String, Object> filledParams = parseParameters(report, rawParams);
            String fmt = (format != null) ? format.toLowerCase() : "pdf";
            if (!"pdf".equals(fmt)) {
                filledParams.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            }
            JasperPrint jasperPrint;

            if (isDynamic) {
                if (report.getQuery() == null || report.getQuery().trim().isEmpty()) {
                    throw new RuntimeException("El reporte no tiene plantilla y tampoco se ha definido una consulta SQL.");
                }

                // Generar plantilla dinámica en base a las columnas del query
                String processedSql = report.getQuery();
                String sNum = rawParams.get("addressNumber");
                processedSql = processSqlQuery(processedSql, sNum);
                
                // Reemplazar parámetros tipo Jasper ($P{...}) con ? para la consulta JDBC de metadata
                List<String> paramNamesInQuery = new ArrayList<String>();
                String jdbcSql = convertJasperParamsToJdbc(processedSql, paramNamesInQuery);
                // Eliminar punto y coma final que causaría error de sintaxis en subconsulta
                jdbcSql = jdbcSql.trim();
                if (jdbcSql.endsWith(";")) {
                    jdbcSql = jdbcSql.substring(0, jdbcSql.length() - 1).trim();
                }
                
                List<String> columnNames = new ArrayList<String>();
                List<String> columnLabels = new ArrayList<String>();
                List<String> columnTypes = new ArrayList<String>();

                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM (" + jdbcSql + ") t LIMIT 1")) {
                    int idx = 1;
                    for (String paramName : paramNamesInQuery) {
                        String val = rawParams.get(paramName);
                        Object typedVal = null;
                        for (ReportParameterMap pMap : report.getParameters()) {
                            ReportParameter rp = pMap.getReportParameter();
                            if (rp != null && rp.getName().equals(paramName)) {
                                typedVal = convertToType(val, rp.getClassName());
                                break;
                            }
                        }
                        if (typedVal == null && val != null) {
                            typedVal = val;
                        }
                        ps.setObject(idx++, typedVal);
                    }
                    
                    try (ResultSet rs = ps.executeQuery()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int cc = rsmd.getColumnCount();
                        for (int i = 1; i <= cc; i++) {
                            columnNames.add(rsmd.getColumnName(i));
                            columnLabels.add(rsmd.getColumnLabel(i));
                            columnTypes.add(rsmd.getColumnClassName(i));
                        }
                    }
                }
                
                if (columnNames.isEmpty()) {
                    throw new RuntimeException("La consulta del reporte no retornó ninguna columna.");
                }
                
                // Construir JasperReport dinámico
                JasperReport jr = generateDynamicReport(report, processedSql, columnNames, columnLabels, columnTypes, format);
                
                // Inyectar logo si está configurado en pdfDesignConfig
                boolean isImageFriendly = "pdf".equals(fmt) || "html".equals(fmt) || "rtf".equals(fmt) || "image".equals(fmt) || "img".equals(fmt);
                if (isImageFriendly && report.getPdfDesignConfig() != null && !report.getPdfDesignConfig().trim().isEmpty()) {
                    try {
                        com.fasterxml.jackson.databind.ObjectMapper logoMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        logoMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        PdfDesignConfig logoCfg = logoMapper.readValue(report.getPdfDesignConfig(), PdfDesignConfig.class);
                        if (logoCfg.logoBase64 != null && !logoCfg.logoBase64.trim().isEmpty()) {
                            String b64 = logoCfg.logoBase64;
                            if (b64.contains(",")) b64 = b64.substring(b64.indexOf(",") + 1);
                            byte[] imgBytes = java.util.Base64.getDecoder().decode(b64);
                            java.awt.image.BufferedImage logoImg = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(imgBytes));
                            if (logoImg != null) {
                                filledParams.put("LOGO_IMAGE", logoImg);
                            }
                        }
                    } catch (Exception logoEx) {
                        // Logo no crítico, continuar sin él
                    }
                }
                
                jasperPrint = JasperFillManager.fillReport(jr, filledParams, conn);
            } else {
                jasperPrint = JasperFillManager.fillReport(reportFile.getAbsolutePath(), filledParams, conn);
            }

            return exportReport(report, jasperPrint, format);

        } finally {
            if (conn != null && isExternalConn) {
                conn.close();
            }
        }
    }

    private JasperReport generateDynamicReport(Report report, String sql, List<String> columnNames, List<String> columnLabels, List<String> columnTypes, String format) throws Exception {
        JasperDesign jd = new JasperDesign();
        jd.setName(report.getName() != null ? report.getName().replaceAll("[^a-zA-Z0-9]", "") : "DynamicReport");
        jd.setLanguage("java");
        
        int colCount = columnNames.size();
        boolean landscape = colCount > 6;
        int pageWidth = landscape ? 792 : 612;
        int pageHeight = landscape ? 612 : 792;
        jd.setPageWidth(pageWidth);
        jd.setPageHeight(pageHeight);
        jd.setColumnWidth(pageWidth - 40);
        jd.setLeftMargin(20);
        jd.setRightMargin(20);
        jd.setTopMargin(20);
        jd.setBottomMargin(20);
        
        JRDesignQuery query = new JRDesignQuery();
        query.setText(sql);
        jd.setQuery(query);
        
        // Parámetros
        for (ReportParameterMap pMap : report.getParameters()) {
            ReportParameter p = pMap.getReportParameter();
            JRDesignParameter param = new JRDesignParameter();
            param.setName(p.getName());
            try {
                param.setValueClass(Class.forName(p.getClassName()));
            } catch (Exception e) {
                param.setValueClass(String.class);
            }
            jd.addParameter(param);
        }
        
        // Campos
        for (int i = 0; i < colCount; i++) {
            JRDesignField field = new JRDesignField();
            field.setName(columnNames.get(i));
            String type = columnTypes.get(i);
            try {
                if (type.contains("Int") || type.contains("Long") || type.contains("Integer")) {
                    field.setValueClass(Long.class);
                } else if (type.contains("Double") || type.contains("Float") || type.contains("Decimal") || type.contains("BigDecimal")) {
                    field.setValueClass(Double.class);
                } else if (type.contains("Date") || type.contains("Time") || type.contains("Timestamp")) {
                    field.setValueClass(java.util.Date.class);
                } else {
                    field.setValueClass(String.class);
                }
            } catch (Exception e) {
                field.setValueClass(String.class);
            }
            jd.addField(field);
        }
        
        // === Parsear configuración del diseñador PDF ===
        PdfDesignConfig cfg = new PdfDesignConfig();
        if (report.getPdfDesignConfig() != null && !report.getPdfDesignConfig().trim().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                cfg = mapper.readValue(report.getPdfDesignConfig(), PdfDesignConfig.class);
            } catch (Exception ignored) {
                // usar defaults
            }
        }
        
        // Aplicar orientación de la configuración
        if (cfg.landscape != null) {
            landscape = cfg.landscape;
            pageWidth = landscape ? 792 : 612;
            pageHeight = landscape ? 612 : 792;
            jd.setPageWidth(pageWidth);
            jd.setPageHeight(pageHeight);
            jd.setColumnWidth(pageWidth - 40);
        }
        
        // Colores del encabezado
        java.awt.Color headerBgColor = parseHexColor(cfg.headerBgColor, new java.awt.Color(0, 48, 110));
        java.awt.Color headerFgColor = parseHexColor(cfg.headerFgColor, java.awt.Color.WHITE);
        java.awt.Color titleColor = parseHexColor(cfg.titleColor, new java.awt.Color(0, 48, 110));
        
        // Fuente y tamaño
        float titleFontSize = cfg.titleFontSize != null ? cfg.titleFontSize : 16f;
        float dataFontSize = cfg.dataFontSize != null ? cfg.dataFontSize : 9f;
        // Alineación del título
        HorizontalTextAlignEnum titleAlign = HorizontalTextAlignEnum.CENTER;
        if ("LEFT".equalsIgnoreCase(cfg.titleAlign)) titleAlign = HorizontalTextAlignEnum.LEFT;
        else if ("RIGHT".equalsIgnoreCase(cfg.titleAlign)) titleAlign = HorizontalTextAlignEnum.RIGHT;
        
        // === Logo: si está configurado y el formato es amigable, agregar parámetro ===
        String fmt = (format != null) ? format.toLowerCase() : "pdf";
        boolean isImageFriendly = "pdf".equals(fmt) || "html".equals(fmt) || "rtf".equals(fmt) || "image".equals(fmt) || "img".equals(fmt);
        boolean hasLogo = isImageFriendly && (cfg.logoBase64 != null && !cfg.logoBase64.trim().isEmpty());
        if (hasLogo) {
            JRDesignParameter logoParam = new JRDesignParameter();
            logoParam.setName("LOGO_IMAGE");
            logoParam.setValueClass(java.awt.Image.class);
            jd.addParameter(logoParam);
        }
        
        int logoWidth = (cfg.logoWidth != null && cfg.logoWidth > 0) ? cfg.logoWidth : 80;
        int logoHeight = (cfg.logoHeight != null && cfg.logoHeight > 0) ? cfg.logoHeight : 40;
        int titleXOffset = hasLogo ? (logoWidth + 8) : 0;
        int titleAreaWidth = (pageWidth - 40) - titleXOffset;
        int titleBandHeight = Math.max(45, hasLogo ? (logoHeight + 5) : 45);
        
        // === Título Band (solo se imprime UNA vez al inicio del reporte) ===
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(titleBandHeight + 20); // +20 para fila de encabezados de columna
        
        // Logo
        if (hasLogo) {
            JRDesignImage logoImg = new JRDesignImage(null);
            JRDesignExpression logoExpr = new JRDesignExpression();
            logoExpr.setText("$P{LOGO_IMAGE}");
            logoImg.setExpression(logoExpr);
            logoImg.setX(0);
            logoImg.setY(0);
            logoImg.setWidth(logoWidth);
            logoImg.setHeight(logoHeight);
            logoImg.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
            titleBand.addElement(logoImg);
        }
        
        // Texto del título
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText(report.getName());
        titleText.setX(titleXOffset);
        titleText.setY(0);
        titleText.setWidth(titleAreaWidth);
        titleText.setHeight(25);
        titleText.setFontSize(titleFontSize);
        titleText.setBold(true);
        titleText.setForecolor(titleColor);
        titleText.setHorizontalTextAlign(titleAlign);
        titleBand.addElement(titleText);
        
        if (report.getDescription() != null && !report.getDescription().isEmpty()) {
            JRDesignStaticText descText = new JRDesignStaticText();
            descText.setText(report.getDescription());
            descText.setX(titleXOffset);
            descText.setY(25);
            descText.setWidth(titleAreaWidth);
            descText.setHeight(15);
            descText.setFontSize(10f);
            descText.setItalic(true);
            descText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleBand.addElement(descText);
        }
        
        // Detalle Band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(18);
        
        int usableWidth = pageWidth - 40;
        
        // Calcular anchos de columna
        int[] colWidths = new int[colCount];
        if (cfg.columns != null && cfg.columns.length == colCount) {
            int totalPct = 0;
            for (PdfColumnConfig cc : cfg.columns) {
                totalPct += (cc.widthPct != null ? cc.widthPct : 0);
            }
            if (totalPct > 0) {
                int usedWidth = 0;
                for (int i = 0; i < colCount; i++) {
                    PdfColumnConfig cc = cfg.columns[i];
                    int pct = cc.widthPct != null ? cc.widthPct : (100 / colCount);
                    colWidths[i] = (i == colCount - 1) ? (usableWidth - usedWidth) : (int) Math.round(usableWidth * pct / 100.0);
                    usedWidth += colWidths[i];
                }
            } else {
                int defaultCellWidth = usableWidth / colCount;
                for (int i = 0; i < colCount; i++) colWidths[i] = defaultCellWidth;
            }
        } else {
            int defaultCellWidth = usableWidth / colCount;
            for (int i = 0; i < colCount; i++) colWidths[i] = defaultCellWidth;
        }
        
        // Fila de encabezados de columna: va al final del title band (se imprime UNA sola vez)
        int headerYInTitle = titleBandHeight; // justo debajo del título
        
        int xOffset = 0;
        for (int i = 0; i < colCount; i++) {
            int x = xOffset;
            int cellWidth = colWidths[i];
            xOffset += cellWidth;
            
            String name = columnNames.get(i);
            String label = columnLabels.get(i);
            
            if (cfg.columns != null && i < cfg.columns.length && cfg.columns[i].header != null && !cfg.columns[i].header.isEmpty()) {
                label = cfg.columns[i].header;
            }
            
            HorizontalTextAlignEnum colDataAlign = null;
            if (cfg.columns != null && i < cfg.columns.length && cfg.columns[i].align != null) {
                String alignStr = cfg.columns[i].align;
                if ("LEFT".equalsIgnoreCase(alignStr)) colDataAlign = HorizontalTextAlignEnum.LEFT;
                else if ("RIGHT".equalsIgnoreCase(alignStr)) colDataAlign = HorizontalTextAlignEnum.RIGHT;
                else if ("CENTER".equalsIgnoreCase(alignStr)) colDataAlign = HorizontalTextAlignEnum.CENTER;
            }
            
            // === Encabezado en el TITLE band (solo imprime una vez) ===
            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setText(label);
            headerText.setX(x);
            headerText.setY(headerYInTitle);
            headerText.setWidth(cellWidth);
            headerText.setHeight(18);
            headerText.setBold(true);
            headerText.setFontSize(dataFontSize);
            headerText.setForecolor(headerFgColor);
            headerText.setBackcolor(headerBgColor);
            headerText.setMode(ModeEnum.OPAQUE);
            headerText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            headerText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            headerText.getLineBox().getPen().setLineWidth(0.5f);
            headerText.getLineBox().getPen().setLineColor(new java.awt.Color(200, 200, 200));
            titleBand.addElement(headerText);
            
            // === Detalle ===
            JRDesignTextField textField = new JRDesignTextField();
            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("$F{" + name + "}");
            textField.setExpression(expression);
            textField.setX(x);
            textField.setY(0);
            textField.setWidth(cellWidth);
            textField.setHeight(18);
            textField.setFontSize(dataFontSize);
            textField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            textField.setBlankWhenNull(true); // FIX: evitar que muestre "null"
            
            // Alternar color de fila (zebra)
            String colType = columnTypes.get(i);
            if (colDataAlign != null) {
                textField.setHorizontalTextAlign(colDataAlign);
            } else if (colType.contains("Int") || colType.contains("Long") || colType.contains("Integer") || colType.contains("Double") || colType.contains("Float") || colType.contains("Decimal") || colType.contains("BigDecimal")) {
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
            } else if (colType.contains("Date") || colType.contains("Time") || colType.contains("Timestamp")) {
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            } else {
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            }
            
            if (colType.contains("Double") || colType.contains("Float") || colType.contains("Decimal") || colType.contains("BigDecimal")) {
                textField.setPattern("#,##0.00");
            } else if (colType.contains("Int") || colType.contains("Long") || colType.contains("Integer")) {
                textField.setPattern("#,##0");
            } else if (colType.contains("Date") || colType.contains("Time") || colType.contains("Timestamp")) {
                textField.setPattern("dd/MM/yyyy");
            }
            
            textField.getLineBox().getPen().setLineWidth(0.5f);
            textField.getLineBox().getPen().setLineColor(new java.awt.Color(220, 220, 220));
            detailBand.addElement(textField);
        }
        
        jd.setTitle(titleBand);
        // NO se usa jd.setColumnHeader() — así el header NO se repite en cada página
        
        JRDesignSection detailSection = (JRDesignSection) jd.getDetailSection();
        detailSection.addBand(detailBand);
        
        // Footer Band — número de página
        JRDesignBand footerBand = new JRDesignBand();
        footerBand.setHeight(15);
        
        JRDesignTextField footerText = new JRDesignTextField();
        JRDesignExpression footerExpr = new JRDesignExpression();
        footerExpr.setText("\"Página \" + $V{PAGE_NUMBER} + \" de \" + $V{PAGE_COUNT}");
        footerText.setExpression(footerExpr);
        footerText.setX(0);
        footerText.setY(0);
        footerText.setWidth(pageWidth - 40);
        footerText.setHeight(12);
        footerText.setFontSize(8f);
        footerText.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        footerBand.addElement(footerText);
        jd.setPageFooter(footerBand);
        
        return JasperCompileManager.compileReport(jd);
    }
    
    private java.awt.Color parseHexColor(String hex, java.awt.Color defaultColor) {
        if (hex == null || hex.trim().isEmpty()) return defaultColor;
        try {
            hex = hex.trim().replace("#", "");
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new java.awt.Color(r, g, b);
        } catch (Exception e) {
            return defaultColor;
        }
    }
    
    // === DTOs internos para deserializar pdfDesignConfig ===
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class PdfDesignConfig {
        public Boolean landscape;
        public String headerBgColor;
        public String headerFgColor;
        public String titleColor;
        public Float titleFontSize;
        public Float dataFontSize;
        public String titleAlign;
        public PdfColumnConfig[] columns;
        public String logoBase64;  // Base64 del logo
        public Integer logoWidth;  // ancho del logo en puntos
        public Integer logoHeight; // alto del logo en puntos
    }
    
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class PdfColumnConfig {
        public String header;
        public String align;
        public Integer widthPct;
    }

    public String convertJasperParamsToJdbc(String sql, List<String> paramNames) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\$P\\{([a-zA-Z0-9_]+)\\}");
        java.util.regex.Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            paramNames.add(m.group(1));
            m.appendReplacement(sb, "?");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String processSqlQuery(String sql, String supplierNumber) {
        if (sql == null) {
            return "";
        }
        
        sql = sql.replace("{schema}.", "").replace("{schema}", "");
        
        String sNum = (supplierNumber != null) ? supplierNumber : "";
        sNum = sNum.replace("'", "''");
        
        sql = sql.replace("'{addressNumber}'", "'" + sNum + "'");
        sql = sql.replace("\"{addressNumber}\"", "'" + sNum + "'");
        sql = sql.replace("{addressNumber}", "'" + sNum + "'");
        
        sql = sql.replace("'{supplier}'", "'" + sNum + "'");
        sql = sql.replace("\"{supplier}\"", "'" + sNum + "'");
        sql = sql.replace("{supplier}", "'" + sNum + "'");
        
        return sql;
    }

    private Map<String, Object> parseParameters(Report report, Map<String, String> rawParams) throws Exception {
        Map<String, Object> parsed = new HashMap<String, Object>();
        String reportDir = getReportDir();
        parsed.put("OPENREPORTS_REPORT_DIR", reportDir + "/");

        for (ReportParameterMap paramMap : report.getParameters()) {
            ReportParameter param = paramMap.getReportParameter();
            if (param == null) continue;
            String rawVal = rawParams.get(param.getName());

            if ((rawVal == null || rawVal.trim().isEmpty()) && param.getDefaultValue() != null) {
                rawVal = param.getDefaultValue();
            }

            if (paramMap.getRequired() != null && paramMap.getRequired() && (rawVal == null || rawVal.trim().isEmpty())) {
                throw new IllegalArgumentException("El parámetro '" + param.getName() + "' es obligatorio.");
            }

            if (rawVal != null) {
                Object typedVal = convertToType(rawVal, param.getClassName());
                parsed.put(param.getName(), typedVal);
            }
        }

        return parsed;
    }

    public Object convertToType(String val, String className) throws Exception {
        if (val == null) return null;
        
        if ("java.lang.String".equals(className)) {
            return val;
        } else if ("java.lang.Integer".equals(className)) {
            return Integer.valueOf(val);
        } else if ("java.lang.Long".equals(className)) {
            return Long.valueOf(val);
        } else if ("java.lang.Double".equals(className)) {
            return Double.valueOf(val);
        } else if ("java.lang.Boolean".equals(className)) {
            return Boolean.valueOf(val);
        } else if ("java.util.Date".equals(className) || "java.sql.Date".equals(className)) {
            return parseDate(val);
        }
        return val;
    }

    private Date parseDate(String val) throws Exception {
        String[] formats = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", "dd/MM/yyyy HH:mm:ss"};
        for (String format : formats) {
            try {
                return new SimpleDateFormat(format).parse(val);
            } catch (Exception ignored) {
            }
        }
        try {
            return new Date(Long.parseLong(val));
        } catch (Exception ignored) {
        }
        throw new IllegalArgumentException("No se pudo parsear el valor de fecha: " + val);
    }

    private byte[] exportReport(Report report, JasperPrint jasperPrint, String format) throws Exception {
        String fmt = (format != null) ? format.toLowerCase() : "pdf";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if ("xlsx".equals(fmt) || "excel".equals(fmt)) {
            SimpleExporterInput xlsInput = new SimpleExporterInput(jasperPrint);
            SimpleOutputStreamExporterOutput xlsOutput = new SimpleOutputStreamExporterOutput(baos);
            JRXlsxExporter xlsExporter = new JRXlsxExporter();
            xlsExporter.setExporterInput(xlsInput);
            xlsExporter.setExporterOutput(xlsOutput);

            SimpleXlsxReportConfiguration xlsConfig = new SimpleXlsxReportConfiguration();
            if (report.getReportExportOption() != null) {
                ReportExportOption opt = report.getReportExportOption();
                xlsConfig.setRemoveEmptySpaceBetweenRows(opt.isXlsRemoveEmptySpaceBetweenRows());
                xlsConfig.setOnePagePerSheet(opt.isXlsOnePagePerSheet());
                xlsConfig.setDetectCellType(opt.isXlsAutoDetectCellType());
                xlsConfig.setWhitePageBackground(opt.isXlsWhitePageBackground());
            } else {
                xlsConfig.setRemoveEmptySpaceBetweenRows(true);
                xlsConfig.setDetectCellType(true);
                xlsConfig.setWhitePageBackground(false);
            }
            xlsExporter.setConfiguration(xlsConfig);
            xlsExporter.exportReport();
            return baos.toByteArray();

        } else if ("csv".equals(fmt)) {
            SimpleExporterInput csvInput = new SimpleExporterInput(jasperPrint);
            SimpleWriterExporterOutput csvOutput = new SimpleWriterExporterOutput(baos);
            JRCsvExporter csvExporter = new JRCsvExporter();
            csvExporter.setExporterInput(csvInput);
            csvExporter.setExporterOutput(csvOutput);
            csvExporter.exportReport();
            return baos.toByteArray();

        } else if ("html".equals(fmt)) {
            SimpleExporterInput htmlInput = new SimpleExporterInput(jasperPrint);
            SimpleHtmlExporterOutput htmlOutput = new SimpleHtmlExporterOutput(baos);
            HtmlExporter htmlExporter = new HtmlExporter();
            htmlExporter.setExporterInput(htmlInput);
            htmlExporter.setExporterOutput(htmlOutput);
            htmlExporter.exportReport();
            return baos.toByteArray();

        } else {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
}
