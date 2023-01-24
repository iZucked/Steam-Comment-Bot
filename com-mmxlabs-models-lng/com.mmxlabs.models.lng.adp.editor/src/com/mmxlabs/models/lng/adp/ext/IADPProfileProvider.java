/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

@NonNullByDefault
public interface IADPProfileProvider {

	void createProfiles(LNGScenarioModel scenarioModel, CommercialModel commercialModelModel, ADPModel existingModel);

	@Nullable
	PurchaseContractProfile createProfile(LNGScenarioModel scenarioModel, CommercialModel commercialModelModel, ADPModel existingModel, PurchaseContract contract);

	@Nullable
	SalesContractProfile createProfile(LNGScenarioModel scenarioModel, CommercialModel commercialModelModel, ADPModel existingModel, SalesContract contract);

}
