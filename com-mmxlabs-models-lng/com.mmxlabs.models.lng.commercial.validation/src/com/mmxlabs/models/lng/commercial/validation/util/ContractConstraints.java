/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation.util;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon McGregor
 * 
 * Utility class to provide methods for help when validating contract constraints. 
 * @since 2.0
 *
 */
public class ContractConstraints {
	/**
	 * @author Simon McGregor
	 * 
	 * Validates a price expression for a given contract
	 * 
	 * @param ctx A validation context.
	 * @param target The EMF object to associate validation failures with.
	 * @param feature The structural feature to attach validation failures to.
	 * @param priceExpression The price expression to validate.
	 * @param parser A parser for price expressions.
	 * @param failures The list of validation failures to append to.
	 * @since 3.0
	 */
	public static void validatePriceExpression(final IValidationContext ctx, final EObject target, final EStructuralFeature feature, final String priceExpression,
			final SeriesParser parser, final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price Expression is missing."));
			dsd.addEObjectAndFeature(target, feature);
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
				dsd.addEObjectAndFeature(target, feature);
				failures.add(dsd);
			}
		}
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */
	@SuppressWarnings("rawtypes")
	public static SeriesParser getParser() {
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
}
