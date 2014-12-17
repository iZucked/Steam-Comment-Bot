/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.Date;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LatenessUtils {
	public static boolean isLate(Event e) {
		if (e instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) e;
			// Exclude DES Purchase and fob sales
			final Sequence seq = visit.getSequence();
			if (seq.getSequenceType() == SequenceType.DES_PURCHASE || seq.getSequenceType() == SequenceType.FOB_SALE) {
				return false;
			}
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return true;
			}

		} else if (e instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) e;
			if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
				return true;
			}
		} else if (e instanceof PortVisit) {
			final PortVisit visit = (PortVisit) e;
			final Sequence seq = visit.getSequence();
		
			final VesselAvailability availability = seq.getVesselAvailability();
			if (availability == null) {
				return false;
			}
			if (seq.getEvents().indexOf(visit) == 0) {

				final Date startBy = availability.getStartBy();
				if (startBy != null && visit.getStart().after(startBy)) {
					return true;
				}

			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				final Date endBy = availability.getEndBy();
				if (endBy != null && visit.getStart().after(endBy)) {
					return true;
				}
			}
			// setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
		}
		return false;
		
	}

}
