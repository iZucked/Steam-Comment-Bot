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
import com.mmxlabs.models.lng.commercial.RegasPricingParams;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class RegasPriceParametersConstraint extends AbstractPriceParametersConstraint<RegasPricingParams> {

	public RegasPriceParametersConstraint() {
		super(RegasPricingParams.class);
	}

	@Override
	protected void doParamsValidate(IValidationContext ctx, IExtraValidationContext extraContext, List<IStatus> failures, RegasPricingParams pricingParams, EObject eContainer,
			DetailConstraintStatusFactory factory) {

		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, pricingParams, CommercialPackage.Literals.REGAS_PRICING_PARAMS__PRICE_EXPRESSION,
				pricingParams.getPriceExpression(), PriceIndexType.COMMODITY);
		if (!result.isOk()) {
			factory.copyName() //
				.withMessage(result.getErrorDetails()) //
				.withObjectAndFeature(pricingParams, CommercialPackage.Literals.REGAS_PRICING_PARAMS__PRICE_EXPRESSION) //
				.make(ctx, failures);
		}
		if (pricingParams.getNumPricingDays() <= 0) {
			factory.copyName() //
				.withMessage("Number of pricing days must be greater than zero") //
				.withObjectAndFeature(pricingParams, CommercialPackage.Literals.REGAS_PRICING_PARAMS__NUM_PRICING_DAYS) //
				.make(ctx, failures);
		}
	}

}
