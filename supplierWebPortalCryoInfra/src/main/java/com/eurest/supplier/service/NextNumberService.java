package com.eurest.supplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.NextNumberDao;
import com.eurest.supplier.model.NextNumber;

@Service("nextNumberService")
public class NextNumberService {
	
	@Autowired
	private NextNumberDao nextNumberDao;
	
	public synchronized NextNumber getNextNumber(String module){
		return nextNumberDao.getNextNumber(module);
	}
	
	public void updateNextNumber(NextNumber o) {
		nextNumberDao.updateNextNumber(o);
	}

}
