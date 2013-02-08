/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.RedirectionPriceParameters;
import com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.validation.util.ContractConstraints;
import com.mmxlabs.models.lng.types.ALNGPriceCalculatorParameters;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class RedirectionPurchaseContractConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof RedirectionPurchaseContract) {
			final SeriesParser parser = ContractConstraints.getParser();
			final RedirectionPurchaseContract contract = (RedirectionPurchaseContract) target;

			ContractConstraints.validatePriceExpression(ctx, contract, CommercialPackage.eINSTANCE.getRedirectionPurchaseContract_BasePurchasePriceExpression(), contract.getBasePurchasePriceExpression(), parser,
					failures);
			ContractConstraints.validatePriceExpression(ctx, contract, CommercialPackage.eINSTANCE.getRedirectionPurchaseContract_BaseSalesPriceExpression(), contract.getBaseSalesPriceExpression(), parser, failures);

			if (contract.getNotionalSpeed() < 10.0) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Notional speed should be a normal speed"));
				dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getRedirectionPurchaseContract_NotionalSpeed());
				failures.add(dsd);
			}
			// TODO: Check notional speed is within the vessel speed range
		}
		else if (target instanceof Contract) {
			ALNGPriceCalculatorParameters priceInfo = ((Contract) target).getPriceInfo();
			if (priceInfo instanceof RedirectionPriceParameters) {
				RedirectionPriceParameters info = (RedirectionPriceParameters) priceInfo;
				final SeriesParser parser = ContractConstraints.getParser();

				ContractConstraints.validatePriceExpression(ctx, info, CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__BASE_PURCHASE_PRICE_EXPRESSION, info.getBasePurchasePriceExpression(), parser,
						failures);
				ContractConstraints.validatePriceExpression(ctx, info, CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__BASE_SALES_PRICE_EXPRESSION, info.getBaseSalesPriceExpression(), parser, failures);

				if (info.getNotionalSpeed() < 10.0) {

					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Notional speed should be a normal speed"));
					dsd.addEObjectAndFeature(info, CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__NOTIONAL_SPEED);
					failures.add(dsd);
				}
			}
			
		}
		
		return Activator.PLUGIN_ID;
	}

	/*
	private void validatePriceExpression(final IValidationContext ctx, final RedirectionPurchaseContract contract, final EStructuralFeature feature, final String priceExpression,
			final SeriesParser parser, final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price Expression is missing."));
			dsd.addEObjectAndFeature(contract, feature);
			failures.add(dsd);
			return;
		}

		if (parser != null) {
			ISeries parsed = null;
			String hints = "";
			try {
				final IExpression<ISeries> expression = parser.parse(priceExpression);
				parsed = expression.evaluate();

			} catch (final Exception e) {
				hints = e.getMessage();
			}
			if (parsed == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Unable to parse price expression. " + hints));
				dsd.addEObjectAndFeature(contract, feature);
				failures.add(dsd);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private SeriesParser getParser() {
		final Activator activator = Activator.getDefault();
		if (activator == null) {
			return null;
		}
		final IExtraValidationContext extraValidationContext = activator.getExtraValidationContext();
		if (extraValidationContext != null) {
			final MMXRootObject rootObject = extraValidationContext.getRootObject();

			final SeriesParser indices = new SeriesParser();

			final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
			for (final Index<Double> index : pricingModel.getCommodityIndices()) {
				if (index instanceof DataIndex) {
					// For this validation, we do not need real times or values
					final int[] times = new int[1];
					final Number[] nums = new Number[1];
					indices.addSeriesData(index.getName(), times, nums);
				} else if (index instanceof DerivedIndex) {
					indices.addSeriesExpression(index.getName(), ((DerivedIndex) index).getExpression());
				}
			}
			return indices;
		}
		return null;
	}
	*/
}
