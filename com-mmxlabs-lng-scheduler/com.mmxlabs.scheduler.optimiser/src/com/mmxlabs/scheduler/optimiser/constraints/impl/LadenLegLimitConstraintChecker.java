/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
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
public class LadenLegLimitConstraintChecker implements IPairwiseConstraintChecker {

	@NonNull
	private final String name;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IActualsDataProvider actualsDataProvider;

	private final int maxLadenDuration = 60 * 24;

	public LadenLegLimitConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev, cur;
		prev = cur = null;

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int maxSpeed = vesselAvailability.getVessel().getVesselClass().getMaxSpeed();

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null && cur != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, maxSpeed)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		return checkConstraints(sequences, changedResources);
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {

	}

	@Override
	/**
	 * Can element 2 be reached from element 1 in accordance with time windows under the best possible circumstances, if using the given resource to service them
	 * 
	 * @param e1
	 * @param e2
	 * @param resource
	 *            the vessel in question
	 * @return
	 */
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		return checkPairwiseConstraint(first, second, resource, vesselAvailability.getVessel().getVesselClass().getMaxSpeed());
	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final int resourceMaxSpeed) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			return true;
		}

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return true;
		}

		if (slot1.getPortType() == PortType.Load && slot2.getPortType() == PortType.Discharge) {
			final ITimeWindow tw1 = slot1.getTimeWindow();
			final ITimeWindow tw2 = slot2.getTimeWindow();

			if ((tw1 == null) || (tw2 == null)) {
				return true; // if the time windows are null, there is no effective constraint
			}

			if (tw2.getInclusiveStart() - tw1.getExclusiveEnd() > maxLadenDuration) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}
