package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="plantAccessWorker")
public class PlantAccessWorker {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaRegistro;
	
	private String membershipIMSS;
	private String datefolioIDcard;
	private String dateInduction;
	private String employeeName;
	private String employeeLastName;
	private String employeeSecondLastName;
	private String cardNumber;
    private String requestNumber;
    private String activities;
    private String listDocuments;
    private boolean allDocuments;
    private boolean docsActivity1;
    private boolean docsActivity2;
    private boolean docsActivity3;
    private boolean docsActivity4;
    private boolean docsActivity5;
    private boolean docsActivity6;
    private boolean docsActivity7;
    private String employeeOrdenes;
    private String employeePuesto;
    private String employeeCurp;
    private String employeeRfc;
    
	
	
	
    @Transient
    private String tempId;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getMembershipIMSS() {
		return membershipIMSS;
	}
	public void setMembershipIMSS(String membershipIMSS) {
		this.membershipIMSS = membershipIMSS;
	}
	public String getDatefolioIDcard() {
		return datefolioIDcard;
	}
	public void setDatefolioIDcard(String datefolioIDcard) {
		this.datefolioIDcard = datefolioIDcard;
	}
	public String getDateInduction() {
		return dateInduction;
	}
	public void setDateInduction(String dateInduction) {
		this.dateInduction = dateInduction;
	}
	public String getRequestNumber() {
		return requestNumber;
	}
	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeLastName() {
		return employeeLastName;
	}
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}
	public String getEmployeeSecondLastName() {
		return employeeSecondLastName;
	}
	public void setEmployeeSecondLastName(String employeeSecondLastName) {
		this.employeeSecondLastName = employeeSecondLastName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getActivities() {
		return activities;
	}
	public void setActivities(String activities) {
		this.activities = activities;
	}
	public String getListDocuments() {
		return listDocuments;
	}
	public void setListDocuments(String listDocuments) {
		this.listDocuments = listDocuments;
	}
	public boolean isAllDocuments() {
		return allDocuments;
	}
	public void setAllDocuments(boolean allDocuments) {
		this.allDocuments = allDocuments;
	}
	public boolean isDocsActivity1() {
		return docsActivity1;
	}
	public void setDocsActivity1(boolean docsActivity1) {
		this.docsActivity1 = docsActivity1;
	}
	public boolean isDocsActivity2() {
		return docsActivity2;
	}
	public void setDocsActivity2(boolean docsActivity2) {
		this.docsActivity2 = docsActivity2;
	}
	public boolean isDocsActivity3() {
		return docsActivity3;
	}
	public void setDocsActivity3(boolean docsActivity3) {
		this.docsActivity3 = docsActivity3;
	}
	public boolean isDocsActivity4() {
		return docsActivity4;
	}
	public void setDocsActivity4(boolean docsActivity4) {
		this.docsActivity4 = docsActivity4;
	}
	public boolean isDocsActivity5() {
		return docsActivity5;
	}
	public void setDocsActivity5(boolean docsActivity5) {
		this.docsActivity5 = docsActivity5;
	}
	public boolean isDocsActivity6() {
		return docsActivity6;
	}
	public void setDocsActivity6(boolean docsActivity6) {
		this.docsActivity6 = docsActivity6;
	}
	public boolean isDocsActivity7() {
		return docsActivity7;
	}
	public void setDocsActivity7(boolean docsActivity7) {
		this.docsActivity7 = docsActivity7;
	}
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getEmployeeOrdenes() {
		return employeeOrdenes;
	}
	public void setEmployeeOrdenes(String employeeOrdenes) {
		this.employeeOrdenes = employeeOrdenes;
	}
	public String getEmployeePuesto() {
		return employeePuesto;
	}
	public void setEmployeePuesto(String employeePuesto) {
		this.employeePuesto = employeePuesto;
	}
	public String getEmployeeCurp() {
		return employeeCurp;
	}
	public void setEmployeeCurp(String employeeCurp) {
		this.employeeCurp = employeeCurp;
	}
	public String getEmployeeRfc() {
		return employeeRfc;
	}
	public void setEmployeeRfc(String employeeRfc) {
		this.employeeRfc = employeeRfc;
	}
	
}