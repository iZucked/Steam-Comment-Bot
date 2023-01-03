/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class BreakEvenSalesPriceCalculator implements ISalesPriceCalculator, IBreakEvenPriceCalculator, IPriceIntervalProvider {
	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	private final ThreadLocal<Integer> price = new ThreadLocal<>();

	@Override
	public int estimateSalesUnitPrice(final IVessel vessel, final IDischargeOption dischargeOptions, final IPortTimesRecord portTimesRecord) {
		return getPrice();
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price.set(newPrice);
	}

	@Override
	public int calculateSalesUnitPrice(final IVesselCharter vesselCharter, final IDischargeOption option, final IAllocationAnnotation allocationAnnotation, final VoyagePlan voyagePlan,
			@Nullable final IDetailTree annotations) {
		return getPrice();
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return dischargeOption.getPricingEvent();
	}

	@Override
	public int getEstimatedSalesPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		return getPrice();
	}

	@Override
	public int getCalculatorPricingDate(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return IPortSlot.NO_PRICING_DATE;
	}

	@Override
	public List<int @NonNull []> getPriceIntervals(final IPortSlot slot, final int startOfRange, final int endOfRange, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final List<int[]> intervals = new LinkedList<>();
		final Integer integer = price.get();
		if (integer == null) {
			intervals.add(new int[] { startOfRange, 0 });

		} else {
			intervals.add(new int[] { startOfRange, integer });
		}
		intervals.add(priceIntervalProviderHelper.getEndInterval(endOfRange));
		return intervals;
	}

	@Override
	public List<@NonNull Integer> getPriceHourIntervals(final IPortSlot slot, final int start, final int end, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		final int[] intervals = new int[] { start, end };
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else {
			return null;
		}
	}

	public int getPrice() {
		final Integer value = price.get();
		if (value == null) {
			return 0;
		}
		return value;
	}
}
