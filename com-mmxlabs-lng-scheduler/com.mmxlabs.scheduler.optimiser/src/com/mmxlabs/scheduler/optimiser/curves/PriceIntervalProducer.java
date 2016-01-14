package com.mmxlabs.scheduler.optimiser.curves;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class PriceIntervalProducer implements IPriceIntervalProducer {

	@Inject
	PriceIntervalProviderHelper priceIntervalProviderUtil;
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getLoadIntervalsIndependentOfDischarge(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.ILoadOption)
	 */
	@Override
	public List<int[]> getLoadIntervalsIndependentOfDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		return ((IPriceIntervalProvider) portSlot.getLoadPriceCalculator()).getPriceIntervals(portSlot, start, end, portTimeWindowRecord);
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getLoadIntervalsBasedOnDischarge(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getLoadIntervalsBasedOnDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		IDischargeOption discharge = priceIntervalProviderUtil.getFirstDischargeOption(portTimeWindowRecord.getSlots());
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		// DO NOT COMMIT (time window
		return ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getPriceIntervals(discharge, start, end, portTimeWindowRecord);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getDischargeWindowIndependentOfLoad(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getDischargeWindowIndependentOfLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		return ((IPriceIntervalProvider) portSlot.getDischargePriceCalculator()).getPriceIntervals(portSlot, start, end, portTimeWindowRecord);
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getDischargeWindowBasedOnLoad(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getDischargeWindowBasedOnLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		ILoadOption loadOption = priceIntervalProviderUtil.getFirstLoadOption(portTimeWindowRecord.getSlots());
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		return ((IPriceIntervalProvider) loadOption.getLoadPriceCalculator()).getPriceIntervals(loadOption, start, end, portTimeWindowRecord);
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption, com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider, com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider, com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, boolean)
	 */
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
		return maxEnd + 1; //DO NOT COMMIT
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}