package com.eurest.supplier.util;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadBean {
 
    private CommonsMultipartFile file;
    private CommonsMultipartFile fileTwo;
    private CommonsMultipartFile fileThree;
 
    public CommonsMultipartFile getFile() {
        return file;
    }
 
    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }

	public CommonsMultipartFile getFileTwo() {
		return fileTwo;
	}

	public void setFileTwo(CommonsMultipartFile fileTwo) {
		this.fileTwo = fileTwo;
	}

	public CommonsMultipartFile getFileThree() {
		return fileThree;
	}

	public void setFileThree(CommonsMultipartFile fileThree) {
		this.fileThree = fileThree;
	}
	
	
    
    
}
