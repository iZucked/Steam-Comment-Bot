/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class PinStatePropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (receiver instanceof ScenarioInstance) {
			ScenarioInstance scenarioInstance = (ScenarioInstance) receiver;
			if (property.equals("pinned")) {
				final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				return selectionProvider.isPinned(scenarioInstance);
			}
		}
		return false;
	}
}
