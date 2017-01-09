/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.time.YearMonth;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class LNGPriceConstraint  extends AbstractModelConstraint {
	final double min = 0;
	final double max = 90;

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof CommodityIndex) {
			final CommodityIndex index = (CommodityIndex) target;
			final Index<Double> data = index.getData();
			for (YearMonth date: data.getDates()) {
				final Double price = data.getValueForMonth(date);
				if (price < min || price > max) {
					//String message = String.format("Index '%s' has price %f for date '%s' (should be between %.2f and %.2f)", index.getName(), price, date.toString(), min, max);
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(index.getName(), price, date.toString(), min, max));
					return dcsd;
				}
			}
		}
		
		return ctx.createSuccessStatus();
	}

}
