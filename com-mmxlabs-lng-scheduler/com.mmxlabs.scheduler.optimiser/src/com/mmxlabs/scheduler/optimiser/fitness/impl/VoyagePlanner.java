/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan.HeelType;

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

	@Inject
	private ICalculatorProvider calculatorProvider;

	@com.google.inject.Inject(optional = true)
	private IBreakEvenEvaluator breakEvenEvaluator;

	@com.google.inject.Inject(optional = true)
	private IGeneratedCharterOutEvaluator generatedCharterOutEvaluator;

	@Inject
	private ScheduleCalculator scheduleCalculator;

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
	final public LinkedHashMap<VoyagePlan, IAllocationAnnotation> makeVoyagePlans(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {

		// TODO: Handle FOB/DES cargoes also

		// IF FOB/DES
		// customFOBDESModeller.createVoyagePlan()
		final int vesselStartTime = arrivalTimes[0];

		final IVessel vessel = vesselProvider.getVessel(resource);
		// TODO: Extract out further for custom base fuel pricing logic?
		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
		voyagePlanOptimiser.setVessel(vessel, vesselStartTime, baseFuelPricePerMT);

		final boolean isShortsSequence = vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final LinkedHashMap<VoyagePlan, IAllocationAnnotation> voyagePlansMap = new LinkedHashMap<>();
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

		for (int idx = 0; itr.hasNext(); ++idx) {
			final ISequenceElement element = itr.next();

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
					VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3);
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

			// Setup for next iteration
			prev2Port = prevPort;

			prevPort = thisPort;
			prevVisitDuration = visitDuration;
			prevElement = element;
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		if (voyageOrPortOptions.size() > 1) {

			VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3);
			if (plan == null) {
				return null;
			}
			heelVolumeInM3 = generateVoyagePlan(vessel, vesselStartTime, voyagePlansMap, voyagePlansList, currentTimes, heelVolumeInM3, plan);
		}

		return voyagePlansMap;
	}

	// TODO: Better naming?
	private long generateVoyagePlan(final IVessel vessel, final int vesselStartTime, final LinkedHashMap<VoyagePlan, IAllocationAnnotation> voyagePlansMap, final List<VoyagePlan> voyagePlansList,
			final List<Integer> currentTimes, long heelVolumeInM3, VoyagePlan plan) {

		// TODO: Handle LNG at end of charter out

		boolean planSet = false;
		if (generatedCharterOutEvaluator != null) {
			Pair<VoyagePlan, IAllocationAnnotation> p = generatedCharterOutEvaluator.processSchedule(vesselStartTime, vessel, plan, currentTimes);
			if (p != null) {
				plan = p.getFirst();
				voyagePlansList.add(p.getFirst());
				final IAllocationAnnotation allocationAnnotation = p.getSecond();
				voyagePlansMap.put(plan, allocationAnnotation);

				if (allocationAnnotation != null) {
					heelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					if (plan.getRemainingHeelType() == HeelType.END) {
						heelVolumeInM3 = plan.getRemainingHeelInM3();
					} else {
						heelVolumeInM3 = 0;
					}
				}
				planSet = true;
			}
		}

		// FIXME: This should be more customisable

		// Execute custom logic to manipulate the schedule and choices
		if (breakEvenEvaluator != null) {
			Pair<VoyagePlan, IAllocationAnnotation> p = breakEvenEvaluator.processSchedule(vesselStartTime, vessel, plan, currentTimes);
			if (p != null) {
				plan = p.getFirst();
				voyagePlansList.add(p.getFirst());
				final IAllocationAnnotation allocationAnnotation = p.getSecond();
				voyagePlansMap.put(plan, allocationAnnotation);

				if (allocationAnnotation != null) {
					heelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					if (plan.getRemainingHeelType() == HeelType.END) {
						heelVolumeInM3 = plan.getRemainingHeelInM3();
					} else {
						heelVolumeInM3 = 0;
					}
				}
				planSet = true;
			}
		}

		if (!planSet) {
			voyagePlansList.add(plan);
			// TODO: Non-cargo cases?
			final AllocationRecord record = volumeAllocator.createAllocationRecord(vessel, vesselStartTime, plan, currentTimes);
			if (record == null) {
				// not a cargo plan?
				voyagePlansMap.put(plan, null);
				if (plan.getRemainingHeelType() == HeelType.END) {
					heelVolumeInM3 = plan.getRemainingHeelInM3();
				} else {
					heelVolumeInM3 = 0;
				}
			} else {
				final IAllocationAnnotation allocationAnnotation = volumeAllocator.allocate(record);
				voyagePlansMap.put(plan, allocationAnnotation);
				heelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
			}
		}
		return heelVolumeInM3;
	}

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	final public VoyagePlan makeVoyage(final IResource resource, final List<ISequenceElement> sequenceElements, final int vesselStartTime, final List<Integer> arrivalTimes, long heelVolumeInM3) {

		final IVessel vessel = vesselProvider.getVessel(resource);
		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
		voyagePlanOptimiser.setVessel(vessel, vesselStartTime, baseFuelPricePerMT);

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
			return getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3);
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
	 * @param heelVolumeInM3
	 * @return An optimised VoyagePlan
	 */
	final public VoyagePlan getOptimisedVoyagePlan(final List<IOptionsSequenceElement> voyageOrPortOptionsSubsequence, final List<Integer> arrivalTimes, final IVoyagePlanOptimiser optimiser,
			final long heelVolumeInM3) {
		// Run sequencer evaluation
		optimiser.setBasicSequence(voyageOrPortOptionsSubsequence);
		optimiser.setArrivalTimes(arrivalTimes);
		optimiser.setStartHeel(heelVolumeInM3);
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

	// public final boolean optimiseSequence(final List<VoyagePlan> voyagePlans, final List<IOptionsSequenceElement> currentSequence, final List<Integer> currentTimes,
	// final IVoyagePlanOptimiser optimiser, final long startHeel) {
	// final VoyagePlan plan = getOptimisedVoyagePlan(currentSequence, currentTimes, optimiser, startHeel);
	// if (plan == null) {
	// return false;
	// }
	//
	// voyagePlans.add(plan);
	// return true;
	// }

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

	/**
	 * Schedule an {@link ISequence} using the given array of arrivalTimes, indexed according to sequence elements. These times will be used as the base arrival time. However is some cases the time
	 * between elements may be too short (i.e. because the vessel is already travelling at max speed). In such cases, if adjustArrivals is true, then arrival times will be adjusted in the
	 * {@link VoyagePlan}s. Otherwise null will be returned.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 *            Array of arrival times at each {@link ISequenceElement} in the {@link ISequence}
	 * @return
	 * @throws InfeasibleVoyageException
	 */
	private ScheduledSequence schedule(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// Virtual vessels are those operated by a third party, for FOB and DES situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, sequence);
		}

		final boolean isShortsSequence = vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		// TODO: document this code path
		if (isShortsSequence && arrivalTimes.length == 0) {
			return new ScheduledSequence(resource, 0, Collections.<VoyagePlan> emptyList(), new int[] { 0 });
		}

		// Get start time
		final int startTime = arrivalTimes[0];

		final Map<VoyagePlan, IAllocationAnnotation> voyagePlans = makeVoyagePlans(resource, sequence, arrivalTimes);
		if (voyagePlans == null) {
			return null;
		}

		final ScheduledSequence scheduledSequence = new ScheduledSequence(resource, startTime, new ArrayList<>(voyagePlans.keySet()), arrivalTimes);
		scheduledSequence.getAllocations().putAll(voyagePlans);
		return scheduledSequence;
	}

	private ScheduledSequence desOrFobSchedule(final IResource resource, final ISequence sequence) {
		// Virtual vessels are those operated by a third party, for FOB and DES situations.
		// Should we compute a schedule for them anyway? The arrival times don't mean much,
		// but contracts need this kind of information to make up numbers with.
		final List<IDetailsSequenceElement> currentSequence = new ArrayList<IDetailsSequenceElement>(5);
		final VoyagePlan currentPlan = new VoyagePlan();

		boolean startSet = false;
		int startTime = 0;
		for (final ISequenceElement element : sequence) {

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);

			// TODO: This might need updating when we complete FOB/DES work - the load slot may not have a real time window
			if (!startSet && !(thisPortSlot instanceof StartPortSlot)) {
				startTime = thisPortSlot.getTimeWindow().getStart();
				startSet = true;
			}
			final PortOptions portOptions = new PortOptions();
			final PortDetails portDetails = new PortDetails();
			portDetails.setOptions(portOptions);
			portOptions.setVisitDuration(0);
			portOptions.setPortSlot(thisPortSlot);
			currentSequence.add(portDetails);

		}
		final int times[] = new int[sequence.size()];
		Arrays.fill(times, startTime);
		currentPlan.setSequence(currentSequence.toArray(new IDetailsSequenceElement[0]));
		ScheduledSequence scheduledSequence = new ScheduledSequence(resource, startTime, Collections.singletonList(currentPlan), times);

		IVessel vessel = vesselProvider.getVessel(resource);
		int vesselStartTime = startTime;

		// TODO: This is not the place!
		final AllocationRecord record = volumeAllocator.createAllocationRecord(vessel, vesselStartTime, currentPlan, CollectionsUtil.toArrayList(times));
		if (record != null) {

			scheduledSequence.getAllocations().put(currentPlan, volumeAllocator.allocate(record));
		} else {
			scheduledSequence.getAllocations().put(currentPlan, null);
		}
		return scheduledSequence;
	}

	public ScheduledSequences schedule(@NonNull final ISequences sequences, @NonNull final int[][] arrivalTimes, @Nullable IAnnotatedSolution solution) {
		final ScheduledSequences result = new ScheduledSequences();

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}
		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.prepareEvaluation(sequences);
		}

		final List<IResource> resources = sequences.getResources();

		for (int i = 0; i < sequences.size(); i++) {
			final ISequence sequence = sequences.getSequence(i);
			final IResource resource = resources.get(i);

			final ScheduledSequence scheduledSequence = schedule(resource, sequence, arrivalTimes[i]);
			if (scheduledSequence == null) {
				return null;
			}
			result.add(scheduledSequence);
		}

		scheduleCalculator.calculateSchedule(sequences, result, solution);
		
		return result;
	}

}
