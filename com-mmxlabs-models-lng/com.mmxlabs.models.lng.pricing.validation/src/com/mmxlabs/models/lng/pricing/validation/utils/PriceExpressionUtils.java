/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.GenericSeriesParsesException;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.UnknownSeriesException;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.parseutils.Nodes;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * 
 * @author Simon McGregor
 * 
 *         Utility class to provide methods for help when validating contract
 *         constraints.
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

	public static void validatePriceExpression(final @NonNull IValidationContext ctx, final @NonNull List<IStatus> failures, final @NonNull DetailConstraintStatusFactory factory,
			final @NonNull EObject target, final EAttribute feature, final boolean missingIsOk, final Pair<EObject, EStructuralFeature>... otherFeatures) {
		final PriceIndexType priceIndexType = getPriceIndexType(feature);
		validatePriceExpression(ctx, failures, factory, target, feature, priceIndexType, missingIsOk, otherFeatures);
	}

	public static @NonNull PriceIndexType getPriceIndexType(final EAttribute feature) {
		PriceIndexType priceIndexType = null;
		final EAnnotation eAnnotation = feature.getEAnnotation(ExpressionAnnotationConstants.ANNOTATION_NAME);
		if (eAnnotation != null) {
			final String value = eAnnotation.getDetails().get(ExpressionAnnotationConstants.ANNOTATION_KEY);
			if (ExpressionAnnotationConstants.TYPE_COMMODITY.equals(value)) {
				priceIndexType = PriceIndexType.COMMODITY;
			} else if (ExpressionAnnotationConstants.TYPE_CHARTER.equals(value)) {
				priceIndexType = PriceIndexType.CHARTER;
			} else if (ExpressionAnnotationConstants.TYPE_BASE_FUEL.equals(value)) {
				priceIndexType = PriceIndexType.BUNKERS;
			} else if (ExpressionAnnotationConstants.TYPE_CURRENCY.equals(value)) {
				priceIndexType = PriceIndexType.CURRENCY;
			}
		}
		if (priceIndexType == null) {
			throw new IllegalArgumentException();
		}
		return priceIndexType;
	}

	public static void validatePriceExpression(final @NonNull IValidationContext ctx, final @NonNull List<IStatus> failures, final @NonNull DetailConstraintStatusFactory factory,
			final @NonNull EObject target, final EAttribute feature, final @NonNull PriceIndexType priceIndexType, final boolean missingIsOk, final Pair<EObject, EStructuralFeature>... otherFeatures) {
		final String expression = (String) target.eGet(feature);
		if (expression == null || expression.isEmpty()) {
			if (!missingIsOk) {
				factory.copyName() //
						.withMessage("Missing price expression") //
						.withObjectAndFeature(target, feature) //
						.withObjectAndFeatures(otherFeatures) //
						.make(ctx, failures);
			}
		} else {
			final ValidationResult result = validatePriceExpression(ctx, target, feature, expression, priceIndexType);
			if (!result.isOk()) {
				factory.copyName() //
						.withMessage(result.getErrorDetails()) //
						.withObjectAndFeature(target, feature) //
						.withObjectAndFeatures(otherFeatures) //
						.make(ctx, failures);
			}
		}
	}

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
	 * @param ctx             A validation context.
	 * @param object          The EMF object to associate validation failures with.
	 * @param feature         The structural feature to attach validation failures
	 *                        to.
	 * @param priceExpression The price expression to validate.
	 * @param parser          A parser for price expressions.
	 * @param failures        The list of validation failures to append to.
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

			} catch (final UnknownSeriesException | GenericSeriesParsesException e) {
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
			// No date, but try to parse expression as a number.
			try {
				final double value = Double.parseDouble(priceExpression);
				checkValue(ctx, object, feature, minValue, maxValue, failures, value);
			} catch (final Exception e) {
				// Ignore errors.
			}

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

			checkValue(ctx, object, feature, minValue, maxValue, failures, value);

		} catch (final Exception e) {
			final String message = String.format("Price expression is not valid: %s", priceExpression);
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(object, feature);
			failures.add(dsd);
		}

	}

	private static void checkValue(final IValidationContext ctx, final EObject object, final EStructuralFeature feature, final Double minValue, final Double maxValue, final List<IStatus> failures,
			final double value) {
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
				final IScenarioDataProvider scenarioDataProvider = extraValidationContext.getScenarioDataProvider();
				if (scenarioDataProvider != null) {
					final ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
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

	public static @Nullable YearMonth getEarliestCurveDate(final @NonNull AbstractYearMonthCurve index) {
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

	public static Collection<@NonNull AbstractYearMonthCurve> getLinkedCurves(final String priceExpression) {
		final ModelMarketCurveProvider modelMarketCurveProvider = getMarketCurveProvider();
		return modelMarketCurveProvider.getLinkedCurves(priceExpression);

	}

	public static PriceIndexType getPriceIndexType(final AbstractYearMonthCurve namedIndexContainer) {
		if (namedIndexContainer instanceof CommodityCurve) {
			return PriceIndexType.COMMODITY;
		} else if (namedIndexContainer instanceof CurrencyCurve) {
			return PriceIndexType.CURRENCY;
		} else if (namedIndexContainer instanceof CharterCurve) {
			return PriceIndexType.CHARTER;
		} else if (namedIndexContainer instanceof BunkerFuelCurve) {
			return PriceIndexType.BUNKERS;
		}
		return null;

	}

	public static void checkExpressionAgainstPricingDate(final IValidationContext ctx, final String priceExpression, final Slot slot, final LocalDate pricingDate, final EStructuralFeature feature, final List<IStatus> failures) {
		final ModelMarketCurveProvider marketCurveProvider = PriceExpressionUtils.getMarketCurveProvider();
		final List<Pair<AbstractYearMonthCurve, LocalDate>> linkedCurvesAndDate = marketCurveProvider.getLinkedCurvesAndDate(priceExpression, pricingDate);

		final Map<AbstractYearMonthCurve, LocalDate> result1 = linkedCurvesAndDate.stream()
				.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (oldValue, newValue) -> (oldValue.isBefore(newValue) ? oldValue : newValue)));

		for (final Map.Entry<AbstractYearMonthCurve, LocalDate> e : result1.entrySet()) {
			final AbstractYearMonthCurve index = e.getKey();
			String type = "index";
			if (index instanceof CommodityCurve) {
				type = "commodity pricing";
			} else if (index instanceof CurrencyCurve) {
				type = "currency";
			}
			final @Nullable YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
			if (date == null) {

				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[Slot|%s] There is no %s data for curve %s", slot.getName(), type, index.getName())));
				dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
				failures.add(dcsd);

			} else if (date.isAfter(YearMonth.from(e.getValue()))) {
				final String monthDisplayname = date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
						.createFailureStatus(String.format("[Slot|%s] There is no %s data before %s %04d for curve %s", slot.getName(), type, monthDisplayname, date.getYear(), index.getName())));
				dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
				failures.add(dcsd);
			}
		}
	}

	public static Node convertCommodityToExpandedNodes(final String expression) {

		final ModelMarketCurveProvider marketCurveProvider = PriceExpressionUtils.getMarketCurveProvider();
		final PricingModel pricingModel = marketCurveProvider.getPricingModel();

		final LookupData lookupData = LookupData.createLookupData(pricingModel);
		final IExpression<Node> parse = new RawTreeParser().parse(expression);
		final Node p = parse.evaluate();
		final Node node = Nodes.expandNode(p, lookupData);

		return node;
	}
}
