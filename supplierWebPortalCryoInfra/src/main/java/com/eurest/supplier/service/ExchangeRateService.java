package com.eurest.supplier.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.ExchangeRateDao;
import com.eurest.supplier.model.ExchangeRate;

@Service("exchangeRateService")
public class ExchangeRateService {
	
	@Autowired
	private ExchangeRateDao exchangeRateDao;

	public List<ExchangeRate> saveMultipleExchangeRate(List<ExchangeRate> objList) {
		return exchangeRateDao.saveMultipleExchangeRate(objList);
	}
	
	public List<ExchangeRate> getExchangeRateByList(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit){
		return exchangeRateDao.getExchangeRateFromToday(dateFrom, currencyCode, currencyCodeTo, start, limit);
	}
	
	public List<ExchangeRate> getExchangeRateFromToday(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit){
		return exchangeRateDao.getExchangeRateFromToday(dateFrom, currencyCode, currencyCodeTo, start, limit);
	}

	public List<ExchangeRate> getExchangeRateBeforeToday(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit){
		return exchangeRateDao.getExchangeRateBeforeToday(dateFrom, currencyCode, currencyCodeTo, start, limit);
	}
	
	public List<ExchangeRate> getExchangeRateAfterToday(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit){
		return exchangeRateDao.getExchangeRateAfterToday(dateFrom, currencyCode, currencyCodeTo, start, limit);
	}
}
