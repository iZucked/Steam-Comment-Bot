/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class BaseFuelPriceConstraint  extends AbstractModelConstraint {
	final double min = 0;
	final double max = 5000;

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		// Disable for now in light of API change..
//		if (target instanceof BaseFuelCost) {
//			final BaseFuelCost cost = (BaseFuelCost) target;
//			final double price = cost.getIndex().getPrice();
//			
//			if (price < min || price > max) {
//				String message = String.format("'%s' has price %.2f (should be between %f and %f)", cost.getFuel().getName(), price, min, max);
//				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
//				return dcsd;
//			}
//
//		}

		return ctx.createSuccessStatus();
	}

}
