package com.eurest.supplier.util;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPClient {
	
	private String REMOTE_HOST ;
	private String USERNAME ;
	private int SESSION_TIMEOUT = 10000;
	private int CHANNEL_TIMEOUT = 5000;
	private String  CHANNEL = "sftp";
	private String KNOWNHOSTS;
	private String STRINGKEY;
	private String NOMBARCHIVO;
	private String ARCHIVOLOCAL;
	private String ARCHIVOREMOTO;
	
	private ChannelSftp channelSftp;
	private Session jschSession = null;
	private Channel sftp=null;
	Logger logger;

	public SFTPClient() {}

	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(SFTPClient.class);

	public SFTPClient(String rEMOTE_HOST, String uSERNAME, String keyFile) {
		super();
		REMOTE_HOST = rEMOTE_HOST;
		USERNAME = uSERNAME;
		this.STRINGKEY = keyFile;
	}
		
	public String conectar() {
		jschSession = null;
		try {

			JSch jsch = new JSch();
			jsch.setKnownHosts(IOUtils.toInputStream(KNOWNHOSTS));
			jschSession = jsch.getSession(USERNAME, REMOTE_HOST);
			jsch.addIdentity("key",STRINGKEY.getBytes(StandardCharsets.US_ASCII),null,null);
			jschSession.connect(SESSION_TIMEOUT);
			sftp = jschSession.openChannel(CHANNEL);
			sftp.connect(CHANNEL_TIMEOUT);
			channelSftp = (ChannelSftp) sftp;
			
			 return "";
		} catch (JSchException e) {
			log4j.error("JSchException" , e);
			e.printStackTrace();
			desconectar();
			return e.getMessage();
		} 
		
	}
	
	
	public String enviar(InputStream archivo,String rutaRemota,String nombreArchivo) {
		ARCHIVOREMOTO=rutaRemota;
		try {
			channelSftp.cd(ARCHIVOREMOTO);
			channelSftp.put(archivo, nombreArchivo);	
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return e.getMessage();
		}finally {
			desconectar();
		}
		
		
	}
	
	public String EliminarByUUid(ArrayList<String> uuids,String rutaRemota) {
		ARCHIVOREMOTO=rutaRemota;
		try {			
		Vector a=	channelSftp.ls(ARCHIVOREMOTO);
		
		
	   for (int i = 0; i < a.size(); i++) {
		String fileString=(String) a.get(i);
		log4j.info(fileString);
		if(uuids.contains(fileString.split("\\")[0])) {
			channelSftp.cd(ARCHIVOREMOTO);
			channelSftp.rm(fileString);
		}
	   }
		
		
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return e.getMessage();
		}finally {
			desconectar();
		}
		
		
	}
	
	
	
	public String enviar(byte[] archivo,String rutaRemota,String nombreArchivo) {
		ARCHIVOREMOTO=rutaRemota;
		try {
			channelSftp.cd(ARCHIVOREMOTO);
			channelSftp.put(new ByteArrayInputStream(archivo), nombreArchivo);	
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return e.getMessage();
		}finally {
			desconectar();
		}
		
		
	}
	
	public String enviar(String rutaLocal,String rutaRemota,String nombreArchivo) {
		ARCHIVOLOCAL=rutaLocal;
		ARCHIVOREMOTO=rutaRemota+nombreArchivo;
		try {
			channelSftp.put(ARCHIVOLOCAL, ARCHIVOREMOTO);	
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return e.getMessage();
		}finally {
			desconectar();
		}
		
		
	}
	
	public String enviar(String rutaLocal,String rutaRemota) {
		ARCHIVOLOCAL=rutaLocal;
		ARCHIVOREMOTO=rutaRemota;
		try {
			channelSftp.put(ARCHIVOLOCAL, ARCHIVOREMOTO);	
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return e.getMessage();
		}finally {
			desconectar();
		}
		
		
	}
	
	
	
	

	public void desconectar() {
		log4j.info("Inicio de desconeccion sftp");
		try {
			log4j.info("Inicio de desconeccion channelSftp");
			channelSftp.exit();
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		try {
			log4j.info("Inicio de desconeccion sftp");
			sftp.disconnect();
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}

		try {
			log4j.info("Inicio de desconeccion jschSession");
			jschSession.disconnect();
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}

	}
	
	
	public String listFiles(InputStream archivo,String rutaRemota,String nombreArchivo) {
		Map<String, String> ListFiles=new HashMap<String, String>();
		
		ARCHIVOREMOTO=rutaRemota;
		try {
			channelSftp.cd(ARCHIVOREMOTO);
			 Vector filelist= channelSftp.ls(ARCHIVOREMOTO);	
			 
			 for(int i=0; i<filelist.size();i++){
				 ListFiles.put(filelist.get(i).toString().replace(".pdf",""),filelist.get(i).toString() );
//	                System.out.println(filelist.get(i).toString());
	            }
			 
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return e.getMessage();
		}finally {
			desconectar();
		}
		
		
	}

	public String getREMOTE_HOST() {
		return REMOTE_HOST;
	}



	public void setREMOTE_HOST(String rEMOTE_HOST) {
		REMOTE_HOST = rEMOTE_HOST;
	}



	public String getUSERNAME() {
		return USERNAME;
	}



	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}



	public int getSESSION_TIMEOUT() {
		return SESSION_TIMEOUT;
	}



	public void setSESSION_TIMEOUT(int sESSION_TIMEOUT) {
		SESSION_TIMEOUT = sESSION_TIMEOUT;
	}



	public int getCHANNEL_TIMEOUT() {
		return CHANNEL_TIMEOUT;
	}



	public void setCHANNEL_TIMEOUT(int cHANNEL_TIMEOUT) {
		CHANNEL_TIMEOUT = cHANNEL_TIMEOUT;
	}



	public String getCHANNEL() {
		return CHANNEL;
	}



	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}



	public String getKnownHosts() {
		return KNOWNHOSTS;
	}



	public void setKnownHosts(String knownHosts) {
		KNOWNHOSTS = knownHosts;
	}



	public String getKeyFile() {
		return STRINGKEY;
	}



	public void setKeyFile(String keyFile) {
		this.STRINGKEY = keyFile;
	}



	public String getArchivo() {
		return NOMBARCHIVO;
	}



	public void setArchivo(String archivo) {
		NOMBARCHIVO = archivo;
	}



	public String getLocalFile() {
		return ARCHIVOLOCAL;
	}



	public void setLocalFile(String localFile) {
		this.ARCHIVOLOCAL = localFile;
	}



	public String getRemoteFile() {
		return ARCHIVOREMOTO;
	}



	public void setRemoteFile(String remoteFile) {
		this.ARCHIVOREMOTO = remoteFile;
	}



	public Logger getLogger() {
		return logger;
	}



	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	
	

}
