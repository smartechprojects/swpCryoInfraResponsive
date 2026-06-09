package com.eurest.supplier.model;

import javax.persistence.*;

@Embeddable
public class ReportParameterMap {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARAMETER_ID")
    private ReportParameter reportParameter;

    @Column(name = "REQUIRED")
    private Boolean required;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "STEP")
    private Integer step;

    public ReportParameterMap() {
    }

    public ReportParameter getReportParameter() {
        return reportParameter;
    }

    public void setReportParameter(ReportParameter reportParameter) {
        this.reportParameter = reportParameter;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
