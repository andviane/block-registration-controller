package com.smartvalor.db;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Conceptual high level entity that contains multiple accounts (different currency, etc).
 */
@Entity
public class Contract {

	/**
	 * UUIDs allow easy replication, can be generated outside db and are more secure as the
	 * sequence is not predictable. Let's give a try for UUIDs.
	 */	
	@Id
	private UUID id;

	// User-visible number (id is secret)
	private String no;
	
	private Timestamp created;
	
	private Timestamp lastUpdated;

	private boolean deleted;
	
	private boolean reviewed;

	private String address;
	
	private String phone;
	
	private String eMail;
	
	private String ipAddress;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	private String title;

	@Column(length = 65536)
	private String description;
	
	private String type;

	@OneToMany(targetEntity = Account.class, mappedBy = "contract")
	private Set<Account> accounts = new LinkedHashSet<Account>();

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String name) {
		this.no = name;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static boolean checkIgnorable(Contract existing) {
		if (existing != null) {
			return existing.isDeleted();
		}
		// New - cannot be ignored
		return false;
	}
}
