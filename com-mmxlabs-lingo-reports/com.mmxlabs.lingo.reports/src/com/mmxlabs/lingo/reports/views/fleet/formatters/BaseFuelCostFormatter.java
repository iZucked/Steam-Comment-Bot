/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

public class BaseFuelCostFormatter extends IntegerFormatter {

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
			if (evt instanceof FuelUsage) {
				FuelUsage fuelUsage = (FuelUsage) evt;
				cost += getFuelCost(Fuel.BASE_FUEL, fuelUsage);
				cost += getFuelCost(Fuel.PILOT_LIGHT, fuelUsage);
			}
		}
		return cost;
	}

	private int getFuelCost(Fuel fuel, FuelUsage fuelUsage) {
		int cost = 0;
		for (FuelQuantity fuelQuantity : fuelUsage.getFuels()) {
			if (fuelQuantity.getFuel() == fuel) {
				cost += fuelQuantity.getCost();
			}
		}
		return cost;
	}

}
