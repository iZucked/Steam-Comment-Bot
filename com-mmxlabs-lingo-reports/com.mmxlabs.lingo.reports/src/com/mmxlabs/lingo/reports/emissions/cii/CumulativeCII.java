package com.mmxlabs.lingo.reports.emissions.cii;

import com.mmxlabs.lingo.reports.emissions.EmissionsUtils;
import com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportModelV1;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;

/**
 * class which is dedicated to accumulate information from events and then compute final CII
 * @author Andrey Popov
 *
 */
public class CumulativeCII {
	
	private final Vessel vessel;
	private double totalEmission = 0.0;
	private double totalDistance = 0.0;
	
	public CumulativeCII(final Vessel vessel) {
		this.vessel = vessel;
	}
	
	final double findCII() {
		return UtilsCII.findCII(vessel, totalEmission, totalDistance);
	}

	public void accumulateEventModel(final VesselEmissionAccountingReportModelV1 model) {
		final Event event = model.getEvent();
		if (event == null) {
			return;
		}
		if (event instanceof final Journey journey) {
			totalDistance += journey.getDistance();
		}
		totalEmission += model.totalEmission;
	}
}
