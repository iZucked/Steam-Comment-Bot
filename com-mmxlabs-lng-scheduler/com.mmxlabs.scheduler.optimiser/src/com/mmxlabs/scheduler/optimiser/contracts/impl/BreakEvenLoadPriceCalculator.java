/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class BreakEvenLoadPriceCalculator implements ILoadPriceCalculator, IBreakEvenPriceCalculator, IPriceIntervalProvider {
	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	private ThreadLocal<Integer> price = new ThreadLocal<>();

	@Override
	public void preparePurchaseForEvaluation(final @NonNull ISequences sequences) {
		// price.set(0);
	}

	/**
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerM3, final IAllocationAnnotation allocationAnnotation,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences,
			final IDetailTree annotations) {
		return getPrice();
	}

	/**
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable final VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {
		return getPrice();
	}

	/**
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final @Nullable VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {
		return getPrice();
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price.set(newPrice);
	}

	@Override
	public int getEstimatedPurchasePrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		throw new IllegalStateException("BreakEvenLoadPriceCalculator should not use getEstimatedPurchasePrice()");
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final ILoadOption loadOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return loadOption.getPricingEvent();
	}

	@Override
	public int getCalculatorPricingDate(final ILoadOption loadOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return IPortSlot.NO_PRICING_DATE;
	}

	@Override
	public List<int @NonNull []> getPriceIntervals(final IPortSlot slot, final int startOfRange, final int endOfRange, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final List<int @NonNull []> intervals = new LinkedList<>();
		Integer priceObj = price.get();
		if (priceObj == null) {
			intervals.add(new int[] { startOfRange, 0 });
		} else {

			intervals.add(new int[] { startOfRange, priceObj });
		}
		intervals.add(priceIntervalProviderHelper.getEndInterval(endOfRange));
		return intervals;
	}

	@Override
	public List<@NonNull Integer> getPriceHourIntervals(final IPortSlot slot, final int start, final int end, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		final int @NonNull [] intervals = new int[] { start, end };
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else {
			return null;
		}
	}

	public int getPrice() {
		Integer p = price.get();
		if (p == null) {
			return 0;
		}
		return p;
	}
}
