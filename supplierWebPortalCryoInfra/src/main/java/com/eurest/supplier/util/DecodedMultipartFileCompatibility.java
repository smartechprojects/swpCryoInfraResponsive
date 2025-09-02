package com.eurest.supplier.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.springframework.web.multipart.MultipartFile;

public class DecodedMultipartFileCompatibility implements MultipartFile {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final byte[] imgContent;
    private final MultipartFile fileItem;

    public DecodedMultipartFileCompatibility(MultipartFile file,byte[] imgContent) {
        this.fileItem = file;
        this.imgContent=imgContent;
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return fileItem.getName();
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return fileItem.getOriginalFilename();
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return fileItem.getContentType();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return fileItem.isEmpty();
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return imgContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		// TODO Auto-generated method stub
		return imgContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return fileItem.getInputStream();
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		fileItem.transferTo(dest);
	}

	
    
}