/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
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
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class ChangeablePriceCalculator implements ISalesPriceCalculator, ILoadPriceCalculator, IPriceIntervalProvider {
	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	private ThreadLocal<Integer> price = new ThreadLocal<>();

	private final int addToPrice;

	public ChangeablePriceCalculator() {
		this.addToPrice = 0;
	}

	public ChangeablePriceCalculator(final int addToPrice) {
		this.addToPrice = addToPrice;
	}

	/**
	 */
	@Override
	public void prepareSalesForEvaluation(final @NonNull ISequences sequences) {
	}

	@Override
	public int estimateSalesUnitPrice(final IVessel vessel, final IDischargeOption dischargeOption, final IPortTimesRecord portTimesRecord) {
		return getPrice();
	}

	private Integer getPrice() {
		final Integer v = price.get();
		if (v == null) {
			return 0;
		}
		return v;
	}

	public void setPrice(final int newPrice) {
		this.price.set(newPrice + addToPrice);
	}

	@Override
	public int calculateSalesUnitPrice(final IVesselCharter vesselCharter, final IDischargeOption dischargeOption, final IAllocationAnnotation allocationAnnotation, final VoyagePlan voyagePlan,
			@Nullable final IDetailTree annotations) {
		return getPrice();
	}

	@Override
	public @Nullable PricingEventType getCalculatorPricingEventType(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
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
		intervals.add(new int[] { startOfRange, getPrice() });
		intervals.add(priceIntervalProviderHelper.getEndInterval(endOfRange));
		return intervals;
	}

	@Override
	public @Nullable List<@NonNull Integer> getPriceHourIntervals(final IPortSlot slot, final int start, final int end, final IPortTimeWindowsRecord portTimeWindowsRecord) {
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
	public int calculateFOBPricePerMMBTu(@NonNull final ILoadSlot loadSlot, @NonNull final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu,
			@NonNull final IAllocationAnnotation allocationAnnotation, @NonNull final IVesselCharter vesselCharter, @NonNull final VoyagePlan plan,
			@Nullable final ProfitAndLossSequences profitAndLossSequences, @Nullable final IDetailTree annotations) {
		return getPrice();
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(@NonNull final ILoadOption loadOption, @NonNull final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu,
			@NonNull final IAllocationAnnotation allocationAnnotation, @Nullable final ProfitAndLossSequences profitAndLossSequences, @Nullable final IDetailTree annotations) {
		return getPrice();
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(@NonNull final ILoadSlot loadSlot, @NonNull final IDischargeOption dischargeOption, final int dischargePricePerMMBTu,
			@NonNull final IAllocationAnnotation allocationAnnotation, @Nullable final ProfitAndLossSequences profitAndLossSequences, @Nullable final IDetailTree annotations) {
		return getPrice();
	}
}
