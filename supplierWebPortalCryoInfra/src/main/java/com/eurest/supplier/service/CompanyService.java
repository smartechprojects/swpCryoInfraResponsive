package com.eurest.supplier.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.CompanyDao;
import com.eurest.supplier.model.Company;

@Service("companyService")
public class CompanyService {
	
	@Autowired
	private CompanyDao companyDao;

	public Company getCompanyById(int id) {
		return companyDao.getCompanyById(id);
	}
	
	public Company saveCompany(Company o) {
		Company current = companyDao.searchByCompany(o.getCompany());
		if(current == null) {
			o.setCreationDate(new Date());
			Company c = companyDao.saveCompany(o);
			return c;
		}else {
			return null;
		}

	}
	
	public Company updateCompany(Company o) {
		return companyDao.updateCompany(o);
	}
	
	public List<Company> searchCriteria(String query, int start, int limit){
		return companyDao.searchCriteria(query, start, limit);
	}
	
	public int searchCriteriaCount(String query){
		return companyDao.searchCriteriaCount(query);
	}
	
	public Company searchByCompany(String company){
		return companyDao.searchByCompany(company);
	}
	
	public List<Company> searchActiveCompanies(String company){
		return companyDao.searchActiveCompanies(company);
	}
	
	public List<Company> getListCompany(){
		return companyDao.getListCompany();
	}
}
