package com.mmxlabs.scheduler.optimiser.components;

/**
 * Interface generic vessel parameters shared between multiple {@link IVessel}
 * of the same underlying type.
 * 
 * @author Simon Goodall
 * 
 */
public interface IVesselClass {

	/**
	 * Return name of vessel class.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Return the upper limit of usable cargo capacity.
	 * 
	 * @return
	 */
	long getCargoCapacity();

	/**
	 * Returns a {@link IConsumptionRateCalculator} to calculate required fuel
	 * consumption for the given state at the given speed. The valid range of
	 * input values should be between {@link #getMinSpeed()} and
	 * {@link #getMaxSpeed()} inclusively.
	 * 
	 * @param vesselState
	 * @return
	 */
	IConsumptionRateCalculator getConsumptionRate(VesselState vesselState);

	/**
	 * Returns the fuel consumption requirements when the vessel is idle.
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleConsumptionRate(VesselState vesselState);

	/**
	 * Returns the rate of NBO when the vessel is idle.
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleNBORate(VesselState vesselState);

	/**
	 * Returns the fastest usable speed of this vessel.
	 * 
	 * @return
	 */
	int getMaxSpeed();

	/**
	 * Return the minimum volume of heel that need to be retained on voyages
	 * that need to retain a heel.
	 * 
	 * @return
	 */
	long getMinHeel();

	/**
	 * Returns the speed the vessel must travel at to use up all NBO.
	 * 
	 * @return
	 */
	int getMinNBOSpeed(VesselState vesselState);

	/**
	 * Returns the slowest usable speed of this vessel.
	 * 
	 * @return
	 */
	int getMinSpeed();

	/**
	 * Returns the rate of NBO when the vessel is travelling.
	 * 
	 * @param vesselState
	 * @return
	 */
	long getNBORate(VesselState vesselState);

	/**
	 * Returns the minimum speed to travel at when using NBO. This will
	 * typically be equivalent to the speed required to use up all NBO as fuel.
	 * 
	 * @param state
	 * @return
	 */
	int getNBOSpeed(VesselState state);

	/**
	 * Set the minimum speed to travel at when using NBO. This will typically be
	 * equivalent to the speed required to use up all NBO as fuel.
	 * 
	 * @param vesselState
	 * @param nboSpeed
	 */
	void setNBOSpeed(VesselState vesselState, int nboSpeed);
}
