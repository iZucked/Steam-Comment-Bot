package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

public class VesselDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof Vessel) {
			Vessel vessel = (Vessel) object;
			return vessel.getName();
		} else if (object instanceof VesselClass) {
			VesselClass vesselClass = (VesselClass) object;
			return vesselClass.getName();
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
