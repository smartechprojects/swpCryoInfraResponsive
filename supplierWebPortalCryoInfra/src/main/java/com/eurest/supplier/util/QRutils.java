package com.eurest.supplier.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.PlantAccessWorker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRutils {

	
	public ArrayList<byte[]> generateQRCodes(PlantAccessRequest solicitud, List<PlantAccessWorker> workers) {
	    ArrayList<byte[]> qrCodeList = new ArrayList<>();
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();

	    // Definir variables para el tamaño del código QR, el tamaño del borde y el tamaño del pie de página
	    int qrCodeSize = 380;
	    int footerHeight = 60;
	    int borderSize = 10;
	    int fontSize = 25;

	    for (PlantAccessWorker worker : workers) {
	        try {
	            BitMatrix bitMatrix = qrCodeWriter.encode( worker.getMembershipIMSS() + "|" +solicitud.getId() + "%", BarcodeFormat.QR_CODE, qrCodeSize+20, qrCodeSize);
	            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

	            // Crear una nueva imagen con espacio adicional para el pie de página y el borde
	            BufferedImage qrImageWithFooterAndBorder = new BufferedImage(
	                    qrImage.getWidth() + 2 * borderSize+50,
	                    qrImage.getHeight() + footerHeight + 2 * borderSize,
	                    BufferedImage.TYPE_INT_ARGB
	            );
	            Graphics2D graphics = qrImageWithFooterAndBorder.createGraphics();

	            // Dibujar el fondo blanco
	            graphics.setColor(Color.WHITE);
	            graphics.fillRect(0, 0, qrImageWithFooterAndBorder.getWidth(), qrImageWithFooterAndBorder.getHeight());

	            // Dibujar el borde negro
	            graphics.setColor(Color.BLACK);
	            graphics.fillRect(0, 0, qrImageWithFooterAndBorder.getWidth(), borderSize); // borde superior
	            graphics.fillRect(0, 0, borderSize, qrImageWithFooterAndBorder.getHeight()); // borde izquierdo
	            graphics.fillRect(0, qrImageWithFooterAndBorder.getHeight() - borderSize, qrImageWithFooterAndBorder.getWidth(), borderSize); // borde inferior
	            graphics.fillRect(qrImageWithFooterAndBorder.getWidth() - borderSize, 0, borderSize, qrImageWithFooterAndBorder.getHeight()); // borde derecho

	            int x = (qrImageWithFooterAndBorder.getWidth() - qrImage.getWidth()) / 2;
	            int y = (qrImageWithFooterAndBorder.getHeight() - qrImage.getHeight() - footerHeight) / 2;

	            // Dibujar el código QR centrado en la nueva imagen
	            graphics.drawImage(qrImage, x, y, null);
	            // Dibujar el código QR en la nueva imagen
//	            graphics.drawImage(qrImage, borderSize, borderSize, null);

	            // Configurar la fuente y el color para el texto del pie de página
	            graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
	            graphics.setColor(Color.BLACK);

	            // Dibujar las dos líneas de texto en la parte inferior de la imagen
	            
	            String firstLine = worker.getEmployeeName() ;
	            String firstLine2 = worker.getEmployeeLastName() + " " + worker.getEmployeeSecondLastName();
	            String secondLine = "No. AFIL. IMSS: " + worker.getMembershipIMSS();
	            
	            
	            graphics.drawString(firstLine.toUpperCase(), borderSize + 10, qrImage.getHeight() + borderSize + (footerHeight / 2) - 50);
	            graphics.drawString(firstLine2.toUpperCase(), borderSize + 10, qrImage.getHeight() + borderSize + (footerHeight / 2) - 20);
	            graphics.drawString(secondLine, borderSize + 10, qrImage.getHeight() + borderSize + (footerHeight / 2)+10);

	            graphics.dispose();

	            // Convertir la imagen con el pie de página y el borde a un arreglo de bytes
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ImageIO.write(qrImageWithFooterAndBorder, "png", baos);
	            byte[] qrCodeBytes = baos.toByteArray();

	            qrCodeList.add(qrCodeBytes);
	        } catch (WriterException | IOException e) {
	            e.printStackTrace();
	        }
	    }

	    return qrCodeList;
	}

	
	 
	 public static void main(String[] args) {
	        ArrayList<PlantAccessWorker> stringList = new ArrayList<>();
	       PlantAccessRequest soli=new PlantAccessRequest();
	       soli.setId(5);

	       PlantAccessWorker woe=new PlantAccessWorker();
	       woe.setEmployeeName("gama");
	       woe.setEmployeeLastName("cruz");
	       woe.setEmployeeSecondLastName("gonzalez");
	       woe.setMembershipIMSS("555412587657");
	       
	       stringList.add(woe); 
	       
	        ArrayList<byte[]> qrCodeList = new QRutils().generateQRCodes(soli, stringList);

	        // Aquí puedes hacer algo con la lista de códigos QR, como guardarlos en archivos, etc.
	        for (int i = 0; i < qrCodeList.size(); i++) {
	            byte[] qrCode = qrCodeList.get(i);
	            // Ejemplo: guardar en archivo
	             try {
					Files.write(Paths.get("C:\\temp\\"+"qr" + i + ".png"), qrCode);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	 
	 }
}
