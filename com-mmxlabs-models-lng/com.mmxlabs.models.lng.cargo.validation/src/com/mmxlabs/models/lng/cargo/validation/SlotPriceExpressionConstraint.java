/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Date;
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
	private static double minExpressionValue = 0.0;
	private static double maxExpressionValue = 90.0;
	
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			//final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			if (slot.isSetPriceExpression()) {
				String priceExpression = slot.getPriceExpression();
				// Permit break even marker
				if (!"?".equals(priceExpression)) {
					PriceExpressionUtils.validatePriceExpression(ctx, slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION, priceExpression, failures);			
				}
				
				final Date start = slot.getWindowStartWithSlotOrPortTime();
				if (start != null) {
					PriceExpressionUtils.constrainPriceExpression(ctx, slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION, priceExpression, minExpressionValue, maxExpressionValue, start, failures);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
	
}
