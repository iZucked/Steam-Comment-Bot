/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CanalCostFormatter extends IntegerFormatter {

	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int cost = 0;
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			cost += calculateCost(sequence);
		} else if (object instanceof List) {
			List objects = (List) object;
			if (objects.size() > 0) {
				for (Object o : objects) {
					if (o instanceof Sequence) {
						cost += calculateCost((Sequence) o);
					}
				}
			}
		}

		return cost;
	}

	private int calculateCost(Sequence sequence) {
		int cost = 0;
		for (Event evt : sequence.getEvents()) {
			if (evt instanceof Journey) {
				Journey journey = (Journey) evt;
				cost += journey.getToll();
			}
		}
		return cost;
	}

}
