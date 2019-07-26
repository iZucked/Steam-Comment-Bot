/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.IntegerIntervalCurveHelper;
import com.mmxlabs.models.lng.transformer.util.LNGScenarioUtils;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getExpressionPriceParameters(), CommercialPackage.eINSTANCE.getDateShiftExpressionPriceParameters(),
			CommercialPackage.eINSTANCE.getSalesContract(), CommercialPackage.eINSTANCE.getPurchaseContract());

	@Inject
	private Injector injector;

	@Inject
	@Named(LNGTransformerModule.Parser_Commodity)
	private SeriesParser indices;

	@Inject
	private IntegerIntervalCurveHelper integerIntervalCurveHelper;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	private LNGScenarioModel lngScenarioModel;

	@Inject
	@Named(LNGTransformerModule.MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve monthIntervalsInHoursCurve;

	@Inject
	private ModelEntityMap modelEntityMap;

	/**
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
	}

	@Override
	public void finishTransforming() {
	}

	private SimpleContract instantiate(final LNGPriceCalculatorParameters priceInfo) {
		if (priceInfo instanceof ExpressionPriceParameters) {
			final ExpressionPriceParameters expressionPriceInfo = (ExpressionPriceParameters) priceInfo;
			return createPriceExpressionContract(expressionPriceInfo.getPriceExpression());
		} else if (priceInfo instanceof DateShiftExpressionPriceParameters) {
			final DateShiftExpressionPriceParameters expressionPriceInfo = (DateShiftExpressionPriceParameters) priceInfo;
			return createDateShiftExpressionContract(expressionPriceInfo.getPriceExpression(), expressionPriceInfo.isSpecificDay(), expressionPriceInfo.getValue());
		}

		return null;
	}

	/**
	 */
	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable final SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	/**
	 */
	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable final PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {

	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}

	/**
	 * Create an internal representation of a PriceExpressionContract from the price expression string.
	 * 
	 * @param priceExpression
	 *            A string containing a valid price expression interpretable by a {@link SeriesParser}
	 * @return An internal representation of a price expression contract for use by the optimiser
	 */
	private PriceExpressionContract createPriceExpressionContract(final String priceExpression) {

		final String splitMonthToken = "splitmonth(";
		boolean isSplitMonth = priceExpression.toLowerCase().contains(splitMonthToken.toLowerCase());

		IIntegerIntervalCurve priceIntervals = monthIntervalsInHoursCurve;
		ICurve curve = null;

		final IExpression<ISeries> expression = indices.parse(priceExpression);

		if (isSplitMonth) {
			final ISeries parsed = expression.evaluate(dateHelper.getEarliestAndLatestTimes());
			curve = generateExpressionCurve(priceExpression, parsed);
			priceIntervals = integerIntervalCurveHelper.getSplitMonthDatesForChangePoint(parsed.getChangePoints());
		} else {
			final ISeries parsed = expression.evaluate();
			curve = generateExpressionCurve(priceExpression, parsed);
		}

		final PriceExpressionContract contract = new PriceExpressionContract(curve, priceIntervals);
		injector.injectMembers(contract);
		return contract;
	}

	/**
	 * Create an internal representation of a PriceExpressionContract from the price expression string.
	 * 
	 * @param priceExpression
	 *            A string containing a valid price expression interpretable by a {@link SeriesParser}
	 * @return An internal representation of a price expression contract for use by the optimiser
	 */
	private PriceExpressionContract createDateShiftExpressionContract(final String priceExpression, final boolean specificDate, final int shift) {
		final IIntegerIntervalCurve priceIntervals;
		final ICurve shiftedCurve;
		if (specificDate) {
			priceIntervals = integerIntervalCurveHelper.getSplitMonthAlignedWithOffsetDatesForRange(integerIntervalCurveHelper.getPreviousMonth(dateHelper.convertTime(dateHelper.getEarliestTime())),
					integerIntervalCurveHelper.getNextMonth(dateHelper.convertTime(dateHelper.getLatestTime())), 0, shift);

			{
				final IExpression<ISeries> expression = indices.parse(priceExpression);
				final ISeries parsed = expression.evaluate(dateHelper.getEarliestAndLatestTimes());

				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				if (parsed.getChangePoints().length == 0) {
					curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
				} else {
					curve.setDefaultValue(0);
					for (final int i : parsed.getChangePoints()) {
						ZonedDateTime date = modelEntityMap.getDateFromHours(i, "UTC");
						date = date.minusMonths(1).withDayOfMonth(shift).withHour(0);
						final int time = dateHelper.convertTime(date);
						curve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				shiftedCurve = curve;
			}
		} else {
			priceIntervals = integerIntervalCurveHelper.getMonthAlignedIntegerIntervalCurve(integerIntervalCurveHelper.getPreviousMonth(dateHelper.convertTime(dateHelper.getEarliestTime())),
					integerIntervalCurveHelper.getNextMonth(dateHelper.convertTime(dateHelper.getLatestTime())), shift * 24 * -1);
			{
				final IExpression<ISeries> expression = indices.parse(priceExpression);
				final ISeries parsed = expression.evaluate(dateHelper.getEarliestAndLatestTimes());

				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				if (parsed.getChangePoints().length == 0) {
					curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
				} else {
					curve.setDefaultValue(0);
					for (final int i : parsed.getChangePoints()) {
						ZonedDateTime date = modelEntityMap.getDateFromHours(i, "UTC");
						date = date.minusDays(shift);
						final int time = dateHelper.convertTime(date);
						curve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				shiftedCurve = curve;
			}
		}

		final PriceExpressionContract contract = new PriceExpressionContract(shiftedCurve, priceIntervals);
		injector.injectMembers(contract);
		return contract;
	}

	private ICurve generateExpressionCurve(final String priceExpression, ISeries parsed) {

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
		} else {
			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}
}
