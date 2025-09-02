package com.eurest.supplier.dto;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ResponseGeneral {
	
	boolean error;
	HashMap<String, String> mensaje;
	String code;
	String tracktrace;
	Date fecha;
	byte[] document;
	
	
	
	
	public ResponseGeneral() {
		this.fecha=new Date();
	}
	
	public ResponseGeneral(boolean error, HashMap<String, String> mensaje, String code, String tracktrace) {
		super();
		this.error = error;
		this.mensaje = mensaje;
		this.code = code;
		this.tracktrace = tracktrace;
		this.fecha=new Date();
	}
	public ResponseGeneral(boolean error, HashMap<String, String> mensaje, String code) {
		super();
		this.error = error;
		this.mensaje = mensaje;
		this.code = code;
		this.fecha=new Date();
	}
	
	public ResponseGeneral(boolean error, String code, String tracktrace) {
		super();
		this.error = error;
		this.code = code;
		this.tracktrace = tracktrace;
		this.fecha=new Date();
	}
	public ResponseGeneral(boolean error, String code) {
		super();
		this.error = error;
		this.code = code;
		this.fecha=new Date();
	}
	
	public ResponseGeneral(boolean error, String code, Exception tracktrace) {
		super();
		this.error = error;
		this.code = code;
		setTracktrace(tracktrace);
		this.fecha=new Date();
	}

	@Override
	public String toString() {
		return "ResponseGeneral [error=" + error + ", mensaje=" + mensaje + ", code=" + code + ", tracktrace="
				+ tracktrace + ", fecha=" + fecha.toLocaleString() + "]";
	}

	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public HashMap<String, String> getMensaje() {
		return mensaje;
	}
	public void setMensaje(HashMap<String, String> mensaje) {
		this.mensaje = mensaje;
	}
	public void addMensaje(String lang,String mensaje) {
		if (this.mensaje==null) {
			this.mensaje=new HashMap<>();
		}
		this.mensaje.put(lang, mensaje);
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTracktrace() {
		return tracktrace;
	}
	public void setTracktrace(String tracktrace) {
		this.tracktrace = tracktrace;
	}
	public void setTracktrace(Exception e) {
		this.tracktrace = ExceptionUtils.getStackTrace(e);
	}
	public Date getFecha() {
		return fecha;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}
	

}
