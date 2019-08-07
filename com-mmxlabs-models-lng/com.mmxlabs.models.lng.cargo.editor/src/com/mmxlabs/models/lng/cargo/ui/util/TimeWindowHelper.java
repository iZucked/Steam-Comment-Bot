package com.mmxlabs.models.lng.cargo.ui.util;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.TimePeriod;

public class TimeWindowHelper {
	
	public static String getTimeWindowSuffix(final Object owner) {
		if (owner instanceof Slot<?>) {
			final Slot<?> slot = (Slot<?>) owner;
			final int size = slot.getWindowSize();
			final TimePeriod units = slot.getWindowSizeUnits();
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
			if (size > 0) {
				return String.format(" +%d%s", size, suffix);
			}		
		}
		return "";
	}

	public static String getCPWindowSuffix(Object owner) {
		if (owner instanceof Slot<?>) {
			final Slot<?> slot = (Slot<?>) owner;
			if (((Slot<?>) owner).isWindowCounterParty()) {
				return "CP";
			}
		}
		return "";
	}
}
