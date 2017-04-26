/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCooldownDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * Implementation of {@link ILNGVoyageCalculator}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGVoyageCalculator implements ILNGVoyageCalculator {

	// Constants to indicate the state of the vessel (without a specific heel level at this point..)
	public static final int STATE_WARM = 0; // Vessel ends warm
	public static final int STATE_COLD_MIN_HEEL = 1; // Vessel ends cold with min heel range.
	public static final int STATE_COLD_WARMING_TIME = 2; // Vessel ends cold as with warming time. Zero heel.
	public static final int STATE_COLD_COOLDOWN = 3; // Vessel ends cold due to cooldown. Zero heel
	public static final int STATE_COLD_NO_VOYAGE = 4; // Vessel ends cold. There was no voyage. We are still at discharge port. Can pick heel...

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IPortCVProvider portCVProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IPortCostProvider portCostProvider;

	@Inject
	private IPortCooldownDataProvider portCooldownDataProvider;

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
		final int additionalRouteTimeInHours = routeCostProvider.getRouteTransitTime(options.getRoute(), vessel);

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
		output.setCooldownPerformed(false);
		if (options.shouldBeCold() == VesselTankState.MUST_BE_COLD) {

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
				output.setCooldownPerformed(true);
			}
		}

		// Route Additional Consumption
		/**
		 * Base fuel requirement for canal traversal
		 */
		calculateRouteAdditionalFuelRequirements(options, output, vessel, vesselState, additionalRouteTimeInHours);

	}

	protected final void calculateRouteAdditionalFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVessel vessel, final VesselState vesselState,
			final int additionalRouteTimeInHours) {

		final int equivalenceFactorMMBTuToMT = vessel.getVesselClass().getBaseFuel().getEquivalenceFactor();

		final long routeRequiredConsumptionInMT = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(options.getRoute(), vessel, vesselState), additionalRouteTimeInHours) / 24L;
		final int cargoCVValue = options.getCargoCVValue();

		if (routeRequiredConsumptionInMT > 0) {

			if (options.useNBOForTravel()) {

				final long nboRouteRateInM3PerDay = routeCostProvider.getRouteNBORate(options.getRoute(), vessel, vesselState);

				/**
				 * How much NBO is produced while in the canal (M3)
				 */
				final long routeNboProvidedInM3 = Calculator.quantityFromRateTime(nboRouteRateInM3PerDay, additionalRouteTimeInHours) / 24L;
				/**
				 * How much NBO is produced while in the canal (MTBFE)
				 */

				final long routeNboProvidedInMT = Calculator.convertM3ToMT(routeNboProvidedInM3, cargoCVValue, equivalenceFactorMMBTuToMT);

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
						routeFboProvidedInM3 = Calculator.convertMTToM3(routeFboProvidedInMT, cargoCVValue, equivalenceFactorMMBTuToMT);

						final long pilotLightRateINMTPerDay = vessel.getVesselClass().getPilotLightRate();
						pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerDay, additionalRouteTimeInHours) / 24L;

					} else {
						// Reliq vessels should not use base fuel supplement.
						assert (!vessel.getVesselClass().hasReliqCapability());

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

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vessel.getVesselClass().getMinBaseFuelConsumptionInMTPerDay(), additionalRouteTimeInHours) / 24L;
		if (options.useNBOForTravel()) {
			if (output.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT) < minBaseFuelConsumptionInMT) {
				output.setRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, minBaseFuelConsumptionInMT);
			}
		} else {
			if (output.getRouteAdditionalConsumption(FuelComponent.Base, FuelUnit.MT) < minBaseFuelConsumptionInMT) {
				output.setRouteAdditionalConsumption(FuelComponent.Base, FuelUnit.MT, minBaseFuelConsumptionInMT);
			}
		}

	}

	protected final void calculateIdleFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVesselClass vesselClass, final VesselState vesselState,
			final int idleTimeInHours) {

		final int equivalenceFactorM3ToMT = vesselClass.getBaseFuel().getEquivalenceFactor();

		if (!options.isCharterOutIdleTime()) {
			final long idleNBORateInM3PerDay = vesselClass.getIdleNBORate(vesselState);

			if (options.useNBOForIdle()) {
				final int cargoCVValue = options.getCargoCVValue();
				final long nboRequiredInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerDay, idleTimeInHours) / 24L;
				final long nboRequiredInMT = Calculator.convertM3ToMT(nboRequiredInM3, cargoCVValue, equivalenceFactorM3ToMT);

				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, nboRequiredInM3);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, nboRequiredInMT);
				output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, 0);

				final long idlePilotLightRateINMTPerDay = vesselClass.getIdlePilotLightRate();
				final long idlePilotLightConsumptionInMT = Calculator.quantityFromRateTime(idlePilotLightRateINMTPerDay, idleTimeInHours) / 24L;
				output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, idlePilotLightConsumptionInMT);
			} else {
				// ...Base fuel idle
				final long idleRateInMTPerDay = vesselClass.getIdleConsumptionRate(vesselState);
				final long idleConsumptionInMT = Calculator.quantityFromRateTime(idleRateInMTPerDay, idleTimeInHours) / 24L;
				output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, idleConsumptionInMT);
			}
		}

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vesselClass.getMinBaseFuelConsumptionInMTPerDay(), idleTimeInHours) / 24L;
		if (output.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT) < minBaseFuelConsumptionInMT) {
			output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, minBaseFuelConsumptionInMT);
		}
	}

	protected final void calculateTravelFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVesselClass vesselClass, final VesselState vesselState,
			final int travelTimeInHours, final int speed) {

		final int equivalenceFactorMMBTuToMT = vesselClass.getBaseFuel().getEquivalenceFactor();

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this journey
		 */
		final long consumptionRateInMTPerDay = speed == 0 ? 0 : vesselClass.getConsumptionRate(vesselState).getRate(speed);
		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consumptionRateInMTPerDay, travelTimeInHours) / 24L;

		if (options.useNBOForTravel()) {
			final int cargoCVValue = options.getCargoCVValue();

			final long nboRateInM3PerDay = vesselClass.getNBORate(vesselState);
			/**
			 * The total quantity of LNG inevitably boiled off in this journey, in M3
			 */
			final long nboProvidedInM3 = Calculator.quantityFromRateTime(nboRateInM3PerDay, travelTimeInHours) / 24L;
			/**
			 * The total quantity of LNG inevitably boiled off in this journey, in MT. Normally less than the amount boiled off in M3
			 */
			final long nboProvidedInMT = Calculator.convertM3ToMT(nboProvidedInM3, cargoCVValue, equivalenceFactorMMBTuToMT);

			if (nboProvidedInMT < requiredConsumptionInMT) {
				/**
				 * How many MT of base-fuel-or-equivalent are required after the NBO amount has been used
				 */
				final long diffInMT = requiredConsumptionInMT - nboProvidedInMT;
				if (options.useFBOForSupplement()) {
					// Use FBO for remaining quantity
					final long diffInM3 = Calculator.convertMTToM3(diffInMT, cargoCVValue, equivalenceFactorMMBTuToMT);
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
				final long pilotLightRateINMTPerDay = vesselClass.getPilotLightRate();
				final long pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerDay, travelTimeInHours) / 24L;
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

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vesselClass.getMinBaseFuelConsumptionInMTPerDay(), travelTimeInHours) / 24L;
		if (options.useNBOForTravel()) {
			if (output.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT) < minBaseFuelConsumptionInMT) {
				output.setFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, minBaseFuelConsumptionInMT);
			}
		} else {
			if (output.getFuelConsumption(FuelComponent.Base, FuelUnit.MT) < minBaseFuelConsumptionInMT) {
				output.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, minBaseFuelConsumptionInMT);
			}
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
	 */
	public long[] calculateVoyagePlanFuelConsumptions(final IVessel vessel, final IDetailsSequenceElement... sequence) {
		final long[] fuelConsumptions = new long[FuelComponent.values().length];

		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];

				// DO NOT INCLUDE THE LAST SEQUENCE ELEMENT IN THE TOTAL COST FOR THE PLAN
				if (sequence.length == 1 || i < sequence.length - 1) {
					for (final FuelComponent fc : FuelComponent.values()) {
						fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc, fc.getDefaultFuelUnit());
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
			if ((i % 2) != 0) {
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
	 * Calculates the cost of a cooldown
	 * 
	 * @param vesselClass
	 * @param arrivalTimes
	 * @param sequence
	 * @return
	 */
	final public long calculateCooldownCost(final IVesselClass vesselClass, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement... sequence) {
		long cooldownCost = 0;

		for (int i = 0; i < sequence.length; ++i) {
			if (sequence[i] instanceof VoyageDetails) {

				final VoyageDetails details = (VoyageDetails) sequence[i];
				if (details.isCooldownPerformed()) {
					final IPort port = details.getOptions().getToPortSlot().getPort();

					// Look up arrival of next port
					final int cooldownTime = portTimesRecord.getSlotTime(details.getOptions().getToPortSlot());

					int cooldownCV = 0;
					IPort cooldownPort;
					// TODO is this how cooldown gas ought to be priced?
					if (details.getOptions().getToPortSlot() instanceof ILoadSlot) {
						// TODO double check how/if this affects caching
						// decisions
						final ILoadSlot loadSlot = (ILoadSlot) details.getOptions().getToPortSlot();
						cooldownCV = loadSlot.getCargoCVValue();
						cooldownPort = loadSlot.getPort();
					} else {
						cooldownCV = portCVProvider.getPortCV(port);
						cooldownPort = port;
					}

					final ICooldownCalculator cooldownCalculator = portCooldownDataProvider.getCooldownCalculator(port);
					if (cooldownCalculator != null) {
						cooldownCost = cooldownCalculator.calculateCooldownCost(vesselClass, cooldownPort, cooldownCV, cooldownTime);
					} else {
						// Trying to cooldown somewhere we are not allowed to cooldown (e.g. discharge port at sequence end?)
					}
				}
			} else {
				assert sequence[i] instanceof PortDetails;
			}
		}

		return cooldownCost;
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
	 */
	final public int[] getLngEffectivePrices(final List<Integer> loadIndices, final List<Integer> dischargeIndices, final IPortTimesRecord portTimesRecord, final long startHeelVolumeInM3,
			final IDetailsSequenceElement... sequence) {
		// TODO: does not need to be this long
		final int[] resultPerMMBtu = new int[sequence.length];

		// require at least one load and discharge port, or no loads and no discharges
		assert ((loadIndices.isEmpty()) == (dischargeIndices.isEmpty()));

		// no loads or discharges
		if (loadIndices.isEmpty()) {

			final IPortSlot firstSlot = ((PortDetails) sequence[0]).getOptions().getPortSlot();

			// price LNG based on the heel value at the first slot
			if (firstSlot instanceof IHeelOptionSupplierPortSlot) {
				final IHeelOptionSupplier options = ((IHeelOptionSupplierPortSlot) firstSlot).getHeelOptionsSupplier();
				if (options.getMaximumHeelAvailableInM3() > 0) {
					final int pricePerMMBTu = options.getHeelPriceCalculator().getHeelPrice(startHeelVolumeInM3, portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot().getPort());
					for (int i = 0; i < sequence.length; i++) {
						resultPerMMBtu[i] = pricePerMMBTu;
					}
					return resultPerMMBtu;
				}
			}

			// NBO priced into start heel
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
			lngValuePerMMBTu = dischargeSlot.getDischargePriceCalculator().estimateSalesUnitPrice(dischargeSlot, portTimesRecord, null);

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
	 */
	@Override
	public final int calculateVoyagePlan(final VoyagePlan voyagePlan, final IVessel vessel, final long[] startHeelRangeInM3, final int baseFuelPricePerMT, final IPortTimesRecord voyageRecord,
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

		final long[] fuelConsumptions = calculateVoyagePlanFuelConsumptions(vessel, sequence);

		/**
		 * Accumulates route costs due to canal decisions.
		 */
		long routeCostAccumulator = 0;

		// The last voyage details in sequence.
		VoyageDetails lastVoyageDetailsElement = null;

		int voyageTime = 0;
		// add up route cost and find the last voyage details element
		for (int i = 0; i < sequence.length; ++i) {
			final IDetailsSequenceElement element = sequence[i];
			if (element instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) element;
				// add route cost
				routeCostAccumulator += details.getOptions().getRouteCost();

				voyageTime += details.getTravelTime();
				voyageTime += details.getIdleTime();

				lastVoyageDetailsElement = details;
			}
		}

		int violationsCount = 0;

		// If load or discharge has been set, then the other must be too.
		assert ((loadIdx == -1) == (dischargeIdx == -1));

		// the LNG which will be required to complete the sequence, including
		// NBO, FBO and any safety heel if travelling on NBO
		long lngCommitmentInM3;

		// the LNG which would by default be consumed for the given fuel usage choices during travel & idle
		lngCommitmentInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];

		// Load/Discharge sequence
		if ((loadIdx != -1) && (dischargeIdx != -1)) {
			final PortDetails loadDetails = (PortDetails) sequence[loadIdx];
			final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
			// Cargo should have fixed input heel range
			assert startHeelRangeInM3[0] == startHeelRangeInM3[1];
			voyagePlan.setStartingHeelInM3(startHeelRangeInM3[0]);

			// Store this value now as we may change it below during the heel calculations
			voyagePlan.setLNGFuelVolume(lngCommitmentInM3);

			// Any violations after this point are slot constraint violations

			final long cargoCapacityInM3 = vessel.getCargoCapacity();

			// Determine how this voyage plan ends. Warm or cold (and with our without a heel)

			final int endHeelState = getExpectedCargoEndHeelState(vesselClass.getWarmupTime(), lastVoyageDetailsElement);

			final long[] expectedRemainingHeelRangeInM3_Plus_Safety_Heel = calculateCargoEndHeelRange(vesselClass, lastVoyageDetailsElement, endHeelState);
			final long heelLevelInM3 = (endHeelState == STATE_COLD_MIN_HEEL || endHeelState == STATE_COLD_NO_VOYAGE) ? expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] : 0L;

			// Set our minimum heel here. Volume allocator may increase this in the allocation annotation.
			voyagePlan.setRemainingHeelInM3(heelLevelInM3);

			final PortDetails dischargeDetails = (PortDetails) sequence[dischargeIdx];
			final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

			// Only index 0 and 1 used for expectedRemainingHeelRangeInM3_Plus_Safety_Heel
			// FIXME: Does not correctly work with LDD
			final int returnedViolations = checkCargoCapacityViolations(startHeelRangeInM3[0], lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, heelLevelInM3,
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel, sequence);
			if (returnedViolations == Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}
			violationsCount += returnedViolations;
			// assert lngCommitmentInM3 <= cargoCapacityInM3;
		} else {
			final int endHeelState = getExpectedCargoEndHeelState(vesselClass.getWarmupTime(), lastVoyageDetailsElement);

			final int returnedViolations = calculateNonCargoEndState(voyagePlan, lastVoyageDetailsElement, voyageTime, startHeelRangeInM3, lngCommitmentInM3, endHeelState,
					vesselClass.getSafetyHeel());
			if (returnedViolations == Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}
			violationsCount += returnedViolations;
		}

		// Sanity checks
		assert lngCommitmentInM3 >= 0;

		final List<Integer> loadIndices = findLoadIndices(sequence);
		final List<Integer> dischargeIndices = findDischargeIndices(sequence);

		// Process details, filling in LNG prices
		// TODO: I don't really like altering the details at this stage of
		// processing, but this is where the information is being processed.
		// Can this be moved into the scheduler? If so, we need to ensure the
		// same price is used in all valid voyage legs.
		final int[] pricesPerMMBTu = getLngEffectivePrices(loadIndices, dischargeIndices, voyageRecord, voyagePlan.getStartingHeelInM3(), sequence);

		// set the LNG values for the voyages
		// final int numVoyages = sequence.length / 2;
		IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot = null;
		final int offset = sequence.length > 1 ? 1 : 0;
		for (int i = 0; i < sequence.length - offset; ++i) {
			final Object element = sequence[i];
			if (element instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) element;
				final int cargoCVValue = details.getOptions().getCargoCVValue();
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

				if (i == 0) {
					@NonNull
					final PortOptions options = details.getOptions();
					@NonNull
					final IPortSlot portSlot = options.getPortSlot();
					if (portSlot instanceof IHeelOptionSupplierPortSlot) {
						heelOptionSupplierPortSlot = (IHeelOptionSupplierPortSlot) portSlot;
					}

				}

				// NO LNG Consumption in Port
				for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {

					details.setFuelUnitPrice(fc, baseFuelPricePerMT);
					// Sum up the voyage costs - breaks ITS
					// final long consumptionInMT = details.getFuelConsumption(fc);
					// final long currentTotal = voyagePlan.getTotalFuelCost(fc);
					// voyagePlan.setTotalFuelCost(fc, currentTotal + Calculator.costFromConsumption(consumptionInMT, baseFuelPricePerMT));
				}

				final int cargoCVValue = details.getOptions().getCargoCVValue();
				for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {

					// Existing consumption data is in M3, also store the MMBtu values
					final long consumptionInM3 = details.getFuelConsumption(fc, FuelUnit.M3);
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
			}
		}

		// Store results in plan
		voyagePlan.setSequence(sequence);

		// Calculate cost of start heel
		if (heelOptionSupplierPortSlot != null) {

			final int pricePerMMBTU = heelOptionSupplierPortSlot.getHeelOptionsSupplier().getHeelPriceCalculator().getHeelPrice(voyagePlan.getStartingHeelInM3(), voyageRecord.getFirstSlotTime(),
					voyageRecord.getFirstSlot().getPort());
			final int cv = heelOptionSupplierPortSlot.getHeelOptionsSupplier().getHeelCVValue();

			final long startHeelInMMBTU = Calculator.convertM3ToMMBTu(voyagePlan.getStartingHeelInM3(), cv);
			final long startHeelCost = Calculator.costFromConsumption(startHeelInMMBTU, pricePerMMBTU);
			voyagePlan.setStartHeelCost(startHeelCost);
		}

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

		final long cooldownCost = calculateCooldownCost(vesselClass, voyageRecord, sequence);
		// calculateCoolDown
		voyagePlan.setTotalFuelCost(FuelComponent.Cooldown, cooldownCost);

		voyagePlan.setTotalRouteCost(routeCostAccumulator);

		// Weight non cooldown violations heavier than cooldown
		violationsCount *= 2;
		// Check for cooldown violations and add to the violations count
		violationsCount += checkCooldownViolations(loadIdx, dischargeIdx, sequence);

		voyagePlan.setViolationsCount(violationsCount);
		return violationsCount;
	}

	protected int calculateNonCargoEndState(final @NonNull VoyagePlan voyagePlan, @Nullable final VoyageDetails lastVoyageDetailsElement, final int voyageDuration, final long[] startHeelRangeInM3,
			final long lngCommitmentInM3, final int expectedEndHeelState, final long safetyHeelInM3) {

		int violationsCount = 0;

		// was not a Cargo sequence
		long startHeelInM3 = startHeelRangeInM3[0];
		// Increase start heel to cover commitment (up to max value);
		if (startHeelInM3 - lngCommitmentInM3 < 0) {
			startHeelInM3 = Math.min(lngCommitmentInM3, startHeelRangeInM3[1]);
		}

		long remainingHeelInM3 = startHeelInM3 - lngCommitmentInM3;

		// Set initial values. We may change with checks below
		voyagePlan.setStartingHeelInM3(startHeelInM3);
		voyagePlan.setLNGFuelVolume(lngCommitmentInM3);
		voyagePlan.setRemainingHeelInM3(remainingHeelInM3);

		// Three cases;
		// A) we have lng, but need more than is available (worst)
		// B) We have lng, but decided not to use it
		// C) We have lng and used some or all of it (best)

		// I.e. we want to force heel use if it is physically possible,

		// if our fuel requirements exceed our onboard fuel
		if (remainingHeelInM3 < 0) {
			// Not enough LNG - infeasible solution. Increase start heel to make maths work, but will be a MAX_HEEL violation.
			voyagePlan.setStartingHeelInM3(lngCommitmentInM3);
			voyagePlan.setRemainingHeelInM3(0);
			remainingHeelInM3 = 0;
			startHeelInM3 = lngCommitmentInM3;
			violationsCount += 300;
			return violationsCount;
		}

		if (voyageDuration > 0 && lngCommitmentInM3 == 0 && startHeelRangeInM3[0] > 0) {
			// Voyage option selected without any LNG use. But heel is available. This is a bad combination.

			// Update state
			voyagePlan.setStartingHeelInM3(0);
			voyagePlan.setRemainingHeelInM3(0);

			// if (!SchedulerConstants.HEEL_COMPAT_MODE) {
			return 100;

			// } else {
			// // Compatibility code path
			// startHeelInM3 = 0;
			// remainingHeelInM3 = 0;
			// violationsCount++;
			// }
		}

		if (lastVoyageDetailsElement != null) {
			final IPortSlot toPortSlot = lastVoyageDetailsElement.getOptions().getToPortSlot();
			if (toPortSlot instanceof IHeelOptionConsumerPortSlot) {
				final IHeelOptionConsumerPortSlot endPortSlot = (IHeelOptionConsumerPortSlot) toPortSlot;
				// TODO: Tricky here to get exact fuel volume, should there be some tolerance?
				VesselTankState endHeelState = endPortSlot.getHeelOptionsConsumer().getExpectedTankState();

				// If we have an either state, we convert to a WARM or COLD check depending on how much heel we have.
				if (endHeelState == VesselTankState.EITHER) {
					if (remainingHeelInM3 == 0) {
						endHeelState = VesselTankState.MUST_BE_WARM;
					} else {
						endHeelState = VesselTankState.MUST_BE_COLD;
					}
				}

				if (endHeelState == VesselTankState.MUST_BE_WARM) {
					if (remainingHeelInM3 > 0) {
						++violationsCount;
					}
				} else if (endHeelState == VesselTankState.MUST_BE_COLD) {

					if (remainingHeelInM3 > endPortSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3()) {
						++violationsCount;
					} else if (remainingHeelInM3 < endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3()) {
						// Current heel is too low for the end requirement. Can we increase our start heel?
						final long spareStartHeelInM3 = (voyageDuration > 0 && lngCommitmentInM3 == 0) ? 0 : Math.max(0, startHeelRangeInM3[1] - startHeelInM3);
						if (endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3() - remainingHeelInM3 < spareStartHeelInM3) {
							// Plenty of extra heel available.
							final long extraStartHeelInM3 = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3() - remainingHeelInM3;

							// Increase the start heel accordingly.
							startHeelInM3 += extraStartHeelInM3;
							voyagePlan.setStartingHeelInM3(startHeelInM3);

							// Increase to min required end state
							remainingHeelInM3 += extraStartHeelInM3;
							assert remainingHeelInM3 == endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
							voyagePlan.setRemainingHeelInM3(remainingHeelInM3);
						} else {
							// Increase start heel to limit..
							startHeelInM3 += spareStartHeelInM3;
							voyagePlan.setStartingHeelInM3(startHeelInM3);
							remainingHeelInM3 += spareStartHeelInM3;
							voyagePlan.setRemainingHeelInM3(remainingHeelInM3);

							// but still flag violation.
							++violationsCount;
						}
					}
				}
			} else {
				// Dummy data can be negative
				assert safetyHeelInM3 >= 0;
				assert expectedEndHeelState >= 0;
				// Try and end with safety heel
				if (expectedEndHeelState == STATE_COLD_MIN_HEEL) {
					final long spareStartHeelInM3 = (voyageDuration > 0 && lngCommitmentInM3 == 0) ? 0 : Math.max(0, startHeelRangeInM3[1] - startHeelInM3);
					final long extraStartHeelInM3 = Math.min(spareStartHeelInM3, safetyHeelInM3);

					startHeelInM3 += extraStartHeelInM3;
					voyagePlan.setStartingHeelInM3(startHeelInM3);
					remainingHeelInM3 += extraStartHeelInM3;
					voyagePlan.setRemainingHeelInM3(remainingHeelInM3);

				}
			}
		}

		return violationsCount;
	}

	private long[] calculateCargoEndHeelRange(final IVesselClass vesselClass, final VoyageDetails lastVoyageDetailsElement, final int endHeelState) {
		// Determine expected end heel range and minimum required heel. Last index is safetyHeel.
		// Bit of a hacky faff, but this allows a single return from the method and we only use index 0 and 1 in range checks
		final long[] expectedRemainingHeelRangeInM3_Plus_Safety_Heel = new long[3];

		expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = vesselClass.getSafetyHeel();

		final IPortSlot toPortSlot = lastVoyageDetailsElement.getOptions().getToPortSlot();
		if (toPortSlot instanceof IHeelOptionConsumerPortSlot) {
			final IHeelOptionConsumerPortSlot endPortSlot = (IHeelOptionConsumerPortSlot) toPortSlot;
			if (endPortSlot.getHeelOptionsConsumer().getExpectedTankState() == VesselTankState.MUST_BE_COLD) {
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = endPortSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3();
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();

			} else if (endPortSlot.getHeelOptionsConsumer().getExpectedTankState() == VesselTankState.MUST_BE_WARM) {
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = 0;
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = 0;
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = 0;
			} else {
				assert (endPortSlot.getHeelOptionsConsumer().getExpectedTankState() == VesselTankState.EITHER);
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
				if (endHeelState == STATE_WARM) {
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = 0;
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = 0;
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = 0;
				} else {
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = endPortSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3();
				}
			}
		} else if (toPortSlot.getPortType() == PortType.DryDock || toPortSlot.getPortType() == PortType.Maintenance) {
			expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = 0;
			expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = 0;
			// if (endHeelState == STATE_WARM) {
			expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = 0;
			// }
		} else {
			if (endHeelState == STATE_COLD_MIN_HEEL) {
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = vesselClass.getSafetyHeel();
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = vesselClass.getSafetyHeel();
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = vesselClass.getSafetyHeel();
			} else {
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = 0;
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = 0;
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = 0;
			}
		}
		return expectedRemainingHeelRangeInM3_Plus_Safety_Heel;
	}

	protected int getExpectedCargoEndHeelState(final int vesselWarmupTime, final VoyageDetails lastVoyageDetailsElement) {

		int totalVoyageTime = 0;
		if (lastVoyageDetailsElement != null) {
			totalVoyageTime = lastVoyageDetailsElement.getTravelTime() + lastVoyageDetailsElement.getIdleTime();
		}

		final int endHeelState;
		if (lastVoyageDetailsElement != null) {
			if (lastVoyageDetailsElement.isCooldownPerformed()) {
				// Cooldown performed. Must be cold now...
				endHeelState = STATE_COLD_COOLDOWN;
			} else if (lastVoyageDetailsElement.getOptions().isWarm()) {
				// No cooldown performed and we started warm, so still must be warn
				endHeelState = STATE_WARM;
			} else if (totalVoyageTime == 0) {
				endHeelState = STATE_COLD_NO_VOYAGE;
			} else {
				assert !lastVoyageDetailsElement.getOptions().isWarm();
				assert !lastVoyageDetailsElement.isCooldownPerformed();
				assert totalVoyageTime > 0;

				// We have started the voyage cold. Was there time to warm up?
				int warmingHours = 0;
				{
					// Check the warming up time.
					if (lastVoyageDetailsElement.getIdleTime() > 0) {
						if (lastVoyageDetailsElement.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3) == 0) {
							warmingHours += lastVoyageDetailsElement.getIdleTime();
						}
					}
					if (lastVoyageDetailsElement.getTravelTime() > 0) {
						if (lastVoyageDetailsElement.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3) == 0) {
							warmingHours += lastVoyageDetailsElement.getTravelTime();
						}
					}
				}
				if (warmingHours > 0) {
					if (warmingHours > vesselWarmupTime) {
						// Spent too long empty, so we are now warm
						endHeelState = STATE_WARM;
					} else {
						// We are empty, but within our grace time
						endHeelState = STATE_COLD_WARMING_TIME;
					}
				} else {
					// Have arrived with our minimum heel requirement.
					endHeelState = STATE_COLD_MIN_HEEL;
				}
			}
		} else {
			endHeelState = STATE_COLD_NO_VOYAGE;
		}
		return endHeelState;
	}

	protected int checkCargoCapacityViolations(final long startHeelInM3, final long lngCommitmentInM3, final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final long cargoCapacityInM3,
			final long heelLevelInM3, final long[] remainingHeelRangeInM3, final IDetailsSequenceElement[] sequence) {

		// TODO: Needs fixing for LDD!
		// NOTE: Strict checking may be too much for actuals

		int violationsCount = 0;
		final long minLoadVolumeInM3 = loadSlot.getMinLoadVolume();
		long maxLoadVolumeInM3 = loadSlot.getMaxLoadVolume();
		final long minDischargeVolumeInM3 = dischargeSlot.getMinDischargeVolume(loadSlot.getCargoCVValue());
		long maxDischargeVolumeInM3 = dischargeSlot.getMaxDischargeVolume(loadSlot.getCargoCVValue());

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
		if (minLoadVolumeInM3 - lngCommitmentInM3 + startHeelInM3 - heelLevelInM3 > maxDischargeVolumeInM3) {
			/*
			 * note - this might not be a genuine violation since rolling over the excess LNG may be permissible and in some cases it is even commercially desirable, but restrictions on LNG
			 * destination or complications from profit share contracts make it a potential violation, and we err on the side of caution
			 */

			// load breach -- need to load less than we are permitted
			++violationsCount;
		}
		// The load should cover at least the fuel usage plus the heel (or the min discharge, whichever is greater)
		if (minDischargeVolumeInM3 + heelLevelInM3 + lngCommitmentInM3 > upperLoadLimitInM3 + startHeelInM3) {
			++violationsCount;
		}

		if (heelLevelInM3 < remainingHeelRangeInM3[0] || heelLevelInM3 > remainingHeelRangeInM3[1]) {
			++violationsCount;
		}

		// Finally, track heel across voyages. If the ballast leg is short (i.e. within the warm up time) we could hit a case where we choose not to NBO on the ballast even though there is some heel
		// left after the discharge. For example if the start heel is greater than the max discharge, (Start Heel - laden NBO) > max discharge and ballast time < warm up time.
		if (sequence != null) {
			// Only expect sequence to be null from some unit tests

			long heelInM3 = startHeelInM3;
			for (final IDetailsSequenceElement e : sequence) {
				if (e instanceof PortDetails) {
					final PortDetails portDetails = (PortDetails) e;
					@NonNull
					final IPortSlot portSlot = portDetails.getOptions().getPortSlot();
					if (portSlot instanceof IDischargeOption) {
						final IDischargeOption dischargeOption = (IDischargeOption) portSlot;
						final int cargoCVValue = portDetails.getOptions().getCargoCVValue();
						heelInM3 -= dischargeOption.getMaxDischargeVolume(cargoCVValue);
					}
				} else if (e instanceof VoyageDetails) {
					final VoyageDetails voyageDetails = (VoyageDetails) e;
					long voyageNBOInM3 = 0;
					voyageNBOInM3 += voyageDetails.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3);
					voyageNBOInM3 += voyageDetails.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3);
					voyageNBOInM3 += voyageDetails.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3);

					if (voyageDetails.getOptions().getAvailableTime() > 0 && voyageNBOInM3 == 0 && heelInM3 > 0) {
						return Integer.MAX_VALUE;
					}

					heelInM3 -= voyageNBOInM3;
				}
			}
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

				// TODO: Original check also looked at the should be cold requirement - not really needed?
				if (!details.getOptions().getAllowCooldown() && details.isCooldownPerformed()) {
					++cooldownViolations;
				}
			} else {
				assert sequence[i] instanceof PortDetails;
			}
		}
		return cooldownViolations;
	}

	/**
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

				final VoyageDetails voyageDetails = new VoyageDetails(options);
				// Calculate voyage cost
				calculateVoyageFuelRequirements(options, voyageDetails);
				result.add(voyageDetails);
			} else if (element instanceof PortOptions) {
				final PortOptions options = ((PortOptions) element).clone();
				final PortDetails details = new PortDetails(options);
				calculatePortFuelRequirements(options, details);

				final long portCosts;
				final IPortSlot portSlot = options.getPortSlot();
				if (actualsDataProvider.hasActuals(portSlot)) {
					portCosts = actualsDataProvider.getPortCosts(portSlot);
				} else {
					portCosts = portCostProvider.getPortCost(portSlot.getPort(), options.getVessel(), portSlot.getPortType());
				}

				details.setPortCosts(portCosts);

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
		final long consumptionRateInMTPerDay;
		final long inPortNBORateInM3PerDay;

		final PortType portType = options.getPortSlot().getPortType();

		// temporary kludge: ignore non-load non-discharge ports for port consumption
		if (portType == PortType.Load || portType == PortType.Discharge) {
			consumptionRateInMTPerDay = vesselClass.getInPortConsumptionRateInMTPerDay(portType);

			if (portType == PortType.Load) {
				inPortNBORateInM3PerDay = vesselClass.getInPortNBORate(VesselState.Laden);
			} else {
				inPortNBORateInM3PerDay = vesselClass.getInPortNBORate(VesselState.Ballast);
			}

		} else if (portType == PortType.End) {
			// Maybe include hotel load for end events?
			// consumptionRateInMTPerDay = vesselClass.getIdleConsumptionRate(VesselState.Ballast);
			consumptionRateInMTPerDay = 0;
			inPortNBORateInM3PerDay = 0;
		} else {
			consumptionRateInMTPerDay = 0;
			inPortNBORateInM3PerDay = 0;
		}

		final int visitDuration = options.getVisitDuration();

		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consumptionRateInMTPerDay, visitDuration) / 24L;

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vesselClass.getMinBaseFuelConsumptionInMTPerDay(), visitDuration) / 24L;
		if (minBaseFuelConsumptionInMT > requiredConsumptionInMT) {
			details.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, minBaseFuelConsumptionInMT);
		} else {
			details.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, requiredConsumptionInMT);
		}

		// inPortBO
		final long NBOinM3 = Calculator.quantityFromRateTime(inPortNBORateInM3PerDay, visitDuration) / 24L;
		details.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, NBOinM3);

	}

	/**
	 * API for unit testing
	 */
	public void setPortCooldownProvider(IPortCooldownDataProvider portCooldownDataProvider) {
		this.portCooldownDataProvider = portCooldownDataProvider;
	}

	/**
	 * API for unit testing
	 */
	public void setRouteCostDataComponentProvider(final IRouteCostProvider provider) {
		this.routeCostProvider = provider;
	}

	/**
	 * API for unit testing
	 */
	public void setPortCVProvider(final IPortCVProvider portCVProvider) {
		this.portCVProvider = portCVProvider;
	}

	/**
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
