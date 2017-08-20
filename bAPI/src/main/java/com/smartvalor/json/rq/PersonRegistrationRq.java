package com.smartvalor.json.rq;

import com.smartvalor.json.Address;

/**
 * Registration request to register the person.
 */
public class PersonRegistrationRq {
	
	// Authentication
	String token;
	
	String country;
	
	String fullName;
	
	// We need to agree on format here. JSON does not have the built-in date type.
	String birthDate;
	
	// This may require some normalization in the database but can be String in JSON message.
	String nationality;
	
	String placeOfBirth;
	
	String idType;
	
	String idNumber;
	
	Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	// We assume the person accepted terms and conditions. If not, there is no sense to sent the form at all.
	
	
}
