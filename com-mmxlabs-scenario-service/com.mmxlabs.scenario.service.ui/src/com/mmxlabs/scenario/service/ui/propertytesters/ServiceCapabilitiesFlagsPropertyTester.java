/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioService;

public class ServiceCapabilitiesFlagsPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (receiver instanceof Container) {

			Container container = (Container) receiver;
			while (container != null && !(container instanceof ScenarioService)) {
				container = container.getParent();
			}

			if (container instanceof ScenarioService) {
				final ScenarioService scenarioService = (ScenarioService) container;
				if (property.equals("forking")) {
					return scenarioService.isSupportsForking();
				}
				else if (property.equals("import")) {
					return scenarioService.isSupportsImport();
				}
			}
		}
		return false;
	}
}
