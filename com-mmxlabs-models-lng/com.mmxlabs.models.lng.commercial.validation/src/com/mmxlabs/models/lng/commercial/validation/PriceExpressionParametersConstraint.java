/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;

public class PriceExpressionParametersConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		EObject target = ctx.getTarget();

		if (target instanceof ExpressionPriceParameters) {
			final SeriesParser parser = PriceExpressionUtils.getParser(null);
			final ExpressionPriceParameters contract = (ExpressionPriceParameters) target;
			PriceExpressionUtils.validatePriceExpression(ctx, contract, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION, contract.getPriceExpression(), parser, failures);
		}

		return Activator.PLUGIN_ID;
	}
}
