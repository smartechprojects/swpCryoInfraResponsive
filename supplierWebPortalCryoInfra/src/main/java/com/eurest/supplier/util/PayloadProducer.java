package com.eurest.supplier.util;

import com.eurest.supplier.dto.InvoiceDTO;

public class PayloadProducer {

		public static String getCFDIPayload(InvoiceDTO doc) {
			String rfcEmisor = doc.getRfcEmisor().trim();
			rfcEmisor = rfcEmisor.replaceAll("&amp;","&").replaceAll("&","&amp;");
			String SOAPRequest = "<![CDATA[?" +
					"re=" + rfcEmisor + "&" +
					"rr=" + doc.getRfcReceptor() + "&" +
					"tt=" + doc.getTotal() + "&" +
					"id=" + doc.getUuid().trim() +
					"]]>";
			return SOAPRequest;
		
		}

}
