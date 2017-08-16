package com.smartvalor.hl.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;

import com.smartvalor.db.Account;
import com.smartvalor.db.Contract;

/**
 * Contract record to render
 */
public class ContractRecord {
	private final Contract contract;

	private int newAccounts;

	private int approvedAccounts;

	private int closedAccounts;

	private int lockedAccounts;

	private int totalAccounts;

	private String status;

	private Timestamp lastActivity;

	public ContractRecord(Contract contract) {
		this.contract = contract;
		Collection<Account> accounts = contract.getAccounts();
		totalAccounts = accounts.size();
		if (accounts != null) {
			for (Account account : accounts) {
				if (lastActivity == null || account.getLastProcessed().before(lastActivity))
					lastActivity = account.getLastProcessed();

				switch (account.getStatus()) {
				case Account.NEW:
					newAccounts++;
					break;
				case Account.APPROVED:
					approvedAccounts++;
					break;
				case Account.CLOSED:
					closedAccounts++;
					break;
				case Account.LOCKED:
					lockedAccounts++;
					break;
				default:
					break;
				}
			}
		}

		if (contract.isDeleted()) {
			status = "Deleted";
		}
	}

	public Set<Account> getAccounts() {
		return contract.getAccounts();
	}

	public static String formatDate(Timestamp dat) {
		if (dat == null || dat.getTime() == 0) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		return format.format(dat);
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return contract.getTitle();
	}

	public String getDescription() {
		return contract.getDescription();
	}

	public String getType() {
		return contract.getType();
	}

	public String getCreated() {
		return formatDate(contract.getCreated());
	}

	public String getLastActivity() {
		return formatDate(lastActivity);
	}

	public int getNewAccounts() {
		return newAccounts;
	}

	public void setNewAccounts(int newAccounts) {
		this.newAccounts = newAccounts;
	}

	public int getApprovedAccounts() {
		return approvedAccounts;
	}

	public void setApprovedAccounts(int approvedAccounts) {
		this.approvedAccounts = approvedAccounts;
	}

	public int getClosedAccounts() {
		return closedAccounts;
	}

	public void setClosedAccounts(int closedAccounts) {
		this.closedAccounts = closedAccounts;
	}

	public int getLockedAccounts() {
		return lockedAccounts;
	}

	public void setLockedAccounts(int lockedAccounts) {
		this.lockedAccounts = lockedAccounts;
	}

	public int getTotalAccounts() {
		return totalAccounts;
	}

	public void setTotalAccounts(int totalAccounts) {
		this.totalAccounts = totalAccounts;
	}

	public Contract getContract() {
		return contract;
	}

	public String getPhone() {
		return contract.getPhone();
	}

	public String geteMail() {
		return contract.geteMail();
	}

	public String getIpAddress() {
		return contract.getIpAddress();
	}

	public boolean isFinalized() {
		return contract.isReviewed();
	}

	public boolean isDeleted() {
		return contract.isDeleted();
	}

	public String getName() {
		return contract.getNo();
	}

}
