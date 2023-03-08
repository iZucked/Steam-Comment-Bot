/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.SplitMonthFunctionASTNode;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DateShiftExpressionPriceParametersConstraint extends AbstractPriceParametersConstraint<DateShiftExpressionPriceParameters> {

	public DateShiftExpressionPriceParametersConstraint() {
		super(DateShiftExpressionPriceParameters.class);
	}

	@Override
	protected void doParamsValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final DateShiftExpressionPriceParameters pricingParams,
			final EObject eContainer, DetailConstraintStatusFactory factory) {

		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION,
				pricingParams.getPriceExpression(), PriceIndexType.COMMODITY);

		if (!result.isOk()) {
			factory.copyName()//
					.withMessage(result.getErrorDetails()) //
					.withObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION) //
					.make(ctx, failures);

		}
		if (pricingParams.getValue() != 0) {
			if (checkNodeForSplitMonth(PriceExpressionUtils.getMarketCurveProvider().getPricingDataCache().getASTNodeFor(pricingParams.getPriceExpression(), PriceIndexType.COMMODITY))) {
				factory.copyName()//
						.withMessage("Cannot use SPLITMONTH with a date shift") //
						.withObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION) //
						.withObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE) //
						.make(ctx, failures);
			}
		}
		if (pricingParams.isSpecificDay()) {
			if (pricingParams.getValue() == 0 || pricingParams.getValue() > 28) {
				factory.copyName()//
						.withMessage("Day should be between 1 and 28") //
						.withObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE) //
						.make(ctx, failures);
			}
		} else {
			if (pricingParams.getValue() < -28 || pricingParams.getValue() > 28) {
				factory.copyName()//
						.withMessage("Offset range should be between -28 and 28") //
						.withObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE) //
						.make(ctx, failures);

			}
		}
	}

	private static boolean checkNodeForSplitMonth(final ASTNode markedUpNode) {
		if (markedUpNode instanceof SplitMonthFunctionASTNode) {
			return true;
		}
		for (ASTNode childMarkedUpNode : markedUpNode.getChildren()) {
			if (checkNodeForSplitMonth(childMarkedUpNode)) {
				return true;
			}
		}
		return false;
		// if (node.token.toLowerCase().equals("splitmonth")) {
		// return true;
		// }
		// if (node.children != null) {
		// for (final Node c : node.children) {
		// if (checkNodeForSplitMonth(c)) {
		// return true;
		// }
		// }
		// }
		// return false;

	}
}
