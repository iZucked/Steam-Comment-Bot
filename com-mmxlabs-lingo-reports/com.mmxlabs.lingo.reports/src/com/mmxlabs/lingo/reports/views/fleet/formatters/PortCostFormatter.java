/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;

public class PortCostFormatter extends CostFormatter {

	public PortCostFormatter(final Type type) {
		super(type);
	}

	@Override
	public Integer getIntValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getCost);
	}

	private int getCost(final Sequence sequence) {
		int cost = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof PortVisit) {
				final PortVisit portVisit = (PortVisit) evt;
				cost += portVisit.getPortCost();
			}
		}
		return cost;
	}
}
