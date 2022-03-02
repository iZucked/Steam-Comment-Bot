/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;

/**
 * A {@link IVessel} contains attributes specific to a vessel.
 * 
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public interface IVessel {

	/**
	 * Returns the name of the vessel.
	 * 
	 * @return
	 */
	@NonNull
	String getName();

	/**
	 * Return the upper limit of usable cargo capacity in M3 (raw capacity * ship
	 * fill)
	 * 
	 * @return
	 */
	long getCargoCapacity();

	/**
	 * Returns a {@link IConsumptionRateCalculator} to calculate required fuel
	 * consumption for the given state at the given speed. The valid range of input
	 * values should be between {@link #getMinSpeed()} and {@link #getMaxSpeed()}
	 * inclusively. It is expected that the rate in MT per Day.
	 * 
	 * @param vesselState
	 * @return
	 */
	IConsumptionRateCalculator getConsumptionRate(VesselState vesselState);

	/**
	 * Returns the fuel consumption requirements when the vessel is idle. Units are
	 * MT Per Day
	 * 
	 * @param vesselState
	 * @return
	 */
	long getIdleConsumptionRate(VesselState vesselState);

	/**
	 * Returns the fuel consumption requirements when the vessel is in port. Units
	 * are MT Per Day
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
	 * Returns the pilot light rate in MT/Day for vessels which require a pilot
	 * light when running on just LNG. Vessels with no pilot light rate can return
	 * 0.
	 * 
	 * @return
	 */
	long getPilotLightRate();

	/**
	 * Returns the fastest usable speed of this vessel.
	 * 
	 * @return
	 */
	int getMaxSpeed();

	/**
	 * Return the minimum volume of heel that need to be retained on voyages that
	 * need to retain a heel.
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
	 * The time in hours for which these vessels can idle with no heel without the
	 * tanks becoming warm and thus requiring a cooldown.
	 * 
	 * @return the time to warm up
	 */
	int getWarmupTime();

	/**
	 * The time in hours needed to purge tanks.
	 */
	int getPurgeTime();

	/**
	 * The volume of LNG in M3 (scaled, see {@link Calculator#ScaleFactor}) required
	 * to cool down the tanks if they have warmed up.
	 * 
	 * In a future version this API may take more parameters, for example an
	 * estimate of the tanks' temperature, or the time spent warming up.
	 * 
	 * @return scaled M3 of LNG
	 */
	long getCooldownVolume();

	/**
	 * MT/day of base fuel required as a *minimum* consumption for all events (port,
	 * travel & idle).
	 * 
	 * @return
	 */
	int getMinBaseFuelConsumptionInMTPerDay();

	/**
	 * Get the base fuel used by this vessel class.
	 * 
	 * @return
	 */
	IBaseFuel getTravelBaseFuel();

	/**
	 * Set the base fuel used by this vessel class.
	 * 
	 * @return
	 */
	void setTravelBaseFuel(IBaseFuel baseFuel);

	/**
	 * Get the in port base fuel used by this vessel class.
	 * 
	 * @return
	 */
	IBaseFuel getInPortBaseFuel();

	/**
	 * Set the in port base fuel used by this vessel class.
	 * 
	 * @return
	 */
	void setInPortBaseFuel(IBaseFuel baseFuel);

	/**
	 * Get the pilot light base fuel used by this vessel class.
	 * 
	 * @return
	 */
	IBaseFuel getPilotLightBaseFuel();

	/**
	 * Set the pilot light base fuel used by this vessel class.
	 * 
	 * @return
	 */
	void setPilotLightBaseFuel(IBaseFuel baseFuel);

	/**
	 * Get the idle base fuel used by this vessel class.
	 * 
	 * @return
	 */
	IBaseFuel getIdleBaseFuel();

	/**
	 * Set the idle base fuel used by this vessel class.
	 * 
	 * @return
	 */
	void setIdleBaseFuel(IBaseFuel baseFuel);

	/**
	 * Returns the rate of NBO when the vessel is in port. Units are M3 Per Day
	 * 
	 * @param vesselState
	 * @return
	 */
	long getInPortNBORate(VesselState vesselState);

	boolean hasReliqCapability();

	FuelKey getTravelBaseFuelInMT();

	FuelKey getSupplementalTravelBaseFuelInMT();

	FuelKey getIdleBaseFuelInMT();

	FuelKey getPilotLightFuelInMT();

	FuelKey getIdlePilotLightFuelInMT();

	FuelKey getInPortBaseFuelInMT();

	Collection<FuelKey> getPortFuelKeys();

	Collection<FuelKey> getTravelFuelKeys();

	Collection<FuelKey> getIdleFuelKeys();

	Collection<FuelKey> getVoyageFuelKeys();

	Collection<FuelKey> getAllFuelKeys();
}
