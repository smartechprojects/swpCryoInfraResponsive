package com.eurest.supplier.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "REPORT_DASHBOARD_TABLE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDashboardTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Lob
    @Column(name = "SQL_QUERY", columnDefinition = "TEXT", nullable = false)
    private String sqlQuery;

    @Column(name = "COLUMN_HEADERS", nullable = false, length = 1000)
    private String columnHeaders;

    @Column(name = "COLUMN_KEYS", nullable = false, length = 1000)
    private String columnKeys;

    @Column(name = "COLUMN_FORMATS", nullable = false, length = 1000)
    private String columnFormats;

    @Column(name = "SORT_ORDER", nullable = false)
    private Integer sortOrder;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_ID", nullable = true)
    private ReportGroup group;

    @Column(name = "ROLE", length = 500)
    private String role;

    public ReportDashboardTable() {
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

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(String columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public String getColumnKeys() {
        return columnKeys;
    }

    public void setColumnKeys(String columnKeys) {
        this.columnKeys = columnKeys;
    }

    public String getColumnFormats() {
        return columnFormats;
    }

    public void setColumnFormats(String columnFormats) {
        this.columnFormats = columnFormats;
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
}
