/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	@NonNull
	private IOrderedSequenceElementsDataComponentProvider provider;

	@NonNull
	private final String name;

	public OrderedSequenceElementsConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {

	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
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
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
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
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}
