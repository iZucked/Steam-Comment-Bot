/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Constraint checker to limit the length of laden legs to avoid excessively long voyages. This checks the the minimum travel time (based on end of load window to start of discharge window) does not
 * exceed a specified limit (e.g. 60 days). This value should be made large enough to cover the load duration, travel time and option to delay discharge until the next pricing month.
 * 
 * @author Simon Goodall
 *
 */
public class LadenLegLimitConstraintChecker implements IPairwiseConstraintChecker, IInitialSequencesConstraintChecker {

	private enum Mode {
		RECORD, APPLY
	}

	@NonNull
	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	private int maxLadenDuration = 60 * 24;

	private Set<IPortSlot> whitelistedSlots = new HashSet<>();

	@Inject(optional = true) // Marked as optional as this constraint checker is active in the initial sequence builder where we do not have an existing initial solution.
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@Nullable
	private ISequences initialSequences;

	public LadenLegLimitConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences fullSequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
		return checkConstraints(fullSequences, changedResources, Mode.APPLY, messages);
	}

	private boolean checkConstraints(@NonNull final ISequences fullSequences, @Nullable final Collection<@NonNull IResource> changedResources, Mode mode, final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = fullSequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = fullSequences.getSequence(resource);
			if (!checkSequence(sequence, resource, mode, messages)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, Mode mode, final List<String> messages) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev = null;
		ISequenceElement cur = null;

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int maxSpeed = vesselAvailability.getVessel().getMaxSpeed();

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null && cur != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, maxSpeed, mode, messages)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Can element 2 be reached from element 1 in accordance with time windows under the best possible circumstances, if using the given resource to service them
	 * 
	 * @param e1
	 * @param e2
	 * @param resource
	 *            the vessel in question
	 * @return
	 */
	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		return checkPairwiseConstraint(first, second, resource, vesselAvailability.getVessel().getMaxSpeed(), Mode.APPLY, messages);
	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final int resourceMaxSpeed, Mode mode, final List<String> messages) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType().isNonShipped()) {
			return true;
		}

		if (slot1.getPortType() == PortType.Load && slot2.getPortType() == PortType.Discharge) {
			final ITimeWindow tw1 = slot1.getTimeWindow();
			final ITimeWindow tw2 = slot2.getTimeWindow();

			if ((tw1 == null) || (tw2 == null)) {
				return true; // if the time windows are null, there is no effective constraint
			}
			assert tw1.getExclusiveEnd() != Integer.MAX_VALUE;
			if (tw2.getInclusiveStart() - tw1.getExclusiveEnd() > maxLadenDuration) {
				// Violation! Check whitelist to see if we can ignore it.
				if (mode == Mode.APPLY && !whitelistedSlots.contains(slot1)) {
					if (messages != null)
						messages.add(String.format("%s: Travel time from last discharge port %s (slot %s) to the next load port %s (slot %s) is longer than allowed %d hours.", 
							this.name, slot1.getPort().getName(), slot1.getId(), slot2.getPort().getName(), slot2.getId(), maxLadenDuration));
					return false;
				} else if (mode == Mode.RECORD) {
					whitelistedSlots.add(slot1);
				}
			}
		}

		return true;

	}

	public int getMaxLadenDuration() {
		return maxLadenDuration;
	}

	public void setMaxLadenDuration(int maxLadenDuration) {
		this.maxLadenDuration = maxLadenDuration;
	}

	@Override
	public void sequencesAccepted(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, final List<String> messages) {
		whitelistedSlots.clear();
		checkConstraints(fullSequences, null, Mode.RECORD, messages);
	}
}
