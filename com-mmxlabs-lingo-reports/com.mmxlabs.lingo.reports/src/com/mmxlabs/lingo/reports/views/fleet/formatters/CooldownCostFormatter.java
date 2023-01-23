/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CooldownCostFormatter extends CostFormatter {

	public CooldownCostFormatter(final boolean includeUnits) {
		super(includeUnits, CostFormatter.Type.COST);
	}

	@Override
	public Integer getIntValue(Object object) {
		return SequenceGrabber.applyToSequences(object, this::getCost);
	}

	private int getCost(final Sequence sequence) {
		int cost = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) evt;
				cost += cooldown.getCost();
			}
		}
		return cost;
	}

}
