/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.PortVisit;

public class CapacityUtils {

	public static boolean hasViolation(final Event e) {
		if (e instanceof PortVisit) {
			final PortVisit portVisit = (PortVisit) e;
			return !portVisit.getViolations().isEmpty();
		}
		return false;
	}

	public static int getViolationCount(final Event event) {
		if (event instanceof PortVisit) {
			final PortVisit portVisit = (PortVisit) event;
			return portVisit.getViolations().size();
		}
		return 0;
	}

	public static int getViolationCount(final EventGrouping eventGrouping) {

		int count = 0;
		for (final Event event : eventGrouping.getEvents()) {
			count += getViolationCount(event);
		}
		return count;
	}
}
