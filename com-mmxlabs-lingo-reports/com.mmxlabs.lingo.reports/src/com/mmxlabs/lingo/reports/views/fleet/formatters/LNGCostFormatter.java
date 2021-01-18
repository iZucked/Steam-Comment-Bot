/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Sequence;

public class LNGCostFormatter extends CostFormatter {

	public LNGCostFormatter(final Type type) {
		super(type);
	}

	@Override
	public Integer getIntValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getCost);
	}

	private int getCost(final Sequence sequence) {

		int cost = 0;
		if (sequence != null) {
			for (final Event evt : sequence.getEvents()) {
				if (evt instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) evt;
					cost += getFuelCost(Fuel.NBO, fuelUsage);
					cost += getFuelCost(Fuel.FBO, fuelUsage);
				}
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
