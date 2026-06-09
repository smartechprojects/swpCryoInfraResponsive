package com.eurest.supplier.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "REPORT_CHART")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHART_ID")
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 255)
    private String description;

    @Lob
    @Column(name = "CHART_QUERY", columnDefinition = "TEXT", nullable = false)
    private String chartQuery;

    @Column(name = "CHART_TYPE", nullable = false)
    private Integer chartType;

    @Column(name = "WIDTH", nullable = false)
    private Integer width;

    @Column(name = "HEIGHT", nullable = false)
    private Integer height;

    @Column(name = "X_AXIS_LABEL", length = 255)
    private String XAxisLabel;

    @Column(name = "Y_AXIS_LABEL", length = 255)
    private String YAxisLabel;

    @Column(name = "SHOW_LEGEND", nullable = false)
    private boolean showLegend;

    @Column(name = "SHOW_TITLE", nullable = false)
    private boolean showTitle;

    @Column(name = "SHOW_VALUES", nullable = false)
    private boolean showValues;

    @Column(name = "PLOT_ORIENTATION")
    private Integer plotOrientation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATASOURCE_ID")
    private ReportDataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report drillDownReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OVERLAY_CHART_ID")
    private ReportChart overlayChart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_ID")
    private ReportGroup group;

    @Column(name = "ROLE", length = 500)
    private String role;

    @Column(name = "COLORS", length = 500)
    private String colors;

    public ReportChart() {
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

    public String getChartQuery() {
        return chartQuery;
    }

    public void setChartQuery(String chartQuery) {
        this.chartQuery = chartQuery;
    }

    public Integer getChartType() {
        return chartType;
    }

    public void setChartType(Integer chartType) {
        this.chartType = chartType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getXAxisLabel() {
        return XAxisLabel;
    }

    public void setXAxisLabel(String XAxisLabel) {
        this.XAxisLabel = XAxisLabel;
    }

    public String getYAxisLabel() {
        return YAxisLabel;
    }

    public void setYAxisLabel(String YAxisLabel) {
        this.YAxisLabel = YAxisLabel;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public boolean isShowValues() {
        return showValues;
    }

    public void setShowValues(boolean showValues) {
        this.showValues = showValues;
    }

    public Integer getPlotOrientation() {
        return plotOrientation;
    }

    public void setPlotOrientation(Integer plotOrientation) {
        this.plotOrientation = plotOrientation;
    }

    public ReportDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReportDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Report getDrillDownReport() {
        return drillDownReport;
    }

    public void setDrillDownReport(Report drillDownReport) {
        this.drillDownReport = drillDownReport;
    }

    public ReportChart getOverlayChart() {
        return overlayChart;
    }

    public void setOverlayChart(ReportChart overlayChart) {
        this.overlayChart = overlayChart;
    }

    public ReportGroup getGroup() {
        return group;
    }

    public void setGroup(ReportGroup group) {
        this.group = group;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }
}
