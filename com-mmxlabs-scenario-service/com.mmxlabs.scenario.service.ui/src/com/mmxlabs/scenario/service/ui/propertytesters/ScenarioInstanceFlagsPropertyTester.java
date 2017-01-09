/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioInstanceFlagsPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (receiver instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) receiver;
			if (property.equals("readonly")) {
				return scenarioInstance.isReadonly();
			} else if (property.equals("dirty")) {
				return scenarioInstance.isDirty();
			} else if (property.equals("archived")) {
				return scenarioInstance.isArchived();
			} else if (property.equals("locked")) {
				return scenarioInstance.isLocked();
			} else if (property.equals("hidden")) {
				return scenarioInstance.isHidden();
			}
		}
		return false;
	}
}
