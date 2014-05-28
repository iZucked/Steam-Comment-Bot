/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
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
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null) {
					if (!checkPairwiseConstraint(prev, current, resource)) {
						return false;
					}
				}
				prev = current;
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {

		final VesselInstanceType instanceType = vesselProvider.getVessel(resource).getVesselInstanceType();
		if (instanceType == VesselInstanceType.CARGO_SHORTS) {
			// Cargo pairs are independent of each other, so only check real load->discharge state and ignore rest
			final PortType t1 = portTypeProvider.getPortType(first);
			final PortType t2 = portTypeProvider.getPortType(second);
			// Accept, or fall through
			if (!(t1 == PortType.Load && t2 == PortType.Discharge)) {
				return true;
			}
		}

		final boolean result = !restrictedElementsProvider.getRestrictedFollowerElements(first).contains(second) && !restrictedElementsProvider.getRestrictedPrecedingElements(second).contains(first);
		return result;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}
}
