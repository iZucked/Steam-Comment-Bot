/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.HeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 7.1
 */
public class VoyagePlanner {
	@Inject
	private IElementDurationProvider durationsProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IVoyagePlanOptimiser voyagePlanOptimiser;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IVolumeAllocator volumeAllocator;

	@com.google.inject.Inject(optional = true)
	private IBreakEvenEvaluator breakEvenEvaluator;

	@com.google.inject.Inject(optional = true)
	private IGeneratedCharterOutEvaluator generatedCharterOutEvaluator;

	@Inject
	private ICharterRateCalculator charterRateCalculator;

	/**
	 * Returns a voyage options object and extends the current VPO with appropriate choices for a particular journey. TODO: refactor this if possible to simplify it and make it stateless (it currently
	 * messes with the VPO).
	 * 
	 * @param vessel
	 * @param vesselState
	 * @param availableTime
	 * @param element
	 * @param prevElement
	 * @param previousOptions
	 * @param useNBO
	 * @return
	 */
	private VoyageOptions getVoyageOptionsAndSetVpoChoices(final IVessel vessel, final VesselState vesselState, final int availableTime, final ISequenceElement element,
			final ISequenceElement prevElement, final VoyageOptions previousOptions, final IVoyagePlanOptimiser optimiser, boolean useNBO) {

		final int nboSpeed = vessel.getVesselClass().getMinNBOSpeed(vesselState);

		final IPort thisPort = portProvider.getPortForElement(element);
		final IPort prevPort = portProvider.getPortForElement(prevElement);
		final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
		final IPortSlot prevPortSlot = portSlotProvider.getPortSlot(prevElement);
		final PortType prevPortType = portTypeProvider.getPortType(prevElement);

		final VoyageOptions options = new VoyageOptions();

		options.setVessel(vessel);
		options.setFromPortSlot(prevPortSlot);
		options.setToPortSlot(thisPortSlot);
		options.setNBOSpeed(nboSpeed);
		options.setVesselState(vesselState);
		options.setAvailableTime(availableTime);

		// Flag to force NBO use over cost choice - e.g. for cases where there is already a heel onboard
		boolean forceNBO = false;

		if ((prevPortType == PortType.Load) || (prevPortType == PortType.CharterOut)) {
			useNBO = true;
		}

		if (prevPortSlot instanceof IHeelOptionsPortSlot) {
			final IHeelOptionsPortSlot heelOptionsSlot = (IHeelOptionsPortSlot) prevPortSlot;
			// options.setAvailableLNG(Math.min(vessel.getVesselClass().getCargoCapacity(), heelOptions.getHeelOptions().getHeelLimit()));
			if (heelOptionsSlot.getHeelOptions().getHeelLimit() > 0) {
				useNBO = true;
				forceNBO = false;
			} else {
				useNBO = false;
				forceNBO = false;
			}
		}

		if ((prevPortType == PortType.DryDock) || (prevPortType == PortType.Maintenance)) {
			options.setWarm(true);
		} else {
			options.setWarm(false);
		}

		// Determined by voyage plan optimiser
		options.setUseNBOForTravel(useNBO);
		options.setUseFBOForSupplement(false);
		// If not forced, then a choice may be added later
		options.setUseNBOForIdle(forceNBO);

		if (thisPortSlot.getPortType() == PortType.Load) {
			options.setShouldBeCold(true);
			final ILoadSlot thisLoadSlot = (ILoadSlot) thisPortSlot;

			if (thisLoadSlot.isCooldownSet()) {
				if (thisLoadSlot.isCooldownForbidden()) {
					// cooldown may still happen if circumstances allow
					// no alternative.
					options.setAllowCooldown(false);
				} else {
					options.setAllowCooldown(true);
				}
			} else {
				if (thisPort.shouldVesselsArriveCold()) {
					// we don't want to use cooldown ever
					options.setAllowCooldown(false);
				} else {
					// we have a choice
					options.setAllowCooldown(false);
					optimiser.addChoice(new CooldownVoyagePlanChoice(options));
				}
			}
		} else {
			options.setShouldBeCold(false);
		}

		// Enable choices only if NBO could be available.
		if (useNBO) {
			// Note ordering of choices is important = NBO must be set
			// before FBO and Idle choices, otherwise if NBO choice is
			// false, FBO and IdleNBO may still be true if set before
			// NBO

			if (vesselState == VesselState.Ballast && !forceNBO) {
				optimiser.addChoice(new NBOTravelVoyagePlanChoice(previousOptions, options));
			}

			optimiser.addChoice(new FBOVoyagePlanChoice(options));

			if (!forceNBO) {
				optimiser.addChoice(new IdleNBOVoyagePlanChoice(options));
			}
		}

		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(prevPort, thisPort));
		// Only add route choice if there is one
		if (distances.size() == 1) {
			final MatrixEntry<IPort, Integer> d = distances.get(0);
			options.setDistance(d.getValue());
			options.setRoute(d.getKey());
		} else {
			optimiser.addChoice(new RouteVoyagePlanChoice(options, distances));
		}

