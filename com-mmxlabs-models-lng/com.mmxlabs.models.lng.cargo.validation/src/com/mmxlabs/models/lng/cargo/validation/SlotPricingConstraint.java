/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SlotPricingConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Slot) {
			final Slot slot = (Slot) target;

			if (slot instanceof SpotSlot) {
				// Skip check!
			} else {

				if (!slot.isSetPriceExpression() && !slot.isSetContract()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot \"" + slot.getName() + "\" - A contract or price expression must be set"));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
					return dsd;
				}
			}
		}
		return ctx.createSuccessStatus();
	}
}
