/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public interface IPriceIntervalProducer {

	public abstract List<int[]> getLoadIntervalsIndependentOfDischarge(@NonNull ILoadOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getLoadIntervalsBasedOnDischarge(@NonNull ILoadOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getDischargeWindowIndependentOfLoad(@NonNull IDischargeOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getDischargeWindowBasedOnLoad(@NonNull IDischargeOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	public abstract List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(@NonNull ILoadOption load, @NonNull IDischargeOption discharge,
			@NonNull IPriceIntervalProvider loadPriceIntervalProvider, @NonNull IPriceIntervalProvider dischargePriceIntervalProvider, @NonNull IPortTimeWindowsRecord portTimeWindowRecord,
			boolean dateFromLoad);

	public void reset();

	public void dispose();

}