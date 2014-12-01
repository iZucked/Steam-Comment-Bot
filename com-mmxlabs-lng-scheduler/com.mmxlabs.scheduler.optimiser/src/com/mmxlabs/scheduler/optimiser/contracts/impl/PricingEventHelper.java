/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class PricingEventHelper {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public int getLoadPricingDate(@NonNull final ILoadOption loadOption, @NonNull final IDischargeOption dischargeOption, @NonNull final IPortTimesRecord portTimesRecord) {
		final int pricingDate;
		final int loadPricingDate = loadOption.getPricingDate();
		if (loadPricingDate != IPortSlot.NO_PRICING_DATE) {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(loadPricingDate, loadOption);
		} else {
			final PricingEventType pricingEvent = loadOption.getPricingEvent();
			switch (pricingEvent) {
			case END_OF_DISCHARGE:
				final int dischargeDuration = portTimesRecord.getSlotDuration(dischargeOption);
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption) + dischargeDuration, dischargeOption);
				break;
			case END_OF_LOAD:
				final int loadDuration = portTimesRecord.getSlotDuration(loadOption);
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption) + loadDuration, loadOption);
				break;
			case START_OF_DISCHARGE:
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption), dischargeOption);
				break;
			default:
				// Default is start of load
			case START_OF_LOAD:
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption), loadOption);
				break;
			}
		}
		return pricingDate;
	}

	public int getDischargePricingDate(@NonNull final IDischargeOption dischargeOption, @NonNull final IPortTimesRecord portTimesRecord) {

		final int pricingDate;
		final int dischargePricingDate = dischargeOption.getPricingDate();
		if (dischargePricingDate != IPortSlot.NO_PRICING_DATE) {
			pricingDate = timeZoneToUtcOffsetProvider.UTC(dischargePricingDate, dischargeOption);
		} else {
			final PricingEventType pricingEvent = dischargeOption.getPricingEvent();
			switch (pricingEvent) {
			case END_OF_DISCHARGE: {
				final int dischargeDuration = portTimesRecord.getSlotDuration(dischargeOption);
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption) + dischargeDuration, dischargeOption);
				break;
			}
			case END_OF_LOAD: {
				final ILoadOption loadOption = findFirstLoadOption(portTimesRecord);
				final int loadDuration = portTimesRecord.getSlotDuration(loadOption);
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption) + loadDuration, loadOption);
				break;
			}
			case START_OF_LOAD: {
				final ILoadOption loadOption = findFirstLoadOption(portTimesRecord);
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(loadOption), loadOption);
				break;
			}
			default:
				// Default is start of discharge
			case START_OF_DISCHARGE: {
				pricingDate = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getSlotTime(dischargeOption), dischargeOption);
				;
				break;
			}
			}
		}
		return pricingDate;
	}

	public static ILoadOption findFirstLoadOption(final IPortTimesRecord portTimesRecord) {
		ILoadOption loadOption = null;
		// Find first load option
		for (final IPortSlot slot : portTimesRecord.getSlots()) {
			if (slot instanceof ILoadOption) {
				loadOption = (ILoadOption) slot;
				break;
			}
		}
		return loadOption;
	}
}
