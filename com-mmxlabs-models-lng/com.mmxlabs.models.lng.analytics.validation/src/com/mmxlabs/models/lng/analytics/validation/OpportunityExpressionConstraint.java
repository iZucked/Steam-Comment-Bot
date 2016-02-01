/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class OpportunityExpressionConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof BuyOpportunity) {
			final SeriesParser parser = getParser(extraContext);
			final BuyOpportunity slot = (BuyOpportunity) target;
			validatePriceExpression(ctx, slot, AnalyticsPackage.eINSTANCE.getBuyOpportunity_PriceExpression(), slot.getPriceExpression(), parser, failures);
		}
		if (target instanceof SellOpportunity) {
			final SeriesParser parser = getParser(extraContext);
			final SellOpportunity slot = (SellOpportunity) target;
			validatePriceExpression(ctx, slot, AnalyticsPackage.eINSTANCE.getSellOpportunity_PriceExpression(), slot.getPriceExpression(), parser, failures);
		}
		return Activator.PLUGIN_ID;
	}

	private void validatePriceExpression(final IValidationContext ctx, final EObject slot, final EStructuralFeature feature, final String priceExpression, final SeriesParser parser,
			final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price Expression is missing."));
			dsd.addEObjectAndFeature(slot, feature);
			failures.add(dsd);
			return;
		}

		// Permit break even marker
		if ("?".equals(priceExpression)) {
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
				dsd.addEObjectAndFeature(slot, feature);
				failures.add(dsd);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private SeriesParser getParser(final IExtraValidationContext extraContext) {
		final MMXRootObject rootObject = extraContext.getRootObject();

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final SeriesParser indices = new SeriesParser();

			final PricingModel pricingModel = lngScenarioModel.getReferenceModel().getPricingModel();
			for (final CommodityIndex commodityIndex : pricingModel.getCommodityIndices()) {
				final Index<Double> index = commodityIndex.getData();
				if (index instanceof DataIndex) {
					// For this validation, we do not need real times or values
					final int[] times = new int[1];
					final Number[] nums = new Number[1];
					indices.addSeriesData(commodityIndex.getName(), times, nums);
				} else if (index instanceof DerivedIndex) {
					indices.addSeriesExpression(commodityIndex.getName(), ((DerivedIndex) index).getExpression());
				}
			}
			return indices;

		}
		return null;
	}
}
