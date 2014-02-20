/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.List;

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
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;

/**
 * Implementation of {@link ILNGVoyageCalculator}.
 * 
 * @author Simon Goodall
 * @since 5.0
 * 
 */
public final class LNGVoyageCalculator implements ILNGVoyageCalculator {

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IPortCVProvider portCVProvider;

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

		// Calculate the appropriate speed
		final int speed = calculateSpeed(options, availableTimeInHours);
		output.setSpeed(speed);
		// Calculate total, travel and idle time

		// May be longer than available time
		final int travelTimeInHours = speed == 0 ? 0 : Calculator.getTimeFromSpeedDistance(speed, distance);
		// If idle time is negative, then there is not enough time for this voyage! This should be caught by the caller
		final int idleTimeInHours = Math.max(0, availableTimeInHours - travelTimeInHours);

		// We output travel time + canal time, but the calculations
		// below only need to care about travel time
		output.setTravelTime(travelTimeInHours + additionalRouteTimeInHours);
		output.setIdleTime(idleTimeInHours);

		// Calculate fuel requirements for travel time
		calculateTravelFuelRequirements(options, output, vesselClass, vesselState, travelTimeInHours, speed);

		// Calculate fuel requirements for an idle time
		calculateIdleFuelRequirements(options, output, vesselClass, vesselState, idleTimeInHours);

