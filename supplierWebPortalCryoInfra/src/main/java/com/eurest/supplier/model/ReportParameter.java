package com.eurest.supplier.model;

import javax.persistence.*;

@Entity
@Table(name = "REPORT_PARAMETER")
public class ReportParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARAMETER_ID")
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "TYPE", nullable = false, length = 255)
    private String type;

    @Column(name = "CLASSNAME", nullable = false, length = 255)
    private String className;

    @Lob
    @Column(name = "DATA", columnDefinition = "TEXT")
    private String data;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATASOURCE_ID")
    private ReportDataSource dataSource;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "REQUIRED")
    private Boolean required;

    @Column(name = "MULTI_SELECT")
    private Boolean multipleSelect;

    @Column(name = "DEFAULT_VALUE", length = 255)
    private String defaultValue;

    public ReportParameter() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ReportDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReportDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getMultipleSelect() {
        return multipleSelect;
    }

    public void setMultipleSelect(Boolean multipleSelect) {
        this.multipleSelect = multipleSelect;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
