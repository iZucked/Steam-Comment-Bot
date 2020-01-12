/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CooldownCostFormatter extends CostFormatter {

	public CooldownCostFormatter(final boolean includeUnits) {
		super(includeUnits, CostFormatter.Type.COST);
	}

	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int cost = 0;
		if (object instanceof Sequence) {
			final Sequence sequence = (Sequence) object;
			cost += getCost(sequence);
		} else if (object instanceof List) {
			final List objects = (List) object;
			if (objects.size() > 0) {
				for (final Object o : objects) {
					if (o instanceof Sequence) {
						cost += getCost((Sequence) o);
					}
				}
			}
		}

		return cost;
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
