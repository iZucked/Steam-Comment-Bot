package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class PricingEventHelper {

	private PricingEventHelper() {

	}

	public static int getLoadPricingDate(final ILoadOption loadOption, final IDischargeOption dischargeOption, final IPortTimesRecord portTimesRecord) {
		final int pricingDate;
		if (loadOption != null) {
			final int loadPricingDate = loadOption.getPricingDate();
			if (loadPricingDate != IPortSlot.NO_PRICING_DATE) {
				pricingDate = loadPricingDate;
			} else {
				final PricingEventType pricingEvent = loadOption.getPricingEvent();
				switch (pricingEvent) {
				case END_OF_DISCHARGE:
					final int dischargeDuration = portTimesRecord.getSlotDuration(dischargeOption);
					pricingDate = portTimesRecord.getSlotTime(dischargeOption) + dischargeDuration;
					break;
				case END_OF_LOAD:
					final int loadDuration = portTimesRecord.getSlotDuration(loadOption);
					pricingDate = portTimesRecord.getSlotTime(loadOption) + loadDuration;
					break;
				case START_OF_DISCHARGE:
					pricingDate = portTimesRecord.getSlotTime(dischargeOption);
					break;
				default:
					// Default is start of load
				case START_OF_LOAD:
					pricingDate = portTimesRecord.getSlotTime(loadOption);
					break;
				}
			}
		} else {
			// Default is start of load
			pricingDate = portTimesRecord.getSlotTime(loadOption);
		}
		return pricingDate;
	}

	public static int getDischargePricingDate(final IDischargeOption dischargeOption, final IPortTimesRecord portTimesRecord) {

		final int pricingDate;
		if (dischargeOption != null) {
			final int dischargePricingDate = dischargeOption.getPricingDate();
			if (dischargePricingDate != IPortSlot.NO_PRICING_DATE) {
				pricingDate = dischargePricingDate;
			} else {
				final PricingEventType pricingEvent = dischargeOption.getPricingEvent();
				switch (pricingEvent) {
				case END_OF_DISCHARGE: {
					final int dischargeDuration = portTimesRecord.getSlotDuration(dischargeOption);
					pricingDate = portTimesRecord.getSlotTime(dischargeOption) + dischargeDuration;
					break;
				}
				case END_OF_LOAD: {
					final ILoadOption loadOption = findFirstLoadOption(portTimesRecord);
					final int loadDuration = portTimesRecord.getSlotDuration(loadOption);
					pricingDate = portTimesRecord.getSlotTime(loadOption) + loadDuration;
					break;
				}
				case START_OF_LOAD: {
					final ILoadOption loadOption = findFirstLoadOption(portTimesRecord);
					pricingDate = portTimesRecord.getSlotTime(loadOption);
					break;
				}
				default:
					// Default is start of discharge
				case START_OF_DISCHARGE: {
					pricingDate = portTimesRecord.getSlotTime(dischargeOption);
					break;
				}
				}
			}
		} else {
			// Default is start of discharge
			pricingDate = portTimesRecord.getSlotTime(dischargeOption);
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
