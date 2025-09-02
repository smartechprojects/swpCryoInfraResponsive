package com.eurest.supplier.edi;

import java.util.List;

public class BatchJournalDTO {
	
	List<BatchJournalEntryDTO> journalEntries;
	List<BatchVoucherTransactionsDTO> voucherEntries;
	
	public List<BatchJournalEntryDTO> getJournalEntries() {
		return journalEntries;
	}
	public void setJournalEntries(List<BatchJournalEntryDTO> journalEntries) {
		this.journalEntries = journalEntries;
	}
	public List<BatchVoucherTransactionsDTO> getVoucherEntries() {
		return voucherEntries;
	}
	public void setVoucherEntries(List<BatchVoucherTransactionsDTO> voucherEntries) {
		this.voucherEntries = voucherEntries;
	}

}
