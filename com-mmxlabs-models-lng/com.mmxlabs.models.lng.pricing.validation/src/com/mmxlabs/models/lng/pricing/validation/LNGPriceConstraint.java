/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class LNGPriceConstraint extends AbstractModelConstraint {
	private final double min = 0.0;
	private final double max = 90.0;

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof CommodityCurve) {
			final CommodityCurve curve = (CommodityCurve) target;
			for (YearMonthPoint pt : curve.getPoints()) {
				final double price = pt.getValue();
				if (price < min || price > max) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(curve.getName(), price, pt.getDate().toString(), min, max));
					return dcsd;
				}
			}
		}

		return ctx.createSuccessStatus();
	}

}
