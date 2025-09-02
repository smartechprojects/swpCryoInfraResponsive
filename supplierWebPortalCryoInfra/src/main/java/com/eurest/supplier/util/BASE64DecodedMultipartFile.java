package com.eurest.supplier.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;

public class BASE64DecodedMultipartFile implements FileItem {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final byte[] imgContent;
    private final FileItem fileItem;

    public BASE64DecodedMultipartFile(FileItem file,byte[] imgContent) {
        this.fileItem = file;
        this.imgContent=imgContent;
    }

	@Override
	public FileItemHeaders getHeaders() {
		// TODO Auto-generated method stub
		return fileItem.getHeaders();
	}

	@Override
	public void setHeaders(FileItemHeaders headers) {
		// TODO Auto-generated method stub
		fileItem.setHeaders(headers);
		
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return fileItem.getInputStream();
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return fileItem.getContentType();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return fileItem.getName();
	}

	@Override
	public boolean isInMemory() {
		// TODO Auto-generated method stub
		return fileItem.isInMemory();
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return fileItem.getSize();
	}

	@Override
	public byte[] get() {
		// TODO Auto-generated method stub
		return imgContent;
	}

	@Override
	public String getString(String encoding) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return fileItem.getString(encoding);
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return fileItem.getString();
	}

	@Override
	public void write(File file) throws Exception {
		// TODO Auto-generated method st
		fileItem.write(file);
		
	}

	@Override
	public void delete() {
		fileItem.delete();
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFieldName() {
		// TODO Auto-generated method stub
		return fileItem.getFieldName();
	}

	@Override
	public void setFieldName(String name) {
		// TODO Auto-generated method stub
		fileItem.setFieldName(name);
	}

	@Override
	public boolean isFormField() {
		// TODO Auto-generated method stub
		return fileItem.isFormField();
	}

	@Override
	public void setFormField(boolean state) {
		// TODO Auto-generated method stub
		fileItem.setFormField(state);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return fileItem.getOutputStream();
	}
    
    
    
    
    
}