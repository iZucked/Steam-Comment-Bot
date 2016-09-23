/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Concrete implementation of an {@link IPriceIntervalProducer}
 * 
 * @author achurchill
 *
 */
public class PriceIntervalProducer implements IPriceIntervalProducer {

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderUtil;

	@Override
	public List<int @NonNull []> getLoadIntervalsIndependentOfDischarge(final ILoadOption portSlot, final IPortTimeWindowsRecord portTimeWindowRecord) {
		assert portSlot.getLoadPriceCalculator() instanceof IPriceIntervalProvider;
		final int start = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(start, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());
		return ((IPriceIntervalProvider) portSlot.getLoadPriceCalculator()).getPriceIntervals(portSlot, start, end, portTimeWindowRecord);
	}

	@Override
	public List<int @NonNull []> getLoadIntervalsBasedOnDischarge(@NonNull final ILoadOption portSlot, @NonNull final IPortTimeWindowsRecord portTimeWindowRecord) {
		final IDischargeOption discharge = priceIntervalProviderUtil.getFirstDischargeOption(portTimeWindowRecord.getSlots());
		assert discharge != null;
		assert discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider;
		final int start = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(start, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());
		return ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getPriceIntervals(discharge, start, end, portTimeWindowRecord);
	}

	@Override
	public List<int @NonNull []> getDischargeWindowIndependentOfLoad(@NonNull final IDischargeOption portSlot, @NonNull final IPortTimeWindowsRecord portTimeWindowRecord) {
		assert portSlot.getDischargePriceCalculator() instanceof IPriceIntervalProvider;
		final int start = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(start, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());

		return ((IPriceIntervalProvider) portSlot.getDischargePriceCalculator()).getPriceIntervals(portSlot, start, end, portTimeWindowRecord);
	}

	@Override
	public List<int @NonNull []> getDischargeWindowBasedOnLoad(final IDischargeOption portSlot, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final ILoadOption loadOption = priceIntervalProviderUtil.getFirstLoadOption(portTimeWindowRecord.getSlots());
		assert loadOption != null;
		assert loadOption.getLoadPriceCalculator() instanceof IPriceIntervalProvider;
		final int start = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(start, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());
		return ((IPriceIntervalProvider) loadOption.getLoadPriceCalculator()).getPriceIntervals(loadOption, start, end, portTimeWindowRecord);
	}

	@Override
	public List<int @NonNull []> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(final @NonNull ILoadOption load, final @NonNull IDischargeOption discharge,
			final @NonNull IPriceIntervalProvider loadPriceIntervalProvider, final @NonNull IPriceIntervalProvider dischargePriceIntervalProvider,
			final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final boolean dateFromLoad) {
		final int start = load.getTimeWindow().getInclusiveStart();
		final ITimeWindow loadFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		final ITimeWindow dischargeFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		final int end = findBestEnd(start, loadFeasibletimeWindow.getInclusiveStart(), discharge.getTimeWindow().getExclusiveEnd(), dischargeFeasibletimeWindow.getExclusiveEnd());
		return priceIntervalProviderUtil.buildComplexPriceIntervals(start, end, load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowsRecord);
	}

	/**
	 * Return the max of the window end and the feasbile end (if late). Add 1 hour if start == end
	 * 
	 * @param windowStart
	 * @param feasibleStart
	 * @param windowEnd
	 * @param feasibleEnd
	 * @return
	 */
	private int findBestEnd(final int windowStart, final int feasibleStart, final int windowEnd, final int feasibleEnd) {
		int maxEnd = Math.max(windowEnd, feasibleEnd);
		if (windowStart == maxEnd || feasibleStart == maxEnd) {
			maxEnd += 1;
			assert false;
		}
		return maxEnd + 1;
	}

	@Override
	public void reset() {
	}

	@Override
	public void dispose() {
	}

}