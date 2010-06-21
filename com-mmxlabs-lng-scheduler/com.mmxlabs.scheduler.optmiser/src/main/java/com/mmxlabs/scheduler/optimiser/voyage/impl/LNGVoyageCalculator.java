package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;

public class LNGVoyageCalculator<T> implements ILNGVoyageCalculator<T> {

	/**
	 * Calculate the fuel requirements between a pair of {@link IPortSlot}s. The
	 * {@link IVoyageOptions} provides the specific choices to evaluate for this
	 * voyage (e.g. fuel choice, route, ...).
	 * 
	 * @param vessel
	 * @param from
	 * @param to
	 * @param options
	 * @param output
	 */
	@Override
	public void calculateVoyageFuelRequirements(final IVoyageOptions options,
			final IVoyageDetails<T> output) {

		output.setOptions(options);

		final IVessel vessel = options.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();
		final VesselState vesselState = options.getVesselState();

		// Get distance
		final long distance = options.getDistance();

		// Get available time
		final long availableTime = options.getAvailableTime();

		// Calculate speed
		// cast to int as if long is required, then what are we doing?
		int speed = (int) Calculator.speedFromDistanceTime(distance,
				availableTime);

		// Check NBO speed
		if (options.useNBOForTravel()) {
			final int nboSpeed = options.getNBOSpeed();
			if (speed < nboSpeed) {
				speed = nboSpeed;
			}
		}

		// Clamp speed to min bound
		final int minSpeed = vesselClass.getMinSpeed();
		if (speed < minSpeed) {
			speed = minSpeed;
		}

		// Clamp speed to max bound
		final int maxSpeed = vesselClass.getMaxSpeed();
		if (speed > maxSpeed) {
			speed = maxSpeed;
		}
		output.setSpeed(speed);

		// Calculate total, travel and idle time

		// May be longer than available time
		final long travelTime = Calculator.getTimeFromSpeedDistance(speed,
				distance);
		final long idleTime = Math.max(0, availableTime - travelTime);

		output.setTravelTime(travelTime);
		output.setIdleTime(idleTime);

		final long consuptionRate = vesselClass.getConsumptionRate(vesselState)
				.getRate(speed);
		final long requiredConsumption = Calculator.quantityFromRateTime(
				consuptionRate, travelTime);

		// Calculate fuel requirements
		if (options.useNBOForTravel()) {

			final long nboProvided = Calculator.quantityFromRateTime(
					vesselClass.getNBORate(vesselState), travelTime);

			output.setFuelConsumption(FuelComponent.NBO, nboProvided);

			if (nboProvided < requiredConsumption) {
				final long diff = requiredConsumption - nboProvided;
				if (options.useFBOForSupplement()) {
					// Use FBO for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, diff);
					output.setFuelConsumption(FuelComponent.Base_Supplemental, 0);
				} else {
					// Use base for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, 0);
					output.setFuelConsumption(FuelComponent.Base_Supplemental, diff);
				}
			}
		} else {
			output.setFuelConsumption(FuelComponent.NBO, 0);
			output.setFuelConsumption(FuelComponent.FBO, 0);
			output.setFuelConsumption(FuelComponent.Base, requiredConsumption);
		}

		long idleNBORate = vesselClass.getIdleNBORate(vesselState);
		if (options.useNBOForIdle()) {
			final long nboProvided = Calculator.quantityFromRateTime(
					idleNBORate, idleTime);

			output.setFuelConsumption(FuelComponent.IdleNBO, nboProvided);
			output.setFuelConsumption(FuelComponent.IdleBase, 0);
		} else {
			final long idleRate = vesselClass
					.getIdleConsumptionRate(vesselState);
			long idleConsumption = Calculator.quantityFromRateTime(idleRate,
					idleTime);
			if (options.useNBOForTravel()) {

				// Run down boil off after travel. On ballast voyages running on
				// NBO, It is necessary to keep a minimum heel of LNG whilst
				// travelling. Once in port, the heel can boil-off as there is
				// no need to keep it around. Base fuel requirements for idling
				// are much less than that provided by boil-off.

				// There is more boil-off than required consumption. We need to
				// work out how long we could provide energy for based on
				// boil-off time rather than use the raw quantity directly.
				final long minHeel = vesselClass.getMinHeel();
				int deltaTime = Calculator.getTimeFromRateQuantity(idleNBORate,
						minHeel);
				long equivalentConsumption = Calculator.quantityFromRateTime(
						idleRate, deltaTime);

				idleConsumption -= equivalentConsumption;
				output.setFuelConsumption(FuelComponent.IdleNBO, minHeel);
			}
			if (idleConsumption > 0) {
				// Use base for remaining quantity
				output.setFuelConsumption(FuelComponent.IdleBase,
						idleConsumption);
			}
		}

		// TODO: Calculate extras - route specific costs etc
	}
}
