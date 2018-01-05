/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

@NonNullByDefault
public interface IADPBindingRuleProvider {
	void generateBindingRules(ADPModel adpModel, LNGScenarioModel lngScenarioModel);

}
