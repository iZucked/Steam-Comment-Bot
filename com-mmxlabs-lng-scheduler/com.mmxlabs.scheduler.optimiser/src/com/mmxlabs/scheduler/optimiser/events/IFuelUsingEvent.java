/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import java.util.Collection;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;

/**
 * A common interface for events that use fuel to implement
 * 
 * @author hinton
 * 
 */
public interface IFuelUsingEvent {

	long getStartHeelInM3();

	long getEndHeelInM3();

	Collection<FuelKey> getFuelKeys();

	/**
	 * Returns the fuel consumption per {@link FuelComponent} during this event.
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelConsumption(FuelKey fk);

	/**
	 * Returns the total cost of the fuel consumed per {@link FuelComponent} during this event. TODO: What about making this the unit cost?
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelCost(FuelKey fk);

	int getBaseFuelUnitPrice(IBaseFuel bf);

	boolean isCooldownPerformed();
}
