package com.eurest.supplier.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "REPORT_KPI")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportKpi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KPI_ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_ID", nullable = true)
    private ReportGroup group;

    @Column(name = "ROLE", length = 500)
    private String role;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "SQL_QUERY", nullable = false, columnDefinition = "TEXT")
    private String sqlQuery;

    @Column(name = "KPI_TYPE", nullable = false, length = 50)
    private String kpiType; // 'CURRENCY', 'NUMBER', 'PERCENTAGE', 'TEXT'

    @Column(name = "ICON_NAME", length = 50)
    private String iconName;

    @Column(name = "SUBTEXT_TEMPLATE", length = 100)
    private String subtextTemplate;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "COLOR", length = 50)
    private String color;

    public ReportKpi() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ReportGroup getGroup() {
        return group;
    }

    public void setGroup(ReportGroup group) {
        this.group = group;
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

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getSubtextTemplate() {
        return subtextTemplate;
    }

    public void setSubtextTemplate(String subtextTemplate) {
        this.subtextTemplate = subtextTemplate;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
