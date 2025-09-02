package com.eurest.supplier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

public class Cronato {
	String DAY_WEEK="L,M,X,J,V,S,D,1,2,3,4,5,6,7,LUNES,MARTES,"
			+ "MIERCOLES,MIÉRCOLES,JUEVES,VIERNES,SABADO,DOMINGO,DOM,LUN,MAR,MIE,MIÉ,JUE,VIE,SAB";
	LocalDate datetime;
	Locale spanishLocale;
	SimpleDateFormat parser;
	SimpleDateFormat parser2;
	SimpleDateFormat parser3;
	
	private Logger log4j = Logger.getLogger(Cronato.class);
	
	public boolean validateAcces(boolean acceso, String dataVerify) {
		String reglas[]=dataVerify.split("\\|");
		for (int i = 0; i < reglas.length; i++) {
			String string = reglas[i];
			boolean accesovalidacion=validateAcces(string);
			if(accesovalidacion) {
			return !(acceso==accesovalidacion);
			}
		}
		
		
		
		
		
		return false;
	}
	
	
	
	public boolean validateAcces(String dataVerify) {
		
		 boolean resp=false;
		String []analis=dataVerify.split(",");
		
		try {
			
			if(analis.length==1) {
				
				switch (getTypeData(analis[0])) {
				case "fecha":
					resp =validateByFecha(analis[0]);
					break;
				case "hora":
					resp =validateByHora(analis[0]);
					break;
				case "fecha-hora":
					resp=validateByFechaHora(analis[0]);
					break;
				case "dia":
					resp= validateByDay(analis[0]);
					break;
				default:
					break;
				}
				}else if(analis.length==2) {
					switch (getTypeData(analis[0])+getTypeData(analis[1])) {
					case "diahora":
						resp=validateByDay(analis[0])&&validateByHora(analis[1]);
					break;
					case "horadia":
						resp=validateByDay(analis[1])&&validateByHora(analis[0]);
					break;
					case "horahora":
						resp=entreHoras(analis[0], analis[1]);
					break;
					case "fechafecha":
						resp=entreFechas(analis[0], analis[1]);
					break;
					case "fecha-horafecha-hora":
						resp=entreFechasHoras(analis[0], analis[1]);
					break;
					default:
						break;
					}
				}else if(analis.length==3) {
					switch (getTypeData(analis[0])+getTypeData(analis[1])+getTypeData(analis[2])) {
					case "diahorahora":
						resp=validateByDay(analis[0])&&entreHoras(analis[1], analis[2]);
					break;
					case "fechahorahora":
						resp=validateByFecha(analis[0])&&entreHoras(analis[1], analis[2]);
					break;
					default:
						break;
					}
				}
		} catch (Exception e) {
			log4j.error("Error de definicion: "+e.getMessage());
			log4j.error("Exception" , e);
		}
		return resp;
	}
	
	public Cronato() {
		 datetime=LocalDate.now();
		 spanishLocale=new Locale("es", "MX");
		 parser = new SimpleDateFormat("HH:mm:ss");
		 parser2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 parser3 = new SimpleDateFormat("dd/MM/yyyy",spanishLocale);
		
	}
	
	public boolean validateByFecha(String valor) {
		
		return (valor.equals(datetime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy",spanishLocale)).toString()));
	}
	
	
	public boolean entreHoras(String Hora1,String Hora2) {
		try {
			Date hora1=parser.parse(Hora1);
			Date hora2=parser.parse(Hora2);
			Date horanow= parser.parse(parser.format(new Date()));
			
			if(hora1.after(hora2)) {
				
				return entreHoras(Hora1, "23:59:59")||horanow.compareTo(hora2)<1;
				
			}
			
			return hora1.compareTo(horanow)<1&&horanow.compareTo(hora2)<1;
			
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
		
		return false;
	}
	public boolean entreFechas(String Fecha1,String Fecha2) {
		try {
			Date fecha1=parser3.parse(Fecha1);
			Date fecha2=parser3.parse(Fecha2);
			Date fechanow= parser3.parse(parser3.format(new Date()));
			
			if(fecha1.after(fecha2)) {
				return entreFechas(Fecha2, Fecha1);
			}
			
			return fecha1.compareTo(fechanow)<1&&fechanow.compareTo(fecha2)<1;
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
		
		return false;
	}
	
	public boolean entreFechas(String Fecha1,String Fecha2,String fechaAnalit) {
		try {
			Date fecha1=parser3.parse(Fecha1);
			Date fecha2=parser3.parse(Fecha2);
			Date fechanow= parser3.parse(fechaAnalit);
			
			if(fecha1.after(fecha2)) {
				return entreFechas(Fecha2, Fecha1,fechaAnalit);
			}
			
			return fecha1.compareTo(fechanow)<1&&fechanow.compareTo(fecha2)<1;
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
		
		return false;
	}
	public boolean entreFechasHoras(String Fecha1,String Fecha2) {
		try {
			Date fecha1=parser2.parse(Fecha1);
			Date fecha2=parser2.parse(Fecha2);
			Date fechanow= parser2.parse(parser2.format(new Date()));
			
			if(fecha1.after(fecha2)) {
				return entreFechas(Fecha2, Fecha1);
			}
			
			return fecha1.compareTo(fechanow)<1&&fechanow.compareTo(fecha2)<1;
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
		
		return false;
	}
	public boolean validateByHora(String valor) {
		Date horaIn;
		Date horanow;
		try {
			horanow = parser.parse(parser.format(new Date()));
			 horaIn = parser.parse(valor);
			return horaIn.equals(horanow)||horaIn.before(horanow);
		} catch (ParseException e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		return false;
		
	}
	
public boolean validateByDay(String valor) {
	return (new ArrayList<>(Arrays.asList(datetime.format(DateTimeFormatter.ofPattern("EEEE",spanishLocale)).toUpperCase(),
			 datetime.format(DateTimeFormatter.ofPattern("E",spanishLocale)).toUpperCase(),
			 datetime.format(DateTimeFormatter.ofPattern("e",spanishLocale)).toUpperCase(),
			 DAY_WEEK.split(",")[Integer.parseInt(datetime.format(DateTimeFormatter.ofPattern("e",spanishLocale)))-1].toUpperCase()
			 )).contains(valor.toUpperCase())) ;
	}
	
public boolean validateByFechaHora(String valor) {
		
return validateByFecha(valor.split(" ")[0])&&validateByHora(valor.split(" ")[1]);
	
	}
	
	
	
	public String getTypeData(String dataVerify) {
		if(GenericValidator.isDate(dataVerify, "dd/MM/yyyy", true)) {
			return ("fecha");
		}else if(GenericValidator.isDate(dataVerify, "HH:mm:SS", true))	{
			return("hora");
		}else if(GenericValidator.isDate(dataVerify, "dd/MM/yyyy HH:mm:SS", true)) {
			return("fecha-hora");
		}else if(new ArrayList<>(Arrays.asList(DAY_WEEK.split(","))).contains(dataVerify.toUpperCase())) {
			return("dia");
			
		}else {
			return("sin definicion");
		}
		
	}

}
