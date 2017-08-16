package com.smartvalor.json.rq;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Registration request.
 */
@JsonInclude
public class RegistrationRq {
	/**
	 * The registration token, must be provided.
	 */
	private String token;
	
	/**
	 * Name of the registered person, not necessary real, or otherwise changeable visible name (not id, not official number)
	 */
	private String title;
	
	/**
	 * Arbitrary free text.
	 */
	private String description;
	
	/**
	 * Address, we can use to send verification letter.
	 */
	private String address;
	
	/**
	 * Phone, we can use to send verification SMS.
	 */
	private String phone;
	
	private String eMail;
	
	/**
	 * This provides valuable information where is the registration from (Switzerland? China?)
	 */
	private String ipAddress;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
