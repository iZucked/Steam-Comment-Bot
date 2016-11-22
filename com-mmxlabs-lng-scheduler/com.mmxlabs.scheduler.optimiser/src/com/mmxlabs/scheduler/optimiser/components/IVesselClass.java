/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Interface generic vessel parameters shared between multiple {@link IVessel} of the same underlying type.
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
	 * Returns a {@link IConsumptionRateCalculator} to calculate required fuel consumption for the given state at the given speed. The valid range of input values should be between
	 * {@link #getMinSpeed()} and {@link #getMaxSpeed()} inclusively. It is expected that the rate in MT per Day.
	 * 
	 * @param vesselState
	 * @return
	 */
	IConsumptionRateCalculator getConsumptionRate(VesselState vesselState);

	/**
	 * Returns the fuel consumption requirements when the vessel is idle. Units are MT Per Day
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleConsumptionRate(VesselState vesselState);

	/**
	 * Returns the fuel consumption requirements when the vessel is in port. Units are MT Per Day
	 * 
	 * @param portType
	 * @return
	 */

	long getInPortConsumptionRateInMTPerDay(PortType portType);

	/**
	 * Returns the rate of NBO when the vessel is idle. Units are M3 Per Day
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleNBORate(VesselState vesselState);

	/**
	 * Returns the pilot light rate in MT/Day for vessels which require a pilot light when running on just LNG. Vessels with no pilot light rate can return 0.
	 * 
	 * @return
	 */
	long getPilotLightRate();

	/**
	 * Returns the idle pilot light rate in MT/Day for vessels which require a pilot light when running on just LNG. Vessels with no pilot light rate can return 0.
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
	 * Return the minimum volume of heel that need to be retained on voyages that need to retain a heel.
	 * 
	 * @return
	 */
	long getSafetyHeel();

	/**
	 * Returns the slowest usable speed of this vessel.
	 * 
	 * @return
	 */
	int getMinSpeed();

	/**
	 * Returns the vessels "service" speed. Used in e.g. P&L based calculations
	 * 
	 * @return
	 */
	int getServiceSpeed(VesselState vesselState);

	/**
	 * Returns the rate of NBO when the vessel is travelling. Units are M3 Per Day
	 * 
	 * @param vesselState
	 * @return
	 */
	long getNBORate(VesselState vesselState);

	/**
	 * The time in hours for which these vessels can idle with no heel without the tanks becoming warm and thus requiring a cooldown.
	 * 
	 * @return the time to warm up
	 */
	int getWarmupTime();

	/**
	 * The volume of LNG in M3 (scaled, see {@link Calculator#ScaleFactor}) required to cool down the tanks if they have warmed up.
	 * 
	 * In a future version this API may take more parameters, for example an estimate of the tanks' temperature, or the time spent warming up.
	 * 
	 * @return scaled M3 of LNG
	 */
	long getCooldownVolume();

	/**
	 * MT/day of base fuel required as a *minimum* consumption for all events (port, travel & idle).
	 * 
	 * @return
	 */
	int getMinBaseFuelConsumptionInMTPerDay();

	/**
	 * Get the base fuel used by this vessel class.
	 * 
	 * @return
	 */
	IBaseFuel getBaseFuel();

	/**
	 * Set the base fuel used by this vessel class.
	 * 
	 * @return
	 */
	void setBaseFuel(IBaseFuel baseFuel);
	
	/**
	 * Returns the rate of NBO when the vessel is in port. Units are M3 Per Day
	 * 
	 * @param vesselState
	 * @return
	 */
	long getInPortNBORate(VesselState vesselState);
	
	
	

	boolean hasReliqCapability();
}
