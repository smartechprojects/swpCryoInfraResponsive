package com.eurest.supplier.service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.edi.AddressBookDTO;
import com.eurest.supplier.edi.BatchJournalDTO;
import com.eurest.supplier.edi.BatchJournalEntryDTO;
import com.eurest.supplier.edi.BatchVoucherTransactionsDTO;
import com.eurest.supplier.edi.ReceivingAdviceDetailDTO;
import com.eurest.supplier.edi.ReceivingAdviceHeaderDTO;
import com.eurest.supplier.edi.SupplierJdeDTO;
import com.eurest.supplier.edi.VoucherDetailDTO;
import com.eurest.supplier.edi.VoucherHeaderDTO;
import com.eurest.supplier.edi.VoucherSummaryDTO;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.model.CustomBrokerConcept;
import com.eurest.supplier.model.NextNumber;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.JdeJavaJulianDateTools;
import com.eurest.supplier.util.StringUtils;

@Service("ediService")
public class EDIService {
  @Autowired
  private DocumentsService documentsService;
  
  @Autowired
  private SupplierService supplierService;
  
  @Autowired
  private UsersService usersService;
  
  @Autowired
  private UdcService udcService;
  
  @Autowired
  JDERestService jDERestService;
  
  @Autowired
  NextNumberService nextNumberService;
  
  @Autowired
  EmailService emailService;
    
  @Autowired
  StringUtils stringUtils;
  
  @Autowired
  UDCDao udcDao;
  
  private Logger log4j = Logger.getLogger(EDIService.class);
  
  public boolean createNewAddressBook(Supplier s) {
    Random rnd = new Random();
    int n = 100000 + rnd.nextInt(900000);
    Supplier sup = this.supplierService.getSupplierById(s.getId());
    sup.setAddresNumber(String.valueOf(n));
    this.supplierService.updateSupplier(sup);
    List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
    for (UserDocument o : list)
      o.setAddressBook(String.valueOf(n)); 
    this.documentsService.updateDocumentList(list);
    Users usr = new Users();
    usr.setUserName(String.valueOf(n));
    usr.setEnabled(true);
    usr.setEmail(s.getEmailSupplier());
    usr.setName(s.getRazonSocial());
    UDC userRole = this.udcService.searchBySystemAndKey("ROLES", "SUPPLIER");
    usr.setRole(userRole.getStrValue1());
    usr.setUserRole(userRole);
    UDC userType = this.udcService.searchBySystemAndKey("USERTYPE", "SUPPLIER");
    usr.setUserType(userType);
    String tempPass = "NuevoProv.29";
    String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
    usr.setPassword(encodePass);
    this.usersService.save(usr, new Date(), "EDI");
    return true;
  }
  
  public Supplier registerNewAddressBook(Supplier s) {
	  
	  NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
	  int nextNbr = nn.getNexInt();
	  String nextBatch = nn.getNextStr();
	  int nextAddresNumber = nn.getNextAddNum();
	  int batchNbr = Integer.valueOf(nextBatch).intValue();
	  s.setBatchNumber(String.valueOf(batchNbr));
	    
	  if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
			s.setAddresNumber(Integer.toString(nextAddresNumber));
			s.setUkuid(nextNbr);
			nextNbr++;
			nextAddresNumber++;
		}
	  
	  batchNbr++;
	  nn.setNexInt(nextNbr);
	  nn.setNextStr(String.valueOf(batchNbr));
	  nn.setNextAddNum(nextAddresNumber);
	  this.nextNumberService.updateNextNumber(nn);
		
	    /*
	    SupplierJdeDTO sdto = new SupplierJdeDTO();
    
    if(s.getRazonSocial() != null && s.getRazonSocial().length() > 40) {
    	s.setRazonSocial(s.getRazonSocial().substring(0, 40));
    }
    
    if(s.getCalleNumero() != null && s.getCalleNumero().length() > 40) {
    	s.setCalleNumero(s.getCalleNumero().substring(0, 40));
    }
    
    sdto.setAddresNumber(s.getAddresNumber());
    sdto.setEmail(s.getEmail());
    sdto.setCurrentApprover(s.getCurrentApprover());
    sdto.setNextApprover(s.getNextApprover());
    sdto.setApprovalStatus(s.getApprovalStatus());
    sdto.setApprovalStep(s.getApprovalStep());
    sdto.setSteps(s.getSteps());
    sdto.setRejectNotes(s.getRejectNotes());
    sdto.setApprovalNotes(s.getApprovalNotes());
    sdto.setRegiones(s.getRegiones());
    sdto.setCategorias(s.getCategorias());
    sdto.setCategoriaJDE(s.getCategoriaJDE());
    sdto.setCuentaBancaria(s.getCuentaBancaria());
    sdto.setDiasCredito(s.getDiasCredito());
    sdto.setTipoMovimiento(s.getTipoMovimiento());
    sdto.setCompradorAsignado(s.getCompradorAsignado());
    sdto.setName(s.getName());
    sdto.setRazonSocial(s.getRazonSocial());
    sdto.setGiroEmpresa(s.getGiroEmpresa());
    sdto.setRfc(s.getRfc());
    sdto.setEmailSupplier(s.getEmailSupplier());
    sdto.setIndustryType(s.getIndustryType());
    sdto.setCalleNumero(s.getCalleNumero());
    sdto.setColonia(s.getColonia());
    sdto.setCodigoPostal(s.getCodigoPostal());
    sdto.setDelegacionMnicipio(s.getDelegacionMnicipio());
    sdto.setEstado(s.getEstado());
    sdto.setCountry(s.getCountry());
    sdto.setWebSite(s.getWebSite());
    sdto.setTelefonoDF(s.getTelefonoDF());
    sdto.setFaxDF(s.getFaxDF());
    sdto.setNombreContactoCxC(s.getNombreContactoCxC());
    sdto.setEmailContactoCxC(s.getEmailContactoCxC());
    sdto.setApellidoPaternoCxC(s.getApellidoPaternoCxC());
    sdto.setApellidoMaternoCxC(s.getApellidoMaternoCxC());
    sdto.setTelefonoContactoCxC(s.getTelefonoContactoCxC());
    sdto.setFaxCxC(s.getFaxCxC());
    sdto.setCargoCxC(s.getCargoCxC());
    sdto.setSearchType(s.getSearchType());
    sdto.setCreditMessage(s.getCreditMessage());
    sdto.setTaxAreaCxC(s.getTaxAreaCxC());
    sdto.setTaxExpl2CxC(s.getTaxExpl2CxC());
    sdto.setPmtTrmCxC(s.getPmtTrmCxC());
    sdto.setPayInstCxC(s.getPayInstCxC());
    sdto.setCurrCodeCxC(s.getCurrCodeCxC());
    sdto.setCatCode15(s.getCatCode15());
    sdto.setCatCode27(s.getCatCode27());
    sdto.setEmailCxP01(s.getEmailCxP01());
    sdto.setNombreCxP01(s.getNombreCxP01());
    sdto.setTelefonoCxP01(s.getTelefonoCxP01());
    sdto.setEmailCxP02(s.getEmailCxP02());
    sdto.setNombreCxP02(s.getNombreCxP02());
    sdto.setTelefonoCxP02(s.getTelefonoCxP02());
    sdto.setEmailCxP03(s.getEmailCxP03());
    sdto.setNombreCxP03(s.getNombreCxP03());
    sdto.setTelefonoCxP03(s.getTelefonoCxP03());
    sdto.setEmailCxP04(s.getEmailCxP04());
    sdto.setNombreCxP04(s.getNombreCxP04());
    sdto.setTelefonoCxP04(s.getTelefonoCxP04());
    sdto.setTipoIdentificacion(s.getTipoIdentificacion());
    sdto.setNumeroIdentificacion(s.getNumeroIdentificacion());
    sdto.setNombreRL(s.getNombreRL());
    sdto.setApellidoPaternoRL(s.getApellidoPaternoRL());
    sdto.setApellidoMaternoRL(s.getApellidoMaternoRL());
    sdto.setBankTitleRepName(s.getBankTitleRepName());
    sdto.setBankName(s.getBankName());
    sdto.setCuentaClabe(s.getCuentaClabe());
    sdto.setBankAccountNumber(s.getBankAccountNumber());
    sdto.setSwiftCode(s.getSwiftCode());
    sdto.setIbanCode(s.getIbanCode());
    sdto.setGlClass(s.getGlClass());
    sdto.setBankTransitNumber(s.getBankTransitNumber());
    sdto.setCustBankAcct(s.getCustBankAcct());
    sdto.setControlDigit(s.getControlDigit());
    sdto.setDescription(s.getDescription());
    sdto.setCheckingOrSavingAccount(s.getCheckingOrSavingAccount());
    sdto.setRollNumber(s.getRollNumber());
    sdto.setBankAddressNumber(s.getBankAddressNumber());
    sdto.setBankCountryCode(s.getBankCountryCode());
    sdto.setNombreBanco(s.getNombreBanco());
    sdto.setFormaPago(s.getFormaPago());
    sdto.setNombreContactoPedidos(s.getNombreContactoPedidos());
    sdto.setEmailContactoPedidos(s.getEmailSupplier());
    sdto.setTelefonoContactoPedidos(s.getTelefonoContactoPedidos());
    sdto.setNombreContactoVentas(s.getNombreContactoVentas());
    sdto.setEmailContactoVentas(s.getEmailContactoVentas());
    sdto.setTelefonoContactoVentas(s.getTelefonoContactoVentas());
    sdto.setNombreContactoCalidad(s.getNombreContactoCalidad());
    sdto.setEmailContactoCalidad(s.getEmailContactoCalidad());
    sdto.setTelefonoContactoCalidad(s.getTelefonoContactoCalidad());
    sdto.setPuestoCalidad(s.getPuestoCalidad());
    sdto.setDireccionPlanta(s.getDireccionPlanta());
    sdto.setDireccionCentroDistribucion(s.getDireccionCentroDistribucion());
    sdto.setTipoProductoServicio(s.getTipoProductoServicio());
    sdto.setRiesgoCategoria(s.getRiesgoCategoria());
    sdto.setObservaciones(s.getObservaciones());
    sdto.setBatchNumber(s.getBatchNumber());
    sdto.setDiasCreditoActual(s.getDiasCreditoActual());
    sdto.setDiasCreditoAnterior(s.getDiasCreditoAnterior());
    sdto.setTasaIva(s.getTasaIva());
    sdto.setTaxRate(s.getTaxRate());
    sdto.setExplCode1(s.getExplCode1());
    sdto.setInvException(s.getInvException());
    sdto.setPaymentMethod(s.getPaymentMethod());
    sdto.setSupplierType(s.getSupplierType());
    sdto.setAutomaticEmail(s.getAutomaticEmail());
    sdto.setFileList("");
    sdto.setEmailComprador(s.getEmailComprador());
    sdto.setCurrencyCode(s.getCurrencyCode());
    sdto.setFisicaMoral(s.getFisicaMoral());
    sdto.setTaxId(s.getTaxId());
    sdto.setFaxNumber(s.getFaxNumber());
    sdto.setLegalRepIdType(s.getLegalRepIdType());
    sdto.setLegalRepIdNumber(s.getLegalRepIdNumber());
    sdto.setLegalRepName(s.getLegalRepName());
    sdto.setIndustryClass(s.getIndustryClass());
    sdto.setUkuid(s.getUkuid());
    sdto.setHold(s.getHold());

    sdto = this.jDERestService.sendAddressBook(sdto);
    
    if(sdto != null) {
    	s.setAddresNumber(sdto.getAddresNumber());
    return s;
    }
    return null;*/
	    
