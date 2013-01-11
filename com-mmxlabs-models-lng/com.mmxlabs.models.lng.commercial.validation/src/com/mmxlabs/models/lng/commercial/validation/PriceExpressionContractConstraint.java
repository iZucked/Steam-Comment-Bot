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
import com.mmxlabs.models.lng.commercial.PriceExpressionContract;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.validation.util.ContractConstraints;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;

public class PriceExpressionContractConstraint extends
		AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof PriceExpressionContract) {
			final SeriesParser parser = ContractConstraints.getParser();
			final PriceExpressionContract contract = (PriceExpressionContract) target;

			ContractConstraints.validatePriceExpression(ctx, contract, CommercialPackage.eINSTANCE.getRedirectionPurchaseContract_BasePurchasePriceExpression(), contract.getPriceExpression(), parser,
					failures);

		}
		return Activator.PLUGIN_ID;
	}
}
