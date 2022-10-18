/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.regas;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.curves.LazyStepwiseIntegerCurve;
import com.mmxlabs.common.curves.PreGeneratedIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.RegasPricingParams;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.LazyIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerEditor;

public class RegasContractTransformer extends SimpleContractTransformer {

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getRegasPricingParams());

	@Inject
	private Injector injector;

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	@Named(LNGTransformerModule.MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve monthIntervalsInHoursCurve;

	@Inject
	private ILazyExpressionManagerEditor lazyExpressionMangerEditor;

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable final SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		if (priceParameters instanceof @NonNull final RegasPricingParams regasPricingParams) {
			return createContract(salesContract, regasPricingParams);
		}
		return null;
	}

	private ISalesPriceCalculator createContract(@Nullable final SalesContract salesContract, @NonNull final RegasPricingParams pricingParams) {
		if (salesContract == null) {
			throw new IllegalArgumentException("Contract cannot be null for Regas type contract");
		}
		final String priceExpression = pricingParams.getPriceExpression();
		if (priceExpression == null || priceExpression.isEmpty()) {
			throw new IllegalStateException("No price expression provided");
		}
		final int numPricingDays = pricingParams.getNumPricingDays();
		if (numPricingDays <= 0) {
			throw new IllegalStateException("Number of pricing days must be greater than zero");
		}
		@NonNull
		final Pair<@NonNull ICurve, @NonNull IIntegerIntervalCurve> curveAndIntervals = createCurveAndIntervals(priceExpression, numPricingDays);
		final PriceExpressionContract contract = new PriceExpressionContract(curveAndIntervals.getFirst(), curveAndIntervals.getSecond());
		injector.injectMembers(contract);
		return contract;
	}

	private @NonNull Pair<@NonNull ICurve, @NonNull IIntegerIntervalCurve> createCurveAndIntervals(@NonNull final String priceExpression, final int numPricingDays) {
		final Function<@NonNull ISeries, @NonNull ICurve> curveFactory = makeCurveCreator(numPricingDays);
		final Function<@NonNull ISeries, @NonNull IIntegerIntervalCurve> priceIntervalFactory = makePriceIntervalCreator(numPricingDays);

		@NonNull
		final ICurve curve;
		@NonNull
		final IIntegerIntervalCurve priceIntervals;

		final IExpression<ISeries> expression = commodityIndices.parse(priceExpression);
		if (expression.canEvaluate()) {
			final ISeries parsed = expression.evaluate();
			curve = curveFactory.apply(parsed);
			priceIntervals = priceIntervalFactory.apply(parsed);
		} else {
			final LazyIntegerIntervalCurve lazyIntervalCurve = new LazyIntegerIntervalCurve(monthIntervalsInHoursCurve, priceIntervalFactory);
			final ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, curveFactory, lazyIntervalCurve::initialise);
			lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
			lazyExpressionMangerEditor.addLazyIntervalCurve(lazyIntervalCurve);

			curve = lazyCurve;
			priceIntervals = lazyIntervalCurve;
		}
		return Pair.of(curve, priceIntervals);
	}

	private Function<@NonNull ISeries, @NonNull ICurve> makeCurveCreator(final int numPricingDays) {
		return parsed -> {
			final PreGeneratedIntegerCurve curve = new PreGeneratedIntegerCurve();
			if (parsed.getChangePoints().length == 0) {
				curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
			} else {
				curve.setDefaultValue(0);
				regasPriceCurveLoopingCalculator(numPricingDays, parsed, curve::setValueAfter);
			}
			return curve;
		};
	}

	private Function<@NonNull ISeries, @NonNull IIntegerIntervalCurve> makePriceIntervalCreator(final int numPricingDays) {
		return parsed -> {
			if (parsed.getChangePoints().length == 0) {
				return monthIntervalsInHoursCurve;
			} else {
				final List<@NonNull Integer> changePointsList = new LinkedList<>();
				regasPriceCurveLoopingCalculator(numPricingDays, parsed, (hour, internalPrice) -> changePointsList.add(hour));
				final int[] changePoints = new int[changePointsList.size()];
				int i = 0;
				for (final int changePoint : changePointsList) {
					changePoints[i] = changePoint;
					++i;
				}
				return DateAndCurveHelper.getSplitMonthDatesForChangePoint(changePoints);
			}
		};
	}

	private void regasPriceCurveLoopingCalculator(final int numPricingDays, @NonNull final ISeries parsed, @NonNull final BiConsumer<@NonNull Integer, @NonNull Integer> consumer) {
		final int maxChangePointHour = parsed.getChangePoints()[parsed.getChangePoints().length - 1];
		final int minChangePointHour = parsed.getChangePoints()[0];
		int previousInternalPrice = 0;
		final int numHoursInRange = numPricingDays * 24;
		// Before this hour average equals default value
		final int minStartHour = minChangePointHour - numHoursInRange + 1;
		// After maxChangePointHour, average equal same value. But we still have to calculate at least one (could add after)
		// TODO: make loop work in steps of 24 hours
		for (int currentHour = minStartHour; currentHour <= maxChangePointHour; ++currentHour) {
			final double nextPrice = calculateAveragePrice(currentHour, numPricingDays, parsed);
			final int nextInternalPrice = OptimiserUnitConvertor.convertToInternalPrice(nextPrice);
			if (previousInternalPrice != nextInternalPrice) {
				consumer.accept(currentHour, nextInternalPrice);
				previousInternalPrice = nextInternalPrice;
			}
		}
	}

	private double calculateAveragePrice(final int startingHour, final int numDays, final ISeries parsed) {
		final int lastHour = startingHour + numDays * 24;
		double sum = 0.0;
		for (int currentHour = startingHour; currentHour < lastHour; currentHour = currentHour + 24) {
			sum += parsed.evaluate(currentHour).doubleValue();
		}
		return sum / numDays;
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		return this.handledClasses;
	}
}