	return s;
  }
  
 public SupplierJdeDTO registerNewAddressBookMdw(Supplier s) {
	  
	 /* NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
	  int nextNbr = nn.getNexInt();
	  String nextBatch = nn.getNextStr();
	  int batchNbr = Integer.valueOf(nextBatch).intValue();
	  s.setBatchNumber(String.valueOf(batchNbr));
	    
	  if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
		s.setAddresNumber(null);
		s.setUkuid(nextNbr);
		nextNbr++;
		}
	    batchNbr++;
	    nn.setNexInt(nextNbr);
	    nn.setNextStr(String.valueOf(batchNbr));
	    this.nextNumberService.updateNextNumber(nn);*/
		
	    SupplierJdeDTO sdto = new SupplierJdeDTO();
    
    if(s.getRazonSocial() != null && s.getRazonSocial().length() > 40) {
    	s.setRazonSocial(s.getRazonSocial().substring(0, 40));
    }
    
    if(s.getCalleNumero() != null && s.getCalleNumero().length() > 40) {
    	s.setCalleNumero(s.getCalleNumero().substring(0, 40));
    }
    
    sdto.setId(s.getId());
    sdto.setAddresNumber(s.getAddresNumber());
    sdto.setEmail(s.getEmail());
    sdto.setCurrentApprover(s.getCurrentApprover());
    sdto.setNextApprover(s.getNextApprover());
    sdto.setApprovalStatus(s.getApprovalStatus());
    sdto.setApprovalStep(s.getApprovalStep());
    sdto.setSteps(s.getSteps());
    sdto.setRejectNotes(s.getRejectNotes());
    sdto.setApprovalNotes(s.getApprovalNotes());
    sdto.setRegiones(s.getRegiones());
    sdto.setCategorias(s.getCategorias());
    sdto.setCategoriaJDE(s.getCategoriaJDE());
    sdto.setCuentaBancaria(s.getCuentaBancaria());
    sdto.setDiasCredito(s.getDiasCredito());
    sdto.setTipoMovimiento(s.getTipoMovimiento());
    sdto.setCompradorAsignado(s.getCompradorAsignado());
    sdto.setName(s.getName());
    sdto.setRazonSocial(s.getRazonSocial());
    sdto.setGiroEmpresa(s.getGiroEmpresa());
    sdto.setRfc(s.getRfc());
    sdto.setEmailSupplier(s.getEmailSupplier());
    sdto.setIndustryType(s.getIndustryType());
    sdto.setCalleNumero(s.getCalleNumero());
    sdto.setColonia(s.getColonia());
    sdto.setCodigoPostal(s.getCodigoPostal());
    sdto.setDelegacionMnicipio(s.getDelegacionMnicipio());
    sdto.setEstado(s.getEstado());
    sdto.setCountry(s.getCountry());
    sdto.setWebSite(s.getWebSite());
    sdto.setTelefonoDF(s.getTelefonoDF());
    sdto.setFaxDF(s.getFaxDF());
    sdto.setNombreContactoCxC(s.getNombreContactoCxC());
    sdto.setEmailContactoCxC(s.getEmailContactoCxC());
    sdto.setApellidoPaternoCxC(s.getApellidoPaternoCxC());
    sdto.setApellidoMaternoCxC(s.getApellidoMaternoCxC());
    sdto.setTelefonoContactoCxC(s.getTelefonoContactoCxC());
    sdto.setFaxCxC(s.getFaxCxC());
    sdto.setCargoCxC(s.getCargoCxC());
    sdto.setSearchType(s.getSearchType());
    sdto.setCreditMessage(s.getCreditMessage());
    sdto.setTaxAreaCxC(s.getTaxAreaCxC());
    sdto.setTaxExpl2CxC(s.getTaxExpl2CxC());
    sdto.setPmtTrmCxC(s.getPmtTrmCxC());
    sdto.setPayInstCxC(s.getPayInstCxC());
    sdto.setCurrCodeCxC(s.getCurrCodeCxC());
    sdto.setCatCode15(s.getCatCode15());
    sdto.setCatCode27(s.getCatCode27());
    sdto.setEmailCxP01(s.getEmailCxP01());
    sdto.setNombreCxP01(s.getNombreCxP01());
    sdto.setTelefonoCxP01(s.getTelefonoCxP01());
    sdto.setEmailCxP02(s.getEmailCxP02());
    sdto.setNombreCxP02(s.getNombreCxP02());
    sdto.setTelefonoCxP02(s.getTelefonoCxP02());
    sdto.setEmailCxP03(s.getEmailCxP03());
    sdto.setNombreCxP03(s.getNombreCxP03());
    sdto.setTelefonoCxP03(s.getTelefonoCxP03());
    sdto.setEmailCxP04(s.getEmailCxP04());
    sdto.setNombreCxP04(s.getNombreCxP04());
    sdto.setTelefonoCxP04(s.getTelefonoCxP04());
    sdto.setTipoIdentificacion(s.getTipoIdentificacion());
    sdto.setNumeroIdentificacion(s.getNumeroIdentificacion());
    sdto.setNombreRL(s.getNombreRL());
    sdto.setApellidoPaternoRL(s.getApellidoPaternoRL());
    sdto.setApellidoMaternoRL(s.getApellidoMaternoRL());
    sdto.setBankTitleRepName(s.getBankTitleRepName());
    sdto.setBankName(s.getBankName());
    sdto.setCuentaClabe(s.getCuentaClabe());
    sdto.setBankAccountNumber(s.getBankAccountNumber());
    sdto.setSwiftCode(s.getSwiftCode());
    sdto.setIbanCode(s.getIbanCode());
    sdto.setGlClass(s.getGlClass());
    sdto.setBankTransitNumber(s.getBankTransitNumber());
    sdto.setCustBankAcct(s.getCustBankAcct());
    sdto.setControlDigit(s.getControlDigit());
    sdto.setDescription(s.getDescription());
    sdto.setCheckingOrSavingAccount(s.getCheckingOrSavingAccount());
    sdto.setRollNumber(s.getRollNumber());
    sdto.setBankAddressNumber(s.getBankAddressNumber());
    sdto.setBankCountryCode(s.getBankCountryCode());
    sdto.setNombreBanco(s.getNombreBanco());
    sdto.setFormaPago(s.getFormaPago());
    sdto.setNombreContactoPedidos(s.getNombreContactoPedidos());
    sdto.setEmailContactoPedidos(s.getEmailSupplier());
    sdto.setTelefonoContactoPedidos(s.getTelefonoContactoPedidos());
    sdto.setNombreContactoVentas(s.getNombreContactoVentas());
    sdto.setEmailContactoVentas(s.getEmailContactoVentas());
    sdto.setTelefonoContactoVentas(s.getTelefonoContactoVentas());
    sdto.setNombreContactoCalidad(s.getNombreContactoCalidad());
    sdto.setEmailContactoCalidad(s.getEmailContactoCalidad());
    sdto.setTelefonoContactoCalidad(s.getTelefonoContactoCalidad());
    sdto.setPuestoCalidad(s.getPuestoCalidad());
    sdto.setDireccionPlanta(s.getDireccionPlanta());
    sdto.setDireccionCentroDistribucion(s.getDireccionCentroDistribucion());
    sdto.setTipoProductoServicio(s.getTipoProductoServicio());
    sdto.setRiesgoCategoria(s.getRiesgoCategoria());
    sdto.setObservaciones(s.getObservaciones());
    sdto.setBatchNumber(s.getBatchNumber());
    sdto.setDiasCreditoActual(s.getDiasCreditoActual());
    sdto.setDiasCreditoAnterior(s.getDiasCreditoAnterior());
    sdto.setTasaIva(s.getTasaIva());
    sdto.setTaxRate(s.getTaxRate());
    sdto.setExplCode1(s.getExplCode1());
    sdto.setInvException(s.getInvException());
    sdto.setPaymentMethod(s.getPaymentMethod());
    sdto.setSupplierType(s.getSupplierType());
    sdto.setAutomaticEmail(s.getAutomaticEmail());
    sdto.setFileList("");
    sdto.setEmailComprador(s.getEmailComprador());
    sdto.setCurrencyCode(s.getCurrencyCode());
    sdto.setFisicaMoral(s.getFisicaMoral());
    sdto.setTaxId(s.getTaxId());
    sdto.setFaxNumber(s.getFaxNumber());
    sdto.setLegalRepIdType(s.getLegalRepIdType());
    sdto.setLegalRepIdNumber(s.getLegalRepIdNumber());
    sdto.setLegalRepName(s.getLegalRepName());
    sdto.setIndustryClass(s.getIndustryClass());
    sdto.setUkuid(s.getUkuid());
    sdto.setHold(s.getHold());
    sdto.setCatCode01(s.getCatCode01());
    sdto.setCatCode20(s.getCatCode20());
    sdto.setCatCode23(s.getCatCode23());
    sdto.setCatCode24(s.getCatCode24());
    sdto.setRequisitosFiscales(s.getRequisitosFiscales());
    sdto.setIdFiscal(s.getIdFiscal());
    sdto.setDataUpdateList(s.getDataUpdateList());
    sdto.setUpdateApprovalFlow(s.getUpdateApprovalFlow());
   // sdto = this.jDERestService.sendAddressBook(sdto);
    
 
    return sdto;
  }
  
  public synchronized boolean createNewReceipt(PurchaseOrder o) {
    ReceivingAdviceHeaderDTO recHdr = new ReceivingAdviceHeaderDTO();
    NextNumber nn = this.nextNumberService.getNextNumber("RECEIPT");
    int nextNbr = nn.getNexInt();
    recHdr.setSYEDOC(nextNbr);
    recHdr.setSYEKCO(o.getOrderCompany());
    recHdr.setSYDOCO(o.getOrderNumber());
    recHdr.setSYDCTO(o.getOrderType());
    recHdr.setSYEDCT(o.getOrderType());
    recHdr.setSYKCOO(o.getOrderCompany());
    recHdr.setSYSFXO(o.getOrderSuffix());
    recHdr.setSYAN8(Integer.valueOf(o.getAddressNumber()).intValue());
    recHdr.setSYSHAN(Integer.valueOf(o.getShipTo()).intValue());
    recHdr.setSYMCU(o.getBusinessUnit());
    List<ReceivingAdviceDetailDTO> dtlList = new ArrayList<>();
    Set<PurchaseOrderDetail> oDtl = o.getPurchaseOrderDetail();
    int lineNumber = 1;
    int numOfLines = 0;
    boolean isValid = false;
    for (PurchaseOrderDetail d : oDtl) {
      isValid = true;
      ReceivingAdviceDetailDTO dto = new ReceivingAdviceDetailDTO();
      dto.setSZEDOC(nextNbr);
      dto.setSZEDCT(o.getOrderType());
      dto.setSZEKCO(o.getOrderCompany());
      dto.setSZEDLN((lineNumber * 1000));
      dto.setSZDOCO(o.getOrderNumber());
      dto.setSZDCTO(o.getOrderType());
      dto.setSZKCOO(o.getCompanyKey());
      dto.setSZSFXO(o.getOrderSuffix());
      dto.setSZLSTS("1");
      dto.setSZLNID((d.getLineNumber() * 1000.0D));
      dto.setSZLITM(d.getAltItemNumber().trim());
      dto.setSZUREC(d.getToReceive() * 100.0D);
      dto.setSZAREC(d.getToReceive() * d.getUnitCost() * 10000.0D);
      dto.setSZPRRC(d.getUnitCost() * 10000.0D);
      dto.setSZUORG(d.getQuantity() * 100.0D);
      dto.setSZUOM(d.getUom());
      dto.setSZAN8(Integer.valueOf(o.getAddressNumber()).intValue());
      dto.setSZSHAN(Integer.valueOf(o.getShipTo()).intValue());
      dto.setSZMCU(o.getBusinessUnit());
      dtlList.add(dto);
      lineNumber++;
      numOfLines++;
    } 
    if (isValid) {
      recHdr.setSYEDDL(numOfLines);
      recHdr.setReceivingAdviceDetailDTO(dtlList);
      nextNbr++;
      nn.setNexInt(nextNbr);
      this.nextNumberService.updateNextNumber(nn);
      this.jDERestService.sendReceivingAdvice(recHdr);
    } 
    return true;
  }
  
  public synchronized String createNewVoucher(PurchaseOrder o, InvoiceDTO inv, int nextNbr, Supplier s, List<Receipt> receipts, String nextNumberType){		
		VoucherHeaderDTO recHdr = new VoucherHeaderDTO();
		NextNumber nn = null;
		int julianEPD = 0;
		
		if(nextNbr == 0) {
			nn = nextNumberService.getNextNumber(nextNumberType);
			nextNbr = nn.getNexInt();
			int nextNbrUpd = nextNbr + 1;
			nn.setNexInt(nextNbrUpd);
			nextNumberService.updateNextNumber(nn);	
		}
		
		if(AppConstants.NC_TC.equals(inv.getTipoComprobante())) {
			inv.setSubTotal(inv.getSubTotal() * -1);
		}
		
		if(o.getEstimatedPaymentDate() != null) {
			julianEPD =Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
		}
		
		recHdr.setSYEDOC(nextNbr);
		recHdr.setSYEKCO(o.getOrderCompany());
		recHdr.setSYEDCT("PV");
		recHdr.setSYEDLN(0);
		recHdr.setSYDOCO(o.getOrderNumber());
		recHdr.setSYDCTO(o.getOrderType());
		recHdr.setSYKCOO(o.getOrderCompany());
		recHdr.setSYSFXO(o.getOrderSuffix());
		recHdr.setSYAN8(Integer.valueOf(o.getAddressNumber()));
		recHdr.setSYSHAN(Integer.valueOf(o.getShipTo()));
		recHdr.setSYMCU(o.getBusinessUnit());
		recHdr.setCRRM(o.getCurrencyMode());
		recHdr.setSYCRCD(o.getCurrecyCode());
		recHdr.setSYCRR(o.getExchangeRate());		
		recHdr.setSYVR01(inv.getUuid().substring(0, 25));
		recHdr.setSYURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
		recHdr.setSYDDU(julianEPD);
		recHdr.setSYDDJ(julianEPD);
		
		String folio = "";
		log4j.info("****** Folio:" + inv.getFolio());
		if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
			folio = inv.getFolio();
		}
		
		String serie = "";
		log4j.info("****** Serie:" + inv.getSerie());
		if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
			serie = inv.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
		}
		
		//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
		if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
			serie = "ZX";
			folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
		}
		
		String vinv = "";
		vinv = serie + folio;
		
		//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
		if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
			vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
		}
		
		//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
		if(vinv.length() > 25) {
			vinv = vinv.substring(0, 25);
		}
		
		log4j.info("****** Vinv:" + vinv);
		
		recHdr.setSYVINV(vinv);
		
		String divj = inv.getFechaTimbrado();
		divj = divj.replace("T", " ");
        String resultDate = null;
		 SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = null;
	        try {
	            date = fmt.parse(divj);
	            resultDate = JdeJavaJulianDateTools.Methods.getJulianDate(date);
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
	        
		recHdr.setSYDIVJ(resultDate);

		List<VoucherDetailDTO> dtlList = new ArrayList<VoucherDetailDTO>();
		int lineNumber = 1;
		double qtyOpen = 0;
		double totalAmount = 0;
		double openAmount = 0;
		
		for(Receipt d : receipts) {	
			if(d.getAmountReceived() != 0) {
				
				VoucherDetailDTO dto = new VoucherDetailDTO();
				dto.setSZEDOC(nextNbr);
				dto.setSZEDCT(o.getOrderType());
				dto.setSZEKCO(o.getOrderCompany());
				dto.setSZEDLN(lineNumber);
				dto.setSZLNTY(d.getLineType());
				
				dto.setSZDOCO(o.getOrderNumber());
				dto.setSZDCTO(o.getOrderType());
				dto.setSZKCOO(o.getCompanyKey());
				dto.setSZSFXO(o.getOrderSuffix());
				dto.setSZFRRC(0);
				dto.setSZACR(0);
				dto.setSZFAP(0);
				
				dto.setSZLNID(d.getLineNumber() * 1000);
				dto.setSZLITM(d.getItemNumber());
				dto.setSZAN8(Integer.valueOf(o.getAddressNumber()));
				dto.setSZMCU(o.getBusinessUnit());
				
				dto.setSZVINV("");
				dto.setSZAID(d.getAccountId());
				dto.setCRRM(o.getCurrencyMode());
				dto.setSZCRCD(o.getCurrecyCode());
								
				dto.setSZUOM(d.getUom());
				dto.setSZUPRC(d.getUnitCost() * 10000);
				dto.setSZAEXP(0);
				dto.setURAB(d.getReceiptLine());				
				dto.setSZUOPN(d.getQuantityReceived());
				qtyOpen = qtyOpen + dto.getSZUOPN();
								
				BigDecimal bd = new BigDecimal(d.getAmountReceived()*100).setScale(2, RoundingMode.HALF_EVEN);
				double detailAmount = bd.doubleValue();
				//float detailAmount = Math.round(d.getAmountReceived()*100); //Fix para cantidades grandes
				dto.setSZAG(detailAmount);
				dto.setSZAAP(detailAmount);
				totalAmount = totalAmount + detailAmount;
				
				openAmount = openAmount + dto.getSZAAP();
				dtlList.add(dto);
				lineNumber = lineNumber + 1;
				dto.setSZDDJ(julianEPD);
			}
		}

		log4j.info("Total: " + totalAmount);
		recHdr.setVoucherDetailDTO(dtlList);
		VoucherSummaryDTO vs = new VoucherSummaryDTO();
		vs.setSWEKCO(o.getOrderCompany());
		vs.setSWEDOC(nextNbr);
		vs.setSWKCO(o.getOrderCompany());
		vs.setSWKCOO(o.getOrderCompany());
		vs.setSWDOCO(o.getOrderNumber());
		vs.setSWDCTO(o.getOrderType());
		vs.setSWSFXO(o.getOrderSuffix());
		vs.setSWAN8(Integer.valueOf(o.getAddressNumber()));
		vs.setSWVINV(vinv);
		vs.setSWUOPN(0);
		vs.setSWAG(totalAmount);
		vs.setSWAAP(totalAmount);
		vs.setCRRM(o.getCurrencyMode());
		recHdr.setVoucherSummaryDTO(vs);

		String resp = jDERestService.sendVoucher(recHdr);
		return resp;
	}
  
  public synchronized String createNewForeignVoucher(PurchaseOrder o, ForeingInvoice inv, int nextNbr, Supplier s, List<Receipt> receipts, String nextNumberType){
		
		VoucherHeaderDTO recHdr = new VoucherHeaderDTO();
		NextNumber nn = null;
		int julianEPD = 0;
		
		if(nextNbr == 0) {
			nn = nextNumberService.getNextNumber(nextNumberType);
			nextNbr = nn.getNexInt();				
			int nextNbrUpd = nextNbr + 1;
			nn.setNexInt(nextNbrUpd);
			nextNumberService.updateNextNumber(nn);	
		}
		
		if(o.getEstimatedPaymentDate() != null) {
			julianEPD =Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
		}
		
		recHdr.setSYEDOC(nextNbr);
		recHdr.setSYEKCO(o.getOrderCompany());
		recHdr.setSYEDCT("PV");
		recHdr.setSYEDLN(0);
		recHdr.setSYDOCO(o.getOrderNumber());
		recHdr.setSYDCTO(o.getOrderType());
		recHdr.setSYKCOO(o.getOrderCompany());
		recHdr.setSYSFXO(o.getOrderSuffix());
		recHdr.setSYAN8(Integer.valueOf(o.getAddressNumber()));
		recHdr.setSYSHAN(Integer.valueOf(o.getShipTo()));
		recHdr.setSYMCU(o.getBusinessUnit());
		recHdr.setCRRM(o.getCurrencyMode());
		recHdr.setSYDDU(julianEPD);
		recHdr.setSYDDJ(julianEPD);
		
		if(inv.getInvoiceNumber() != null && !"".equals(inv.getInvoiceNumber())) {
			inv.setFolio(inv.getInvoiceNumber());
			inv.setSerie("");
		}
		
		String folio = "";
		log4j.info("****** Folio:" + inv.getFolio());
		if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
			folio = inv.getFolio();
		}
		
		String serie = "";
		log4j.info("****** Serie:" + inv.getSerie());
		if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
			serie = inv.getSerie().replace("/", "_").concat("-");
		}
		
		//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
		if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
			serie = "ZX";
			folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
		}
		
		String vinv = "";
		vinv = serie + folio;
		
		//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
		if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
			vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
		}
		
		//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
		if(vinv.length() > 25) {
			vinv = vinv.substring(0, 25);
		}
		
		log4j.info("****** Vinv:" + vinv);
		
		recHdr.setSYVINV(vinv);		
		recHdr.setSYCRCD(o.getCurrecyCode());
		recHdr.setSYCRR(o.getExchangeRate());
		
		recHdr.setSYVR01(inv.getUuid().substring(0, 25));
		recHdr.setSYURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
		
		String divj = inv.getExpeditionDate();
		divj = divj.replace("T", " ");
        String resultDate = null;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = null;
	        try {
	            date = fmt.parse(divj);
	            resultDate = JdeJavaJulianDateTools.Methods.getJulianDate(date);
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
	        
		recHdr.setSYDIVJ(resultDate);

		List<VoucherDetailDTO> dtlList = new ArrayList<VoucherDetailDTO>();
		int lineNumber = 1;
		double qtyOpen = 0;
		double totalAmount = 0;
		double openAmount = 0;
		double exchRate = 0;

		for(Receipt d : receipts) {
			
			if(d.getForeignAmountReceived() != 0) {
				VoucherDetailDTO dto = new VoucherDetailDTO();
	
				dto.setSZEDOC(nextNbr);
				dto.setSZEDCT(o.getOrderType());
				dto.setSZEKCO(o.getOrderCompany());
				dto.setSZEDLN(lineNumber);
				dto.setSZLNTY(d.getLineType());
				
				dto.setSZDOCO(o.getOrderNumber());
				dto.setSZDCTO(o.getOrderType());
				dto.setSZKCOO(o.getCompanyKey());
				dto.setSZSFXO(o.getOrderSuffix());
				dto.setSZLNID(d.getLineNumber() * 1000);
				dto.setSZLITM(d.getItemNumber());
				dto.setSZAN8(Integer.valueOf(o.getAddressNumber()));
				dto.setSZMCU(o.getBusinessUnit());
				dto.setSZVINV("");
				dto.setSZAID(d.getAccountId());
				dto.setCRRM(o.getCurrencyMode());
				dto.setSZCRCD(o.getCurrecyCode());
				
				dto.setSZUOM(d.getUom());
				dto.setSZUPRC(0);
				dto.setSZAEXP(0);
				dto.setURAB(d.getReceiptLine());
				
				if(d.getExchangeRate() == 0) {
					dto.setSZCRR(o.getExchangeRate());
				}else {
					dto.setSZCRR(d.getExchangeRate());
				}
				exchRate = dto.getSZCRR();
				
				dto.setSZFRRC(d.getForeignUnitCost());				
				dto.setSZAG(0);
				dto.setSZAAP(0);				
				dto.setSZUOPN(d.getQuantityReceived());
				qtyOpen = qtyOpen + dto.getSZUOPN();
								
				BigDecimal bd = new BigDecimal(d.getForeignAmountReceived()*100).setScale(2, RoundingMode.HALF_EVEN);
				double foreignAmountReceived = bd.doubleValue();
				//float foreignAmountReceived = Math.round(d.getForeignAmountReceived() * 100); //Fix para cantidades grandes
				dto.setSZACR(foreignAmountReceived);
				dto.setSZFAP(foreignAmountReceived);
				totalAmount = totalAmount + foreignAmountReceived;
				
				openAmount = openAmount + dto.getSZAAP();
				dtlList.add(dto);
				lineNumber = lineNumber + 1;
				dto.setSZDDJ(julianEPD);
			}
		}		
		recHdr.setVoucherDetailDTO(dtlList);
		
		VoucherSummaryDTO vs = new VoucherSummaryDTO();
		vs.setSWEKCO(o.getOrderCompany());
		vs.setSWEDOC(nextNbr);
		vs.setSWKCO(o.getOrderCompany());
		vs.setSWKCOO(o.getOrderCompany());
		vs.setSWDOCO(o.getOrderNumber());
		vs.setSWDCTO(o.getOrderType());
		vs.setSWSFXO(o.getOrderSuffix());
		vs.setSWAN8(Integer.valueOf(o.getAddressNumber()));
		vs.setSWVINV("");
		vs.setSWUOPN(0);
		vs.setSWACR(totalAmount);
		vs.setSWFAP(totalAmount);
		vs.setCRRM(o.getCurrencyMode());
		vs.setSWCRCD(o.getCurrecyCode());
		vs.setSWCRR(exchRate);
		vs.setSWAG(0);
		vs.setSWAAP(0);
		recHdr.setVoucherSummaryDTO(vs);
		
		String resp = jDERestService.sendVoucher(recHdr);
		return resp;
	}

  public synchronized String createNewBatchJournal(CustomBroker o, InvoiceDTO inv, int nextNbr, Supplier s, String nextNumberType){
	    BatchJournalDTO batchJournalDTO = new BatchJournalDTO();		
		NextNumber nn = null;
		int julianDateInvoiceUpload = 0;
		int julianDateInvoice = 0;
		int julianDatePayment = 0;
		int julianDateToday = 0;
		int julianDateTime = 0;
		double lineNumber = 1D;
		String supplierName = "";
		String advancePaymentMessage = "";
		String invoiceNumber = "";

		if(nextNbr == 0) {
			nn = nextNumberService.getNextNumber(nextNumberType);
			nextNbr = nn.getNexInt();
			int nextNbrUpd = nextNbr + 1;
			nn.setNexInt(nextNbrUpd);
			nextNumberService.updateNextNumber(nn);	
		}

		if(AppConstants.NC_TC.equals(inv.getTipoComprobante())) {
			inv.setSubTotal(inv.getSubTotal() * -1);
		}
		
		if(s.getName().length() > 30) {
			supplierName = s.getName().substring(0, 30);
		} else {
			supplierName = s.getName();
		}
		
		if(inv.getFechaTimbrado() != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String divj = inv.getFechaTimbrado();		
		    Date date = null;
		    
	        try {
	        	divj = divj.replace("T", " ");
	            date = fmt.parse(divj);
	            julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
		}

		if(o.getEstimatedPaymentDate() != null) {
	        try {
	            julianDatePayment = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
		}
		
		if(o.getInvoiceUploadDate() != null) {
			try {
				julianDateInvoiceUpload = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getInvoiceUploadDate()));
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}
		
		julianDateToday = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue();
		julianDateTime = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJDETimeStamp()).intValue();
		
		String folio = "";
		log4j.info("****** Folio:" + inv.getFolio());
		if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
			folio = inv.getFolio();
		}
		
		String serie = "";
		log4j.info("****** Serie:" + inv.getSerie());
		if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
			serie = inv.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
		}
		
		//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
		if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
			serie = "ZX";
			folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
		}
		
		String vinv = "";
		vinv = serie + folio;
		
		//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
		if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
			vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
		}
		
		//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
		if(vinv.length() > 25) {
			vinv = vinv.substring(0, 25);
		}
		
		log4j.info("****** Vinv:" + vinv);
		invoiceNumber = vinv;
		
		if(o.getAdvancePayment() > 0D) {
			advancePaymentMessage = "Anticipo: ".concat(String.format("%,.2f", o.getAdvancePayment())); 			
		}
		
		//Encabezado Batch Voucher Transactions (F0411Z1)
	    List<BatchVoucherTransactionsDTO> batchTransDTOList = new ArrayList<BatchVoucherTransactionsDTO>();	    	    
		BatchVoucherTransactionsDTO batchTransHdr = new BatchVoucherTransactionsDTO();
		batchTransHdr.setVLEDUS(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLEDTN("1");
		batchTransHdr.setVLEDLN(lineNumber*1000D);
		batchTransHdr.setVLEDER("B");
		batchTransHdr.setVLEDSP("0");
		batchTransHdr.setVLEDTC("A");
		batchTransHdr.setVLEDTR("V");
		batchTransHdr.setVLEDBT(String.valueOf(nextNbr));
		batchTransHdr.setVLEDDH("1");
		batchTransHdr.setVLKCO(o.getCompanyFD());
		batchTransHdr.setVLSFXE("0");
		batchTransHdr.setVLAN8(Integer.valueOf(o.getAddressNumber()).intValue());		
		batchTransHdr.setVLPYE(Integer.valueOf(o.getAddressNumber()).intValue());		
		batchTransHdr.setVLDDJ(julianDatePayment);
		batchTransHdr.setVLDDNJ(julianDatePayment);
		batchTransHdr.setVLDIVJ(julianDateInvoice);
		batchTransHdr.setVLDSVJ(julianDateToday);
		batchTransHdr.setVLDGJ(julianDateInvoiceUpload);
		batchTransHdr.setVLCTRY(20);
		batchTransHdr.setVLCO(o.getCompanyFD());		
		batchTransHdr.setVLICUT("V");
		batchTransHdr.setVLBALJ("Y");
		batchTransHdr.setVLPST("H");
		batchTransHdr.setVLAG(inv.getTotal()*100D);
		batchTransHdr.setVLAAP(inv.getTotal()*100D);
		batchTransHdr.setVLATXA(inv.getSubTotal()*100D);
		batchTransHdr.setVLSTAM(o.getImpuestos()*100D);
		batchTransHdr.setVLTXA1(o.getTaxCode());//TAX CODE
		batchTransHdr.setVLEXR1("V");
		batchTransHdr.setVLCRRM(o.getCurrencyMode());
		batchTransHdr.setVLCRCD(o.getCurrencyCode());
		batchTransHdr.setVLGLC(o.getGlOffset());
		batchTransHdr.setVLGLBA(o.getAccountingAccount());//8 CHAR
		batchTransHdr.setVLAM("2");//1-Short ID, 2-Long ID
		batchTransHdr.setVLMCU(o.getCentroCostos());
		batchTransHdr.setVLPTC(o.getPaymentTerms());
		batchTransHdr.setVLVINV(invoiceNumber);		
		batchTransHdr.setVLVR01(inv.getUuid().substring(0, 25));
		batchTransHdr.setVLURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
		batchTransHdr.setVLPYIN("T");
		batchTransHdr.setVLTORG(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLUSER(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLPID(AppConstants.PROGRAM_ID_ZP0411Z1);
		batchTransHdr.setVLUPMJ(julianDateToday);
		batchTransHdr.setVLUPMT(julianDateTime);
		batchTransHdr.setVLJOBN(AppConstants.WORK_STN_ID_COBOWB04);
		batchTransHdr.setVLRMK(advancePaymentMessage);		
		if(!"MXP".equals(o.getCurrencyCode())) {
			batchTransHdr.setVLAG(0D);
			batchTransHdr.setVLAAP(0D);
			batchTransHdr.setVLATXA(0D);
			batchTransHdr.setVLSTAM(0D);					
			batchTransHdr.setVLCRR(inv.getTipoCambio());
			batchTransHdr.setVLACR(inv.getTotal()*100D);
			batchTransHdr.setVLFAP(inv.getTotal()*100D);
			batchTransHdr.setVLCTXA(inv.getSubTotal()*100D);
			batchTransHdr.setVLCTAM(o.getImpuestos()*100D);					
		}		
		batchTransDTOList.add(batchTransHdr);

		//Encabezado Batch Journal Entry (F0911Z1)
		List<BatchJournalEntryDTO> batchJournalDTOList = new ArrayList<BatchJournalEntryDTO>();
		BatchJournalEntryDTO batchJournalHdr = new BatchJournalEntryDTO();		
		batchJournalHdr.setVNEDUS(AppConstants.USER_SMARTECH);
		batchJournalHdr.setVNEDTN("1");
		batchJournalHdr.setVNEDLN(lineNumber*1000D);
		batchJournalHdr.setVNEDER("B");
		batchJournalHdr.setVNEDSP("0");
		batchJournalHdr.setVNEDTC("A");
		batchJournalHdr.setVNEDTR("V");
		batchJournalHdr.setVNEDBT(String.valueOf(nextNbr));
		batchJournalHdr.setVNDGJ(julianDateInvoiceUpload);
		batchJournalHdr.setVNICUT("V");
		batchJournalHdr.setVNCO(o.getCompanyFD());
		batchJournalHdr.setVNANI(o.getAccountNumber());
		batchJournalHdr.setVNAM("2");
		batchJournalHdr.setVNAID("");
		batchJournalHdr.setVNMCU(o.getCentroCostos());
		batchJournalHdr.setVNLT("AA");
		batchJournalHdr.setVNCTRY(20);
		batchJournalHdr.setVNCRCD(o.getCurrencyCode());
		batchJournalHdr.setVNAA(inv.getSubTotal()*100D);
		batchJournalHdr.setVNEXA(supplierName);
		batchJournalHdr.setVNEXR(AppConstants.EXPLANATION_REMARK);
		batchJournalHdr.setVNAN8(Integer.valueOf(s.getAddresNumber()));
		batchJournalHdr.setVNVINV(invoiceNumber);
		batchJournalHdr.setVNIVD(julianDateInvoice);
		batchJournalHdr.setVNDSVJ(julianDateToday);
		batchJournalHdr.setVNUSER(AppConstants.USER_SMARTECH);
		batchJournalHdr.setVNPID(AppConstants.PROGRAM_ID_ZP0411Z1);
		batchJournalHdr.setVNJOBN(AppConstants.WORK_STN_ID_COBOWB04);
		batchJournalHdr.setVNUPMJ(julianDateToday);
		batchJournalHdr.setVNUPMT(julianDateTime);
		batchJournalHdr.setVNCRRM(o.getCurrencyMode());
		if(!"MXP".equals(o.getCurrencyCode())) {
			batchJournalHdr.setVNACR(inv.getSubTotal()*100D);
		}
		batchJournalDTOList.add(batchJournalHdr);
		
		//Líneas de Conceptos
		if(o.getConceptTotalAmount() > 0D) {
			String invoiceNumberDtl;
			for(CustomBrokerConcept concept : o.getConcepts()) {
				invoiceNumberDtl = "";
				lineNumber = lineNumber + 1D;
				
				julianDateInvoice = 0;
				if(concept.getInvoiceDate() != null && !concept.getInvoiceDate().isEmpty()) {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String divj = concept.getInvoiceDate();		
				    Date date = null;
				    
			        try {
			        	divj = divj.replace("T", " ");
			            date = fmt.parse(divj);
			            julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
			        } catch (Exception e) {
			        	log4j.error("Exception" , e);
			            e.printStackTrace();
			        }
				}
				folio = "";				
				if(concept.getFolio() != null && !"null".equals(concept.getFolio()) && !"NULL".equals(concept.getFolio()) ) {
					folio = concept.getFolio();
				}
				
				serie = "";
				if(concept.getSerie() != null && !"null".equals(concept.getSerie()) && !"NULL".equals(concept.getSerie()) ) {
					serie = concept.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
				}
				
				//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
				if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
					serie = "ZX";
					folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
				}
				
				vinv = "";
				vinv = serie + folio;
				
				//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
				if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
					vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
				}
				
				//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
				if(vinv.length() > 25) {
					vinv = vinv.substring(0, 25);
				}
				
				log4j.info("****** Vinv:" + vinv);				
				invoiceNumberDtl = vinv;
				
				if("".equals(invoiceNumberDtl) && !"".equals(invoiceNumber)) {
					invoiceNumberDtl = invoiceNumber.concat("-").concat(String.valueOf(Double.valueOf(lineNumber).intValue()));
				}
				log4j.info("****** Concept InvoiceNumber:" + invoiceNumberDtl);
				
				//Líneas de Batch Voucher Transactions (F0411Z1)
				BatchVoucherTransactionsDTO batchTransDtl = (BatchVoucherTransactionsDTO) SerializationUtils.clone(batchTransHdr);				
				batchTransDtl.setVLEDLN(lineNumber*1000D);
				batchTransDtl.setVLAG(concept.getAmount()*100D);
				batchTransDtl.setVLAAP(concept.getAmount()*100D);
				batchTransDtl.setVLATXA(concept.getSubtotal()*100D);
				batchTransDtl.setVLSTAM(concept.getImpuestos()*100D);
				batchTransDtl.setVLTXA1(concept.getTaxCode());
				batchTransDtl.setVLCRRM(concept.getCurrencyMode());
				batchTransDtl.setVLCRCD(concept.getCurrencyCode());
				batchTransDtl.setVLGLC(concept.getGlOffset());
				batchTransDtl.setVLGLBA(concept.getAccountingAccount());				
				batchTransDtl.setVLVR01(concept.getUuid().substring(0, 25));
				batchTransDtl.setVLURRF(concept.getUuid().substring(concept.getUuid().length() - 11));
				batchTransDtl.setVLDIVJ(julianDateInvoice);
				batchTransDtl.setVLVINV(invoiceNumberDtl);
				if(!"MXP".equals(concept.getCurrencyCode())) {
					batchTransDtl.setVLAG(0D);
					batchTransDtl.setVLAAP(0D);
					batchTransDtl.setVLATXA(0D);
					batchTransDtl.setVLSTAM(0D);
					batchTransDtl.setVLCRR(concept.getTipoCambio());
					batchTransDtl.setVLACR(concept.getAmount()*100D);
					batchTransDtl.setVLFAP(concept.getAmount()*100D);
					batchTransDtl.setVLCTXA(concept.getSubtotal()*100D);
					batchTransDtl.setVLCTAM(concept.getImpuestos()*100D);
				}
				batchTransDTOList.add(batchTransDtl);
				
				//Líneas de Batch Journal Entry (F0911Z1)
				BatchJournalEntryDTO batchJournalDtl = (BatchJournalEntryDTO) SerializationUtils.clone(batchJournalHdr);
				batchJournalDtl.setVNEDLN(lineNumber*1000D);
				batchJournalDtl.setVNANI(concept.getConceptAccount());				
				batchJournalDtl.setVNAA(concept.getSubtotal()*100D);
				batchJournalDtl.setVNCRCD(concept.getCurrencyCode());
				batchJournalDtl.setVNCRRM(concept.getCurrencyMode());
				batchJournalDtl.setVNIVD(julianDateInvoice);
				batchJournalDtl.setVNVINV(invoiceNumberDtl);
				if(!"MXP".equals(concept.getCurrencyCode())) {
					batchJournalDtl.setVNACR(concept.getAmount()*100D);
				}				
				batchJournalDTOList.add(batchJournalDtl);
			}
		}
		
		batchJournalDTO.setVoucherEntries(batchTransDTOList);
		batchJournalDTO.setJournalEntries(batchJournalDTOList);		
		String resp = jDERestService.sendJournalEntries(batchJournalDTO);
		return resp;
	}
	  
  public synchronized String createNewBatchJournal(FiscalDocuments o, InvoiceDTO inv, int nextNbr, Supplier s, String nextNumberType){
	    BatchJournalDTO batchJournalDTO = new BatchJournalDTO();		
		NextNumber nn = null;
		int julianDateInvoiceUpload = 0;
		int julianDateInvoice = 0;
		int julianDatePayment = 0;
		int julianDateToday = 0;
		int julianDateTime = 0;
		double lineNumber = 1D;
		String supplierName = "";
		String advancePaymentMessage = "";
		String invoiceNumber = "";

		if(nextNbr == 0) {
			nn = nextNumberService.getNextNumber(nextNumberType);
			nextNbr = nn.getNexInt();
			int nextNbrUpd = nextNbr + 1;
			nn.setNexInt(nextNbrUpd);
			nextNumberService.updateNextNumber(nn);	
		}

		if(AppConstants.NC_TC.equals(inv.getTipoComprobante())) {
			inv.setSubTotal(inv.getSubTotal() * -1);
		}
		
		if(s.getName().length() > 30) {
			supplierName = s.getName().substring(0, 30);
		} else {
			supplierName = s.getName();
		}
		
		if(inv.getFechaTimbrado() != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String divj = inv.getFechaTimbrado();		
		    Date date = null;
		    
	        try {
	        	divj = divj.replace("T", " ");
	            date = fmt.parse(divj);
	            julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
		}

		if(o.getEstimatedPaymentDate() != null) {
	        try {
	            julianDatePayment = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
		}
		
		if(o.getInvoiceUploadDate() != null) {
			try {
				julianDateInvoiceUpload = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getInvoiceUploadDate()));
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}
		
		julianDateToday = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue();
		julianDateTime = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJDETimeStamp()).intValue();
		
		String folio = "";
		log4j.info("****** Folio:" + inv.getFolio());
		if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
			folio = inv.getFolio();
		}
		
		String serie = "";
		log4j.info("****** Serie:" + inv.getSerie());
		if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
			serie = inv.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
		}
		
		//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
		if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
			serie = "ZX";
			folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
		}
		
		String vinv = "";
		vinv = serie + folio;
		
		//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
		if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
			vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
		}
		
		//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
		if(vinv.length() > 25) {
			vinv = vinv.substring(0, 25);
		}
		
		log4j.info("****** Vinv:" + vinv);
		invoiceNumber = vinv;
		
		if(o.getAdvancePayment() > 0D) {
			advancePaymentMessage = "Anticipo: ".concat(String.format("%,.2f", o.getAdvancePayment())); 			
		}
		
		//Encabezado Batch Voucher Transactions (F0411Z1)
	    List<BatchVoucherTransactionsDTO> batchTransDTOList = new ArrayList<BatchVoucherTransactionsDTO>();	    	    
		BatchVoucherTransactionsDTO batchTransHdr = new BatchVoucherTransactionsDTO();
		batchTransHdr.setVLEDUS(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLEDTN("1");
		batchTransHdr.setVLEDLN(lineNumber*1000D);
		batchTransHdr.setVLEDER("B");
		batchTransHdr.setVLEDSP("0");
		batchTransHdr.setVLEDTC("A");
		batchTransHdr.setVLEDTR("V");
		batchTransHdr.setVLEDBT(String.valueOf(nextNbr));
		batchTransHdr.setVLEDDH("1");
		batchTransHdr.setVLKCO(o.getCompanyFD());
		batchTransHdr.setVLSFXE("0");
		batchTransHdr.setVLAN8(Integer.valueOf(o.getAddressNumber()).intValue());		
		batchTransHdr.setVLPYE(Integer.valueOf(o.getAddressNumber()).intValue());		
		batchTransHdr.setVLDDJ(julianDatePayment);
		batchTransHdr.setVLDDNJ(julianDatePayment);
		batchTransHdr.setVLDIVJ(julianDateInvoice);
		batchTransHdr.setVLDSVJ(julianDateToday);
		batchTransHdr.setVLDGJ(julianDateInvoiceUpload);
		batchTransHdr.setVLCTRY(20);
		batchTransHdr.setVLCO(o.getCompanyFD());		
		batchTransHdr.setVLICUT("V");
		batchTransHdr.setVLBALJ("Y");
		batchTransHdr.setVLPST("H");
		batchTransHdr.setVLAG(inv.getTotal()*100D);
		batchTransHdr.setVLAAP(inv.getTotal()*100D);
		batchTransHdr.setVLATXA(inv.getSubTotal()*100D);
		batchTransHdr.setVLSTAM(o.getImpuestos()*100D);
		batchTransHdr.setVLTXA1(o.getTaxCode());//TAX CODE
		batchTransHdr.setVLEXR1("V");
		batchTransHdr.setVLCRRM(o.getCurrencyMode());
		batchTransHdr.setVLCRCD(o.getCurrencyCode());
		batchTransHdr.setVLGLC(o.getGlOffset());
		batchTransHdr.setVLGLBA(o.getAccountingAccount());//8 CHAR
		batchTransHdr.setVLAM("2");//1-Short ID, 2-Long ID
		batchTransHdr.setVLMCU(o.getCentroCostos());
		batchTransHdr.setVLPTC(o.getPaymentTerms());
		batchTransHdr.setVLVINV(invoiceNumber);		
		batchTransHdr.setVLVR01(inv.getUuid().substring(0, 25));
		batchTransHdr.setVLURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
		batchTransHdr.setVLPYIN("T");
		batchTransHdr.setVLTORG(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLUSER(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLPID(AppConstants.PROGRAM_ID_ZP0411Z1);
		batchTransHdr.setVLUPMJ(julianDateToday);
		batchTransHdr.setVLUPMT(julianDateTime);
		batchTransHdr.setVLJOBN(AppConstants.WORK_STN_ID_COBOWB04);
		batchTransHdr.setVLRMK(advancePaymentMessage);		
		if(!"MXP".equals(o.getCurrencyCode())) {
			batchTransHdr.setVLAG(0D);
			batchTransHdr.setVLAAP(0D);
			batchTransHdr.setVLATXA(0D);
			batchTransHdr.setVLSTAM(0D);					
			batchTransHdr.setVLCRR(inv.getTipoCambio());
			batchTransHdr.setVLACR(inv.getTotal()*100D);
			batchTransHdr.setVLFAP(inv.getTotal()*100D);
			batchTransHdr.setVLCTXA(inv.getSubTotal()*100D);
			batchTransHdr.setVLCTAM(o.getImpuestos()*100D);					
		}		
		batchTransDTOList.add(batchTransHdr);

		//Encabezado Batch Journal Entry (F0911Z1)
		List<BatchJournalEntryDTO> batchJournalDTOList = new ArrayList<BatchJournalEntryDTO>();
		BatchJournalEntryDTO batchJournalHdr = new BatchJournalEntryDTO();		
		batchJournalHdr.setVNEDUS(AppConstants.USER_SMARTECH);
		batchJournalHdr.setVNEDTN("1");
		batchJournalHdr.setVNEDLN(lineNumber*1000D);
		batchJournalHdr.setVNEDER("B");
		batchJournalHdr.setVNEDSP("0");
		batchJournalHdr.setVNEDTC("A");
		batchJournalHdr.setVNEDTR("V");
		batchJournalHdr.setVNEDBT(String.valueOf(nextNbr));
		batchJournalHdr.setVNDGJ(julianDateInvoiceUpload);
		batchJournalHdr.setVNICUT("V");
		batchJournalHdr.setVNCO(o.getCompanyFD());
		batchJournalHdr.setVNANI(o.getAccountNumber());
		batchJournalHdr.setVNAM("2");
		batchJournalHdr.setVNAID("");
		batchJournalHdr.setVNMCU(o.getCentroCostos());
		batchJournalHdr.setVNLT("AA");
		batchJournalHdr.setVNCTRY(20);
		batchJournalHdr.setVNCRCD(o.getCurrencyCode());
		batchJournalHdr.setVNAA(inv.getSubTotal()*100D);
		batchJournalHdr.setVNEXA(supplierName);
		batchJournalHdr.setVNEXR(AppConstants.EXPLANATION_REMARK);
		batchJournalHdr.setVNAN8(Integer.valueOf(s.getAddresNumber()));
		batchJournalHdr.setVNVINV(invoiceNumber);
		batchJournalHdr.setVNIVD(julianDateInvoice);
		batchJournalHdr.setVNDSVJ(julianDateToday);
		batchJournalHdr.setVNUSER(AppConstants.USER_SMARTECH);
		batchJournalHdr.setVNPID(AppConstants.PROGRAM_ID_ZP0411Z1);
		batchJournalHdr.setVNJOBN(AppConstants.WORK_STN_ID_COBOWB04);
		batchJournalHdr.setVNUPMJ(julianDateToday);
		batchJournalHdr.setVNUPMT(julianDateTime);
		batchJournalHdr.setVNCRRM(o.getCurrencyMode());
		if(!"MXP".equals(o.getCurrencyCode())) {
			batchJournalHdr.setVNACR(inv.getSubTotal()*100D);
		}
		batchJournalDTOList.add(batchJournalHdr);
		
		//Líneas de Conceptos
		if(o.getConceptTotalAmount() > 0D) {
			String invoiceNumberDtl;
			for(FiscalDocumentsConcept concept : o.getConcepts()) {
				invoiceNumberDtl = "";
				lineNumber = lineNumber + 1D;
				
				julianDateInvoice = 0;
				if(concept.getInvoiceDate() != null && !concept.getInvoiceDate().isEmpty()) {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String divj = concept.getInvoiceDate();		
				    Date date = null;
				    
			        try {
			        	divj = divj.replace("T", " ");
			            date = fmt.parse(divj);
			            julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
			        } catch (Exception e) {
			        	log4j.error("Exception" , e);
			            e.printStackTrace();
			        }
				}
				folio = "";				
				if(concept.getFolio() != null && !"null".equals(concept.getFolio()) && !"NULL".equals(concept.getFolio()) ) {
					folio = concept.getFolio();
				}
				
				serie = "";
				if(concept.getSerie() != null && !"null".equals(concept.getSerie()) && !"NULL".equals(concept.getSerie()) ) {
					serie = concept.getSerie();
				}
				
				
				invoiceNumberDtl = serie.concat(folio);
				if("".equals(invoiceNumberDtl) && !"".equals(invoiceNumber)) {
					invoiceNumberDtl = invoiceNumber.concat("-").concat(String.valueOf(Double.valueOf(lineNumber).intValue()));
				}
				log4j.info("****** Concept InvoiceNumber:" + invoiceNumberDtl);
				
				//Líneas de Batch Voucher Transactions (F0411Z1)
				BatchVoucherTransactionsDTO batchTransDtl = (BatchVoucherTransactionsDTO) SerializationUtils.clone(batchTransHdr);				
				batchTransDtl.setVLEDLN(lineNumber*1000D);
				batchTransDtl.setVLAG(concept.getAmount()*100D);
				batchTransDtl.setVLAAP(concept.getAmount()*100D);
				batchTransDtl.setVLATXA(concept.getSubtotal()*100D);
				batchTransDtl.setVLSTAM(concept.getImpuestos()*100D);
				batchTransDtl.setVLTXA1(concept.getTaxCode());
				batchTransDtl.setVLCRRM(concept.getCurrencyMode());
				batchTransDtl.setVLCRCD(concept.getCurrencyCode());
				batchTransDtl.setVLGLC(concept.getGlOffset());
				batchTransDtl.setVLGLBA(concept.getAccountingAccount());				
				batchTransDtl.setVLVR01(concept.getUuid().substring(0, 25));
				batchTransDtl.setVLURRF(concept.getUuid().substring(concept.getUuid().length() - 11));
				batchTransDtl.setVLDIVJ(julianDateInvoice);
				batchTransDtl.setVLVINV(invoiceNumberDtl);
				if(!"MXP".equals(concept.getCurrencyCode())) {
					batchTransDtl.setVLAG(0D);
					batchTransDtl.setVLAAP(0D);
					batchTransDtl.setVLATXA(0D);
					batchTransDtl.setVLSTAM(0D);
					batchTransDtl.setVLCRR(concept.getTipoCambio());
					batchTransDtl.setVLACR(concept.getAmount()*100D);
					batchTransDtl.setVLFAP(concept.getAmount()*100D);
					batchTransDtl.setVLCTXA(concept.getSubtotal()*100D);
					batchTransDtl.setVLCTAM(concept.getImpuestos()*100D);
				}
				batchTransDTOList.add(batchTransDtl);
				
				//Líneas de Batch Journal Entry (F0911Z1)
				BatchJournalEntryDTO batchJournalDtl = (BatchJournalEntryDTO) SerializationUtils.clone(batchJournalHdr);
				batchJournalDtl.setVNEDLN(lineNumber*1000D);
				batchJournalDtl.setVNANI(concept.getConceptAccount());				
				batchJournalDtl.setVNAA(concept.getSubtotal()*100D);
				batchJournalDtl.setVNCRCD(concept.getCurrencyCode());
				batchJournalDtl.setVNCRRM(concept.getCurrencyMode());
				batchJournalDtl.setVNIVD(julianDateInvoice);
				batchJournalDtl.setVNVINV(invoiceNumberDtl);
				if(!"MXP".equals(concept.getCurrencyCode())) {
					batchJournalDtl.setVNACR(concept.getAmount()*100D);
				}				
				batchJournalDTOList.add(batchJournalDtl);
			}
		}
		
		batchJournalDTO.setVoucherEntries(batchTransDTOList);
		batchJournalDTO.setJournalEntries(batchJournalDTOList);		
		String resp = jDERestService.sendJournalEntries(batchJournalDTO);
		return resp;
	}
  
  public boolean createNewVoucherFromInvoice(InvoiceDTO inv) {
    VoucherHeaderDTO recHdr = new VoucherHeaderDTO();
    NextNumber nn = this.nextNumberService.getNextNumber("VOUCHER");
    int nextNbr = nn.getNexInt();
    recHdr.setSYEDOC(nextNbr);
    recHdr.setSYEDCT("PV");
    recHdr.setSYEDLN(0.0D);
    recHdr.setSYVINV(inv.getFolio());
    recHdr.setSYDIVJ(inv.getFechaTimbrado());
    nextNbr++;
    nn.setNexInt(nextNbr);
    this.nextNumberService.updateNextNumber(nn);
    this.jDERestService.sendVoucher(recHdr);
    return true;
  }
  
  public boolean createJournalEntries(InvoiceDTO inv, InvoiceDTO cn, String addressBook, int documentNumber, String documentType, PurchaseOrder po) {
    Supplier s = this.supplierService.searchByAddressNumber(addressBook);
    String emailRecipient = String.valueOf(s.getEmailContactoVentas()) + "," + s.getEmailSupplier();
    UDC udc = this.udcService.searchBySystemAndKey("ACCOUNT", "DIFPRICE");
    String account = udc.getStrValue1();
    String subAccount = udc.getStrValue2();
    if (!"".equals(subAccount))
      subAccount = "." + subAccount; 
    double ncTotal = cn.getTotal();
    PurchaseOrderDetail item = null;
    Set<PurchaseOrderDetail> items = po.getPurchaseOrderDetail();
    for (PurchaseOrderDetail dtl : items) {
      if (dtl.getAmuntReceived() > 0.0D) {
        item = dtl;
        break;
      } 
    } 
    if (item != null) {
      String divj = cn.getFechaTimbrado();
      divj = divj.replace("T", " ");
      String resultDate = null;
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date invDate = null;
      try {
        invDate = fmt.parse(divj);
        resultDate = JdeJavaJulianDateTools.Methods.getJulianDate(invDate);
      } catch (Exception e) {
    	log4j.error("Exception" , e);
        e.printStackTrace();
      } 
      NextNumber nn = this.nextNumberService.getNextNumber("VOUCHER");
      int nextNbr = nn.getNexInt();
      ncTotal = ncTotal * 100.0D * -1.0D;
      
      List<BatchVoucherTransactionsDTO> vtList = new ArrayList<>();
      List<BatchJournalEntryDTO> jeList = new ArrayList<>();
      BatchVoucherTransactionsDTO vt = new BatchVoucherTransactionsDTO();
      BatchJournalEntryDTO je = new BatchJournalEntryDTO();
      vt.setVLEDUS("JDE");
      vt.setVLEDTN(String.valueOf(nextNbr + 9000000));
      vt.setVLEDLN(1.0D);
      vt.setVLEDER("B");
      vt.setVLEDDL(0);
      vt.setVLEDSP("0");
      vt.setVLEDTC("A");
      vt.setVLEDTR("V");
      vt.setVLEDBT(String.valueOf(nextNbr));
      vt.setVLEDDH("1");
      vt.setVLKCO(po.getOrderCompany());
      vt.setVLAN8(Integer.valueOf(po.getAddressNumber()).intValue());
      vt.setVLPYE(Integer.valueOf(po.getAddressNumber()).intValue());
      vt.setVLDIVJ(Integer.valueOf(resultDate).intValue());
      vt.setVLDSVJ(Integer.valueOf(resultDate).intValue());
      vt.setVLDDJ(0);
      vt.setVLDDNJ(0);
      vt.setVLDGJ(Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue());
      vt.setVLFY(0);
      vt.setVLCTRY(20);
      vt.setVLCO(po.getOrderCompany());
      vt.setVLICUT("V");
      vt.setVLBALJ("Y");
      vt.setVLPST("A");
      vt.setVLAG(ncTotal);
      vt.setVLAAP(ncTotal);
      double tax = 0.0D;
      String taxType = "IVA 0";
      double ncTax = cn.getTotalImpuestos();
      if (ncTax != 0.0D) {
        String str;
        switch ((str = taxType).hashCode()) {
          case 2259460:
            if (!str.equals("IVA8"))
              break; 
            tax = 0.08D;
            break;
          case 70042564:
            if (!str.equals("IVA 0"))
              break; 
            tax = 0.0D;
            break;
          case 70043097:
            if (!str.equals("IVA16"))
              break; 
            tax = 0.16D;
            break;
        } 
        taxType = s.getTaxRate().trim();
      } 
      double taxableAmount = ncTotal / (1.0D + tax);
      double taxAmount = taxableAmount * tax;
      taxableAmount = Math.round(taxableAmount * 100.0D) / 100.0D;
      taxAmount = Math.round(taxAmount * 100.0D) / 100.0D;
      vt.setVLATXA(0.0D);
      vt.setVLSTAM(0.0D);
      vt.setVLTXA1(taxType);
      vt.setVLEXR1(s.getExplCode1().trim());
      vt.setVLCRRM("D");
      vt.setVLCRCD(po.getCurrecyCode());
      vt.setVLGLC("");
      vt.setVLGLBA("");
      vt.setVLAM("2");
      vt.setVLMCU(po.getBusinessUnit());
      vt.setVLPTC(String.valueOf(s.getDiasCredito()) + "F");
      vt.setVLVINV(cn.getFolio());
      vt.setVLRMK("FAC:" + inv.getFolio());
      vt.setVLPYIN("F");
      vt.setVLTORG("JDE");
      vt.setVLUSER("JDE");
      vt.setVLPID("ZP0411Z1");
      vt.setVLUPMJ(Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue());
      vt.setVLUPMT(92519);
      vt.setVLJOBN("WEBEURE3");
      vtList.add(vt);
      je.setVNEDUS("JDE");
      je.setVNEDTN(String.valueOf(nextNbr + 9000000));
      je.setVNEDLN(1.0D);
      je.setVNEDER("B");
      je.setVNEDDL(0);
      je.setVNEDSP("0");
      je.setVNEDTC("A");
      je.setVNEDTR("V");
      je.setVNEDBT(String.valueOf(nextNbr));
      je.setVNDGJ(Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue());
      je.setVNICU(0);
      je.setVNICUT("V");
      je.setVNTICU(0);
      je.setVNCO(po.getOrderCompany());
      je.setVNANI(String.valueOf(po.getBusinessUnit()) + "." + account + subAccount);
      je.setVNAM("2");
      je.setVNAID("");
      je.setVNLT("AA");
      je.setVNCTRY(20);
      je.setVNFY(0);
      je.setVNCRCD(po.getCurrecyCode());
      je.setVNAA(taxableAmount * 100.0D * -1.0D);
      je.setVNEXA(item.getItemDescription());
      je.setVNEXR("FAC:" + inv.getFolio());
      je.setVNAN8(Integer.valueOf(po.getAddressNumber()).intValue());
      je.setVNVINV(String.valueOf(cn.getFolio()));
      je.setVNIVD(Integer.valueOf(resultDate).intValue());
      je.setVNWY(0);
      je.setVNDSVJ(Integer.valueOf(resultDate).intValue());
      je.setVNUSER("JDE");
      je.setVNPID("ZP0411Z1");
      je.setVNJOBN("WEBEURE3");
      je.setVNUPMJ(Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue());
      je.setVNUPMT(92519);
      je.setVNCRRM("D");
      jeList.add(je);
      BatchJournalDTO bj = new BatchJournalDTO();
      bj.setVoucherEntries(vtList);
      bj.setJournalEntries(jeList);
      this.jDERestService.sendJournalEntries(bj);
      createNewVoucher(po, inv, nextNbr, s, null, AppConstants.NN_MODULE_VOUCHER);
      nextNbr++;
      nn.setNexInt(nextNbr);
      this.nextNumberService.updateNextNumber(nn);
      this.emailService.sendEmail("CryoInfra - Notificacidel Portal de Proveedores. No Responder.", "La factura y la nota de crha sido aceptadas para la Orden de Compra:" + po.getOrderNumber() + "-" + po.getOrderType(), emailRecipient);
    } else {
      return false;
    } 
    return true;
  }
  
  @SuppressWarnings("unused")
private AddressBookDTO getMasterData(Supplier s, NextNumber nn) {
    AddressBookDTO o = new AddressBookDTO();
    String nextBatch = nn.getNextStr();
    o.setVOEDUS("PORTALPROV");
    o.setVOEDTN(nextBatch);
    o.setVOEDLN(1);
    o.setVOEDDT(JdeJavaJulianDateTools.Methods.getJulianDateInteger(new Date()));
    o.setVOEDER("R");
    o.setVOEDSP("0");
    o.setVOEDTC("A");
    o.setVOEDBT(nextBatch);
    o.setVOAN8(Integer.valueOf(s.getAddresNumber()).intValue());
    o.setVOALPH(s.getRazonSocial());
    o.setVOMLNM(s.getRazonSocial());
    o.setVOIDLN(0);
    o.setVOPH1("");
    o.setVOADD1(s.getCalleNumero());
    o.setVOADD2(s.getColonia());
    o.setVOADD3(s.getDelegacionMnicipio());
    o.setVOADD4(s.getEstado());
    o.setVOADDZ(s.getCodigoPostal());
    o.setVOCTR(s.getCountry());
    o.setVOMCU("           1");
    o.setVOAT1(s.getCategoriaJDE());
    o.setVOAC12(s.getTipoProductoServicio());
    o.setVOTAX(s.getRfc());
    o.setVOTX2(s.getTaxId());
    o.setVOTAXC(s.getFisicaMoral());
    o.setVOTXA2(s.getTaxRate());
    o.setVOEXR2(s.getExplCode1());
    o.setVOATP("Y");
    o.setVOAPC(s.getCategorias());
    o.setVOCRRP(s.getCurrencyCode());
    o.setVOCRCA(s.getCurrencyCode());
    o.setVOCRCP(s.getCurrencyCode());
    o.setVOTRAP(s.getDiasCredito());
    o.setVOPYIN(s.getFormaPago());
    o.setVOATE("N");
    o.setVOATR("N");
    o.setVOGLBA("");
    return o;
  }
  
  @SuppressWarnings("unused")
private AddressBookDTO getBankData(Supplier s, NextNumber nn) {
    AddressBookDTO o = new AddressBookDTO();
    String nextBatch = nn.getNextStr();
    o.setVOEDUS("PORTALPROV");
    o.setVOEDTN(nextBatch);
    o.setVOEDLN(2);
    o.setVOEDDT(JdeJavaJulianDateTools.Methods.getJulianDateInteger(new Date()));
    o.setVOEDER("R");
    o.setVOEDSP("0");
    o.setVOEDTC("B");
    o.setVOCTR(s.getCountry());
    o.setVOEDBT(nextBatch);
    o.setVOAN8(Integer.valueOf(s.getAddresNumber()).intValue());
    o.setVOALPH(s.getCuentaClabe());
    o.setVOMLNM(s.getNombreBanco());
    o.setVOGLBA("");
    return o;
  }
  
  @SuppressWarnings("unused")
private void getContactData(Supplier s, NextNumber nn, List<AddressBookDTO> l) {
    String nextBatch = nn.getNextStr();
    AddressBookDTO o = new AddressBookDTO();
    o.setVOEDUS("PORTALPROV");
    o.setVOEDTN(nextBatch);
    o.setVOEDLN(3);
    o.setVOEDDT(JdeJavaJulianDateTools.Methods.getJulianDateInteger(new Date()));
    o.setVOEDER("R");
    o.setVOEDSP("0");
    o.setVOEDTC("C");
    o.setVOEDBT(nextBatch);
    o.setVOAN8(Integer.valueOf(s.getAddresNumber()).intValue());
    o.setVOALPH(s.getEmailSupplier());
    o.setVOMLNM(s.getNombreContactoPedidos());
    o.setVOIDLN(1);
    o.setVOPH1(s.getTelefonoContactoPedidos());
    o.setVOGLBA("");
    l.add(o);
    o = new AddressBookDTO();
    o.setVOEDUS("PORTALPROV");
    o.setVOEDTN(nextBatch);
    o.setVOEDLN(4);
    o.setVOEDDT(JdeJavaJulianDateTools.Methods.getJulianDateInteger(new Date()));
    o.setVOEDER("R");
    o.setVOEDSP("0");
    o.setVOEDTC("C");
    o.setVOEDBT(nextBatch);
    o.setVOAN8(Integer.valueOf(s.getAddresNumber()).intValue());
    o.setVOALPH(s.getEmailContactoVentas());
    o.setVOMLNM(s.getNombreContactoVentas());
    o.setVOIDLN(2);
    o.setVOPH1(s.getTelefonoContactoVentas());
    o.setVOGLBA("");
    l.add(o);
    o = new AddressBookDTO();
    o.setVOEDUS("PORTALPROV");
    o.setVOEDTN(nextBatch);
    o.setVOEDLN(5);
    o.setVOEDDT(JdeJavaJulianDateTools.Methods.getJulianDateInteger(new Date()));
    o.setVOEDER("R");
    o.setVOEDSP("0");
    o.setVOEDTC("C");
    o.setVOEDBT(nextBatch);
    o.setVOAN8(Integer.valueOf(s.getAddresNumber()).intValue());
    o.setVOALPH(s.getEmailContactoCxC());
    o.setVOMLNM(s.getNombreContactoCxC());
    o.setVOIDLN(3);
    o.setVOPH1(s.getTelefonoContactoCxC());
    o.setVOGLBA("");
    l.add(o);
    o = new AddressBookDTO();
    o.setVOEDUS("PORTALPROV");
    o.setVOEDTN(nextBatch);
    o.setVOEDLN(6);
    o.setVOEDDT(JdeJavaJulianDateTools.Methods.getJulianDateInteger(new Date()));
    o.setVOEDER("R");
    o.setVOEDSP("0");
    o.setVOEDTC("C");
    o.setVOEDBT(nextBatch);
    o.setVOAN8(Integer.valueOf(s.getAddresNumber()).intValue());
    o.setVOALPH(s.getEmailContactoCalidad());
    o.setVOMLNM(s.getNombreContactoCalidad());
    o.setVOIDLN(4);
    o.setVOPH1(s.getTelefonoContactoCalidad());
    o.setVOGLBA("");
    l.add(o);
  }
  
  public void sendFiles(List<File> files) {
	  LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
	  for(File f : files) {
	      map.add("files", new FileSystemResource(f));
	  }
	  
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.exchange(AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/loadFiles", HttpMethod.POST, requestEntity, String.class);
  }
  
  @SuppressWarnings("unused")
private String getAlphaNumericString(int n) {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      int index = 
        (int)(AlphaNumericString.length() * 
        Math.random());
      sb.append(AlphaNumericString
          .charAt(index));
    } 
    return sb.toString();
  }

  public  synchronized BatchJournalDTO createBatchJournalDTO(FiscalDocumentsConcept fiscalDocumentsConcept, InvoiceDTO inv, Supplier s, String nextNumberType, FiscalDocuments fiscalDocuments){
		BatchJournalDTO batchJournalDTO = new BatchJournalDTO();
		int julianDateInvoiceUpload = 0;
		int julianDateInvoice = 0;
		int julianDatePayment = 0;
		int julianDateToday = 0;
		int julianDateTime = 0;
		double lineNumber = 1D;
		String supplierName = "";

		if (inv.getMoneda().equals("MXN")) {
			inv.setMoneda("MXP");
		}

		if (AppConstants.NC_TC.equals(inv.getTipoComprobante())) {
			inv.setSubTotal(inv.getSubTotal() * -1);
		}

		if (s.getName().length() > 30) {
			supplierName = s.getName().substring(0, 30);
		} else {
			supplierName = s.getName();
		}

		if (inv.getFechaTimbrado() != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String divj = inv.getFechaTimbrado();
			Date date = null;

			try {
				divj = divj.replace("T", " ");
				date = fmt.parse(divj);
				julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}

		if (fiscalDocumentsConcept.getEstimatedPaymentDate() != null) {
			try {
				julianDatePayment = Integer.valueOf(
						JdeJavaJulianDateTools.Methods.getJulianDate(fiscalDocumentsConcept.getEstimatedPaymentDate()));
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}

		if (fiscalDocumentsConcept.getInvoiceUploadDate() != null) {
			try {
				julianDateInvoiceUpload = Integer.valueOf(
						JdeJavaJulianDateTools.Methods.getJulianDate(fiscalDocumentsConcept.getInvoiceUploadDate()));
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}

		julianDateToday = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue();
		julianDateTime = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJDETimeStamp()).intValue();

		UDC company = new UDC();
		company = udcService.searchBySystemAndStrValue1("COMPANYCB", fiscalDocumentsConcept.getRfcReceptor());
if (company==null) {
	System.out.println(new Date().toLocaleString()+"--"+ fiscalDocumentsConcept.getRfcReceptor()+": no esta dada de alta en los udc del sistema COMPANYCB, dar de alta para que el documento pase");

}
		// Encabezado Batch Voucher Transactions (F0411Z1)
		List<BatchVoucherTransactionsDTO> batchTransDTOList = new ArrayList<BatchVoucherTransactionsDTO>();
		BatchVoucherTransactionsDTO batchTransHdr = new BatchVoucherTransactionsDTO();
		String accountingAcc = fiscalDocumentsConcept.getCtaPresupuestal().split("\\.")[0];
		batchTransHdr.setVLEDCT("P3");
		batchTransHdr.setVLEDLN(lineNumber * 1000D);
		batchTransHdr.setVLICU(fiscalDocumentsConcept.getBatchID() + "");/// numero de baucher
		batchTransHdr.setVLRMK("PORTAL CRYOINFRA");
		batchTransHdr.setVLDCT("P3");

		batchTransHdr.setVLEDUS(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLEDTN("1");
		batchTransHdr.setVLEDLN(lineNumber * 1000D);
		batchTransHdr.setVLEDER("B");
		batchTransHdr.setVLEDSP("0");
		batchTransHdr.setVLEDTC("A");
		batchTransHdr.setVLEDTR("V");
		batchTransHdr.setVLEDBT(fiscalDocumentsConcept.getBatchID() + "");
		batchTransHdr.setVLEDDH("1");
		batchTransHdr.setVLKCO(company.getUdcKey());
		batchTransHdr.setVLSFXE("0");
		batchTransHdr.setVLAN8(Integer.valueOf(fiscalDocumentsConcept.getAddressNumber()).intValue());
		batchTransHdr.setVLPYE(Integer.valueOf(fiscalDocumentsConcept.getAddressNumber()).intValue());
		batchTransHdr.setVLDDJ(julianDatePayment);
		batchTransHdr.setVLDDNJ(julianDatePayment);
		batchTransHdr.setVLDIVJ(julianDateInvoice);
		batchTransHdr.setVLDSVJ(julianDateToday);
		batchTransHdr.setVLDGJ(julianDateInvoiceUpload);
		batchTransHdr.setVLCTRY(20);
		batchTransHdr.setVLCO(company.getUdcKey());
		batchTransHdr.setVLICUT("V");
		batchTransHdr.setVLBALJ("Y");
		batchTransHdr.setVLPST(
				fiscalDocuments.getReplicationStatus().equals(AppConstants.STATUS_PENDING_REPLICATION) ? AppConstants.STATUS_JDE_PENDI_FLETES : AppConstants.STATUS_JDE_APROV_FLETES);
		batchTransHdr.setVLATXA(fiscalDocuments.getSubtotal() * 100D);
		batchTransHdr.setVLTXA1(s.getTaxRate());
		batchTransHdr.setVLEXR1("V");
		batchTransHdr.setVLCRRM("D");
		batchTransHdr.setVLCRCD(inv.getMoneda());

		batchTransHdr.setVLAM("2");
		batchTransHdr.setVLVINV(
				fiscalDocuments.getSemanaPago().length() > 25 ? fiscalDocuments.getSemanaPago().substring(0, 25)
						: fiscalDocuments.getSemanaPago());
		batchTransHdr.setVLVR01(inv.getUuid().substring(0, 25));
		batchTransHdr.setVLURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
		batchTransHdr.setVLPYIN("T");
		batchTransHdr.setVLTORG(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLUSER(AppConstants.USER_SMARTECH);
		batchTransHdr.setVLPID(AppConstants.PROGRAM_ID_ZP0411Z1);
		batchTransHdr.setVLUPMJ(julianDateToday);
		batchTransHdr.setVLUPMT(julianDateTime);
		batchTransHdr.setVLJOBN(AppConstants.WORK_STN_ID_COBOWB04);

		// if(!"MXP".equals(inv.getMoneda())) {
		// batchTransHdr.setVLAG(0D);
		// batchTransHdr.setVLAAP(0D);
		// batchTransHdr.setVLATXA(0D);
		// batchTransHdr.setVLSTAM(0D);
		// batchTransHdr.setVLCRR(inv.getTipoCambio());
		// batchTransHdr.setVLACR(inv.getTotal()*100D);
		// batchTransHdr.setVLFAP(inv.getTotal()*100D);
		// batchTransHdr.setVLCTXA(inv.getSubTotal()*100D);
		// batchTransHdr.setVLCTAM(fiscalDocumentsConcept.getImpuestos()*100D);
		// }
		batchTransDTOList.add(batchTransHdr);

		// Encabezado Batch Journal Entry (F0911Z1)
		List<BatchJournalEntryDTO> batchJournalDTOList = new ArrayList<BatchJournalEntryDTO>();
		BatchJournalEntryDTO batchJournalHdr = new BatchJournalEntryDTO();

		batchJournalHdr.setVNEDCT("P3");
		batchJournalHdr.setVNDCT("P3");
		batchJournalHdr.setVNEDUS(AppConstants.USER_SMARTECH);
		batchJournalHdr.setVNEDTN("1");
		batchJournalHdr.setVNEDLN(lineNumber * 1000D);
		batchJournalHdr.setVNEDER("B");
		batchJournalHdr.setVNEDSP("0");
		batchJournalHdr.setVNEDTC("A");
		batchJournalHdr.setVNEDTR("V");
		batchJournalHdr.setVNEDBT(fiscalDocumentsConcept.getBatchID() + "");
		batchJournalHdr.setVNDGJ(julianDateInvoiceUpload);
		batchJournalHdr.setVNICUT("V");
		batchJournalHdr.setVNCO(company.getUdcKey());
		batchJournalHdr.setVNANI(fiscalDocumentsConcept.getCtaPresupuestal());
		batchJournalHdr.setVNAM("2");
		batchJournalHdr.setVNAID("");
		batchJournalHdr.setVNLT("AA");
		batchJournalHdr.setVNCTRY(20);
		batchJournalHdr.setVNCRCD(inv.getMoneda());
		batchJournalHdr.setVNAA(fiscalDocuments.getSubtotal() * 100D);
		batchJournalHdr.setVNEXA(supplierName);
		batchJournalHdr.setVNEXR("BITACORA FLETES");
		batchJournalHdr.setVNAN8(Integer.valueOf(s.getAddresNumber()));
		batchJournalHdr.setVNVINV(
				fiscalDocuments.getSemanaPago().length() > 25 ? fiscalDocuments.getSemanaPago().substring(0, 25)
						: fiscalDocuments.getSemanaPago());
		batchJournalHdr.setVNIVD(julianDateInvoice);
		batchJournalHdr.setVNDSVJ(julianDateToday);
		batchJournalHdr.setVNUSER(AppConstants.USER_SMARTECH);
		batchJournalHdr.setVNPID(AppConstants.PROGRAM_ID_ZP0411Z1);
		batchJournalHdr.setVNJOBN(AppConstants.WORK_STN_ID_COBOWB04);
		batchJournalHdr.setVNUPMJ(julianDateToday);
		batchJournalHdr.setVNUPMT(julianDateTime);
		batchJournalHdr.setVNCRRM("D");
		// if(!"MXP".equals(inv.getMoneda())) {
		// batchJournalHdr.setVNACR(inv.getSubTotal()*100D);
		// }
		batchJournalDTOList.add(batchJournalHdr);

		batchJournalDTO.setVoucherEntries(batchTransDTOList);
		batchJournalDTO.setJournalEntries(batchJournalDTOList);

		return batchJournalDTO;
	}
  
  public synchronized VoucherHeaderDTO createNewForeignVoucherMdw(PurchaseOrder o, ForeingInvoice inv, int nextNbr, Supplier s, List<Receipt> receipts, String nextNumberType,FiscalDocuments fisDoc){
		
	  int linenumber=1;
	  VoucherHeaderDTO recHdr = new VoucherHeaderDTO();
		NextNumber nn = null;
		int julianEPD = 0;
		int nextNbrBatch=0;

		if(nextNbr == 0) {
			nn = nextNumberService.getNextNumber(nextNumberType);
			nextNbr = nn.getNexInt();				
			int nextNbrUpd = nextNbr + 1;
			nn.setNexInt(nextNbrUpd);
			nextNumberService.updateNextNumber(nn);	
		}
		
		Users usuarioaprob=usersService.getByUserName(fisDoc.getCurrentApprover());

		linenumber=usuarioaprob.getLinenumber();
		nextNbrBatch=usuarioaprob.getBatchnumber();
		
		if(o.getEstimatedPaymentDate() != null) {
			julianEPD =Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
		}
		recHdr.setSYDCTV("P4");
		recHdr.setSYEDOC(nextNbr);
		recHdr.setSYEDBT((nextNbrBatch==0?nextNbr:nextNbrBatch)+"");
		recHdr.setSYEKCO(o.getOrderCompany());
		recHdr.setSYEDCT("PV");
		recHdr.setSYEDLN(linenumber);
		recHdr.setSYDOCO(o.getOrderNumber());
		recHdr.setSYDCTO(o.getOrderType());
		recHdr.setSYKCOO(o.getOrderCompany());
		recHdr.setSYSFXO(o.getOrderSuffix());
		recHdr.setSYAN8(Integer.valueOf(o.getAddressNumber()));
		recHdr.setSYSHAN(Integer.valueOf(o.getShipTo()));
		recHdr.setSYMCU(o.getBusinessUnit());
		recHdr.setCRRM(o.getCurrencyMode());
		recHdr.setSYDDU(julianEPD);
		recHdr.setSYDDJ(julianEPD);
		
		if(inv.getInvoiceNumber() != null && !"".equals(inv.getInvoiceNumber())) {
			inv.setFolio(inv.getInvoiceNumber());
			inv.setSerie("");
		}
		
		String folio = "";
		log4j.info("****** Folio:" + inv.getFolio());
		if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
			folio = inv.getFolio();
		}
		
		String serie = "";
		log4j.info("****** Serie:" + inv.getSerie());
		if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
			serie = inv.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
		}
		
		//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
		if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
			serie = "ZX";
			folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
		}
		
		String vinv = "";
		vinv = serie + folio;
		
		//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
		if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
			vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
		}
		
		//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
		if(vinv.length() > 25) {
			vinv = vinv.substring(0, 25);
		}
		
		log4j.info("****** Vinv:" + vinv);
		
		recHdr.setSYVINV(vinv);		
		recHdr.setSYCRCD(o.getCurrecyCode());
		recHdr.setSYCRR(o.getExchangeRate());
		
		recHdr.setSYVR01(inv.getUuid().substring(0, 25));
		recHdr.setSYURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
		
		String divj = inv.getExpeditionDate();
		divj = divj.replace("T", " ");
      String resultDate = null;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = null;
	        try {
	            date = fmt.parse(divj);
	            resultDate = JdeJavaJulianDateTools.Methods.getJulianDate(date);
	        } catch (Exception e) {
	        	log4j.error("Exception" , e);
	            e.printStackTrace();
	        }
	        
		recHdr.setSYDIVJ(resultDate);

		List<VoucherDetailDTO> dtlList = new ArrayList<VoucherDetailDTO>();
		int lineNumber = 1;
		double qtyOpen = 0;
		double totalAmount = 0;
		double openAmount = 0;
		double exchRate = 0;
		double totalQuantity=0;

		for(Receipt d : receipts) {
			
			if(d.getForeignAmountReceived() != 0) {
				VoucherDetailDTO dto = new VoucherDetailDTO();
	
				dto.setSZEDOC(nextNbr);
				dto.setSZEDBT((nextNbrBatch==0?nextNbr:nextNbrBatch)+"");
				dto.setSZEDCT(o.getOrderType());
				dto.setSZEKCO(o.getOrderCompany());
				dto.setSZEDLN(lineNumber);
				dto.setSZLNTY(d.getLineType());
				
				dto.setSZDOCO(o.getOrderNumber());
				dto.setSZDCTO(o.getOrderType());
				dto.setSZKCOO(o.getCompanyKey());
				dto.setSZSFXO(o.getOrderSuffix());
				dto.setSZLNID(d.getLineNumber() * 1000);
				dto.setSZLITM(d.getItemNumber());
				dto.setSZAN8(Integer.valueOf(o.getAddressNumber()));
				dto.setSZMCU(o.getBusinessUnit());
				dto.setSZVINV(vinv);
				dto.setSZAID(d.getAccountId());
				dto.setCRRM(o.getCurrencyMode());
				dto.setSZCRCD(o.getCurrecyCode());
				
				dto.setSZPST("OP".equals(o.getOrderType()) ? "R" : "H");
				dto.setSZDCTV("P4");
				
				dto.setSZUOM(d.getUom());
				dto.setSZUPRC(0);
				dto.setSZAEXP(0);
				dto.setURAB(d.getReceiptLine());
				
				if(d.getExchangeRate() == 0) {
					dto.setSZCRR(o.getExchangeRate());
				}else {
					dto.setSZCRR(d.getExchangeRate());
				}
				exchRate = dto.getSZCRR();
				
				dto.setSZFRRC(d.getForeignUnitCost());				
				dto.setSZAG(0);
				dto.setSZAAP(0);				
				dto.setSZUOPN(d.getQuantityReceived());
				qtyOpen = qtyOpen + dto.getSZUOPN();
								
				BigDecimal bd = new BigDecimal(d.getForeignAmountReceived()*100).setScale(2, RoundingMode.HALF_EVEN);
				double foreignAmountReceived = bd.doubleValue();
				//float foreignAmountReceived = Math.round(d.getForeignAmountReceived() * 100); //Fix para cantidades grandes
				dto.setSZACR(foreignAmountReceived);
				dto.setSZFAP(foreignAmountReceived);
				totalAmount = totalAmount + foreignAmountReceived;
				
				openAmount = openAmount + dto.getSZAAP();
				totalQuantity = totalQuantity + d.getQuantityReceived();
				dtlList.add(dto);
				lineNumber = lineNumber + 1;
				dto.setSZDDJ(julianEPD);
			}
		}		
		recHdr.setVoucherDetailDTO(dtlList);
		
		VoucherSummaryDTO vs = new VoucherSummaryDTO();
		vs.setSWEKCO(o.getOrderCompany());
		vs.setSWEDOC(nextNbr);
		vs.setSWKCO(o.getOrderCompany());
		vs.setSWKCOO(o.getOrderCompany());
		vs.setSWDOCO(o.getOrderNumber());
		vs.setSWDCTO(o.getOrderType());
		vs.setSWSFXO(o.getOrderSuffix());
		vs.setSWAN8(Integer.valueOf(o.getAddressNumber()));
		vs.setSWVINV(vinv);
		vs.setSWUOPN(totalQuantity);
		vs.setSWACR(totalAmount);
		vs.setSWFAP(totalAmount);
		vs.setCRRM(o.getCurrencyMode());
		vs.setSWCRCD(o.getCurrecyCode());
		vs.setSWCRR(exchRate);
		vs.setSWAG(0);
		vs.setSWAAP(0);
		vs.setSWDCTV("P4");
		recHdr.setVoucherSummaryDTO(vs);
		
		//String resp = jDERestService.sendVoucher(recHdr);
		return recHdr;
	}
  
}
