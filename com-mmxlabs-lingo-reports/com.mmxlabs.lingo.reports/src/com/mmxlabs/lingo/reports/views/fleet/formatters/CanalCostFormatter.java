/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CanalCostFormatter extends CostFormatter {

	public CanalCostFormatter(final Type type) {
		super(type);
	}

	@Override
	public Integer getIntValue(final Object object) {

		return SequenceGrabber.applyToSequences(object, this::calculateCost);
	}

	private int calculateCost(final Sequence sequence) {
		int cost = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof Journey) {
				final Journey journey = (Journey) evt;
				cost += journey.getToll();
			}
		}
		return cost;
	}

}
