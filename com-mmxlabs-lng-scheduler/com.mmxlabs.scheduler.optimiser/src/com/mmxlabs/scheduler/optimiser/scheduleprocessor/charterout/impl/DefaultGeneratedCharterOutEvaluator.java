/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.GeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.FBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IdleNBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.NBOTravelVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class DefaultGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {

	@Inject
	Injector injector;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private Provider<IVolumeAllocator> volumeAllocator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IVoyagePlanOptimiser vpo;

	@Inject
	private IRouteCostProvider routeCostProvider;

	private final GeneratedCharterOutOptionCache generatedCharterOutOptionCache = new GeneratedCharterOutOptionCache();

	private static final double canalChoiceThreshold = 0.1; // percentage improvement required to choose a canal route

	private static final boolean DO_CACHING = false;

	private final int hits = 0;

	@Override
	public List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final int vesselStartTime, final long startHeelVolumeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp,
			final IPortTimesRecord portTimesRecord) {
		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		final long startHeelInM3 = startHeelVolumeInM3;

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
		final IPortSlot firstSlot = firstDetails.getOptions().getPortSlot();
		final int firstStartTime = portTimesRecord.getSlotTime(firstSlot);
		final IPortSlot slotBeforeCharter = ballastDetails.getOptions().getFromPortSlot();
		final IPortSlot slotAfterCharter = ballastDetails.getOptions().getToPortSlot();
		boolean solutionSeen = false;
		List<Pair<VoyagePlan, IPortTimesRecord>> cachedSolution = null;

		if (DO_CACHING) {
			solutionSeen = isSolutionCached(firstStartTime, startHeelInM3, ballastStartTime, availableTime, firstSlot, slotBeforeCharter, slotAfterCharter, vesselAvailability);
			if (solutionSeen) {
				// try to retrieve solution from cache first
				cachedSolution = getCachedSolution(firstStartTime, startHeelInM3, ballastStartTime, availableTime, firstSlot, slotBeforeCharter, slotAfterCharter, vesselAvailability);
			}
		}

		if (solutionSeen) {
			return cachedSolution;
		} else {
			// no cached solution, generate a split voyage plan and cache
			final GeneratedCharterOutOption gcoMarket = getCharterOutOption(ballastStartTime, availableTime, slotBeforeCharter, slotAfterCharter, vesselAvailability);

			// Have we found a market?
			if (gcoMarket.getOption() == null) {
				if (DO_CACHING) {
					addCachedSolution(firstStartTime, startHeelInM3, ballastStartTime, availableTime, firstSlot, slotBeforeCharter, slotAfterCharter, vesselAvailability, null, null);
				}
				return null;
			}

			final ExtendedCharterOutSequence bigSequence = constructNewRawSequenceWithCharterOuts(vesselAvailability, currentSequence, gcoMarket, portTimesRecord, ballastIdx, ballastStartTime);

			final VoyagePlan bigVoyagePlan = runVPOOnBigSequence(vesselAvailability.getVessel(), vp, startHeelInM3, bigSequence);

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
			// calculate pre-charter plan
			voyageCalculator.calculateVoyagePlan(upToCharterPlan, vesselAvailability.getVessel(), bigVoyagePlan.getStartingHeelInM3(),
					vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), preCharteringTimes), preCharteringTimes, upToCharterPlan.getSequence());
			// remaining heel may have been overwritten
			upToCharterPlan.setRemainingHeelInM3(firstPlanRemainingHeel);

			final IAllocationAnnotation preCharterAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, upToCharterPlan, preCharteringTimes);

			// add on delta to starting heel and remaining heel
			if (preCharterAllocation != null) {
				final long delta = preCharterAllocation.getRemainingHeelVolumeInM3() - charterToEndPlan.getStartingHeelInM3();
				charterToEndPlan.setStartingHeelInM3(charterToEndPlan.getStartingHeelInM3() + delta);
				charterToEndPlan.setRemainingHeelInM3(charterToEndPlan.getRemainingHeelInM3() + delta);
				// now we set the heel options on the charter out event as it will be need when the second voyage plan is calculated
				final IGeneratedCharterOutVesselEventPortSlot charter = (IGeneratedCharterOutVesselEventPortSlot) ((PortDetails) charterToEndPlan.getSequence()[0]).getOptions().getPortSlot();
				setHeelOptions(charter.getVesselEvent(), preCharteringTimes, upToCharterPlan.getSequence(), charterToEndPlan.getStartingHeelInM3());
			}

			// TODO: really we need partial boiloff here (see BugzID: 1798)
			final long secondPlanStartHeel = charterToEndPlan.getStartingHeelInM3();
			final long secondPlanRemainingHeel = charterToEndPlan.getRemainingHeelInM3();
			// calculate post-charter plan
			voyageCalculator.calculateVoyagePlan(charterToEndPlan, vesselAvailability.getVessel(), charterToEndPlan.getStartingHeelInM3(),
					vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), postCharteringTimes), postCharteringTimes, charterToEndPlan.getSequence());
			// remaining heel may have been overwritten
			charterToEndPlan.setStartingHeelInM3(secondPlanStartHeel);
			charterToEndPlan.setRemainingHeelInM3(secondPlanRemainingHeel);

			final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans = new LinkedList<Pair<VoyagePlan, IPortTimesRecord>>();
			charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(upToCharterPlan, preCharterAllocation != null ? preCharterAllocation : preCharteringTimes));
			charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(charterToEndPlan, postCharteringTimes));
			if (DO_CACHING) {
				setAdditionalDataForCaching(gcoMarket);
				if (cachedSolution == null) {
					addCachedSolution(firstStartTime, startHeelInM3, ballastStartTime, availableTime, firstSlot, slotBeforeCharter, slotAfterCharter, vesselAvailability, charterPlans, gcoMarket);
				}
			}
			return charterPlans;
		}
	}

	/**
	 * Finds a charter out option that maximises charter out time, considering travel time to the charter out port
	 * 
	 * @param ballastStartTime
	 * @param availableTime
	 * @param discharge
	 * @param nextLoad
	 * @param vesselAvailability
	 * @return
	 */
	@NonNull
	private GeneratedCharterOutOption getCharterOutOption(final int ballastStartTime, final int availableTime, final IPortSlot dischargeSlot, final IPortSlot nextLoadSlot,
			final IVesselAvailability vesselAvailability) {
		final GeneratedCharterOutOption gcoo = new GeneratedCharterOutOption();
		final IPort discharge = dischargeSlot.getPort();
		final IPort nextLoad = nextLoadSlot.getPort();
		gcoo.setMaxCharteringRevenue(-1);

		// set end ports
		boolean goingToEnd = false;
		final Set<IPort> endPorts = new HashSet<IPort>();
		if (nextLoadSlot instanceof EndPortSlot && charterMarketProvider.getCharteringPortsForVesselClass(vesselAvailability.getVessel().getVesselClass()).contains(nextLoad)) {
			goingToEnd = true;
			endPorts.add(nextLoad);
		}
		// Scan all the markets for a match
		for (final CharterMarketOptions option : charterMarketProvider.getCharterOutOptions(vesselAvailability.getVessel().getVesselClass(), ballastStartTime)) {
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
				final Triple<Integer, ERouteOption, Integer> toCharterPort = calculateShortestTimeToPort(discharge, charterOutPort, vesselAvailability.getVessel(), ballastStartTime);
				if (toCharterPort == null) {
					continue;
				}
				final Triple<Integer, ERouteOption, Integer> fromCharterPort = calculateShortestTimeToPort(charterOutPort, nextLoad, vesselAvailability.getVessel(), ballastStartTime);
				if (fromCharterPort == null) {
					continue;
				}
				final int availableCharteringTime = availableTime - toCharterPort.getThird() - fromCharterPort.getThird();
				final int charterStartTime = ballastStartTime + toCharterPort.getThird();
				final long dailyPrice = (long) option.getCharterPrice(charterStartTime);
				if (charterStartTime < charterMarketProvider.getCharterOutStartTime()) {
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
				}
			}
		}
		return gcoo;
	}

	@Nullable
	private Triple<Integer, ERouteOption, Integer> calculateShortestTimeToPort(final IPort slotPort, final IPort charterPort, final IVessel vessel, final int voyageStartTime) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		ERouteOption route = ERouteOption.DIRECT;

		final List<Pair<ERouteOption, Integer>> distances = distanceProvider.getDistanceValues(slotPort, charterPort, voyageStartTime);
		int directTime = Integer.MAX_VALUE;
		Pair<ERouteOption, Integer> directEntry = null;
		for (final Pair<ERouteOption, Integer> d : distances) {
			final ERouteOption routeOption = d.getFirst();
			final int thisDistance = d.getSecond();
			if (thisDistance == Integer.MAX_VALUE) {
				continue;
			}
			final int travelTime = Calculator.getTimeFromSpeedDistance(vessel.getVesselClass().getMaxSpeed(), d.getSecond()) + routeCostProvider.getRouteTransitTime(routeOption, vessel);
			if (routeOption == ERouteOption.DIRECT) {
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
				distance = directEntry.getSecond();
				route = ERouteOption.DIRECT;
				shortestTime = directTime;
			}
		}
		return new Triple<>(distance, route, shortestTime);
	}

	/**
	 * Produces a new large sequence including a generated charter out and a ballast to and from the charter out port
	 * 
	 * @param currentSequence
	 * @param charterOutOption
	 * @param portTimesRecord
	 * @param ballastIdx
	 * @param ballastStartTime
	 * @return
	 */
	private ExtendedCharterOutSequence constructNewRawSequenceWithCharterOuts(final @NonNull IVesselAvailability vesselAvailability, final Object[] currentSequence,
			final GeneratedCharterOutOption charterOutOption, final IPortTimesRecord portTimesRecord, final int ballastIdx, final int ballastStartTime) {
		final List<IOptionsSequenceElement> newRawSequence = new ArrayList<IOptionsSequenceElement>(currentSequence.length);
		final ExtendedCharterOutSequence bigSequence = new ExtendedCharterOutSequence();
		final IVessel vessel = vesselAvailability.getVessel();

		// build new sequence up to and not including last ballast leg
		IPort loadPort = null;
		ERouteOption ladenRoute = null;
		for (int i = 0; i < ballastIdx; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				newRawSequence.add(((PortDetails) o).getOptions().clone());
			} else if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				newRawSequence.add(voyageDetails.getOptions().clone());
				if (voyageDetails.getOptions().getVesselState() == VesselState.Laden) {
					loadPort = voyageDetails.getOptions().getFromPortSlot().getPort();
					ladenRoute = voyageDetails.getOptions().getRoute();
					bigSequence.setLaden(voyageDetails.getOptions().clone());
				}
			}
		}

		// Now insert elements from the charter out option
		final VoyageDetails originalBallast = (VoyageDetails) currentSequence[ballastIdx];
		final IPortSlot existingSlotUsedToGenerateCharterOut = originalBallast.getOptions().getFromPortSlot();

		// now update port slot
		final GeneratedCharterOutVesselEventPortSlot charterOutPortSlot = new GeneratedCharterOutVesselEventPortSlot(

				/* ID */ String.format("gco-%s-%s", originalBallast.getOptions().getFromPortSlot().getPort(), originalBallast.getOptions().getToPortSlot().getPort()), //
				/* Start / End Port */ charterOutOption.getPort(), //
				/* Hire Revenue */ charterOutOption.getMaxCharteringRevenue(), //
				/* Repositioning */ 0, /* Duration */ charterOutOption.getCharterDuration());
		// injector.injectMembers(charterOutPortSlot);

		// copy port times record
		final Triple<IPortSlot, Integer, Integer> charterOutTimesRecord = new Triple<IPortSlot, Integer, Integer>(charterOutPortSlot, charterOutOption.getCharterStartTime(),
				charterOutOption.getCharterDuration());

		// (1) ballast to charter out

		CostType dischargeToCharterPortCostType = CostType.Ballast;
		// Is this a round trip?
		if (ladenRoute == charterOutOption.getToCharterPort().getSecond()) {
			dischargeToCharterPortCostType = CostType.RoundTripBallast;
		}
		final long dischargeToCharterPortRouteCosts = routeCostProvider.getRouteCost(charterOutOption.getToCharterPort().getSecond(), vessel, CostType.Ballast);

		final VoyageOptions dischargeToCharterPortVoyageOptions = new VoyageOptions(originalBallast.getOptions().getFromPortSlot(), charterOutPortSlot);
		dischargeToCharterPortVoyageOptions.setRoute(charterOutOption.getToCharterPort().getSecond(), charterOutOption.getToCharterPort().getFirst(), dischargeToCharterPortRouteCosts);
		dischargeToCharterPortVoyageOptions.setAvailableTime(charterOutOption.getToCharterPort().getThird());
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

		// final IGeneratedCharterOutVesselEvent charterOutEvent = charterOutPortSlot.getVesselEvent();
		// charterOutEvent.setStartPort(charterOutOption.getPort());
		// charterOutEvent.setEndPort(charterOutOption.getPort());
		// charterOutEvent.setHireOutRevenue(charterOutOption.getMaxCharteringRevenue());
		// charterOutEvent.setRepositioning(0);
		// charterOutEvent.setDurationHours(charterOutOption.getCharterDuration());

		charterOutOption.setPortOptions(generatedCharterPortOptions);
		// (3) ballast to return port

		final long charterToReturnPortRouteCosts = routeCostProvider.getRouteCost(charterOutOption.getFromCharterPort().getSecond(), vessel, CostType.Ballast);

		final VoyageOptions charterToReturnPortVoyageOptions = new VoyageOptions(charterOutPortSlot, originalBallast.getOptions().getToPortSlot());
		charterToReturnPortVoyageOptions.setRoute(charterOutOption.getFromCharterPort().getSecond(), charterOutOption.getFromCharterPort().getFirst(), charterToReturnPortRouteCosts);
		charterToReturnPortVoyageOptions.setAvailableTime(charterOutOption.getFromCharterPort().getThird());
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
		newRawSequence.add(((PortDetails) currentSequence[currentSequence.length - 1]).getOptions().clone());
		final IPortTimesRecord bigPlanPortTimesRecord = createPortTimesRecordForExtendedPlan(portTimesRecord, charterOutTimesRecord);

		// store data in extended sequence data structure
		setExtendedSequence(newRawSequence, bigSequence, bigPlanPortTimesRecord, dischargeToCharterPortVoyageOptions, generatedCharterPortOptions, charterToReturnPortVoyageOptions);

		return bigSequence;
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
	 * Create the port times record for a new large sequence that includes the generated charter out
	 * 
	 * @param existing
	 * @param slotsToAdd
	 * @return
	 */
	@NonNull
	private IPortTimesRecord createPortTimesRecordForExtendedPlan(@NonNull final IPortTimesRecord existing, final Triple<IPortSlot, Integer, Integer>... slotsToAdd) {
		final PortTimesRecord newPortsTimeRecord = new PortTimesRecord();
		// existing
		for (int i = 0; i < existing.getSlots().size(); i++) {
			newPortsTimeRecord.setSlotTime(existing.getSlots().get(i), existing.getSlotTime(existing.getSlots().get(i)));
			newPortsTimeRecord.setSlotDuration(existing.getSlots().get(i), existing.getSlotDuration(existing.getSlots().get(i)));
		}
		// new
		for (final Triple<IPortSlot, Integer, Integer> slotToAdd : slotsToAdd) {
			newPortsTimeRecord.setSlotTime(slotToAdd.getFirst(), slotToAdd.getSecond());
			newPortsTimeRecord.setSlotDuration(slotToAdd.getFirst(), slotToAdd.getThird());
		}
		// existing return
		final IPortSlot returnSlot = existing.getReturnSlot();
		newPortsTimeRecord.setReturnSlotTime(returnSlot, existing.getSlotTime(returnSlot));
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
		}
		firstPortsTimeRecord.setReturnSlotTime(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		// second (... CO -- NL)
		secondPortsTimeRecord.setSlotTime(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setSlotDuration(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setReturnSlotTime(existing.getReturnSlot(), existing.getSlotTime(existing.getReturnSlot()));
		return new Pair<IPortTimesRecord, IPortTimesRecord>(firstPortsTimeRecord, secondPortsTimeRecord);
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
	private VoyagePlan runVPOOnBigSequence(@NonNull final IVessel vessel, final VoyagePlan originalVoyagePlan, final long startHeelInM3, final ExtendedCharterOutSequence bigSequence) {
		// // We will use the VPO to optimise fuel and route choices
		// vpo.reset();
		//
		int baseFuelPricePerMT = vesselBaseFuelCalculator.getBaseFuelPrice(vessel, bigSequence.getPortTimesRecord());
		// vpo.setVessel(vessel, null, baseFuelPrice);
		// vpo.setVesselCharterInRatePerDay();
		//
		// // Install our new alternative sequence
		// vpo.setBasicSequence(bigSequence.getSequence());
		// vpo.setStartHeel(startHeelInM3);
		//
		// // Rebuilt the arrival times list
		// vpo.setPortTimesRecord(bigSequence.getPortTimesRecord());

		List<@NonNull IVoyagePlanChoice> vpoChoices = new LinkedList<>();
		// Add in NBO etc choices (ballast 1)
		vpoChoices.add(new NBOTravelVoyagePlanChoice(bigSequence.getLaden() == null ? null : bigSequence.getLaden(), bigSequence.getToCharter()));
		vpoChoices.add(new FBOVoyagePlanChoice(bigSequence.getToCharter()));
		vpoChoices.add(new IdleNBOVoyagePlanChoice(bigSequence.getToCharter()));

		// Add in NBO etc choices (ballast 2)
		vpoChoices.add(new NBOTravelVoyagePlanChoice(bigSequence.getToCharter(), bigSequence.getFromCharter()));
		vpoChoices.add(new FBOVoyagePlanChoice(bigSequence.getFromCharter()));
		vpoChoices.add(new IdleNBOVoyagePlanChoice(bigSequence.getFromCharter()));

		// Calculate our new plan
		// final VoyagePlan newVoyagePlan = vpo.optimise();

		final VoyagePlan newVoyagePlan = vpo.optimise(null, vessel, startHeelInM3, baseFuelPricePerMT, originalVoyagePlan.getCharterInRatePerDay(), bigSequence.getPortTimesRecord(),
				bigSequence.getSequence(), vpoChoices);

		return newVoyagePlan;
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
		return new Pair<IDetailsSequenceElement[], IDetailsSequenceElement[]>(first, second);
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

		return new Pair<VoyagePlan, VoyagePlan>(upToCharterPlan, charterToEndPlan);
	}

	private void setPlanParameters(final VoyagePlan bigVoyagePlan, final VoyagePlan charterToEndPlan, final VoyagePlan upToCharterPlan, final VoyagePlan originalVP, final long remainingHeelInM3) {
		final long totalBoilOffPostCharter = getTotalBoilOffInSequence(charterToEndPlan.getSequence());

		charterToEndPlan.setRemainingHeelInM3(remainingHeelInM3);
		charterToEndPlan.setStartingHeelInM3(remainingHeelInM3 + totalBoilOffPostCharter);

		upToCharterPlan.setStartingHeelInM3(bigVoyagePlan.getStartingHeelInM3());
		upToCharterPlan.setRemainingHeelInM3(remainingHeelInM3 + totalBoilOffPostCharter);

		upToCharterPlan.setCharterInRatePerDay(originalVP.getCharterInRatePerDay());
		charterToEndPlan.setCharterInRatePerDay(originalVP.getCharterInRatePerDay());

		upToCharterPlan.setIgnoreEnd(true);
		charterToEndPlan.setIgnoreEnd(originalVP.isIgnoreEnd());
	}

	private long getTotalBoilOffInSequence(final IDetailsSequenceElement[] sequence) {
		long totalBoilOff = 0;
		for (final IDetailsSequenceElement sequenceElement : sequence) {
			if (sequenceElement instanceof VoyageDetails) {
				totalBoilOff += getTotalBoilOffForVoyage((VoyageDetails) sequenceElement);
			}
		}
		return totalBoilOff;
	}

	private long getTotalBoilOffForVoyage(final VoyageDetails voyageDetails) {
		long totalBoilOffM3 = 0;
		for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {
			totalBoilOffM3 += voyageDetails.getFuelConsumption(fc, FuelUnit.M3);
			totalBoilOffM3 += voyageDetails.getRouteAdditionalConsumption(fc, FuelUnit.M3);
		}
		return totalBoilOffM3;
	}

	/**
	 * Loop through the first voyage plan (up to the GCO) and find an element to extract heel options information
	 * 
	 * @param generatedCharterOutVesselEvent
	 * @param sequence
	 * @param heelVolume
	 * @param portTimesRecord
	 * @return
	 */
	private void setHeelOptions(final IGeneratedCharterOutVesselEvent generatedCharterOutVesselEvent, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement[] sequence,
			final long heelVolume) {
		int pricePerMBTU = 0;
		int cv = 0;

		final IDetailsSequenceElement startOfSequence = sequence[0];
		if (startOfSequence instanceof PortDetails) {
			final IPortSlot portSlot = ((PortDetails) startOfSequence).getOptions().getPortSlot();
			if (portSlot instanceof IHeelOptionsPortSlot) {
				pricePerMBTU = ((IHeelOptionsPortSlot) portSlot).getHeelOptions().getHeelUnitPrice();
				cv = ((IHeelOptionsPortSlot) portSlot).getHeelOptions().getHeelCVValue();
			} else if (portSlot instanceof ILoadSlot) {
				IDischargeSlot discharge = null;
				for (int i = sequence.length - 1; i >= 0; i--) {
					if (sequence[i] instanceof PortDetails) {
						if (((PortDetails) sequence[i]).getOptions().getPortSlot() instanceof IDischargeSlot) {
							discharge = (IDischargeSlot) ((PortDetails) sequence[i]).getOptions().getPortSlot();
							break;
						}
					}
				}
				if (discharge != null) {
					pricePerMBTU = discharge.getDischargePriceCalculator().estimateSalesUnitPrice(discharge, portTimesRecord, null);
				}
				cv = ((ILoadSlot) portSlot).getCargoCVValue();
			}
		}
		generatedCharterOutVesselEvent.setHeelOptions(pricePerMBTU, cv, heelVolume);
	}

	private boolean isSolutionCached(final int firstLoadTime, final long startHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad, final IPortSlot discharge,
			final IPortSlot nextLoad, final IVesselAvailability vesselAvailability) {
		return generatedCharterOutOptionCache.isSolutionCached(firstLoadTime, startHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability);
	}

	private List<Pair<VoyagePlan, IPortTimesRecord>> getCachedSolution(final int firstLoadTime, final long startHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad,
			final IPortSlot discharge, final IPortSlot nextLoad, final IVesselAvailability vesselAvailability) {
		return generatedCharterOutOptionCache.getSplitVoyagePlans(firstLoadTime, startHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability);
	}

	private void addCachedSolution(final int firstLoadTime, final long startHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad, final IPortSlot discharge,
			final IPortSlot nextLoad, final IVesselAvailability vesselAvailability, final List<Pair<VoyagePlan, IPortTimesRecord>> solution, final GeneratedCharterOutOption charterOutOption) {
		Pair<List<Pair<VoyagePlan, IPortTimesRecord>>, GeneratedCharterOutOption> solutionToCache = null;
		if (solution != null) {
			solutionToCache = new Pair<List<Pair<VoyagePlan, IPortTimesRecord>>, GeneratedCharterOutOption>(solution, charterOutOption);
		}
		generatedCharterOutOptionCache.addSplitVoyagePlans(firstLoadTime, startHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability, solutionToCache);
	}

	private void setAdditionalDataForCaching(final GeneratedCharterOutOption gcoo) {
		final PortOptions gcoOptions = gcoo.getPortOptions();
		// options
		gcoo.setGCOVessel(gcoOptions.getVessel());
		gcoo.setGCODuration(gcoOptions.getVisitDuration());

		// port slot
		gcoo.setGCOID(gcoOptions.getPortSlot().getId());
		gcoo.setGCOPort(gcoOptions.getPortSlot().getPort());

		// event
		gcoo.setGCOEventStartPort(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getStartPort());
		gcoo.setGCOEventEndPort(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getEndPort());
		gcoo.setGCOEventHireOutRevenue(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getHireOutRevenue());
		gcoo.setGCOEventDurationHours(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getDurationHours());
		gcoo.setGCOEventHeelPrice(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getHeelUnitPrice());
		gcoo.setGCOEventHeelCV(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getHeelCVValue());
		gcoo.setGCOEventHeelVolume(((IGeneratedCharterOutVesselEventPortSlot) gcoOptions.getPortSlot()).getVesselEvent().getHeelLimit());
	}
}