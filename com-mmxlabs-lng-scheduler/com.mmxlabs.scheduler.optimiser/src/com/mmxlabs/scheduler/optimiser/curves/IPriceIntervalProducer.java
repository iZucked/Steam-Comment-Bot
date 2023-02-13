/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

	List<int @NonNull []> getLoadIntervalsIndependentOfDischarge(@NonNull ILoadOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	List<int @NonNull []> getLoadIntervalsBasedOnDischarge(@NonNull ILoadOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	List<int @NonNull []> getDischargeWindowIndependentOfLoad(@NonNull IDischargeOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	List<int @NonNull []> getDischargeWindowBasedOnLoad(@NonNull IDischargeOption portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	List<int @NonNull []> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(@NonNull ILoadOption load, @NonNull IDischargeOption discharge,
			@NonNull IPriceIntervalProvider loadPriceIntervalProvider, @NonNull IPriceIntervalProvider dischargePriceIntervalProvider, @NonNull IPortTimeWindowsRecord portTimeWindowRecord,
			boolean dateFromLoad);

	void reset();

	void dispose();

}