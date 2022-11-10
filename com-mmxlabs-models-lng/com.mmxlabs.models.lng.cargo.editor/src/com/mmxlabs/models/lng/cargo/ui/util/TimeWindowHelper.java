/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.util;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.TimePeriod;

public class TimeWindowHelper {

	private TimeWindowHelper() {
	}

	public static String getTimeWindowSuffix(final Object owner) {
		if (owner instanceof Slot<?> slot) {
			final int size = (Integer) slot.eGetWithDefault(CargoPackage.eINSTANCE.getSlot_WindowSize());
			if (size > 0) {
				final TimePeriod units = (TimePeriod) slot.eGetWithDefault(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits());
				String suffix;
				switch (units) {
				case DAYS:
					suffix = "d";
					break;
				case HOURS:
					suffix = "h";
					break;
				case MONTHS:
					suffix = "m";
					break;
				default:
					return "";
				}
				return String.format(" +%d%s", size, suffix);
			}
		}
		return "";
	}

	public static String getCPWindowSuffix(Object owner) {
		return (owner instanceof Slot<?> slot && slot.isWindowCounterParty()) ? "CP" : "";
	}
}
