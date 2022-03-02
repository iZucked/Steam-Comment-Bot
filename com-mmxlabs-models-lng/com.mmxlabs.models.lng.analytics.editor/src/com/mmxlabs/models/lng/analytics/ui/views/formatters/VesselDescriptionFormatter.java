/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.VesselAndClassContentProvider;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class VesselDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof VesselAndClassContentProvider.VesselContainer) {
			return "<<Vessels>>";
		} else if (object instanceof Vessel) {
			Vessel vessel = (Vessel) object;
			return vessel.getName();
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
