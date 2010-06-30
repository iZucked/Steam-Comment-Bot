package com.mmxlabs.scheduler.optimiser.voyage;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * Data structure populated by a {@link ILNGVoyageCalculator} when evaluating a
 * {@link IPortSlot} to {@link IPortSlot} voyage.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IVoyageDetails<T> {

	/**
	 * The {@link IVoyageOptions} used to calculate these details.
	 * 
	 * @return
	 */
	IVoyageOptions getOptions();

	/**
	 * Set the {@link IVoyageOptions} used to populate the details here
	 * 
	 * @param options
	 */
	void setOptions(IVoyageOptions options);

	/**
	 * Set the amount of time spent travelling between ports
	 * 
	 * @param travelTime
	 */
	void setTravelTime(int travelTime);

	/**
	 * Returns the amount of time spent travelling between ports
	 * 
	 * @return
	 */
	int getTravelTime();

	/**
	 * Set the amount of time spent idling outside of a port
	 * 
	 * @param idleTime
	 */
	void setIdleTime(int idleTime);

	/**
	 * Returns the amount of time spent idling outside of a port
	 * 
	 * @return
	 */
	int getIdleTime();

	/**
	 * Set the speed used to travel between ports.
	 * 
	 * @param speed
	 */
	void setSpeed(int speed);

	/**
	 * Returns the speed used to travel between ports.
	 * 
	 * @return
	 */
	int getSpeed();

	/**
	 * Set the amount of fuel consumed for the given {@link FuelComponent}
	 * 
	 * @param fuel
	 * @param consumption
	 */
	void setFuelConsumption(FuelComponent fuel, long consumption);

	/**
	 * Returns the amount of fuel consumed for the given {@link FuelComponent}
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelConsumption(FuelComponent fuel);

	/**
	 * Returns the start time of this voyage. The end time is the start time
	 * plus the result of {@link #getTravelTime()} and {@link #getIdleTime()}
	 * 
	 * @return
	 */
	int getStartTime();

	/**
	 * Sets the start time of this voyage.
	 * 
	 * @param startTime
	 */
	void setStartTime(int startTime);

}
