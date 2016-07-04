/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Sequence;

public class VesselFormatter extends BaseFormatter {

	@Override
	public String render(Object object) {
		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			return sequence.getName();
		}
		return "";
	}

}
