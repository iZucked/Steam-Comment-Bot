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
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Abstract {@link ISequenceScheduler} implementation to manage the sequence optimisation given a set of arrival times at each sequence element. This class handles the construction of
 * {@link VoyagePlan}s and uses a {@link IVoyagePlanOptimiser} implementation to make the best route choices.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractSequenceScheduler extends AbstractLoggingSequenceScheduler implements ISequenceScheduler {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	protected IRouteCostProvider routeCostProvider;

	@Inject
	private VoyagePlanner voyagePlanner;

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
	 * 
	 * @deprecated Use {@link VoyagePlanner#getVoyageOptionsAndSetVpoChoices(IVessel, VesselState, int, ISequenceElement, ISequenceElement, VoyageOptions, IVoyagePlanOptimiser, boolean)}
	@Deprecated
	 * 
	 * @deprecated Use {@link VoyagePlanner#findSequenceBreaks(ISequence)}
	 * @deprecated Use {@link VoyagePlanner#findVesselStates(ISequence)}
	@Deprecated
	 * 
	 * @deprecated Use {@link VoyagePlanner#makeVoyagePlans(IResource, ISequence, int[])}
	@Deprecated
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

		final List<VoyagePlan> voyagePlans = voyagePlanner.makeVoyagePlans(resource, sequence, arrivalTimes);
		if (voyagePlans == null) {
			return null;
		}

		return new ScheduledSequence(resource, startTime, voyagePlans, arrivalTimes);
	}
	 * @deprecated Use {@link VoyagePlanner#getOptimisedVoyagePlan(List, List, IVoyagePlanOptimiser)}
	@Deprecated
		return voyagePlanner.getOptimisedVoyagePlan(voyageOrPortOptionsSubsequence, arrivalTimes, optimiser);
	/**
	 * 
	 * @param voyagePlans
	 * @param currentSequence
	 * @param currentTimes
	 * @param optimiser
	 * @return
	 * 
	 * @deprecated Use {@link VoyagePlanner#optimiseSequence(List, List, List, IVoyagePlanOptimiser)}
	 */
	@Deprecated
}