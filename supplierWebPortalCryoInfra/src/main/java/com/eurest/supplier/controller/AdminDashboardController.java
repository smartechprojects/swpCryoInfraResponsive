package com.eurest.supplier.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.eurest.supplier.dao.ReportDao;
import com.eurest.supplier.model.*;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private com.eurest.supplier.dao.UsersDao usersDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // --- GROUPS ENDPOINTS ---
    @RequestMapping(value = "/groups/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listGroups() {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        res.put("data", reportDao.getAllReportGroups());
        return res;
    }

    @RequestMapping(value = "/groups/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveGroup(java.io.Reader reader) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ReportGroup details = mapper.readValue(reader, ReportGroup.class);
            ReportGroup groupToSave;
            if (details.getId() != null) {
                ReportGroup existing = reportDao.getReportGroupById(details.getId());
                if (existing == null) {
                    res.put("success", false);
                    res.put("message", "Grupo no encontrado");
                    return res;
                }
                existing.setName(details.getName());
                existing.setDescription(details.getDescription());
                existing.setDatabaseSchema(details.getDatabaseSchema());
                
                if (details.getReports() != null) {
                    existing.getReports().clear();
                    // Volver a vincular los reportes desde la BD si vienen sólo con ID
                    for (Report r : details.getReports()) {
                        if (r.getId() != null) {
                            Report dbReport = reportDao.getReportById(r.getId());
                            if (dbReport != null) {
                                existing.getReports().add(dbReport);
                            }
                        }
                    }
                }
                groupToSave = existing;
            } else {
                groupToSave = details;
                // Vincular reportes para nuevo grupo
                if (groupToSave.getReports() != null) {
                    List<Report> linked = new ArrayList<Report>();
                    for (Report r : groupToSave.getReports()) {
                        if (r.getId() != null) {
                            Report dbReport = reportDao.getReportById(r.getId());
                            if (dbReport != null) {
                                linked.add(dbReport);
                            }
                        }
                    }
                    groupToSave.setReports(linked);
                }
            }
            ReportGroup saved = reportDao.saveReportGroup(groupToSave);
            res.put("success", true);
            res.put("data", saved);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/groups/delete.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteGroup(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ReportGroup rg = reportDao.getReportGroupById(id);
            if (rg != null) {
                reportDao.deleteReportGroup(rg);
                res.put("success", true);
                res.put("message", "Grupo eliminado exitosamente");
            } else {
                res.put("success", false);
                res.put("message", "Grupo no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    // --- DATASOURCES ENDPOINTS ---
    @RequestMapping(value = "/datasources/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listDataSources() {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        res.put("data", reportDao.getAllReportDataSources());
        return res;
    }

    @RequestMapping(value = "/datasources/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveDataSource(java.io.Reader reader) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ReportDataSource details = mapper.readValue(reader, ReportDataSource.class);
            ReportDataSource dsToSave;
            if (details.getId() != null) {
                ReportDataSource existing = reportDao.getReportDataSourceById(details.getId());
                if (existing == null) {
                    res.put("success", false);
                    res.put("message", "DataSource no encontrado");
                    return res;
                }
                existing.setName(details.getName());
                existing.setJndi(details.getJndi());
                existing.setUrl(details.getUrl());
                existing.setDriverClassName(details.getDriverClassName());
                existing.setUsername(details.getUsername());
                existing.setPassword(details.getPassword());
                dsToSave = existing;
            } else {
                dsToSave = details;
            }
            ReportDataSource saved = reportDao.saveReportDataSource(dsToSave);
            res.put("success", true);
            res.put("data", saved);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/datasources/delete.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteDataSource(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ReportDataSource ds = reportDao.getReportDataSourceById(id);
            if (ds != null) {
                reportDao.deleteReportDataSource(ds);
                res.put("success", true);
                res.put("message", "DataSource eliminado exitosamente");
            } else {
                res.put("success", false);
                res.put("message", "DataSource no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    // --- KPIS ENDPOINTS ---
    @RequestMapping(value = "/kpis/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listKpis() {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        res.put("data", reportDao.getAllKpis());
        return res;
    }

    @RequestMapping(value = "/kpis/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveKpi(java.io.Reader reader) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ReportKpi details = mapper.readValue(reader, ReportKpi.class);
            ReportKpi kpiToSave;
            if (details.getId() != null) {
                ReportKpi existing = reportDao.getKpiById(details.getId());
                if (existing == null) {
                    res.put("success", false);
                    res.put("message", "KPI no encontrado");
                    return res;
                }
                existing.setName(details.getName());
                existing.setDescription(details.getDescription());
                existing.setSqlQuery(details.getSqlQuery());
                existing.setKpiType(details.getKpiType());
                existing.setIconName(details.getIconName());
                existing.setSubtextTemplate(details.getSubtextTemplate());
                existing.setSortOrder(details.getSortOrder());
                existing.setIsActive(details.getIsActive());
                existing.setRole(details.getRole());
                existing.setColor(details.getColor());
                existing.setTextColor(details.getTextColor());
                kpiToSave = existing;
            } else {
                kpiToSave = details;
            }
            ReportKpi saved = reportDao.saveKpi(kpiToSave);
            res.put("success", true);
            res.put("data", saved);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/kpis/delete.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteKpi(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ReportKpi kpi = reportDao.getKpiById(id);
            if (kpi != null) {
                reportDao.deleteKpi(kpi);
                res.put("success", true);
                res.put("message", "KPI eliminado exitosamente");
            } else {
                res.put("success", false);
                res.put("message", "KPI no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/kpis/preview.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> previewKpi(@RequestParam(required = false) Integer groupId, @RequestParam String sqlQuery, @RequestParam String kpiType, @RequestParam(required = false) String subtextTemplate) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            String sql = processSqlQuery(sqlQuery);
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            if (rows.isEmpty()) {
                res.put("success", true);
                res.put("value", "Sin datos");
                res.put("subtext", "La consulta no devolvió filas");
                return res;
            }

            Map<String, Object> firstRow = rows.get(0);
            Object primaryValObj = firstRow.containsKey("value") ? firstRow.get("value") : (firstRow.isEmpty() ? 0 : firstRow.values().iterator().next());
            double numericValue = 0.0;
            if (primaryValObj instanceof Number) {
                numericValue = ((Number) primaryValObj).doubleValue();
            }

            String formattedValue = "";
            if ("CURRENCY".equalsIgnoreCase(kpiType)) {
                formattedValue = String.format(Locale.US, "$%,.2f", numericValue);
            } else if ("PERCENTAGE".equalsIgnoreCase(kpiType)) {
                formattedValue = String.format(Locale.US, "%.1f%%", numericValue);
            } else if ("NUMBER".equalsIgnoreCase(kpiType)) {
                formattedValue = String.format(Locale.US, "%,.0f", numericValue);
            } else {
                formattedValue = primaryValObj != null ? String.valueOf(primaryValObj) : "";
            }

            String subtext = subtextTemplate;
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

            res.put("success", true);
            res.put("value", formattedValue);
            res.put("subtext", subtext);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error en consulta SQL");
        }
        return res;
    }

    @RequestMapping(value = "/charts/preview.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> previewChart(
            @RequestParam String chartQuery,
            @RequestParam String chartTypeStr,
            @RequestParam(required = false) String xAxisLabel,
            @RequestParam(required = false) String yAxisLabel,
            @RequestParam(required = false) Boolean showLegend,
            @RequestParam(required = false) Boolean showTitle,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String description) {
        
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            String sql = processSqlQuery(chartQuery);
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
            
            Map<String, Object> chartData = new HashMap<String, Object>();
            chartData.put("name", "Vista Previa de Gráfico");
            chartData.put("description", description != null ? description : "");
            chartData.put("chartType", chartTypeStr);
            chartData.put("xAxisLabel", xAxisLabel != null ? xAxisLabel : "");
            chartData.put("yAxisLabel", yAxisLabel != null ? yAxisLabel : "");
            chartData.put("showLegend", showLegend != null ? showLegend : true);
            chartData.put("showTitle", showTitle != null ? showTitle : true);
            chartData.put("colors", colors != null ? colors : "");
            chartData.put("labels", labels);
            chartData.put("datasets", datasets);
            
            res.put("success", true);
            res.put("data", chartData);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error en consulta SQL");
        }
        return res;
    }

    @RequestMapping(value = "/tables/preview.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> previewTable(
            @RequestParam String sqlQuery,
            @RequestParam String columnHeaders,
            @RequestParam String columnKeys,
            @RequestParam String columnFormats,
            @RequestParam(required = false) String description) {
        
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            String sql = processSqlQuery(sqlQuery);
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
            
            String[] headersArray = columnHeaders.split(",");
            String[] keysArray = columnKeys.split(",");
            String[] formatsArray = columnFormats.split(",");
            
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
                            formattedVal = String.format(Locale.US, "$%,.2f", ((Number) originalVal).doubleValue());
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

            Map<String, Object> tableData = new HashMap<String, Object>();
            tableData.put("name", "Vista Previa de Tabla");
            tableData.put("description", description != null ? description : "");
            tableData.put("headers", headers);
            tableData.put("keys", keys);
            tableData.put("rows", rowList);
            
            res.put("success", true);
            res.put("data", tableData);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error en consulta SQL");
        }
        return res;
    }

    // --- CHARTS ENDPOINTS ---
    @RequestMapping(value = "/charts/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listCharts() {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        res.put("data", reportDao.getAllReportCharts());
        return res;
    }

    @RequestMapping(value = "/charts/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveChart(java.io.Reader reader) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ReportChart details = mapper.readValue(reader, ReportChart.class);
            ReportChart chartToSave;
            if (details.getId() != null) {
                ReportChart existing = reportDao.getReportChartById(details.getId());
                if (existing == null) {
                    res.put("success", false);
                    res.put("message", "Gráfico no encontrado");
                    return res;
                }
                existing.setName(details.getName());
                existing.setDescription(details.getDescription());
                existing.setChartQuery(details.getChartQuery());
                existing.setChartType(details.getChartType());
                existing.setXAxisLabel(details.getXAxisLabel());
                existing.setYAxisLabel(details.getYAxisLabel());
                existing.setShowLegend(details.isShowLegend());
                existing.setShowTitle(details.isShowTitle());
                existing.setRole(details.getRole());
                existing.setColors(details.getColors());
                chartToSave = existing;
            } else {
                chartToSave = details;
            }
            ReportChart saved = reportDao.saveReportChart(chartToSave);
            res.put("success", true);
            res.put("data", saved);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/charts/delete.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteChart(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ReportChart chart = reportDao.getReportChartById(id);
            if (chart != null) {
                reportDao.deleteReportChart(chart);
                res.put("success", true);
                res.put("message", "Gráfico eliminado exitosamente");
            } else {
                res.put("success", false);
                res.put("message", "Gráfico no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    // --- TABLES ENDPOINTS ---
    @RequestMapping(value = "/tables/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listTables() {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        res.put("data", reportDao.getAllDashboardTables());
        return res;
    }

    @RequestMapping(value = "/tables/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveTable(java.io.Reader reader) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ReportDashboardTable details = mapper.readValue(reader, ReportDashboardTable.class);
            ReportDashboardTable tableToSave;
            if (details.getId() != null) {
                ReportDashboardTable existing = (ReportDashboardTable) reportDao.getAllDashboardTables().stream()
                        .filter(t -> t.getId().equals(details.getId())).findFirst().orElse(null);
                if (existing == null) {
                    res.put("success", false);
                    res.put("message", "Tabla no encontrada");
                    return res;
                }
                existing.setName(details.getName());
                existing.setDescription(details.getDescription());
                existing.setSqlQuery(details.getSqlQuery());
                existing.setColumnHeaders(details.getColumnHeaders());
                existing.setColumnKeys(details.getColumnKeys());
                existing.setColumnFormats(details.getColumnFormats());
                existing.setSortOrder(details.getSortOrder());
                existing.setIsActive(details.getIsActive());
                existing.setRole(details.getRole());
                tableToSave = existing;
            } else {
                tableToSave = details;
            }
            ReportDashboardTable saved = reportDao.saveDashboardTable(tableToSave);
            res.put("success", true);
            res.put("data", saved);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/tables/analyze.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> analyzeTableQuery(@RequestParam String sqlQuery) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            final String sql = processSqlQuery(sqlQuery);
            List<Map<String, Object>> columns = jdbcTemplate.execute(new org.springframework.jdbc.core.ConnectionCallback<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> doInConnection(java.sql.Connection conn) throws java.sql.SQLException, org.springframework.dao.DataAccessException {
                    List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
                    try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setMaxRows(1);
                        try (java.sql.ResultSet rs = ps.executeQuery()) {
                            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
                            int cc = rsmd.getColumnCount();
                            for (int i = 1; i <= cc; i++) {
                                Map<String, Object> col = new HashMap<String, Object>();
                                String name = rsmd.getColumnLabel(i);
                                if (name == null || name.isEmpty()) {
                                    name = rsmd.getColumnName(i);
                                }
                                col.put("key", name);
                                
                                String className = rsmd.getColumnClassName(i);
                                String type = "TEXT";
                                if (className != null && (className.contains("Integer") || className.contains("Long") || className.contains("Double") || className.contains("Float") || className.contains("BigDecimal") || className.contains("Number") || className.contains("Short"))) {
                                    type = "NUMBER";
                                }
                                col.put("type", type);
                                cols.add(col);
                            }
                        }
                    }
                    return cols;
                }
            });
            res.put("success", true);
            res.put("columns", columns);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error al analizar consulta");
        }
        return res;
    }

    @RequestMapping(value = "/tables/delete.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteTable(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ReportDashboardTable table = (ReportDashboardTable) reportDao.getAllDashboardTables().stream()
                    .filter(t -> t.getId().equals(id)).findFirst().orElse(null);
            if (table != null) {
                reportDao.deleteDashboardTable(table);
                res.put("success", true);
                res.put("message", "Tabla eliminada exitosamente");
            } else {
                res.put("success", false);
                res.put("message", "Tabla no encontrada");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    // --- LOGS ENDPOINTS ---
    @RequestMapping(value = "/logs/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listLogs(@RequestParam(required = false, defaultValue = "0") Integer start, @RequestParam(required = false, defaultValue = "20") Integer limit) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            List<ReportLog> list = reportDao.getReportLogs(start, limit);
            int count = reportDao.getReportLogsCount();
            
            // Mapper manual simplificado para evitar ciclos de entidad (Lazy properties)
            List<Map<String, Object>> mapped = new ArrayList<Map<String, Object>>();
            for (ReportLog log : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", log.getId());
                map.put("startTime", log.getStartTime());
                map.put("endTime", log.getEndTime());
                map.put("status", log.getStatus());
                map.put("message", log.getMessage());
                map.put("reportName", log.getReport() != null ? log.getReport().getName() : "");
                map.put("userName", log.getUser() != null ? log.getUser().getUserName() : "");
                mapped.add(map);
            }
            
            res.put("success", true);
            res.put("data", mapped);
            res.put("total", count);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    private String processSqlQuery(String sql) {
        if (sql == null) {
            return "";
        }
        sql = sql.replace("{schema}.", "").replace("{schema}", "");
        
        String supplierNumber = "";
        try {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                String username = auth.getName();
                if (usersDao != null) {
                    Users user = usersDao.getByUserName(username);
                    if (user != null && user.getAddressNumber() != null) {
                        supplierNumber = user.getAddressNumber();
                    }
                }
            }
        } catch (Exception e) {
            // Ignore security context
        }
        
        String sNum = supplierNumber.replace("'", "''");
        
        sql = sql.replace("'{addressNumber}'", "'" + sNum + "'");
        sql = sql.replace("\"{addressNumber}\"", "'" + sNum + "'");
        sql = sql.replace("{addressNumber}", "'" + sNum + "'");
        
        sql = sql.replace("'{supplier}'", "'" + sNum + "'");
        sql = sql.replace("\"{supplier}\"", "'" + sNum + "'");
        sql = sql.replace("{supplier}", "'" + sNum + "'");
        
        return sql;
    }
}
