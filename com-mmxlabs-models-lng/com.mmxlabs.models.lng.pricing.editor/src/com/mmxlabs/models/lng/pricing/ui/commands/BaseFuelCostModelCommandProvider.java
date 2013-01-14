/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
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
			final PricingModel pricing = rootObject.getSubModel(PricingModel.class);
			if (pricing == null)
				return null;
			for (final BaseFuelCost existing : pricing.getFleetCost().getBaseFuelPrices()) {
				if (existing.getFuel() == added)
					return null;
			}
			final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
			cost.setFuel((BaseFuel) added);
			return AddCommand.create(domain, pricing.getFleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_BaseFuelPrices(), cost);
		}
		return null;
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (deleted instanceof BaseFuel) {
			final PricingModel pricing = rootObject.getSubModel(PricingModel.class);
			if (pricing == null)
				return null;
			for (final BaseFuelCost cost : pricing.getFleetCost().getBaseFuelPrices()) {
				if (cost.getFuel() == deleted) {
					return DeleteCommand.create(domain, cost);
				}
			}
		}
		return null;
	}
}
