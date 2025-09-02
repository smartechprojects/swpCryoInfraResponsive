package com.eurest.supplier.util;

public class ExtJSFormResult {
	 
    private boolean success;
    @SuppressWarnings("unused")
	private String message;
    
    public void setMessage(String msg){
    	this.message = msg;
    }
 
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
 
    public String toString(){
        return "{success:"+this.success+"}";
    }
}
