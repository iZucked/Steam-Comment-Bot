/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CharterCostFormatter extends CostFormatter {

	public CharterCostFormatter(boolean includeUnits) {
		super(includeUnits);
	}

	public CharterCostFormatter(boolean includeUnits, Type type) {
		super(includeUnits, type);
	}

	@Override
	public Integer getIntValue(Object object) {

		return SequenceGrabber.applyToSequences(object, this::getCost);
	}

	private int getCost(Sequence sequence) {
		int cost = 0;
		for (Event evt : sequence.getEvents()) {
			cost += evt.getCharterCost();
		}
		return cost;
	}

}
