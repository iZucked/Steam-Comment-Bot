/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class ScenarioInstanceFlagsPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (receiver instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) receiver;
			if (property.equals("readonly")) {
				return scenarioInstance.isReadonly();
			} else if (property.equals("dirty")) {
				final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
				try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("ScenarioInstanceFlagsPropertyTester:1")) {
					if (ref != null) {
						return ref.isDirty();
					}
					return false;
				}
			} else if (property.equals("archived")) {
				return scenarioInstance.isArchived();
			} else if (property.equals("locked")) {
				final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
				try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("ScenarioInstanceFlagsPropertyTester:2")) {
					if (ref != null) {
						return ref.isLocked();
					}
					return false;
				}
			} else if (property.equals("hidden")) {
				return scenarioInstance.isHidden();
			}
		}
		return false;
	}
}
