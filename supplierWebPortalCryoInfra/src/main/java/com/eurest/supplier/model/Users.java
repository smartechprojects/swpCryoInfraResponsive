package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users")
public class Users {

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
    private String userName;
    private String password;
    private String email;
    private String addressNumber;
    
	@OneToOne
	private UDC userRole;
	
    private String role;
    
	@OneToOne
	private UDC userType;
    
	@Column(nullable = true, columnDefinition = "TINYINT", length = 1)
    private boolean enabled;
	
	@Column(nullable = true, columnDefinition = "TINYINT", length = 1)
    private boolean logged = false;
	
	@Column(nullable = true, columnDefinition = "TINYINT", length = 1)
    private boolean agreementAccept = false;
	
    private String notes;
    
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean exepAccesRule = false;
    
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean supplier = false;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean subUser = false;
    
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean mainSupplierUser = false;
    
    private int batchnumber;
    private int linenumber;
    
    @Temporal(TemporalType.TIMESTAMP)
	  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	  private Date bacthDate;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public UDC getUserRole() {
		return userRole;
	}

	public void setUserRole(UDC userRole) {
		this.userRole = userRole;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UDC getUserType() {
		return userType;
	}

	public void setUserType(UDC userType) {
		this.userType = userType;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isAgreementAccept() {
		return agreementAccept;
	}

	public void setAgreementAccept(boolean agreementAccept) {
		this.agreementAccept = agreementAccept;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isExepAccesRule() {
		return exepAccesRule;
	}

	public void setExepAccesRule(boolean exepAccesRule) {
		this.exepAccesRule = exepAccesRule;
	}

	public boolean isSupplier() {
		return supplier;
	}

	public void setSupplier(boolean supplier) {
		this.supplier = supplier;
	}

	public boolean isSubUser() {
		return subUser;
	}

	public void setSubUser(boolean subUser) {
		this.subUser = subUser;
	}

	public boolean isMainSupplierUser() {
		return mainSupplierUser;
	}

	public void setMainSupplierUser(boolean mainSupplierUser) {
		this.mainSupplierUser = mainSupplierUser;
	}
	

	public int getBatchnumber() {
		return batchnumber;
	}

	public void setBatchnumber(int batchnumber) {
		this.batchnumber = batchnumber;
	}

	public int getLinenumber() {
		return linenumber;
	}

	public void setLinenumber(int linenumber) {
		this.linenumber = linenumber;
	}

	public Date getBacthDate() {
		return bacthDate;
	}

	public void setBacthDate(Date bacthDate) {
		this.bacthDate = bacthDate;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", name=" + name + ", userName=" + userName + ", password=" + password + ", email="
				+ email + ", addressNumber=" + addressNumber + ", userRole=" + userRole + ", role=" + role
				+ ", userType=" + userType + ", enabled=" + enabled + ", logged=" + logged + ", agreementAccept="
				+ agreementAccept + ", notes=" + notes + ", exepAccesRule=" + exepAccesRule + ", supplier=" + supplier
				+ ", subUser=" + subUser + ", mainSupplierUser=" + mainSupplierUser + "]";
	}
	
}
