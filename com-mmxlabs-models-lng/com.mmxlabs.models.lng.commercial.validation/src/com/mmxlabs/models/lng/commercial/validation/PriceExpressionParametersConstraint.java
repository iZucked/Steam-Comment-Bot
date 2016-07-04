/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PriceExpressionParametersConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		EObject target = ctx.getTarget();

		if (target instanceof ExpressionPriceParameters) {
			final ExpressionPriceParameters pricingParams = (ExpressionPriceParameters) target;
			ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, pricingParams, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION,
					pricingParams.getPriceExpression(), PriceIndexType.COMMODITY);
			if (!result.isOk()) {
				EObject eContainer = pricingParams.eContainer();
				final DetailConstraintStatusDecorator dsd;
				if (eContainer instanceof Contract) {
					Contract contract = (Contract) eContainer;
					final String message = String.format("[Contract|'%s']%s", contract.getName(), result.getErrorDetails());
					dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(eContainer, CommercialPackage.Literals.CONTRACT__PRICE_INFO);
				} else {
					final String message = String.format("%s", result.getErrorDetails());
					dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				}
				dsd.addEObjectAndFeature(pricingParams, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);
				failures.add(dsd);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