		if (vessel.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER && thisPortSlot.getPortType() == PortType.End) {
			options.setAllowCooldown(false);
			options.setShouldBeCold(true);
		}

		return options;
	}

	/**
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	final public List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> makeVoyagePlans(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {

		// TODO: Handle FOB/DES cargoes also

		// IF FOB/DES
		// customFOBDESModeller.createVoyagePlan()
		final int vesselStartTime = arrivalTimes[0];

		final IVessel vessel = vesselProvider.getVessel(resource);
		// TODO: Extract out further for custom base fuel pricing logic?
		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
		voyagePlanOptimiser.setVessel(vessel, baseFuelPricePerMT);

		final boolean isShortsSequence = vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> voyagePlansMap = new LinkedList<>();
		final List<VoyagePlan> voyagePlansList = new LinkedList<>();

		final List<IOptionsSequenceElement> voyageOrPortOptions = new ArrayList<IOptionsSequenceElement>(5);
		final List<Integer> currentTimes = new ArrayList<Integer>(3);
		final boolean[] breakSequence = findSequenceBreaks(sequence);
		final VesselState[] states = findVesselStates(sequence);

		ISequenceElement prevElement = null;
		IPort prevPort = null;
		IPort prev2Port = null;

		VoyageOptions previousOptions = null;
		boolean useNBO = false;

		int prevVisitDuration = 0;
		final Iterator<ISequenceElement> itr = sequence.iterator();

		long heelVolumeInM3 = 0;
		// For spot charters, start with the safety heel.
		if (vessel.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER) {
			heelVolumeInM3 = vessel.getVesselClass().getMinHeel();
		}

		for (int idx = 0; itr.hasNext(); ++idx) {
			final ISequenceElement element = itr.next();

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final PortType portType = portTypeProvider.getPortType(element);

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			int shortCargoReturnArrivalTime = 0;
			if (prevPort != null) {
				final int availableTime;
				final boolean isShortCargoEnd = isShortsSequence && portType == PortType.Short_Cargo_End;

				if (!isShortCargoEnd) {
					// Available time, as determined by inputs.
					availableTime = arrivalTimes[idx] - arrivalTimes[idx - 1] - prevVisitDuration;
				} else { // shorts cargo end on shorts sequence
					int minTravelTime = Integer.MAX_VALUE;
					for (final MatrixEntry<IPort, Integer> entry : distanceProvider.getValues(prevPort, prev2Port)) {
						final int distance = entry.getValue();
						final int extraTime = routeCostProvider.getRouteTransitTime(entry.getKey(), vessel.getVesselClass());
						final int minByRoute = Calculator.getTimeFromSpeedDistance(vessel.getVesselClass().getMaxSpeed(), distance) + extraTime;
						minTravelTime = Math.min(minTravelTime, minByRoute);
					}
					availableTime = minTravelTime;

					shortCargoReturnArrivalTime = arrivalTimes[idx - 1] + prevVisitDuration + availableTime;
				}

				final VoyageOptions options = getVoyageOptionsAndSetVpoChoices(vessel, states[idx], availableTime, element, prevElement, previousOptions, voyagePlanOptimiser, useNBO);
				useNBO = options.useNBOForTravel();

				voyageOrPortOptions.add(options);
				previousOptions = options;
			}

			final int visitDuration = durationsProvider.getElementDuration(element, resource);

			final PortOptions portOptions = new PortOptions();
			portOptions.setVisitDuration(visitDuration);
			portOptions.setPortSlot(thisPortSlot);
			portOptions.setVessel(vessel);

			if (isShortsSequence && portType == PortType.Short_Cargo_End) {
				currentTimes.add(shortCargoReturnArrivalTime);
			} else {
				currentTimes.add(arrivalTimes[idx]);
			}
			voyageOrPortOptions.add(portOptions);

			if (breakSequence[idx]) {

				final boolean shortCargoEnd = ((PortOptions) voyageOrPortOptions.get(0)).getPortSlot().getPortType() == PortType.Short_Cargo_End;

				// Special case for cargo shorts routes. There is no voyage between a Short_Cargo_End and the next load - which this current sequence will represent. However we do need to model the
				// Short_Cargo_End for the VoyagePlanIterator to work correctly. Here we strip the voyage and make this a single element sequence.
				if (!shortCargoEnd) {
					final int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, vesselStartTime, currentTimes.get(0));
					final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3, vesselCharterInRatePerDay);
					if (plan == null) {
						return null;
					}
					heelVolumeInM3 = generateVoyagePlan(vessel, vesselStartTime, voyagePlansMap, voyagePlansList, currentTimes, heelVolumeInM3, plan);
				}

				if (isShortsSequence) {
					voyagePlansList.get(voyagePlansList.size() - 1).setIgnoreEnd(false);
				}

				// Reset useNBO flag
				useNBO = false;
				previousOptions = null;

				voyageOrPortOptions.clear();
				currentTimes.clear();

				voyageOrPortOptions.add(portOptions);
				currentTimes.add(arrivalTimes[idx]);
			}

			// If we are a heel options slots (i.e. Start or other vessel event slot, overwrite previous heel (assume lost) and replace with a new heel value
			// TODO: Move (back?)into VPO code
			if (thisPortSlot instanceof IHeelOptionsPortSlot) {
				heelVolumeInM3 = ((IHeelOptionsPortSlot) thisPortSlot).getHeelOptions().getHeelLimit();
			}

			// Setup for next iteration
			prev2Port = prevPort;

			prevPort = thisPort;
			prevVisitDuration = visitDuration;
			prevElement = element;
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		if (voyageOrPortOptions.size() > 1) {
			final int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, vesselStartTime, currentTimes.get(0));
			final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3, vesselCharterInRatePerDay);
			if (plan == null) {
				return null;
			}
			plan.setIgnoreEnd(false);
			heelVolumeInM3 = generateVoyagePlan(vessel, vesselStartTime, voyagePlansMap, voyagePlansList, currentTimes, heelVolumeInM3, plan);
		}

		return voyagePlansMap;
	}

	// TODO: Better naming?
	private long generateVoyagePlan(final IVessel vessel, final int vesselStartTime, final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> voyagePlansMap,
			final List<VoyagePlan> voyagePlansList, final List<Integer> currentTimes, final long startHeelVolumeInM3, VoyagePlan plan) {

		// TODO: Handle LNG at end of charter out
		long endHeelVolumeInM3 = 0;
		boolean planSet = false;
		IAllocationAnnotation allocationAnnotation = null;
		if (generatedCharterOutEvaluator != null) {
			final Pair<VoyagePlan, IAllocationAnnotation> p = generatedCharterOutEvaluator.processSchedule(vesselStartTime, vessel, plan, currentTimes);
			if (p != null) {
				plan = p.getFirst();
				voyagePlansList.add(p.getFirst());
				allocationAnnotation = p.getSecond();

				if (allocationAnnotation != null) {
					endHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					endHeelVolumeInM3 = plan.getRemainingHeelInM3();
				}
				planSet = true;
			}
		}

		// FIXME: This should be more customisable

		// Execute custom logic to manipulate the schedule and choices
		if (breakEvenEvaluator != null) {
			final Pair<VoyagePlan, IAllocationAnnotation> p = breakEvenEvaluator.processSchedule(vesselStartTime, vessel, plan, currentTimes);
			if (p != null) {
				plan = p.getFirst();
				voyagePlansList.add(p.getFirst());
				allocationAnnotation = p.getSecond();

				if (allocationAnnotation != null) {
					endHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					endHeelVolumeInM3 = plan.getRemainingHeelInM3();
				}
				planSet = true;
			}
		}

		if (!planSet) {
			voyagePlansList.add(plan);
			// TODO: Non-cargo cases?
			allocationAnnotation = volumeAllocator.allocate(vessel, vesselStartTime, plan, currentTimes);
			if (allocationAnnotation == null) {
				// not a cargo plan?
				endHeelVolumeInM3 = plan.getRemainingHeelInM3();
			} else {
				endHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
			}
		}

		// Generate heel level annotations
		final Map<IPortSlot, IHeelLevelAnnotation> heelLevelAnnotations = new HashMap<IPortSlot, IHeelLevelAnnotation>();
		{
			final IDetailsSequenceElement[] sequence = plan.getSequence();
			long currentHeelInM3 = startHeelVolumeInM3;
			long totalVoyageBOG = 0;
			int voyageTime = 0;
			IPortSlot optionalHeelUsePortSlot = null;
			int adjust = plan.isIgnoreEnd() ? 1 : 0;
			for (int i = 0; i < sequence.length - adjust; ++i) {
				final IDetailsSequenceElement e = sequence[i];
				if (e instanceof PortDetails) {
					final PortDetails portDetails = (PortDetails) e;
					final IPortSlot portSlot = portDetails.getOptions().getPortSlot();
					final long start = currentHeelInM3;
					if (portSlot.getPortType() != PortType.End) {
						optionalHeelUsePortSlot = null;
						if (allocationAnnotation != null) {
							if (portSlot.getPortType() == PortType.Load) {
								currentHeelInM3 += allocationAnnotation.getSlotVolumeInM3(portSlot);
							} else if (portSlot.getPortType() == PortType.Discharge) {
								currentHeelInM3 -= allocationAnnotation.getSlotVolumeInM3(portSlot);
							}
						} else {
							if (portSlot instanceof IHeelOptionsPortSlot) {
								optionalHeelUsePortSlot = portSlot;
								// FIXME: This volume is optional use
								final IHeelOptionsPortSlot heelOptionsPortSlot = (IHeelOptionsPortSlot) portSlot;
								currentHeelInM3 = heelOptionsPortSlot.getHeelOptions().getHeelLimit();
							} else if (portSlot.getPortType() != PortType.End) {
								currentHeelInM3 = 0;
							}
						}
					}
					final long end = currentHeelInM3;

					final HeelLevelAnnotation heelLevelAnnotation = new HeelLevelAnnotation(start, end);
					heelLevelAnnotations.put(portSlot, heelLevelAnnotation);
				} else if (e instanceof VoyageDetails) {
					final VoyageDetails voyageDetails = (VoyageDetails) e;
					long voyageBOGInM3 = 0;
					for (final FuelComponent fuel : FuelComponent.getLNGFuelComponents()) {
						voyageBOGInM3 += voyageDetails.getFuelConsumption(fuel, FuelUnit.M3);
						voyageBOGInM3 += voyageDetails.getRouteAdditionalConsumption(fuel, FuelUnit.M3);
					}
					totalVoyageBOG += voyageBOGInM3;
					currentHeelInM3 -= voyageBOGInM3;

					voyageTime += voyageDetails.getTravelTime();
					voyageTime += voyageDetails.getIdleTime();

				}
			}
			// The optional heel use port slot has heel on board which may or may not have been used.
			// The default code path assumes it has been used. However, if there is no NBO at all, we assume it did not exist,
			// thus we need to update the data to accommodate.
			if (optionalHeelUsePortSlot != null && voyageTime > 0 && totalVoyageBOG == 0) {
				// Replace heel level annotation
				heelLevelAnnotations.put(optionalHeelUsePortSlot, new HeelLevelAnnotation(0, 0));
				// Update current heel - this will still be the start heel value as there was no boil-off
				currentHeelInM3 = 0;
			}

			// Sanity check these calculations match expected values
			assert totalVoyageBOG == plan.getLNGFuelVolume();
			assert endHeelVolumeInM3 == currentHeelInM3;
		}

		voyagePlansMap.add(new Triple<>(plan, heelLevelAnnotations, allocationAnnotation));

		return endHeelVolumeInM3;

	}

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	final public VoyagePlan makeVoyage(final IResource resource, final List<ISequenceElement> sequenceElements, final int vesselCharterInRatePerDay, final List<Integer> arrivalTimes,
			long heelVolumeInM3) {

		final IVessel vessel = vesselProvider.getVessel(resource);
		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
		voyagePlanOptimiser.setVessel(vessel, baseFuelPricePerMT);

		final boolean isShortsSequence = vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final List<IOptionsSequenceElement> voyageOrPortOptions = new ArrayList<IOptionsSequenceElement>(5);
		final List<Integer> currentTimes = new ArrayList<Integer>(3);
		final VesselState[] states = findVesselStates(sequenceElements);

		ISequenceElement prevElement = null;
		IPort prevPort = null;
		IPort prev2Port = null;

		VoyageOptions previousOptions = null;
		boolean useNBO = false;

		int prevVisitDuration = 0;
		for (int idx = 0; idx < sequenceElements.size(); ++idx) {
			final ISequenceElement element = sequenceElements.get(idx);

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final PortType portType = portTypeProvider.getPortType(element);

			// If we are a heel options slots (i.e. Start or other vessel event slot, overwrite previous heel (assume lost) and replace with a new heel value
			if (thisPortSlot instanceof IHeelOptionsPortSlot) {
				heelVolumeInM3 = ((IHeelOptionsPortSlot) thisPortSlot).getHeelOptions().getHeelLimit();
			}

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			int shortCargoReturnArrivalTime = 0;
			if (prevPort != null) {
				final int availableTime;
				final boolean isShortCargoEnd = isShortsSequence && portType == PortType.Short_Cargo_End;

				if (!isShortCargoEnd) {
					// Available time, as determined by inputs.
					availableTime = arrivalTimes.get(idx) - arrivalTimes.get(idx - 1) - prevVisitDuration;
				} else { // shorts cargo end on shorts sequence
					int minTravelTime = Integer.MAX_VALUE;
					for (final MatrixEntry<IPort, Integer> entry : distanceProvider.getValues(prevPort, prev2Port)) {
						final int distance = entry.getValue();
						final int extraTime = routeCostProvider.getRouteTransitTime(entry.getKey(), vessel.getVesselClass());
						final int minByRoute = Calculator.getTimeFromSpeedDistance(vessel.getVesselClass().getMaxSpeed(), distance) + extraTime;
						minTravelTime = Math.min(minTravelTime, minByRoute);
					}
					availableTime = minTravelTime;

					shortCargoReturnArrivalTime = arrivalTimes.get(idx - 1) + prevVisitDuration + availableTime;
				}

				final VoyageOptions options = getVoyageOptionsAndSetVpoChoices(vessel, states[idx], availableTime, element, prevElement, previousOptions, voyagePlanOptimiser, useNBO);
				useNBO = options.useNBOForTravel();

				voyageOrPortOptions.add(options);
				previousOptions = options;
			}

			final int visitDuration = durationsProvider.getElementDuration(element, resource);

			final PortOptions portOptions = new PortOptions();
			portOptions.setVisitDuration(visitDuration);
			portOptions.setPortSlot(thisPortSlot);
			portOptions.setVessel(vessel);

			if (isShortsSequence && portType == PortType.Short_Cargo_End) {
				currentTimes.add(shortCargoReturnArrivalTime);
			} else {
				currentTimes.add(arrivalTimes.get(idx));
			}
			voyageOrPortOptions.add(portOptions);

			// Setup for next iteration
			prev2Port = prevPort;

			prevPort = thisPort;
			prevVisitDuration = visitDuration;
			prevElement = element;
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		if (voyageOrPortOptions.size() > 1) {
			return getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3, vesselCharterInRatePerDay);
		}
		return null;
	}

	/**
	 * Returns a VoyagePlan produced by the optimiser from a cargo itinerary.
	 * 
	 * @param voyageOrPortOptionsSubsequence
	 *            An alternating list of PortOptions and VoyageOptions objects
	 * @param arrivalTimes
	 * @param optimiser
	 * @param startHeelVolumeInM3
	 * @return An optimised VoyagePlan
	 */
	final public VoyagePlan getOptimisedVoyagePlan(final List<IOptionsSequenceElement> voyageOrPortOptionsSubsequence, final List<Integer> arrivalTimes, final IVoyagePlanOptimiser optimiser,
			final long startHeelVolumeInM3, final int vesselCharterInRatePerDay) {
		// Run sequencer evaluation
		optimiser.setVesselCharterInRatePerDay(vesselCharterInRatePerDay);
		optimiser.setBasicSequence(voyageOrPortOptionsSubsequence);
		optimiser.setArrivalTimes(arrivalTimes);
		optimiser.setStartHeel(startHeelVolumeInM3);
		optimiser.init();
		final VoyagePlan result = optimiser.optimise();
		if (result == null) {
			return null;
		}

		final Object[] sequence = result.getSequence();
		for (int i = 0; i < sequence.length; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				final int availableTime = details.getOptions().getAvailableTime();

				// Take voyage details time as this can be larger than
				// available time e.g. due to reaching max speed.
				final int duration = details.getTravelTime() + details.getIdleTime();

				// assert duration >= (availableTime - 1) :
				// "Duration should exceed available time less one, but is "
				// + duration + " vs " + availableTime; // hack
				if (duration > availableTime) {
					// TODO: replace by throwing an exception, since if any one subsequence is infeasible, the whole sequence is infeasible
					return null;
				}
			}
		}

		// Reset VPO ready for next iteration
		optimiser.reset();

		return result;

	}

	/**
	 * Returns an array of boolean values indicating whether, for each index of the vessel location sequence, a sequence break occurs at that location (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	private boolean[] findSequenceBreaks(final ISequence sequence) {
		final boolean[] result = new boolean[sequence.size()];

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			final PortType portType = portTypeProvider.getPortType(element);
			switch (portType) {
			case Load:
				result[idx] = (idx > 0); // don't break on first load port
				break;
			case CharterOut:
			case DryDock:
			case Other:
			case Maintenance:
			case Short_Cargo_End:
				result[idx] = true;
				break;
			default:
				result[idx] = false;
				break;
			}
			idx++;
		}

		return result;
	}

	/**
	 * Returns an array of vessel states determining, for each index of the vessel location sequence, whether the vessel arrives at that location with LNG cargo on board for resale or not
	 * 
	 * @param sequence
	 * @return
	 */
	private VesselState[] findVesselStates(final ISequence sequence) {
		final VesselState[] result = new VesselState[sequence.size()];

		VesselState state = VesselState.Ballast;
		int possiblePartialDischargeIndex = -1;

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			result[idx] = state;
			// Determine vessel state changes from this location
			switch (portTypeProvider.getPortType(element)) {
			case Load:
				state = VesselState.Laden;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			case Discharge:
				// we'll assume this is a full discharge
				state = VesselState.Ballast;
				// but the last discharge which might have been partial *was* a partial discharge
				if (possiblePartialDischargeIndex > -1) {
					result[possiblePartialDischargeIndex + 1] = VesselState.Laden;
				}
				// and this one might be too
				possiblePartialDischargeIndex = idx;
				break;
			case CharterOut:
			case DryDock:
			case Maintenance:
			case Short_Cargo_End:
				state = VesselState.Ballast;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			default:
				break;
			}

			idx++;
		}
		return result;
	}

	/**
	 * Returns an array of vessel states determining, for each index of the vessel location sequence, whether the vessel arrives at that location with LNG cargo on board for resale or not
	 * 
	 * @param sequence
	 * @return
	 */
	private VesselState[] findVesselStates(final List<ISequenceElement> elements) {
		final VesselState[] result = new VesselState[elements.size()];

		VesselState state = VesselState.Ballast;
		int possiblePartialDischargeIndex = -1;

		int idx = 0;
		for (final ISequenceElement element : elements) {
			result[idx] = state;
			// Determine vessel state changes from this location
			switch (portTypeProvider.getPortType(element)) {
			case Load:
				state = VesselState.Laden;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			case Discharge:
				// we'll assume this is a full discharge
				state = VesselState.Ballast;
				// but the last discharge which might have been partial *was* a partial discharge
				if (possiblePartialDischargeIndex > -1) {
					result[possiblePartialDischargeIndex + 1] = VesselState.Laden;
				}
				// and this one might be too
				possiblePartialDischargeIndex = idx;
				break;
			case CharterOut:
			case DryDock:
			case Maintenance:
			case Short_Cargo_End:
				state = VesselState.Ballast;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			default:
				break;
			}

			idx++;
		}
		return result;
	}

}
