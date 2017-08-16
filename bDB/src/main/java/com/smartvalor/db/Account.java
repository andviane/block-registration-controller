package com.smartvalor.db;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Conceptual second level entity, contract has multiple instance of these.
 */
@Entity
public class Account {
	public static final int NEW = 0;	
	public static final int APPROVED = 1;
	public static final int LOCKED = 2;
	public static final int CLOSED = 3;

	/**
	 * UUIDs allow easy replication, can be generated outside db and are more secure as the
	 * sequence is not predicatable. Let's give a try for UUIDs.
	 */
	@Id
	private UUID id;
	
	// The visible account name, account number, etc. id is secret
	private String no;
	
	// Smallest possible amount (cents, etc).
	private long amount;
	
	// The currency code
	private String currency;
	
	private Timestamp created;
	
	private Timestamp lastProcessed;	
	
	private int status;
	
	@ManyToOne
	private Contract contract;

	public UUID getId() {
		return id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String name) {
		this.no = name;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public Timestamp getLastProcessed() {
		return lastProcessed;
	}

	public void setLastProcessed(Timestamp lastProcessed) {
		this.lastProcessed = lastProcessed;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
}
