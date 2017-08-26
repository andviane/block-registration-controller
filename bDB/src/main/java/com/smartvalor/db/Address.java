package com.smartvalor.db;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The address/contact entry, use in various messages. 
 */
@Entity
public class Address {
	
	@Id
	private UUID id;	
	
	String country;	

	String streetName;
	
	/**
	 * While "number", may be something like 12A
	 */
	String houseNumber;
	
	String zipCode;
	
	String city;
	
	String phone;
	
	String email;
	
	@CreationTimestamp		
	Timestamp created;
	
	@UpdateTimestamp
	Timestamp updated;
	
	// Used if this is the person address.
	@ManyToOne	
	Person person;
	
	// Used if this is the official business entity address without the person associated.
	@ManyToOne
	BusinessEntity businessEntity;
	
	boolean deleted;

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id == null ? 0: id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (id == null) {
			return false; // unknown not equals to any
		}
		if (obj instanceof Address) {
			Address other = (Address) obj;
			return id.equals(other.id);
		}
		return false;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}	
}
