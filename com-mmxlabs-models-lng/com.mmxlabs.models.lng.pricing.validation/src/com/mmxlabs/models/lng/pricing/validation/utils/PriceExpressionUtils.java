/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation.utils;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon McGregor
 * 
 *         Utility class to provide methods for help when validating contract constraints.
 * 
 */
public class PriceExpressionUtils {
	static Pattern pattern = Pattern.compile("([^0-9 a-zA-Z_+-/*%()])");

	public static void validatePriceExpression(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression, final List<IStatus> failures) {
		validatePriceExpression(ctx, object, feature, priceExpression, getCommodityParser(null), failures);
	}

	/**
	 * @author Simon McGregor
	 * 
	 *         Validates a price expression for a given EMF object
	 * 
	 * @param ctx
	 *            A validation context.
	 * @param object
	 *            The EMF object to associate validation failures with.
	 * @param feature
	 *            The structural feature to attach validation failures to.
	 * @param priceExpression
	 *            The price expression to validate.
	 * @param parser
	 *            A parser for price expressions.
	 * @param failures
	 *            The list of validation failures to append to.
	 */
	public static void validatePriceExpression(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression, final SeriesParser parser,
			final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price Expression is missing."));
			dsd.addEObjectAndFeature(object, feature);
			failures.add(dsd);
			return;
		}

		final Matcher matcher = pattern.matcher(priceExpression);