		// Check cooldown
		if (options.shouldBeCold()) {

			// Work out how long we have been warming up
			long warmingHours = 0;
			if (!options.useNBOForIdle()) {
				warmingHours += idleTimeInHours;
			}
			if (!options.useNBOForTravel()) {
				warmingHours += travelTimeInHours;
			}
			if (options.isWarm() || (warmingHours > vesselClass.getWarmupTime())) {
				final long cooldownVolume = vesselClass.getCooldownVolume();
				output.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, cooldownVolume);
			}
		}

		// Route Additional Consumption
		/**
		 * Base fuel requirement for canal traversal
		 */
		calculateRouteAdditionalFuelRequirements(options, output, vesselClass, vesselState, additionalRouteTimeInHours);

	}

	protected final void calculateRouteAdditionalFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVesselClass vesselClass, final VesselState vesselState,
			final int additionalRouteTimeInHours) {

		final int equivalenceFactorM3ToMT = vesselClass.getBaseFuelConversionFactor();

		final long routeRequiredConsumptionInMT = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(options.getRoute(), vesselClass, vesselState), additionalRouteTimeInHours);

		if (routeRequiredConsumptionInMT > 0) {

			if (options.useNBOForTravel()) {

				final long nboRouteRateInM3PerHour = routeCostProvider.getRouteNBORate(options.getRoute(), vesselClass, vesselState);

				/**
				 * How much NBO is produced while in the canal (M3)
				 */
				final long routeNboProvidedInM3 = Calculator.quantityFromRateTime(nboRouteRateInM3PerHour, additionalRouteTimeInHours);
				/**
				 * How much NBO is produced while in the canal (MTBFE)
				 */
				final long routeNboProvidedInMT = Calculator.convertM3ToMT(routeNboProvidedInM3, equivalenceFactorM3ToMT);

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
					if (options.useFBOForSupplement()) {
						routeDiffInMT = 0;
						routeFboProvidedInMT = routeRequiredConsumptionInMT - routeNboProvidedInMT;
						routeFboProvidedInM3 = Calculator.convertMTToM3(routeFboProvidedInMT, equivalenceFactorM3ToMT);

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

	protected final void calculateIdleFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVesselClass vesselClass, final VesselState vesselState, final int idleTimeInHours) {

		final int equivalenceFactorM3ToMT = vesselClass.getBaseFuelConversionFactor();

		if (!options.isCharterOutIdleTime()) {
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
				// ...Base fuel idle
				final long idleRateInMTPerHour = vesselClass.getIdleConsumptionRate(vesselState);
				final long idleConsumptionInMT = Calculator.quantityFromRateTime(idleRateInMTPerHour, idleTimeInHours);
				output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, idleConsumptionInMT);
			}
		}

	}

	protected final void calculateTravelFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVesselClass vesselClass, final VesselState vesselState,
			final int travelTimeInHours, final int speed) {

		final int equivalenceFactorM3ToMT = vesselClass.getBaseFuelConversionFactor();

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this journey
		 */
		final long consuptionRateInMTPerHour = speed == 0 ? 0 : vesselClass.getConsumptionRate(vesselState).getRate(speed);
		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consuptionRateInMTPerHour, travelTimeInHours);

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
	}

	/**
	 * Calculate the slowest speed the vessel can travel at given the time, distance and NBO constraints.
	 * 
	 * @param options
	 * @param vesselClass
	 * @param distance
	 * @param availableTimeInHours
	 * @return
	 */
	protected final int calculateSpeed(final VoyageOptions options, final int availableTimeInHours) {

		final IVesselClass vesselClass = options.getVessel().getVesselClass();
		final int distance = options.getDistance();

		// Calculate speed
		int speed;
		if (options.isCharterOutIdleTime()) {
			// If we are charting out the idle time, the fuel cost is not an issue!
			speed = vesselClass.getMaxSpeed();
		} else {
			speed = availableTimeInHours == 0 ? 0 : Calculator.speedFromDistanceTime(distance, availableTimeInHours);

			// speed calculation is not always correct - with a linear consumption
			// curve on base fuel for example, the best option is always either maximum speed
			// or minimum speed.

			if (distance != 0) {
				// Check NBO speed
				if (options.useNBOForTravel()) {
					final int nboSpeed = options.getNBOSpeed();
					if (speed < nboSpeed) {
						// Always go fast enough to consume all the NBO
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

				assert minSpeed <= maxSpeed;
				assert speed != 0;
			}
		}
		return speed;
	}

	/**
	 * Calculates the fuel consumptions for a sequence of alternating PortDetails / VoyageDetails objects, returning a total consumption array.
	 * 
	 * @param vessel
	 * @param sequence
	 * @return
	 * 
	 * @author Simon McGregor
	 * @since 8.0
	 */
	public long[] calculateVoyagePlanFuelConsumptions(final IVessel vessel, final IDetailsSequenceElement... sequence) {
		final long[] fuelConsumptions = new long[FuelComponent.values().length];

		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];

				// DO NOT INCLUDE THE LAST SEQUENCE ELEMENT IN THE TOTAL COST FOR THE PLAN
				if (i < sequence.length - 1) {
					for (final FuelComponent fc : FuelComponent.values()) {
						fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc);
					}
				}

			} else {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];

				for (final FuelComponent fc : FuelComponent.values()) {

					fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc, fc.getDefaultFuelUnit());
					fuelConsumptions[fc.ordinal()] += details.getRouteAdditionalConsumption(fc, fc.getDefaultFuelUnit());
				}
			}
		}
		return fuelConsumptions;
	}

	public void sanityCheckVesselState(final int loadIdx, final int dischargeIdx, final Object... sequence) {
		// If load or discharge has been set, then the other must be too.
		assert (loadIdx < 0 && dischargeIdx < 0) || (loadIdx >= 0 && dischargeIdx >= 0);

		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 1) {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];

				if (loadIdx <= i && i < dischargeIdx)
					assert (details.getOptions().getVesselState() == VesselState.Laden);
				else
					assert (details.getOptions().getVesselState() == VesselState.Ballast);
			}
		}

	}

	/**
	 * Calculates the price per M3 of cooldown and update the voyage details with the MMBTu cooldown quanity
	 * 
	 * @param vesselClass
	 * @param arrivalTimes
	 * @param sequence
	 * @return
	 * @since 8.0
	 */
	final public int calculateCooldownPrices(final IVesselClass vesselClass, final List<Integer> arrivalTimes, final IDetailsSequenceElement... sequence) {
		int cooldownM3Price = 0;

		int arrivalTimeIndex = -1;
		for (int i = 0; i < sequence.length; ++i) {
			if (sequence[i] instanceof VoyageDetails) {

				final VoyageDetails details = (VoyageDetails) sequence[i];
				final long cooldownVolumeInM3 = details.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3);
				if (details.getOptions().shouldBeCold() && (cooldownVolumeInM3 > 0)) {
					final IPort port = details.getOptions().getToPortSlot().getPort();

					// Look up arrival of next port
					final int cooldownTime = arrivalTimes.get(arrivalTimeIndex + 1);

					int cooldownPricePerMMBTU = 0;
					int cooldownCV = 0;
					// TODO is this how cooldown gas ought to be priced?
					if (details.getOptions().getToPortSlot() instanceof ILoadSlot) {
						// TODO double check how/if this affects caching
						// decisions
						cooldownPricePerMMBTU = port.getCooldownPriceCalculator().calculateCooldownUnitPrice((ILoadSlot) details.getOptions().getToPortSlot(), cooldownTime);
						cooldownCV = ((ILoadSlot) details.getOptions().getToPortSlot()).getCargoCVValue();
						cooldownM3Price = Calculator.costPerM3FromMMBTu(cooldownPricePerMMBTU, cooldownCV);
					} else {
						cooldownPricePerMMBTU = port.getCooldownPriceCalculator().calculateCooldownUnitPrice(cooldownTime);
						cooldownCV = portCVProvider.getPortCV(port);
						cooldownM3Price = Calculator.costPerM3FromMMBTu(cooldownPricePerMMBTU, cooldownCV);
					}

					// Store the MMBTu cooldown value
					details.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.MMBTu, Calculator.convertM3ToMMBTu(cooldownVolumeInM3, cooldownCV));

					assert FuelComponent.Cooldown.getPricingFuelUnit() == FuelUnit.MMBTu;
					details.setFuelUnitPrice(FuelComponent.Cooldown, cooldownPricePerMMBTU);
				}
			} else {
				assert sequence[i] instanceof PortDetails;
				arrivalTimeIndex++;
			}
		}

		return cooldownM3Price;
	}

	/**
	 * Returns an array of LNG prices per MMBTu corresponding to each index on an interleaved sequence of port / voyage details. LNG is priced at the discharge value for the next discharge, or the
	 * previous discharge if there is no next discharge in the sequence.
	 * 
	 * This method expects either no load or discharge ports, or at least one load and at least one discharge port. Behaviour in any other situation is undefined.
	 * 
	 * @param loadIndices
	 * @param dischargeIndices
	 * @param arrivalTimes
	 * @param sequence
	 * @return A list of LNG prices, or null if there was no way to establish LNG prices.
	 * @since 8.0
	 */
	final public int[] getLngEffectivePrices(final List<Integer> loadIndices, final List<Integer> dischargeIndices, final List<Integer> arrivalTimes, final IDetailsSequenceElement... sequence) {
		// TODO: does not need to be this long
		final int[] resultPerMMBtu = new int[sequence.length];

		// require at least one load and discharge port, or no loads and no discharges
		assert ((loadIndices.isEmpty()) == (dischargeIndices.isEmpty()));

		// no loads or discharges
		if (loadIndices.isEmpty()) {
			final IPortSlot firstSlot = ((PortDetails) sequence[0]).getOptions().getPortSlot();

			// price LNG based on the heel value at the first slot
			if (firstSlot instanceof IHeelOptionsPortSlot) {
				final IHeelOptions options = ((IHeelOptionsPortSlot) firstSlot).getHeelOptions();
				if (options.getHeelLimit() > 0) {
					final int pricePerMMBTu = options.getHeelUnitPrice();
					for (int i = 0; i < sequence.length; i++) {
						resultPerMMBtu[i] = pricePerMMBTu;
					}
					return resultPerMMBtu;
				}
			}
			// or refuse to price the LNG otherwise
			return null;
		}

		// further logic is for the load / discharge case

		final int prevDischargeIndex = 0;
		int lngValuePerMMBTu = 0;

		// step through the discharge ports
		for (final int i : dischargeIndices) {
			final IPortSlot slot = ((PortDetails) sequence[i]).getOptions().getPortSlot();
			final IDischargeSlot dischargeSlot = (IDischargeSlot) slot;

			// calculate the effective LNG value based on this discharge slot
			lngValuePerMMBTu = dischargeSlot.getDischargePriceCalculator().estimateSalesUnitPrice(dischargeSlot, arrivalTimes.get(i / 2), null);

			// and apply the value to prices on all preceding voyages
			for (int j = prevDischargeIndex; j < i; j++) {
				resultPerMMBtu[j] = lngValuePerMMBTu;
			}
		}

		// remember the final value coming out of the loop
		final int finalLngValuePerMMBTu = lngValuePerMMBTu;

		// now apply the value from the last discharge port to all voyages following it
		final int finalDischargeIndex = dischargeIndices.get(dischargeIndices.size() - 1);
		for (int j = finalDischargeIndex; j < sequence.length; j++) {
			resultPerMMBtu[j] = finalLngValuePerMMBTu;
		}

		return resultPerMMBtu;
	}

	/**
	 * Given a sequence of {@link IPortDetails} interleaved with {@link VoyageDetails}, compute the total base fuel and LNG requirements, taking into account initial conditions, load and discharge
	 * commitments etc. It is assumed that the first and last {@link IPortDetails} will be a Loading slot or other slot where the vessel state is set to a known state. Intermediate slots are any other
	 * type of slot (e.g. one discharge, multiple waypoints, etc). If the first slot is a load slot, then this is the only reason we should see a discharge slot.
	 * 
	 * NOTE: this method intentionally ignores the last sequence element when calculating the total cost of a voyage plan, in order to avoid double-counting elements, since voyage plans are expected
	 * to come in A-B-C C-D-E E-F-G chains, where the last element of the chain does not get counted.
	 * 
	 * @param sequence
	 * @param dischargeTime
	 * @param loadTime
	 * @return Number of capacity constraints which had to be violated in the allocation
	 * @since 8.0
	 */
	@Override
	public final int calculateVoyagePlan(final VoyagePlan voyagePlan, final IVessel vessel, final long startHeelInM3, final int baseFuelPricePerMT, final List<Integer> arrivalTimes,
			final IDetailsSequenceElement... sequence) {
		/*
		 * TODO: instead of taking an interleaved List<Object> as a parameter, this would have a far more informative signature (and cleaner logic?) if it passed a list of IPortDetails and a list of
		 * VoyageDetails as separate parameters. Worth doing at some point?
		 */

		// Ensure odd number of elements
		assert (sequence.length % 2) == 1;

		final IVesselClass vesselClass = vessel.getVesselClass();

		final int loadIdx = findFirstLoadIndex(sequence);
		final int dischargeIdx = findFirstDischargeIndex(sequence);
		// sanityCheckVesselState(loadIdx, dischargeIdx, sequence);

		voyagePlan.setStartingHeelInM3(startHeelInM3);

		final long[] fuelConsumptions = calculateVoyagePlanFuelConsumptions(vessel, sequence);

		/**
		 * Accumulates route costs due to canal decisions.
		 */
		int routeCostAccumulator = 0;

		// The last voyage details in sequence.
		VoyageDetails lastVoyageDetailsElement = null;

		int voyageTime = 0;
		// add up route cost and find the last voyage details element
		for (int i = 0; i < sequence.length; ++i) {
			final IDetailsSequenceElement element = sequence[i];
			if (element instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) element;
				// add route cost
				routeCostAccumulator += details.getRouteCost();

				voyageTime += details.getTravelTime();
				voyageTime += details.getIdleTime();

				lastVoyageDetailsElement = details;
			}
		}

		final boolean boiloffWasUsed = (lastVoyageDetailsElement != null);

		int violationsCount = 0;

		// If load or discharge has been set, then the other must be too.
		assert ((loadIdx == -1) == (dischargeIdx == -1));

		int cargoCVValue = 0;

		// the LNG which will be required to complete the sequence, including
		// NBO, FBO and any minimum heel if travelling on NBO
		long lngCommitmentInM3;

		// Load/Discharge sequence
		if ((loadIdx != -1) && (dischargeIdx != -1)) {
			final PortDetails loadDetails = (PortDetails) sequence[loadIdx];
			final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
			final PortDetails dischargeDetails = (PortDetails) sequence[dischargeIdx];
			final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

			// Store cargoCVValue
			cargoCVValue = loadSlot.getCargoCVValue();

			// the LNG which would by default be consumed for the given fuel usage choices during travel & idle
			lngCommitmentInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];

			// Store this value now as we may change it below during the heel calculations
			voyagePlan.setLNGFuelVolume(lngCommitmentInM3);

			// Any violations after this point are slot constraint violations

			final long minDischargeVolumeInM3 = dischargeSlot.getMinDischargeVolume();

			final long cargoCapacityInM3 = vessel.getCargoCapacity();

			// Apply safety heel if required
			final long remainingHeelInM3;
			if (lastVoyageDetailsElement != null && lastVoyageDetailsElement.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3) > 0) {
				remainingHeelInM3 = 0;
			} else if (lastVoyageDetailsElement != null && lastVoyageDetailsElement.getOptions().shouldBeCold()) {
				remainingHeelInM3 = vesselClass.getMinHeel();
				voyagePlan.setRemainingHeelInM3(remainingHeelInM3);
			} else {
				remainingHeelInM3 = 0;
			}

			violationsCount += checkCargoCapacityViolations(startHeelInM3, lngCommitmentInM3, loadDetails, loadSlot, dischargeDetails, dischargeSlot, minDischargeVolumeInM3, cargoCapacityInM3,
					remainingHeelInM3);

			// Sanity checks
			assert lngCommitmentInM3 >= 0;
			// assert lngCommitmentInM3 <= cargoCapacityInM3;
		} else {
			// was not a Cargo sequence
			lngCommitmentInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];
			final long remainingHeelInM3 = startHeelInM3 - lngCommitmentInM3;

			// Store this value now as we may change it below during the heel calculations
			voyagePlan.setLNGFuelVolume(lngCommitmentInM3);

			// Three cases;
			// A) we have lng, but need more than is available (worst)
			// B) We have lng, but decided not to use it
			// C) We have lng and used some or all of it (best)

			// I.e. we want to force heel use if it is physically possible,

			// if our fuel requirements exceed our onboard fuel
			if (remainingHeelInM3 < 0) {
				// This is worse than not using it at all -- case A
				violationsCount += 2;
			} else if (remainingHeelInM3 > 0) {
				voyagePlan.setRemainingHeelInM3(remainingHeelInM3);
			}

			// Look up the heel options CV value if present
			final IPortSlot firstSlot = ((PortDetails) sequence[0]).getOptions().getPortSlot();
			if (firstSlot instanceof IHeelOptionsPortSlot) {
				final IHeelOptions options = ((IHeelOptionsPortSlot) firstSlot).getHeelOptions();
				cargoCVValue = options.getHeelCVValue();

				// If we have not been able to use NBO, assume there was no LNG..
				// .. unless we have a zero length voyage in which case we would not have used any BOG.
				if (voyageTime > 0 && lngCommitmentInM3 == 0 && options.getHeelLimit() > 0) {
					voyagePlan.setStartingHeelInM3(0);
					voyagePlan.setRemainingHeelInM3(0);
					// Mark as a violation to prefer other options -- case B
					++violationsCount;
				}
			}

		}

		final List<Integer> loadIndices = findLoadIndices(sequence);
		final List<Integer> dischargeIndices = findDischargeIndices(sequence);

		// Process details, filling in LNG prices
		// TODO: I don't really like altering the details at this stage of
		// processing, but this is where the information is being processed.
		// Can this be moved into the scheduler? If so, we need to ensure the
		// same price is used in all valid voyage legs.
		final int[] pricesPerMMBTu = getLngEffectivePrices(loadIndices, dischargeIndices, arrivalTimes, sequence);

		// set the LNG values for the voyages
		// final int numVoyages = sequence.length / 2;
		for (int i = 0; i < sequence.length - 1; ++i) {
			final Object element = sequence[i];
			if (element instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) element;
				for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {
					// Existing consumption data is in M3, also store the MMBtu values
					final long consumptionInM3 = details.getFuelConsumption(fc, fc.getDefaultFuelUnit()) + details.getRouteAdditionalConsumption(fc, fc.getDefaultFuelUnit());
					final long consumptionInMMBTu = Calculator.convertM3ToMMBTu(consumptionInM3, cargoCVValue);
					details.setFuelConsumption(fc, FuelUnit.MMBTu, consumptionInMMBTu);

					if (pricesPerMMBTu != null) {
						// Set the LNG unit price
						final int unitPrice = pricesPerMMBTu[i];
						details.setFuelUnitPrice(fc, unitPrice);
						// Sum up the voyage costs
						final long currentTotal = voyagePlan.getTotalFuelCost(fc);
						voyagePlan.setTotalFuelCost(fc, currentTotal + Calculator.costFromConsumption(consumptionInMMBTu, unitPrice));
					}
				}
				for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {
					//
					details.setFuelUnitPrice(fc, baseFuelPricePerMT);
					// // Sum up the voyage costs - breaks ITS
					// final long consumptionInMT = details.getFuelConsumption(fc, fc.getDefaultFuelUnit()) + details.getRouteAdditionalConsumption(fc, fc.getDefaultFuelUnit());
					// final long currentTotal = voyagePlan.getTotalFuelCost(fc);
					// voyagePlan.setTotalFuelCost(fc, currentTotal + Calculator.costFromConsumption(consumptionInMT, baseFuelPricePerMT));
				}
			} else {
				assert element instanceof PortDetails;
				final PortDetails details = (PortDetails) element;
				// NO LNG Consumption in Port
				for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {

					details.setFuelUnitPrice(fc, baseFuelPricePerMT);
					// Sum up the voyage costs - breaks ITS
					// final long consumptionInMT = details.getFuelConsumption(fc);
					// final long currentTotal = voyagePlan.getTotalFuelCost(fc);
					// voyagePlan.setTotalFuelCost(fc, currentTotal + Calculator.costFromConsumption(consumptionInMT, baseFuelPricePerMT));
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

		// Could be performed above, but ITS fails :( - possibly rounding in calcs, may be a real bug?
		for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {
			final long c = fuelConsumptions[fc.ordinal()];
			voyagePlan.setTotalFuelCost(fc, Calculator.costFromConsumption(c, baseFuelPricePerMT));
		}

		final int cooldownM3Price = calculateCooldownPrices(vesselClass, arrivalTimes, sequence);
		voyagePlan.setTotalFuelCost(FuelComponent.Cooldown, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.Cooldown.ordinal()], cooldownM3Price));

		voyagePlan.setTotalRouteCost(routeCostAccumulator);

		// Weight non cooldown violations heavier than cooldown
		violationsCount *= 2;
		// Check for cooldown violations and add to the violations count
		violationsCount += checkCooldownViolations(loadIdx, dischargeIdx, sequence);

		voyagePlan.setViolationsCount(violationsCount);
		return violationsCount;
	}

	protected int checkCargoCapacityViolations(final long startHeelInM3, final long lngCommitmentInM3, final PortDetails loadDetails, final ILoadSlot loadSlot, final PortDetails dischargeDetails,
			final IDischargeSlot dischargeSlot, final long minDischargeVolumeInM3, final long cargoCapacityInM3, final long heelToDischarge) {

		int violationsCount = 0;
		final long minLoadVolumeInM3 = loadSlot.getMinLoadVolume();
		long maxLoadVolumeInM3 = loadSlot.getMaxLoadVolume();
		long maxDischargeVolumeInM3 = dischargeSlot.getMaxDischargeVolume();

		if (maxLoadVolumeInM3 == 0) {
			maxLoadVolumeInM3 = Long.MAX_VALUE;
		}
		if (maxDischargeVolumeInM3 == 0) {
			maxDischargeVolumeInM3 = Long.MAX_VALUE;
		}
		// We cannot load more than is available or which would exceed
		// vessel capacity.
		final long upperLoadLimitInM3 = Math.min(cargoCapacityInM3 - startHeelInM3, maxLoadVolumeInM3);

		if (minLoadVolumeInM3 > cargoCapacityInM3 - startHeelInM3) {
			++violationsCount;
		}

		if (lngCommitmentInM3 > upperLoadLimitInM3 + startHeelInM3) {
			// ++violationsCount;
		}

		// This is the smallest amount of gas we can load
		if (minLoadVolumeInM3 - lngCommitmentInM3 + startHeelInM3 > maxDischargeVolumeInM3) {
			/*
			 * note - this might not be a genuine violation since rolling over the excess LNG may be permissible and in some cases it is even commercially desirable, but restrictions on LNG
			 * destination or complications from profit share contracts make it a potential violation, and we err on the side of caution
			 */

			// load breach -- need to load less than we are permitted
			++violationsCount;
		}

		// The load should cover at least the fuel usage plus the heel (or the min discharge, whichever is greater)
		if (Math.max(minDischargeVolumeInM3, heelToDischarge) + lngCommitmentInM3 > upperLoadLimitInM3 + startHeelInM3) {
			++violationsCount;
		}
		return violationsCount;
	}

	protected int checkCooldownViolations(final int loadIdx, final int dischargeIdx, final IDetailsSequenceElement... sequence) {
		int cooldownViolations = 0;
		// check for cooldown violations
		for (int i = 0; i < sequence.length; ++i) {
			if ((i & 1) == 1) {
				assert sequence[i] instanceof VoyageDetails;

				final VoyageDetails details = (VoyageDetails) sequence[i];

				final boolean shouldBeCold = details.getOptions().shouldBeCold() && !details.getOptions().getAllowCooldown();
				final long fuelConsumption = details.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3);
				if (shouldBeCold && (fuelConsumption > 0)) {

					// if ((loadIdx != -1)) {
					++cooldownViolations;
					// }

				}
			} else {
				assert sequence[i] instanceof PortDetails;
			}
		}
		return cooldownViolations;
	}

	/**
	 * @since 8.0
	 * 
	 */
	@Override
	public final List<IDetailsSequenceElement> generateFuelCostCalculatedSequence(final IOptionsSequenceElement... baseSequence) {
		final List<IDetailsSequenceElement> result = new ArrayList<IDetailsSequenceElement>(baseSequence.length);
		for (final Object element : baseSequence) {

			// Loop through basic elements, calculating voyage requirements
			// to build up basic voyage plan details.
			if (element instanceof VoyageOptions) {
				final VoyageOptions options = (VoyageOptions) element;

				final VoyageDetails voyageDetails = new VoyageDetails();
				voyageDetails.setOptions(options);
				// Calculate voyage cost
				calculateVoyageFuelRequirements(options, voyageDetails);
				result.add(voyageDetails);
			} else if (element instanceof PortOptions) {
				final PortOptions options = ((PortOptions) element).clone();
				final PortDetails details = new PortDetails();
				details.setOptions(options);
				calculatePortFuelRequirements(options, details);
				result.add(details);
			} else {
				System.err.println("Warning: non-Option type in option sequence: " + element);
				result.add((IDetailsSequenceElement) element);
			}
		}

		return result;
	}

	/**
	 * @author Simon McGregor
	 * 
	 *         Populate a PortDetails object with correct fuel costs based on a PortOptions object.
	 * 
	 * @param options
	 *            The PortOptions to use, specifying vessel class, vessel state and port visit duration.
	 * @param details
	 *            The PortDetails to set the correct fuel consumption for.
	 * @since 2.0
	 */
	public final void calculatePortFuelRequirements(final PortOptions options, final PortDetails details) {
		details.setOptions(options);
		//
		// TODO fix up port details, call this method
		// Add in port costs?
		//
		final IVessel vessel = options.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();
		// final VesselState vesselState = options.getVesselState();

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this port visit
		 */
		final long consumptionRateInMTPerHour;

		final PortType portType = options.getPortSlot().getPortType();

		// temporary kludge: ignore non-load non-discharge ports for port consumption
		if (portType == PortType.Load || portType == PortType.Discharge)
			consumptionRateInMTPerHour = vesselClass.getInPortConsumptionRate(portType);
		else
			consumptionRateInMTPerHour = 0;

		final int visitDuration = options.getVisitDuration();

		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consumptionRateInMTPerHour, visitDuration);

		details.setFuelConsumption(FuelComponent.Base, requiredConsumptionInMT);
	}

	public void setRouteCostDataComponentProvider(final IRouteCostProvider provider) {
		this.routeCostProvider = provider;
	}

	/**
	 * @since 2.0
	 */
	public void setPortCVProvider(final IPortCVProvider portCVProvider) {
		this.portCVProvider = portCVProvider;
	}

	/**
	 * @since 8.0
	 */
	final public int findFirstLoadIndex(final IDetailsSequenceElement... sequence) {
		// ignore the last element in the sequence, to avoid double-counting (it will be included in the next sequence)
		for (int i = 0; i < sequence.length - 1; ++i) {
			if (sequence[i] instanceof PortDetails) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getOptions().getPortSlot();
				if (slot instanceof ILoadSlot) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * @since 8.0
	 */
	final public int findFirstDischargeIndex(final IDetailsSequenceElement... sequence) {
		for (int i = 0; i < sequence.length; ++i) {
			if (sequence[i] instanceof PortDetails) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getOptions().getPortSlot();
				if (slot instanceof IDischargeSlot) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * @since 8.0
	 */
	final public List<Integer> findLoadIndices(final IDetailsSequenceElement... sequence) {
		final List<Integer> storage = new ArrayList<Integer>();

		// ignore the last element in the sequence, to avoid double-counting (it will be included in the next sequence)
		for (int i = 0; i < sequence.length - 1; i++) {
			if (sequence[i] instanceof PortDetails) {
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getOptions().getPortSlot();
				if (slot instanceof ILoadSlot) {
					storage.add(i);
				}
			}
		}

		return storage;
	}

	/**
	 * @since 8.0
	 */
	final public List<Integer> findDischargeIndices(final IDetailsSequenceElement... sequence) {
		final List<Integer> storage = new ArrayList<Integer>();

		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] instanceof PortDetails) {
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getOptions().getPortSlot();
				if (slot instanceof IDischargeSlot) {
					storage.add(i);
				}
			}
		}

		return storage;
	}

}
