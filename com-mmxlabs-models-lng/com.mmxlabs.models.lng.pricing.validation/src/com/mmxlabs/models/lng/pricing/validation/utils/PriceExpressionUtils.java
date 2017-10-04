/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation.utils;

import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.UnknownSeriesException;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * 
 * @author Simon McGregor
 * 
 *         Utility class to provide methods for help when validating contract constraints.
 * 
 */
public class PriceExpressionUtils {
	public static class ValidationResult {
		private final boolean ok;
		@NonNull
		private final String errorDetails;

		@NonNull
		private static final ValidationResult OK_RESULT = new ValidationResult(true, "");

		private ValidationResult(final boolean ok, @NonNull final String errorDetails) {
			this.ok = ok;
			this.errorDetails = errorDetails;
		}

		@NonNull
		public static ValidationResult createOKStatus() {
			return OK_RESULT;
		}

		@NonNull
		public static ValidationResult createErrorStatus(@NonNull final String errorDetails) {
			return new ValidationResult(false, errorDetails);
		}

		public boolean isOk() {
			return ok;
		}

		public String getErrorDetails() {
			return errorDetails;
		}

	}

	// Pattern to match invalid characters
	private final @NonNull static Pattern pattern = Pattern.compile("([^0-9 a-zA-Z_+-/*%()])");

	// Pattern to detect use of SHIFT function.
	private final @NonNull static Pattern shiftDetectPattern = Pattern.compile(".*SHIFT\\p{Space}*\\(.*", Pattern.CASE_INSENSITIVE);

	private final @NonNull static Pattern shiftUsePattern = Pattern.compile("SHIFT\\p{Space}*\\(\\p{Space}*[a-z][a-z0-9_]*\\p{Space}*,\\p{Space}*-?[0-9]+\\p{Space}*\\)", Pattern.CASE_INSENSITIVE);

	@NonNull
	public static ValidationResult validatePriceExpression(final @NonNull IValidationContext ctx, final @NonNull EObject object, final @NonNull EStructuralFeature feature,
			final @Nullable String priceExpression) {
		return validatePriceExpression(ctx, object, feature, priceExpression, PriceIndexType.COMMODITY);
	}

