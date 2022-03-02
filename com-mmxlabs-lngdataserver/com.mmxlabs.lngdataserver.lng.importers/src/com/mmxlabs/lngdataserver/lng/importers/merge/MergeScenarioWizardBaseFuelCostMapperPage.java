/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;

public class MergeScenarioWizardBaseFuelCostMapperPage extends AbstractEObjectMergeScenarioWizardDataMapperPage {

	protected MergeScenarioWizardBaseFuelCostMapperPage(String title) {
		super(title, s -> ScenarioModelUtil.getCostModel(s), PricingPackage.Literals.COST_MODEL__BASE_FUEL_COSTS);
	}

	@Override
	protected String getName(Object o) {
		if (o instanceof BaseFuelCost) {
			BaseFuelCost bfc = (BaseFuelCost)o;
			return bfc.getFuel().getName();
		}
		else {
			return "Unknown";
		}
	}

	@Override
	protected List<? extends EObject> getEObjects(LNGScenarioModel sm) {
		return sm.getReferenceModel().getCostModel().getBaseFuelCosts();
	}
}
