/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class DefaultExportContext implements IMMXExportContext {

	private final MMXRootObject rootObject;

	private final char decimalSeparator;

	private IScenarioDataProvider scenarioDataProvider;

	public DefaultExportContext(final MMXRootObject rootObject, IScenarioDataProvider scenarioDataProvider, final char decimalSeparator) {
		this.rootObject = rootObject;
		this.scenarioDataProvider = scenarioDataProvider;
		this.decimalSeparator = decimalSeparator;
	}

	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
	}

	@Override
	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	@Override
	public IScenarioDataProvider getScenarioDataProvider() {
		return scenarioDataProvider;
	}
}
