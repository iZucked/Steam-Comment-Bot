/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @author hinton
 * 
 */
public class BaseFuelCostModelCommandProvider extends BaseModelCommandProvider<Object> {
	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return addedObject instanceof BaseFuel;
	}

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return shouldHandleAddition(deletedObject, overrides, editSet);
	}

	@Override
	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object added, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (added instanceof BaseFuel) {
			if (!(rootObject instanceof LNGScenarioModel)) {
				return null;
			}
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

			final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioModel);
			for (final BaseFuelCost existing : costModel.getBaseFuelCosts()) {
				if (existing.getFuel() == added) {
					return null;
				}
			}
			final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
			cost.setFuel((BaseFuel) added);
			return AddCommand.create(domain, costModel, PricingPackage.eINSTANCE.getCostModel_BaseFuelCosts(), cost);
		}
		return null;
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (deleted instanceof BaseFuel) {
			if (!(rootObject instanceof LNGScenarioModel)) {
				return null;
			}
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

			final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioModel);
			for (final BaseFuelCost cost : costModel.getBaseFuelCosts()) {
				if (cost.getFuel() == deleted) {
					return DeleteCommand.create(domain, cost);
				}
			}
		}
		return null;
	}
}
