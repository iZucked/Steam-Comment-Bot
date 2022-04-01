/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ExpressionPriceParametersConstraint extends AbstractPriceParametersConstraint<ExpressionPriceParameters> {

	public ExpressionPriceParametersConstraint() {
		super(ExpressionPriceParameters.class);
	}

	@Override
	protected void doParamsValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final ExpressionPriceParameters pricingParams,
			final EObject eContainer, final DetailConstraintStatusFactory factory) {

		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, pricingParams, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION,
				pricingParams.getPriceExpression(), PriceIndexType.COMMODITY);

		if (!result.isOk()) {
			factory.copyName()//
					.withMessage(result.getErrorDetails()) //
					.withObjectAndFeature(pricingParams, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION) //
					.make(ctx, failures);
		}
	}
}
