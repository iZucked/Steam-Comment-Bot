/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.utils;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IMullRelativeEntitlementImportCommandProvider {
	public void run(@NonNull final MullSubprofile subprofile, @NonNull final ScenarioInstance scenarioInstance);
}
