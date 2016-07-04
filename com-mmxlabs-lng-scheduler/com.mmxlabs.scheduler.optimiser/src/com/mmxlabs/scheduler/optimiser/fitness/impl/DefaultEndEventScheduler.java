/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

public class DefaultEndEventScheduler implements IEndEventScheduler {

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private ILNGVoyageCalculator lngVoyageCalculator;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Override
	public List<IPortTimesRecord> scheduleEndEvent(final IResource resource, final IVesselAvailability vesselAvailability, final PortTimesRecord partialPortTimesRecord, final int scheduledTime,
			@NonNull final IPortSlot endEventSlot) {

		final List<IPortTimesRecord> additionalRecords = new LinkedList<>();
		@NonNull
		final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);

		if ((vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {

			if (endRequirement.isOpen()) {
				return scheduleOpenEndedVessel(vesselAvailability, partialPortTimesRecord, scheduledTime, endEventSlot);
			}
		}
		if (endRequirement.hasTimeRequirement()) {
			// Not ready yet, fall back to existing code in VPO
			// List<@NonNull IPortTimesRecord> l = findBestRedeliveryTime(vesselAvailability, partialPortTimesRecord, scheduledTime, endEventSlot, endRequirement);
			// if (l != null) {
			// return l;
			// }
		}
		partialPortTimesRecord.setReturnSlotTime(endEventSlot, scheduledTime);
		partialPortTimesRecord.setSlotDuration(endEventSlot, 0);

		return additionalRecords;
	}

	protected @NonNull List<@NonNull IPortTimesRecord> scheduleOpenEndedVessel(final @NonNull IVesselAvailability vesselAvailability, final @NonNull PortTimesRecord partialPortTimesRecord,
			final int scheduledTime, final @NonNull IPortSlot endEventSlot) {

		final IPortSlot prevPortSlot = partialPortTimesRecord.getSlots().get(partialPortTimesRecord.getSlots().size() - 1);

		assert prevPortSlot != null;
		final int prevArrivalTime = partialPortTimesRecord.getSlotTime(prevPortSlot);
		final int prevVisitDuration = partialPortTimesRecord.getSlotDuration(prevPortSlot);

		// TODO: Quickest != most economical routing
		final int availableTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), prevPortSlot.getPort(), endEventSlot.getPort(), prevArrivalTime + prevVisitDuration,
				vesselAvailability.getVessel().getVesselClass().getMaxSpeed()).getSecond();
		final int shortCargoReturnArrivalTime = prevArrivalTime + prevVisitDuration + availableTime;

		partialPortTimesRecord.setReturnSlotTime(endEventSlot, shortCargoReturnArrivalTime);
		partialPortTimesRecord.setSlotDuration(endEventSlot, 0);

		// Create new PTR to record end event duration.
		// Note: This fouls up the GCO stuff for the end event.
		final PortTimesRecord endPortTimesRecord = new PortTimesRecord();
		endPortTimesRecord.setSlotTime(endEventSlot, shortCargoReturnArrivalTime);

		final int duration = scheduledTime - shortCargoReturnArrivalTime;
		endPortTimesRecord.setSlotDuration(endEventSlot, duration);

