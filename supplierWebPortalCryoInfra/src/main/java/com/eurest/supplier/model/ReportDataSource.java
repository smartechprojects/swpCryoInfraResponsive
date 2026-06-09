package com.eurest.supplier.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "REPORT_DATASOURCE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATASOURCE_ID")
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "DRIVER", length = 255)
    private String driverClassName;

    @Column(name = "URL", nullable = false, length = 255)
    private String url;

    @Column(name = "USERNAME", length = 255)
    private String username;

    @Column(name = "PASSWORD", length = 255)
    private String password;

    @Column(name = "MAX_IDLE")
    private Integer maxIdle;

    @Column(name = "MAX_ACTIVE")
    private Integer maxActive;

    @Column(name = "MAX_WAIT")
    private Long maxWait;

    @Column(name = "VALIDATION_QUERY", length = 255)
    private String validationQuery;

    @Column(name = "JNDI")
    private Boolean jndi;

    public ReportDataSource() {
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

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public Boolean getJndi() {
        return jndi;
    }

    public void setJndi(Boolean jndi) {
        this.jndi = jndi;
    }
}
