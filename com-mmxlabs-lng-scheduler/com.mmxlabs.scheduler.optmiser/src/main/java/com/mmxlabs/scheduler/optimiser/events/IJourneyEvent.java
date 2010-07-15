package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * {@link IScheduledEvent} defining a journey or travel between ports.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IJourneyEvent<T> extends IScheduledEvent<T> {

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
	long getFuelConsumption(FuelComponent fuel);

	/**
	 * Returns the total cost of the fuel consumed per {@link FuelComponent}
	 * during this event. TODO: What about making this the unit cost?
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelCost(FuelComponent fuel);
}
