/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DateShiftExpressionPriceParametersConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		EObject target = ctx.getTarget();

		if (target instanceof DateShiftExpressionPriceParameters) {
			final DateShiftExpressionPriceParameters pricingParams = (DateShiftExpressionPriceParameters) target;

			String prefix;
			EObject eContainer = pricingParams.eContainer();
			if (eContainer instanceof Contract) {
				Contract contract = (Contract) eContainer;
				prefix = String.format("[Contract|'%s']", contract.getName());
			} else if (eContainer instanceof SpotMarket) {
				SpotMarket spotMarket = (SpotMarket) eContainer;
				prefix = String.format("[Market|'%s']", spotMarket.getName());
			} else if (eContainer instanceof NamedObject) {
				NamedObject namedObject = (NamedObject) eContainer;
				prefix = String.format("['%s']", namedObject.getName());
			} else {
				prefix = "";
			}

			ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION,
					pricingParams.getPriceExpression(), PriceIndexType.COMMODITY);
			if (!result.isOk()) {
				final String message = String.format("%s%s", prefix, result.getErrorDetails());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				if (eContainer instanceof Contract) {
					dsd.addEObjectAndFeature(eContainer, CommercialPackage.Literals.CONTRACT__PRICE_INFO);
				}

				dsd.addEObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);
				failures.add(dsd);
			}

			if (pricingParams.isSpecificDay()) {
				if (pricingParams.getValue() == 0 || pricingParams.getValue() > 28) {
					final String message = String.format("%sDay should be between 1 and 28", prefix);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					if (eContainer instanceof Contract) {
						dsd.addEObjectAndFeature(eContainer, CommercialPackage.Literals.CONTRACT__PRICE_INFO);
					}

					dsd.addEObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE);
					failures.add(dsd);
				}
			} else {
				if (pricingParams.getValue() < -28 || pricingParams.getValue() > 28) {
					final String message = String.format("%sOffset range should be between -28 and 28", prefix);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					if (eContainer instanceof Contract) {
						dsd.addEObjectAndFeature(eContainer, CommercialPackage.Literals.CONTRACT__PRICE_INFO);
					}

					dsd.addEObjectAndFeature(pricingParams, CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE);
//					failures.add(dsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
