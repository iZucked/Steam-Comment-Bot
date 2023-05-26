/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
	protected void doParamsValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,//
			final ExpressionPriceParameters pricingParams,//
			final EObject eContainer, final DetailConstraintStatusFactory factory) {
		
		if (pricingParams.eIsSet(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION)) {
			validatePrice(ctx, failures, pricingParams, pricingParams.getPriceExpression(), factory, PriceIndexType.COMMODITY, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);
		}
		if (pricingParams.getPreferredFormulae() != null && !pricingParams.getPreferredFormulae().isEmpty()) {
			pricingParams.getPreferredFormulae().forEach( w -> {
				validatePrice(ctx, failures, w, w.getName(), factory, PriceIndexType.COMMODITY, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PREFERRED_FORMULAE);
			});
		}
	}
	
	private void validatePrice(final IValidationContext ctx, final List<IStatus> failures, final EObject target,//
			final String pricingExpression, //
			final DetailConstraintStatusFactory factory, final PriceIndexType type, final EStructuralFeature feature) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, target, feature, pricingExpression, type);
		if (!result.isOk()) {
			factory.copyName()//
					.withMessage(result.getErrorDetails()) //
					.withObjectAndFeature(target, feature) //
					.make(ctx, failures);
		}
	}
}
