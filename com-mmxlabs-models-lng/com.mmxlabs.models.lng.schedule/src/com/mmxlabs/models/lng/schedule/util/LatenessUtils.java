/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LatenessUtils {

	public static boolean isLateExcludingFlex(final Event e) {
		if (e instanceof PortVisit) {
			final PortVisitLateness portVisitLateness = ((PortVisit) e).getLateness();
			if (portVisitLateness != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if it is late, event after considering the Slot flex time. If this returns false and {@link #isLateExcludingFlex(Event)} returns true, then the slot is late,but within the flex
	 * time.
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isLateAfterFlex(final Event e) {
		if (e instanceof PortVisit) {
			final PortVisitLateness portVisitLateness = ((PortVisit) e).getLateness();
			if (portVisitLateness != null) {
				if (e instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) e;
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					if (slotAllocation != null) {
						final Slot slot = slotAllocation.getSlot();
						if (slot.getWindowFlex() > 0 && getLatenessInHours(slotVisit) < slot.getWindowFlex()) {
							return false;
						}
					}

				}

				return true;
			}
		}
		return false;
	}

	public static ZonedDateTime getWindowStartDate(final Object object) {
		if (object instanceof SlotVisit) {
			return ((SlotVisit) object).getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime();
		} else if (object instanceof VesselEventVisit) {
			return ((VesselEventVisit) object).getVesselEvent().getStartAfterAsDateTime();
		}
		return null;
	}

	public static ZonedDateTime getWindowEndDate(final Object object) {
		if (object instanceof SlotVisit) {
			return ((SlotVisit) object).getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime();
		} else if (object instanceof VesselEventVisit) {
			return ((VesselEventVisit) object).getVesselEvent().getStartByAsDateTime();
		} else if (object instanceof PortVisit) {
			final PortVisit visit = (PortVisit) object;
			final Sequence seq = visit.getSequence();
			final VesselAvailability vesselAvailability = seq.getVesselAvailability();
			if (vesselAvailability == null) {
				return null;
			}
			if (seq.getEvents().indexOf(visit) == 0) {
				return vesselAvailability.getStartByAsDateTime();
			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				return vesselAvailability.getEndByAsDateTime();
			}
		}
		return null;
	}

	public static String formatLatenessHours(final int hours) {
		if (hours / 24 == 0) {
			return String.format("%2dh", hours % 24);
		} else {
			return String.format("%2dd, %2dh", hours / 24, hours % 24);
		}

	}

	public static int getLatenessInHours(final PortVisit visit) {

		final PortVisitLateness portVisitLateness = visit.getLateness();
		if (portVisitLateness != null) {
			return portVisitLateness.getLatenessInHours();
		}
		return 0;
	}

	public static int getLatenessAfterFlex(@Nullable final EventGrouping eventGrouping) {
		int lateness = 0;
		if (eventGrouping != null) {

			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final boolean isLate = LatenessUtils.isLateAfterFlex(visit);
					if (isLate) {
						lateness += LatenessUtils.getLatenessInHours(visit);
					}
				}
			}
		}
		return lateness;
	}

	public static long getLatenessExcludingFlex(@Nullable final EventGrouping eventGrouping) {
		long lateness = 0;
		if (eventGrouping != null) {

			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final boolean isLate = LatenessUtils.isLateExcludingFlex(visit);
					if (isLate) {
						lateness += LatenessUtils.getLatenessInHours(visit);
					}
				}
			}
		}
		return lateness;
	}

}