		if (matcher.find()) {
			final String message = String.format("[Price expression|'%s'] Contains unexpected character '%s'.", priceExpression, matcher.group(1));
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

			} catch (final EmptyStackException e) {
				final String operatorPattern = "([+-/*%][+-/*%]+)";
				final Pattern p = Pattern.compile(operatorPattern);
				final Matcher m = p.matcher(priceExpression);
				if (m.find()) {
					hints = "Consecutive operators: " + m.group(0);
				} else {
					hints = "Unknown problem";
				}
			} catch (final Exception e) {
				hints = e.getMessage();
			}
			if (parsed == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Price expression|" + priceExpression
						+ "] Unable to parse: " + hints));
				dsd.addEObjectAndFeature(object, feature);
				failures.add(dsd);
			}
		}

	}

	/**
	 */
	public static void constrainPriceExpression(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression, final Double minValue,
			final Double maxValue, final Date date, final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return;
		}

		// Break even symbol
		if (priceExpression.equals("?")) {
			return;
		}

		final SeriesParser parser = getCommodityParser(date);
		try {
			final IExpression<ISeries> expression = parser.parse(priceExpression);
			final ISeries parsed = expression.evaluate();
			final double value = parsed.evaluate(0).doubleValue();

			final boolean lessThanMin = minValue != null && value < minValue;
			final boolean moreThanMax = minValue != null && value > maxValue;
			if (lessThanMin || moreThanMax) {
				final String boundLabel = lessThanMin ? "minimum" : "maximum";
				final double boundValue = lessThanMin ? minValue : maxValue;
				final String comparisonLabel = lessThanMin ? "less" : "more";

				final String message = String.format("Price expression has value %.2f which is %s than %s value %.2f", value, comparisonLabel, boundLabel, boundValue);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(object, feature);
				failures.add(dsd);

			}

		} catch (final Exception e) {
			final String message = String.format("Price expression is not valid: %s", priceExpression);
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(object, feature);
			failures.add(dsd);
		}

	}

	public static ISeries getParsedSeries(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression, final Date date,
			final List<IStatus> failures) {
		final SeriesParser parser = getCommodityParser(date);
		try {
			final IExpression<ISeries> expression = parser.parse(priceExpression);
			final ISeries parsed = expression.evaluate();
			return parsed;
		} catch (final Exception e) {
			final String message = String.format("Price expression is not valid: %s", priceExpression);
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(object, feature);
			failures.add(dsd);
			return null;
		}
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */
	@SuppressWarnings("rawtypes")
	public static SeriesParser getCommodityParser(final Date dateZero) {
		return getIndexParser(dateZero, PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES);

//		final Activator activator = Activator.getDefault();
//		if (activator == null) {
//			return null;
//		}
//		final IExtraValidationContext extraValidationContext = activator.getExtraValidationContext();
//		if (extraValidationContext != null) {
//			final MMXRootObject rootObject = extraValidationContext.getRootObject();
//
//			if (rootObject instanceof LNGScenarioModel) {
//				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
//				final SeriesParser indices = new SeriesParser();
//
//				final PricingModel pricingModel = lngScenarioModel.getPricingModel();
//				for (final CommodityIndex commodityIndex : pricingModel.getCommodityIndices()) {
//					final Index<Double> index = commodityIndex.getData();
//					if (index instanceof DataIndex) {
//						PriceIndexUtils.addSeriesDataFromDataIndex(indices, commodityIndex.getName(), dateZero, (DataIndex<? extends Number>) index);
//					} else if (index instanceof DerivedIndex) {
//						indices.addSeriesExpression(commodityIndex.getName(), ((DerivedIndex) index).getExpression());
//					}
//				}
//				return indices;
//			}
//		}
//		return null;
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */
	@SuppressWarnings("rawtypes")
	public static SeriesParser getCharterParser(final Date dateZero) {
		return getIndexParser(dateZero, PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES);

//		final Activator activator = Activator.getDefault();
//		if (activator == null) {
//			return null;
//		}
//		final IExtraValidationContext extraValidationContext = activator.getExtraValidationContext();
//		if (extraValidationContext != null) {
//			final MMXRootObject rootObject = extraValidationContext.getRootObject();
//
//			if (rootObject instanceof LNGScenarioModel) {
//				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
//				final SeriesParser indices = new SeriesParser();
//
//				final PricingModel pricingModel = lngScenarioModel.getPricingModel();
//				for (final CharterIndex charterIndex : pricingModel.getCharterIndices()) {
//					final Index<Integer> index = charterIndex.getData();
//					if (index instanceof DataIndex) {
//						PriceIndexUtils.addSeriesDataFromDataIndex(indices, charterIndex.getName(), dateZero, (DataIndex<? extends Number>) index);
//					} else if (index instanceof DerivedIndex) {
//						indices.addSeriesExpression(charterIndex.getName(), ((DerivedIndex) index).getExpression());
//					}
//				}
//				return indices;
//			}
//		}
//		return null;
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */
	@SuppressWarnings("rawtypes")
	public static SeriesParser getBaseFuelIndexParser(final Date dateZero) {
		return getIndexParser(dateZero, PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES);
	}

	public static SeriesParser getIndexParser(final Date dateZero, final EReference reference) {
		final Activator activator = Activator.getDefault();
		if (activator == null) {
			return null;
		}
		final IExtraValidationContext extraValidationContext = activator.getExtraValidationContext();
		if (extraValidationContext != null) {
			final MMXRootObject rootObject = extraValidationContext.getRootObject();

			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final SeriesParser indices = new SeriesParser();

				final PricingModel pricingModel = lngScenarioModel.getPricingModel();
				final List<NamedIndexContainer<? extends Number>> namedIndexContainerList = (List<NamedIndexContainer<? extends Number>>) pricingModel.eGet(reference);
				for (final NamedIndexContainer<? extends Number> namedIndexContainer : namedIndexContainerList) {
					final Index<? extends Number> index = namedIndexContainer.getData();
					if (index instanceof DataIndex) {
						PriceIndexUtils.addSeriesDataFromDataIndex(indices, namedIndexContainer.getName(), dateZero, (DataIndex<? extends Number>) index);
					} else if (index instanceof DerivedIndex) {
						indices.addSeriesExpression(namedIndexContainer.getName(), ((DerivedIndex) index).getExpression());
					}
				}
				return indices;
			}
		}
		return null;
	}
}
