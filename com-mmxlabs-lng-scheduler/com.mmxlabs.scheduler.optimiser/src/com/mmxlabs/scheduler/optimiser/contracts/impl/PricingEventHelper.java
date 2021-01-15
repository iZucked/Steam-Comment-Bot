/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class PricingEventHelper {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;
	
	public IPort getLoadTimingPort(@NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption) {

		if (!(loadOption instanceof ILoadSlot)) {
			// DES Purchase - take discharge port
			return dischargeOption.getPort();
		} else {
			return loadOption.getPort();
		}
	}

	public IPort getDischargeTimingPort(@NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption) {

		if (!(dischargeOption instanceof IDischargeSlot)) {
			// FOB Sale - take load port
			return loadOption.getPort();
		} else {
			return dischargeOption.getPort();
		}
	}

	public int getStartOfLoadInUTC(@NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption, @NonNull final IPortTimesRecord portTimesRecord) {

		final int localTime = portTimesRecord.getSlotTime(loadOption);
		return timeZoneToUtcOffsetProvider.UTC(localTime, getLoadTimingPort(loadOption, dischargeOption));
	}

	public int getDischargePricingDate(@NonNull final IDischargeOption dischargeOption, @NonNull final IPortTimesRecord portTimesRecord) {

		final int pricingDate;
		final int dischargePricingDate = dischargeOption.getPricingDate();
		if (dischargePricingDate != IPortSlot.NO_PRICING_DATE) {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(dischargePricingDate, dischargeOption);
		} else {
			final PricingEventType pricingEvent = dischargeOption.getPricingEvent();
			pricingDate = getDischargePricingDateFromEventType(pricingEvent, dischargeOption, portTimesRecord);
		}
		return pricingDate;
	}

	public int getLoadPricingDate(@NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption, @NonNull final IPortTimesRecord portTimesRecord) {
		final int pricingDate;
		final int loadPricingDate = loadOption.getPricingDate();
		if (loadPricingDate != IPortSlot.NO_PRICING_DATE) {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(loadPricingDate, loadOption);
		} else {
			final PricingEventType pricingEvent = loadOption.getPricingEvent();
			pricingDate = getLoadPricingDateFromEventType(pricingEvent, loadOption, dischargeOption, portTimesRecord);
		}
		return pricingDate;
	}

	public int getDischargePricingDateFromEventType(@NonNull final PricingEventType pricingEvent, @NonNull final IDischargeOption dischargeOption, @NonNull final IPortTimesRecord portTimesRecord) {
		int pricingDate;
		final ILoadOption loadOption = findFirstLoadOption(portTimesRecord);
		assert loadOption != null;
		
		final IPort timingPortForLoad = getLoadTimingPort(loadOption, dischargeOption);
		final IPort timingPortForDischarge = getDischargeTimingPort(loadOption, dischargeOption);
		switch (pricingEvent) {
		case END_OF_DISCHARGE: {
			final int dischargeDuration = portTimesRecord.getSlotDuration(dischargeOption);
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption) + dischargeDuration, timingPortForDischarge);
			break;
		}
		case END_OF_DISCHARGE_WINDOW: {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(dischargeOption.getTimeWindow().getExclusiveEnd(), dischargeOption);
			break;
		}
		case END_OF_LOAD: {
			final int loadDuration = portTimesRecord.getSlotDuration(loadOption);
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption) + loadDuration, timingPortForLoad);
			break;
		}
		case END_OF_LOAD_WINDOW: {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(loadOption.getTimeWindow().getExclusiveEnd(), loadOption);
			break;
		}
		case START_OF_LOAD: {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption), timingPortForLoad);
			break;
		}
		case START_OF_LOAD_WINDOW: {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(loadOption.getTimeWindow().getInclusiveStart(), loadOption);
			break;
		}
		case START_OF_DISCHARGE_WINDOW: {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(dischargeOption.getTimeWindow().getInclusiveStart(), dischargeOption);
			break;
		}
		default:
			// Default is start of discharge
		case START_OF_DISCHARGE: {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption), timingPortForDischarge);
			
			break;
		}
		}
		return pricingDate;
	}

	public int getLoadPricingDateFromEventType(@NonNull final PricingEventType pricingEvent, @NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption,
			@NonNull final IPortTimesRecord portTimesRecord) {
		int pricingDate;
		final IPort timingPortForLoad = getLoadTimingPort(loadOption, dischargeOption);
		final IPort timingPortForDischarge = getDischargeTimingPort(loadOption, dischargeOption);

		switch (pricingEvent) {
		case END_OF_DISCHARGE:
			final int dischargeDuration = portTimesRecord.getSlotDuration(dischargeOption);
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption) + dischargeDuration, timingPortForDischarge);
			break;
		case END_OF_DISCHARGE_WINDOW:
			pricingDate = timeZoneToUtcOffsetProvider.UTC(dischargeOption.getTimeWindow().getExclusiveEnd(), dischargeOption);
			break;
		case END_OF_LOAD:
			final int loadDuration = portTimesRecord.getSlotDuration(loadOption);
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption) + loadDuration, timingPortForLoad);
			break;
		case END_OF_LOAD_WINDOW:
			pricingDate = timeZoneToUtcOffsetProvider.UTC(loadOption.getTimeWindow().getExclusiveEnd(), loadOption);
			break;
		case START_OF_DISCHARGE:
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption), timingPortForDischarge);
			break;
		case START_OF_DISCHARGE_WINDOW:
			pricingDate = timeZoneToUtcOffsetProvider.UTC(dischargeOption.getTimeWindow().getInclusiveStart(), dischargeOption);
			break;
		case START_OF_LOAD_WINDOW:
			pricingDate = timeZoneToUtcOffsetProvider.UTC(loadOption.getTimeWindow().getInclusiveStart(), loadOption);
			break;
		default:
			// Default is start of load
		case START_OF_LOAD:
			pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption), timingPortForLoad);
			break;
		}
		return pricingDate;
	}

	public static ILoadOption findFirstLoadOption(final IPortTimesRecord portTimesRecord) {
		ILoadOption loadOption = null;
		// Find first load option
		for (final IPortSlot slot : portTimesRecord.getSlots()) {
			assert (slot != null);
			if (slot instanceof ILoadOption) {
				loadOption = (ILoadOption) slot;
				return loadOption;
			}
		}
		return loadOption;
	}
}
