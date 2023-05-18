/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotPricingConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof final Slot<?> slot) {
			final Contract contract = slot.getContract();
			if (contract != null) {
				if (slot instanceof LoadSlot && !(contract instanceof PurchaseContract)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Slot \"" + slot.getName() + "\" - A load slot can only use a purchase contract"));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
					statuses.add(dsd);

				} else if (slot instanceof DischargeSlot && !(contract instanceof SalesContract)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Slot \"" + slot.getName() + "\" - A discharge slot can only use a sales contract"));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
					statuses.add(dsd);
				}
			}
			if (slot instanceof SpotSlot) {
				// Skip check!
			} else {

				if (!slot.isSetPriceExpression() && !slot.isSetPricingBasis() && !slot.isSetContract()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Slot \"" + slot.getName() + "\" - A contract or price expression must be set"));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
					statuses.add(dsd);
				}
			}
		}
	}
}
