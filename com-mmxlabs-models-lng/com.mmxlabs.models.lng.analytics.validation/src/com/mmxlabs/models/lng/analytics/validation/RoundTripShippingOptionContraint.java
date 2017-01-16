/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class RoundTripShippingOptionContraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof RoundTripShippingOption) {
			final RoundTripShippingOption option = (RoundTripShippingOption) target;
			ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, option, AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__HIRE_COST, option.getHireCost(),
					PriceIndexType.CHARTER);
			if (!result.isOk()) {
				final String message = String.format("%s", result.getErrorDetails());
				DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__HIRE_COST);
				failures.add(dsd);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
