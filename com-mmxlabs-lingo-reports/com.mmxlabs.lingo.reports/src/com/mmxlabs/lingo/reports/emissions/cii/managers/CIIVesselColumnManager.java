package com.mmxlabs.lingo.reports.emissions.cii.managers;

import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.CIITabularReportView.CIIGradesData;
import com.mmxlabs.models.lng.fleet.Vessel;

public class CIIVesselColumnManager extends ColumnManager<CIIGradesData> {

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

	@Override
	public int compare(final CIIGradesData o1, final CIIGradesData o2) {
		final String s1 = getColumnText(o1);
		final String s2 = getColumnText(o2);
		if (s1 == null) {
			return -1;
		}
		if (s2 == null) {
			return 1;
		}
		return s1.compareTo(s2);
	}

	@Override
	public boolean isTree() {
		return false;
	}
}
