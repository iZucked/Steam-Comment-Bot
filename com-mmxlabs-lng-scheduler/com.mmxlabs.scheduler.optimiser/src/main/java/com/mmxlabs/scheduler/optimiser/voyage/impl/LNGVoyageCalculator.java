/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;

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
	 * {@link VoyageOptions} provides the specific choices to evaluate for this
	 * voyage (e.g. fuel choice, route, ...).
	 * 
	 * @param vessel
	 * @param from
	 * @param to
	 * @param options
	 * @param output
	 */
	@Override
	public final void calculateVoyageFuelRequirements(
			final VoyageOptions options, final VoyageDetails<T> output) {

		output.setOptions(options);

		final IVessel vessel = options.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();
		final VesselState vesselState = options.getVesselState();

		final int equivalenceFactorM3ToMT = vesselClass
				.getBaseFuelConversionFactor();

		// Get distance
		final long distance = options.getDistance();

		// Get available time
		final int availableTimeInHours = options.getAvailableTime();

		// Calculate speed
		// cast to int as if long is required, then what are we doing?
		int speed = availableTimeInHours == 0 ? 0 : Calculator
				.speedFromDistanceTime(distance, availableTimeInHours);

		if (distance != 0) {
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
		}

		// Calculate total, travel and idle time

		// May be longer than available time
		final int travelTimeInHours = speed == 0 ? 0 : Calculator
				.getTimeFromSpeedDistance(speed, distance);
		final int idleTimeInHours = Math.max(0, availableTimeInHours
				- travelTimeInHours);

		output.setTravelTime(travelTimeInHours);
		output.setIdleTime(idleTimeInHours);

		final long consuptionRateInMTPerHour = speed == 0 ? 0 : vesselClass
				.getConsumptionRate(vesselState).getRate(speed);
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(
				consuptionRateInMTPerHour, travelTimeInHours);

		// Calculate fuel requirements
		if (options.useNBOForTravel()) {

			long nboRateInM3PerHour = vesselClass.getNBORate(vesselState);
			final long nboProvidedInM3 = Calculator.quantityFromRateTime(
					nboRateInM3PerHour, travelTimeInHours);

			final long nboProvidedInMT = Calculator.convertM3ToMT(
					nboProvidedInM3, equivalenceFactorM3ToMT);

			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3,
					nboProvidedInM3);
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.MT,
					nboProvidedInMT);

			if (nboProvidedInMT < requiredConsumptionInMT) {
				final long diffInMT = requiredConsumptionInMT - nboProvidedInMT;
				final long diffInM3 = Calculator.convertMTToM3(diffInMT,
						equivalenceFactorM3ToMT);
				if (options.useFBOForSupplement()) {
					// Use FBO for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3,
							diffInM3);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT,
							diffInMT);
					output.setFuelConsumption(FuelComponent.Base_Supplemental,
							FuelUnit.MT, 0);
				} else {
					// Use base for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
					output.setFuelConsumption(FuelComponent.Base_Supplemental,
							FuelUnit.MT, diffInMT);
				}
			}
		} else {
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.MT, 0);
			output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
			output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
			output.setFuelConsumption(FuelComponent.Base, FuelUnit.MT,
					requiredConsumptionInMT);
		}

		final long idleNBORateInM3PerHour = vesselClass
				.getIdleNBORate(vesselState);
		if (options.useNBOForIdle()) {
			final long nboProvidedInM3 = Calculator.quantityFromRateTime(
					idleNBORateInM3PerHour, idleTimeInHours);
			final long nboProvidedInMT = Calculator.convertM3ToMT(
					nboProvidedInM3, equivalenceFactorM3ToMT);

			output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3,
					nboProvidedInM3);
			output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT,
					nboProvidedInMT);
			output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, 0);
		} else {
			final long idleRateInMTPerHour = vesselClass
					.getIdleConsumptionRate(vesselState);

			long idleConsumptionInMT = Calculator.quantityFromRateTime(
					idleRateInMTPerHour, idleTimeInHours);
			if (options.useNBOForTravel()) {

				// Run down boil off after travel. On ballast voyages running on
				// NBO, It is necessary to keep a minimum heel of LNG whilst
				// travelling. Once in port, the heel can boil-off as there is
				// no need to keep it around. Base fuel requirements for idling
				// are much less than that provided by boil-off.

				// There is more boil-off than required consumption. We need to
				// work out how long we could provide energy for based on
				// boil-off time rather than use the raw quantity directly.
				final long minHeelInM3 = vesselClass.getMinHeel();
				final long minHeelInMT = Calculator.convertM3ToMT(minHeelInM3,
						equivalenceFactorM3ToMT);

				// How long will the boil-off last
				final int deltaTimeInHours = Calculator
						.getTimeFromRateQuantity(idleNBORateInM3PerHour,
								minHeelInM3);

				// Get fuel equivalence factor
				final long equivalentConsumptionInMT = Calculator
						.quantityFromRateTime(idleRateInMTPerHour,
								deltaTimeInHours);

				idleConsumptionInMT -= equivalentConsumptionInMT;

				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3,
						minHeelInM3);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT,
						minHeelInMT);
			}

			if (idleConsumptionInMT > 0) {
				// Use base for remaining quantity
				output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT,
						idleConsumptionInMT);
			}
		}

		// TODO: Calculate extras - route specific data etc
	}

	/**
	 * Given a sequence of {@link IPortDetails} interleaved with
	 * {@link VoyageDetails}, compute the total base fuel and LNG requirements,
	 * taking into account initial conditions, load and discharge commitments
	 * etc. It is assumed that the first and last {@link IPortDetails} will be a
	 * Loading slot or other slot where the vessel state is set to a known
	 * state. Intermediate slots are any other type of slot (e.g. one discharge,
	 * multiple waypoints, etc). If the first slot is a load slot, then this is
	 * the only reason we should see a discharge slot.
	 * 
	 * @param sequence
	 * @param dischargeTime
	 * @param loadTime
	 */
	@Override
	public final void calculateVoyagePlan(final VoyagePlan voyagePlan,
			final IVessel vessel, final int[] arrivalTimes,
			final Object... sequence) {

		// Ensure odd number of elements
		assert sequence.length % 2 == 1;

		final IVesselClass vesselClass = vessel.getVesselClass();

		int loadIdx = -1;
		int dischargeIdx = -1;

		final long[] fuelConsumptions = new long[FuelComponent.values().length];

		final int baseFuelPricePerMT = vessel.getVesselClass()
				.getBaseFuelUnitPrice();

		for (int i = 0; i < sequence.length; ++i) {
			if (i % 2 == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getPortSlot();
				if (slot instanceof ILoadSlot) {
					if (i == sequence.length - 1) {
						// End of run, so skip
					} else {
						loadIdx = i;
					}
				} else if (slot instanceof IDischargeSlot) {
					dischargeIdx = i;
				} else {
					// Currently another slot type, no LNG state to pull in as
					// yet.
				}

				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details
							.getFuelConsumption(fc);
				}
			} else {
				// Voyage
				final VoyageDetails<?> details = (VoyageDetails<?>) sequence[i];
				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details
							.getFuelConsumption(fc, fc.getDefaultFuelUnit());
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

				// TODO: Add test case for this information
				details.setFuelUnitPrice(FuelComponent.Base, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.Base_Supplemental,
						baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.IdleBase,
						baseFuelPricePerMT);
			}
		}

		// If load or discharge has been set, then the other must be too.
		assert loadIdx == -1 || dischargeIdx != -1;
		assert dischargeIdx == -1 || loadIdx != -1;

		long loadVolumeInM3 = 0;
		long dischargeVolumeInM3 = 0;

		int loadUnitPrice = 0;
		int dischargeUnitPrice = 0;

		int loadM3Price = 0;
		int dischargeM3Price = 0;

		int cargoCVValue = 0;

		final long lngConsumed;

		// Load/Discharge sequence
		if (loadIdx != -1 && dischargeIdx != -1) {
			final ILoadSlot loadSlot = (ILoadSlot) ((PortDetails) sequence[loadIdx])
					.getPortSlot();
			final IDischargeSlot dischargeSlot = (IDischargeSlot) ((PortDetails) sequence[dischargeIdx])
					.getPortSlot();

			// Store unit prices for later on
			loadUnitPrice = loadSlot
					.getPurchasePriceAtTime(arrivalTimes[loadIdx/2]);
			dischargeUnitPrice = dischargeSlot
					.getSalesPriceAtTime(arrivalTimes[dischargeIdx/2]);

			// Store cargoCVValue
			cargoCVValue = loadSlot.getCargoCVValue();

			dischargeM3Price = (int) Calculator.multiply(dischargeUnitPrice,
					cargoCVValue);
			loadM3Price = (int) Calculator
					.multiply(loadUnitPrice, cargoCVValue);

			lngConsumed = fuelConsumptions[FuelComponent.NBO.ordinal()]
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

			dischargeVolumeInM3 = Math.min(upperLoadLimit - lngConsumed,
					maxDischargeVolume);

			if (dischargeVolumeInM3 < 0) {
				throw new RuntimeException(
						"Capacity violation: discharge volume = "
								+ dischargeVolumeInM3);
			}
			loadVolumeInM3 = dischargeVolumeInM3 + lngConsumed;

			// These should be guaranteed by the Math.min call above
			assert (loadVolumeInM3 <= upperLoadLimit);
			assert (dischargeVolumeInM3 <= maxDischargeVolume);

			// Check the bounds
			if (loadVolumeInM3 < minLoadVolume) {
				// problem
				throw new RuntimeException("Capacity violation");
			}

			if (dischargeVolumeInM3 < minDischargeVolume) {
				// problem
				throw new RuntimeException("Capacity violation");
			}

			// Sanity checks
			assert loadVolumeInM3 <= cargoCapacity;
			assert loadVolumeInM3 <= maxLoadVolume;
			assert loadVolumeInM3 >= minLoadVolume;
			assert dischargeVolumeInM3 <= maxDischargeVolume;
			assert dischargeVolumeInM3 >= minDischargeVolume;

		} else {
			lngConsumed = fuelConsumptions[FuelComponent.NBO.ordinal()]
					+ fuelConsumptions[FuelComponent.FBO.ordinal()]
					+ fuelConsumptions[FuelComponent.IdleNBO.ordinal()];
			if (lngConsumed > 0) {
				throw new RuntimeException("LNG Required, but non loaded");
			}
		}

		// Process details, filling in LNG prices
		// TODO: I don't really like altering the details at this stage of
		// processing, but this is where the information is being processed.
		// Can this be moved into the scheduler? If so, we need to ensure the
		// same price is used in all valid voyage legs.
		if (dischargeIdx != -1) {

			// assert FuelComponent.NBO.getDefaultFuelUnit() == FuelUnit.M3;
			// assert FuelComponent.FBO.getDefaultFuelUnit() == FuelUnit.M3;
			// assert FuelComponent.IdleNBO.getDefaultFuelUnit() == FuelUnit.M3;
			for (int i = 0; i < sequence.length; ++i) {
				if (i % 2 == 1) {
					assert sequence[i] instanceof VoyageDetails;

					final VoyageDetails<?> details = (VoyageDetails<?>) sequence[i];
					details.setFuelUnitPrice(FuelComponent.NBO,
							dischargeM3Price);
					details.setFuelUnitPrice(FuelComponent.FBO,
							dischargeM3Price);
					details.setFuelUnitPrice(FuelComponent.IdleNBO,
							dischargeM3Price);
				} else {
					assert sequence[i] instanceof PortDetails;
				}
			}
		}

		// Store results in plan
		voyagePlan.setSequence(sequence);

		// We can just loop over each component here for consumptions ....
		for (final FuelComponent fc : FuelComponent.values()) {
			final long consumption = fuelConsumptions[fc.ordinal()];
			voyagePlan.setFuelConsumption(fc, consumption);
		}
		// .. but costs need to be calculated differently
		voyagePlan.setTotalFuelCost(FuelComponent.Base, Calculator
				.costFromConsumption(
						fuelConsumptions[FuelComponent.Base.ordinal()],
						baseFuelPricePerMT));
		voyagePlan.setTotalFuelCost(FuelComponent.Base_Supplemental, Calculator
				.costFromConsumption(
						fuelConsumptions[FuelComponent.Base_Supplemental
								.ordinal()], baseFuelPricePerMT));
		voyagePlan.setTotalFuelCost(FuelComponent.IdleBase, Calculator
				.costFromConsumption(
						fuelConsumptions[FuelComponent.IdleBase.ordinal()],
						baseFuelPricePerMT));

		/**
		 * The opportunity cost of burning a unit of LNG for fuel;
		 * it doesn't cost 1 discharge unit to burn some fuel, it costs
		 * whatever sales opportunity we lost on this leg.
		 */
		final int lngM3OpportunityCost = dischargeM3Price - loadM3Price;
		
		voyagePlan.setTotalFuelCost(FuelComponent.NBO, Calculator
				.costFromConsumption(
						fuelConsumptions[FuelComponent.NBO.ordinal()],
						lngM3OpportunityCost));
		voyagePlan.setTotalFuelCost(FuelComponent.FBO, Calculator
				.costFromConsumption(
						fuelConsumptions[FuelComponent.FBO.ordinal()],
						lngM3OpportunityCost));
		voyagePlan.setTotalFuelCost(FuelComponent.IdleNBO, Calculator
				.costFromConsumption(
						fuelConsumptions[FuelComponent.IdleNBO.ordinal()],
						lngM3OpportunityCost));

		voyagePlan.setLNGFuelVolume(lngConsumed);

		voyagePlan.setLoadVolume(loadVolumeInM3);
		long purchaseCost = Calculator.multiply(loadM3Price, loadVolumeInM3);
		voyagePlan.setPurchaseCost(purchaseCost);

		voyagePlan.setDischargeVolume(dischargeVolumeInM3);
		long salesRevenue = Calculator.multiply(dischargeM3Price,
				dischargeVolumeInM3);
		voyagePlan.setSalesRevenue(salesRevenue);
	}
}
