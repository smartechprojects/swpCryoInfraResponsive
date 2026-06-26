package com.eurest.supplier.controller;

import com.eurest.supplier.dao.ReportDao;
import com.eurest.supplier.dao.UsersDao;
import com.eurest.supplier.dto.ReportParameterValue;
import com.eurest.supplier.model.Report;
import com.eurest.supplier.model.ReportGroup;
import com.eurest.supplier.model.ReportLog;
import com.eurest.supplier.model.ReportParameterMap;
import com.eurest.supplier.model.ReportDataSource;
import com.eurest.supplier.model.ReportParameter;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.JasperReportService;
import com.eurest.supplier.service.ReportParameterService;
import com.eurest.supplier.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/reports")
public class UserReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private ReportParameterService reportParameterService;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private DataSource dataSource;

    private String getReportDir() {
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            return fullPath + "jasperReports";
        } catch (Exception e) {
            throw new RuntimeException("Error resolviendo el directorio de reportes", e);
        }
    }

    @RequestMapping(value = "/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getMyReports() {
        Map<String, Object> res = new HashMap<String, Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        try {
            List<Report> list = reportService.getReportsForUser(username);
            res.put("success", true);
            res.put("data", list);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/parameters.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getReportParameters(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        Report report = reportService.getReportById(id);
        if (report != null) {
            res.put("success", true);
            res.put("data", report.getParameters());
        } else {
            res.put("success", false);
            res.put("message", "Reporte no encontrado");
        }
        return res;
    }

    @RequestMapping(value = "/parameterValues.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getParameterValues(@RequestParam Integer paramId) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            List<ReportParameterValue> values = reportParameterService.getParameterValues(paramId);
            res.put("success", true);
            res.put("data", values);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/groups.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getMyReportGroups() {
        Map<String, Object> res = new HashMap<String, Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        try {
            List<ReportGroup> list = reportService.getReportGroupsForUser(username);
            res.put("success", true);
            res.put("data", list);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/run.action", method = {RequestMethod.POST, RequestMethod.GET})
    public void runReport(
            @RequestParam Integer id,
            @RequestParam(required = false, defaultValue = "pdf") String format,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Date startTime = new Date();
        String status = "success";
        String message = "Reporte generado en formato: " + format;

        Report report = reportService.getReportById(id);
        if (report == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 1. Validar autorización de acceso al reporte
        List<Report> myReports = reportService.getReportsForUser(username);
        boolean authorized = false;
        for (Report r : myReports) {
            if (r.getId().equals(id)) {
                authorized = true;
                break;
            }
        }

        if (!authorized) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // 2. Extraer parámetros crudos del request HTTP
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }

        // Inyectar parámetros del usuario logueado por seguridad y consistencia
        try {
            Users loggedInUser = usersDao.getByUserName(username);
            if (loggedInUser != null) {
                String userRole = loggedInUser.getRole() != null ? loggedInUser.getRole() : "";
                // Para el proveedor, forzar su propio número por seguridad
                if ("ROLE_SUPPLIER".equals(userRole)) {
                    String addrNum = loggedInUser.getAddressNumber() != null ? loggedInUser.getAddressNumber() : "";
                    params.put("addressNumber", addrNum);
                    params.put("supplier", addrNum);
                } 
                // Para administradores u otros roles, inyectar el valor por defecto sólo si no lo enviaron en el request
                else {
                    if (!params.containsKey("addressNumber") || params.get("addressNumber") == null || params.get("addressNumber").trim().isEmpty()) {
                        String addrNum = loggedInUser.getAddressNumber() != null ? loggedInUser.getAddressNumber() : "";
                        params.put("addressNumber", addrNum);
                    }
                    if (!params.containsKey("supplier") || params.get("supplier") == null || params.get("supplier").trim().isEmpty()) {
                        String addrNum = loggedInUser.getAddressNumber() != null ? loggedInUser.getAddressNumber() : "";
                        params.put("supplier", addrNum);
                    }
                }
                params.put("role", userRole);
            }
        } catch (Exception ex) {
            System.err.println("Error al inyectar parámetros de sesión: " + ex.getMessage());
        }

        try {
            byte[] reportBytes = jasperReportService.runReport(report, format, params);
            
            String cleanName = report.getName().replaceAll("[^a-zA-Z0-9.-]", "_");
            String formatExt = format.toLowerCase();
            String contentType = "application/pdf";
            String filename = cleanName + ".pdf";

            if ("xlsx".equals(formatExt) || "excel".equals(formatExt)) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                filename = cleanName + ".xlsx";
            } else if ("csv".equals(formatExt)) {
                contentType = "text/csv";
                filename = cleanName + ".csv";
            } else if ("html".equals(formatExt)) {
                contentType = "text/html";
                filename = cleanName + ".html";
            }

            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setContentLength(reportBytes.length);

            OutputStream out = response.getOutputStream();
            out.write(reportBytes);
            out.flush();

        } catch (Exception e) {
            status = "failure";
            message = "Error al ejecutar: " + e.getMessage();
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error al ejecutar el reporte: " + e.getMessage());
            } catch (Exception ignored) {}
        } finally {
            Date endTime = new Date();
            try {
                Users user = usersDao.getByUserName(username);
                ReportLog log = new ReportLog();
                log.setStartTime(startTime);
                log.setEndTime(endTime);
                log.setStatus(status);
                log.setMessage(message);
                log.setReport(report);
                log.setUser(user);
                reportDao.saveReportLog(log);
            } catch (Exception logEx) {
                logEx.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/scheduler/downloads.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getScheduledDownloads() {
        Map<String, Object> res = new HashMap<String, Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = usersDao.getByUserName(username);
        boolean isAdmin = user != null && "ROLE_ADMIN".equals(user.getRole());

        List<Report> myReports = reportService.getReportsForUser(username);
        List<Map<String, Object>> downloads = new ArrayList<Map<String, Object>>();

        try {
            String reportDir = getReportDir();
            String genDirPath = reportDir + File.separator + "generated";
            File genDir = new File(genDirPath);
            
            if (genDir.exists()) {
                File[] files = genDir.listFiles(new java.io.FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".json");
                    }
                });
                
                if (files != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    for (File file : files) {
                        try {
                            Map<String, Object> metadata = mapper.readValue(file, Map.class);
                            Integer rId = (Integer) metadata.get("reportId");
                            
                            boolean hasAccess = isAdmin;
                            if (!hasAccess) {
                                for (Report r : myReports) {
                                    if (r.getId().equals(rId)) {
                                        hasAccess = true;
                                        break;
                                    }
                                }
                            }
                                    
                            if (hasAccess) {
                                downloads.add(metadata);
                            }
                        } catch (Exception ignored) {}
                    }
                }
            }

            downloads.sort(new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> a, Map<String, Object> b) {
                    String idA = (String) a.get("id");
                    String idB = (String) b.get("id");
                    if (idA == null || idB == null) return 0;
                    return idB.compareTo(idA); // Descendiente
                }
            });

            res.put("success", true);
            res.put("data", downloads);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/scheduler/downloadFile.action", method = RequestMethod.GET)
    public void downloadScheduledFile(
            @RequestParam String fileName,
            HttpServletResponse response) {
            
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = usersDao.getByUserName(username);
        boolean isAdmin = user != null && "ROLE_ADMIN".equals(user.getRole());

        List<Report> myReports = reportService.getReportsForUser(username);

        try {
            String reportDir = getReportDir();
            String genDirPath = reportDir + File.separator + "generated";
            
            // Buscar archivo meta .json para validar autorización
            File metaFile = new File(genDirPath, fileName + ".json");
            if (!metaFile.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> metadata = mapper.readValue(metaFile, Map.class);
            Integer rId = (Integer) metadata.get("reportId");
            String actualFileName = (String) metadata.get("fileName");
            
            boolean hasAccess = isAdmin;
            if (!hasAccess) {
                for (Report r : myReports) {
                    if (r.getId().equals(rId)) {
                        hasAccess = true;
                        break;
                    }
                }
            }

            if (!hasAccess) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            File downloadFile = new File(genDirPath, actualFileName);
            if (!downloadFile.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String contentType = "application/octet-stream";
            if (actualFileName.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (actualFileName.endsWith(".xlsx")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if (actualFileName.endsWith(".csv")) {
                contentType = "text/csv";
            }

            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + actualFileName + "\"");
            response.setContentLength((int) downloadFile.length());

            FileInputStream in = new FileInputStream(downloadFile);
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/preview.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> previewReport(
            @RequestParam Integer id,
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "25") int limit,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<String, Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            Report report = reportService.getReportById(id);
            if (report == null) {
                response.put("success", false);
                response.put("error", "Reporte no encontrado");
                return response;
            }

            // 1. Validar autorización de acceso al reporte
            List<Report> myReports = reportService.getReportsForUser(username);
            boolean authorized = false;
            for (Report r : myReports) {
                if (r.getId().equals(id)) {
                    authorized = true;
                    break;
                }
            }

            if (!authorized) {
                response.put("success", false);
                response.put("error", "No autorizado para ver este reporte");
                return response;
            }

            // 2. Extraer parámetros crudos del request HTTP
            Map<String, String> params = new HashMap<String, String>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                params.put(paramName, request.getParameter(paramName));
            }

            // Inyectar parámetros del usuario logueado por seguridad y consistencia
            Users loggedInUser = usersDao.getByUserName(username);
            if (loggedInUser != null) {
                String userRole = loggedInUser.getRole() != null ? loggedInUser.getRole() : "";
                if ("ROLE_SUPPLIER".equals(userRole)) {
                    String addrNum = loggedInUser.getAddressNumber() != null ? loggedInUser.getAddressNumber() : "";
                    params.put("addressNumber", addrNum);
                    params.put("supplier", addrNum);
                } else {
                    if (!params.containsKey("addressNumber") || params.get("addressNumber") == null || params.get("addressNumber").trim().isEmpty()) {
                        String addrNum = loggedInUser.getAddressNumber() != null ? loggedInUser.getAddressNumber() : "";
                        params.put("addressNumber", addrNum);
                    }
                    if (!params.containsKey("supplier") || params.get("supplier") == null || params.get("supplier").trim().isEmpty()) {
                        String addrNum = loggedInUser.getAddressNumber() != null ? loggedInUser.getAddressNumber() : "";
                        params.put("supplier", addrNum);
                    }
                }
                params.put("role", userRole);
            }

            // 3. Obtener query SQL
            String sql = report.getQuery();
            if (sql == null || sql.trim().isEmpty()) {
                // Intentar extraer la consulta desde el archivo JRXML si existe
                if (report.getFile() != null) {
                    String jrxmlName = report.getFile().replace(".jasper", ".jrxml");
                    File jrxmlFile = new File(getReportDir(), jrxmlName);
                    if (jrxmlFile.exists()) {
                        try {
                            String content = new String(java.nio.file.Files.readAllBytes(jrxmlFile.toPath()), "UTF-8");
                            java.util.regex.Pattern p = java.util.regex.Pattern.compile("<queryString[^>]*>\\s*<!\\[CDATA\\[(.*?)\\]\\]>\\s*</queryString>", java.util.regex.Pattern.DOTALL);
                            java.util.regex.Matcher m = p.matcher(content);
                            if (m.find()) {
                                sql = m.group(1).trim();
                            }
                        } catch (Exception e) {
                            System.err.println("Error al extraer queryString de JRXML para preview: " + e.getMessage());
                        }
                    }
                }
            }

            if (sql == null || sql.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "El reporte no posee una consulta SQL definida.");
                return response;
            }

            // 4. Procesar y sustituir comodines de sesión
            String sNum = params.get("addressNumber");
            sql = jasperReportService.processSqlQuery(sql, sNum);

            // Reemplazar rol si es necesario
            if (loggedInUser != null && loggedInUser.getRole() != null) {
                sql = sql.replace("{role}", "'" + loggedInUser.getRole().replace("'", "''") + "'");
            }

            // 5. Convertir parámetros tipo Jasper ($P{...}) a JDBC ?
            List<String> paramNamesInQuery = new ArrayList<String>();
            String jdbcSql = jasperReportService.convertJasperParamsToJdbc(sql, paramNamesInQuery);
            jdbcSql = jdbcSql.trim();
            if (jdbcSql.endsWith(";")) {
                jdbcSql = jdbcSql.substring(0, jdbcSql.length() - 1).trim();
            }

            // 6. Obtener conexión y ejecutar
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
                            if (ctx != null) ctx.close();
                        }
                    } else {
                        Class.forName(ds.getDriverClassName());
                        conn = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
                    }
                    isExternalConn = true;
                } else {
                    conn = dataSource.getConnection();
                }

                // 7. Preparar valores de parámetros ordenados
                List<Object> paramValues = new ArrayList<Object>();
                for (String paramName : paramNamesInQuery) {
                    String val = params.get(paramName);
                    Object typedVal = null;
                    for (ReportParameterMap pMap : report.getParameters()) {
                        ReportParameter rp = pMap.getReportParameter();
                        if (rp != null && rp.getName().equals(paramName)) {
                            typedVal = jasperReportService.convertToType(val, rp.getClassName());
                            break;
                        }
                    }
                    if (typedVal == null && val != null) {
                        typedVal = val;
                    }
                    paramValues.add(typedVal);
                }

                // 8. Consulta de Conteo (Total)
                long totalCount = 0;
                String countSql = "SELECT COUNT(*) FROM (" + jdbcSql + ") _count_tbl";
                try (PreparedStatement countPs = conn.prepareStatement(countSql)) {
                    int idx = 1;
                    for (Object pVal : paramValues) {
                        countPs.setObject(idx++, pVal);
                    }
                    try (ResultSet countRs = countPs.executeQuery()) {
                        if (countRs.next()) {
                            totalCount = countRs.getLong(1);
                        }
                    }
                }

                // 9. Consulta de Datos Paginada y Ordenada
                String sortCol = null;
                String sortDir = "ASC";
                if (request.getParameter("sort") != null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<Map<String, Object>> sortList = mapper.readValue(request.getParameter("sort"), List.class);
                        if (sortList != null && !sortList.isEmpty()) {
                            sortCol = (String) sortList.get(0).get("property");
                            sortDir = (String) sortList.get(0).get("direction");
                            if (sortDir == null) sortDir = "ASC";
                        }
                    } catch (Exception e) {
                        System.err.println("Error al parsear sort params: " + e.getMessage());
                    }
                }

                String dataSql = jdbcSql;
                if (sortCol != null && !sortCol.trim().isEmpty() && sortCol.matches("^[a-zA-Z0-9_]+$")) {
                    dataSql = "SELECT * FROM (" + dataSql + ") _sort_tbl ORDER BY " + sortCol + " " + (sortDir.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
                }
                dataSql = dataSql + " LIMIT " + start + ", " + limit;

                List<Map<String, Object>> rowsList = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> columnsMeta = new ArrayList<Map<String, Object>>();
                List<String> fieldsList = new ArrayList<String>();

                try (PreparedStatement dataPs = conn.prepareStatement(dataSql)) {
                    int idx = 1;
                    for (Object pVal : paramValues) {
                        dataPs.setObject(idx++, pVal);
                    }
                    try (ResultSet rs = dataPs.executeQuery()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int colCount = rsmd.getColumnCount();

                        for (int i = 1; i <= colCount; i++) {
                            String colLabel = rsmd.getColumnLabel(i);
                            int colType = rsmd.getColumnType(i);

                            Map<String, Object> colMeta = new HashMap<String, Object>();
                            colMeta.put("text", colLabel);
                            colMeta.put("dataIndex", colLabel);
                            fieldsList.add(colLabel);

                            // Predecir el tipo de dato
                            String formatType = "TEXT";
                            if (isIdentifierColumn(colLabel)) {
                                formatType = "TEXT";
                            } else if (colType == java.sql.Types.DECIMAL || colType == java.sql.Types.DOUBLE || colType == java.sql.Types.REAL || colType == java.sql.Types.FLOAT) {
                                String upperLabel = colLabel.toUpperCase();
                                if (upperLabel.contains("MONTO") || upperLabel.contains("TOTAL") || upperLabel.contains("IMPORTE") || upperLabel.contains("PAGO") || upperLabel.contains("IVA") || upperLabel.contains("SUBTOTAL")) {
                                    formatType = "CURRENCY";
                                } else {
                                    formatType = "NUMBER";
                                }
                            } else if (colType == java.sql.Types.INTEGER || colType == java.sql.Types.BIGINT || colType == java.sql.Types.SMALLINT || colType == java.sql.Types.TINYINT) {
                                formatType = "NUMBER";
                            }
                            colMeta.put("type", formatType);
                            columnsMeta.add(colMeta);
                        }

                        while (rs.next()) {
                            Map<String, Object> row = new LinkedHashMap<String, Object>();
                            for (int i = 1; i <= colCount; i++) {
                                String colLabel = rsmd.getColumnLabel(i);
                                Object val = rs.getObject(i);
                                if (val instanceof java.util.Date) {
                                    row.put(colLabel, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((java.util.Date) val));
                                } else {
                                    row.put(colLabel, val);
                                }
                            }
                            rowsList.add(row);
                        }
                    }
                }

                response.put("success", true);
                response.put("columns", columnsMeta);
                response.put("fields", fieldsList);
                response.put("data", rowsList);
                response.put("total", totalCount);

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage() != null ? e.getMessage() : "Error interno al previsualizar el reporte.");
        }

        return response;
    }

    private boolean isIdentifierColumn(String colLabel) {
        if (colLabel == null) return false;
        String upperLabel = colLabel.toUpperCase();
        if (upperLabel.contains("NUMBER") || upperLabel.contains("NUMERO") || upperLabel.contains("NUMER") || 
            upperLabel.equals("NUM") || upperLabel.endsWith("_NUM") || upperLabel.startsWith("NUM_") ||
            upperLabel.equals("ID") || upperLabel.endsWith("ID") || upperLabel.contains("_ID") || upperLabel.contains("ID_") ||
            upperLabel.contains("FOLIO") || upperLabel.contains("CODE") || upperLabel.contains("CODIGO") || upperLabel.contains("CÓDIGO") ||
            upperLabel.equals("YEAR") || upperLabel.equals("ANIO") || upperLabel.equals("AÑO") || upperLabel.endsWith("_YEAR") || upperLabel.endsWith("_ANIO") || upperLabel.endsWith("_AÑO") ||
            upperLabel.equals("MES") || upperLabel.equals("MONTH") || upperLabel.endsWith("_MES") || upperLabel.endsWith("_MONTH") ||
            upperLabel.contains("TELEFONO") || upperLabel.contains("TELEFÓN") || upperLabel.contains("PHONE") ||
            upperLabel.equals("ZIP") || upperLabel.contains("POSTAL")) {
            return true;
        }
        return false;
    }
}
