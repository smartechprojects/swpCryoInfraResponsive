package com.eurest.supplier.model;

import javax.persistence.*;

@Entity
@Table(name = "REPORT_DASHBOARD_WIDGET")
public class ReportDashboardWidget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WIDGET_ID")
    private Integer id;

    @Column(name = "GROUP_ID", nullable = true)
    private Integer groupId;

    @Column(name = "ROLE", length = 500)
    private String role;

    @Column(name = "WIDGET_TYPE", nullable = false, length = 50)
    private String widgetType;

    @Column(name = "WIDGET_REF_ID", nullable = false)
    private Integer widgetRefId;

    @Column(name = "COL_SPAN", nullable = false)
    private Integer colSpan;

    @Column(name = "SORT_ORDER", nullable = false)
    private Integer sortOrder;

    public ReportDashboardWidget() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public Integer getWidgetRefId() {
        return widgetRefId;
    }

    public void setWidgetRefId(Integer widgetRefId) {
        this.widgetRefId = widgetRefId;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
