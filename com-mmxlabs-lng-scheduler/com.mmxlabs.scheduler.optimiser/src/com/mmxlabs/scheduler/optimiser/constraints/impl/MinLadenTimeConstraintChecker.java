/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMinTravelTimeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class MinLadenTimeConstraintChecker implements IPairwiseConstraintChecker, IInitialSequencesConstraintChecker {

	private enum Mode {
		RECORD, APPLY
	}

	@NonNull
	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IElementDurationProvider elementDurationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IMinTravelTimeProvider minTravelTimeProvider;

	private Set<@NonNull Pair<@NonNull ISequenceElement, @NonNull ISequenceElement>> whiteListedPairs = new HashSet<>();

	@Inject(optional = true) // Marked as optional as this constraint checker is active in the initial
								// sequence builder where we do not have an existing initial solution.
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@Nullable
	private ISequences initialSequences;

	public MinLadenTimeConstraintChecker(@NonNull final String name) {
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

		final Collection<@NonNull IResource> loopResources = changedResources != null ? changedResources : fullSequences.getResources();
		for (final IResource resource : loopResources) {
			final ISequence sequence = fullSequences.getSequence(resource);
			if (!checkSequence(sequence, resource, mode, messages)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, Mode mode, final List<String> messages) {
		if (sequence.isEmpty()) {
			return true;
		}

		final Iterator<@NonNull ISequenceElement> iter = sequence.iterator();
		@NonNull
		ISequenceElement prev = iter.next();
		@NonNull
		ISequenceElement cur;
		cur = iter.next();
		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (!checkPairwiseConstraint(prev, cur, resource, mode, messages)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Can element 2 be reached from element 1 in accordance with time windows under
	 * the best possible circumstances, if using the given resource to service them
	 * 
	 * @param e1
	 * @param e2
	 * @param resource the vessel in question
	 * @return
	 */
	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		return checkPairwiseConstraint(first, second, resource, Mode.APPLY, messages);
	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, Mode mode,
			final List<String> messages) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2) //
				|| vesselProvider.getVesselCharter(resource).getVesselInstanceType().isNonShipped()) {
			return true;
		}

		if (slot1 instanceof @NonNull LoadSlot ls && slot2.getPortType() == PortType.Discharge) {
			final Integer minLadenTime = minTravelTimeProvider.getMinTravelTime(ls);
			if (minLadenTime != null) {
				final ITimeWindow tw1 = slot1.getTimeWindow();
				final ITimeWindow tw2 = slot2.getTimeWindow();
				if ((tw1 == null) || (tw2 == null)) {
					return true;
				}
				assert tw1.getExclusiveEnd() != Integer.MAX_VALUE;
				if (tw1.getInclusiveStart() + elementDurationProvider.getElementDuration(first) + minLadenTime.intValue() >= tw2.getExclusiveEnd()) {
					if (mode == Mode.APPLY && !whiteListedPairs.contains(Pair.of(first, second))) {
						if (messages != null)
							messages.add(String.format("%s: Window time difference between %s and %s exceed min laden time of %d", this.name, slot1.getId(), slot2.getId(), minLadenTime));
						return false;
					} else if (mode == Mode.RECORD) {
						whiteListedPairs.add(Pair.of(first, second));
					}
				}
			}
		}

		return true;

	}

	@Override
	public void sequencesAccepted(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, final List<String> messages) {
		whiteListedPairs.clear();
		checkConstraints(fullSequences, null, Mode.RECORD, messages);
	}

}