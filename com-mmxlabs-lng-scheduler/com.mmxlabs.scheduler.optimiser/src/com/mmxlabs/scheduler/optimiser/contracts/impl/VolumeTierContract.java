/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class VolumeTierContract implements ISalesPriceCalculator, ILoadPriceCalculator, IPriceIntervalProvider {

	public static final String ANNOTATION_KEY = "VolumeTierContract.annotation";

	public enum VolumeType {
		M3, MMBTU
	}

	public record Parameters(PricePair lowExpressionCurve, //
			PricePair highExpressionCurve, //
			long threshold, //
			VolumeType type, //
			IIntegerIntervalCurve combinedIntervals) {
	}

	public record PricePair(ICurve curve, IIntegerIntervalCurve intervals) {
	}

	private final Map<IPortSlot, Parameters> perSlotParameters = new HashMap<>();

	private final Parameters defaultParameters;

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderUtil;

	@Inject(optional = true)
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private PricingEventHelper pricingEventHelper;

	public VolumeTierContract(final Parameters defaultParameters) {
		this.defaultParameters = defaultParameters;
	}

	@Override
	public List<int @NonNull []> getPriceIntervals(final IPortSlot slot, final int startOfRange, final int endOfRange, final IPortTimeWindowsRecord portTimeWindowRecord) {
		if (slot instanceof final ILoadOption option) {
			return priceIntervalProviderUtil.getPriceIntervalsList(option, getParametersForSlot(option).combinedIntervals(), startOfRange, endOfRange, portTimeWindowRecord);
		} else if (slot instanceof final IDischargeOption option) {
			return priceIntervalProviderUtil.getPriceIntervalsList(option, getParametersForSlot(option).combinedIntervals(), startOfRange, endOfRange, portTimeWindowRecord);
		} else {
			throw new IllegalStateException("getPriceIntervals() requires either a IDischargeOption");
		}
	}

	@Override
	public @Nullable List<@NonNull Integer> getPriceHourIntervals(final IPortSlot slot, final int start, final int end, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (slot instanceof final IDischargeOption option) {
			final int[] intervals = getParametersForSlot(option).combinedIntervals().getIntervalsAs1dArray(start, end);
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else {
			return null;
		}
	}

	@Override
	public int estimateSalesUnitPrice(@NonNull IVessel vessel, @NonNull IDischargeOption dischargeOption, @NonNull IPortTimesRecord portTimesRecord) {
		if (actualsDataProvider != null && actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(dischargeOption);
		}

		final int pricingDate = pricingEventHelper.getDischargePricingDate(dischargeOption, portTimesRecord);
		return getPriceForSlot(dischargeOption, getAllocationAnnotation(portTimesRecord), pricingDate, null);
	}

	@Override
	public int getEstimatedSalesPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		if (actualsDataProvider != null && actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(dischargeOption);
		}

		return getPriceForSlot(dischargeOption, getAllocationAnnotation(null), timeInHours, null);

	}

	@Override
	public int calculateSalesUnitPrice(@NonNull IVesselCharter vesselCharter, @NonNull IDischargeOption dischargeOption, @NonNull IAllocationAnnotation allocationAnnotation,
			@NonNull VoyagePlan voyagePlan, @Nullable IDetailTree annotations) {
		if (actualsDataProvider != null && actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(dischargeOption);
		}

		return getPriceForSlot(dischargeOption, getAllocationAnnotation(allocationAnnotation), pricingEventHelper.getDischargePricingDate(dischargeOption, allocationAnnotation), annotations);
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (actualsDataProvider.hasActuals(dischargeOption)) {
			return PricingEventType.DATE_SPECIFIED;
		}
		// FOB Sales are priced at start of load, DES Purchase at start of discharge
		if (dischargeOption instanceof IDischargeSlot) {
			return PricingEventType.START_OF_DISCHARGE;
		} else {
			return PricingEventType.START_OF_LOAD;
		}
	}

	@Override
	public int getCalculatorPricingDate(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getArrivalTime(dischargeOption);
		} else {
			return IPortSlot.NO_PRICING_DATE;
		}

	}

	@Override
	public int calculateFOBPricePerMMBTu(@NonNull final ILoadSlot loadSlot, @NonNull final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu,
			@NonNull final IAllocationAnnotation allocationAnnotation, @NonNull final IVesselCharter vesselCharter, @NonNull final VoyagePlan plan,
			@Nullable final ProfitAndLossSequences volumeAllocatedSequences, @Nullable final IDetailTree annotations) {

		final int timeInHours = pricingEventHelper.getLoadPricingDate(loadSlot, dischargeSlot, allocationAnnotation);
		return getPriceForSlot(loadSlot, getAllocationAnnotation(allocationAnnotation), timeInHours, annotations);
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(@NonNull final ILoadOption loadOption, @NonNull final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu,
			@NonNull final IAllocationAnnotation allocationAnnotation, @Nullable final ProfitAndLossSequences volumeAllocatedSequences, @Nullable final IDetailTree annotations) {

		final int timeInHours = pricingEventHelper.getLoadPricingDate(loadOption, dischargeSlot, allocationAnnotation);

		return getPriceForSlot(loadOption, getAllocationAnnotation(allocationAnnotation), timeInHours, annotations);
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(@NonNull final ILoadSlot loadSlot, @NonNull final IDischargeOption dischargeOption, final int dischargePricePerMMBTu,
			@NonNull final IAllocationAnnotation allocationAnnotation, @Nullable final ProfitAndLossSequences volumeAllocatedSequences, @Nullable final IDetailTree annotations) {

		final int timeInHours = pricingEventHelper.getLoadPricingDate(loadSlot, dischargeOption, allocationAnnotation);
		return getPriceForSlot(loadSlot, getAllocationAnnotation(allocationAnnotation), timeInHours, annotations);
	}

	public void setPricingParams(final IPortSlot slot, final Parameters parameters) {
		this.perSlotParameters.put(slot, parameters);
	}

	private @NonNull Parameters getParametersForSlot(final IPortSlot slot) {
		Parameters parameters = defaultParameters;

		if (perSlotParameters.containsKey(slot)) {
			parameters = perSlotParameters.get(slot);
		}
		return parameters;

	}

	private int getPriceForSlot(final IPortSlot slot, @Nullable final IAllocationAnnotation allocationAnnotation, final int pricingTime, @Nullable final IDetailTree annotations) {
		Parameters parameters = defaultParameters;

		if (perSlotParameters.containsKey(slot)) {
			parameters = perSlotParameters.get(slot);
		}

		long volume = Long.MAX_VALUE;
		if (allocationAnnotation != null) {
			if (parameters.type() == VolumeType.M3) {
				volume = allocationAnnotation.getCommercialSlotVolumeInM3(slot);
			} else {
				assert parameters.type() == VolumeType.MMBTU;
				volume = allocationAnnotation.getCommercialSlotVolumeInMMBTu(slot);
			}
		}
		if (volume <= parameters.threshold()) {

			VolumeTierAnnotation annotation = new VolumeTierAnnotation();
			annotation.lowPrice = parameters.lowExpressionCurve.curve().getValueAtPoint(pricingTime);
			annotation.lowVolume = volume;

			if (annotations != null) {
				annotations.addChild(ANNOTATION_KEY, annotation);
			}

			return annotation.lowPrice;
		} else {
			// Blend the
			assert allocationAnnotation != null;

			VolumeTierAnnotation annotation = new VolumeTierAnnotation();
			annotation.lowPrice = parameters.lowExpressionCurve.curve().getValueAtPoint(pricingTime);
			annotation.highPrice = parameters.highExpressionCurve.curve().getValueAtPoint(pricingTime);

			annotation.lowVolume = parameters.threshold();
			annotation.highVolume = volume - parameters.threshold();

			final long lowValue = Calculator.costFromVolume(annotation.lowVolume, annotation.lowPrice);
			final long highValue = Calculator.costFromVolume(annotation.highVolume, annotation.highPrice);

			if (annotations != null) {
				annotations.addChild(ANNOTATION_KEY, annotation);
			}

			// Volume mode doesn't matter as units match
			return Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(lowValue + highValue, volume);
		}
	}

	private @Nullable IAllocationAnnotation getAllocationAnnotation(final IPortTimesRecord ptr) {
		if (ptr instanceof final IAllocationAnnotation aa) {
			return aa;
		}
		return null;
	}
}
