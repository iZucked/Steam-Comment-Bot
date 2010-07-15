package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * {@link IScheduledEvent} defining a period of idle time at a {@link IPort}
 * 
 * l@author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IIdleEvent<T> extends IScheduledEvent<T> {

	/**
	 * Returns the {@link IPort} at which this idle event occurs.
	 * 
	 * @return
	 */
	IPort getPort();

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
	long getFuelConsumption(FuelComponent fuel);

	/**
	 * Returns the total cost of the fuel consumed per {@link FuelComponent}
	 * during this event.
	 * TODO: What about making this the unit cost?
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelCost(FuelComponent fuel);
}
