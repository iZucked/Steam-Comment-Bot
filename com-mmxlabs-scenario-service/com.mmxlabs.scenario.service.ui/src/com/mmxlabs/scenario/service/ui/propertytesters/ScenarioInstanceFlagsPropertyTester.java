/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class ScenarioInstanceFlagsPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (receiver instanceof ScenarioInstance scenarioInstance) {
			if (property.equals("readonly")) {
				return scenarioInstance.isReadonly();
			} else if (property.equals("dirty")) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
				if (modelRecord != null) {
					try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("ScenarioInstanceFlagsPropertyTester:1")) {
						if (ref != null) {
							return ref.isDirty();
						}
					}
				}
				return false;
			} else if (property.equals("archived")) {
				return scenarioInstance.isArchived();
			} else if (property.equals("locked")) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
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
