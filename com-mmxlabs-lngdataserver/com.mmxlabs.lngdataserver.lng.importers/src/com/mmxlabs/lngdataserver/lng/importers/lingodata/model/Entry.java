/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.model;

public class Entry {
	private String dataVersion;
	private String modelVersion;
	private String type;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(final String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public String getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(final String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

}
