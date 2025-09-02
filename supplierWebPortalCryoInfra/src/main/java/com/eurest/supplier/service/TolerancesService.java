package com.eurest.supplier.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.TolerancesDao;
import com.eurest.supplier.model.Tolerances;

@Service("tolerancesService")
public class TolerancesService {

	@Autowired
	private TolerancesDao tolerancesDao;
	
	public List<Tolerances> getItemTolerances(List<Integer> items){
		return tolerancesDao.getItemTolerances(items);
	}
	
	public void updateTolerance(Tolerances o) {
		tolerancesDao.updateTolerance(o);
	}
	
	public void saveMultiple(List<Tolerances> list) {
		tolerancesDao.saveMultiple(list);
	}
	
	public void deleteRecords(){
		tolerancesDao.deleteRecords();
	}
	
	public Map<String,Tolerances> getTolerancesMap(){
		List<Tolerances> list = tolerancesDao.getTolerances();
		Map<String,Tolerances> map = new HashMap<String,Tolerances>();
		if(!list.isEmpty()) {
			for(Tolerances t : list) {
				String key = String.valueOf(t.getItemNumber()).trim();
				map.put(key, t);
			}
		}
		return map;
	}
}
