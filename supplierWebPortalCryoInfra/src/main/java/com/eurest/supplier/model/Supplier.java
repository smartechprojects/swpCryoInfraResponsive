package com.eurest.supplier.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;

@JsonAutoDetect
	@Entity(name = "Supplier")
	@Table(name = "supplier")
	public class Supplier  implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	  @Column
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private int id;
	  
	  private String addresNumber;
	  
	  private String email;
	  
	  private String currentApprover;
	  
	  private String nextApprover;
	  
	  private String approvalStatus;
	  
	  private String approvalStep;
	  
	  private int ukuid;
	  
	  private int steps;
	  
	  private String rejectNotes;
	  
	  private String approvalNotes;
	  
	  @Temporal(TemporalType.DATE)
	  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	  private Date fechaSolicitud;
	  
	  @Temporal(TemporalType.DATE)
	  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	  private Date fechaAprobacion;
	  
	  private String regiones;
	  
	  private String categorias;
	  
	  private String categoriaJDE;
	  
	  private String cuentaBancaria;
	  
	  private String diasCredito;
	  
	  private String tipoMovimiento;
	  
	  private String compradorAsignado;
	  
	  private String name;
	  
	  private String razonSocial;
	  
	  private String giroEmpresa;
	  
	  private String rfc;
	  
	  private String emailSupplier;
	  
	  private String industryType;
	  
	  private String calleNumero;
	  
	  private String colonia;
	  
	  private String codigoPostal;
	  
	  private String delegacionMnicipio;
	  
	  private String estado;
	  
	  private String country;
	  
	  private String webSite;
	  
	  private String telefonoDF;
	  
	  private String faxDF;
	  
	  private String nombreContactoCxC;
	  
	  private String emailContactoCxC;
	  
	  private String apellidoPaternoCxC;
	  
	  private String apellidoMaternoCxC;
	  
	  private String telefonoContactoCxC;
	  
	  private String faxCxC;
	  
	  private String cargoCxC;
	  
	  private String searchType;
	  
	  private String creditMessage;
	  
	  private String taxAreaCxC;
	  
	  private String taxExpl2CxC;
	  
	  private String pmtTrmCxC;
	  
	  private String payInstCxC;
	  
	  private String currCodeCxC;
	  
	  private String catCode15;
	  
	  private String catCode27;
	  
	  private String catCode01;
	  
	  private String catCode20;
	  
	  private String catCode23;
	  
	  private String catCode24;
	  
	  private String requisitosFiscales;
	  
	  private String emailCxP01;
	  
	  private String nombreCxP01;
	  
	  private String telefonoCxP01;
	  
	  private String emailCxP02;
	  
	  private String nombreCxP02;
	  
	  private String telefonoCxP02;
	  
	  private String emailCxP03;
	  
	  private String nombreCxP03;
	  
	  private String telefonoCxP03;
	  
	  private String emailCxP04;
	  
	  private String nombreCxP04;
	  
	  private String telefonoCxP04;
	  
	  private String tipoIdentificacion;
	  
	  private String numeroIdentificacion;
	  
	  private String nombreRL;
	  
	  private String apellidoPaternoRL;
	  
	  private String apellidoMaternoRL;
	  
	  private String bankTitleRepName;
	  
	  private String bankName;
	  
	  private String cuentaClabe;
	  
	  private String bankAccountNumber;
	  
	  private String swiftCode;
	  
	  private String ibanCode;
	  
	  private String glClass;
	  
	  private String bankTransitNumber;
	  
	  private String custBankAcct;
	  
	  private String controlDigit;
	  
	  private String description;
	  
	  private String checkingOrSavingAccount;
	  
	  private String rollNumber;
	  
	  private String bankAddressNumber;
	  
	  private String bankCountryCode; 
	  
	  private String nombreBanco;
	  
	  private String formaPago;
	  
	  private String nombreContactoPedidos;
	  
	  private String emailContactoPedidos;
	  
	  private String telefonoContactoPedidos;
	  
	  private String nombreContactoVentas;
	  
	  private String emailContactoVentas;
	  
	  private String telefonoContactoVentas;
	  
	  private String nombreContactoCalidad;
	  
	  private String emailContactoCalidad;
	  
	  private String telefonoContactoCalidad;
	  
	  private String puestoCalidad;
	  
	  private String direccionPlanta;
	  
	  private String direccionCentroDistribucion;
	  
	  private String tipoProductoServicio;
	  
	  private String riesgoCategoria;
	  
	  private String observaciones;
	  
	  private String batchNumber;
	  
	  private String diasCreditoActual;
	  
	  private String diasCreditoAnterior;
	  
	  private String tasaIva;
	  
	  private String taxRate;
	  
	  private String explCode1;
	  
	  private String invException;
	  
	  private boolean acceptOpenOrder;
	  
	  private String paymentMethod;
	  
	  private String supplierType;
	  
	  private String automaticEmail;
	  
	  @Column(length = 500)
	  private String fileList;
	  
	  private Long ticketId;
	  
	  private String emailComprador;
	  
	  private String currencyCode;
	  
	  private String fisicaMoral;
	  
	  private String taxId;
	  
	  private String faxNumber;
	  
	  private String legalRepIdType;
	  
	  private String legalRepIdNumber;
	  
	  private String legalRepName;
	  
	  private String industryClass;
	  
	  private String hold;
	  
	  private boolean outSourcing;
		  
	  private boolean outSourcingAccept;
		
	  private boolean outSourcingMonthlyAccept;
	  
	  private boolean outSourcingQuarterlyAccept;
	  
	  @Temporal(TemporalType.TIMESTAMP)
	  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	  private Date outSourcingRecordDate;
	  
	  private String replicationStatus;
	  
	  @Temporal(TemporalType.DATE)
	  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	  private Date replicationDate;
	  
	  private String replicationMessage;
	  
	  private String idFiscal;
	  
      private String updateApprovalFlow;
      
      @Column(length = 1500)
      private String dataUpdateList; 
		
	  public String getHold() {
			return hold;
		}
	  
	  public void setHold(String hold) {
			this.hold = hold;
		}
	  
	  public String getIndustryClass() {
		return industryClass;
	}

	public void setIndustryClass(String industryClass) {
		this.industryClass = industryClass;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getCreditMessage() {
		return creditMessage;
	}

	public void setCreditMessage(String creditMessage) {
		this.creditMessage = creditMessage;
	}

	public String getCatCode15() {
		return catCode15;
	}

	public void setCatCode15(String catCode15) {
		this.catCode15 = catCode15;
	}

	public String getCatCode27() {
		return catCode27;
	}

	public void setCatCode27(String catCode27) {
		this.catCode27 = catCode27;
	}

	public String getGlClass() {
		return glClass;
	}

	public void setGlClass(String glClass) {
		this.glClass = glClass;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getFaxCxC() {
		return faxCxC;
	}

	public void setFaxCxC(String faxCxC) {
		this.faxCxC = faxCxC;
	}

	public String getEmailSupplier() {
		return emailSupplier;
	}

	public void setEmailSupplier(String emailSupplier) {
		this.emailSupplier = emailSupplier;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getGiroEmpresa() {
		return giroEmpresa;
	}

	public void setGiroEmpresa(String giroEmpresa) {
		this.giroEmpresa = giroEmpresa;
	}

	public String getTelefonoDF() {
		return telefonoDF;
	}

	public void setTelefonoDF(String telefonoDF) {
		this.telefonoDF = telefonoDF;
	}

	public String getFaxDF() {
		return faxDF;
	}

	public void setFaxDF(String faxDF) {
		this.faxDF = faxDF;
	}

	public String getApellidoPaternoCxC() {
		return apellidoPaternoCxC;
	}

	public void setApellidoPaternoCxC(String apellidoPaternoCxC) {
		this.apellidoPaternoCxC = apellidoPaternoCxC;
	}

	public String getApellidoMaternoCxC() {
		return apellidoMaternoCxC;
	}

	public void setApellidoMaternoCxC(String apellidoMaternoCxC) {
		this.apellidoMaternoCxC = apellidoMaternoCxC;
	}

	public String getCargoCxC() {
		return cargoCxC;
	}

	public void setCargoCxC(String cargoCxC) {
		this.cargoCxC = cargoCxC;
	}

	public String getTaxAreaCxC() {
		return taxAreaCxC;
	}

	public void setTaxAreaCxC(String taxAreaCxC) {
		this.taxAreaCxC = taxAreaCxC;
	}

	public String getTaxExpl2CxC() {
		return taxExpl2CxC;
	}

	public void setTaxExpl2CxC(String taxExpl2CxC) {
		this.taxExpl2CxC = taxExpl2CxC;
	}

	public String getPmtTrmCxC() {
		return pmtTrmCxC;
	}

	public void setPmtTrmCxC(String pmtTrmCxC) {
		this.pmtTrmCxC = pmtTrmCxC;
	}

	public String getPayInstCxC() {
		return payInstCxC;
	}

	public void setPayInstCxC(String payInstCxC) {
		this.payInstCxC = payInstCxC;
	}

	public String getCurrCodeCxC() {
		return currCodeCxC;
	}

	public void setCurrCodeCxC(String currCodeCxC) {
		this.currCodeCxC = currCodeCxC;
	}

	public String getEmailCxP01() {
		return emailCxP01;
	}

	public void setEmailCxP01(String emailCxP01) {
		this.emailCxP01 = emailCxP01;
	}

	public String getNombreCxP01() {
		return nombreCxP01;
	}

	public void setNombreCxP01(String nombreCxP01) {
		this.nombreCxP01 = nombreCxP01;
	}

	public String getTelefonoCxP01() {
		return telefonoCxP01;
	}

	public void setTelefonoCxP01(String telefonoCxP01) {
		this.telefonoCxP01 = telefonoCxP01;
	}

	public String getEmailCxP02() {
		return emailCxP02;
	}

	public void setEmailCxP02(String emailCxP02) {
		this.emailCxP02 = emailCxP02;
	}

	public String getNombreCxP02() {
		return nombreCxP02;
	}

	public void setNombreCxP02(String nombreCxP02) {
		this.nombreCxP02 = nombreCxP02;
	}

	public String getTelefonoCxP02() {
		return telefonoCxP02;
	}

	public void setTelefonoCxP02(String telefonoCxP02) {
		this.telefonoCxP02 = telefonoCxP02;
	}

	public String getEmailCxP03() {
		return emailCxP03;
	}

	public void setEmailCxP03(String emailCxP03) {
		this.emailCxP03 = emailCxP03;
	}

	public String getNombreCxP03() {
		return nombreCxP03;
	}

	public void setNombreCxP03(String nombreCxP03) {
		this.nombreCxP03 = nombreCxP03;
	}

	public String getTelefonoCxP03() {
		return telefonoCxP03;
	}

	public void setTelefonoCxP03(String telefonoCxP03) {
		this.telefonoCxP03 = telefonoCxP03;
	}

	public String getEmailCxP04() {
		return emailCxP04;
	}

	public void setEmailCxP04(String emailCxP04) {
		this.emailCxP04 = emailCxP04;
	}

	public String getNombreCxP04() {
		return nombreCxP04;
	}

	public void setNombreCxP04(String nombreCxP04) {
		this.nombreCxP04 = nombreCxP04;
	}

	public String getTelefonoCxP04() {
		return telefonoCxP04;
	}

	public void setTelefonoCxP04(String telefonoCxP04) {
		this.telefonoCxP04 = telefonoCxP04;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getNombreRL() {
		return nombreRL;
	}

	public void setNombreRL(String nombreRL) {
		this.nombreRL = nombreRL;
	}

	public String getApellidoPaternoRL() {
		return apellidoPaternoRL;
	}

	public void setApellidoPaternoRL(String apellidoPaternoRL) {
		this.apellidoPaternoRL = apellidoPaternoRL;
	}

	public String getApellidoMaternoRL() {
		return apellidoMaternoRL;
	}

	public void setApellidoMaternoRL(String apellidoMaternoRL) {
		this.apellidoMaternoRL = apellidoMaternoRL;
	}

	public String getCustBankAcct() {
		return custBankAcct;
	}

	public void setCustBankAcct(String custBankAcct) {
		this.custBankAcct = custBankAcct;
	}

	public String getControlDigit() {
		return controlDigit;
	}

	public void setControlDigit(String controlDigit) {
		this.controlDigit = controlDigit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCheckingOrSavingAccount() {
		return checkingOrSavingAccount;
	}

	public void setCheckingOrSavingAccount(String checkingOrSavingAccount) {
		this.checkingOrSavingAccount = checkingOrSavingAccount;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getBankAddressNumber() {
		return bankAddressNumber;
	}

	public void setBankAddressNumber(String bankAddressNumber) {
		this.bankAddressNumber = bankAddressNumber;
	}

	public String getBankCountryCode() {
		return bankCountryCode;
	}

	public void setBankCountryCode(String bankCountryCode) {
		this.bankCountryCode = bankCountryCode;
	}

	public int getId() {
	    return this.id;
	  }
	  
	  public void setId(int id) {
	    this.id = id;
	  }
	  
	  public String getAddresNumber() {
	    return this.addresNumber;
	  }
	  
	  public void setAddresNumber(String addresNumber) {
	    this.addresNumber = addresNumber;
	  }
	  
	  public String getName() {
	    return this.name;
	  }
	  
	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public String getEmail() {
	    return this.email;
	  }
	  
	  public void setEmail(String email) {
	    this.email = email;
	  }
	  
	  public String getCurrentApprover() {
	    return this.currentApprover;
	  }
	  
	  public void setCurrentApprover(String currentApprover) {
	    this.currentApprover = currentApprover;
	  }
	  
	  public String getNextApprover() {
	    return this.nextApprover;
	  }
	  
	  public void setNextApprover(String nextApprover) {
	    this.nextApprover = nextApprover;
	  }
	  
	  public String getApprovalStatus() {
	    return this.approvalStatus;
	  }
	  
	  public void setApprovalStatus(String approvalStatus) {
	    this.approvalStatus = approvalStatus;
	  }
	  
	  public String getApprovalStep() {
	    return this.approvalStep;
	  }
	  
	  public void setApprovalStep(String approvalStep) {
	    this.approvalStep = approvalStep;
	  }
	  
	  public String getRejectNotes() {
	    return this.rejectNotes;
	  }
	  
	  public void setRejectNotes(String rejectNotes) {
	    this.rejectNotes = rejectNotes;
	  }
	  
	  public String getApprovalNotes() {
	    return this.approvalNotes;
	  }
	  
	  public void setApprovalNotes(String approvalNotes) {
	    this.approvalNotes = approvalNotes;
	  }
	  
	  public Date getFechaSolicitud() {
	    return this.fechaSolicitud;
	  }
	  
	  public void setFechaSolicitud(Date fechaSolicitud) {
	    this.fechaSolicitud = fechaSolicitud;
	  }
	  
	  public Date getFechaAprobacion() {
	    return this.fechaAprobacion;
	  }
	  
	  public void setFechaAprobacion(Date fechaAprobacion) {
	    this.fechaAprobacion = fechaAprobacion;
	  }
	  
	  public String getCategoriaJDE() {
	    return this.categoriaJDE;
	  }
	  
	  public void setCategoriaJDE(String categoriaJDE) {
	    this.categoriaJDE = categoriaJDE;
	  }
	  
	  public String getCuentaBancaria() {
	    return this.cuentaBancaria;
	  }
	  
	  public void setCuentaBancaria(String cuentaBancaria) {
	    this.cuentaBancaria = cuentaBancaria;
	  }
	  
	  public String getDiasCredito() {
	    return this.diasCredito;
	  }
	  
	  public void setDiasCredito(String diasCredito) {
	    this.diasCredito = diasCredito;
	  }
	  
	  public String getTipoMovimiento() {
	    return this.tipoMovimiento;
	  }
	  
	  public void setTipoMovimiento(String tipoMovimiento) {
	    this.tipoMovimiento = tipoMovimiento;
	  }
	  
	  public String getCompradorAsignado() {
	    return this.compradorAsignado;
	  }
	  
	  public void setCompradorAsignado(String compradorAsignado) {
	    this.compradorAsignado = compradorAsignado;
	  }
	  
	  public String getRazonSocial() {
	    return this.razonSocial;
	  }
	  
	  public void setRazonSocial(String razonSocial) {
	    this.razonSocial = razonSocial;
	  }
	  
	  public String getRfc() {
	    return this.rfc;
	  }
	  
	  public void setRfc(String rfc) {
	    this.rfc = rfc;
	  }
	  
	  public String getCalleNumero() {
	    return this.calleNumero;
	  }
	  
	  public void setCalleNumero(String calleNumero) {
	    this.calleNumero = calleNumero;
	  }
	  
	  public String getColonia() {
	    return this.colonia;
	  }
	  
	  public void setColonia(String colonia) {
	    this.colonia = colonia;
	  }
	  
	  public String getCodigoPostal() {
	    return this.codigoPostal;
	  }
	  
	  public void setCodigoPostal(String codigoPostal) {
	    this.codigoPostal = codigoPostal;
	  }
	  
	  public String getDelegacionMnicipio() {
	    return this.delegacionMnicipio;
	  }
	  
	  public void setDelegacionMnicipio(String delegacionMnicipio) {
	    this.delegacionMnicipio = delegacionMnicipio;
	  }
	  
	  public String getEstado() {
	    return this.estado;
	  }
	  
	  public void setEstado(String estado) {
	    this.estado = estado;
	  }
	  
	  public String getNombreBanco() {
	    return this.nombreBanco;
	  }
	  
	  public void setNombreBanco(String nombreBanco) {
	    this.nombreBanco = nombreBanco;
	  }
	  
	  public String getCuentaClabe() {
	    return this.cuentaClabe;
	  }
	  
	  public void setCuentaClabe(String cuentaClabe) {
	    this.cuentaClabe = cuentaClabe;
	  }
	  
	  public String getNombreContactoPedidos() {
	    return this.nombreContactoPedidos;
	  }
	  
	  public void setNombreContactoPedidos(String nombreContactoPedidos) {
	    this.nombreContactoPedidos = nombreContactoPedidos;
	  }
	  
	  public String getEmailContactoPedidos() {
	    return this.emailContactoPedidos;
	  }
	  
	  public void setEmailContactoPedidos(String emailContactoPedidos) {
	    this.emailContactoPedidos = emailContactoPedidos;
	  }
	  
	  public String getTelefonoContactoPedidos() {
	    return this.telefonoContactoPedidos;
	  }
	  
	  public void setTelefonoContactoPedidos(String telefonoContactoPedidos) {
	    this.telefonoContactoPedidos = telefonoContactoPedidos;
	  }
	  
	  public String getNombreContactoVentas() {
	    return this.nombreContactoVentas;
	  }
	  
	  public void setNombreContactoVentas(String nombreContactoVentas) {
	    this.nombreContactoVentas = nombreContactoVentas;
	  }
	  
	  public String getEmailContactoVentas() {
	    return this.emailContactoVentas;
	  }
	  
	  public void setEmailContactoVentas(String emailContactoVentas) {
	    this.emailContactoVentas = emailContactoVentas;
	  }
	  
	  public String getTelefonoContactoVentas() {
	    return this.telefonoContactoVentas;
	  }
	  
	  public void setTelefonoContactoVentas(String telefonoContactoVentas) {
	    this.telefonoContactoVentas = telefonoContactoVentas;
	  }
	  
	  public String getNombreContactoCxC() {
	    return this.nombreContactoCxC;
	  }
	  
	  public void setNombreContactoCxC(String nombreContactoCxC) {
	    this.nombreContactoCxC = nombreContactoCxC;
	  }
	  
	  public String getEmailContactoCxC() {
	    return this.emailContactoCxC;
	  }
	  
	  public void setEmailContactoCxC(String emailContactoCxC) {
	    this.emailContactoCxC = emailContactoCxC;
	  }
	  
	  public String getTelefonoContactoCxC() {
	    return this.telefonoContactoCxC;
	  }
	  
	  public void setTelefonoContactoCxC(String telefonoContactoCxC) {
	    this.telefonoContactoCxC = telefonoContactoCxC;
	  }
	  
	  public String getNombreContactoCalidad() {
	    return this.nombreContactoCalidad;
	  }
	  
	  public void setNombreContactoCalidad(String nombreContactoCalidad) {
	    this.nombreContactoCalidad = nombreContactoCalidad;
	  }
	  
	  public String getEmailContactoCalidad() {
	    return this.emailContactoCalidad;
	  }
	  
	  public void setEmailContactoCalidad(String emailContactoCalidad) {
	    this.emailContactoCalidad = emailContactoCalidad;
	  }
	  
	  public String getTelefonoContactoCalidad() {
	    return this.telefonoContactoCalidad;
	  }
	  
	  public void setTelefonoContactoCalidad(String telefonoContactoCalidad) {
	    this.telefonoContactoCalidad = telefonoContactoCalidad;
	  }
	  
	  public String getPuestoCalidad() {
	    return this.puestoCalidad;
	  }
	  
	  public void setPuestoCalidad(String puestoCalidad) {
	    this.puestoCalidad = puestoCalidad;
	  }
	  
	  public String getDireccionPlanta() {
	    return this.direccionPlanta;
	  }
	  
	  public void setDireccionPlanta(String direccionPlanta) {
	    this.direccionPlanta = direccionPlanta;
	  }
	  
	  public String getDireccionCentroDistribucion() {
	    return this.direccionCentroDistribucion;
	  }
	  
	  public void setDireccionCentroDistribucion(String direccionCentroDistribucion) {
	    this.direccionCentroDistribucion = direccionCentroDistribucion;
	  }
	  
	  public String getTipoProductoServicio() {
	    return this.tipoProductoServicio;
	  }
	  
	  public void setTipoProductoServicio(String tipoProductoServicio) {
	    this.tipoProductoServicio = tipoProductoServicio;
	  }
	  
	  public String getRiesgoCategoria() {
	    return this.riesgoCategoria;
	  }
	  
	  public void setRiesgoCategoria(String riesgoCategoria) {
	    this.riesgoCategoria = riesgoCategoria;
	  }
	  
	  public String getObservaciones() {
	    return this.observaciones;
	  }
	  
	  public void setObservaciones(String observaciones) {
	    this.observaciones = observaciones;
	  }
	  
	  public String getDiasCreditoActual() {
	    return this.diasCreditoActual;
	  }
	  
	  public void setDiasCreditoActual(String diasCreditoActual) {
	    this.diasCreditoActual = diasCreditoActual;
	  }
	  
	  public String getDiasCreditoAnterior() {
	    return this.diasCreditoAnterior;
	  }
	  
	  public void setDiasCreditoAnterior(String diasCreditoAnterior) {
	    this.diasCreditoAnterior = diasCreditoAnterior;
	  }
	  
	  public String getTasaIva() {
	    return this.tasaIva;
	  }
	  
	  public void setTasaIva(String tasaIva) {
	    this.tasaIva = tasaIva;
	  }
	  
	  public String getRegiones() {
	    return this.regiones;
	  }
	  
	  public void setRegiones(String regiones) {
	    this.regiones = regiones;
	  }
	  
	  public String getCategorias() {
	    return this.categorias;
	  }
	  
	  public void setCategorias(String categorias) {
	    this.categorias = categorias;
	  }
	  
	  public String getFormaPago() {
	    return this.formaPago;
	  }
	  
	  public void setFormaPago(String formaPago) {
	    this.formaPago = formaPago;
	  }
	  
	  public String getFileList() {
	    return this.fileList;
	  }
	  
	  public void setFileList(String fileList) {
	    this.fileList = fileList;
	  }
	  
	  public int getSteps() {
	    return this.steps;
	  }
	  
	  public void setSteps(int steps) {
	    this.steps = steps;
	  }
	  
	  public Long getTicketId() {
	    return this.ticketId;
	  }
	  
	  public void setTicketId(Long ticketId) {
	    this.ticketId = ticketId;
	  }
	  
	  public boolean isAcceptOpenOrder() {
	    return this.acceptOpenOrder;
	  }
	  
	  public void setAcceptOpenOrder(boolean acceptOpenOrder) {
	    this.acceptOpenOrder = acceptOpenOrder;
	  }
	  
	  public String getTaxRate() {
	    return this.taxRate;
	  }
	  
	  public void setTaxRate(String taxRate) {
	    this.taxRate = taxRate;
	  }
	  
	  public String getExplCode1() {
	    return this.explCode1;
	  }
	  
	  public void setExplCode1(String explCode1) {
	    this.explCode1 = explCode1;
	  }
	  
	  public String getInvException() {
	    return this.invException;
	  }
	  
	  public void setInvException(String invException) {
	    this.invException = invException;
	  }
	  
	  public String getPaymentMethod() {
	    return this.paymentMethod;
	  }
	  
	  public void setPaymentMethod(String paymentMethod) {
	    this.paymentMethod = paymentMethod;
	  }
	  
	  public String getSupplierType() {
	    return this.supplierType;
	  }
	  
	  public void setSupplierType(String supplierType) {
	    this.supplierType = supplierType;
	  }
	  
	  public String getAutomaticEmail() {
	    return this.automaticEmail;
	  }
	  
	  public void setAutomaticEmail(String automaticEmail) {
	    this.automaticEmail = automaticEmail;
	  }
	  
	  public String getCountry() {
	    return this.country;
	  }
	  
	  public void setCountry(String country) {
	    this.country = country;
	  }
	  
	  public String getEmailComprador() {
	    return this.emailComprador;
	  }
	  
	  public void setEmailComprador(String emailComprador) {
	    this.emailComprador = emailComprador;
	  }
	  
	  public String getCurrencyCode() {
	    return this.currencyCode;
	  }
	  
	  public void setCurrencyCode(String currencyCode) {
	    this.currencyCode = currencyCode;
	  }
	  
	  public String getFisicaMoral() {
	    return this.fisicaMoral;
	  }
	  
	  public void setFisicaMoral(String fisicaMoral) {
	    this.fisicaMoral = fisicaMoral;
	  }
	  
	  public String getTaxId() {
	    return this.taxId;
	  }
	  
	  public void setTaxId(String taxId) {
	    this.taxId = taxId;
	  }
	  
	  public String getBatchNumber() {
	    return this.batchNumber;
	  }
	  
	  public void setBatchNumber(String batchNumber) {
	    this.batchNumber = batchNumber;
	  }
	  
	  public String getWebSite() {
	    return this.webSite;
	  }
	  
	  public void setWebSite(String webSite) {
	    this.webSite = webSite;
	  }
	  
	  public String getFaxNumber() {
	    return this.faxNumber;
	  }
	  
	  public void setFaxNumber(String faxNumber) {
	    this.faxNumber = faxNumber;
	  }
	  
	  public String getLegalRepIdType() {
	    return this.legalRepIdType;
	  }
	  
	  public void setLegalRepIdType(String legalRepIdType) {
	    this.legalRepIdType = legalRepIdType;
	  }
	  
	  public String getLegalRepIdNumber() {
	    return this.legalRepIdNumber;
	  }
	  
	  public void setLegalRepIdNumber(String legalRepIdNumber) {
	    this.legalRepIdNumber = legalRepIdNumber;
	  }
	  
	  public String getLegalRepName() {
	    return this.legalRepName;
	  }
	  
	  public void setLegalRepName(String legalRepName) {
	    this.legalRepName = legalRepName;
	  }
	  
	  public String getBankTitleRepName() {
	    return this.bankTitleRepName;
	  }
	  
	  public void setBankTitleRepName(String bankTitleRepName) {
	    this.bankTitleRepName = bankTitleRepName;
	  }
	  
	  public String getBankName() {
	    return this.bankName;
	  }
	  
	  public void setBankName(String bankName) {
	    this.bankName = bankName;
	  }
	  
	  public String getBankAccountNumber() {
	    return this.bankAccountNumber;
	  }
	  
	  public void setBankAccountNumber(String bankAccountNumber) {
	    this.bankAccountNumber = bankAccountNumber;
	  }
	  
	  public String getSwiftCode() {
	    return this.swiftCode;
	  }
	  
	  public void setSwiftCode(String swiftCode) {
	    this.swiftCode = swiftCode;
	  }
	  
	  public String getIbanCode() {
	    return this.ibanCode;
	  }
	  
	  public void setIbanCode(String ibanCode) {
	    this.ibanCode = ibanCode;
	  }
	  
	  public String getBankTransitNumber() {
	    return this.bankTransitNumber;
	  }
	  
	  public void setBankTransitNumber(String bankTransitNumber) {
	    this.bankTransitNumber = bankTransitNumber;
	  }

	  public int getUkuid() {
			return ukuid;
	  }
	
	  public void setUkuid(int ukuid) {
			this.ukuid = ukuid;
	  }

	public boolean isOutSourcing() {
		return outSourcing;
	}

	public void setOutSourcing(boolean outSourcing) {
		this.outSourcing = outSourcing;
	}

	public boolean isOutSourcingAccept() {
		return outSourcingAccept;
	}

	public void setOutSourcingAccept(boolean outSourcingAccept) {
		this.outSourcingAccept = outSourcingAccept;
	}

	public Date getOutSourcingRecordDate() {
		return outSourcingRecordDate;
	}

	public void setOutSourcingRecordDate(Date outSourcingRecordDate) {
		this.outSourcingRecordDate = outSourcingRecordDate;
	}

	public boolean isOutSourcingMonthlyAccept() {
		return outSourcingMonthlyAccept;
	}

	public void setOutSourcingMonthlyAccept(boolean outSourcingMonthlyAccept) {
		this.outSourcingMonthlyAccept = outSourcingMonthlyAccept;
	}

	public String getReplicationStatus() {
		return replicationStatus;
	}

	public void setReplicationStatus(String replicationStatus) {
		this.replicationStatus = replicationStatus;
	}

	public boolean isOutSourcingQuarterlyAccept() {
		return outSourcingQuarterlyAccept;
	}

	public void setOutSourcingQuarterlyAccept(boolean outSourcingQuarterlyAccept) {
		this.outSourcingQuarterlyAccept = outSourcingQuarterlyAccept;
	}
	
	public Date getReplicationDate() {
		return replicationDate;
	}

	public void setReplicationDate(Date replicationDate) {
		this.replicationDate = replicationDate;
	}

	public String getReplicationMessage() {
		return replicationMessage;
	}

	public void setReplicationMessage(String replicationMessage) {
		this.replicationMessage = replicationMessage;
	}

	public String getCatCode01() {
		return catCode01;
	}

	public void setCatCode01(String catCode01) {
		this.catCode01 = catCode01;
	}

	public String getCatCode20() {
		return catCode20;
	}

	public void setCatCode20(String catCode20) {
		this.catCode20 = catCode20;
	}

	public String getCatCode23() {
		return catCode23;
	}

	public void setCatCode23(String catCode23) {
		this.catCode23 = catCode23;
	}

	public String getCatCode24() {
		return catCode24;
	}

	public void setCatCode24(String catCode24) {
		this.catCode24 = catCode24;
	}

	public String getRequisitosFiscales() {
		return requisitosFiscales;
	}

	public void setRequisitosFiscales(String requisitosFiscales) {
		this.requisitosFiscales = requisitosFiscales;
	}

	public String getIdFiscal() {
		return idFiscal;
	}

	public void setIdFiscal(String idFiscal) {
		this.idFiscal = idFiscal;
	}

	public String getUpdateApprovalFlow() {
		return updateApprovalFlow;
	}

	public void setUpdateApprovalFlow(String updateApprovalFlow) {
		this.updateApprovalFlow = updateApprovalFlow;
	}

	public String getDataUpdateList() {
		return dataUpdateList;
	}

	public void setDataUpdateList(String dataUpdateList) {
		this.dataUpdateList = dataUpdateList;
	}
	
	
	  
	}
