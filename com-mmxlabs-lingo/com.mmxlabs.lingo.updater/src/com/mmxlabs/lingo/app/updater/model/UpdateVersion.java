/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.model;

import org.eclipse.jdt.annotation.NonNull;

public class UpdateVersion {

	private String code;
	private String version;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isBetter(Version v) {
		if (v != null) {
			Version tmp = Version.fromString(version);
			
			return tmp.compareTo(v) > 0;
		}
		
		return false;
	}
	public boolean isBetter(UpdateVersion v) {
		if (v != null) {
			Version a = Version.fromString(version);
			Version b = Version.fromString(v.version);
			
			return a.compareTo(b) > 0;
		}
		
		return false;
	}
	
	@Override
	public @NonNull String toString() {
		return version;
	}
}
