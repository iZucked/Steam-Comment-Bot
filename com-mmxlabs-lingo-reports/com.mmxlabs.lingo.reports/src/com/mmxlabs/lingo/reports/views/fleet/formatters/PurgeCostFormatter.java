/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Sequence;

public class PurgeCostFormatter extends CostFormatter {

	public PurgeCostFormatter(final boolean includeUnits) {
		super(includeUnits, CostFormatter.Type.COST);
	}

	@Override
	public Integer getIntValue(final Object object) {

		return SequenceGrabber.applyToSequences(object, this::getCost);
	}

	private int getCost(final Sequence sequence) {
		int cost = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof Purge) {
				final Purge purge = (Purge) evt;
				cost += purge.getCost();
			}
		}
		return cost;
	}

}
