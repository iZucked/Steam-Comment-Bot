package com.mmxlabs.scheduler.optimiser.components.util;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

public class CargoTypeUtil {

	public static enum CargoType {
		SHIPPED, FOB_SALE, DES_PURCHASE
	}

	public static CargoType getCargoType(final ILoadOption buy, final IDischargeOption sell) {
		if (buy instanceof ILoadSlot && sell instanceof IDischargeSlot) {
			return CargoType.SHIPPED;
		} else if (buy instanceof ILoadSlot) {
			return CargoType.FOB_SALE;
		} else {
			return CargoType.DES_PURCHASE;
		}
	}

}

