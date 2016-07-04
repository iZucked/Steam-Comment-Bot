/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;

public class SensibleIndexDateConstraint extends AbstractModelConstraint {
	private final YearMonth earliestDate = YearMonth.of(2000, 1);

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof CommodityIndex) {
			final CommodityIndex index = (CommodityIndex) target;
			final Index<Double> data = index.getData();
			if (data instanceof DataIndex) {
				for (final YearMonth date : data.getDates()) {
					if (date.isBefore(earliestDate)) {
						return (IConstraintStatus) ctx.createFailureStatus(index.getName(), earliestDate);
					}
				}

			}
		}

		return ctx.createSuccessStatus();
	}

}
