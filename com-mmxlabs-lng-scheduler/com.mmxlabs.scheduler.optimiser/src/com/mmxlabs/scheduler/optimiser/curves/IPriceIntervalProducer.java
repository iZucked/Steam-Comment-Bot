package com.mmxlabs.scheduler.optimiser.curves;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public interface IPriceIntervalProducer {

	public abstract List<int[]> getLoadIntervalsIndependentOfDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getLoadIntervalsBasedOnDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getDischargeWindowIndependentOfLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getDischargeWindowBasedOnLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad);

	public void reset();
	
	public void dispose();

}