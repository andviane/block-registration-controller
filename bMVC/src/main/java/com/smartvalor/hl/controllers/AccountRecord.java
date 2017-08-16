package com.smartvalor.hl.controllers;

import com.smartvalor.db.Account;

/**
 * Structure for rendering accounts.
 */
public class AccountRecord {
	protected final Account account;

	public AccountRecord(Account account) {
		this.account = account;
	}
	
	public Account getAccount() {
		return account;
	}

	public ContractRecord getContract() {
		return new ContractRecord(account.getContract());
	}

	public String getLastProcessed() {
		return ContractRecord.formatDate(account.getLastProcessed());
	}

	public String getCreated() {
		return ContractRecord.formatDate(account.getCreated());
	}
	
	public String getAmount() {
		// TODO currency formatting required here.
		return Long.toString(account.getAmount());
	}
	
	public String getStatus() {
		switch (account.getStatus()) {
		case Account.NEW:
			return "New";
		case Account.APPROVED:
			return "Approved";
		case Account.LOCKED:
			return "Locked";
		case Account.CLOSED:
			return "Closed";
		default:
		}
		return "Unknown (" + account.getStatus() + ")";
	}
	
}
