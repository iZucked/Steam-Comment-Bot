/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import org.eclipse.jdt.annotation.NonNull;

public enum CloudManifestProblemType {
	OPTIMISATION("optimisation"), OPTIONISER("optioniser"), SANDBOX("sandbox");

	@NonNull
	private final String manifestString;

	private CloudManifestProblemType(@NonNull final String manifestString) {
		this.manifestString = manifestString;
	}

	@NonNull
	public String getManifestString() {
		return manifestString;
	}
}