	@NonNull
	public static ValidationResult validatePriceExpression(final @NonNull IValidationContext ctx, final @NonNull EObject object, final @NonNull EStructuralFeature feature,
			final @Nullable String priceExpression, @NonNull final PriceIndexType priceIndexType) {
		return validatePriceExpression(ctx, object, feature, priceExpression, getIndexParser(priceIndexType));
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
	@NonNull
	public static ValidationResult validatePriceExpression(final @NonNull IValidationContext ctx, final @NonNull EObject object, final @NonNull EStructuralFeature feature,
			final @Nullable String priceExpression, final @NonNull SeriesParser parser) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return ValidationResult.createErrorStatus("Price Expression is missing.");
		}
		// Check for illegal characters
		{
			final Matcher matcher = pattern.matcher(priceExpression);
			if (matcher.find()) {
				final String message = String.format("[Price expression|'%s'] Contains unexpected character '%s'.", priceExpression, matcher.group(1));
				return ValidationResult.createErrorStatus(message);
			}
		}
		// Test SHIFT function use
		{
			final Matcher matcherA = shiftDetectPattern.matcher(priceExpression);
			if (matcherA.find()) {
				final Matcher matcherB = shiftUsePattern.matcher(priceExpression);
				if (!matcherB.find()) {
					final String message = String.format("[Price expression|'%s'] Unexpected use of SHIFT function. Expect SHIFT(<index name>, <number of months>.", priceExpression);
					return ValidationResult.createErrorStatus(message);
				}
			}
		}
		// TODO DATED AVG USE
		if (parser != null) {
			ISeries parsed = null;
			String hints = "";
			try {
				final IExpression<ISeries> expression = parser.parse(priceExpression);
				parsed = expression.evaluate();

			} catch (final UnknownSeriesException e) {
				hints = e.getMessage();
			} catch (final Exception e) {
				final String operatorPattern = "([-/*+][-/*+]+)";
				final Pattern p = Pattern.compile(operatorPattern);
				final Matcher m = p.matcher(priceExpression);
				if (m.find()) {
					hints = "Consecutive operators: " + m.group(0);
				} else {
					hints = "Unknown problem: " + e.getMessage();
				}
			}
			if (parsed == null) {
				return ValidationResult.createErrorStatus("Unable to parse: " + hints);
			}
		}
		return ValidationResult.createOKStatus();
	}

	/**
	 */
	public static void constrainPriceExpression(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression, final Double minValue,
			final Double maxValue, final YearMonth date, final List<IStatus> failures) {

		if (date == null) {
			return;
		}

		if (priceExpression == null || priceExpression.isEmpty()) {
			return;
		}

		// Break even symbol
		if (priceExpression.equals("?")) {
			return;
		}
		if (date == null) {
			return;
		}

		final SeriesParser parser = getIndexParser(PriceIndexType.COMMODITY);
		try {
			final IExpression<ISeries> expression = parser.parse(priceExpression);
			final ISeries parsed = expression.evaluate();

			final int pricingTime = Hours.between(PriceIndexUtils.dateZero, date);

			final double value = parsed.evaluate(pricingTime).doubleValue();

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

	public static ISeries getParsedSeries(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final String priceExpression, final YearMonth date,
			final List<IStatus> failures) {
		final SeriesParser parser = getIndexParser(PriceIndexType.COMMODITY);
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

	public static @NonNull ModelMarketCurveProvider getMarketCurveProvider() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {
			final IExtraValidationContext extraValidationContext = activator.getExtraValidationContext();
			if (extraValidationContext != null) {
				IScenarioDataProvider scenarioDataProvider = extraValidationContext.getScenarioDataProvider();
				if (scenarioDataProvider != null) {
					ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
					if (provider != null) {
						return provider;
					}
				}
			}
		}
		throw new IllegalStateException("Unable to get market curve provider");
	}

	public static SeriesParser getIndexParser(final @NonNull PriceIndexType priceIndexType) {
		final ModelMarketCurveProvider modelMarketCurveProvider = getMarketCurveProvider();
		return modelMarketCurveProvider.getSeriesParser(priceIndexType);
	}

	public static @Nullable YearMonth getEarliestCurveDate(final @NonNull NamedIndexContainer<?> index) {
		final ModelMarketCurveProvider modelMarketCurveProvider = getMarketCurveProvider();
		return modelMarketCurveProvider.getEarliestDate(index);
	}

	public static <T extends LNGPriceCalculatorParameters> void checkPriceExpressionInPricingParams(final IValidationContext ctx, final List<IStatus> failures, final T target,
			final EAttribute attribute, final String expression, @NonNull final PriceIndexType priceIndexType) {
		if (target instanceof LNGPriceCalculatorParameters) {
			final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, target, attribute, expression, priceIndexType);
			if (!result.isOk()) {
				final EObject eContainer = target.eContainer();
				final DetailConstraintStatusDecorator dsd;
				if (eContainer instanceof Contract) {
					final Contract contract = (Contract) eContainer;
					final String message = String.format("[Contract|'%s']%s", contract.getName(), result.getErrorDetails());
					dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(eContainer, attribute);
				} else {
					final String message = String.format("%s", result.getErrorDetails());
					dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				}
				dsd.addEObjectAndFeature(target, attribute);
				failures.add(dsd);
			}
		}
	}

	/**
	 * Used by unit tests
	 * 
	 * @param priceExpression
	 * @return
	 */
	public static boolean validateBasicSyntax(final String priceExpression) {
		{
			final Matcher matcher = pattern.matcher(priceExpression);
			if (matcher.find()) {
				return false;
			}
		}
		{
			final Matcher matcherA = shiftDetectPattern.matcher(priceExpression);
			if (matcherA.find()) {
				final Matcher matcherB = shiftUsePattern.matcher(priceExpression);
				if (!matcherB.find()) {
					return false;
				}
			}
		}
		return true;
	}

	public static Collection<@NonNull NamedIndexContainer<?>> getLinkedCurves(final String priceExpression) {
		final ModelMarketCurveProvider modelMarketCurveProvider = getMarketCurveProvider();
		return modelMarketCurveProvider.getLinkedCurves(priceExpression);

	}

	public static PriceIndexType getPriceIndexType(NamedIndexContainer<?> namedIndexContainer) {
		if (namedIndexContainer instanceof CommodityIndex) {
			return PriceIndexType.COMMODITY;
		} else if (namedIndexContainer instanceof CurrencyIndex) {
			return PriceIndexType.CURRENCY;
		} else if (namedIndexContainer instanceof CharterIndex) {
			return PriceIndexType.CHARTER;
		} else if (namedIndexContainer instanceof BaseFuelIndex) {
			return PriceIndexType.BUNKERS;
		}
		return null;

	}
}
