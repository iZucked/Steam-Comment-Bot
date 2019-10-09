/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.scenario.service.model.ScenarioService;

public class BaseCaseFlagsPropertyTester extends PropertyTester {
	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {

		if (property.equals("isservice")) {
			if (receiver instanceof ScenarioService) {
				ScenarioService ss = (ScenarioService) receiver;
				if (ss.getServiceID().startsWith("base-case-")) {
					return true;
				}
			}
			return false;
		} else if (property.equals("needslocking")) {
			return BaseCaseServiceClient.INSTANCE.needsLocking();
		} else if (property.equals("lockedbyme")) {
			return BaseCaseServiceClient.INSTANCE.isServiceLockedByMe();
		} else if (property.equals("locked")) {
			return BaseCaseServiceClient.INSTANCE.isServiceLocked();
		} else if (property.equals("unlocked")) {
			return !BaseCaseServiceClient.INSTANCE.isServiceLocked();
		}
		return false;
	}
}
