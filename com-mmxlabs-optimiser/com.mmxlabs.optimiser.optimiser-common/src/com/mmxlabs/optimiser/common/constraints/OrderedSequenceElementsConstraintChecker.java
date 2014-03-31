/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementation of {@link IConstraintChecker} to enforce a specific ordering of sequence elements. This uses a {@link IOrderedSequenceElementsDataComponentProvider} instance to provide the
 * constraint data.
 * 
 * @author Simon Goodall
 * 
 */
public final class OrderedSequenceElementsConstraintChecker implements IPairwiseConstraintChecker {
	@Inject
	private IOrderedSequenceElementsDataComponentProvider provider;

	private final String name;

	public OrderedSequenceElementsConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, messages)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check the given {@link ISequence} ensuring that the expected element follows the preceding element.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	private boolean checkSequence(final ISequence sequence, final List<String> messages) {
		ISequenceElement prevElement = null;
		for (final ISequenceElement element : sequence) {
			if (prevElement != null) {
				final ISequenceElement expected = provider.getNextElement(prevElement);
				// If null, any element is allowed to follow.
				if (expected != null) {
					if (!expected.equals(element)) {
						if (messages != null) {
							final String msg = String.format("Element (%s) is not followed by (%s)", prevElement, expected);
							messages.add(msg);
						}

						return false;
					}
				}
			}
			prevElement = element;
		}

		// Check that the last element in the sequence has a follow on item.
		if (prevElement != null) {
			final ISequenceElement expected = provider.getNextElement(prevElement);
			// If null, any element is allowed to follow.
			if (expected != null) {
				if (messages != null) {
					final String msg = String.format("Element (%s) is not followed by (%s)", prevElement, expected);
					messages.add(msg);
				}

				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final ISequenceElement afterFirst = provider.getNextElement(first);
		if (afterFirst == null) {
			final ISequenceElement beforeSecond = provider.getPreviousElement(second);
			if (beforeSecond == null) {
				return true;
			} else {
				// This should be reflexive anyway.
				return first.equals(beforeSecond);
			}
		} else {
			return afterFirst.equals(second);
		}
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}
}
