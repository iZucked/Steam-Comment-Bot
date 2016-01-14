package com.mmxlabs.scheduler.optimiser.curves;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public interface IPriceIntervalProducer {

	/**
	 * Reset the state of this object ready for a new optimisation.
	 */
	public abstract void reset();
	
	/**
	 * Clean up all references.
	 */
	public abstract void dispose();

	public abstract List<int[]> getLoadIntervalsIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load);

	public abstract List<int[]> getLoadIntervalsBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge);

	public abstract List<int[]> getDischargeWindowIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge);

	public abstract List<int[]> getDischargeWindowBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge);

	public abstract List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad);



}