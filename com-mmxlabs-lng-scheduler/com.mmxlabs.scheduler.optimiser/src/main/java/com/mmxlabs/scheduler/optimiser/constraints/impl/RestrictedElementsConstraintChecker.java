package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IRestrictedElementsProvider;

public class RestrictedElementsConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	@Inject
	private IRestrictedElementsProvider restrictedElementsProvider;

	
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

		boolean result = !restrictedElementsProvider.getRestrictedFollowerElements(first).contains(second) && !restrictedElementsProvider.getRestrictedPrecedingElements(second).contains(first);
		return result;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}
}
