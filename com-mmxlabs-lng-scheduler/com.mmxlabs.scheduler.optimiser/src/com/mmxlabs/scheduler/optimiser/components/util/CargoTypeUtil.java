/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.util;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;

/**
 */
public class CargoTypeUtil {

	@Inject
	@NonNull
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	public static enum SimpleCargoType {
		SHIPPED, FOB_SALE, DES_PURCHASE
	}

	public static enum DetailedCargoType {
		UNKNOWN, SHIPPED, OPEN_FOB_PURCHASE, OPEN_DES_SALE, FOB_SALE, DIVERTABLE_FOB_SALE, DES_PURCHASE, DIVERTABLE_DES_PURCHASE
	}

	@NonNull
	public static SimpleCargoType getSimpleCargoType(@Nullable final ILoadOption buy, @Nullable final IDischargeOption sell) {
		if (buy instanceof ILoadSlot && sell instanceof IDischargeSlot) {
			return SimpleCargoType.SHIPPED;
		} else if (buy instanceof ILoadSlot) {
			if (sell == null) {
				// Assume shipped if sell is null
				return SimpleCargoType.SHIPPED;
			} else {
				return SimpleCargoType.FOB_SALE;
			}
		} else {
			return SimpleCargoType.DES_PURCHASE;
		}
	}

	/**
	 * Determines a more detailed cargo type analysis of the given slots. This will return more specifically whether or not a non-shipped cargo is divertable or not. This method will indicate an open
	 * shipped position, but not an open non-shipped position.
	 * 
	 * @param slots
	 * @return
	 */
	@NonNull
	public DetailedCargoType getDetailedCargoType(@NonNull final Collection<IPortSlot> slots) {
		DetailedCargoType type = DetailedCargoType.UNKNOWN;
		if (slots.size() == 0) {
			return type;
		}
		if (slots.size() == 1) {
			final IPortSlot slot = slots.iterator().next();
			if (slot instanceof ILoadOption) {
				if (slot instanceof ILoadSlot) {
					return DetailedCargoType.OPEN_FOB_PURCHASE;
				} else if (shippingHoursRestrictionProvider.isDivertable(portSlotProvider.getElement(slot))) {
					return DetailedCargoType.DIVERTABLE_DES_PURCHASE;
				} else {
					return DetailedCargoType.DES_PURCHASE;
				}
			} else if (slot instanceof IDischargeOption) {
				if (slot instanceof IDischargeSlot) {
					return DetailedCargoType.OPEN_DES_SALE;
				} else if (shippingHoursRestrictionProvider.isDivertable(portSlotProvider.getElement(slot))) {
					return DetailedCargoType.DIVERTABLE_FOB_SALE;
				} else {
					return DetailedCargoType.FOB_SALE;
				}
			}
			return type;
		}

		for (final IPortSlot slot : slots) {
			// Vessel event, unknown cargo type.
			if (slot instanceof VesselEventPortSlot) {
				return DetailedCargoType.UNKNOWN;
			}
			if (slot instanceof ILoadOption) {
				if (slot instanceof ILoadSlot) {
					// Might be shipped, depends on other parts of the cargo
					type = DetailedCargoType.SHIPPED;
				} else if (shippingHoursRestrictionProvider.isDivertable(portSlotProvider.getElement(slot))) {
					return DetailedCargoType.DIVERTABLE_DES_PURCHASE;
				} else {
					return DetailedCargoType.DES_PURCHASE;
				}
			} else if (slot instanceof IDischargeOption) {
				if (slot instanceof IDischargeSlot) {
					// Might be shipped, depends on other parts of the cargo
					type = DetailedCargoType.SHIPPED;
				} else if (shippingHoursRestrictionProvider.isDivertable(portSlotProvider.getElement(slot))) {
					return DetailedCargoType.DIVERTABLE_FOB_SALE;
				} else {
					return DetailedCargoType.FOB_SALE;
				}
			}
		}
		return type;
	}

}
