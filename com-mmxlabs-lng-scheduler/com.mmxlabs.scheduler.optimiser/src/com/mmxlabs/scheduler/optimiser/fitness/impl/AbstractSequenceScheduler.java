/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
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
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan.HeelType;

/**
 * Abstract {@link ISequenceScheduler} implementation to manage the sequence optimisation given a set of arrival times at each sequence element. This class handles the construction of
 * {@link VoyagePlan}s and uses a {@link IVoyagePlanOptimiser} implementation to make the best route choices.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractSequenceScheduler implements ISequenceScheduler {
	private static final Logger log = LoggerFactory.getLogger(AbstractSequenceScheduler.class);

	@Inject
	private IElementDurationProvider durationsProvider;

	@Inject
	private ITimeWindowDataComponentProvider timeWindowProvider;

	@Inject
	private IElementDurationProvider durationProvider;

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
	protected IRouteCostProvider routeCostProvider;

	@Inject
	private IReturnElementProvider returnElementProvider;
	
	public IRouteCostProvider getRouteCostProvider() {
		return routeCostProvider;
	}

	public void setRouteCostProvider(final IRouteCostProvider routeCostProvider) {
		this.routeCostProvider = routeCostProvider;
	}

	public ScheduledSequences schedule(final ISequences sequences, final int[][] arrivalTimes) {
		final ScheduledSequences result = new ScheduledSequences();

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

		return result;
	}

	final public ScheduledSequence desOrFobSchedule(final IResource resource, final ISequence sequence) {
		// Virtual vessels are those operated by a third party, for FOB and DES situations.
		// Should we compute a schedule for them anyway? The arrival times don't mean much,
		// but contracts need this kind of information to make up numbers with.
		final List<Object> currentSequence = new ArrayList<Object>(5);
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
		currentPlan.setSequence(currentSequence.toArray());
		return new ScheduledSequence(resource, startTime, Collections.singletonList(currentPlan), times);
	}

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
	final public VoyageOptions getVoyageOptionsAndSetVpoChoices(final IVessel vessel, final VesselState vesselState, final int availableTime, final ISequenceElement element,
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
				options.setAvailableLNG(heelOptionsSlot.getHeelOptions().getHeelLimit() );
				useNBO = true;
				forceNBO = true;
			}
		} else if (useNBO) {
			options.setAvailableLNG(vessel.getCargoCapacity());
		} else {
			options.setAvailableLNG(0);
		}

		if (options.getAvailableLNG() == 0) {
			useNBO = false;
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
				if (useNBO) {
					if (thisPort.shouldVesselsArriveCold()) {
						// we don't want to use cooldown ever
						options.setAllowCooldown(false);
					} else {
						// we have a choice
						options.setAllowCooldown(false);
						optimiser.addChoice(new CooldownVoyagePlanChoice(options));
					}
				} else {
					// we have to allow cooldown, because there is no
					// NBO.
					options.setAllowCooldown(true);
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
	 * Returns an array of boolean values indicating whether, for each index of the vessel location sequence, a sequence break occurs at that location (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	final public boolean[] findSequenceBreaks(final ISequence sequence) {
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
	final public VesselState[] findVesselStates(final ISequence sequence) {
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
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	final public List<VoyagePlan> makeVoyagePlans(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {
		final IVessel vessel = vesselProvider.getVessel(resource);
		final boolean isShortsSequence = vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final List<VoyagePlan> voyagePlans = new LinkedList<VoyagePlan>();
		final List<Object> voyageOrPortOptions = new ArrayList<Object>(5);
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
					final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3);
					if (plan == null) {
						return null;
					}
					voyagePlans.add(plan);
					
					if (plan.getRemainingHeelType() == HeelType.END) {
						heelVolumeInM3 = plan.getRemainingHeelInM3();
					}
					else{
						heelVolumeInM3 = 0;
					}
				}

				if (isShortsSequence) {
					voyagePlans.get(voyagePlans.size() - 1).setIgnoreEnd(false);
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
			final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, currentTimes, voyagePlanOptimiser, heelVolumeInM3);
			if (plan == null) {
				return null;
			}
			voyagePlans.add(plan);
		}

		return voyagePlans;
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
	public ScheduledSequence schedule(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {
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
		voyagePlanOptimiser.setVessel(vessel, startTime);

		final List<VoyagePlan> voyagePlans = makeVoyagePlans(resource, sequence, arrivalTimes);
		if (voyagePlans == null) {
			return null;
		}

		return new ScheduledSequence(resource, startTime, voyagePlans, arrivalTimes);
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
	final public VoyagePlan getOptimisedVoyagePlan(final List<Object> voyageOrPortOptionsSubsequence, final List<Integer> arrivalTimes, final IVoyagePlanOptimiser optimiser, long heelVolumeInM3) {
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

	public final boolean optimiseSequence(final List<VoyagePlan> voyagePlans, final List<Object> currentSequence, final List<Integer> currentTimes, final IVoyagePlanOptimiser optimiser, final long startHeel) {
		final VoyagePlan plan = getOptimisedVoyagePlan(currentSequence, currentTimes, optimiser, startHeel);
		if (plan == null) {
			return false;
		}

		voyagePlans.add(plan);
		return true;
	}

	public final IPortSlotProvider getPortSlotProvider() {
		return portSlotProvider;
	}

	public final void setPortSlotProvider(final IPortSlotProvider portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}

	public final IPortTypeProvider getPortTypeProvider() {
		return portTypeProvider;
	}

	public final void setPortTypeProvider(final IPortTypeProvider portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public final IElementDurationProvider getDurationsProvider() {
		return durationsProvider;
	}

	public final void setDurationsProvider(final IElementDurationProvider durationsProvider) {
		this.durationsProvider = durationsProvider;
	}

	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(final ITimeWindowDataComponentProvider timeWindowProvider) {
		this.timeWindowProvider = timeWindowProvider;
	}

	public final IPortProvider getPortProvider() {
		return portProvider;
	}

	public final void setPortProvider(final IPortProvider portProvider) {
		this.portProvider = portProvider;
	}

	public final IMultiMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public final void setDistanceProvider(final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public final IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public final void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public IVoyagePlanOptimiser getVoyagePlanOptimiser() {
		return voyagePlanOptimiser;
	}

	public void setVoyagePlanOptimiser(final IVoyagePlanOptimiser voyagePlanOptimiser) {
		this.voyagePlanOptimiser = voyagePlanOptimiser;
	}

	/**
	 * Ensures all required data items are present, otherwise an {@link IllegalStateException} will be thrown.
	 */
	public void init() {

		// Verify that everything is in place
		if (routeCostProvider == null) {
			throw new IllegalStateException("No route cost provider set");
		}
		if (portProvider == null) {
			throw new IllegalStateException("No port provider set");
		}
		if (portSlotProvider == null) {
			throw new IllegalStateException("No port slot provider set");
		}
		if (portTypeProvider == null) {
			throw new IllegalStateException("No port type provider set");
		}
		if (distanceProvider == null) {
			throw new IllegalStateException("No port distance provider set");
		}
		if (timeWindowProvider == null) {
			throw new IllegalStateException("No time window provider set");
		}
		if (durationsProvider == null) {
			throw new IllegalStateException("No port durations provider set");
		}
		if (vesselProvider == null) {
			throw new IllegalStateException("No vessel provider set");
		}
		if (voyagePlanOptimiser == null) {
			throw new IllegalStateException("No voyage plan optimiser set");
		}
	}

	private BufferedWriter logWriter;
	private static boolean loggingEnabled = false;

	public static void setLoggingEnabled(final boolean loggingEnabled) {
		AbstractSequenceScheduler.loggingEnabled = loggingEnabled;
	}

	private static int tag = 0;

	private static synchronized int getTag() {
		return tag++;
	}

	protected final void createLog() {
		if (!loggingEnabled) {
			return;
		}
		try {
			final String name = getClass().getSimpleName() + "_log_" + getTag();
			final File f = new File("./" + name + ".py");
			logWriter = new BufferedWriter(new FileWriter(f));

			log.debug("Created scheduler log " + f.getAbsolutePath());
		} catch (final IOException ex) {
		}
	}

	protected final void startLogEntry(final int sequenceSize) {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write("Schedule(" + sequenceSize + ",[");
		} catch (final IOException e) {

		}
	}

	protected final void logValue(final long fitness) {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write(fitness + ", ");
		} catch (final IOException e) {
		}
	}

	protected final void endLogEntry() {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write("])\n");
			logWriter.flush();
		} catch (final IOException e) {
		}
	}

	protected final void closeLog() {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.flush();
			logWriter.close();
		} catch (final IOException e) {
		}
		logWriter = null;
	}

	@Override
	public void dispose() {

		durationsProvider = null;
		timeWindowProvider = null;
		portProvider = null;
		portSlotProvider = null;
		portTypeProvider = null;
		vesselProvider = null;
		distanceProvider = null;
		voyagePlanOptimiser = null;
	}

}