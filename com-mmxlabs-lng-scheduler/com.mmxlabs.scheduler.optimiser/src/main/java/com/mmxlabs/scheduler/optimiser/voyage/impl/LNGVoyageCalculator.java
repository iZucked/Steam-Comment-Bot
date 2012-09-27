/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import javax.inject.Inject;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;

/**
 * Implementation of {@link ILNGVoyageCalculator}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGVoyageCalculator implements ILNGVoyageCalculator {

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Override
	public final void init() {
		if (routeCostProvider == null) {
			throw new IllegalStateException("Route Cost Provider is not set");
		}
	}

	/**
	 * Calculate the fuel requirements between a pair of {@link IPortSlot}s. The {@link VoyageOptions} provides the specific choices to evaluate for this voyage (e.g. fuel choice, route, ...).
	 * 
	 * @param vessel
	 * @param from
	 * @param to
	 * @param options
	 * @param output
	 */
	@Override
	public final void calculateVoyageFuelRequirements(final VoyageOptions options, final VoyageDetails output) {
		output.setOptions(options);

		final IVessel vessel = options.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();
		final VesselState vesselState = options.getVesselState();

		final int equivalenceFactorM3ToMT = vesselClass.getBaseFuelConversionFactor();

		// Get distance for the route choice
		final long distance = options.getDistance();

		/**
		 * How much of the time given to us by the scheduler has to be spent travelling by an alternative route.
		 */
		final int additionalRouteTimeInHours = routeCostProvider.getRouteTransitTime(options.getRoute(), vesselClass);

		/**
		 * How much time is available to cover the distance, excluding time which must be spent traversing any canals
		 */
		final int availableTimeInHours = options.getAvailableTime() - additionalRouteTimeInHours;

		// Calculate speed
		// cast to int. as if long is required, then what are we doing?
		int speed = availableTimeInHours == 0 ? 0 : Calculator.speedFromDistanceTime(distance, availableTimeInHours);

		// speed calculation is not always correct - with a linear consumption
		// curve on base fuel for example, the best option is always either maximum speed
		// or minimum speed.

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
		final int travelTimeInHours = speed == 0 ? 0 : Calculator.getTimeFromSpeedDistance(speed, distance);
		final int idleTimeInHours = Math.max(0, availableTimeInHours - travelTimeInHours);

		// We output travel time + canal time, but the calculations
		// below only need to care about travel time
		output.setTravelTime(travelTimeInHours + additionalRouteTimeInHours);
		output.setIdleTime(idleTimeInHours);

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this journey
		 */
		final long consuptionRateInMTPerHour = speed == 0 ? 0 : vesselClass.getConsumptionRate(vesselState).getRate(speed);
		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consuptionRateInMTPerHour, travelTimeInHours);

		// Calculate fuel requirements
		if (options.useNBOForTravel()) {
			final long nboRateInM3PerHour = vesselClass.getNBORate(vesselState);
			/**
			 * The total quantity of LNG inevitably boiled off in this journey, in M3
			 */
			final long nboProvidedInM3 = Calculator.quantityFromRateTime(nboRateInM3PerHour, travelTimeInHours);
			/**
			 * The total quantity of LNG inevitably boiled off in this journey, in MT. Normally less than the amount boiled off in M3
			 */
			final long nboProvidedInMT = Calculator.convertM3ToMT(nboProvidedInM3, equivalenceFactorM3ToMT);

			if (nboProvidedInMT < requiredConsumptionInMT) {
				/**
				 * How many MT of base-fuel-or-equivalent are required after the NBO amount has been used
				 */
				final long diffInMT = requiredConsumptionInMT - nboProvidedInMT;
				if (options.useFBOForSupplement()) {
					// Use FBO for remaining quantity
					final long diffInM3 = Calculator.convertMTToM3(diffInMT, equivalenceFactorM3ToMT);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, diffInM3);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, diffInMT);
					output.setFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, 0);
				} else {
					// Use base for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
					output.setFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, diffInMT);
				}
			} 

			// TODO There is an edge case here where the supplemental base is less than pilot light
			// in which case we ought to bump it up to the right amount.
			if (output.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT) == 0) {
				final long pilotLightRateINMTPerHour = vesselClass.getPilotLightRate();
				final long pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerHour, travelTimeInHours);
				output.setFuelConsumption(FuelComponent.PilotLight, FuelUnit.MT, pilotLightConsumptionInMT);
			}

			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, nboProvidedInM3);
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.MT, nboProvidedInMT);
		} else {
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.MT, 0);
			output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
			output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
			output.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, requiredConsumptionInMT);
			output.setFuelConsumption(FuelComponent.PilotLight, FuelUnit.MT, 0);
		}

		final long idleNBORateInM3PerHour = vesselClass.getIdleNBORate(vesselState);

		if (options.useNBOForIdle()) {
			final long nboRequiredInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerHour, idleTimeInHours);
			final long nboRequiredInMT = Calculator.convertM3ToMT(nboRequiredInM3, equivalenceFactorM3ToMT);

			output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, nboRequiredInM3);
			output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, nboRequiredInMT);
			output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, 0);

			final long idlePilotLightRateINMTPerHour = vesselClass.getIdlePilotLightRate();
			final long idlePilotLightConsumptionInMT = Calculator.quantityFromRateTime(idlePilotLightRateINMTPerHour, idleTimeInHours);
			output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, idlePilotLightConsumptionInMT);
		} else {
			final long idleRateInMTPerHour = vesselClass.getIdleConsumptionRate(vesselState);

			int remainingIdleTimeInHours = idleTimeInHours;

			// long idleConsumptionInMT = Calculator.quantityFromRateTime(idleRateInMTPerHour, idleTimeInHours);

			if (options.useNBOForTravel()) {
				// Run down boil off after travel. On ballast voyages running on
				// NBO, It is necessary to keep a minimum heel of LNG whilst
				// travelling. Once in port, the heel can boil-off as there is
				// no need to keep it around. Base fuel requirements for idling
				// are much less than that provided by boil-off.

				// There is more boil-off than required consumption. We need to
				// work out how long we could provide energy for based on
				// boil-off time rather than use the raw quantity directly.
				long heelInM3 = vesselClass.getMinHeel();

				// How long will the boil-off last
				int deltaTimeInHours = Calculator.getTimeFromRateQuantity(idleNBORateInM3PerHour, heelInM3);

				if (options.shouldBeCold()) {
					/**
					 * How many hours the vessel has been empty and warming up.
					 */
					final int warmingTimeInHours = idleTimeInHours - deltaTimeInHours;

					if (warmingTimeInHours > vesselClass.getWarmupTime()) {
						if (options.getAllowCooldown() && (idleTimeInHours > (vesselClass.getWarmupTime() + vesselClass.getCooldownTime()))) {
							output.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, vesselClass.getCooldownVolume());
							// don't use any idle base during the cooldown
							remainingIdleTimeInHours -= vesselClass.getCooldownTime();
						} else {
							// warming time = idle - delta
							// therefore we need
							// idle - delta = vesselClass.getWarmupTime();
							// delta = idle - warmup
							deltaTimeInHours = idleTimeInHours - vesselClass.getWarmupTime();
							heelInM3 = idleNBORateInM3PerHour * deltaTimeInHours;
						}
					}
				}

				remainingIdleTimeInHours -= deltaTimeInHours;

				final long heelInMT = Calculator.convertM3ToMT(heelInM3, equivalenceFactorM3ToMT);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, heelInM3);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, heelInMT);

				final long idlePilotLightRateINMTPerHour = vesselClass.getIdlePilotLightRate();
				final long idlePilotLightConsumptionInMT = Calculator.quantityFromRateTime(idlePilotLightRateINMTPerHour, deltaTimeInHours);
				output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, idlePilotLightConsumptionInMT);
			} else {
				output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, 0);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 0);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, 0);

				if (options.shouldBeCold()) {
					// we can only do a cooldown here, because there is no LNG.
					// this situation shouldn't get presented to us by the
					// sequence scheduler unless it's unavoidable.
					if (options.isWarm() || (options.getAvailableTime() > vesselClass.getWarmupTime())) {
						output.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, vesselClass.getCooldownVolume());
						remainingIdleTimeInHours -= vesselClass.getCooldownTime();
					}
				}
			}

			if (remainingIdleTimeInHours > 0) {
				// Use base for remaining quantity
				final long idleConsumptionInMT = Calculator.quantityFromRateTime(idleRateInMTPerHour, remainingIdleTimeInHours);
				output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, idleConsumptionInMT);
			}
		}

		// Route Additional Consumption
		/**
		 * Base fuel requirement for canal traversal
		 */
		final long routeRequiredConsumptionInMT = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(options.getRoute(), vesselClass, vesselState), additionalRouteTimeInHours);
		if (routeRequiredConsumptionInMT > 0) {

			if (options.useNBOForTravel()) {
				
				final long nboRouteRateInM3PerHour = routeCostProvider.getRouteNBORate(options.getRoute(), vesselClass, vesselState);
				
				/**
				 * How much NBO is produced while in the canal (M3)
				 */
				long routeNboProvidedInM3 = Calculator.quantityFromRateTime(nboRouteRateInM3PerHour, additionalRouteTimeInHours);
				/**
				 * How much NBO is produced while in the canal (MTBFE)
				 */
				long routeNboProvidedInMT = Calculator.convertM3ToMT(routeNboProvidedInM3, equivalenceFactorM3ToMT);

				/**
				 * How much FBO is produced while in the canal (M3)
				 */
				long routeFboProvidedInM3;
				/**
				 * How much FBO is produced while in the canal (MTBFE)
				 */
				long routeFboProvidedInMT;

				/**
				 * How much supplement is needed over and above NBO while in the canal (MTBFE)
				 */
				final long routeDiffInMT;

				/**
				 * Consumed pilot light
				 */
				final long pilotLightConsumptionInMT;
				
				if (routeNboProvidedInMT < routeRequiredConsumptionInMT) {
					// Need to supplement
					if (options.useNBOForIdle()) {
						routeDiffInMT = 0;
						routeFboProvidedInMT = routeRequiredConsumptionInMT - routeNboProvidedInMT;
						routeFboProvidedInM3 = Calculator.convertMTToM3(routeDiffInMT, equivalenceFactorM3ToMT);
						
						final long pilotLightRateINMTPerHour = vesselClass.getPilotLightRate();
						pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerHour, additionalRouteTimeInHours);
						
					} else {
						routeDiffInMT = routeRequiredConsumptionInMT - routeNboProvidedInMT;
						routeFboProvidedInMT = 0;
						routeFboProvidedInM3 = 0;
						pilotLightConsumptionInMT = 0;
					}

				} else {
					routeDiffInMT = 0;
					routeFboProvidedInMT = 0;
					routeFboProvidedInM3 = 0;
					pilotLightConsumptionInMT = 0;
				}
				
				
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.M3, routeNboProvidedInM3);
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.MT, routeNboProvidedInMT);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.M3, routeFboProvidedInM3);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.MT, routeFboProvidedInMT);
				output.setRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, routeDiffInMT);
				
				output.setRouteAdditionalConsumption(FuelComponent.PilotLight, FuelUnit.MT, pilotLightConsumptionInMT);
			} else {
				// Base fuel only
				output.setRouteAdditionalConsumption(FuelComponent.Base, FuelUnit.MT, routeRequiredConsumptionInMT);
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.MT, 0);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
				output.setRouteAdditionalConsumption(FuelComponent.PilotLight, FuelUnit.MT, 0);
			}
		}
		output.setRouteCost(routeCostProvider.getRouteCost(options.getRoute(), vesselClass, vesselState));
	}

	/**
	 * Given a sequence of {@link IPortDetails} interleaved with {@link VoyageDetails}, compute the total base fuel and LNG requirements, taking into account initial conditions, load and discharge
	 * commitments etc. It is assumed that the first and last {@link IPortDetails} will be a Loading slot or other slot where the vessel state is set to a known state. Intermediate slots are any other
	 * type of slot (e.g. one discharge, multiple waypoints, etc). If the first slot is a load slot, then this is the only reason we should see a discharge slot.
	 * 
	 * @param sequence
	 * @param dischargeTime
	 * @param loadTime
	 */
	@Override
	public final int calculateVoyagePlan(final VoyagePlan voyagePlan, final IVessel vessel, final int[] arrivalTimes, final Object... sequence) {

		// Ensure odd number of elements
		assert (sequence.length % 2) == 1;

		final IVesselClass vesselClass = vessel.getVesselClass();

		int loadIdx = -1;
		int dischargeIdx = -1;
		long availableHeelinM3 = Long.MAX_VALUE;

		final long[] fuelConsumptions = new long[FuelComponent.values().length];

		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();

		/**
		 * Accumulates route costs due to canal decisions.
		 */
		int routeCostAccumulator = 0;

		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getPortSlot();
				if (slot instanceof ILoadSlot) {
					if (i == (sequence.length - 1)) {
						// End of run, so skip
					} else {
						loadIdx = i;
					}
				} else if (slot instanceof IDischargeSlot) {
					dischargeIdx = i;
				} else if ((i == 0) && (slot instanceof IHeelOptionsPortSlot)) {
					availableHeelinM3 = ((IHeelOptionsPortSlot) slot).getHeelOptions().getHeelLimit();
				}

				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc);
				}

			} else {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];
				// add route cost
				routeCostAccumulator += details.getRouteCost();
				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc, fc.getDefaultFuelUnit());
					fuelConsumptions[fc.ordinal()] += details.getRouteAdditionalConsumption(fc, fc.getDefaultFuelUnit());
				}

				// TODO: Assert that if discharge.heelOut set, then future
				// voyages have no LNG
				/*
				 * assert dischargeIdx == -1 || ((IDischargeSlot)sequence[dischargeIdx]).getHeelOut() == false || (details.getFuelConsumption(FuelComponent.NBO) == 0 && details
				 * .getFuelConsumption(FuelComponent.FBO) == 0 && details .getFuelConsumption(FuelComponent.IdleNBO) == 0);
				 */

				// Assert Laden/Ballast state switches after load and discharge
				assert ((loadIdx == -1) && (dischargeIdx == -1) && (details.getOptions().getVesselState() == VesselState.Ballast))
						|| ((loadIdx > -1) && (dischargeIdx == -1) && (details.getOptions().getVesselState() == VesselState.Laden))
						|| ((loadIdx > -1) && (dischargeIdx > -1) && (details.getOptions().getVesselState() == VesselState.Ballast));

				// TODO: Add test case for this information
				details.setFuelUnitPrice(FuelComponent.Base, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.Base_Supplemental, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.IdleBase, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.PilotLight, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.IdlePilotLight, baseFuelPricePerMT);

			}
		}

		int capacityViolations = 0;

		// If load or discharge has been set, then the other must be too.
		assert (loadIdx == -1) || (dischargeIdx != -1);
		assert (dischargeIdx == -1) || (loadIdx != -1);

		// long loadVolumeInM3 = 0;
		// long dischargeVolumeInM3 = 0;
		//
		// final int loadUnitPrice = 0;
		int dischargeUnitPrice = 0;

		// final int loadM3Price = 0;
		int dischargeM3Price = 0;

		int cargoCVValue = 0;

		final long lngConsumedInM3;

		// Load/Discharge sequence
		if ((loadIdx != -1) && (dischargeIdx != -1)) {
			final PortDetails loadDetails = (PortDetails) sequence[loadIdx];
			final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getPortSlot();
			final PortDetails dischargeDetails = (PortDetails) sequence[dischargeIdx];
			final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getPortSlot();

			// Store unit prices for later on
			// loadUnitPrice = loadSlot
			// .getPurchasePriceAtTime(arrivalTimes[loadIdx / 2]);
			dischargeUnitPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, arrivalTimes[dischargeIdx / 2]);
			// Store cargoCVValue
			cargoCVValue = loadSlot.getCargoCVValue();

			dischargeM3Price = (int) Calculator.multiply(dischargeUnitPrice, cargoCVValue);

			lngConsumedInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];

			final long cargoCapacityInM3 = vesselClass.getCargoCapacity();

			if (lngConsumedInM3 > cargoCapacityInM3) {
				// This is a real issue - hit physical constraints - reject
				// dischargeDetails.setCapacityViolation(CapacityViolationType.VESSEL_CAPACITY, lngConsumed - cargoCapacity);
				// Should we do this? - Could continue calculations and return a large number
				return -1;
			}

			// Any violations after this point are slot constraint violations
			final long minLoadVolumeInM3 = loadSlot.getMinLoadVolume();
			// final long maxLoadVolume = loadSlot.getMaxLoadVolume();

			final long minDischargeVolumeInM3 = dischargeSlot.getMinDischargeVolume();
			final long maxDischargeVolumeInM3 = dischargeSlot.getMaxDischargeVolume();

			// We cannot load more than is available or which would exceed
			// vessel capacity.
			final long upperLoadLimitInM3 = Math.min(cargoCapacityInM3, loadSlot.getMaxLoadVolume());

			if (minLoadVolumeInM3 - lngConsumedInM3 > maxDischargeVolumeInM3) {
				if (minLoadVolumeInM3 - lngConsumedInM3 < 0) {
					// discharge breach
					dischargeDetails.setCapacityViolation(CapacityViolationType.MAX_DISCHARGE, (minLoadVolumeInM3 - lngConsumedInM3) - maxDischargeVolumeInM3);
					++capacityViolations;
				} else {
					// load breach
					loadDetails.setCapacityViolation(CapacityViolationType.MIN_LOAD, minLoadVolumeInM3 - (maxDischargeVolumeInM3 + lngConsumedInM3));
					++capacityViolations;
				}
			}

			if (minDischargeVolumeInM3 + lngConsumedInM3 > upperLoadLimitInM3) {

				if (upperLoadLimitInM3 - lngConsumedInM3 < 0) {
					// load breach
					loadDetails.setCapacityViolation(CapacityViolationType.MAX_LOAD, lngConsumedInM3 - upperLoadLimitInM3);
					++capacityViolations;
				} else {
					// discharge breach
					dischargeDetails.setCapacityViolation(CapacityViolationType.MIN_DISCHARGE, upperLoadLimitInM3 - lngConsumedInM3);
					++capacityViolations;
				}
			}

			// Sanity checks
			assert lngConsumedInM3 >= 0;
			assert lngConsumedInM3 <= cargoCapacityInM3;
		} else {
			lngConsumedInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];
			if (lngConsumedInM3 > availableHeelinM3) {
				final PortDetails portDetails = (PortDetails) sequence[2];
				portDetails.setCapacityViolation(CapacityViolationType.MAX_HEEL, lngConsumedInM3 - availableHeelinM3);
				++capacityViolations;
			}
		}

		// Process details, filling in LNG prices
		// TODO: I don't really like altering the details at this stage of
		// processing, but this is where the information is being processed.
		// Can this be moved into the scheduler? If so, we need to ensure the
		// same price is used in all valid voyage legs.

		final boolean setLNGPrice;
		if (dischargeIdx != -1) {
			setLNGPrice = true;
		} else {
			if (((PortDetails) sequence[0]).getPortSlot() instanceof IHeelOptionsPortSlot) {
				final IHeelOptions options = ((IHeelOptionsPortSlot) ((PortDetails) sequence[0]).getPortSlot()).getHeelOptions();
				if (options.getHeelLimit() > 0) {
					setLNGPrice = true;
					dischargeM3Price = (int) Calculator.multiply(options.getHeelUnitPrice(), options.getHeelCVValue());
				} else {
					setLNGPrice = false;
				}
			} else {
				setLNGPrice = false;
			}
		}

		int cooldownM3Price = 0;

		// assert FuelComponent.NBO.getDefaultFuelUnit() == FuelUnit.M3;
		// assert FuelComponent.FBO.getDefaultFuelUnit() == FuelUnit.M3;
		// assert FuelComponent.IdleNBO.getDefaultFuelUnit() == FuelUnit.M3;
		for (int i = 0; i < sequence.length; ++i) {
			if ((i & 1) == 1) {
				assert sequence[i] instanceof VoyageDetails;

				final VoyageDetails details = (VoyageDetails) sequence[i];
				if (setLNGPrice || (dischargeIdx != -1)) {
					details.setFuelUnitPrice(FuelComponent.NBO, dischargeM3Price);
					details.setFuelUnitPrice(FuelComponent.FBO, dischargeM3Price);
					details.setFuelUnitPrice(FuelComponent.IdleNBO, dischargeM3Price);
				}
				if (details.getOptions().shouldBeCold() && (details.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3) > 0)) {
					final IPort port = details.getOptions().getToPortSlot().getPort();

					if ((loadIdx != -1) && (dischargeIdx != -1) && port.shouldVesselsArriveCold()) {
						// Cooldown violation! -- Assume index 4 is next load

						final PortDetails portDetails = (PortDetails) sequence[4];
						portDetails.setCapacityViolation(CapacityViolationType.FORCED_COOLDOWN, 1);
						++capacityViolations;
					}

					final int cooldownTime = arrivalTimes[i / 2] - vesselClass.getCooldownTime();


					// TODO is this how cooldown gas ought to be priced?
					if (details.getOptions().getToPortSlot() instanceof ILoadSlot) {
						// TODO double check how/if this affects caching
						// decisions
						final int cooldownPricePerMMBTU = port.getCooldownPriceCalculator().calculateCooldownUnitPrice((ILoadSlot)details.getOptions().getToPortSlot(), cooldownTime);
						final int cooldownPricePerM3 = (int) Calculator.multiply(cooldownPricePerMMBTU, ((ILoadSlot) details.getOptions().getToPortSlot()).getCargoCVValue());

						details.setFuelUnitPrice(FuelComponent.Cooldown, cooldownPricePerM3);
						cooldownM3Price = cooldownPricePerM3;
					} else {
						cooldownM3Price = 1000000;
					}
				}
			} else {
				assert sequence[i] instanceof PortDetails;
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
		voyagePlan.setTotalFuelCost(FuelComponent.Base, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.Base.ordinal()], baseFuelPricePerMT));
		voyagePlan.setTotalFuelCost(FuelComponent.Base_Supplemental, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.Base_Supplemental.ordinal()], baseFuelPricePerMT));
		voyagePlan.setTotalFuelCost(FuelComponent.IdleBase, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.IdleBase.ordinal()], baseFuelPricePerMT));

		voyagePlan.setTotalFuelCost(FuelComponent.PilotLight, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.PilotLight.ordinal()], baseFuelPricePerMT));
		voyagePlan.setTotalFuelCost(FuelComponent.IdlePilotLight, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.IdlePilotLight.ordinal()], baseFuelPricePerMT));

		voyagePlan.setTotalFuelCost(FuelComponent.NBO, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.NBO.ordinal()], dischargeM3Price));
		voyagePlan.setTotalFuelCost(FuelComponent.FBO, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.FBO.ordinal()], dischargeM3Price));
		voyagePlan.setTotalFuelCost(FuelComponent.IdleNBO, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.IdleNBO.ordinal()], dischargeM3Price));

		voyagePlan.setTotalFuelCost(FuelComponent.Cooldown, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.Cooldown.ordinal()], cooldownM3Price));

		voyagePlan.setLNGFuelVolume(lngConsumedInM3);

		voyagePlan.setTotalRouteCost(routeCostAccumulator);

		voyagePlan.setCapacityViolations(capacityViolations);

		return capacityViolations;
	}

	@Override
	public void setRouteCostDataComponentProvider(final IRouteCostProvider provider) {
		this.routeCostProvider = provider;
	}
}
