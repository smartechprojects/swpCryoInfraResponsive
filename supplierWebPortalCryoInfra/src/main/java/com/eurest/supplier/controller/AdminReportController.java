package com.eurest.supplier.controller;

import com.eurest.supplier.model.Report;
import com.eurest.supplier.model.ReportExportOption;
import com.eurest.supplier.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private com.eurest.supplier.dao.ReportDao reportDao;

    private String getReportDir() {
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = java.net.URLDecoder.decode(path, "UTF-8");
            return fullPath + "jasperReports";
        } catch (Exception e) {
            throw new RuntimeException("Error resolviendo el directorio de reportes", e);
        }
    }

    @RequestMapping(value = "/list.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getAllReports() {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        res.put("data", reportService.getAllReports());
        return res;
    }

    @RequestMapping(value = "/view.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getReportById(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        Report r = reportService.getReportById(id);
        if (r != null) {
            res.put("success", true);
            res.put("data", r);
        } else {
            res.put("success", false);
            res.put("message", "Reporte no encontrado");
        }
        return res;
    }

    @RequestMapping(value = "/save.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveReport(java.io.Reader reader) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Report details = mapper.readValue(reader, Report.class);
            
            // Persistir/Actualizar los parámetros asociados para evitar transient objects o nulls
            if (details.getParameters() != null) {
                for (com.eurest.supplier.model.ReportParameterMap pm : details.getParameters()) {
                    com.eurest.supplier.model.ReportParameter rp = pm.getReportParameter();
                    if (rp != null) {
                        com.eurest.supplier.model.ReportParameter existingRp = reportDao.getReportParameterByName(rp.getName());
                        if (existingRp != null) {
                            existingRp.setDescription(rp.getDescription());
                            existingRp.setClassName(rp.getClassName());
                            existingRp.setDefaultValue(rp.getDefaultValue());
                            existingRp.setData(rp.getData()); // sqlQuery
                            existingRp.setRequired(rp.getRequired());
                            existingRp.setType(rp.getType() != null ? rp.getType() : "TEXT");
                            existingRp.setMultipleSelect(rp.getMultipleSelect() != null ? rp.getMultipleSelect() : false);
                            reportDao.saveReportParameter(existingRp);
                            pm.setReportParameter(existingRp);
                        } else {
                            rp.setId(null); // Asegurar que sea una inserción limpia
                            if (rp.getType() == null) rp.setType("TEXT");
                            if (rp.getMultipleSelect() == null) rp.setMultipleSelect(false);
                            reportDao.saveReportParameter(rp);
                            pm.setReportParameter(rp);
                        }
                    }
                }
            }

            Report reportToSave;
            if (details.getId() != null) {
                Report existing = reportService.getReportById(details.getId());
                if (existing == null) {
                    res.put("success", false);
                    res.put("message", "Reporte a actualizar no encontrado");
                    return res;
                }
                existing.setName(details.getName());
                existing.setDescription(details.getDescription());
                existing.setFile(details.getFile());
                existing.setPdfExportEnabled(details.isPdfExportEnabled());
                existing.setCsvExportEnabled(details.isCsvExportEnabled());
                existing.setXlsExportEnabled(details.isXlsExportEnabled());
                existing.setHtmlExportEnabled(details.isHtmlExportEnabled());
                existing.setRtfExportEnabled(details.isRtfExportEnabled());
                existing.setTextExportEnabled(details.isTextExportEnabled());
                existing.setExcelExportEnabled(details.isExcelExportEnabled());
                existing.setImageExportEnabled(details.isImageExportEnabled());
                existing.setVirtualizationEnabled(details.isVirtualizationEnabled());
                existing.setHidden(details.isHidden());
                existing.setQuery(details.getQuery());
                existing.setDataSource(details.getDataSource());
                existing.setReportChart(details.getReportChart());
                existing.setRole(details.getRole());
                existing.setPdfDesignConfig(details.getPdfDesignConfig());
                
                if (details.getReportExportOption() != null) {
                    if (existing.getReportExportOption() == null) {
                        existing.setReportExportOption(new ReportExportOption());
                    }
                    ReportExportOption opt = existing.getReportExportOption();
                    ReportExportOption detailsOpt = details.getReportExportOption();
                    opt.setXlsRemoveEmptySpaceBetweenRows(detailsOpt.isXlsRemoveEmptySpaceBetweenRows());
                    opt.setXlsOnePagePerSheet(detailsOpt.isXlsOnePagePerSheet());
                    opt.setXlsAutoDetectCellType(detailsOpt.isXlsAutoDetectCellType());
                    opt.setXlsWhitePageBackground(detailsOpt.isXlsWhitePageBackground());
                    opt.setHtmlRemoveEmptySpaceBetweenRows(detailsOpt.isHtmlRemoveEmptySpaceBetweenRows());
                    opt.setHtmlWhitePageBackground(detailsOpt.isHtmlWhitePageBackground());
                    opt.setHtmlUsingImagesToAlign(detailsOpt.isHtmlUsingImagesToAlign());
                    opt.setHtmlWrapBreakWord(detailsOpt.isHtmlWrapBreakWord());
                }

                if (details.getParameters() != null) {
                    existing.getParameters().clear();
                    existing.getParameters().addAll(details.getParameters());
                }
                reportToSave = existing;
            } else {
                reportToSave = details;
            }
            
            Report saved = reportService.saveReport(reportToSave);
            res.put("success", true);
            res.put("data", saved);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error al guardar el reporte");
        }
        return res;
    }

    @RequestMapping(value = "/delete.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteReport(@RequestParam Integer id) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            reportService.deleteReport(id);
            res.put("success", true);
            res.put("message", "Reporte eliminado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage() != null ? e.getMessage() : "Error al eliminar el reporte");
        }
        return res;
    }

    @RequestMapping(value = "/files.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> listReportFiles() {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            String reportDir = getReportDir();
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File[] files = dir.listFiles(new java.io.FilenameFilter() {
                @Override
                public boolean accept(File d, String name) {
                    return name.endsWith(".jasper") || name.endsWith(".jrxml");
                }
            });
            List<String> filenames = new ArrayList<String>();
            if (files != null) {
                for (File file : files) {
                    filenames.add(file.getName());
                }
            }
            res.put("success", true);
            res.put("files", filenames);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/upload.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> uploadReportFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("error", "El archivo está vacío.");
                return response;
            }
            
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".jasper") && !filename.endsWith(".jrxml"))) {
                response.put("success", false);
                response.put("error", "Sólo se permiten archivos compilados .jasper o plantillas .jrxml.");
                return response;
            }

            String reportDir = getReportDir();
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path path = Paths.get(reportDir, filename);
            Files.write(path, file.getBytes());

            response.put("success", true);
            response.put("message", "Archivo subido exitosamente.");
            response.put("filename", filename);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Error al guardar el archivo: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/validate.action", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> validateReport(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "") String formats) {
        Map<String, Object> res = new HashMap<String, Object>();
        List<String> messages = new ArrayList<String>();
        boolean valid = true;

        String sql = query == null ? "" : query.trim();
        if (sql.isEmpty()) {
            res.put("success", false);
            res.put("error", "La consulta SQL est\u00e1 vac\u00eda.");
            return res;
        }
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1).trim();
        }
        // Procesar parámetros de Jasper ($P{...}) a NULL para validar la sintaxis sin errores en base de datos
        sql = sql.replaceAll("\\$P\\{[a-zA-Z0-9_]+\\}", "NULL");

        // Procesar comodines de proveedor/rol y esquema para evitar errores de sintaxis al validar
        sql = sql.replace("{schema}.", "").replace("{schema}", "");
        sql = sql.replace("'{addressNumber}'", "'0'")
                 .replace("\"{addressNumber}\"", "'0'")
                 .replace("{addressNumber}", "'0'")
                 .replace("'{supplier}'", "'0'")
                 .replace("\"{supplier}\"", "'0'")
                 .replace("{supplier}", "'0'")
                 .replace("'{role}'", "'ROLE_SUPPLIER'")
                 .replace("\"{role}\"", "'ROLE_SUPPLIER'")
                 .replace("{role}", "'ROLE_SUPPLIER'");

        try {
            java.sql.Connection conn = dataSource.getConnection();
            try {
                String testSql = "SELECT * FROM (" + sql + ") _val_alias LIMIT 1";
                java.sql.PreparedStatement ps = conn.prepareStatement(testSql);
                try {
                    java.sql.ResultSet rs = ps.executeQuery();
                    try {
                        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
                        int colCount = rsmd.getColumnCount();
                        messages.add("\u2705 Consulta SQL v\u00e1lida \u2014 " + colCount + " columna(s) detectada(s).");
                    } finally { rs.close(); }
                } finally { ps.close(); }
            } finally { conn.close(); }
        } catch (Exception e) {
            valid = false;
            messages.add("\u274c Error en la consulta SQL: " + e.getMessage());
        }

        if (!formats.trim().isEmpty()) {
            for (String fmt : formats.split(",")) {
                fmt = fmt.trim().toUpperCase();
                if (!fmt.isEmpty()) {
                    messages.add("\u2705 Formato " + fmt + " habilitado.");
                }
            }
        }

        res.put("success", valid);
        res.put("messages", messages);
        return res;
    }
}
