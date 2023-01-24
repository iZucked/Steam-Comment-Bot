/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

public enum LicenseState {
	Valid, //
	Expired("License has expired. Please contact Minimax Labs."), //
	Unknown("Unknown problem validating license file."), //
	NotYetValid("License is not valid yet. Please contact Minimax Labs."), //
	KeystoreNotFound("Unable to find license file");

	private final String message;

	private LicenseState() {
		message = toString();
	}

	private LicenseState(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}