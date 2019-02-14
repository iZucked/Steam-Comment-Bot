/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.cases;

import org.eclipse.jdt.annotation.NonNull;

public class UATCase extends AbstractUATCase {

	public final @NonNull String cargoName;

	public UATCase(final @NonNull String lingoFilePath, final @NonNull String cargoName) {
		super(lingoFilePath);
		this.cargoName = cargoName;
	}

}
