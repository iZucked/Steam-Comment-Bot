/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;

/**
 * @since 2.0
 */
public class SlotPriceExpressionConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			//final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			String priceExpression = slot.getPriceExpression();
			} else if (!slot.isSetContract()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A price expression or a contract must be set."));
				dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
				dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
				failures.add(dsd);

			if (slot.isSetPriceExpression()) {
				// Permit break even marker
				if (!"?".equals(priceExpression)) {
					PriceExpressionUtils.validatePriceExpression(ctx, slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION, priceExpression, failures);			
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
	
			if (pricingModel == null) {
				return null;
			}
}
