/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.model;

public class ServerResponse {
	private int code;
	private String message;
	private String details;
	
	public ServerResponse(int code, String message, String details) {
		super();
		this.code = code;
		this.message = message;
		this.details = details;
	}

	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}

}