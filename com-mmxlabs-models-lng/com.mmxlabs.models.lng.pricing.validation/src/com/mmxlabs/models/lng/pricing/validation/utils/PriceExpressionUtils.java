/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
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
public class PriceExpressionUtils {
	static Pattern pattern = Pattern.compile("([^0-9 a-zA-Z_+-/*()])");
	/**
	 * @author Simon McGregor
	 * 
	 * Validates a price expression for a given EMF object
	 * 
	 * @param ctx A validation context.
	 * @param object The EMF object to associate validation failures with.
	 * @param feature The structural feature to attach validation failures to.
	 * @param priceExpression The price expression to validate.
	 * @param parser A parser for price expressions.
	 * @param failures The list of validation failures to append to.
	 */
	public static void validatePriceExpression(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression,
			final SeriesParser parser, final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price Expression is missing."));
			dsd.addEObjectAndFeature(object, feature);
			failures.add(dsd);
			return;
		}

		Matcher matcher = pattern.matcher(priceExpression);
		
		if (matcher.find()) {
			String message = String.format("Price Expression contains unexpected character '%s'.", matcher.group(1));
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(object, feature);
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
				dsd.addEObjectAndFeature(object, feature);
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
