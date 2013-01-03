package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 * @since 2.0
 */
public abstract class AbstractPairwiseConstraintChecker implements IPairwiseConstraintChecker {

	protected final String name;

	public AbstractPairwiseConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean checkSequence(final ISequence sequence, final IResource resource) {
		return checkSequence(sequence, resource, null);
	}

	public boolean checkSequence(final ISequence sequence, final IResource resource, final List<String> messages) {
		boolean valid = true;
		ISequenceElement prev = null;
		for (final ISequenceElement current : sequence) {
			if (prev != null) {
				if (!checkPairwiseConstraint(prev, current, resource)) {
					if (messages == null) {
						return false;
					} else {
						valid = false;
						messages.add(explain(prev, current, resource));
					}
				}
			}
			prev = current;
		}
		return valid;
	}
	
	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		boolean valid = true;

		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				if (messages == null) {
					return false;
				} else {
					valid = false;
				}
			}
		}

		return valid;
	}
	
	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}

}
