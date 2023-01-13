/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

/**
 * Implementation of {@link IConstraintChecker} to enforce a specific ordering
 * of sequence elements. This uses a
 * {@link IOrderedSequenceElementsDataComponentProvider} instance to provide the
 * constraint data.
 * 
 * @author Simon Goodall
 * 
 */
public final class OrderedSequenceElementsConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
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
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

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
	 * Check the given {@link ISequence} ensuring that the expected element follows
	 * the preceding element.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	private boolean checkSequence(final ISequence sequence, final List<String> messages) {
		ISequenceElement prevElement = null;
		for (final ISequenceElement element : sequence) {
			if (prevElement != null) {
				{
					final ISequenceElement expected = provider.getNextElement(prevElement);
					// If null, any element is allowed to follow.
					if (expected != null && expected != element) {
						if (messages != null)
							messages.add(String.format("%s: current element %s is not the expected one %s!", this.name, element.getName(), expected.getName()));
						return false;
					}
				}
				{
					final ISequenceElement expected = provider.getPreviousElement(element);
					// If null, any element is allowed to follow.
					if (expected != null && expected != prevElement) {
						if (messages != null)
							messages.add(String.format("%s: previous element %s is not the expected one %s!", this.name, prevElement.getName(), expected.getName()));
						return false;
					}
				}
			}
			prevElement = element;
		}

		// Check that the last element in the sequence does not have a follow on item.
		if (prevElement != null) {
			final ISequenceElement expected = provider.getNextElement(prevElement);
			// If not null, we expected another element, but there is none.
			if (expected != null) {
				if (messages != null)
					messages.add(String.format("%s: last element %s is unexpectedly followed by element %s!", this.name, prevElement.getName(), expected.getName()));
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		final ISequenceElement afterFirst = provider.getNextElement(first);
		if (afterFirst != null && afterFirst != second) {
			if (messages != null)
				messages.add(String.format("%s: element %s must follow %s, but is followed by %s!", this.name, second.getName(), first.getName(), afterFirst.getName()));
			return false;
		}

		final ISequenceElement beforeSecond = provider.getPreviousElement(second);
		if (beforeSecond != null && first != beforeSecond) {
			if (messages != null)
				messages.add(String.format("%s: element %s must follow %s, but is follows %s!", this.name, first.getName(), second.getName(), beforeSecond.getName()));
			return false;
		}
		return true;
	}
}
