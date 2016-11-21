/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * A common interface for events that use fuel to implement
 * 
 * @author hinton
 * 
 */
public interface IFuelUsingEvent {

	long getStartHeelInM3();

	long getEndHeelInM3();

	/**
	 * Returns the fuel consumption per {@link FuelComponent} during this event.
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelConsumption(FuelComponent fuel, FuelUnit unit);

	/**
	 * Returns the total cost of the fuel consumed per {@link FuelComponent} during this event. TODO: What about making this the unit cost?
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelCost(FuelComponent fuel);

	/**
	 * Return the unit price for this fuel component. See also {@link #getFuelPriceUnit(FuelComponent)}
	 * 
	 * @param fuel
	 * @return
	 */
	int getFuelUnitPrice(FuelComponent fuel);

	/**
	 * Returns the {@link FuelUnit} used for the price per unit. See {@link #getFuelUnitPrice(FuelComponent)}.
	 * 
	 * @param fuel
	 * @return
	 */
	FuelUnit getFuelPriceUnit(FuelComponent fuel);

	boolean isCooldownPerformed();
}
