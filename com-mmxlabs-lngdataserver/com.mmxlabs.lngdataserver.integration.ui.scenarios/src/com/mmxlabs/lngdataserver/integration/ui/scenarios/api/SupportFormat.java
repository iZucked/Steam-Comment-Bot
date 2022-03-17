/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.util.Objects;

public class SupportFormat {
	private String name;
	private String version;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof SupportFormat) {
			SupportFormat other = (SupportFormat) obj;
			return Objects.equals(name, other.name) && Objects.equals(version, other.version);

		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}
}
