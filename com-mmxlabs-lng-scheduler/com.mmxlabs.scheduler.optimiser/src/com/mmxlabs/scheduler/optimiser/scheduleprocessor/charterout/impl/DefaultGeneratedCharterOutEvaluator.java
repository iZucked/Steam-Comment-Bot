/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.GeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IdleNBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.ReliqVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.TravelVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IdleFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class DefaultGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {

	@Inject
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean checkPanamaCanalBookings = false;

	@Inject
	@Named(SchedulerConstants.Key_UseBestPanamaCanalIdleDaysWindowTrimming)
	private boolean useBestCanalIdleDays = false;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IVolumeAllocator volumeAllocator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IVoyagePlanOptimiser voyagePlanOptimiser;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IPanamaBookingsProvider panamaBookingsProvider;

	private static final double canalChoiceThreshold = 0.1; // percentage improvement required to choose a canal route

	@Override
	public @Nullable List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final long[] startHeelVolumeRangeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp,
			final IPortTimesRecord portTimesRecord, @Nullable final IAnnotatedSolution annotatedSolution) {

		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		// First step, find a ballast leg which is long enough to charter-out
		final Object[] currentSequence = vp.getSequence();
		int ballastIdx = -1;
		int ballastStartTime = -1;
		PortDetails firstDetails = null;
		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails && firstDetails == null) {
				firstDetails = (PortDetails) obj;
			} else if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				// record last ballast leg
				if (details.getOptions().getVesselState() == VesselState.Ballast) {
					ballastIdx = idx;
					ballastStartTime = portTimesRecord.getSlotTime(details.getOptions().getFromPortSlot()) + portTimesRecord.getSlotDuration(details.getOptions().getFromPortSlot());
				}
			}
		}

		assert ((ballastIdx < 0) == (ballastStartTime < 0));
		if (firstDetails == null || ballastIdx == -1 || ballastStartTime == -1) {
			// no ballast leg?
			return null;
		}

		final VoyageDetails ballastDetails = (VoyageDetails) currentSequence[ballastIdx];
		final int availableTime = ballastDetails.getOptions().getAvailableTime();
		final IPortSlot slotBeforeCharter = ballastDetails.getOptions().getFromPortSlot();
		final IPortSlot slotAfterCharter = ballastDetails.getOptions().getToPortSlot();

		{
			// generate a split voyage plan
			final GeneratedCharterOutOption gcoMarket = getCharterOutOption(ballastStartTime, availableTime, slotBeforeCharter, //
					slotAfterCharter, vesselAvailability);

			// Have we found a market?
			if (gcoMarket.getOption() == null) {
				return null;
			}

			final ExtendedCharterOutSequence bigSequence = constructNewRawSequenceWithCharterOuts(vesselAvailability, currentSequence, gcoMarket, portTimesRecord, ballastIdx, ballastStartTime);

			final VoyagePlan bigVoyagePlan = runVPOOnBigSequence(vesselAvailability.getVessel(), vp, vesselAvailability.getCharterCostCalculator(), startHeelVolumeRangeInM3, bigSequence);

			final long remainingHeelInM3 = bigVoyagePlan.getRemainingHeelInM3();

			// now split into two
			final Pair<VoyagePlan, VoyagePlan> vPlans = splitVoyagePlan(bigVoyagePlan);
			final VoyagePlan upToCharterPlan = vPlans.getFirst();
			final VoyagePlan charterToEndPlan = vPlans.getSecond();

			// split the port times records in two...
			final Pair<IPortTimesRecord, IPortTimesRecord> splitPortTimesRecords = splitPortTimesRecord(bigSequence.getPortTimesRecord()); // -----> move up
			final IPortTimesRecord preCharteringTimes = splitPortTimesRecords.getFirst();
			final IPortTimesRecord postCharteringTimes = splitPortTimesRecords.getSecond();

			// set parameters such as heels and ends to ignore
			setPlanParameters(bigVoyagePlan, charterToEndPlan, upToCharterPlan, vp, remainingHeelInM3);
			final long firstPlanRemainingHeel = upToCharterPlan.getRemainingHeelInM3();

			final long[] bigVoyageStartingHeelRangeInM3 = new long[2];
			bigVoyageStartingHeelRangeInM3[0] = bigVoyagePlan.getStartingHeelInM3();
			bigVoyageStartingHeelRangeInM3[1] = bigVoyagePlan.getStartingHeelInM3();

			// calculate pre-charter plan
			voyageCalculator.calculateVoyagePlan(upToCharterPlan, vesselAvailability.getVessel(), vesselAvailability.getCharterCostCalculator(), bigVoyageStartingHeelRangeInM3,
					vesselBaseFuelCalculator.getBaseFuelPrices(vesselAvailability.getVessel(), preCharteringTimes), preCharteringTimes, upToCharterPlan.getSequence());
			// remaining heel may have been overwritten
			upToCharterPlan.setRemainingHeelInM3(firstPlanRemainingHeel);

			final IAllocationAnnotation preCharterAllocation = volumeAllocator.allocate(vesselAvailability, upToCharterPlan, preCharteringTimes, annotatedSolution);

			// add on delta to starting heel and remaining heel
			if (preCharterAllocation != null) {
				final long delta = preCharterAllocation.getRemainingHeelVolumeInM3() - charterToEndPlan.getStartingHeelInM3();
				charterToEndPlan.setStartingHeelInM3(charterToEndPlan.getStartingHeelInM3() + delta);
				charterToEndPlan.setRemainingHeelInM3(charterToEndPlan.getRemainingHeelInM3() + delta);
				// now we set the heel options on the charter out event as it will be need when
				// the second voyage plan is calculated
				final IGeneratedCharterOutVesselEventPortSlot charter = (IGeneratedCharterOutVesselEventPortSlot) ((PortDetails) charterToEndPlan.getSequence()[0]).getOptions().getPortSlot();
				setHeelOptions(charter.getVesselEvent(), preCharteringTimes, upToCharterPlan.getSequence(), upToCharterPlan.getRemainingHeelInM3());
			} else {
				final IGeneratedCharterOutVesselEventPortSlot charter = (IGeneratedCharterOutVesselEventPortSlot) ((PortDetails) charterToEndPlan.getSequence()[0]).getOptions().getPortSlot();
				setHeelOptions(charter.getVesselEvent(), preCharteringTimes, upToCharterPlan.getSequence(), upToCharterPlan.getRemainingHeelInM3());
			}

			// TODO: really we need partial boiloff here (see BugzID: 1798)
			final long secondPlanStartHeel = charterToEndPlan.getStartingHeelInM3();
			final long secondPlanRemainingHeel = charterToEndPlan.getRemainingHeelInM3();

			final long[] charterToEndPlanStartingHeelRangeInM3 = new long[2];
			charterToEndPlanStartingHeelRangeInM3[0] = charterToEndPlan.getStartingHeelInM3();
			charterToEndPlanStartingHeelRangeInM3[1] = charterToEndPlan.getStartingHeelInM3();

			// calculate post-charter plan
			voyageCalculator.calculateVoyagePlan(charterToEndPlan, vesselAvailability.getVessel(), vesselAvailability.getCharterCostCalculator(), charterToEndPlanStartingHeelRangeInM3,
					vesselBaseFuelCalculator.getBaseFuelPrices(vesselAvailability.getVessel(), postCharteringTimes), postCharteringTimes, charterToEndPlan.getSequence());

			// remaining heel may have been overwritten
			charterToEndPlan.setStartingHeelInM3(secondPlanStartHeel);
			charterToEndPlan.setRemainingHeelInM3(secondPlanRemainingHeel);

			final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans = new LinkedList<>();
			charterPlans.add(new Pair<>(upToCharterPlan, preCharterAllocation != null ? preCharterAllocation : preCharteringTimes));
			charterPlans.add(new Pair<>(charterToEndPlan, postCharteringTimes));

			// Note: this assertion is not a valid thing to do here. A GCO could generate a
			// violation, e.g. with tight volume limits it could boil-off down to safety
			// before GCO but with GCO there's too much LNG leftover.
			// assert
			// bigVoyagePlan.getVoyagePlanMetrics()[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]
			// == charterPlans.stream().map(Pair::getFirst).mapToLong(lvp ->
			// lvp.getVoyagePlanMetrics()[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]).sum();

			return charterPlans;
		}
	}

	/**
	 * Finds a charter out option that maximises charter out time, considering
	 * travel time to the charter out port
	 * 
	 * @param ballastStartTime
	 * @param availableTime
	 * @param discharge
	 * @param nextLoad
	 * @param vesselAvailability
	 * @return
	 */

	private GeneratedCharterOutOption getCharterOutOption(final int ballastStartTime, final int availableTime, final IPortSlot dischargeSlot, final IPortSlot nextLoadSlot,
			final IVesselAvailability vesselAvailability) {
		final GeneratedCharterOutOption gcoo = new GeneratedCharterOutOption();
		final IPort discharge = dischargeSlot.getPort();
		final IPort nextLoad = nextLoadSlot.getPort();
		gcoo.setMaxCharteringRevenue(-1);

		// set end ports
		boolean goingToEnd = false;
		final Set<IPort> endPorts = new HashSet<>();
		if (nextLoadSlot instanceof IEndPortSlot && charterMarketProvider.getCharteringPortsForVessel(vesselAvailability.getVessel()).contains(nextLoad)) {
			goingToEnd = true;
			endPorts.add(nextLoad);
		}
		// Scan all the markets for a match
		for (final CharterMarketOptions option : charterMarketProvider.getCharterOutOptions(vesselAvailability.getVessel(), ballastStartTime)) {
			Set<IPort> ports = option.getAllowedPorts();
			if (ports.isEmpty()) {
				// If no ports, charter out at next load port (this is primarily for ITS cases)
				ports = new HashSet<>();
				ports.add(nextLoad);
			} else {
				// end slot exceptions
				if (goingToEnd) {
					if (ports.contains(nextLoad)) {
						// only going to charter at the end port
						ports = endPorts;
					} else {
						// end port not in this option so move on to the next
						continue;
					}
				}
			}
			for (final IPort charterOutPort : ports) {
				final GeneratedCharterOutLeg toCharterPort = calculateShortestTimeToPort(discharge, charterOutPort, vesselAvailability.getVessel(), dischargeSlot, ballastStartTime,
						Math.min(availableTime, option.getMaxDuration()), false);
				if (toCharterPort == null) {
					continue;
				}
				final GeneratedCharterOutLeg fromCharterPort = calculateShortestTimeToPort(charterOutPort, nextLoad, vesselAvailability.getVessel(), dischargeSlot,
						ballastStartTime + toCharterPort.getShortestTime(), Math.min(availableTime - toCharterPort.getShortestTime(), option.getMaxDuration()), true);
				if (fromCharterPort == null) {
					continue;
				}
				final int availableCharteringTime = Math.min(availableTime - toCharterPort.getShortestTime() - fromCharterPort.getShortestTime(), option.getMaxDuration());
				final int charterOutIdleTime = availableTime - toCharterPort.getShortestTime() - fromCharterPort.getShortestTime() - availableCharteringTime;

				final int charterStartTime = ballastStartTime + toCharterPort.getShortestTime();
				final long dailyPrice = (long) option.getCharterPrice(charterStartTime);
				if (charterStartTime < charterMarketProvider.getCharterOutStartTime() || charterStartTime > charterMarketProvider.getCharterOutEndTime()) {
					continue;
				}
				final long charteringRevenue = Calculator.quantityFromRateTime(dailyPrice, availableCharteringTime) / 24L;
				if (availableCharteringTime >= option.getMinDuration() && charteringRevenue > gcoo.getMaxCharteringRevenue()) {
					gcoo.setToCharterPort(toCharterPort);
					gcoo.setFromCharterPort(fromCharterPort);
					gcoo.setOption(option);
					gcoo.setCharterDuration(availableCharteringTime);
					gcoo.setPort(charterOutPort);
					gcoo.setMaxCharteringRevenue(charteringRevenue);
					gcoo.setCharterStartTime(charterStartTime);
					gcoo.setCharterOutIdleTimeHours(charterOutIdleTime);
				}
			}
		}
		return gcoo;
	}

	@Nullable
	private GeneratedCharterOutLeg calculateShortestTimeToPort(final IPort fromPort, final IPort toPort, final IVessel vessel, final IPortSlot fromPortSlot, //
			final int startOfTheJourney, final int availableTime, boolean backwards) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		ERouteOption route = ERouteOption.DIRECT;
		int panamaIdleHours = Integer.MIN_VALUE;

		final List<DistanceMatrixEntry> distances = distanceProvider.getDistanceValues(fromPort, toPort, vessel);
		ECanalEntry panamaCanalEntry = distanceProvider.getRouteOptionCanalEntrance(fromPort, ERouteOption.PANAMA);
		final Pair<Integer, Integer> toAndFromPanama = computeTimeToAndFromPanamaBetweenPorts(fromPort, toPort, vessel, panamaCanalEntry);
		final int travelTimeToPanama = toAndFromPanama.getFirst();
		final int travelTimeFromPanama = toAndFromPanama.getSecond();

		int directTime = Integer.MAX_VALUE;
		DistanceMatrixEntry directEntry = null;
		for (final DistanceMatrixEntry d : distances) {
			final ERouteOption routeOption = d.getRoute();
			final int thisDistance = d.getDistance();
			if (thisDistance == Integer.MAX_VALUE) {
				continue;
			}

			int travelTime = Calculator.getTimeFromSpeedDistance(vessel.getMaxSpeed(), d.getDistance()) + routeCostProvider.getRouteTransitTime(routeOption, vessel);
			if (travelTime == Integer.MAX_VALUE) {
				continue;
			}
			// Check this route is possible in the available time
			if (travelTime > availableTime) {
				continue;
			}
			if (routeOption == ERouteOption.PANAMA) {
				if (travelTimeToPanama == Integer.MAX_VALUE || travelTimeFromPanama == Integer.MAX_VALUE) {
					continue;
				}
				// If Panama, make sure we take into account the idle days
				// It might include the high season when chartering out, whereas we might never
				// actually reach it
				travelTime = travelTimeToPanama;
				int latestPanamaWindowEnd = startOfTheJourney + travelTimeToPanama;
				if (backwards) {
					int t = startOfTheJourney + availableTime - travelTimeFromPanama;
					if (latestPanamaWindowEnd > t) {
						// Is there enough time to go via panama?
						// Should be covered by previous check, but the split voyage could contain a
						// little rounding.
						continue;
					}
					latestPanamaWindowEnd = t;
				}
				if (checkPanamaCanalBookings) {
					panamaIdleHours = getMaxIdleHours(vessel, panamaCanalEntry == ECanalEntry.SouthSide, startOfTheJourney + travelTimeToPanama, latestPanamaWindowEnd + 1);
				} else {
					panamaIdleHours = 0;
				}
				if (panamaIdleHours == Integer.MIN_VALUE || panamaIdleHours < 0) {
					throw new IllegalStateException(String.format("Journey from %s to %s via Panama Canal has an Panama waiting time in %s direction", fromPort.getName(), toPort.getName(),
							panamaCanalEntry == ECanalEntry.SouthSide ? "northbound" : "southbound"));
				}
				travelTime += panamaIdleHours;
				travelTime += travelTimeFromPanama;
			} else if (routeOption == ERouteOption.DIRECT) {
				directTime = travelTime;
				directEntry = d;
			}
			if (travelTime < shortestTime) {
				distance = thisDistance;
				route = routeOption;
				shortestTime = travelTime;
			}
		}
		// No distance found at all.
		if (distance == Integer.MAX_VALUE) {
			return null;
		}

		// heuristic to only use canal if a big improvement
		if (route != ERouteOption.DIRECT && directEntry != null) {
			final double improvement = ((double) directTime - (double) shortestTime) / (double) directTime;
			if (directTime == 0 || improvement < canalChoiceThreshold) {
				distance = directEntry.getDistance();
				route = ERouteOption.DIRECT;
				shortestTime = directTime;
			}
		}
		return new GeneratedCharterOutLeg(distance, route, shortestTime, panamaIdleHours);
	}

	private int getMaxIdleHours(final IVessel vessel, final boolean northbound, int startDateInclusive, int endDateExclusive) {
		if (useBestCanalIdleDays) {
			return panamaBookingsProvider.getBestIdleHours(vessel, startDateInclusive, endDateExclusive, northbound);
		}
		return panamaBookingsProvider.getWorstIdleHours(vessel, startDateInclusive, endDateExclusive, northbound);
	}

	/**
	 * Produces a new large sequence including a generated charter out and a ballast
	 * to and from the charter out port
	 * 
	 * @param currentSequence
	 * @param charterOutOption
	 * @param portTimesRecord
	 * @param ballastIdx
	 * @param ballastStartTime
	 * @return
	 */
	private ExtendedCharterOutSequence constructNewRawSequenceWithCharterOuts(final IVesselAvailability vesselAvailability, final Object[] currentSequence,
			final GeneratedCharterOutOption charterOutOption, final IPortTimesRecord portTimesRecord, final int ballastIdx, final int ballastStartTime) {
		final List<IOptionsSequenceElement> newRawSequence = new ArrayList<>(currentSequence.length);
		final ExtendedCharterOutSequence bigSequence = new ExtendedCharterOutSequence();
		final IVessel vessel = vesselAvailability.getVessel();

		// build new sequence up to and not including last ballast leg
		ERouteOption ladenRoute = null;
		for (int i = 0; i < ballastIdx; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails pd) {
				newRawSequence.add(pd.getOptions().copy());
			} else if (o instanceof VoyageDetails voyageDetails) {
				newRawSequence.add(voyageDetails.getOptions().copy());
				if (voyageDetails.getOptions().getVesselState() == VesselState.Laden) {
					ladenRoute = voyageDetails.getOptions().getRoute();
					bigSequence.setLaden(voyageDetails.getOptions().copy());
				}
			}
		}

		// Now insert elements from the charter out option
		final VoyageDetails originalBallast = (VoyageDetails) currentSequence[ballastIdx];

		// These will be updated later on
		final HeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(0, Long.MAX_VALUE, VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0), false);
		final HeelOptionSupplier heelOptionSupplier = new HeelOptionSupplier(0, 0, originalBallast.getOptions().getCargoCVValue(), new ConstantHeelPriceCalculator(0));

		// now update port slot
		final GeneratedCharterOutVesselEventPortSlot charterOutPortSlot = new GeneratedCharterOutVesselEventPortSlot(

				/* ID */ String.format("gco-%s-%s", originalBallast.getOptions().getFromPortSlot().getId(), originalBallast.getOptions().getToPortSlot().getId()), //
				/* time window */ null, //
				/* Start / End Port */ charterOutOption.getPort(), //
				/* Hire Revenue */ charterOutOption.getMaxCharteringRevenue(), //
				/* Repositioning */ 0, /* Duration */ charterOutOption.getCharterDuration(), //
				heelOptionConsumer, heelOptionSupplier, charterOutOption.getOption().getMarket());

		// copy port times record
		final Triple<IPortSlot, Integer, Integer> charterOutTimesRecord = new Triple<>(charterOutPortSlot, charterOutOption.getCharterStartTime(), charterOutOption.getCharterDuration());

		// (1) ballast to charter out

		CostType dischargeToCharterPortCostType = CostType.Ballast;
		// Is this a round trip?
		if (ladenRoute == charterOutOption.getToCharterPort().getRoute()) {
			dischargeToCharterPortCostType = CostType.RoundTripBallast;
		}
		final long dischargeToCharterPortRouteCosts = routeCostProvider.getRouteCost(charterOutOption.getToCharterPort().getRoute(), //
				originalBallast.getOptions().getFromPortSlot().getPort(), charterOutPortSlot.getPort(), //
				vessel, ballastStartTime, CostType.Ballast);

		final VoyageOptions dischargeToCharterPortVoyageOptions = new VoyageOptions(originalBallast.getOptions().getFromPortSlot(), charterOutPortSlot);
		dischargeToCharterPortVoyageOptions.setRoute(charterOutOption.getToCharterPort().getRoute(), charterOutOption.getToCharterPort().getDistance(), dischargeToCharterPortRouteCosts);
		dischargeToCharterPortVoyageOptions.setAvailableTime(charterOutOption.getToCharterPort().getShortestTime());
		dischargeToCharterPortVoyageOptions.setVesselState(VesselState.Ballast);
		dischargeToCharterPortVoyageOptions.setVessel(originalBallast.getOptions().getVessel());
		dischargeToCharterPortVoyageOptions.setShouldBeCold(originalBallast.getOptions().shouldBeCold());
		dischargeToCharterPortVoyageOptions.setWarm(originalBallast.getOptions().isWarm());
		dischargeToCharterPortVoyageOptions.setAllowCooldown(false);
		dischargeToCharterPortVoyageOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());

		// (2) charter out

		final PortOptions generatedCharterPortOptions = new PortOptions(charterOutPortSlot);
		generatedCharterPortOptions.setVessel(originalBallast.getOptions().getVessel());
		generatedCharterPortOptions.setVisitDuration(charterOutOption.getCharterDuration());
		generatedCharterPortOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());

		charterOutOption.setPortOptions(generatedCharterPortOptions);
		// (3) ballast to return port
		final int startOfPostCharterVoyage = charterOutOption.getCharterStartTime() + charterOutOption.getCharterDuration();
		final long charterToReturnPortRouteCosts = routeCostProvider.getRouteCost(charterOutOption.getFromCharterPort().getRoute(), //
				charterOutPortSlot.getPort(), originalBallast.getOptions().getToPortSlot().getPort(), //
				vessel, startOfPostCharterVoyage, CostType.Ballast);

		final VoyageOptions charterToReturnPortVoyageOptions = new VoyageOptions(charterOutPortSlot, originalBallast.getOptions().getToPortSlot());
		charterToReturnPortVoyageOptions.setRoute(charterOutOption.getFromCharterPort().getRoute(), charterOutOption.getFromCharterPort().getDistance(), charterToReturnPortRouteCosts);
		charterToReturnPortVoyageOptions.setAvailableTime(charterOutOption.getFromCharterPort().getShortestTime() + charterOutOption.getCharterOutIdleTimeHours());
		charterToReturnPortVoyageOptions.setVesselState(VesselState.Ballast);
		charterToReturnPortVoyageOptions.setVessel(originalBallast.getOptions().getVessel());
		charterToReturnPortVoyageOptions.setShouldBeCold(originalBallast.getOptions().shouldBeCold());
		charterToReturnPortVoyageOptions.setWarm(originalBallast.getOptions().isWarm());
		charterToReturnPortVoyageOptions.setAllowCooldown(originalBallast.getOptions().getAllowCooldown());
		charterToReturnPortVoyageOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());
		// add options to new sequence
		newRawSequence.add(dischargeToCharterPortVoyageOptions);
		newRawSequence.add(generatedCharterPortOptions);
		newRawSequence.add(charterToReturnPortVoyageOptions);
		newRawSequence.add(((PortDetails) currentSequence[currentSequence.length - 1]).getOptions().copy());
		final IPortTimesRecord bigPlanPortTimesRecord = createPortTimesRecordForExtendedPlan(portTimesRecord, charterOutTimesRecord);

		if (charterOutOption.getToCharterPort().getRoute() == ERouteOption.PANAMA) {
			int idleDays = charterOutOption.getToCharterPort().getPanamaIdleHours();
			if (idleDays != Integer.MAX_VALUE) {
				bigPlanPortTimesRecord.setSlotAdditionalPanamaIdleHours(originalBallast.getOptions().getFromPortSlot(), idleDays);
				// TODO: Assuming there will be no excess idle time with the GCO
				bigPlanPortTimesRecord.setSlotMaxAvailablePanamaIdleHours(originalBallast.getOptions().getFromPortSlot(), idleDays);
			}
		} else {
			// Reset field if route has changed as this was copied from the original record
			bigPlanPortTimesRecord.setSlotAdditionalPanamaIdleHours(originalBallast.getOptions().getFromPortSlot(), 0);
			bigPlanPortTimesRecord.setSlotMaxAvailablePanamaIdleHours(originalBallast.getOptions().getFromPortSlot(), 0);
		}
		if (charterOutOption.getFromCharterPort().getRoute() == ERouteOption.PANAMA) {
			int idleDays = charterOutOption.getFromCharterPort().getPanamaIdleHours();

			if (idleDays != Integer.MAX_VALUE) {
				bigPlanPortTimesRecord.setSlotAdditionalPanamaIdleHours(charterOutPortSlot, idleDays);
				// TODO: Assuming there will be no excess idle time with the GCO
				bigPlanPortTimesRecord.setSlotMaxAvailablePanamaIdleHours(charterOutPortSlot, idleDays);
			}
		}

		// store data in extended sequence data structure
		setExtendedSequence(newRawSequence, bigSequence, bigPlanPortTimesRecord, dischargeToCharterPortVoyageOptions, generatedCharterPortOptions, charterToReturnPortVoyageOptions);

		return bigSequence;
	}

	private Pair<Integer, Integer> computeTimeToAndFromPanamaBetweenPorts(final IPort fromPort, final IPort toPort, final IVessel vessel, final ECanalEntry panamaCanalEntry) {
		int travelTimeToPanama = Integer.MAX_VALUE;
		int travelTimeFromPanama = Integer.MAX_VALUE;
		IPort panamaCanalEntryPort = distanceProvider.getRouteOptionEntryPort(ERouteOption.PANAMA, panamaCanalEntry);
		if (panamaCanalEntryPort != null) {
			int vesselMaxSpeed = vessel.getMaxSpeed();
			travelTimeToPanama = distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, fromPort, panamaCanalEntryPort, Math.min(panamaBookingsProvider.getSpeedToCanal(), vesselMaxSpeed));
			travelTimeFromPanama = distanceProvider.getTravelTimeViaCanal(ERouteOption.PANAMA, vessel, panamaCanalEntryPort, toPort, vesselMaxSpeed);
		}
		return new Pair(travelTimeToPanama, travelTimeFromPanama);
	}

	private void setExtendedSequence(final List<IOptionsSequenceElement> newRawSequence, final ExtendedCharterOutSequence bigSequence, final IPortTimesRecord bigPlanPortTimesRecord,
			final VoyageOptions dischargeToCharterPortVoyageOptions, final PortOptions generatedCharterPortOptions, final VoyageOptions charterToReturnPortVoyageOptions) {
		bigSequence.setToCharter(dischargeToCharterPortVoyageOptions);
		bigSequence.setFromCharter(charterToReturnPortVoyageOptions);
		bigSequence.setCharter(generatedCharterPortOptions);
		bigSequence.setSequence(newRawSequence);
		bigSequence.setPortTimesRecord(bigPlanPortTimesRecord);
	}

	/**
	 * Create the port times record for a new large sequence that includes the
	 * generated charter out
	 * 
	 * @param existing
	 * @param slotsToAdd
	 * @return
	 */

	private IPortTimesRecord createPortTimesRecordForExtendedPlan(final IPortTimesRecord existing, final Triple<IPortSlot, Integer, Integer>... slotsToAdd) {
		final PortTimesRecord newPortsTimeRecord = new PortTimesRecord();
		// existing
		for (int i = 0; i < existing.getSlots().size(); i++) {
			newPortsTimeRecord.setSlotTime(existing.getSlots().get(i), existing.getSlotTime(existing.getSlots().get(i)));
			newPortsTimeRecord.setSlotDuration(existing.getSlots().get(i), existing.getSlotDuration(existing.getSlots().get(i)));
			newPortsTimeRecord.setSlotExtraIdleTime(existing.getSlots().get(i), existing.getSlotExtraIdleTime(existing.getSlots().get(i)));
			newPortsTimeRecord.setSlotAdditionalPanamaIdleHours(existing.getSlots().get(i), existing.getSlotAdditionaPanamaIdleHours(existing.getSlots().get(i)));
			newPortsTimeRecord.setSlotMaxAvailablePanamaIdleHours(existing.getSlots().get(i), existing.getSlotMaxAdditionaPanamaIdleHours(existing.getSlots().get(i)));
		}
		// new
		for (final Triple<IPortSlot, Integer, Integer> slotToAdd : slotsToAdd) {
			newPortsTimeRecord.setSlotTime(slotToAdd.getFirst(), slotToAdd.getSecond());
			newPortsTimeRecord.setSlotDuration(slotToAdd.getFirst(), slotToAdd.getThird());
			newPortsTimeRecord.setSlotExtraIdleTime(slotToAdd.getFirst(), 0);
		}
		// existing return
		final IPortSlot returnSlot = existing.getReturnSlot();
		if (returnSlot != null) {
			newPortsTimeRecord.setReturnSlotTime(returnSlot, existing.getSlotTime(returnSlot));
		}
		return newPortsTimeRecord;
	}

	/**
	 * Create a ports times record for both child sequences
	 * 
	 * @param existing
	 * @return
	 */
	private Pair<IPortTimesRecord, IPortTimesRecord> splitPortTimesRecord(final IPortTimesRecord existing) {
		final PortTimesRecord firstPortsTimeRecord = new PortTimesRecord();
		final PortTimesRecord secondPortsTimeRecord = new PortTimesRecord();
		// first (... D -- CO)
		for (int i = 0; i < existing.getSlots().size() - 1; i++) {
			firstPortsTimeRecord.setSlotTime(existing.getSlots().get(i), existing.getSlotTime(existing.getSlots().get(i)));
			firstPortsTimeRecord.setSlotDuration(existing.getSlots().get(i), existing.getSlotDuration(existing.getSlots().get(i)));
			firstPortsTimeRecord.setSlotAdditionalPanamaIdleHours(existing.getSlots().get(i), existing.getSlotAdditionaPanamaIdleHours(existing.getSlots().get(i)));
			firstPortsTimeRecord.setSlotMaxAvailablePanamaIdleHours(existing.getSlots().get(i), existing.getSlotMaxAdditionaPanamaIdleHours(existing.getSlots().get(i)));
		}
		firstPortsTimeRecord.setReturnSlotTime(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		// second (... CO -- NL)
		secondPortsTimeRecord.setSlotTime(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setSlotDuration(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotDuration(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setSlotAdditionalPanamaIdleHours(existing.getSlots().get(existing.getSlots().size() - 1),
				existing.getSlotAdditionaPanamaIdleHours(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setSlotMaxAvailablePanamaIdleHours(existing.getSlots().get(existing.getSlots().size() - 1),
				existing.getSlotMaxAdditionaPanamaIdleHours(existing.getSlots().get(existing.getSlots().size() - 1)));
		final IPortSlot returnSlot = existing.getReturnSlot();
		if (returnSlot != null) {
			secondPortsTimeRecord.setReturnSlotTime(returnSlot, existing.getSlotTime(returnSlot));
		}
		return new Pair<>(firstPortsTimeRecord, secondPortsTimeRecord);
	}

	/**
	 * Run the VPO on the parent sequence and return a voyage plan.
	 * 
	 * @param vessel
	 * @param originalVoyagePlan
	 * @param startHeelInM3
	 * @param bigSequence
	 * @return
	 */
	private @Nullable VoyagePlan runVPOOnBigSequence(final IVessel vessel, final VoyagePlan originalVoyagePlan, final ICharterCostCalculator charterCostCalculator, final long[] startHeelRangeInM3,
			final ExtendedCharterOutSequence bigSequence) {
		final int[] baseFuelPricePerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, bigSequence.getPortTimesRecord());

		final List<@NonNull IVoyagePlanChoice> vpoChoices = new LinkedList<>();
		final boolean isCargo = isFirstSequenceElementIsLoadSlot(bigSequence);
		if (!vessel.hasReliqCapability()) {
			if (isCargo || checkForNonEmptyHeel(startHeelRangeInM3)) {
				// Add in NBO etc choices (ballast 1)
				vpoChoices.add(new TravelVoyagePlanChoice(bigSequence.getLaden() == null ? null : bigSequence.getLaden(), bigSequence.getToCharter()));
				vpoChoices.add(new IdleNBOVoyagePlanChoice(bigSequence.getToCharter()));
				// Add in NBO etc choices (ballast 2)
				vpoChoices.add(new TravelVoyagePlanChoice(bigSequence.getToCharter(), bigSequence.getFromCharter()));
				vpoChoices.add(new IdleNBOVoyagePlanChoice(bigSequence.getFromCharter()));
			} else {
				bigSequence.getToCharter().setIdleFuelChoice(IdleFuelChoice.BUNKERS);
				bigSequence.getToCharter().setTravelFuelChoice(TravelFuelChoice.BUNKERS);
				bigSequence.getFromCharter().setIdleFuelChoice(IdleFuelChoice.BUNKERS);
				bigSequence.getFromCharter().setTravelFuelChoice(TravelFuelChoice.BUNKERS);
			}
		} else {
			// Add in NBO etc choices (ballast 1)
			vpoChoices.add(new ReliqVoyagePlanChoice(bigSequence.getLaden() == null ? null : bigSequence.getLaden(), bigSequence.getToCharter()));
			// Add in NBO etc choices (ballast 2)
			vpoChoices.add(new ReliqVoyagePlanChoice(bigSequence.getToCharter(), bigSequence.getFromCharter()));
		}
		// Calculate our new plan
		final VoyagePlan newVoyagePlan = voyagePlanOptimiser.optimise(null, vessel, startHeelRangeInM3, baseFuelPricePerMT, charterCostCalculator, bigSequence.getPortTimesRecord(),
				bigSequence.getSequence(), vpoChoices);

		return newVoyagePlan;
	}

	private boolean isFirstSequenceElementIsLoadSlot(final ExtendedCharterOutSequence bigSequence) {
		final IOptionsSequenceElement foo = bigSequence.getSequence().get(0);
		if (foo instanceof PortOptions portOptions) {
			if (portOptions.getPortSlot().getPortType() == PortType.Load) {
				return true;
			}
		}
		return false;
	}

	private boolean checkForNonEmptyHeel(final long[] startHeelRangeInM3) {
		boolean nonEmptyHeel = true;
		if (startHeelRangeInM3.length == 2) {
			if (startHeelRangeInM3[0] == 0 && startHeelRangeInM3[1] == 0) {
				nonEmptyHeel = false;
			}
		}
		return nonEmptyHeel;
	}

	/**
	 * Split the parent sequence into two child sequences
	 * 
	 * @param vp
	 * @return
	 */
	private Pair<IDetailsSequenceElement[], IDetailsSequenceElement[]> splitSequenceDetails(final VoyagePlan vp) {
		final int elementsUpToCO = vp.getSequence().length - 2;
		final IDetailsSequenceElement[] first = new IDetailsSequenceElement[elementsUpToCO];
		final IDetailsSequenceElement[] second = new IDetailsSequenceElement[3];
		// .... up to Ballast -- CO
		for (int i = 0; i < elementsUpToCO; i++) {
			first[i] = vp.getSequence()[i];
		}
		// CO -- Ballast -- Next Load
		for (int i = elementsUpToCO - 1; i < vp.getSequence().length; i++) {
			second[i - (elementsUpToCO - 1)] = vp.getSequence()[i];
		}
		return new Pair<>(first, second);
	}

	/**
	 * Split the parent plan into two child plans
	 * 
	 * @param bigVoyagePlan
	 * @return
	 */
	private Pair<VoyagePlan, VoyagePlan> splitVoyagePlan(final VoyagePlan bigVoyagePlan) {
		// now split into two
		final VoyagePlan upToCharterPlan = new VoyagePlan();
		final VoyagePlan charterToEndPlan = new VoyagePlan();

		final Pair<IDetailsSequenceElement[], IDetailsSequenceElement[]> splitSequenceDetails = splitSequenceDetails(bigVoyagePlan);
		upToCharterPlan.setSequence(splitSequenceDetails.getFirst());
		charterToEndPlan.setSequence(splitSequenceDetails.getSecond());

		return new Pair<>(upToCharterPlan, charterToEndPlan);
	}

	private void setPlanParameters(final VoyagePlan bigVoyagePlan, final VoyagePlan charterToEndPlan, final VoyagePlan upToCharterPlan, final VoyagePlan originalVP, final long remainingHeelInM3) {
		final long totalBoilOffPostCharter = getTotalBoilOffInSequence(charterToEndPlan.getSequence());

		charterToEndPlan.setRemainingHeelInM3(remainingHeelInM3);
		charterToEndPlan.setStartingHeelInM3(remainingHeelInM3 + totalBoilOffPostCharter);

		upToCharterPlan.setStartingHeelInM3(bigVoyagePlan.getStartingHeelInM3());
		upToCharterPlan.setRemainingHeelInM3(remainingHeelInM3 + totalBoilOffPostCharter);

		upToCharterPlan.setIgnoreEnd(true);
		charterToEndPlan.setIgnoreEnd(originalVP.isIgnoreEnd());
	}

	private long getTotalBoilOffInSequence(final IDetailsSequenceElement[] sequence) {
		long totalBoilOff = 0;
		for (final IDetailsSequenceElement sequenceElement : sequence) {
			if (sequenceElement instanceof VoyageDetails vd) {
				totalBoilOff += getTotalBoilOffForVoyage(vd);
			}
		}
		return totalBoilOff;
	}

	private long getTotalBoilOffForVoyage(final VoyageDetails voyageDetails) {
		long totalBoilOffM3 = 0;
		for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
			assert fk != null;
			totalBoilOffM3 += voyageDetails.getFuelConsumption(fk);
			totalBoilOffM3 += voyageDetails.getRouteAdditionalConsumption(fk);
		}
		return totalBoilOffM3;
	}

	/**
	 * Loop through the first voyage plan (up to the GCO) and find an element to
	 * extract heel options information
	 * 
	 * @param generatedCharterOutVesselEvent
	 * @param sequence
	 * @param heelVolume
	 * @param portTimesRecord
	 * @return
	 */
	private void setHeelOptions(final IGeneratedCharterOutVesselEvent generatedCharterOutVesselEvent, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement[] sequence,
			final long heelVolume) {
		IHeelPriceCalculator heelPriceCalculator = null;
		int cv = 0;

		final IDetailsSequenceElement startOfSequence = sequence[0];
		if (startOfSequence instanceof PortDetails pd) {
			final IPortSlot portSlot = pd.getOptions().getPortSlot();
			if (portSlot instanceof IHeelOptionSupplierPortSlot hospt) {
				heelPriceCalculator = hospt.getHeelOptionsSupplier().getHeelPriceCalculator();
				cv = hospt.getHeelOptionsSupplier().getHeelCVValue();
			} else if (portSlot instanceof ILoadSlot loadSlot) {
				IDischargeSlot discharge = null;
				for (int i = sequence.length - 1; i >= 0; i--) {
					if (sequence[i]instanceof PortDetails pdi) {
						if (pdi.getOptions().getPortSlot()instanceof IDischargeSlot ds) {
							discharge = ds;
							break;
						}
					}
				}
				if (discharge != null) {
					heelPriceCalculator = new ConstantHeelPriceCalculator(discharge.getDischargePriceCalculator().estimateSalesUnitPrice(discharge, portTimesRecord, null));
				}
				cv = loadSlot.getCargoCVValue();
			} else {
				heelPriceCalculator = new ConstantHeelPriceCalculator(0);
			}
		}
		assert heelPriceCalculator != null;
		final HeelOptionConsumer heelConsumer = new HeelOptionConsumer(heelVolume, heelVolume, heelVolume > 0 ? VesselTankState.MUST_BE_COLD : VesselTankState.MUST_BE_WARM, heelPriceCalculator,
				false);
		final HeelOptionSupplier heelSupplier = new HeelOptionSupplier(heelVolume, heelVolume, cv, heelPriceCalculator);
		generatedCharterOutVesselEvent.setHeelConsumer(heelConsumer);
		generatedCharterOutVesselEvent.setHeelSupplier(heelSupplier);
	}
}