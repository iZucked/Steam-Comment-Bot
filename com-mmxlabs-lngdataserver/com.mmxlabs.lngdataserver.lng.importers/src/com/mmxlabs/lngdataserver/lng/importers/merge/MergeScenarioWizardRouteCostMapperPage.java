/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.AVesselSet;

public class MergeScenarioWizardRouteCostMapperPage extends AbstractEObjectMergeScenarioWizardDataMapperPage {

	public MergeScenarioWizardRouteCostMapperPage(String title) {
		super(title, s -> ScenarioModelUtil.findReferenceModel(s).getCostModel(), PricingPackage.Literals.COST_MODEL__ROUTE_COSTS);
	}

	@Override
	protected String getName(Object o) {
		if (o instanceof RouteCost) {
			var rc = (RouteCost) o;
			String name = "";
			if (rc.getRouteOption() != null) {
				name += rc.getRouteOption().getName();
			}
			if (rc.getVessels() != null) {
				if (rc.getVessels().size() > 0) {
					name += ":(";
				}
				boolean firstV = true;
				for (AVesselSet<Vessel> v : rc.getVessels()) {
					if (!firstV) {
						name += ",";
					}
					name += v.getName();
					firstV = false;
				}
				if (rc.getVessels().size() > 0) {
					name += ")";
				}
			}
			return name;
		} else {
			return super.getName(o);
		}
	}

	@Override
	protected List<? extends EObject> getEObjects(LNGScenarioModel sm) {
		List<? extends EObject> emfObjects = ScenarioModelUtil.findReferenceModel(sm).getCostModel().getRouteCosts();
		return emfObjects;
	}
}
