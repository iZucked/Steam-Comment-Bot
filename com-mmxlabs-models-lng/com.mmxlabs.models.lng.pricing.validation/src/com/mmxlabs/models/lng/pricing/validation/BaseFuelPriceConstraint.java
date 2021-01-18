/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseFuelPriceConstraint extends AbstractModelMultiConstraint {
	final double min = 0;
	final double max = 5000;

	@Override
	protected String validate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		// Disable for now in light of API change..
		if (target instanceof BaseFuelCost) {
			final BaseFuelCost baseFuelCost = (BaseFuelCost) target;

			BaseFuel fuel = baseFuelCost.getFuel();
			String baseFuelName = fuel == null ? "<Unspecified fuel>" : fuel.getName();
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("Base Fuel", baseFuelName);

			final String expression = baseFuelCost.getExpression();

			if (expression == null || expression.isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(baseFuelCost, PricingPackage.Literals.BASE_FUEL_COST__EXPRESSION) //
						.withMessage("Price is missing") //
						.make(ctx, statuses);
				return Activator.PLUGIN_ID;
			} else {
				try {
					if (Double.parseDouble(expression) == 0.0) {
						factory.copyName() //
								.withObjectAndFeature(baseFuelCost, PricingPackage.Literals.BASE_FUEL_COST__EXPRESSION) //
								.withMessage("Price is zero") //
								.make(ctx, statuses);
						return Activator.PLUGIN_ID;
					}
				} catch (NumberFormatException e) {
					// Ignore, as could be an arbitrary expression
				}

				PriceExpressionUtils.validatePriceExpression(ctx, statuses, factory, baseFuelCost, PricingPackage.Literals.BASE_FUEL_COST__EXPRESSION, true);
			}
		}

		return Activator.PLUGIN_ID;
	}

}
