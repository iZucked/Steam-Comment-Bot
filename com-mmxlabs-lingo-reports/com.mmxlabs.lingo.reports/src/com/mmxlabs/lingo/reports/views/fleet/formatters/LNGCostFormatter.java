/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Sequence;

public class LNGCostFormatter extends IntegerFormatter {

	@Override
	public Integer getIntValue(Object object) {
		int cost = 0;
		if (object instanceof Row) {
			Row row = ((Row) object);
			if (row.getLinkedSequences().size() > 0) {
				for (Sequence s : row.getLinkedSequences()) {
					cost += getCost(s);
				}
			} else {
				if (row.getSequence() != null) {
					cost += getCost(row.getSequence());
				}
			}
		} else if (object instanceof Sequence) {
			final Sequence sequence = (Sequence) object;
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

	private int getCost(final Sequence sequence) {
		int cost = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) evt;
				cost += getFuelCost(Fuel.NBO, fuelUsage);
				cost += getFuelCost(Fuel.FBO, fuelUsage);
			}
		}
		return cost;
	}

	private int getFuelCost(final Fuel fuel, final FuelUsage fuelUsage) {
		int cost = 0;
		for (final FuelQuantity fuelQuantity : fuelUsage.getFuels()) {
			if (fuelQuantity.getFuel() == fuel) {
				cost += fuelQuantity.getCost();
			}
		}
		return cost;
	}

}
