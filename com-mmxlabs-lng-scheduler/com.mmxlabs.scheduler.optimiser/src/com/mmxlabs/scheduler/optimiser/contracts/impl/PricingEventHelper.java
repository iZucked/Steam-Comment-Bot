package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

public final class PricingEventHelper {

	private PricingEventHelper() {

	}

	public static int getLoadPricingDate(final ILoadOption loadOption, IDischargeOption dischargeOption, final IAllocationAnnotation allocationAnnotation) {
		final int pricingDate;
		if (loadOption != null) {
			final int loadPricingDate = loadOption.getPricingDate();
			if (loadPricingDate != IPortSlot.NO_PRICING_DATE) {
				pricingDate = loadPricingDate;
			} else {
//				final PricingEventType pricingEvent = loadOption.getPricingEvent();
//				switch (pricingEvent) {
//				case END_OF_DISCHARGE:
//					int dischargeDuration = allocationAnnotation.getSlotDuration(dischargeOption);
//					pricingDate = allocationAnnotation.getSlotTime(dischargeOption) + dischargeDuration;
//					break;
//				case END_OF_LOAD:
//					int loadDuration = allocationAnnotation.getSlotDuration(loadOption);
//					pricingDate = allocationAnnotation.getSlotTime(loadOption) + loadDuration;
//					break;
//				case START_OF_DISCHARGE:
//					pricingDate = allocationAnnotation.getSlotTime(dischargeOption);
//					break;
//				default:
//					pricingDate = allocationAnnotation.getSlotTime(loadOption);
//					break;
//				}
				pricingDate = allocationAnnotation.getSlotTime(loadOption);
			}
		} else {
			// Default is start of load
			pricingDate = allocationAnnotation.getSlotTime(loadOption);
		}
		return pricingDate;
	}

	public static int getDischargePricingDate(final IDischargeOption dischargeOption, ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation) {
		final int pricingDate;
		if (dischargeOption != null) {
			final int dischargePricingDate = dischargeOption.getPricingDate();
			if (dischargePricingDate != IPortSlot.NO_PRICING_DATE) {
				pricingDate = dischargePricingDate;
			} else {
//				final PricingEventType pricingEvent = dischargeOption.getPricingEvent();
//				switch (pricingEvent) {
//				case END_OF_DISCHARGE:
//					int dischargeDuration = allocationAnnotation.getSlotDuration(dischargeOption);
//					pricingDate = allocationAnnotation.getSlotTime(dischargeOption) + dischargeDuration;
//					break;
//				case END_OF_LOAD:
//					int loadDuration = allocationAnnotation.getSlotDuration(loadOption);
//					pricingDate = allocationAnnotation.getSlotTime(loadOption) + loadDuration;
//					break;
//				case START_OF_DISCHARGE:
//					pricingDate = allocationAnnotation.getSlotTime(dischargeOption);
//					break;
//				default:
//					pricingDate = allocationAnnotation.getSlotTime(loadOption);
//					break;
//				}
				pricingDate = allocationAnnotation.getSlotTime(dischargeOption);
			}
		} else {
			// Default is start of discharge
			pricingDate = allocationAnnotation.getSlotTime(dischargeOption);
		}
		return pricingDate;
	}
}
