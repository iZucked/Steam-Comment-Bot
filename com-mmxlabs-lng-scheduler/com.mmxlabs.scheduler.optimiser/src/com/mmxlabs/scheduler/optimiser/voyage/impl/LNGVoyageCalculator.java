/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.MaintenanceVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCooldownDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IScheduledPurgeProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IdleFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan.VoyagePlanMetrics;

/**
 * Implementation of {@link ILNGVoyageCalculator}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGVoyageCalculator implements ILNGVoyageCalculator {

	// Constants to indicate the state of the vessel (without a specific heel level
	// at this point..)
	public static final int STATE_WARM = 0; // Vessel ends warm
	public static final int STATE_COLD_MIN_HEEL = 1; // Vessel ends cold with min heel range.
	public static final int STATE_COLD_WARMING_TIME = 2; // Vessel ends cold as with warming time. Zero heel.
	public static final int STATE_COLD_COOLDOWN = 3; // Vessel ends cold due to cooldown. Zero heel
	public static final int STATE_COLD_NO_VOYAGE = 4; // Vessel ends cold. There was no voyage. We are still at discharge port. Can
														// pick heel...

	@Inject
	@Named(SchedulerConstants.COMMERCIAL_VOLUME_OVERCAPACITY)
	private boolean adjustForLoadPortBOG;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IPortCVProvider portCVProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IPortCostProvider portCostProvider;

	@Inject
	private IScheduledPurgeProvider scheduledPurgeProvider;

	@Inject
	private IPortCooldownDataProvider portCooldownDataProvider;

	// See default binding in LNGTransformerModule
	@Inject
	@Named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)
	private int hoursBeforeCooldownsNoLongerForced;

	@Inject
	@Named(SchedulerConstants.Key_SchedulePurges)
	private boolean purgeSchedulingEnabled;

	public void setTimeZoneToUtcOffsetProvider(final ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider) {
		this.timeZoneToUtcOffsetProvider = timeZoneToUtcOffsetProvider;
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
	public final long calculateVoyageFuelRequirements(final VoyageOptions options, final VoyageDetails output, long nboAvailableInM3) {

		final long nboAvailableInM3AtStartOfMethod = nboAvailableInM3;

		output.setOptions(options);

		final IVessel vessel = options.getVessel();
		final VesselState vesselState = options.getVesselState();

		// Get distance for the route choice
		final long distance = options.getDistance();

		/**
		 * How much of the time given to us by the scheduler has to be spent travelling by an alternative route.
		 */
		final int additionalRouteTimeInHours = routeCostProvider.getRouteTransitTime(options.getRoute(), vessel);
		final int extraIdleTime = options.getExtraIdleTime();

		/**
		 * How much time is available to cover the distance, excluding time which must be spent traversing any canals
		 */
		final int availableTimeInHours = options.getAvailableTime() - additionalRouteTimeInHours - extraIdleTime;

		// Calculate the appropriate speed
		final int speed = calculateSpeed(options, availableTimeInHours);
		output.setSpeed(speed);

		// Check purge
		int purgeTime = 0;

		if (purgeSchedulingEnabled) {
			if (options.getFromPortSlot().getPortType() == PortType.DryDock) {
				purgeTime = vessel.getPurgeTime();
				output.setPurgePerformed(true);
				output.setPurgeDuration(purgeTime);
			} else if (options.getToPortSlot().getPortType() == PortType.Load) {
				if (scheduledPurgeProvider.isPurgeScheduled(options.getToPortSlot())) {
					purgeTime = vessel.getPurgeTime();
					output.setPurgePerformed(true);
					output.setPurgeDuration(purgeTime);
				}
			}
		}

		// Calculate total, travel and idle time

		// May be longer than available time
		final int travelTimeInHours = speed == 0 ? 0 : Calculator.getTimeFromSpeedDistance(speed, distance);
		// If idle time is negative, then there is not enough time for this voyage! This
		// should be caught by the caller
		final int idleTimeInHours = extraIdleTime + Math.max(0, availableTimeInHours - travelTimeInHours) - purgeTime;

		// We output travel time + canal time, but the calculations
		// below only need to care about travel time
		output.setTravelTime(travelTimeInHours + additionalRouteTimeInHours);
		output.setIdleTime(idleTimeInHours);

		// assert travelTimeInHours + additionalRouteTimeInHours + idleTimeInHours ==
		// options.getAvailableTime();
		// Route Additional Consumption
		/**
		 * Base fuel requirement for canal traversal
		 */
		calculateRouteAdditionalFuelRequirements(options, output, vessel, vesselState, additionalRouteTimeInHours, nboAvailableInM3);

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vessel.getMinBaseFuelConsumptionInMTPerDay(), additionalRouteTimeInHours) / 24L;
		if (options.getTravelFuelChoice() != TravelFuelChoice.BUNKERS && output.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()) > minBaseFuelConsumptionInMT) {
			// Ran out of LNG in canal travel, calculate travel (which should also run out but has run dry logic) first
			output.setTravelNBOHours(0); // Reset NBO hours
			calculateTravelFuelRequirements(options, output, vessel, vesselState, travelTimeInHours, speed, nboAvailableInM3);
			if (nboAvailableInM3 != Long.MAX_VALUE) { // Keep Max_Value at Max_Value
				nboAvailableInM3 -= output.getFuelConsumption(LNGFuelKeys.NBO_In_m3);
				nboAvailableInM3 -= output.getFuelConsumption(LNGFuelKeys.FBO_In_m3);
			}
			calculateRouteAdditionalFuelRequirements(options, output, vessel, vesselState, additionalRouteTimeInHours, nboAvailableInM3);
			if (nboAvailableInM3 != Long.MAX_VALUE) { // Keep Max_Value at Max_Value
				nboAvailableInM3 -= output.getRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3);
				nboAvailableInM3 -= output.getRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3);
			}

		} else {
			// Did not run out of LNG in the canal calculation - update available NBO and calculate travel fuel consumption
			if (nboAvailableInM3 != Long.MAX_VALUE) { // Keep Max_Value at Max_Value
				nboAvailableInM3 -= output.getRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3);
				nboAvailableInM3 -= output.getRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3);
			}

			// Calculate fuel requirements for travel time
			calculateTravelFuelRequirements(options, output, vessel, vesselState, travelTimeInHours, speed, nboAvailableInM3);
			if (nboAvailableInM3 != Long.MAX_VALUE) { // Keep Max_Value at Max_Value
				nboAvailableInM3 -= output.getFuelConsumption(LNGFuelKeys.NBO_In_m3);
				nboAvailableInM3 -= output.getFuelConsumption(LNGFuelKeys.FBO_In_m3);
			}
		}

		// Calculate fuel requirements for an idle time
		calculateIdleFuelRequirements(options, output, vessel, vesselState, idleTimeInHours, nboAvailableInM3);
		if (nboAvailableInM3 != Long.MAX_VALUE) { // Keep Max_Value at Max_Value
			nboAvailableInM3 -= output.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);
		}

		// Check cooldown
		output.setCooldownPerformed(false);
		if (options.shouldBeCold() == VesselTankState.MUST_BE_COLD) {

			// Work out how long we have been warming up
			long warmingHours = options.getAvailableTime();
			warmingHours -= output.getTravelNBOHours();
			warmingHours -= output.getRouteAdditionalNBOHours();
			warmingHours -= output.getIdleNBOHours();

			if (options.isWarm() || (warmingHours > vessel.getWarmupTime())) {
				final long cooldownVolume = vessel.getCooldownVolume();
				output.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, cooldownVolume);
				output.setCooldownPerformed(true);
			} else if (false && (warmingHours == 0 && nboAvailableInM3 <= 0 && nboAvailableInM3AtStartOfMethod > 0)) {
				// This is an extra clause to see if we have arrived with exactly 0 NBO left, but started out with NBO
				// This is to avoid arriving empty but avoiding a cooldown. Although strictly ok considering warming time rules, but
				// also looks odd in some cases!
				final long cooldownVolume = vessel.getCooldownVolume();
				output.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, cooldownVolume);
				output.setCooldownPerformed(true);
			}
		}
		return nboAvailableInM3;
	}

	protected final void calculateRouteAdditionalFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVessel vessel, final VesselState vesselState,
			final int additionalRouteTimeInHours, final long nboAvailableInM3) {

		final IBaseFuel baseFuel = vessel.getTravelBaseFuel();

		final int equivalenceFactorMMBTuToMT = baseFuel.getEquivalenceFactor();

		final long routeRequiredConsumptionInMT = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(options.getRoute(), vessel, vesselState), additionalRouteTimeInHours) / 24L;
		final int cargoCVValue = options.getCargoCVValue();

		//Reset the field
		output.setRouteAdditionalNBOHours(0);

		if (routeRequiredConsumptionInMT > 0) {

			// if no nboAvailable, have to use bunkers irrespective of fuel choice
			if (options.getTravelFuelChoice() != TravelFuelChoice.BUNKERS && nboAvailableInM3 > 0) {

				final long nboRouteRateInM3PerDay = routeCostProvider.getRouteNBORate(options.getRoute(), vessel, vesselState);

				/**
				 * How much NBO is produced while in the canal (M3)
				 */
				final long routeNboRequestedInM3 = Calculator.quantityFromRateTime(nboRouteRateInM3PerDay, additionalRouteTimeInHours) / 24L;

				int nboHours= additionalRouteTimeInHours;
				boolean ranDry = false;
				if (routeNboRequestedInM3 > nboAvailableInM3) {
					// Ran dry
					nboHours = Calculator.getTimeFromRateQuantity(nboRouteRateInM3PerDay, nboAvailableInM3 * 24L);
					ranDry = true;

				}

				output.setIdleNBOHours(nboHours);
				
				// Check that we actually have enough lng to make the NBO journey
				final long routeNboProvidedInM3 = routeNboRequestedInM3 <= nboAvailableInM3 ? routeNboRequestedInM3 : nboAvailableInM3;
				/**
				 * How much NBO is produced while in the canal (MTBFE)
				 */

				final long routeNboProvidedInMT = Calculator.convertM3ToMT(routeNboProvidedInM3, cargoCVValue, equivalenceFactorMMBTuToMT);

				final long leftoverLngInM3 = nboAvailableInM3 - routeNboProvidedInM3;

				final long leftoverNboAvailableInM3 = nboAvailableInM3 - routeNboProvidedInM3;
				final long leftoverNboAvailableInMT = Calculator.convertM3ToMT(leftoverNboAvailableInM3, cargoCVValue, equivalenceFactorMMBTuToMT);
				/**
				 * How much FBO is produced while in the canal (M3)
				 */
				long routeFboProvidedInM3 = 0;
				/**
				 * How much FBO is produced while in the canal (MTBFE)
				 */
				long routeFboProvidedInMT = 0;
				long supplementalBaseInMT = 0;

				/**
				 * Consumed pilot light
				 */
				long pilotLightConsumptionInMT = 0;

				boolean usePilotLight = false;
				/**
				 * How much supplement is needed over and above NBO while in the canal (in MT)
				 */
				final long routeDiffInMT = routeRequiredConsumptionInMT - routeNboProvidedInMT;

				if (routeDiffInMT > 0) {
					// Need to supplement
					if (options.getTravelFuelChoice() == TravelFuelChoice.NBO_PLUS_FBO) {
						routeFboProvidedInMT = routeDiffInMT;
						routeFboProvidedInM3 = Calculator.convertMTToM3(routeFboProvidedInMT, cargoCVValue, equivalenceFactorMMBTuToMT);
						if (routeFboProvidedInM3 > leftoverLngInM3) {
							// We do not have enough lng to make the journey
							// Use up remainder of lng and use base fuel
							final long routeFboActualInM3 = leftoverLngInM3;
							final long routeFboActualInMT = Calculator.convertM3ToMT(routeFboActualInM3, cargoCVValue, equivalenceFactorMMBTuToMT);
							routeFboProvidedInM3 = routeFboActualInM3;
							routeFboProvidedInMT = routeFboActualInMT;
							final long leftoverDiffInMT = routeDiffInMT - routeFboProvidedInMT;
							assert leftoverDiffInMT > 0;
							supplementalBaseInMT = leftoverDiffInMT;
						} else {
							usePilotLight = true;
						}
					} else {
						assert options.getTravelFuelChoice() == TravelFuelChoice.NBO_PLUS_BUNKERS;

						// Reliq vessels should not use base fuel supplement.
						assert (!vessel.hasReliqCapability());

						supplementalBaseInMT = routeDiffInMT;
					}

				} else {
					// ...therefore will be pure NBO regardless of fuel choice
					assert routeNboProvidedInMT >= routeRequiredConsumptionInMT;

					// NB: Still need pilot light, if running on only NBO as not using bunkers.
					usePilotLight = true;
				}

				if (usePilotLight) {
					final long pilotLightRateInMTPerDay = vessel.getPilotLightRate();
					if (pilotLightRateInMTPerDay > 0) {
						pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateInMTPerDay, additionalRouteTimeInHours) / 24L;
					}
				}

				output.setRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3, routeNboProvidedInM3);
				output.setRouteAdditionalConsumption(LNGFuelKeys.NBO_In_MT, routeNboProvidedInMT);
				output.setRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3, routeFboProvidedInM3);
				output.setRouteAdditionalConsumption(LNGFuelKeys.FBO_In_MT, routeFboProvidedInMT);
				output.setRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT(), supplementalBaseInMT);
				output.setRouteAdditionalConsumption(vessel.getPilotLightFuelInMT(), pilotLightConsumptionInMT);
				
				output.setRouteAdditionalNBOHours(nboHours);

			} else {
				// Base fuel only
				output.setRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT(), routeRequiredConsumptionInMT);
				output.setRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3, 0);
				output.setRouteAdditionalConsumption(LNGFuelKeys.NBO_In_MT, 0);
				output.setRouteAdditionalConsumption(LNGFuelKeys.NBO_In_MT, 0);
				output.setRouteAdditionalConsumption(LNGFuelKeys.FBO_In_MT, 0);
				output.setRouteAdditionalConsumption(vessel.getPilotLightFuelInMT(), 0);
				// Reset these values since they might be set on previous calls to this method
				output.setRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT(), 0);
				output.setRouteAdditionalConsumption(vessel.getPilotLightFuelInMT(), 0);
			}

		}

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vessel.getMinBaseFuelConsumptionInMTPerDay(), additionalRouteTimeInHours) / 24L;
		// Check if there is NBO available for which the pilot light would be needed
		if (options.getTravelFuelChoice() != TravelFuelChoice.BUNKERS && nboAvailableInM3 > 0) {
			if (output.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()) < minBaseFuelConsumptionInMT) {
				output.setRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT(), minBaseFuelConsumptionInMT);
			}
		} else {
			if (output.getRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT()) < minBaseFuelConsumptionInMT) {
				output.setRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT(), minBaseFuelConsumptionInMT);
			}
		}

	}

	protected final void calculateIdleFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVessel vessel, final VesselState vesselState, final int idleTimeInHours,
			final long nboAvailableInM3) {

		if (options.isCharterOutIdleTime()) {
			return;
		}

		final int equivalenceFactorMMBTuToMT = vessel.getTravelBaseFuel().getEquivalenceFactor();
		final int cargoCVValue = options.getCargoCVValue();
		final int minBaseFuelConsumptionInMTPerDay = vessel.getMinBaseFuelConsumptionInMTPerDay();
		final long idleNBORateInM3PerDay = vessel.getIdleNBORate(vesselState);

		int nboHours = 0;
		if (options.getIdleFuelChoice() == IdleFuelChoice.NBO) {

			final long nboRequiredInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerDay, idleTimeInHours) / 24L;
			nboHours = idleTimeInHours;
			boolean ranDry = false;
			if (nboRequiredInM3 > nboAvailableInM3) {
				// Ran dry
				nboHours = Calculator.getTimeFromRateQuantity(idleNBORateInM3PerDay, nboAvailableInM3 * 24L);
				ranDry = true;

			}

			output.setIdleNBOHours(nboHours);

			long nboInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerDay, nboHours) / 24L;
			// Totally legit, as nboAvailableInM3 is set to Long.MAX_VALUE SOMEWHERE
			if (ranDry && (nboInM3 < nboAvailableInM3 && nboAvailableInM3 != Long.MAX_VALUE)) {
				nboInM3 = nboAvailableInM3;
			}
			final long nboInMT = Calculator.convertM3ToMT(nboInM3, cargoCVValue, equivalenceFactorMMBTuToMT);

			output.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, nboInM3);
			output.setFuelConsumption(LNGFuelKeys.IdleNBO_In_MT, nboInMT);

			final long pilotLightRateINMTPerDay = vessel.getPilotLightRate();
			if (pilotLightRateINMTPerDay > 0) {
				final long pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerDay, nboHours) / 24L;
				output.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), pilotLightConsumptionInMT);
			}
		}

		final int baseHours = idleTimeInHours - nboHours;
		final long idleConsumptionInMT = baseHours == 0 ? 0 : Calculator.quantityFromRateTime(vessel.getIdleConsumptionRate(vesselState), baseHours) / 24L;
		final long minBaseConsumptionInMT = minBaseFuelConsumptionInMTPerDay == 0 ? 0 : Calculator.quantityFromRateTime(minBaseFuelConsumptionInMTPerDay, idleTimeInHours) / 24L;
		output.setFuelConsumption(vessel.getIdleBaseFuelInMT(), Math.max(idleConsumptionInMT, minBaseConsumptionInMT));
	}

	protected final void calculateTravelFuelRequirements(final VoyageOptions options, final VoyageDetails output, final IVessel vessel, final VesselState vesselState, final int travelTimeInHours,
			final int speed, final long nboAvailableInM3) {

		final IBaseFuel baseFuel = vessel.getTravelBaseFuel();
		final int equivalenceFactorMMBTuToMT = baseFuel.getEquivalenceFactor();

		final int cargoCVValue = options.getCargoCVValue();

		final long nboAvailableInMT = nboAvailableInM3 == Long.MAX_VALUE ? Long.MAX_VALUE : Calculator.convertM3ToMT(nboAvailableInM3, cargoCVValue, equivalenceFactorMMBTuToMT);

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this journey
		 */
		final long consumptionRateInMTPerDay = speed == 0 ? 0 : vessel.getConsumptionRate(vesselState).getRate(speed);

		final int minBaseFuelConsumptionInMTPerDay = vessel.getMinBaseFuelConsumptionInMTPerDay();

		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consumptionRateInMTPerDay, travelTimeInHours) / 24L;
		final long nboRateInM3PerDay = vessel.getNBORate(vesselState);
		int nboHours = 0;
		if (options.getTravelFuelChoice() != TravelFuelChoice.BUNKERS) {
			nboHours = travelTimeInHours;
			boolean ranDry = false;
			final long totalTravelTimeNBOInM3 = Calculator.quantityFromRateTime(nboRateInM3PerDay, nboHours) / 24L;
			if (totalTravelTimeNBOInM3 > nboAvailableInM3 // NBO Exceeds available quantity
					|| requiredConsumptionInMT > nboAvailableInMT) { // Not enough gas onboard to cover the fuel requirements
				// Ran dry
				if (options.getTravelFuelChoice() == TravelFuelChoice.NBO_PLUS_FBO) {
					final long nboRateInMTPerDay = Calculator.convertM3ToMT(nboRateInM3PerDay, cargoCVValue, equivalenceFactorMMBTuToMT);
					nboHours = Calculator.getTimeFromRateQuantity(Math.max(nboRateInMTPerDay, consumptionRateInMTPerDay), nboAvailableInMT * 24L);
				} else {
					nboHours = Calculator.getTimeFromRateQuantity(nboRateInM3PerDay, nboAvailableInM3 * 24L);
				}
				ranDry = true;
			}

			// Retain any canal nbo hours
			output.setTravelNBOHours(output.getTravelNBOHours() + nboHours);

			long nboInM3 = Calculator.quantityFromRateTime(nboRateInM3PerDay, nboHours) / 24L;

			final long nboInMT = Calculator.convertM3ToMT(nboInM3, cargoCVValue, equivalenceFactorMMBTuToMT);

			output.setFuelConsumption(LNGFuelKeys.NBO_In_m3, nboInM3);
			output.setFuelConsumption(LNGFuelKeys.NBO_In_MT, nboInMT);

			final long nboPerDayInMT = Calculator.convertM3ToMT(nboRateInM3PerDay, cargoCVValue, equivalenceFactorMMBTuToMT);
			final long baseConsumptionInMTForNBOHours = Calculator.quantityFromRateTime(consumptionRateInMTPerDay, nboHours) / 24L;

			boolean usePilotLight = false;
			final long diffInMT = baseConsumptionInMTForNBOHours - nboInMT;
			long fboInM3 = 0L;
			if (diffInMT > 0) {
				if (options.getTravelFuelChoice() == TravelFuelChoice.NBO_PLUS_FBO) {
					final long fboInMT = diffInMT;
					fboInM3 = Calculator.convertMTToM3(fboInMT, cargoCVValue, equivalenceFactorMMBTuToMT);

					output.setFuelConsumption(LNGFuelKeys.FBO_In_m3, fboInM3);
					output.setFuelConsumption(LNGFuelKeys.FBO_In_MT, fboInMT);
					usePilotLight = true;
				} else {
					if (nboPerDayInMT >= consumptionRateInMTPerDay) {
						// NBO + Base, but NBO is high, so no base used - need pilot light
						usePilotLight = true;
					} else {
						final long supplementalBaseInMT = diffInMT;
						output.setFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT(), supplementalBaseInMT);
					}
				}
			} else {
				usePilotLight = true;
			}

			if (ranDry && ((nboInM3 + fboInM3) < nboAvailableInM3 && nboAvailableInM3 != Long.MAX_VALUE)) {
				// Handle volume -> hours -> vol rounding.
				// Make sure we use up all the available LNG if we run dry.
				// Direct m3 -> mt conversion may be out a little.
				nboInM3 = nboAvailableInM3 - fboInM3;
				output.setFuelConsumption(LNGFuelKeys.NBO_In_m3, nboInM3);
			}

			if (usePilotLight) {
				final long pilotLightRateINMTPerDay = vessel.getPilotLightRate();
				if (pilotLightRateINMTPerDay > 0) {
					final long pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerDay, nboHours) / 24L;
					output.setFuelConsumption(vessel.getPilotLightFuelInMT(), pilotLightConsumptionInMT);
				}
			}
			if (minBaseFuelConsumptionInMTPerDay > 0) {
				final long supplemental = output.getFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT());
				final long minBaseConsumptionInMT = Calculator.quantityFromRateTime(minBaseFuelConsumptionInMTPerDay, nboHours) / 24L;
				if (minBaseConsumptionInMT > supplemental) {
					output.setFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT(), minBaseConsumptionInMT);
				}
			}
		}
		final int baseHours = travelTimeInHours - nboHours;
		if (baseHours > 0) {
			final long baseConsumptionInMT = Calculator.quantityFromRateTime(Math.max(consumptionRateInMTPerDay, minBaseFuelConsumptionInMTPerDay), baseHours) / 24L;
			output.setFuelConsumption(vessel.getTravelBaseFuelInMT(), baseConsumptionInMT);
		}
	}

	/**
	 * Calculate the slowest speed the vessel can travel at given the time, distance and NBO constraints.
	 * 
	 * @param options
	 * @param vessel
	 * @param distance
	 * @param availableTimeInHours
	 * @return
	 */
	protected final int calculateSpeed(final VoyageOptions options, final int availableTimeInHours) {

		final IVessel vessel = options.getVessel();
		final int distance = options.getDistance();

		// Calculate speed
		int speed;
		if (options.isCharterOutIdleTime()) {
			// If we are charting out the idle time, the fuel cost is not an issue!
			speed = vessel.getMaxSpeed();
		} else {
			speed = availableTimeInHours == 0 ? 0 : Calculator.speedFromDistanceTime(distance, availableTimeInHours);

			// speed calculation is not always correct - with a linear consumption
			// curve on base fuel for example, the best option is always either maximum
			// speed
			// or minimum speed.

			if (distance != 0) {
				if (availableTimeInHours == 0) {
					return vessel.getMaxSpeed();
				}
				// Check NBO speed
				if (options.getTravelFuelChoice() != TravelFuelChoice.BUNKERS) {
					final int nboSpeed = options.getNBOSpeed();
					if (speed < nboSpeed) {
						// Always go fast enough to consume all the NBO
						speed = nboSpeed;
					}
				}

				// Clamp speed to min bound
				final int minSpeed = vessel.getMinSpeed();
				if (speed < minSpeed) {
					speed = minSpeed;
				}

				// Clamp speed to max bound
				final int maxSpeed = vessel.getMaxSpeed();
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
	public long calculateLNGFuelConsumptions(final IVessel vessel, final IDetailsSequenceElement... sequence) {
		long lngInM3 = 0;

		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];

				// DO NOT INCLUDE THE LAST SEQUENCE ELEMENT IN THE TOTAL COST FOR THE PLAN
				if (sequence.length == 1 || i < sequence.length - 1) {
					for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
						lngInM3 += details.getFuelConsumption(fk);
					}
				}
			} else {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];
				for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
					lngInM3 += details.getFuelConsumption(fk);
					lngInM3 += details.getRouteAdditionalConsumption(fk);
				}
			}
		}
		return lngInM3;
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
	 * @param vessel
	 * @param arrivalTimes
	 * @param sequence
	 * @return
	 */
	public final long calculateCooldownCost(final IVessel vessel, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement... sequence) {
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
						cooldownCost = cooldownCalculator.calculateCooldownCost(vessel, cooldownPort, cooldownCV, cooldownTime);
					} else {
						// Trying to cooldown somewhere we are not allowed to cooldown (e.g. discharge
						// port at sequence end?)
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
	 * @param vessel
	 * @param loadIndices
	 * @param dischargeIndices
	 * @param arrivalTimes
	 * @param sequence
	 * @return A list of LNG prices, or null if there was no way to establish LNG prices.
	 */
	public final int[] getLngEffectivePrices(final IVessel vessel, final List<Integer> loadIndices, final List<Integer> dischargeIndices, final IPortTimesRecord portTimesRecord,
			final long startHeelVolumeInM3, final IDetailsSequenceElement... sequence) {
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
			lngValuePerMMBTu = dischargeSlot.getDischargePriceCalculator().estimateSalesUnitPrice(vessel, dischargeSlot, portTimesRecord, null);

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
	 * Given a sequence of {@link IPortDetails} interleaved with
	 * {@link VoyageDetails}, compute the total base fuel and LNG requirements,
	 * taking into account initial conditions, load and discharge commitments etc.
	 * It is assumed that the first and last {@link IPortDetails} will be a Loading
	 * slot or other slot where the vessel state is set to a known state.
	 * Intermediate slots are any other type of slot (e.g. one discharge, multiple
	 * waypoints, etc). If the first slot is a load slot, then this is the only
	 * reason we should see a discharge slot.
	 * 
	 * NOTE: this method intentionally ignores the last sequence element when
	 * calculating the total cost of a voyage plan, in order to avoid
	 * double-counting elements, since voyage plans are expected to come in A-B-C
	 * C-D-E E-F-G chains, where the last element of the chain does not get counted.
	 * 
	 * @param sequence
	 * @param dischargeTime
	 * @param loadTime
	 * @return Number of capacity constraints which had to be violated in the
	 *         allocation
	 */
	@Override
	public final long[] calculateVoyagePlan(final VoyagePlan voyagePlan, final IVessel vessel, final ICharterCostCalculator charterCostCalculator, final long[] startHeelRangeInM3,
			final int[] baseFuelPricesPerMT, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement... sequence) {
		/*
		 * TODO: instead of taking an interleaved List<Object> as a parameter, this
		 * would have a far more informative signature (and cleaner logic?) if it passed
		 * a list of IPortDetails and a list of VoyageDetails as separate parameters.
		 * Worth doing at some point?
		 */

		// Ensure odd number of elements
		assert (sequence.length % 2) == 1;

		final int loadIdx = findFirstLoadIndex(sequence);
		final int dischargeIdx = findFirstDischargeIndex(sequence);
		// sanityCheckVesselState(loadIdx, dischargeIdx, sequence);

		// the LNG which will be required to complete the sequence, including
		// NBO, FBO and any safety heel if travelling on NBO
		final long lngCommitmentInM3 = calculateLNGFuelConsumptions(vessel, sequence);

		/**
		 * Accumulates route costs due to canal decisions.
		 */
		long routeCostAccumulator = 0;

		// The last voyage details in sequence.
		VoyageDetails lastVoyageDetailsElement = null;

		final List<Triple<VoyageDetails, PortDetails, Integer>> maintenanceDetailsElements = new LinkedList<>();
		final LinkedList<Integer> maintenanceIndices = new LinkedList<>();

		boolean isDuringMaintenanceEvaluator = false;
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
			} else if (element instanceof PortDetails) {
				final PortDetails details = (PortDetails) element;
				if (details.getOptions().getPortSlot().getPortType() == PortType.Maintenance) {
					if (details.getOptions().getPortSlot() instanceof MaintenanceVesselEventPortSlot) {
						isDuringMaintenanceEvaluator = true;
					}
					if (i != sequence.length - 1) {
						maintenanceIndices.add(i);
						maintenanceDetailsElements.add(Triple.of(lastVoyageDetailsElement, details, voyageTime));
					}
				}
			}
		}
//		final long lngCommitmentInM3;
//		if (((PortDetails) sequence[sequence.length-1]).getOptions().getPortSlot() instanceof MaintenanceVesselEventPortSlot) {
//			final long maintenanceConsumption = ((MaintenanceVesselEventPortSlot) ((PortDetails) sequence[sequence.length-1]).getOptions().getPortSlot()).getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
//			lngCommitmentInM3 = nonEndLngCommitmentInM3 + maintenanceConsumption;
//		} else {
//			lngCommitmentInM3 = nonEndLngCommitmentInM3;
//		}

		long[] voyagePlanMetrics;

		// If load or discharge has been set, then the other must be too.
		assert ((loadIdx == -1) == (dischargeIdx == -1));

		boolean isCargo = false;
		// Load/Discharge sequence
		if ((loadIdx != -1) && (dischargeIdx != -1)) {
			isCargo = true;

			final PortDetails loadDetails = (PortDetails) sequence[loadIdx];
			final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
			// Cargo should have fixed input heel range
			// assert startHeelRangeInM3[0] == startHeelRangeInM3[1];
			voyagePlan.setStartingHeelInM3(startHeelRangeInM3[1]);

			// Store this value now as we may change it below during the heel calculations
			voyagePlan.setLNGFuelVolume(lngCommitmentInM3);

			// Any violations after this point are slot constraint violations

			final long cargoCapacityInM3 = vessel.getCargoCapacity();

			// Determine how this voyage plan ends. Warm or cold (and with our without a heel)
			final int endHeelState = getExpectedCargoEndHeelState(vessel.getWarmupTime(), lastVoyageDetailsElement);

			final long[] expectedRemainingHeelRangeInM3_Plus_Safety_Heel = calculateCargoEndHeelRange(vessel, lastVoyageDetailsElement, endHeelState);
 
			long heelLevelInM3 = (endHeelState == STATE_COLD_MIN_HEEL || endHeelState == STATE_COLD_NO_VOYAGE) ? expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] : 0L;
			if (heelLevelInM3 < 0) {
				heelLevelInM3 = 0;
			}
			// Set our minimum heel here. Volume allocator may increase this in the allocation annotation.
			voyagePlan.setRemainingHeelInM3(heelLevelInM3);

			final PortDetails dischargeDetails = (PortDetails) sequence[dischargeIdx];
			final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

			// Only index 0 and 1 used for expectedRemainingHeelRangeInM3_Plus_Safety_Heel
			// FIXME: Does not correctly work with LDD
			final long[] returnedViolations = checkCargoCapacityViolations(startHeelRangeInM3[0], lngCommitmentInM3, loadSlot, dischargeSlot, cargoCapacityInM3, heelLevelInM3,
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel, sequence);
			if (returnedViolations == null) {
				return null;
			}
			voyagePlanMetrics = returnedViolations;
		} else {
			final int endHeelState = getExpectedCargoEndHeelState(vessel.getWarmupTime(), lastVoyageDetailsElement);
			final int returnedViolations = calculateNonCargoEndState(voyagePlan, lastVoyageDetailsElement, voyageTime, startHeelRangeInM3, lngCommitmentInM3, endHeelState, vessel.getSafetyHeel());
			if (returnedViolations == Integer.MAX_VALUE) {
				return null;
			}
			
			voyagePlanMetrics = new long[VoyagePlan.VoyagePlanMetrics.values().length];
			voyagePlanMetrics[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()] += returnedViolations;
			
			if (!maintenanceIndices.isEmpty()) {
				final long[] beforeMaintenanceLNGCommitments = new long[maintenanceIndices.size()];
				long currentLNGCommitmentInM3 = lngCommitmentInM3;
				int j = beforeMaintenanceLNGCommitments.length - 1;
				Iterator<Integer> maintenanceIter = maintenanceIndices.descendingIterator();
				int currentMaintenanceIndex = maintenanceIter.next();
				for (int i = sequence.length - 2; i >= 0; --i) {
					IDetailsSequenceElement elem = sequence[i];
					if (elem instanceof PortDetails) {
						PortDetails details = (PortDetails) elem;
						for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
							currentLNGCommitmentInM3 -= details.getFuelConsumption(fk);
						}
					} else if (elem instanceof VoyageDetails) {
						VoyageDetails details = (VoyageDetails) elem;
						for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
							currentLNGCommitmentInM3 -= details.getFuelConsumption(fk);
							currentLNGCommitmentInM3 -= details.getRouteAdditionalConsumption(fk);
						}
					}
					if (i == currentMaintenanceIndex) {
						beforeMaintenanceLNGCommitments[j] = currentLNGCommitmentInM3;
						if (maintenanceIter.hasNext()) {
							--j;
							currentMaintenanceIndex = maintenanceIter.next();
						} else {
							break;
						}
					}
				}
				int[] expectedEndHeelStates = getExpectedMaintenanceEndHeelStates(maintenanceDetailsElements, vessel.getWarmupTime());
				final Iterator<Triple<VoyageDetails, PortDetails, Integer>> iterDetails = maintenanceDetailsElements.iterator();
				maintenanceIter = maintenanceIndices.iterator();
				for (int i = 0; i < beforeMaintenanceLNGCommitments.length; ++i) {
					currentMaintenanceIndex = maintenanceIter.next();
					Triple<VoyageDetails, PortDetails, Integer> currentTriple = iterDetails.next();
					final VoyagePlan placeHolderVP = new VoyagePlan();
					final VoyageDetails currentLastVoyageDetailsElement = currentTriple.getFirst();
					final int currentVoyageTime = currentTriple.getThird();
					currentLNGCommitmentInM3 = beforeMaintenanceLNGCommitments[i];
					int currentExpectedEndHeelState = expectedEndHeelStates[i];
					final int returnedMaintenanceViolations = calculateNonCargoEndState(placeHolderVP, currentLastVoyageDetailsElement, currentVoyageTime, startHeelRangeInM3, currentLNGCommitmentInM3,
							currentExpectedEndHeelState, vessel.getSafetyHeel());
					if (returnedMaintenanceViolations == Integer.MAX_VALUE) {
						return null;
					}
					
					voyagePlanMetrics[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()] += returnedMaintenanceViolations;
				}
			}
	
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
		final int[] pricesPerMMBTu = getLngEffectivePrices(vessel, loadIndices, dischargeIndices, portTimesRecord, voyagePlan.getStartingHeelInM3(), sequence);

		// set the LNG values for the voyages
		IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot = null;
		final int offset = sequence.length > 1 ? 1 : 0;
		for (int i = 0; i < sequence.length - offset; ++i) {
			final Object element = sequence[i];
			if (element instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) element;
				final int cargoCVValue = details.getOptions().getCargoCVValue();
				for (final FuelKey[] fkp : LNGFuelKeys.LNG_In_m3_mmBtu_Pair) {
					// Existing consumption data is in M3, also store the MMBtu values
					final long consumptionInM3 = details.getFuelConsumption(fkp[0]);
					final long additionalConsumptionInM3 = details.getRouteAdditionalConsumption(fkp[0]);
					final long consumptionInMMBTu = Calculator.convertM3ToMMBTu(consumptionInM3, cargoCVValue);
					final long additionalConsumptionInMMBTu = Calculator.convertM3ToMMBTu(additionalConsumptionInM3, cargoCVValue);
					details.setFuelConsumption(fkp[1], consumptionInMMBTu);
					details.setRouteAdditionalConsumption(fkp[1], additionalConsumptionInMMBTu);

					if (pricesPerMMBTu != null) {
						// Set the LNG unit price
						final int unitPrice = pricesPerMMBTu[i];
						details.setFuelUnitPrice(fkp[1].getFuelComponent(), unitPrice);
						// Sum up the voyage costs
						if (isCargo) {
							// Non-cargoes boil-off should be covered by heel payment.
							final long currentTotal = voyagePlan.getLngFuelCost();
							voyagePlan.setLngFuelCost(currentTotal + Calculator.costFromConsumption(consumptionInMMBTu + additionalConsumptionInMMBTu, unitPrice));
						}
					}
				}
				for (final FuelKey fk : vessel.getVoyageFuelKeys()) {

					final IBaseFuel bf = fk.getBaseFuel();
					if (bf != IBaseFuel.LNG) {
						final int unitPrice = baseFuelPricesPerMT[bf.getIndex()];
						details.setFuelUnitPrice(fk.getFuelComponent(), unitPrice);

						// Existing consumption data is in M3, also store the MMBtu values
						final long consumptionInMT = details.getFuelConsumption(fk);

						if (consumptionInMT != 0) {
							final long currentTotal = voyagePlan.getBaseFuelCost();
							voyagePlan.setBaseFuelCost(currentTotal + Calculator.costFromConsumption(consumptionInMT, unitPrice));
						}
					}
				}

				for (final FuelKey fk : vessel.getTravelFuelKeys()) {
					final IBaseFuel bf = fk.getBaseFuel();
					final int unitPrice = baseFuelPricesPerMT[bf.getIndex()];
					details.setFuelUnitPrice(fk.getFuelComponent(), unitPrice);

					// Existing consumption data is in M3, also store the MMBtu values
					final long consumptionInMT = details.getRouteAdditionalConsumption(fk);

					if (consumptionInMT != 0) {
						final long currentTotal = voyagePlan.getBaseFuelCost();
						voyagePlan.setBaseFuelCost(currentTotal + Calculator.costFromConsumption(consumptionInMT, unitPrice));
					}
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

				final int cargoCVValue = details.getOptions().getCargoCVValue();
				{
					// Existing consumption data is in M3, also store the MMBtu values
					final long consumptionInM3 = details.getFuelConsumption(LNGFuelKeys.NBO_In_m3);
					final long consumptionInMMBTu = Calculator.convertM3ToMMBTu(consumptionInM3, cargoCVValue);
					details.setFuelConsumption(LNGFuelKeys.NBO_In_mmBtu, consumptionInMMBTu);

					if (pricesPerMMBTu != null) {
						// Set the LNG unit price
						final int unitPrice = pricesPerMMBTu[i];
						details.setFuelUnitPrice(FuelComponent.NBO, unitPrice);
						// Sum up the voyage costs
						if (isCargo) {
							// Non-cargoes (currently) have no in-port boil-off, but if they did, it should
							// be covered by heel payment.
							final long currentTotal = voyagePlan.getLngFuelCost();
							voyagePlan.setLngFuelCost(currentTotal + Calculator.costFromConsumption(consumptionInMMBTu, unitPrice));
						}
					}
				}

				{
					final FuelKey fk = vessel.getInPortBaseFuelInMT();
					final IBaseFuel bf = fk.getBaseFuel();
					final int unitPrice = baseFuelPricesPerMT[bf.getIndex()];
					details.setFuelUnitPrice(fk.getFuelComponent(), unitPrice);
					// Existing consumption data is in M3, also store the MMBtu values
					final long consumptionInMT = details.getFuelConsumption(fk);
					if (consumptionInMT != 0) {
						final long currentTotal = voyagePlan.getBaseFuelCost();
						voyagePlan.setBaseFuelCost(currentTotal + Calculator.costFromConsumption(consumptionInMT, unitPrice));
					}
				}
			}
		}

		calculateCharterCosts(charterCostCalculator, sequence, portTimesRecord);

		// Store results in plan
		voyagePlan.setSequence(sequence);

		// Calculate cost of start heel
		if (heelOptionSupplierPortSlot != null) {

			final int pricePerMMBTU = heelOptionSupplierPortSlot.getHeelOptionsSupplier().getHeelPriceCalculator().getHeelPrice(voyagePlan.getStartingHeelInM3(), portTimesRecord.getFirstSlotTime(),
					portTimesRecord.getFirstSlot().getPort());
			final int cv = heelOptionSupplierPortSlot.getHeelOptionsSupplier().getHeelCVValue();

			final long startHeelInMMBTU = Calculator.convertM3ToMMBTu(voyagePlan.getStartingHeelInM3(), cv);
			final long startHeelCost = Calculator.costFromConsumption(startHeelInMMBTU, pricePerMMBTU);
			voyagePlan.setStartHeelCost(startHeelCost);
		}

		final long cooldownCost = calculateCooldownCost(vessel, portTimesRecord, sequence);
		// calculateCoolDown
		voyagePlan.setCooldownCost(cooldownCost);

		voyagePlan.setTotalRouteCost(routeCostAccumulator);

		// Check for cooldown violations and add to the violations count
		voyagePlanMetrics[VoyagePlanMetrics.COOLDOWN_COUNT.ordinal()] += checkCooldownViolations(loadIdx, dischargeIdx, sequence);

		voyagePlan.setVoyagePlanMetrics(voyagePlanMetrics);
		return voyagePlanMetrics;
	}

	private void calculateCharterCosts(final ICharterCostCalculator charterCostCalculator, final IDetailsSequenceElement[] sequence, final IPortTimesRecord portTimesRecord) {
		final int voyagePlanStartTimeUTC = timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot());
		final int offset = sequence.length > 1 ? 1 : 0;
		int time = portTimesRecord.getFirstSlotTime();
		for (int i = 0; i < sequence.length - offset; ++i) {
			final IDetailsSequenceElement element = sequence[i];

			if (element instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) element;
				// Calculate travel charter cost.
				int eventStartTime = time;
				int duration = details.getTravelTime();
				long charterCost = charterCostCalculator.getCharterCost(voyagePlanStartTimeUTC, eventStartTime, duration);
				details.setTravelCharterCost(charterCost);
				time += duration;

				// Calculate idle charter cost.
				eventStartTime = time;
				duration = details.getIdleTime();
				charterCost = charterCostCalculator.getCharterCost(voyagePlanStartTimeUTC, eventStartTime, duration);
				details.setIdleCharterCost(charterCost);
				time += duration;

				// Calculate purge charter cost.
				eventStartTime = time;
				duration = details.getPurgeDuration();
				charterCost = charterCostCalculator.getCharterCost(voyagePlanStartTimeUTC, eventStartTime, duration);
				details.setPurgeCharterCost(charterCost);
				time += duration;
			} else {
				assert element instanceof PortDetails;
				final PortDetails details = (PortDetails) element;
				final int eventStartTime = time;
				final int duration = details.getOptions().getVisitDuration();

				final long charterCost = charterCostCalculator.getCharterCost(voyagePlanStartTimeUTC, eventStartTime, duration);
				details.setCharterCost(charterCost);
				time += duration;
			}
		}
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
			// Not enough LNG - infeasible solution. Increase start heel to make maths work,
			// but will be a MAX_HEEL violation.
			voyagePlan.setStartingHeelInM3(lngCommitmentInM3);
			voyagePlan.setRemainingHeelInM3(0);
			remainingHeelInM3 = 0;
			startHeelInM3 = lngCommitmentInM3;
			violationsCount += 300;
			return violationsCount;
		}
		if (expectedEndHeelState == STATE_WARM || expectedEndHeelState == STATE_COLD_COOLDOWN) {
			if (remainingHeelInM3 > 0) {
				voyagePlan.setStartingHeelInM3(lngCommitmentInM3);
				voyagePlan.setRemainingHeelInM3(0);
				++violationsCount;
			}
		}

		if (voyageDuration > 0 && lngCommitmentInM3 == 0 && startHeelRangeInM3[0] > 0) {
			// Voyage option selected without any LNG use. But heel is available. This is a
			// bad combination.

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
				VesselTankState endHeelState;
				if (endPortSlot.getPortType() == PortType.Maintenance && !(endPortSlot instanceof MaintenanceVesselEventPortSlot)) {
					endHeelState = VesselTankState.EITHER;
				} else {
					endHeelState = endPortSlot.getHeelOptionsConsumer().getExpectedTankState();
				}

				// If we have an either state, we convert to a WARM or COLD check depending on
				// how much heel we have.
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
					if (endPortSlot.getPortType() != PortType.Maintenance) {
						if (remainingHeelInM3 > endPortSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3()) {
							++violationsCount;
						} else if (remainingHeelInM3 < endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3()) {
							// Current heel is too low for the end requirement. Can we increase our start
							// heel?
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
					} else if (endPortSlot instanceof MaintenanceVesselEventPortSlot) {
						if (remainingHeelInM3 < endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3()) {
							final long spareStartHeelInM3 = (voyageDuration > 0 && lngCommitmentInM3 == 0) ? 0 : Math.max(0, startHeelRangeInM3[1] - startHeelInM3);
							if (endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3() - remainingHeelInM3 <= spareStartHeelInM3) {
								final long extraStartHeelInM3 = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3() - remainingHeelInM3;

								startHeelInM3 += extraStartHeelInM3;
								voyagePlan.setStartingHeelInM3(startHeelInM3);

								remainingHeelInM3 += extraStartHeelInM3;
								assert remainingHeelInM3 == endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
								voyagePlan.setRemainingHeelInM3(remainingHeelInM3);
							}
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

	private long[] calculateCargoEndHeelRange(final IVessel vessel, final VoyageDetails lastVoyageDetailsElement, final int endHeelState) {
		// Determine expected end heel range and minimum required heel. Last index is
		// safetyHeel.
		// Bit of a hacky faff, but this allows a single return from the method and we
		// only use index 0 and 1 in range checks
		final long[] expectedRemainingHeelRangeInM3_Plus_Safety_Heel = new long[3];

		expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = vessel.getSafetyHeel();

		final IPortSlot toPortSlot = lastVoyageDetailsElement.getOptions().getToPortSlot();
		if (toPortSlot instanceof IHeelOptionConsumerPortSlot) {
			final IHeelOptionConsumerPortSlot endPortSlot = (IHeelOptionConsumerPortSlot) toPortSlot;
			if (endPortSlot.getHeelOptionsConsumer().getExpectedTankState() == VesselTankState.MUST_BE_COLD) {
				// If we are in charter-length mode and idle is long, ignore lower bound of
				// heel.
				if (lastVoyageDetailsElement.getOptions().getVesselState() == VesselState.Ballast //
						&& lastVoyageDetailsElement.getIdleTime() > hoursBeforeCooldownsNoLongerForced //
						&& (endHeelState == STATE_WARM || endHeelState == STATE_COLD_COOLDOWN)) {
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = 0;
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = 0;
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = 0;
				} else {
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = endPortSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3();
					expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = endPortSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
				}
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
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = vessel.getSafetyHeel();
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = vessel.getSafetyHeel();
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = vessel.getSafetyHeel();
			} else {
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[0] = 0;
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[1] = 0;
				expectedRemainingHeelRangeInM3_Plus_Safety_Heel[2] = 0;
			}
		}
		return expectedRemainingHeelRangeInM3_Plus_Safety_Heel;
	}

	protected int[] getExpectedMaintenanceEndHeelStates(final List<Triple<VoyageDetails, PortDetails, Integer>> maintenanceDetailsElements, final int vesselWarmupTime) {
		if (maintenanceDetailsElements.isEmpty()) {
			return null;
		}
		final int[] endHeelStates = new int[maintenanceDetailsElements.size()];
		final Iterator<VoyageDetails> voyageIter = maintenanceDetailsElements.stream().map(Triple::getFirst).iterator();
		for (int i = 0; i < maintenanceDetailsElements.size(); ++i) {
			endHeelStates[i] = getExpectedCargoEndHeelState(vesselWarmupTime, voyageIter.next());
		}
		return endHeelStates;
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
				int nboHours = lastVoyageDetailsElement.getTravelNBOHours() +  lastVoyageDetailsElement.getRouteAdditionalNBOHours() + lastVoyageDetailsElement.getIdleNBOHours();
				int warmingHours = lastVoyageDetailsElement.getOptions().getAvailableTime() - nboHours;

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

	protected long[] checkCargoCapacityViolations(final long startHeelInM3, final long baseLNGCommitmentInM3, final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot,
			final long baseCargoCapacityInM3, final long heelLevelInM3, final long[] remainingHeelRangeInM3, final IDetailsSequenceElement[] sequence) {

		long[] metrics = new long[VoyagePlanMetrics.values().length];

		// TODO: Needs fixing for LDD!
		// NOTE: Strict checking may be too much for actuals

		final long loadPortBOGInM3;
		if (adjustForLoadPortBOG) {
			loadPortBOGInM3 = ((PortDetails) sequence[0]).getFuelConsumption(LNGFuelKeys.NBO_In_m3);
		} else {
			loadPortBOGInM3 = 0L;
		}
		// If we model load port BOG on top of max load volume, then model an increase
		// in vessel capacity so we can leave the load port with a full ship load.
		final long cargoCapacityInM3 = baseCargoCapacityInM3 + loadPortBOGInM3;

		final long lngCommitmentInM3 = baseLNGCommitmentInM3;

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
			// From an optimisation point of view, this cannot be fixed by the voyage plan optimiser. Maybe we should ignore this case?

			long qty = minLoadVolumeInM3 - (cargoCapacityInM3 - startHeelInM3);
			if (qty > SchedulerConstants.CAPACITY_VIOLATION_THRESHOLD) {
				metrics[VoyagePlanMetrics.VOLUME_VIOLATION_QUANTITY.ordinal()] += qty;
				metrics[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]++;

			}
		}

		if (lngCommitmentInM3 > upperLoadLimitInM3 + startHeelInM3) {
			// Ignore this violation - should be covered elsewhere
			// ++violationsCount;
		}

		// This is the smallest amount of gas we can load
		if (minLoadVolumeInM3 - lngCommitmentInM3 + startHeelInM3 - heelLevelInM3 > maxDischargeVolumeInM3) {
			/*
			 * note - this might not be a genuine violation since rolling over the excess LNG may be permissible and in some cases it is even commercially desirable, but restrictions on LNG
			 * destination or complications from profit share contracts make it a potential violation, and we err on the side of caution
			 */

			// load breach -- need to load less than we are permitted

			long qty = (minLoadVolumeInM3 - lngCommitmentInM3 + startHeelInM3 - heelLevelInM3) - maxDischargeVolumeInM3;
			if (qty > SchedulerConstants.CAPACITY_VIOLATION_THRESHOLD) {
				metrics[VoyagePlanMetrics.VOLUME_VIOLATION_QUANTITY.ordinal()] += qty;
				metrics[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]++;

			}
		}
		{

			// The load should cover at least the fuel usage plus the heel (or the min discharge, whichever is greater)
			long qty = (minDischargeVolumeInM3 + heelLevelInM3 + lngCommitmentInM3) - (upperLoadLimitInM3 + startHeelInM3);
			if (qty > SchedulerConstants.CAPACITY_VIOLATION_THRESHOLD) {
				metrics[VoyagePlanMetrics.VOLUME_VIOLATION_QUANTITY.ordinal()] += qty;
				metrics[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]++;
			}
		}
		if (heelLevelInM3 < remainingHeelRangeInM3[0] || heelLevelInM3 > remainingHeelRangeInM3[1]) {
			metrics[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]++;
		}

		// Finally, track heel across voyages. If the ballast leg is short (i.e. within
		// the warm up time) we could hit a case where we choose not to NBO on the
		// ballast even though there is some heel
		// left after the discharge. For example if the start heel is greater than the
		// max discharge, (Start Heel - laden NBO) > max discharge and ballast time <
		// warm up time.
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
						// Check for Long.MAX_VALUE and keep heel positive
						final long maxDischargeVolume = dischargeOption.getMaxDischargeVolume(cargoCVValue);
						if (maxDischargeVolume == Long.MAX_VALUE) {
							heelInM3 = 0;
						} else {
							heelInM3 -= maxDischargeVolume;
						}
						if (heelInM3 < 0) {
							heelInM3 = 0;
						}
					}
				} else if (e instanceof VoyageDetails) {
					final VoyageDetails voyageDetails = (VoyageDetails) e;
					long voyageNBOInM3 = 0;
					voyageNBOInM3 += voyageDetails.getFuelConsumption(LNGFuelKeys.NBO_In_m3);
					voyageNBOInM3 += voyageDetails.getFuelConsumption(LNGFuelKeys.FBO_In_m3);
					voyageNBOInM3 += voyageDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);

					if (voyageDetails.getOptions().getAvailableTime() > 0 && voyageNBOInM3 == 0 && heelInM3 > 0) {
						return null;
					}

					heelInM3 -= voyageNBOInM3;
				}
			}
		}

		return metrics;
	}

	protected int checkCooldownViolations(final int loadIdx, final int dischargeIdx, final IDetailsSequenceElement... sequence) {
		int cooldownViolations = 0;
		// check for cooldown violations
		for (int i = 0; i < sequence.length; ++i) {
			if ((i & 1) == 1) {
				assert sequence[i] instanceof VoyageDetails;

				final VoyageDetails details = (VoyageDetails) sequence[i];

				if (details.getIdleTime() <= hoursBeforeCooldownsNoLongerForced //
						|| details.getOptions().getToPortSlot().getPortType() == PortType.End //
				) {
					// TODO: Original check also looked at the should be cold requirement - not
					// really needed?
					if (!details.getOptions().getAllowCooldown() && details.isCooldownPerformed()) {
						++cooldownViolations;
					}
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
	public final List<IDetailsSequenceElement> generateFuelCostCalculatedSequence(IVessel vessel, CargoRunDryMode cargoRunDry, long[] startHeelInM3, final IOptionsSequenceElement... baseSequence) {
		final List<IDetailsSequenceElement> result = new ArrayList<>(baseSequence.length);

		boolean incNBO = false;
		int cv = 0;
		long nboAvailableInM3 = Long.MAX_VALUE;
		ILoadSlot loadSlot = null;
		long ladenNBO = 0;// = Long.MAX_VALUE;
		int idx = 0;
		for (final IOptionsSequenceElement element : baseSequence) {

			// Loop through basic elements, calculating voyage requirements
			// to build up basic voyage plan details.
			if (element instanceof VoyageOptions) {
				final VoyageOptions options = (VoyageOptions) element;

				final VoyageDetails voyageDetails = new VoyageDetails(options);
				// Calculate voyage cost
				nboAvailableInM3 = calculateVoyageFuelRequirements(options, voyageDetails, nboAvailableInM3);
				if (incNBO && options.getVesselState() == VesselState.Laden) {
					for (FuelKey fk : LNGFuelKeys.LNG_In_m3) {
						ladenNBO -= voyageDetails.getFuelConsumption(fk);
						ladenNBO -= voyageDetails.getRouteAdditionalConsumption(fk);
					}
				}
				result.add(voyageDetails);
			} else if (element instanceof PortOptions) {
				final PortOptions options = ((PortOptions) element).copy();

				final PortDetails details = new PortDetails(options);
				calculatePortFuelRequirements(options, details);
				

				if (idx == 0) {
					if (options.getPortSlot() instanceof IHeelOptionSupplierPortSlot) {
						final IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot = (IHeelOptionSupplierPortSlot) options.getPortSlot();
						// This enables "running dry"
						nboAvailableInM3 = heelOptionSupplierPortSlot.getHeelOptionsSupplier().getMaximumHeelAvailableInM3();
					}

					else if (options.getPortSlot() instanceof ILoadSlot) {
						if (cargoRunDry != CargoRunDryMode.OFF) {

							incNBO = true;

							loadSlot = ((ILoadSlot) options.getPortSlot());
							cv = ((ILoadSlot) options.getPortSlot()).getCargoCVValue();
							
							for (FuelKey fk : LNGFuelKeys.LNG_In_m3) {
								ladenNBO -= details.getFuelConsumption(fk);
							}							
						}
					}
				} else if (options.getPortSlot() instanceof IDischargeSlot) {
					if (incNBO) {

						
						assert nboAvailableInM3 == Long.MAX_VALUE;
						
						final long loadPortBOGInM3;
						if (adjustForLoadPortBOG) {
							loadPortBOGInM3 = ((PortDetails) result.get(0)).getFuelConsumption(LNGFuelKeys.NBO_In_m3);
						} else {
							loadPortBOGInM3 = 0L;
						}

						if (cargoRunDry == CargoRunDryMode.PREFER_MAX_MAX) {

							// WORK OUT HEEL
							// nboAvailableInM3 should be negative for the laden BOG
							long maxLoadInM3 = Math.min(vessel.getCargoCapacity() + loadPortBOGInM3, loadSlot.getMaxLoadVolume() + startHeelInM3[1]);

							long nbo = maxLoadInM3 + ladenNBO;
							nbo -= ((IDischargeSlot) options.getPortSlot()).getMaxDischargeVolume(cv);

							if (nbo < 0) {
								// not enough gas ....
								return null;
							} else {
								nboAvailableInM3 = nbo;
							}
						} else if (cargoRunDry == CargoRunDryMode.PREFER_MAX_MIN) {

							// WORK OUT HEEL
							// nboAvailableInM3 should be negative for the laden BOG
							long maxLoadInM3 = Math.min(vessel.getCargoCapacity() + loadPortBOGInM3, loadSlot.getMaxLoadVolume() + startHeelInM3[1]);

							long nbo = maxLoadInM3 + ladenNBO;
							nbo -= ((IDischargeSlot) options.getPortSlot()).getMinDischargeVolume(cv);

							if (nbo < 0) {
								// not enough gas ....
								return null;
							} else {
								nboAvailableInM3 = nbo;
							}

						} else if (cargoRunDry == CargoRunDryMode.PREFER_MIN_MAX) {

							// WORK OUT HEEL
							// nboAvailableInM3 should be negative for the laden BOG
							long maxLoadInM3 = Math.min(vessel.getCargoCapacity() + loadPortBOGInM3, loadSlot.getMinLoadVolume() + startHeelInM3[1]);

							long nbo = Math.max(0, maxLoadInM3 + ladenNBO);
							nbo -= ((IDischargeSlot) options.getPortSlot()).getMaxDischargeVolume(cv);

							if (nbo < 0) {
								// not enough gas ....
								return null;
							} else {
								nboAvailableInM3 = nbo;
							}
						} else if (cargoRunDry == CargoRunDryMode.PREFER_MIN_MIN) {

							// WORK OUT HEEL
							// nboAvailableInM3 should be negative for the laden BOG
							long maxLoadInM3 = Math.min(vessel.getCargoCapacity() + loadPortBOGInM3, loadSlot.getMinLoadVolume() + startHeelInM3[1]);

							long nbo = Math.max(0, maxLoadInM3 + ladenNBO);
							nbo -= ((IDischargeSlot) options.getPortSlot()).getMinDischargeVolume(cv);

							if (nbo < 0) {
								// not enough gas ....
								return null;
							} else {
								nboAvailableInM3 = nbo;
							}
						}
					} else {
						// Run dry mode is off, or not a cargo
						if (nboAvailableInM3 != Long.MAX_VALUE) {
							for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
								nboAvailableInM3 -= details.getFuelConsumption(fuelKey);
							}
						}
					}
				} else {
					if (nboAvailableInM3 != Long.MAX_VALUE) {
						for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
							nboAvailableInM3 -= details.getFuelConsumption(fuelKey);
						}
					}
				}

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
			idx++;
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

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this port visit
		 */
		final long consumptionRateInMTPerDay;
		final long inPortNBORateInM3PerDay;

		final PortType portType = options.getPortSlot().getPortType();

		// temporary kludge: ignore non-load non-discharge ports for port consumption
		if (portType == PortType.Load || portType == PortType.Discharge) {
			consumptionRateInMTPerDay = vessel.getInPortConsumptionRateInMTPerDay(portType);

			if (portType == PortType.Load) {
				inPortNBORateInM3PerDay = vessel.getInPortNBORate(VesselState.Laden);
			} else {
				inPortNBORateInM3PerDay = vessel.getInPortNBORate(VesselState.Ballast);
			}

		} else if (portType == PortType.End) {
			// Maybe include hotel load for end events?
			// consumptionRateInMTPerDay =
			// vessel.getIdleConsumptionRate(VesselState.Ballast);
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

		final long minBaseFuelConsumptionInMT = Calculator.quantityFromRateTime(vessel.getMinBaseFuelConsumptionInMTPerDay(), visitDuration) / 24L;
		final FuelKey fk = vessel.getInPortBaseFuelInMT();
		if (minBaseFuelConsumptionInMT > requiredConsumptionInMT) {
			details.setFuelConsumption(fk, minBaseFuelConsumptionInMT);
		} else {
			details.setFuelConsumption(fk, requiredConsumptionInMT);
		}

		// inPortBO
		final long NBOinM3 = Calculator.quantityFromRateTime(inPortNBORateInM3PerDay, visitDuration) / 24L;
		details.setFuelConsumption(LNGFuelKeys.NBO_In_m3, NBOinM3);

	}

	/**
	 * API for unit testing
	 */
	public void setPortCooldownProvider(final IPortCooldownDataProvider portCooldownDataProvider) {
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
	public final int findFirstLoadIndex(final IDetailsSequenceElement... sequence) {
		// ignore the last element in the sequence, to avoid double-counting (it will be
		// included in the next sequence)
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
	public final int findFirstDischargeIndex(final IDetailsSequenceElement... sequence) {
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
	public final List<Integer> findLoadIndices(final IDetailsSequenceElement... sequence) {
		final List<Integer> storage = new ArrayList<>();

		// ignore the last element in the sequence, to avoid double-counting (it will be
		// included in the next sequence)
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
	public final List<Integer> findDischargeIndices(final IDetailsSequenceElement... sequence) {
		final List<Integer> storage = new ArrayList<>();

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

	public boolean isAdjustForLoadPortBOG() {
		return adjustForLoadPortBOG;
	}

	public void setAdjustForLoadPortBOG(boolean adjustForLoadPortBOG) {
		this.adjustForLoadPortBOG = adjustForLoadPortBOG;
	}

}
