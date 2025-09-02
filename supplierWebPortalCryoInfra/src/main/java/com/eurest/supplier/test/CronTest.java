package com.eurest.supplier.test;

import com.eurest.supplier.util.Cronato;

public class CronTest {

	public static void main(String[] args) {
		
//		String fecha="jueves";
//		String fecha="1";
//		String fecha="L"; 
//		String fecha="LUN";
//		String fecha="15:00:00"; 
//		String fecha="17/08/2021"; 
//		String fecha="25/08/2021 02:00:00"; 
//		String fecha="sabado,15:00:00"; 
//		String fecha="20:48:00,viernes";
//		String fecha="20:48:00,vie";
//		String fecha="20:48:00,v";
//		String fecha="20:50:00,v";
//		String fecha="11:09:00,13:00:00";
//		String fecha="22:09:00,01:53:00";
//		String fecha="24/08/2021,24/08/2021";
//		String fecha="24/08/2021 15:00:00,25/08/2021 01:23:00";
//		String fecha="24/08/2021 15:00:00,25/08/2021 01:23:00";
//		String fecha="25/08/2021,01:00:00,02:01:00";
//		String fecha="x,01:00:00,02:04:00";
//		String fecha="mié,01:00:00,02:04:00";
		String fecha="miércoles,01:00:00,02:04:00";
		
		String reglas="s,08:00:00,10:00:00|s,12:00:00,13:00:00"; //validacion de sabado y que el horario se de 8 a 10 o de 12 a 13
		
		
		System.out.println(new Cronato().validateAcces(fecha));//devuelve true si coincide la regla
		
		System.out.println(new Cronato().validateAcces(true,reglas));///la validacion boolean TRUE la validacion es para que la validacion sea que accese
																	///la validacion boolean FALSE la validacion es para que la validacion sea impida que accese
																	/// string conjunto de reglas separadas por pipe
	}
	
}
