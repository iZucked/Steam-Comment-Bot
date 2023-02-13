/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

public class CustomReportModelVersionHeader {
	private int modelVersion;
	private String type; // Some sort of type field
	private int flags; // Misc flags

	public int getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(int modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}
}
