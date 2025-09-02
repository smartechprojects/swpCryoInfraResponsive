package com.eurest.supplier.dto;

public class OrdenRequestDTO {

	 private String OrdenTemporal;
     private String TipoOrden;
     private long FolioOrden;
	public String getOrdenTemporal() {
		return OrdenTemporal;
	}
	public void setOrdenTemporal(String ordenTemporal) {
		OrdenTemporal = ordenTemporal;
	}
	public String getTipoOrden() {
		return TipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		TipoOrden = tipoOrden;
	}
	public long getFolioOrden() {
		return FolioOrden;
	}
	public void setFolioOrden(long folioOrden) {
		FolioOrden = folioOrden;
	}
     
     
     
}
