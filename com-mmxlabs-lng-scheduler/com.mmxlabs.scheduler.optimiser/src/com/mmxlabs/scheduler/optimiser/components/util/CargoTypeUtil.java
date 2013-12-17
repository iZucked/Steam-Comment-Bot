/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.util;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

/**
 * @since 8.0
 */
public class CargoTypeUtil {

	public static enum CargoType {
		SHIPPED, FOB_SALE, DES_PURCHASE
	}

	public static CargoType getCargoType(final ILoadOption buy, final IDischargeOption sell) {
		if (buy instanceof ILoadSlot && sell instanceof IDischargeSlot) {
			return CargoType.SHIPPED;
		} else if (buy instanceof ILoadSlot) {
			if (sell == null) {
				// Assume shipped if sell is null
				return CargoType.SHIPPED;
			} else {
				return CargoType.FOB_SALE;
			}
		} else {
			return CargoType.DES_PURCHASE;
		}
	}

}
