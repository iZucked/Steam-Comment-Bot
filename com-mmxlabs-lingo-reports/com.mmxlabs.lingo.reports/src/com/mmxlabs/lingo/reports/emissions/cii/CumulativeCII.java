package com.mmxlabs.lingo.reports.emissions.cii;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.cii.CIIAccumulatableEventModel;
import com.mmxlabs.models.lng.schedule.cii.UtilsCII;

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
	
	public void accumulateEventModel(final CIIAccumulatableEventModel model) {
		accumulateEventModel(model, 1.0);
	}
	
	public void accumulateEventModel(final CIIAccumulatableEventModel model, final double linearInterpolationCoefficient) {
		final Event event = model.getCIIEvent();
		if (event == null) {
			return;
		}
		if (event instanceof final Journey journey) {
			totalDistance += journey.getDistance() * linearInterpolationCoefficient;
		}
		totalEmission += model.getTotalEmissionForCII() * linearInterpolationCoefficient;
	}
}
