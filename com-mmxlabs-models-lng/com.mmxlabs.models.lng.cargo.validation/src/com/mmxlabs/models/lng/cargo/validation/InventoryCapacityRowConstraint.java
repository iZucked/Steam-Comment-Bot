/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class InventoryCapacityRowConstraint extends AbstractModelMultiConstraint {
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		
		if (object instanceof InventoryCapacityRow) {
			final InventoryCapacityRow icr = (InventoryCapacityRow) object;
			
			if (icr.eContainer() instanceof Inventory) {
				final Inventory inventory = (Inventory) icr.eContainer();
				if (icr.getDate() == null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) 
							ctx.createFailureStatus(String.format("Inventory model '%s': has an empty date in capacity row.", inventory.getName())));
					failure.addEObjectAndFeature(inventory, CargoPackage.Literals.INVENTORY_CAPACITY_ROW__DATE);
					failures.add(failure);
				}
				if (icr.getMaxVolume() < icr.getMinVolume()) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) 
							ctx.createFailureStatus(String.format("Inventory model '%s': min capacity is greater than the max capacity!", inventory.getName())));
					failure.addEObjectAndFeature(inventory, CargoPackage.Literals.INVENTORY_CAPACITY_ROW__MIN_VOLUME);
					failures.add(failure);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