		return Collections.singletonList(endPortTimesRecord);
	}

	private @Nullable List<@NonNull IPortTimesRecord> findBestRedeliveryTime(final IVesselAvailability vesselAvailability, final PortTimesRecord partialPortTimesRecord, final int scheduledTime,
			final IPortSlot endEventSlot, final IEndRequirement endRequirement) {
		@Nullable
		ITimeWindow timeWindow = endRequirement.getTimeWindow();
		final ITimeWindow window = timeWindow;
		final int extraExtent = window == null ? 1 : (scheduledTime >= window.getExclusiveEnd() ? 0 : 1);
		// Experimental speed - step code to replace VPO. -- WIP
		if (false && extraExtent > 0) {
			// TODO: Cacheable!

			// >>>>>>>>>>>>>SPLIT OFF INTO OVERRIABLE METHOD

			// There are some cases where we wish to evaluate the best time to
			// end the sequence, rather than the specified value. Typically
			// this will be because no end date has been set and the specified
			// time will just be the quickest time to get between ports. Here we
			// increase the available time and pick the cheapest cost.

			// There are two elements to this implementation which should be
			// considered further.
			// 1) We break out at the first sign of a cost increase. This
			// assumes there is no local minima/maxima which may not hold true.
			// 2) We limit the number of iterations to avoid potential infinite
			// loops should we never get a cost value to compare against.
			// However this may miss potential cheaper solutions past this
			// boundary

			final IPortSlot prevPortSlot = partialPortTimesRecord.getSlots().get(partialPortTimesRecord.getSlots().size() - 1);
			final PortType prevPortType = prevPortSlot.getPortType();

			assert prevPortSlot != null;
			final int prevArrivalTime = partialPortTimesRecord.getSlotTime(prevPortSlot);
			final int prevVisitDuration = partialPortTimesRecord.getSlotDuration(prevPortSlot);

			final int departureTime = prevArrivalTime + prevVisitDuration;

			final VoyageOptions finalOptions = new VoyageOptions(prevPortSlot, endEventSlot);

			final IVessel vessel = vesselAvailability.getVessel();
			final IVesselClass vesselClass = vessel.getVesselClass();
			final boolean isReliq = vesselClass.hasReliqCapability();

			final IPortSlot firstSlot = partialPortTimesRecord.getFirstSlot();

			final int cargoCV;
			long maxLoadInM3 = vesselClass.getCargoCapacity();
			long minDischargeInM3 = 0;
			final int bunkerPricePerMT = 0;
			int lngPricePerMMBtu = 0;
			// TODO: Grab from input data
			final long hireRatePerDay = 0L;// .currentPlan.getCharterInRatePerDay();
			// TODO: Split into travel & idle time
			final long ladenEstimateInM3;

			if (firstSlot instanceof ILoadSlot) {
				final int ladenTravelTime = 0;
				ladenEstimateInM3 = Calculator.quantityFromRateTime(vesselClass.getNBORate(VesselState.Laden), ladenTravelTime) / 24L;
				final ILoadSlot loadSlot = (ILoadSlot) firstSlot;
				cargoCV = loadSlot.getCargoCVValue();

				long volume = vessel.getCargoCapacity();
				if (loadSlot.getMaxLoadVolume() != 0) {
					volume = Math.min(volume, loadSlot.getMaxLoadVolume());
				}
				maxLoadInM3 = volume;
				// LDD!
				assert partialPortTimesRecord.getSlots().size() == 2;
				minDischargeInM3 = ((IDischargeSlot) partialPortTimesRecord.getSlots().get(1)).getMinDischargeVolume();
			} else if (firstSlot instanceof IHeelOptionsPortSlot) {
				final IHeelOptionsPortSlot heelOptionsPortSlot = (IHeelOptionsPortSlot) firstSlot;
				IHeelOptions heelOptions = heelOptionsPortSlot.getHeelOptions();
				if (heelOptions != null) {
					cargoCV = heelOptions.getHeelCVValue();
					lngPricePerMMBtu = heelOptions.getHeelUnitPrice();
					maxLoadInM3 = heelOptions.getHeelLimit();
				} else {
					cargoCV = 0;
					lngPricePerMMBtu = 0;
					maxLoadInM3 = 0;
				}
				ladenEstimateInM3 = 0;
			} else {
				cargoCV = 0;
				lngPricePerMMBtu = 0;
				minDischargeInM3 = 0;
				maxLoadInM3 = 0;
				ladenEstimateInM3 = 0;
			}

			{
				finalOptions.setVessel(vessel);
				finalOptions.setVesselState(VesselState.Ballast);

				if ((prevPortType == PortType.DryDock) || (prevPortType == PortType.Maintenance)) {
					finalOptions.setWarm(true);
				} else {
					finalOptions.setWarm(false);
				}

				finalOptions.setCargoCVValue(cargoCV);

				// Convert rate to MT equivalent per day
				final int nboRateInMTPerDay = (int) Calculator.convertM3ToMT(vesselClass.getNBORate(VesselState.Ballast), cargoCV, vesselClass.getBaseFuel().getEquivalenceFactor());
				if (nboRateInMTPerDay > 0) {
					final int nboSpeed = vesselClass.getConsumptionRate(VesselState.Ballast).getSpeed(nboRateInMTPerDay);
					finalOptions.setNBOSpeed(nboSpeed);
				}

				// We are not considering idle time here
				finalOptions.setUseNBOForIdle(false);

				finalOptions.setShouldBeCold(endRequirement.isEndCold());
				finalOptions.setAllowCooldown(false);
			}

			final int maxSpeed = vessel.getVesselClass().getMaxSpeed();
			final int minSpeed = vessel.getVesselClass().getMinSpeed();

			final IPort from = prevPortSlot.getPort();
			final IPort to = endEventSlot.getPort();

			final long endHeelInM3 = endRequirement.isEndCold() ? endRequirement.getTargetHeelInM3() : 0;
			final long startHeelInM3 = (finalOptions.isWarm()) ? 0 : vesselClass.getSafetyHeel();

			final long estimatedBOGAvailableInM3 = startHeelInM3 + maxLoadInM3 - minDischargeInM3 - endHeelInM3 - ladenEstimateInM3;

			// Our speeds will give us a time interval, not all of these will be valid. The min/max speed gives a time window to intersect with the end requirment window.
			final int validMinTime;
			final int validMaxTime;

			long bestCost = Long.MAX_VALUE;
			int bestSpeed = 0;
			boolean bestViolation = true;
			boolean bestNBO = false;
			boolean bestFBO = true;

			Pair<ERouteOption, Integer> bestRoute = null;
			LOOP_ROUTE: for (final Pair<ERouteOption, Integer> distanceOption : distanceProvider.getAllDistanceValues(from, to)) {
				final ERouteOption route = distanceOption.getFirst();
				final int distance = distanceOption.getSecond();

				// Here we cannot determine whether or not we are round trip (it *may* be possible to determine this in some cases based on travel time).
				final long routeCost = routeCostProvider.getRouteCost(route, vessel, CostType.Ballast);
				finalOptions.setRoute(route, distance, routeCost);

				for (final boolean useNBO : new boolean[] { true, false }) {

					if (finalOptions.isWarm() && useNBO) {
						continue;
					}
					// If we need to end cold and have found a valid plan, do not evaluate options to end warm
					if (bestCost != Long.MAX_VALUE && !useNBO && endRequirement.isEndCold()) {
						continue;
					}

					finalOptions.setUseNBOForTravel(useNBO);

					for (final boolean useFBO : new boolean[] { true, false }) {
						// // Determined by voyage plan optimiser
						// // If NBO is enabled for a reliq vessel, then force FBO too
						finalOptions.setUseFBOForSupplement(useFBO);

						if (isReliq && useNBO != useFBO) {
							continue;
						}

						// Reverse the iterator, max speed to min/nboSpeed. Increment will floor at NBO if needed
						final int loopMinSpeed = useNBO ? finalOptions.getNBOSpeed() : minSpeed;
						for (int speed = maxSpeed; speed >= loopMinSpeed; speed -= Math.min(100, Math.max(1, speed - loopMinSpeed))) {
							final int availableTime = Calculator.getTimeFromSpeedDistance(speed, distance) + routeCostProvider.getRouteTransitTime(route, vessel);
							/// Too soon, keep slowing down...
							if (departureTime + availableTime < timeWindow.getInclusiveStart()) {
								continue;
							}
							// Too slow now...
							if (departureTime + availableTime >= timeWindow.getExclusiveEnd()) {
								continue LOOP_ROUTE;
							}

							finalOptions.setAvailableTime(availableTime);

							final VoyageDetails voyageDetails = new VoyageDetails(finalOptions);
							lngVoyageCalculator.calculateVoyageFuelRequirements(finalOptions, voyageDetails);

							// Too slow now on this route option, flip to another one.
							if (voyageDetails.getIdleTime() > 0) {
								// (IF Max to min, then this is valid)
								continue LOOP_ROUTE;
							}

							// TODO: Include violation count

							// >>>> Split off into method

							long cost = 0;
							// Route Cost
							cost += finalOptions.getRouteCost();
							// Charter cost
							cost += hireRatePerDay * (long) availableTime / 24L;

							// Bunker Cost estimate
							final long bunkerInMT = voyageDetails.getFuelConsumption(FuelComponent.Base, FuelUnit.MT) + voyageDetails.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT)
									+ voyageDetails.getFuelConsumption(FuelComponent.PilotLight, FuelUnit.MT) + voyageDetails.getRouteAdditionalConsumption(FuelComponent.Base, FuelUnit.MT)
									+ voyageDetails.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT)
									+ voyageDetails.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelUnit.MT);
							cost += Calculator.costFromConsumption(bunkerInMT, bunkerPricePerMT);

							final long lngInM3 = voyageDetails.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3) + voyageDetails.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3)
									+ voyageDetails.getRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.M3) + voyageDetails.getRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.M3);
							// LNG Cost Estimate.
							final long lngInMMBTu = Calculator.convertM3ToMMBTu(lngInM3, cargoCV);
							cost += Calculator.costFromConsumption(lngInMMBTu, lngPricePerMMBtu);

							// violation estimate.
							boolean violation = false;
							if (estimatedBOGAvailableInM3 - lngInM3 < 0) {
								violation = true;
							}

							if (bestViolation && !violation || cost < bestCost) {
								bestSpeed = speed;
								bestRoute = distanceOption;
								bestCost = cost;
								bestViolation = violation;
								bestNBO = useNBO;
								bestFBO = useFBO;
							}
						}
					}
				}
			}

			if (bestCost == Long.MAX_VALUE) {
				// Lets return as fast as possible
				final int availableTime = Math.max(timeWindow.getInclusiveStart(),
						prevArrivalTime + prevVisitDuration + distanceProvider.getQuickestTravelTime(vessel, from, to, prevArrivalTime + prevVisitDuration, maxSpeed).getSecond());
				partialPortTimesRecord.setReturnSlotTime(endEventSlot, prevArrivalTime + prevVisitDuration + availableTime);
				partialPortTimesRecord.setSlotDuration(endEventSlot, 0);

				return Collections.emptyList();
			} else {
				assert bestRoute != null;
				final int availableTime = Calculator.getTimeFromSpeedDistance(bestSpeed, bestRoute.getSecond()) + routeCostProvider.getRouteTransitTime(bestRoute.getFirst(), vessel);
				partialPortTimesRecord.setReturnSlotTime(endEventSlot, prevArrivalTime + prevVisitDuration + availableTime);
				partialPortTimesRecord.setSlotDuration(endEventSlot, 0);
				return Collections.emptyList();
			}
		}
		return null;

	}
}
