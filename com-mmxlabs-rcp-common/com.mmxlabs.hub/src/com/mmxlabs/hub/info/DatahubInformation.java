/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.info;

public class DatahubInformation {

	private String version;
	private String authenticationScheme;
	private String instanceTag;

	public DatahubInformation() {
		super();
	}

	public DatahubInformation(String version, String authenticationScheme) {
		this.version = version;
		this.authenticationScheme = authenticationScheme;
	}

	public String getInstanceTag() {
		return instanceTag;
	}

	public void setInstanceTag(String instanceTag) {
		this.instanceTag = instanceTag;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setAuthenticationScheme(String authenticationScheme) {
		this.authenticationScheme = authenticationScheme;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthenticationScheme() {
		return authenticationScheme;
	}
}
