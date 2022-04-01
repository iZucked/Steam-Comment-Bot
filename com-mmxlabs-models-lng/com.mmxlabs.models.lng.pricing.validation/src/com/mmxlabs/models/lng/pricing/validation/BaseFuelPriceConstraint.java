/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseFuelPriceConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		// Disable for now in light of API change..
		if (target instanceof final BaseFuelCost baseFuelCost) {

			final BaseFuel fuel = baseFuelCost.getFuel();
			final String baseFuelName = fuel == null ? "<Unspecified fuel>" : fuel.getName();
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("Base Fuel", baseFuelName);

			final String expression = baseFuelCost.getExpression();

			if (expression == null || expression.isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(baseFuelCost, PricingPackage.Literals.BASE_FUEL_COST__EXPRESSION) //
						.withMessage("Price is missing") //
						.make(ctx, statuses);
			} else {
				try {
					if (Double.parseDouble(expression) == 0.0) {
						factory.copyName() //
								.withObjectAndFeature(baseFuelCost, PricingPackage.Literals.BASE_FUEL_COST__EXPRESSION) //
								.withMessage("Price is zero") //
								.make(ctx, statuses);
						return;
					}
				} catch (final NumberFormatException e) {
					// Ignore, as could be an arbitrary expression
				}

				PriceExpressionUtils.validatePriceExpression(ctx, statuses, factory, baseFuelCost, PricingPackage.Literals.BASE_FUEL_COST__EXPRESSION, true);
			}
		}
	}

}
