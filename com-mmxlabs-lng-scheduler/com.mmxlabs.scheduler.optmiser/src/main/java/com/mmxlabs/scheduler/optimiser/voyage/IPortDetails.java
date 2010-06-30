package com.mmxlabs.scheduler.optimiser.voyage;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * Interface recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortDetails {

	/**
	 * Returns the {@link IPortSlot} this visit pertains to.
	 * 
	 * @return
	 */
	IPortSlot getPortSlot();

	void setPortSlot(IPortSlot portSlot);

	/**
	 * Returns the duration of the visit
	 * 
	 * @return
	 */
	int getVisitDuration();

	void setVisitDuration(int visitDuration);

	/**
	 * Returns various port costs by key.
	 * 
	 * @return
	 */
	long getPortCost(Object key);

	/**
	 * Returns fuel consumption at the port.
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelConsumption(FuelComponent fuel);

	/**
	 * Set the fuel consumption at the port.
	 * 
	 * @param fuel
	 * @param consumption
	 */
	void setFuelConsumption(FuelComponent fuel, long consumption);

	/**
	 * Return the start time of this port event. The end time is the start time
	 * plus {@link #getVisitDuration()}.
	 * 
	 * @return
	 */
	int getStartTime();

	/**
	 * Set the start time of this port event.
	 * 
	 * @param startTime
	 */
	void setStartTime(int startTime);

}
