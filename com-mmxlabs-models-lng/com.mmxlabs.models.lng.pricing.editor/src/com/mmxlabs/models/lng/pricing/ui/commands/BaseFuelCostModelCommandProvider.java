/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider;

/**
 * @author hinton
 *
 */
public class BaseFuelCostModelCommandProvider extends BaseModelCommandProvider {
	@Override
	protected boolean shouldHandleAddition(Object addedObject) {
		return addedObject instanceof BaseFuel;
	}

	@Override
	protected boolean shouldHandleDeletion(Object deletedObject) {
		return shouldHandleAddition(deletedObject);
	}

	@Override
	protected Command objectAdded(EditingDomain domain, MMXRootObject rootObject, Object added) {
		if (added instanceof BaseFuel) {
			final PricingModel pricing = rootObject.getSubModel(PricingModel.class);
			if (pricing == null) return null;
			for (final BaseFuelCost existing : pricing.getFleetCost().getBaseFuelPrices()) {
				if (existing.getFuel() == added) return null;
			}
			final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
			cost.setFuel((BaseFuel) added);
			return AddCommand.create(domain, pricing.getFleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_BaseFuelPrices(), cost);
		}
		return null;
	}

	@Override
	protected Command objectDeleted(EditingDomain domain, MMXRootObject rootObject, Object deleted) {
		if (deleted instanceof BaseFuel) {
			final PricingModel pricing = rootObject.getSubModel(PricingModel.class);
			if (pricing == null) return null;
			for (final BaseFuelCost cost : pricing.getFleetCost().getBaseFuelPrices()) {
				if (cost.getFuel() == deleted) {
					return DeleteCommand.create(domain, cost);
				}
			}
		}
		return null;
	}
}
