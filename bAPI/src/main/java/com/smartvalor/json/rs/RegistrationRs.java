package com.smartvalor.json.rs;

/**
 * Registration response.
 */
public class RegistrationRs {
	
	/**
	 * The contract number as issued by successful registration.
	 */
	private String contractNo;
	
	/**
	 * Response code, 0 if success (code must be converted into localized message on the server side.
	 */
	int responseCode;

	public String getContractNo() {
		return contractNo;
	}

	public void setRegisteredEntityNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	

}
