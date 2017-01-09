/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.internal.ScenarioServiceSelectionProvider;

public class PinStatePropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (receiver instanceof ScenarioInstance) {
			if (property.equals("pinned")) {
				final ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				final ScenarioInstance pinned = selectionProvider.getPinnedInstance();
				return pinned == receiver;
			}
		}
		return false;
	}
}
