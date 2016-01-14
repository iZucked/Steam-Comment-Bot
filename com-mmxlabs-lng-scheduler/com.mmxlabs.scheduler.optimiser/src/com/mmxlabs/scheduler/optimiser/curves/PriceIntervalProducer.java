package com.mmxlabs.scheduler.optimiser.curves;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceIntervalProviderUtil;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class PriceIntervalProducer implements IPriceIntervalProducer {

	@Inject
	PriceIntervalProviderUtil priceIntervalProviderUtil;
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getLoadIntervalsIndependentOfDischarge(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.ILoadOption)
	 */
	@Override
	public List<int[]> getLoadIntervalsIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		
		int start = load.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		int end = feasibletimeWindow.getEnd(); // in case we're late

		return ((IPriceIntervalProvider) load.getLoadPriceCalculator()).getPriceIntervals(start, end, load, null, portTimeWindowRecord);
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getLoadIntervalsBasedOnDischarge(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getLoadIntervalsBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		int start = load.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		int end = feasibletimeWindow.getEnd(); // in case we're late

		return ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getPriceIntervals(start, end, null, discharge, portTimeWindowRecord);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getDischargeWindowIndependentOfLoad(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getDischargeWindowIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		int start = discharge.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		int end = feasibletimeWindow.getEnd(); // in case we're late

		return ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getPriceIntervals(start, end, null, discharge, portTimeWindowRecord);

	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getDischargeWindowBasedOnLoad(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getDischargeWindowBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		int start = load.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		int end = feasibletimeWindow.getEnd(); // in case we're late

		return ((IPriceIntervalProvider) load.getLoadPriceCalculator()).getPriceIntervals(start, end, load, null, portTimeWindowRecord);
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption, com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider, com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider, com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, boolean)
	 */
	@Override
	public List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowsRecord, boolean dateFromLoad) {
		int start = load.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		int end = feasibletimeWindow.getEnd(); // in case we're late

		return priceIntervalProviderUtil.buildComplexPriceIntervals(start, end, load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowsRecord, dateFromLoad);
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