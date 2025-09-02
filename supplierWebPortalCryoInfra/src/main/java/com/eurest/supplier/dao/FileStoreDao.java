package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.FileStore;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.util.AppConstants;

@Repository("fileStoreDao")
@Transactional
public class FileStoreDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public FileStore getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (FileStore) session.get(FileStore.class, id);
	}
	
	public List<FileStore> getFilesPlantAccess(String uuid, boolean getB64) {
		Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(FileStore.class);
	   /* criteria.add(
	        (Criterion)Restrictions.disjunction()
	        .add((Criterion)Restrictions.eq("uuid", uuid)));*/
	    criteria.add((Criterion)Restrictions.eq("uuid", uuid));
	    
	    if(!getB64) {
	    criteria.setProjection(Projections.projectionList()
	    	    .add(Projections.property("id"), "id")
	    	    .add(Projections.property("dateUpload"), "dateUpload")
	    	    .add(Projections.property("originName"), "originName")
	    	    .add(Projections.property("namefile"), "namefile")
	    	    .add(Projections.property("fileType"), "fileType")
	    	    .add(Projections.property("documentType"), "documentType")
	    	    .add(Projections.property("numRefer"), "numRefer")
	    	    .add(Projections.property("status"), "status")
	    	    .add(Projections.property("uuid"), "uuid")
	    	);
	    criteria.setResultTransformer(Transformers.aliasToBean(FileStore.class));
	    }
	    
	    
	    List<FileStore> list = criteria.list();
	    /*if (!list.isEmpty())
	      return list.get(0); */
	    return list;
	}

	public List<FileStore> getFilesPlantAccessRequest(String uuid, boolean getB64) {
		Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(FileStore.class);
	    criteria.add((Criterion)Restrictions.eq("uuid", uuid));
	    criteria.add(Restrictions.like("documentType", "REQUEST_%"));
	    
	    if(!getB64) {
	    criteria.setProjection(Projections.projectionList()
	    	    .add(Projections.property("id"), "id")
	    	    .add(Projections.property("dateUpload"), "dateUpload")
	    	    .add(Projections.property("originName"), "originName")
	    	    .add(Projections.property("namefile"), "namefile")
	    	    .add(Projections.property("fileType"), "fileType")
	    	    .add(Projections.property("documentType"), "documentType")
	    	    .add(Projections.property("numRefer"), "numRefer")
	    	    .add(Projections.property("status"), "status")
	    	    .add(Projections.property("uuid"), "uuid")
	    	);
	    criteria.setResultTransformer(Transformers.aliasToBean(FileStore.class));
	    }
	    
	    
	    List<FileStore> list = criteria.list();
	    /*if (!list.isEmpty())
	      return list.get(0); */
	    return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<FileStore> getFilesPlantAccessWorker(String idRequest, int idWorker, boolean getB64) {
		Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(FileStore.class);
	    criteria.add((Criterion)Restrictions.eq("uuid", idRequest));
	    criteria.add((Criterion)Restrictions.eq("numRefer", idWorker));
	    
	    if(!getB64) {
	    criteria.setProjection(Projections.projectionList()
	    	    .add(Projections.property("id"), "id")
	    	    .add(Projections.property("dateUpload"), "dateUpload")
	    	    .add(Projections.property("originName"), "originName")
	    	    .add(Projections.property("namefile"), "namefile")
	    	    .add(Projections.property("fileType"), "fileType")
	    	    .add(Projections.property("documentType"), "documentType")
	    	    .add(Projections.property("numRefer"), "numRefer")
	    	    .add(Projections.property("status"), "status")
	    	    .add(Projections.property("uuid"), "uuid")
	    	);
	    criteria.setResultTransformer(Transformers.aliasToBean(FileStore.class));
	    }
	    
	    List<FileStore> list = criteria.list();
	    return list;
	}
	
	public void save(FileStore o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(FileStore o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteFiscalDocument(FileStore o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}


	public int updateFileRequest(String uuid, String idref) {
		
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "update FileStore "
				+ " set namefile=(concat(documentType,(IF(INSTR(documentType , 'WORKER') > 0, concat('_',numRefer), '')),'.',SUBSTRING_INDEX(originName, '.', -1))), "
				+ " numRefer = IF(INSTR(documentType , 'WORKER') > 0,numRefer,'"+idref+"'), "
				+ " status = 'COMPPLETED' "
				+ " where namefile = '"+uuid+"'"; 
		Query query = session.createQuery(hql); 
		
	    return query.executeUpdate();
	}
	
	public List<FileStore> getFileStores(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FileStore.class);
		criteria.add(Restrictions.like("namefile", "%" + uuid + "%"));
		return criteria.list();
	}
	public List<FileStore> deleteFilesPlantAccess(int idWorker, String documentType) {
		Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(FileStore.class);
	    
	    
	    criteria.add((Criterion)Restrictions.eq("numRefer", idWorker));
	    criteria.add((Criterion)Restrictions.eq("uuid", idWorker+""));
	    criteria.add((Criterion) Restrictions.in("documentType", documentType.split(",")));

	    criteria.setProjection(Projections.projectionList()
	    	    .add(Projections.property("id"), "id")
	    	    .add(Projections.property("dateUpload"), "dateUpload")
	    	    .add(Projections.property("originName"), "originName")
	    	    .add(Projections.property("namefile"), "namefile")
	    	    .add(Projections.property("fileType"), "fileType")
	    	    .add(Projections.property("documentType"), "documentType")
	    	    .add(Projections.property("numRefer"), "numRefer")
	    	    .add(Projections.property("status"), "status")
	    	    .add(Projections.property("uuid"), "uuid")
	    	);
	    criteria.setResultTransformer(Transformers.aliasToBean(FileStore.class));
	    
	    
	    List<FileStore> filesToDelete = criteria.list();
	    for (FileStore file : filesToDelete) {
	        session.delete(file);
	    }
	    return getFilesPlantAccess(idWorker+"", false);
	}

}
