/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.scenario;

import org.junit.jupiter.api.Assertions;

import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.CSVImporter;

/**
 * Subclass of {@link CSVImporter} calling Assertion.fail for import exceptions
 * rather than just logging to stderr.
 */
public class ITSCSVImporter extends CSVImporter {

	@Override
	public void handleThrowable(Throwable th) {
		Assertions.fail(th.getMessage());
	}
}
