/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CharterCostFormatter extends CostFormatter {

	public CharterCostFormatter(boolean includeUnits) {
		super(includeUnits);
	}

	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int cost = 0;
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			for (Event evt : sequence.getEvents()) {
				cost += evt.getCharterCost();
			}
		}

		return cost;
	}

}
