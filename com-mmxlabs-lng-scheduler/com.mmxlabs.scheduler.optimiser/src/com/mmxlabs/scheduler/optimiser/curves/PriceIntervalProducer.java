package com.mmxlabs.scheduler.optimiser.curves;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Concrete implementation of an {@link IPriceIntervalProducer}
 * @author achurchill
 *
 */
public class PriceIntervalProducer implements IPriceIntervalProducer {

	@Inject
	PriceIntervalProviderHelper priceIntervalProviderUtil;
	
	@Override
	public List<int[]> getLoadIntervalsIndependentOfDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		assert portSlot.getLoadPriceCalculator() instanceof IPriceIntervalProvider;
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());
		return ((IPriceIntervalProvider) portSlot.getLoadPriceCalculator()).getPriceIntervals(portSlot, start, end, portTimeWindowRecord);
	}
	
	@Override
	public List<int[]> getLoadIntervalsBasedOnDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		IDischargeOption discharge = priceIntervalProviderUtil.getFirstDischargeOption(portTimeWindowRecord.getSlots());
		assert discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider;
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());
		return ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getPriceIntervals(discharge, start, end, portTimeWindowRecord);
	}

	@Override
	public List<int[]> getDischargeWindowIndependentOfLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		assert portSlot.getDischargePriceCalculator() instanceof IPriceIntervalProvider;
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		return ((IPriceIntervalProvider) portSlot.getDischargePriceCalculator()).getPriceIntervals(portSlot, start, end, portTimeWindowRecord);
	}
	
	@Override
	public List<int[]> getDischargeWindowBasedOnLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		ILoadOption loadOption = priceIntervalProviderUtil.getFirstLoadOption(portTimeWindowRecord.getSlots());
		assert loadOption.getLoadPriceCalculator() instanceof IPriceIntervalProvider;
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());
		return ((IPriceIntervalProvider) loadOption.getLoadPriceCalculator()).getPriceIntervals(loadOption, start, end, portTimeWindowRecord);
	}
	
	@Override
	public List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowsRecord, boolean dateFromLoad) {
		int start = load.getTimeWindow().getStart();
		ITimeWindow loadFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		ITimeWindow dischargeFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		int end = findBestEnd(start, loadFeasibletimeWindow.getStart(), discharge.getTimeWindow().getEnd(), dischargeFeasibletimeWindow.getEnd());
		return priceIntervalProviderUtil.buildComplexPriceIntervals(start, end, load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowsRecord);
	}

	/**
	 * Return the max of the window end and the feasbile end (if late). Add 1 hour if start == end
	 * @param windowStart
	 * @param feasibleStart
	 * @param windowEnd
	 * @param feasibleEnd
	 * @return
	 */
	private int findBestEnd(int windowStart, int feasibleStart, int windowEnd, int feasibleEnd) {
		int maxEnd = Math.max(windowEnd, feasibleEnd);
		if (windowStart == maxEnd || feasibleStart == maxEnd) {
			maxEnd += 1;
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