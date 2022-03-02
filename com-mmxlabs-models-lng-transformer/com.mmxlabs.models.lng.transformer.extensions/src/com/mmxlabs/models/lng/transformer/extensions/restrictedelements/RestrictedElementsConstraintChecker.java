/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public class RestrictedElementsConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	@Inject
	private IRestrictedElementsProvider restrictedElementsProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	public RestrictedElementsConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null) {
					if (!checkPairwiseConstraint(prev, current, resource, messages)) {
						return false;
					}
				}
				prev = current;
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IPhaseOptimisationData optimisationData) {

	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource, List<String> messages) {

		final VesselInstanceType instanceType = vesselProvider.getVesselAvailability(resource).getVesselInstanceType();
		if (instanceType == VesselInstanceType.ROUND_TRIP) {
			// Cargo pairs are independent of each other, so only check real load->discharge state and ignore rest
			final PortType t1 = portTypeProvider.getPortType(first);
			final PortType t2 = portTypeProvider.getPortType(second);
			// Accept, or fall through
			if (!(t1 == PortType.Load && t2 == PortType.Discharge)) {
				return true;
			}
		}

		final boolean result = !restrictedElementsProvider.getRestrictedFollowerElements(first).contains(second) && !restrictedElementsProvider.getRestrictedPrecedingElements(second).contains(first);
		if (!result && messages != null)
			messages.add(String.format("%s: Sequence element %s is restricted to follow/preceed the sequence element %s!", this.name, first.getName(), second.getName()));
		return result;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}
}
