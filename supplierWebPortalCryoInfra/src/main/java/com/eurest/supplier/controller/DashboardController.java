package com.eurest.supplier.controller;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.eurest.supplier.dao.ReportDao;
import com.eurest.supplier.dao.UsersDao;
import com.eurest.supplier.model.*;
import com.eurest.supplier.service.ReportService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private UsersDao usersDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    Logger log4j = LogManager.getLogger(DashboardController.class);

    @javax.annotation.PostConstruct
    public void initKpiIconsAndColorColumn() {
        try {
            // 0. Alter table to add PDF_DESIGN_CONFIG column to REPORT if it does not exist
            try {
                jdbcTemplate.execute("SELECT PDF_DESIGN_CONFIG FROM REPORT LIMIT 1");
            } catch (Exception alterEx) {
            	log4j.error("=== AGREGANDO COLUMNA PDF_DESIGN_CONFIG A REPORT ===");
                try {
                    jdbcTemplate.execute("ALTER TABLE REPORT ADD COLUMN PDF_DESIGN_CONFIG TEXT DEFAULT NULL");
                } catch (Exception e) {
                	log4j.error("Error al alterar tabla REPORT: " + e.getMessage());
                }
            }

            // 1. Alter table to add COLOR column to REPORT_KPI if it does not exist
            try {
                jdbcTemplate.execute("SELECT COLOR FROM REPORT_KPI LIMIT 1");
            } catch (Exception alterEx) {
            	log4j.error("=== AGREGANDO COLUMNA COLOR A REPORT_KPI ===");
                try {
                    jdbcTemplate.execute("ALTER TABLE REPORT_KPI ADD COLUMN COLOR VARCHAR(50) DEFAULT '#00306E'");
                } catch (Exception e) {
                	log4j.error("Error al alterar tabla REPORT_KPI: " + e.getMessage());
                }
            }

            // 1.2 Alter table to add COLORS column to REPORT_CHART if it does not exist
            try {
                jdbcTemplate.execute("SELECT COLORS FROM REPORT_CHART LIMIT 1");
            } catch (Exception alterEx) {
            	log4j.error("=== AGREGANDO COLUMNA COLORS A REPORT_CHART ===");
                try {
                    jdbcTemplate.execute("ALTER TABLE REPORT_CHART ADD COLUMN COLORS VARCHAR(500) DEFAULT NULL");
                } catch (Exception e) {
                	log4j.error("Error al alterar tabla REPORT_CHART: " + e.getMessage());
                }
            }

            // 1.3 Expand ROLE column to VARCHAR(500) in all reporteador tables for multi-role support
            String[] roleTablesMultiRole = {"REPORT", "REPORT_KPI", "REPORT_CHART", "REPORT_DASHBOARD_TABLE", "REPORT_DASHBOARD_WIDGET"};
            for (String tbl : roleTablesMultiRole) {
                try {
                    jdbcTemplate.execute("ALTER TABLE " + tbl + " MODIFY COLUMN ROLE VARCHAR(500)");
                } catch (Exception e) {
                    // Column may not exist or already be correct size — safe to ignore
                	log4j.error(e);
                }
            }

            // 1.5 Expand UDCKEY column in udc table to avoid data truncation for long icon names
            try {
                jdbcTemplate.execute("ALTER TABLE udc MODIFY COLUMN UDCKEY VARCHAR(50)");
            } catch (Exception e) {
            	log4j.error("Error al alterar columna UDCKEY en tabla udc: " + e.getMessage());
            }

            // 2. Check and seed UDC KPI_ICONS
            try {
                jdbcTemplate.update("DELETE FROM udc WHERE UDCSYSTEM = 'KPI_ICONS'");
                String insertSql = "INSERT INTO udc (UDCSYSTEM, UDCKEY, STRVALUE1, STRVALUE2, BOOLEANVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-shopping-cart", "fa-shopping-cart", "Carrito de Compras", 1, "Icono Carrito de Compras");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-users", "fa-users", "Usuarios / Proveedores", 1, "Icono Usuarios / Proveedores");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-money", "fa-money", "Dinero / Pagos", 1, "Icono Dinero / Pagos");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-usd", "fa-usd", "Moneda D\u00f3lar", 1, "Icono Moneda D\u00f3lar");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-file-text-o", "fa-file-text-o", "Documento / Texto", 1, "Icono Documento / Texto");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-line-chart", "fa-line-chart", "Gr\u00e1fico de L\u00edneas", 1, "Icono Gr\u00e1fico de L\u00edneas");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-bar-chart", "fa-bar-chart", "Gr\u00e1fico de Barras", 1, "Icono Gr\u00e1fico de Barras");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-pie-chart", "fa-pie-chart", "Gr\u00e1fico de Pastel", 1, "Icono Gr\u00e1fico de Pastel");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-truck", "fa-truck", "Cami\u00f3n / Log\u00edstica", 1, "Icono Cami\u00f3n / Log\u00edstica");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-industry", "fa-industry", "F\u00e1brica / Planta", 1, "Icono F\u00e1brica / Planta");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-lock", "fa-lock", "Candado / Seguridad", 1, "Icono Candado / Seguridad");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-check-circle", "fa-check-circle", "Check / Aprobado", 1, "Icono Check / Aprobado");
                jdbcTemplate.update(insertSql, "KPI_ICONS", "fa-exclamation-triangle", "fa-exclamation-triangle", "Advertencia / Alerta", 1, "Icono Advertencia");
            } catch (Exception e) {
            	log4j.error("Error al sembrar iconos: " + e.getMessage());
            }

            // 3. Clean up and repair corrupted Spanish accents in dashboard tables
            try {
                jdbcTemplate.update("UPDATE REPORT_DASHBOARD_TABLE SET NAME = ?, COLUMN_HEADERS = ? WHERE TABLE_ID = 1",
                        "\u00daltimas \u00d3rdenes de Compra", "N\u00famero, Email Proveedor, Monto, Estado");
                jdbcTemplate.update("UPDATE REPORT_DASHBOARD_TABLE SET NAME = ? WHERE TABLE_ID = 2",
                        "\u00daltimos Documentos Fiscales");
            } catch (Exception e) {
            	log4j.error("Error al corregir acentos en tablas: " + e.getMessage());
            }

            // 4. Update default chart queries to use beautiful aliases (Spanish legends) instead of value_0
            try {
                jdbcTemplate.update("UPDATE REPORT_CHART SET CHART_QUERY = ? WHERE CHART_ID = 1",
                        "SELECT status AS label, COUNT(*) AS 'Cantidad de \u00d3rdenes' FROM purchaseorder WHERE status IS NOT NULL AND TRIM(status) != '' GROUP BY status");
                jdbcTemplate.update("UPDATE REPORT_CHART SET CHART_QUERY = ? WHERE CHART_ID = 2",
                        "SELECT COALESCE(currencyCode, 'MXN') AS label, COUNT(*) AS 'Cantidad de Facturas' FROM fiscaldocuments GROUP BY currencyCode");
            } catch (Exception e) {
            	log4j.error("Error al actualizar consultas de graficos: " + e.getMessage());
            }

            // 5. Seed KPIs and widgets for ROLE_SUPPLIER if they do not exist
            try {
                List<Map<String, Object>> supplierKpis = jdbcTemplate.queryForList(
                    "SELECT KPI_ID FROM REPORT_KPI WHERE ROLE = 'ROLE_SUPPLIER'"
                );
                if (supplierKpis.isEmpty()) {
                    
                    // Insert "Facturas Pendientes"
                    jdbcTemplate.update(
                        "INSERT INTO REPORT_KPI (NAME, DESCRIPTION, SQL_QUERY, KPI_TYPE, ICON_NAME, SUBTEXT_TEMPLATE, SORT_ORDER, IS_ACTIVE, ROLE, COLOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        "Facturas Pendientes",
                        "Facturas pendientes de pago para el proveedor",
                        "SELECT COUNT(*) FROM fiscaldocuments WHERE addressNumber = {addressNumber} AND (paymentStatus IS NULL OR paymentStatus != 'P') AND status != 'CANCELADO' AND status != 'RECHAZADO'",
                        "NUMBER",
                        "fa-file-text-o",
                        "Facturas pendientes de cobro",
                        1,
                        true,
                        "ROLE_SUPPLIER",
                        "#FAAD14"
                    );
                    
                    // Insert "Facturas Pagadas"
                    jdbcTemplate.update(
                        "INSERT INTO REPORT_KPI (NAME, DESCRIPTION, SQL_QUERY, KPI_TYPE, ICON_NAME, SUBTEXT_TEMPLATE, SORT_ORDER, IS_ACTIVE, ROLE, COLOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        "Facturas Pagadas",
                        "Facturas pagadas para el proveedor",
                        "SELECT COUNT(*) FROM fiscaldocuments WHERE addressNumber = {addressNumber} AND paymentStatus = 'P'",
                        "NUMBER",
                        "fa-check-circle",
                        "Facturas pagadas con \u00e9xito",
                        2,
                        true,
                        "ROLE_SUPPLIER",
                        "#52C41A"
                    );
                    
                }

                // Check if widgets for ROLE_SUPPLIER exist in REPORT_DASHBOARD_WIDGET
                List<Map<String, Object>> supplierWidgets = jdbcTemplate.queryForList(
                    "SELECT WIDGET_ID FROM REPORT_DASHBOARD_WIDGET WHERE ROLE = 'ROLE_SUPPLIER'"
                );
                if (supplierWidgets.isEmpty()) {
                    
                    // Fetch KPI ids for ROLE_SUPPLIER
                    List<Map<String, Object>> kpis = jdbcTemplate.queryForList(
                        "SELECT KPI_ID, NAME FROM REPORT_KPI WHERE ROLE = 'ROLE_SUPPLIER' ORDER BY SORT_ORDER"
                    );
                    
                    int sortOrder = 1;
                    for (Map<String, Object> kpi : kpis) {
                        Integer kpiId = (Integer) kpi.get("KPI_ID");
                        jdbcTemplate.update(
                            "INSERT INTO REPORT_DASHBOARD_WIDGET (WIDGET_TYPE, WIDGET_REF_ID, COL_SPAN, SORT_ORDER, ROLE) VALUES (?, ?, ?, ?, ?)",
                            "KPI",
                            kpiId,
                            2, // colSpan = 2 (50% wide)
                            sortOrder++,
                            "ROLE_SUPPLIER"
                        );
                    }
                }
            } catch (Exception e) {
            	log4j.error("Error al sembrar KPIs y widgets para ROLE_SUPPLIER: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log4j.error(e);
        }
    }


    @RequestMapping(value = "/metrics.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getDashboardMetrics(
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) String supplierNumber) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = usersDao.getByUserName(username);
        if (user == null) {
            Map<String, Object> err = new HashMap<String, Object>();
            err.put("success", false);
            err.put("message", "Usuario no encontrado: " + username);
            return err;
        }

        String role = user.getRole();
        if (role == null || role.trim().isEmpty()) {
            return getEmptyDashboardResponse("Usuario sin rol asignado");
        }

        String finalRole = role;
        String finalSupplierNumber = user.getAddressNumber();

        // Control de Seguridad Obligatorio para Administrador
        if ("ROLE_ADMIN".equals(role)) {
            if (tenantId != null && !tenantId.isEmpty()) {
                finalRole = tenantId;
            }
            if (supplierNumber != null) {
                finalSupplierNumber = supplierNumber;
            }
        }

        return queryDashboardData(finalRole, finalSupplierNumber);
    }

    @RequestMapping(value = "/suppliers/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listSuppliersForAdmin() {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Users user = usersDao.getByUserName(username);
            if (user == null || !"ROLE_ADMIN".equals(user.getRole())) {
                res.put("success", false);
                res.put("message", "No autorizado");
                return res;
            }
            
            String sql = "SELECT DISTINCT addressNumber, name FROM users WHERE addressNumber IS NOT NULL AND TRIM(addressNumber) != '' ORDER BY name";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            res.put("success", true);
            res.put("data", list);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error al listar proveedores");
        }
        return res;
    }

    @RequestMapping(value = "/layout/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveDashboardLayout(
            @RequestParam String role,
            java.io.Reader reader) {
        
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            reportDao.deleteDashboardWidgetsByRole(role);
            
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> layoutList = mapper.readValue(reader, List.class);
            
            for (int i = 0; i < layoutList.size(); i++) {
                Map<String, Object> dto = layoutList.get(i);
                ReportDashboardWidget widget = new ReportDashboardWidget();
                widget.setRole(role);
                widget.setWidgetType((String) dto.get("type"));
                widget.setWidgetRefId((Integer) dto.get("refId"));
                widget.setColSpan((Integer) dto.get("colSpan"));
                widget.setSortOrder(i + 1);
                reportDao.saveDashboardWidget(widget);
            }
            
            response.put("success", true);
            response.put("message", "Diseño del Dashboard guardado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage() != null ? e.getMessage() : "Error al guardar el diseño");
        }
        return response;
    }

    private Map<String, Object> queryDashboardData(String role, String supplierNumber) {
        Map<String, Object> response = new HashMap<String, Object>();
        List<Map<String, Object>> widgetResponses = new ArrayList<Map<String, Object>>();

        // A. Cargar los widgets configurados para este rol
        List<ReportDashboardWidget> savedWidgets = reportDao.getDashboardWidgetsByRole(role);
        
        // B. Cargar KPIs, Gráficos y Tablas
        Map<Integer, Map<String, Object>> kpiMap = new HashMap<Integer, Map<String, Object>>();
        try {
            List<ReportKpi> activeKpis = reportDao.getKpisByRole(role);
            for (ReportKpi kpi : activeKpis) {
                try {
                    String sql = processSqlQuery(kpi.getSqlQuery(), supplierNumber);
                    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
                    if (!rows.isEmpty()) {
                        Map<String, Object> firstRow = rows.get(0);
                        Object primaryValObj = firstRow.containsKey("value") ? firstRow.get("value") : (firstRow.isEmpty() ? 0 : firstRow.values().iterator().next());
                        
                        String formattedValue = "0";
                        double numericVal = 0.0;
                        if (primaryValObj instanceof Number) {
                            numericVal = ((Number) primaryValObj).doubleValue();
                        }
                        
                        String kpiType = kpi.getKpiType() != null ? kpi.getKpiType() : "NUMBER";
                        if ("CURRENCY".equalsIgnoreCase(kpiType)) {
                            formattedValue = formatCurrency(numericVal);
                        } else if ("PERCENTAGE".equalsIgnoreCase(kpiType)) {
                            formattedValue = String.format(Locale.US, "%.1f%%", numericVal);
                        } else if ("NUMBER".equalsIgnoreCase(kpiType)) {
                            formattedValue = String.format(Locale.US, "%,.0f", numericVal);
                        } else {
                            formattedValue = String.valueOf(primaryValObj);
                        }
                        
                        String subtext = kpi.getSubtextTemplate();
                        if (subtext != null && !subtext.isEmpty()) {
                            for (Map.Entry<String, Object> entry : firstRow.entrySet()) {
                                String placeholder = "{" + entry.getKey() + "}";
                                String valStr = entry.getValue() != null ? String.valueOf(entry.getValue()) : "0";
                                if (entry.getValue() instanceof Number) {
                                    valStr = String.format(Locale.US, "%,.0f", ((Number) entry.getValue()).doubleValue());
                                }
                                subtext = subtext.replace(placeholder, valStr);
                            }
                            subtext = subtext.replaceAll("\\{[A-Za-z0-9_]+\\}", "0");
                        } else {
                            subtext = "";
                        }
                        
                        Map<String, Object> kpiRes = new HashMap<String, Object>();
                        kpiRes.put("name", kpi.getName());
                        kpiRes.put("value", formattedValue);
                        kpiRes.put("subtext", subtext);
                        kpiRes.put("icon", kpi.getIconName());
                        kpiRes.put("color", kpi.getColor() != null ? kpi.getColor() : "#00306E");
                        
                        kpiMap.put(kpi.getId(), kpiRes);
                    }
                } catch (Exception e) {
                    System.err.println("Error al calcular KPI: " + kpi.getName() + " | " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error general en KPIs: " + e.getMessage());
        }

        // Cargar Gráficos
        Map<Integer, Map<String, Object>> chartMap = new HashMap<Integer, Map<String, Object>>();
        try {
            List<ReportChart> activeCharts = reportDao.getChartsByRole(role);
            for (ReportChart chart : activeCharts) {
                try {
                    String sql = processSqlQuery(chart.getChartQuery(), supplierNumber);
                    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
                    
                    List<String> labels = new ArrayList<String>();
                    List<Map<String, Object>> datasets = new ArrayList<Map<String, Object>>();
                    
                    if (!rows.isEmpty()) {
                        Map<String, Object> firstRow = rows.get(0);
                        List<String> colNames = new ArrayList<String>(firstRow.keySet());
                        
                        if (!colNames.isEmpty()) {
                            String labelCol = colNames.get(0);
                            List<String> numericCols = new ArrayList<String>();
                            
                            for (int i = 1; i < colNames.size(); i++) {
                                String col = colNames.get(i);
                                if (firstRow.get(col) instanceof Number) {
                                    numericCols.add(col);
                                }
                            }
                            
                            Map<String, List<Double>> datasetDataMap = new LinkedHashMap<String, List<Double>>();
                            for (String numCol : numericCols) {
                                datasetDataMap.put(numCol, new ArrayList<Double>());
                            }
                            
                            for (Map<String, Object> row : rows) {
                                labels.add(String.valueOf(row.get(labelCol)));
                                for (String numCol : numericCols) {
                                    Number numVal = (Number) row.get(numCol);
                                    datasetDataMap.get(numCol).add(numVal != null ? numVal.doubleValue() : 0.0);
                                }
                            }
                            
                            for (Map.Entry<String, List<Double>> entry : datasetDataMap.entrySet()) {
                                Map<String, Object> ds = new HashMap<String, Object>();
                                ds.put("label", entry.getKey());
                                ds.put("data", entry.getValue());
                                datasets.add(ds);
                            }
                        }
                    }
                    
                    Map<String, Object> chartRes = new HashMap<String, Object>();
                    chartRes.put("id", chart.getId());
                    chartRes.put("name", chart.getName());
                    chartRes.put("description", chart.getDescription());
                    chartRes.put("chartType", chart.getChartType());
                    chartRes.put("xAxisLabel", chart.getXAxisLabel());
                    chartRes.put("yAxisLabel", chart.getYAxisLabel());
                    chartRes.put("showLegend", chart.isShowLegend());
                    chartRes.put("showTitle", chart.isShowTitle());
                    chartRes.put("colors", chart.getColors());
                    chartRes.put("labels", labels);
                    chartRes.put("datasets", datasets);
                    
                    chartMap.put(chart.getId(), chartRes);
                } catch (Exception e) {
                    System.err.println("Error al calcular grafico: " + chart.getName() + " | " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error general en Graficos: " + e.getMessage());
        }

        // Cargar Tablas
        Map<Integer, Map<String, Object>> tableMap = new HashMap<Integer, Map<String, Object>>();
        try {
            List<ReportDashboardTable> activeTables = reportDao.getDashboardTablesByRole(role);
            for (ReportDashboardTable table : activeTables) {
                try {
                    String sql = processSqlQuery(table.getSqlQuery(), supplierNumber);
                    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
                    
                    String[] headersArray = table.getColumnHeaders().split(",");
                    String[] keysArray = table.getColumnKeys().split(",");
                    String[] formatsArray = table.getColumnFormats().split(",");
                    
                    List<String> headers = new ArrayList<String>();
                    for (String h : headersArray) {
                        headers.add(h.trim());
                    }
                    
                    List<Map<String, String>> rowList = new ArrayList<Map<String, String>>();
                    for (Map<String, Object> row : rows) {
                        Map<String, String> formattedRow = new LinkedHashMap<String, String>();
                        for (int i = 0; i < keysArray.length; i++) {
                            String header = headersArray[i].trim();
                            String key = keysArray[i].trim();
                            String format = i < formatsArray.length ? formatsArray[i].trim() : "TEXT";
                            
                            Object originalVal = row.get(key);
                            if (originalVal == null) {
                                for (String rowKey : row.keySet()) {
                                    if (rowKey.equalsIgnoreCase(key)) {
                                        originalVal = row.get(rowKey);
                                        break;
                                    }
                                }
                            }
                            
                            String formattedVal = "";
                            if (originalVal != null) {
                                if ("CURRENCY".equalsIgnoreCase(format) && originalVal instanceof Number) {
                                    formattedVal = formatCurrency(((Number) originalVal).doubleValue());
                                } else if ("NUMBER".equalsIgnoreCase(format) && originalVal instanceof Number) {
                                    formattedVal = String.format(Locale.US, "%,.0f", ((Number) originalVal).doubleValue());
                                } else if ("PERCENTAGE".equalsIgnoreCase(format) && originalVal instanceof Number) {
                                    formattedVal = String.format(Locale.US, "%.1f%%", ((Number) originalVal).doubleValue());
                                } else {
                                    formattedVal = String.valueOf(originalVal);
                                }
                            }
                            formattedRow.put(key, formattedVal);
                        }
                        rowList.add(formattedRow);
                    }
                    
                    List<String> keys = new ArrayList<String>();
                    for (String k : keysArray) {
                        keys.add(k.trim());
                    }

                    Map<String, Object> tableRes = new HashMap<String, Object>();
                    tableRes.put("id", table.getId());
                    tableRes.put("name", table.getName());
                    tableRes.put("description", table.getDescription());
                    tableRes.put("headers", headers);
                    tableRes.put("keys", keys);
                    tableRes.put("rows", rowList);
                    
                    tableMap.put(table.getId(), tableRes);
                } catch (Exception e) {
                    System.err.println("Error al calcular tabla: " + table.getName() + " | " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error general en Tablas: " + e.getMessage());
        }

        // C. Formatear la lista de widgets del Layout
        for (ReportDashboardWidget w : savedWidgets) {
            Map<String, Object> wRes = new HashMap<String, Object>();
            wRes.put("id", w.getId());
            wRes.put("type", w.getWidgetType());
            wRes.put("refId", w.getWidgetRefId());
            wRes.put("colSpan", w.getColSpan());
            wRes.put("sortOrder", w.getSortOrder());
            
            if ("KPI".equalsIgnoreCase(w.getWidgetType())) {
                wRes.put("kpi", kpiMap.get(w.getWidgetRefId()));
            } else if ("CHART".equalsIgnoreCase(w.getWidgetType())) {
                wRes.put("chart", chartMap.get(w.getWidgetRefId()));
            } else if ("TABLE".equalsIgnoreCase(w.getWidgetType())) {
                wRes.put("table", tableMap.get(w.getWidgetRefId()));
            }
            widgetResponses.add(wRes);
        }

        response.put("id", role);
        response.put("nombre", "Dashboard de " + role);
        response.put("widgets", widgetResponses);
        return response;
    }

    private Map<String, Object> getEmptyDashboardResponse(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("id", "empty");
        response.put("nombre", message);
        response.put("widgets", new ArrayList<Map<String, Object>>());
        return response;
    }

    private String formatCurrency(double amount) {
        return String.format(Locale.US, "$%,.2f", amount);
    }

    private String processSqlQuery(String sql, String supplierNumber) {
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
}
