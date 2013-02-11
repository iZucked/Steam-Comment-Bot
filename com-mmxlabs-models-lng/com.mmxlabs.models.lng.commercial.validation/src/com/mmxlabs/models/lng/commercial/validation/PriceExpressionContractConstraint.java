/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.validation.util.ContractConstraints;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;

public class PriceExpressionContractConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		EObject target = ctx.getTarget();

		ExpressionPriceParameters parameters = null;
		if (target instanceof Contract) {
			target = ((Contract) target).getPriceInfo();
		}
		if (target instanceof ExpressionPriceParameters) {
			parameters = (ExpressionPriceParameters) target;
		}
		if (parameters != null) {
			final SeriesParser parser = ContractConstraints.getParser();

			ContractConstraints.validatePriceExpression(ctx, parameters, CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION, parameters.getPriceExpression(), parser, failures);

		}

		return Activator.PLUGIN_ID;
	}
}
