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
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;

public class SensibleIndexDateConstraint extends AbstractModelConstraint {
	private final YearMonth earliestDate = YearMonth.of(2000, 1);

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof NamedIndexContainer<?>) {
			final NamedIndexContainer<?> index = (NamedIndexContainer<?>) target;
			final Index<?> data = index.getData();
			if (data instanceof DataIndex) {
				@Nullable
				final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
				if (date != null) {
					if (date.isBefore(earliestDate)) {
						return (IConstraintStatus) ctx.createFailureStatus(index.getName(), earliestDate);
					}
				}
			}
		}

		return ctx.createSuccessStatus();
	}

}
