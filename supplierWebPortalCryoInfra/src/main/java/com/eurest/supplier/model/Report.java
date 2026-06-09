package com.eurest.supplier.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REPORT")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 255)
    private String description;

    @Column(name = "REPORT_FILE", nullable = false, length = 255)
    private String file;

    @Column(name = "PDF_EXPORT", nullable = false)
    private boolean pdfExportEnabled;

    @Column(name = "CSV_EXPORT", nullable = false)
    private boolean csvExportEnabled;

    @Column(name = "XLS_EXPORT", nullable = false)
    private boolean xlsExportEnabled;

    @Column(name = "HTML_EXPORT", nullable = false)
    private boolean htmlExportEnabled;

    @Column(name = "RTF_EXPORT", nullable = false)
    private boolean rtfExportEnabled;

    @Column(name = "TEXT_EXPORT", nullable = false)
    private boolean textExportEnabled;

    @Column(name = "EXCEL_EXPORT", nullable = false)
    private boolean excelExportEnabled;

    @Column(name = "IMAGE_EXPORT", nullable = false)
    private boolean imageExportEnabled;

    @Column(name = "FILL_VIRTUAL", nullable = false)
    private boolean virtualizationEnabled;

    @Column(name = "HIDDEN_REPORT", nullable = false)
    private boolean hidden;

    @Lob
    @Column(name = "REPORT_QUERY", columnDefinition = "TEXT")
    private String query;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATASOURCE_ID")
    private ReportDataSource dataSource;

    @Column(name = "ROLE", length = 500)
    private String role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHART_ID")
    private ReportChart reportChart;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "EXPORT_OPTION_ID")
    private ReportExportOption reportExportOption;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "REPORT_PARAMETER_MAP", joinColumns = @JoinColumn(name = "REPORT_ID"))
    @OrderColumn(name = "MAP_ID")
    private List<ReportParameterMap> parameters = new ArrayList<ReportParameterMap>();

    @Lob
    @Column(name = "PDF_DESIGN_CONFIG", columnDefinition = "TEXT")
    private String pdfDesignConfig;

    public Report() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isPdfExportEnabled() {
        return pdfExportEnabled;
    }

    public void setPdfExportEnabled(boolean pdfExportEnabled) {
        this.pdfExportEnabled = pdfExportEnabled;
    }

    public boolean isCsvExportEnabled() {
        return csvExportEnabled;
    }

    public void setCsvExportEnabled(boolean csvExportEnabled) {
        this.csvExportEnabled = csvExportEnabled;
    }

    public boolean isXlsExportEnabled() {
        return xlsExportEnabled;
    }

    public void setXlsExportEnabled(boolean xlsExportEnabled) {
        this.xlsExportEnabled = xlsExportEnabled;
    }

    public boolean isHtmlExportEnabled() {
        return htmlExportEnabled;
    }

    public void setHtmlExportEnabled(boolean htmlExportEnabled) {
        this.htmlExportEnabled = htmlExportEnabled;
    }

    public boolean isRtfExportEnabled() {
        return rtfExportEnabled;
    }

    public void setRtfExportEnabled(boolean rtfExportEnabled) {
        this.rtfExportEnabled = rtfExportEnabled;
    }

    public boolean isTextExportEnabled() {
        return textExportEnabled;
    }

    public void setTextExportEnabled(boolean textExportEnabled) {
        this.textExportEnabled = textExportEnabled;
    }

    public boolean isExcelExportEnabled() {
        return excelExportEnabled;
    }

    public void setExcelExportEnabled(boolean excelExportEnabled) {
        this.excelExportEnabled = excelExportEnabled;
    }

    public boolean isImageExportEnabled() {
        return imageExportEnabled;
    }

    public void setImageExportEnabled(boolean imageExportEnabled) {
        this.imageExportEnabled = imageExportEnabled;
    }

    public boolean isVirtualizationEnabled() {
        return virtualizationEnabled;
    }

    public void setVirtualizationEnabled(boolean virtualizationEnabled) {
        this.virtualizationEnabled = virtualizationEnabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ReportDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReportDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ReportChart getReportChart() {
        return reportChart;
    }

    public void setReportChart(ReportChart reportChart) {
        this.reportChart = reportChart;
    }

    public ReportExportOption getReportExportOption() {
        return reportExportOption;
    }

    public void setReportExportOption(ReportExportOption reportExportOption) {
        this.reportExportOption = reportExportOption;
    }

    public List<ReportParameterMap> getParameters() {
        return parameters;
    }

    public void setParameters(List<ReportParameterMap> parameters) {
        this.parameters = parameters;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPdfDesignConfig() {
        return pdfDesignConfig;
    }

    public void setPdfDesignConfig(String pdfDesignConfig) {
        this.pdfDesignConfig = pdfDesignConfig;
    }
}
