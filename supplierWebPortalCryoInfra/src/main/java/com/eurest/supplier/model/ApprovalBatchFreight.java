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
	@Table(name="approvalBatchFreight")
	public class ApprovalBatchFreight {
		
		@SuppressWarnings("unused")
		private static final long serialVersionUID = 1L;
		@Id
		@Column
		@GeneratedValue(strategy=GenerationType.AUTO)
		private int id;
		
		private String batchID;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(iso = ISO.DATE_TIME)
		private Date approvalDate;
		
		private String approver;
		
		private int step;
		
		private String action;

		private String message;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Date getApprovalDate() {
			return approvalDate;
		}

		public void setApprovalDate(Date approvalDate) {
			this.approvalDate = approvalDate;
		}

		public String getApprover() {
			return approver;
		}

		public void setApprover(String approver) {
			this.approver = approver;
		}

		public int getStep() {
			return step;
		}

		public void setStep(int step) {
			this.step = step;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getBatchID() {
			return batchID;
		}

		public void setBatchID(String batchID) {
			this.batchID = batchID;
		}

		
		
}
