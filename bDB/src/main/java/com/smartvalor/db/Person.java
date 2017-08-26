package com.smartvalor.db;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Person {
	
	@Id
	@Column(updatable=false)	
	private UUID id;

	// User-visible number (id is secret)
	private String no;	
	
	private String fullName;
	
	// We need to agree on format here. JSON does not have the built-in date type.
	private Timestamp birthDate;
	
	// This may require some normalization in the database but can be String in JSON message.
	private String nationality;
	
	private String placeOfBirth;
	
	private String idType;
	
	private String idNumber;
	
	// Business entity if the person belongs to such. Independently registered
	// persons have this field not set. It would be very unusual for the person
	// to belong to more than one business entity but theoretically may be
	// (works part time id a company and creates own company, works part time
	// in two companies, independent lawyer representing multiple clients, etc).
	@ManyToMany(targetEntity = Address.class, mappedBy = "person")	
	private Set<BusinessEntity> businessEntity;
	
	// Multiple addresses may exist for the same person. If just additional phone number 
	// or e-mail address, many fields can be null. I try to avoid creating really many 
	// tables without the need.
	@OneToMany(targetEntity = Address.class, mappedBy = "person")
	private Set<Address> address = new LinkedHashSet<Address>();
	
	@CreationTimestamp	
	Timestamp created;
	
	@UpdateTimestamp
	Timestamp updated;	
	
	boolean deleted;
	
	boolean reviewed;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Timestamp getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getUpdated() {
		return updated;
	}
}
