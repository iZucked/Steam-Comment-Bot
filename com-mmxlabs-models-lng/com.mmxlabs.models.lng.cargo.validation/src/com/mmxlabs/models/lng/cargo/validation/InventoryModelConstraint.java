/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class InventoryModelConstraint extends AbstractModelMultiConstraint {
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		
		if (object instanceof Inventory) {
			final Inventory inventory = (Inventory) object;
			if (inventory.getName() != null) {
				if (inventory.getPort() == null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) 
							ctx.createFailureStatus(String.format("Inventory model '%s': requires a port.", inventory.getName())));
					failure.addEObjectAndFeature(inventory, CargoPackage.Literals.INVENTORY__PORT);
					failures.add(failure);
				}
			}
		}

		

		return Activator.PLUGIN_ID;
	}
}
