package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

/**
 * Implementation of {@link ILNGVoyageCalculator}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class LNGVoyageCalculator<T> implements ILNGVoyageCalculator<T> {

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
		final int availableTime = options.getAvailableTime();

		// Calculate speed
		// cast to int as if long is required, then what are we doing?
		int speed = availableTime == 0 ? 0 : Calculator.speedFromDistanceTime(distance,
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

		assert minSpeed <= maxSpeed;
		assert speed != 0;
		
		// Calculate total, travel and idle time

		// May be longer than available time
		final int travelTime = Calculator.getTimeFromSpeedDistance(speed,
				distance);
		final int idleTime = Math.max(0, availableTime - travelTime);

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
					output.setFuelConsumption(FuelComponent.Base_Supplemental,
							0);
				} else {
					// Use base for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, 0);
					output.setFuelConsumption(FuelComponent.Base_Supplemental,
							diff);
				}
			}
		} else {
			output.setFuelConsumption(FuelComponent.NBO, 0);
			output.setFuelConsumption(FuelComponent.FBO, 0);
			output.setFuelConsumption(FuelComponent.Base, requiredConsumption);
		}

		final long idleNBORate = vesselClass.getIdleNBORate(vesselState);
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
				final int deltaTime = Calculator.getTimeFromRateQuantity(
						idleNBORate, minHeel);
				final long equivalentConsumption = Calculator
						.quantityFromRateTime(idleRate, deltaTime);

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

	/**
	 * Given a sequence of {@link IPortDetails} interleaved with
	 * {@link IVoyageDetails}, compute the total base fuel and LNG requirements,
	 * taking into account initial conditions, load and discharge commitments
	 * etc. It is assumed that the first and last {@link IPortDetails} will be a
	 * Loading slot or other slot where the vessel state is set to a known
	 * state. Intermediate slots are any other type of slot (e.g. one discharge,
	 * multiple waypoints, etc). If the first slot is a load slot, then this is
	 * the only reason we should see a discharge slot.
	 * 
	 * @param sequence
	 */
	@Override
	public void calculateVoyagePlan(final IVoyagePlan voyagePlan,
			final IVessel vessel, final Object... sequence) {

		// Ensure odd number of elements
		assert sequence.length % 2 == 1;

		final IVesselClass vesselClass = vessel.getVesselClass();

		int loadIdx = -1;
		int dischargeIdx = -1;

		final long[] fuelConsumptions = new long[FuelComponent.values().length];

		for (int i = 0; i < sequence.length; ++i) {
			if (i % 2 == 0) {
				// Port Slot
				final IPortDetails details = (IPortDetails) sequence[i];
				final IPortSlot slot = details.getPortSlot();
				if (slot instanceof ILoadSlot) {
					loadIdx = i;
				} else if (slot instanceof IDischargeSlot) {
					dischargeIdx = i;
				} else {
					// Currently another slot type, no LNG state to pull in as
					// yet.
				}
				
				// TODO: add up costs/consumptions
				
				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details
							.getFuelConsumption(fc);
				}
				
			} else {
				// Voyage
				final IVoyageDetails<?> details = (IVoyageDetails<?>) sequence[i];
				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details
							.getFuelConsumption(fc);
				}

				// TODO: Assert that if discharge.heelOut set, then future
				// voyages have no LNG
				/*
				 * assert dischargeIdx == -1 ||
				 * ((IDischargeSlot)sequence[dischargeIdx]).getHeelOut() ==
				 * false || (details.getFuelConsumption(FuelComponent.NBO) == 0
				 * && details .getFuelConsumption(FuelComponent.FBO) == 0 &&
				 * details .getFuelConsumption(FuelComponent.IdleNBO) == 0);
				 */

				// Assert Laden/Ballast state switches after load and discharge
				assert (loadIdx == -1 && dischargeIdx == -1 && details
						.getOptions().getVesselState() == VesselState.Ballast)
						|| (loadIdx > -1 && dischargeIdx == -1 && details
								.getOptions().getVesselState() == VesselState.Laden)
						|| (loadIdx > -1 && dischargeIdx > -1 && details
								.getOptions().getVesselState() == VesselState.Ballast);
			}
		}

		// If load or discharge has been set, then the other must be too.
		assert loadIdx == -1 || dischargeIdx != -1;
		assert dischargeIdx == -1 || loadIdx != -1;

		long loadVolume = 0;
		long dischargeVolume = 0;

		// Load/Discharge sequence
		if (loadIdx != -1 && dischargeIdx != -1) {
			final ILoadSlot loadSlot = (ILoadSlot)((IPortDetails) sequence[loadIdx]).getPortSlot();
			final IDischargeSlot dischargeSlot = (IDischargeSlot)((IPortDetails) sequence[dischargeIdx]).getPortSlot();

			final long lngConsumed = fuelConsumptions[FuelComponent.NBO
					.ordinal()]
					+ fuelConsumptions[FuelComponent.FBO.ordinal()]
					+ fuelConsumptions[FuelComponent.IdleNBO.ordinal()];

			long cargoCapacity = vesselClass.getCargoCapacity();

			long minLoadVolume = loadSlot.getMinLoadVolume();
			long maxLoadVolume = loadSlot.getMaxLoadVolume();
			long minDischargeVolume = dischargeSlot.getMinDischargeVolume();
			long maxDischargeVolume = dischargeSlot.getMaxDischargeVolume();

			// We cannot load more than is available or which would exceed
			// vessel capacity.
			long upperLoadLimit = Math.min(cargoCapacity, maxLoadVolume);

			// Now find the amount of leeway we have between limits. By
			// subtracting lngConsumed from the load limits, we bring the load
			// and discharge limits into the same range. We can then find the
			// highest intersecting point as the additional amount of lng we can
			// load to max out load and discharge. This is the sales quantity.
			
			dischargeVolume = Math.min(upperLoadLimit - lngConsumed, maxDischargeVolume);
			
			
			if (dischargeVolume < 0 ) {
				throw new RuntimeException("Capacity violation");
			}
			loadVolume = dischargeVolume + lngConsumed;
			
			// These should be guaranteed by the Math.min call above
			assert (loadVolume <=  upperLoadLimit);
			assert (dischargeVolume <=  maxDischargeVolume);

			// Check the bounds
			if (loadVolume < minLoadVolume) {
				//problem
				throw new RuntimeException("Capacity violation");
			}
			
			if (dischargeVolume < minDischargeVolume) {
				//problem
				throw new RuntimeException("Capacity violation");
			}
			
			// Sanity checks
			assert loadVolume <= cargoCapacity;
			assert loadVolume <= maxLoadVolume;
			assert loadVolume >= minLoadVolume;
			assert dischargeVolume <= maxDischargeVolume;
			assert dischargeVolume >= minDischargeVolume;

		} else {
			final long lngConsumed = fuelConsumptions[FuelComponent.NBO
					.ordinal()]
					+ fuelConsumptions[FuelComponent.FBO.ordinal()]
					+ fuelConsumptions[FuelComponent.IdleNBO.ordinal()];
			if (lngConsumed > 0) {
				throw new RuntimeException("LNG Required, but non loaded");
			}
		}

		// Store results in plan
		voyagePlan.setSequence(sequence);

		for (final FuelComponent fc : FuelComponent.values()) {
			final long consumption = fuelConsumptions[fc.ordinal()];
			voyagePlan.setFuelConsumption(fc, consumption);

			final long cost = consumption * 1;
			voyagePlan.setFuelCost(fc, cost);
		}

		voyagePlan.setLoadVolume(loadVolume);
		voyagePlan.setPurchaseCost(loadVolume * 1);

		voyagePlan.setDischargeVolume(dischargeVolume);
		voyagePlan.setSalesRevenue(dischargeVolume * 1);
	}
}
