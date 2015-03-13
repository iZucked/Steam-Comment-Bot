/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CanalCostFormatter extends CostFormatter {

	public CanalCostFormatter(boolean includeUnits) {
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
				if (evt instanceof Journey) {
					Journey journey = (Journey) evt;
					cost += journey.getToll();
				}
			}
		}

		return cost;
	}

}
