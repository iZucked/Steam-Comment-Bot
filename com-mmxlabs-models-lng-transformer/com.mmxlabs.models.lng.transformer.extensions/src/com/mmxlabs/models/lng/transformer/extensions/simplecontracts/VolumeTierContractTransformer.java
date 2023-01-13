/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VolumeTierContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VolumeTierContract.VolumeType;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;

/**
 * @author Simon Goodall
 * 
 */
public class VolumeTierContractTransformer implements IContractTransformer, ISlotTransformer {

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getVolumeTierPriceParameters());

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	private Injector injector;

	private VolumeTierContract instantiate(final LNGPriceCalculatorParameters priceInfo) {
		if (priceInfo instanceof final VolumeTierPriceParameters volTierPriceInfo) {

			Pair<IParameterisedCurve, IIntegerIntervalCurve> lowExpressionCurve = dateHelper.createCurveAndIntervals(commodityIndices, volTierPriceInfo.getLowExpression());
			if (lowExpressionCurve == null) {
				lowExpressionCurve = dateHelper.createConstantParameterisedCurveAndIntervals(0);
			}
			Pair<IParameterisedCurve, IIntegerIntervalCurve> highExpressionCurve = dateHelper.createCurveAndIntervals(commodityIndices, volTierPriceInfo.getHighExpression());
			if (highExpressionCurve == null) {
				highExpressionCurve = dateHelper.createConstantParameterisedCurveAndIntervals(0);
			}

			final long threshold = OptimiserUnitConvertor.convertToInternalVolume(volTierPriceInfo.getThreshold());
			final VolumeTierContract.VolumeType type = switch (volTierPriceInfo.getVolumeLimitsUnit()) {
			case M3 -> VolumeType.M3;
			case MMBTU -> VolumeType.MMBTU;
			default -> throw new IllegalArgumentException();
			};

			final TreeSet<Integer> mergedIntervals = new TreeSet<>();

			mergedIntervals.addAll(((IntegerIntervalCurve) lowExpressionCurve.getSecond()).getIntervals());
			mergedIntervals.addAll(((IntegerIntervalCurve) highExpressionCurve.getSecond()).getIntervals());

			final IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
			intervals.addAll(mergedIntervals);

			final VolumeTierContract.PricePair lowPair = new VolumeTierContract.PricePair(lowExpressionCurve.getFirst(), lowExpressionCurve.getSecond());
			final VolumeTierContract.PricePair highPair = new VolumeTierContract.PricePair(highExpressionCurve.getFirst(), highExpressionCurve.getSecond());
			final VolumeTierContract.Parameters defaultParameters = new VolumeTierContract.Parameters(lowPair, highPair, threshold, type, intervals);

			final var c = new VolumeTierContract(defaultParameters);
			injector.injectMembers(c);
			return c;
		}

		return null;
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable final SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable final PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}

	@Override
	public void slotTransformed(@NonNull final Slot<?> modelSlot, @NonNull final IPortSlot optimiserSlot) {

		VolumeTierContract oContract = null;
		if (optimiserSlot instanceof final ILoadOption option && option.getLoadPriceCalculator() instanceof final VolumeTierContract c) {
			oContract = c;
		} else if (optimiserSlot instanceof final IDischargeOption option && option.getDischargePriceCalculator() instanceof final VolumeTierContract c) {
			oContract = c;
		}
		if (oContract != null) {
			final VolumeTierSlotParams params = SlotContractParamsHelper.findSlotContractParams(modelSlot, VolumeTierSlotParams.class, VolumeTierPriceParameters.class);
			if (params != null && params.isOverrideContract()) {
				Pair<IParameterisedCurve, IIntegerIntervalCurve> lowExpressionCurve = dateHelper.createCurveAndIntervals(commodityIndices, params.getLowExpression());
				if (lowExpressionCurve == null) {
					lowExpressionCurve = dateHelper.createConstantParameterisedCurveAndIntervals(0);
				}
				Pair<IParameterisedCurve, IIntegerIntervalCurve> highExpressionCurve = dateHelper.createCurveAndIntervals(commodityIndices, params.getHighExpression());
				if (highExpressionCurve == null) {
					highExpressionCurve = dateHelper.createConstantParameterisedCurveAndIntervals(0);
				}

				final long threshold = OptimiserUnitConvertor.convertToInternalVolume(params.getThreshold());
				final VolumeTierContract.VolumeType type = switch (params.getVolumeLimitsUnit()) {
				case M3 -> VolumeType.M3;
				case MMBTU -> VolumeType.MMBTU;
				default -> throw new IllegalArgumentException();
				};
				
				final TreeSet<Integer> mergedIntervals = new TreeSet<>();

				mergedIntervals.addAll(((IntegerIntervalCurve) lowExpressionCurve.getSecond()).getIntervals());
				mergedIntervals.addAll(((IntegerIntervalCurve) highExpressionCurve.getSecond()).getIntervals());

				final IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
				intervals.addAll(mergedIntervals);

				final VolumeTierContract.PricePair lowPair = new VolumeTierContract.PricePair(lowExpressionCurve.getFirst(), lowExpressionCurve.getSecond());
				final VolumeTierContract.PricePair highPair = new VolumeTierContract.PricePair(highExpressionCurve.getFirst(), highExpressionCurve.getSecond());
				final VolumeTierContract.Parameters slotParameters = new VolumeTierContract.Parameters(lowPair, highPair, threshold, type, intervals);

				oContract.setPricingParams(optimiserSlot, slotParameters);
			}
		}
	}
}
