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
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class InventoryEventRowConstraint extends AbstractModelMultiConstraint {
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		
		if (object instanceof InventoryEventRow) {
			final InventoryEventRow ier = (InventoryEventRow) object;
			
			if (ier.eContainer() instanceof Inventory) {
				final Inventory inventory = (Inventory) ier.eContainer();
			
				if (ier.getStartDate() == null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) 
							ctx.createFailureStatus(String.format("Inventory model '%s':Inventory event row has an empty start date.", inventory.getName())));
					failure.addEObjectAndFeature(ier, CargoPackage.Literals.INVENTORY_EVENT_ROW__START_DATE);
					failures.add(failure);
				}
				if (ier.getEndDate() == null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) 
							ctx.createFailureStatus(String.format("Inventory model '%s':Inventory event row has an empty end date.", inventory.getName())));
					failure.addEObjectAndFeature(ier, CargoPackage.Literals.INVENTORY_EVENT_ROW__END_DATE);
					failures.add(failure);
				}
				if (ier.getStartDate() != null && ier.getEndDate() != null) {
					if (ier.getEndDate().isBefore(ier.getStartDate())) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) 
								ctx.createFailureStatus(String.format("Inventory model '%s':Inventory event row's start date is after its end date.", inventory.getName())));
						failure.addEObjectAndFeature(ier, CargoPackage.Literals.INVENTORY_EVENT_ROW__START_DATE);
						failures.add(failure);
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
