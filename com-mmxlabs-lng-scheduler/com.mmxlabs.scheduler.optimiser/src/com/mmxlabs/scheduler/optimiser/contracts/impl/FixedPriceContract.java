/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public class FixedPriceContract implements ILoadPriceCalculator, ISalesPriceCalculator, IPriceIntervalProvider {
	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	private final int pricePerMMBTU;

	public FixedPriceContract(final int pricePerMMBTU) {
		this.pricePerMMBTU = pricePerMMBTU;
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption option, final IPortTimesRecord voyageRecord, @Nullable final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateSalesUnitPrice(final IDischargeOption option, final IAllocationAnnotation allocationAnnotation, @Nullable final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences,
			final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable final VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable final VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int getEstimatedPurchasePrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		return pricePerMMBTU;
	}

	@Override
	public List<Integer> getPriceHourIntervals(final IPortSlot slot, final int start, final int end, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		final int[] intervals = new int[] { start, end };
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else {
			return null;
		}
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return dischargeOption.getPricingEvent();
	}

	@Override
	public int getEstimatedSalesPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		return pricePerMMBTU;
	}

	@Override
	public int getCalculatorPricingDate(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return IPortSlot.NO_PRICING_DATE;
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final ILoadOption loadOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return loadOption.getPricingEvent();
	}

	@Override
	public int getCalculatorPricingDate(final ILoadOption loadOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return pricePerMMBTU;
	}

	@Override
	public List<int[]> getPriceIntervals(final IPortSlot slot, final int startOfRange, final int endOfRange, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final List<int[]> intervals = new LinkedList<>();
		intervals.add(new int[] { startOfRange, pricePerMMBTU });
		intervals.add(priceIntervalProviderHelper.getEndInterval(endOfRange));
		return intervals;
	}

}
