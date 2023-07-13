package com.mmxlabs.lingo.reports.emissions.cii.managers;

import com.mmxlabs.lingo.reports.emissions.cii.CIIGradesData;
import com.mmxlabs.models.lng.fleet.Vessel;

public class CIIVesselColumnManager extends AbstractCIIColumnManager {

	public CIIVesselColumnManager() {
		super("Vessel");
	}

	@Override
	public String getColumnText(final CIIGradesData data) {
		final Vessel vessel = data.getVessel();
		if (vessel == null) {
			return null;
		}
		return vessel.getName();
	}
}
