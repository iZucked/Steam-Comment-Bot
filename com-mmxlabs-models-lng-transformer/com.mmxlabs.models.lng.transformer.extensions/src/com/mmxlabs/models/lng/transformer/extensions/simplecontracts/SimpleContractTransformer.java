/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.curves.LazyStepwiseIntegerCurve;
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
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.IntegerIntervalCurveHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.LazyIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerEditor;

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
	@Named(SchedulerConstants.Parser_Commodity)
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

	@Inject 
	private ILazyExpressionManagerEditor lazyExpressionMangerEditor;

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
			if (expressionPriceInfo.getValue() == 0) {
				// No shift set, return simple variant.
				return createPriceExpressionContract(expressionPriceInfo.getPriceExpression());
			} else {
				return createDateShiftExpressionContract(expressionPriceInfo.getPriceExpression(), expressionPriceInfo.isSpecificDay(), expressionPriceInfo.getValue());
			}
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
	protected PriceExpressionContract createPriceExpressionContract(final String priceExpression) {

		final String splitMonthToken = "splitmonth(";
		boolean isSplitMonth = priceExpression.toLowerCase().contains(splitMonthToken.toLowerCase());

		IIntegerIntervalCurve priceIntervals = monthIntervalsInHoursCurve;
		ICurve curve = null;

		final IExpression<ISeries> expression = indices.parse(priceExpression);

		if (isSplitMonth) {
			if (expression.canEvaluate()) {
				final ISeries parsed = expression.evaluate();
				curve = generateExpressionCurve(priceExpression, parsed);
				priceIntervals = integerIntervalCurveHelper.getSplitMonthDatesForChangePoint(parsed.getChangePoints());
			} else {
				final LazyIntegerIntervalCurve lazyIntervalCurve = new LazyIntegerIntervalCurve(priceIntervals, parsed -> integerIntervalCurveHelper.getSplitMonthDatesForChangePoint(parsed.getChangePoints()));
				priceIntervals = lazyIntervalCurve;
				final ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, this::generateExpressionCurve, lazyIntervalCurve::initialise);
				curve = lazyCurve;
				lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
				lazyExpressionMangerEditor.addLazyIntervalCurve(lazyIntervalCurve);
			}
		} else {
			if (expression.canEvaluate()) {
				final ISeries parsed = expression.evaluate();
				curve = generateExpressionCurve(priceExpression, parsed);
			} else {
				ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, this::generateExpressionCurve, null);
				curve = lazyCurve;
				lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
			}
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
				if (expression.canEvaluate()) {
					shiftedCurve = generateShiftedCurve(expression, date -> date.minusMonths(1L).withDayOfMonth(shift).withHour(0));
				} else {
					ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, expr -> generateShiftedCurve(expr, date -> date.minusMonths(1L).withDayOfMonth(shift).withHour(0)), null);
					shiftedCurve = lazyCurve;
					lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
				}
			}
		} else {
			priceIntervals = integerIntervalCurveHelper.getMonthAlignedIntegerIntervalCurve(integerIntervalCurveHelper.getPreviousMonth(dateHelper.convertTime(dateHelper.getEarliestTime())),
					integerIntervalCurveHelper.getNextMonth(dateHelper.convertTime(dateHelper.getLatestTime())), shift * 24 * -1);
			{
				final IExpression<ISeries> expression = indices.parse(priceExpression);
				if (expression.canEvaluate()) {
					shiftedCurve = generateShiftedCurve(expression, date -> date.minusDays(shift));
				} else {
					ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, expr -> generateShiftedCurve(expr, date -> date.minusDays(shift)), null);
					shiftedCurve = lazyCurve;
					lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
				}
			}
		}

		final PriceExpressionContract contract = new PriceExpressionContract(shiftedCurve, priceIntervals);
		injector.injectMembers(contract);
		return contract;
	}

	protected ICurve generateShiftedCurve(final ISeries series, UnaryOperator<ZonedDateTime> dateShifter) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (series.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(0).doubleValue()));
		} else {
			curve.setDefaultValue(0);
			for (final int i : series.getChangePoints()) {
				ZonedDateTime date = modelEntityMap.getDateFromHours(i, "UTC");
				date = dateShifter.apply(date);
				final int time = dateHelper.convertTime(date);
				curve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}

	protected ICurve generateShiftedCurve(final IExpression<ISeries> expression, UnaryOperator<ZonedDateTime> dateShifter) {
		final ISeries parsed = expression.evaluate();
		return generateShiftedCurve(parsed, dateShifter);
	}

	protected ICurve generateExpressionCurve(final IExpression<ISeries> expression) {
		final ISeries parsed = expression.evaluate();
		return generateExpressionCurve(parsed);
	}

	protected ICurve generateExpressionCurve(final String priceExpression, ISeries parsed) {
		return generateExpressionCurve(parsed);
	}

	protected ICurve generateExpressionCurve(ISeries parsed) {

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
