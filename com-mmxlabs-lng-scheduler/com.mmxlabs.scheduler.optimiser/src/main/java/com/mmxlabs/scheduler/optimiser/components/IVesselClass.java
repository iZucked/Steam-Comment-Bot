/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

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
	 * Return the upper limit of usable cargo capacity in M3
	 * 
	 * @return
	 */
	long getCargoCapacity();

	/**
	 * Returns a {@link IConsumptionRateCalculator} to calculate required fuel
	 * consumption for the given state at the given speed. The valid range of
	 * input values should be between {@link #getMinSpeed()} and
	 * {@link #getMaxSpeed()} inclusively. It is expected that the rate in MT
	 * per Hour.
	 * 
	 * @param vesselState
	 * @return
	 */
	IConsumptionRateCalculator getConsumptionRate(VesselState vesselState);

	/**
	 * Returns the fuel consumption requirements when the vessel is idle. Units
	 * are MT Per Hour
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleConsumptionRate(VesselState vesselState);

	/**
	 * Returns the rate of NBO when the vessel is idle. Units are M3 Per Hour
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleNBORate(VesselState vesselState);

	/**
	 * Returns the pilot light rate in MT/Hour for vessels which require a pilot
	 * light when running on just LNG. Vessels with no pilot light rate can
	 * return 0.
	 * 
	 * @return
	 */
	long getPilotLightRate();

	/**
	 * Returns the idle pilot light rate in MT/Hour for vessels which require a pilot
	 * light when running on just LNG. Vessels with no pilot light rate can
	 * return 0.
	 * 
	 * @return
	 */
	long getIdlePilotLightRate();

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
	 * Returns the rate of NBO when the vessel is travelling. Units are M3 Per
	 * Hour
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

	/**
	 * Returns the unit price of base fuel for this vessel class, where the unit
	 * is {@link FuelComponent#getDefaultFuelUnit()}
	 * 
	 * @return
	 */
	int getBaseFuelUnitPrice();

	/**
	 * Returns the conversion factor to use to convert 1 {@link FuelUnit#M3} of
	 * LNG to {@link FuelUnit#MT} equivalence.
	 * 
	 * @return
	 */
	int getBaseFuelConversionFactor();

	/**
	 * Returns the hourly charter price for this vessel class, if chartered.
	 * 
	 * @return hourly charter price
	 */
	int getHourlyCharterPrice();

}
