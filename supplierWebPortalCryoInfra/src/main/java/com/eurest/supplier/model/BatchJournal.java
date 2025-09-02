package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

	@JsonAutoDetect
	@Entity
	@Table(name="batchjournal")
	public class BatchJournal {
		
		@SuppressWarnings("unused")
		private static final long serialVersionUID = 1L;
		@Id
		@Column
		@GeneratedValue(strategy=GenerationType.AUTO)
		private int id;
		
		private String batchID;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(iso = ISO.DATE_TIME)
		private Date creationDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(iso = ISO.DATE_TIME)
		private Date approvalDate;
		
		@Column(length = 1200)
		private String content;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		
		public String getBatchID() {
			return batchID;
		}

		public void setBatchID(String batchID) {
			this.batchID = batchID;
		}

		public Date getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		public Date getApprovalDate() {
			return approvalDate;
		}

		public void setApprovalDate(Date approvalDate) {
			this.approvalDate = approvalDate;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}


		
		
		
		
}
