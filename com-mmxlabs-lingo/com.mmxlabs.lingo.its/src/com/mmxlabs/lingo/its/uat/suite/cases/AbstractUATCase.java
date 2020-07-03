/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.cases;

import org.eclipse.jdt.annotation.NonNull;

public abstract class AbstractUATCase {

	public final @NonNull String lingoFilePath;

	public AbstractUATCase(final @NonNull String lingoFilePath) {
		this.lingoFilePath = lingoFilePath;
	}
}
