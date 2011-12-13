/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * {@link IScheduledEvent} defining a journey or travel between ports.
 * 
 * @author Simon Goodall
 * 
 */
public interface IJourneyEvent extends IScheduledEvent {

	/**
	 * Returns the originating {@link IPort} for this journey.
	 * 
	 * @return
	 */
	IPort getFromPort();

	/**
	 * Returns the destination {@link IPort} for this journey.
	 * 
	 * @return
	 */
	IPort getToPort();

	/**
	 * Returns the distance of this journey.
	 * 
	 * @return
	 */
	int getDistance();

	/**
	 * Returns the speed at which the vessel was travelling during this event.
	 * 
	 * @return
	 */
	int getSpeed();

	/**
	 * Returns the state of the vessel during this event.
	 * 
	 * @return
	 */
	VesselState getVesselState();

	/**
	 * Returns the fuel consumption per {@link FuelComponent} during this event.
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelConsumption(FuelComponent fuel, FuelUnit unit);

	/**
	 * Returns the total cost of the fuel consumed per {@link FuelComponent}
	 * during this event. TODO: What about making this the unit cost?
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelCost(FuelComponent fuel);

	/**
	 * Returns the route undertaken in this journey.
	 * 
	 * @return
	 */
	String getRoute();
	
	/**
	 * Returns the cost of the route undertaken in this journey
	 * @return
	 */
	long getRouteCost();
}
