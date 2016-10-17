/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

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
			cost += getCost(sequence);
		} else if (object instanceof List) {
			List objects = (List) object;
			if (objects.size() > 0) {
				for (Object o : objects) {
					if (o instanceof Sequence) {
						cost += getCost((Sequence) o);
					}
				}
			}
		}

		return cost;
	}

	private int getCost(Sequence sequence) {
		int cost = 0;
		for (Event evt : sequence.getEvents()) {
			cost += evt.getCharterCost();
		}
		return cost;
	}

}
