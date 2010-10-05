package com.mmxlabs.optimiser.common.constraints;

import java.util.List;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementation of {@link IConstraintChecker} to enforce a specific ordering
 * of sequence elements. This uses a
 * {@link IOrderedSequenceElementsDataComponentProvider} instance to provide the
 * constraint data.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class OrderedSequenceElementsConstraintChecker<T> implements
		IPairwiseConstraintChecker<T> {

	private final String name;

	private final String dataProviderKey;

	private IOrderedSequenceElementsDataComponentProvider<T> provider;

	public OrderedSequenceElementsConstraintChecker(final String name,
			final String dataProviderKey) {
		this.name = name;
		this.dataProviderKey = dataProviderKey;
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences,
			final List<String> messages) {

		for (final IResource resource : sequences.getResources()) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, messages)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check the given {@link ISequence} ensuring that the expected element
	 * follows the preceding element.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	private boolean checkSequence(final ISequence<T> sequence,
			final List<String> messages) {
		T prevElement = null;
		for (final T element : sequence) {
			if (prevElement != null) {
				final T expected = provider.getNextElement(prevElement);
				// If null, any element is allowed to follow.
				if (expected != null) {
					if (!expected.equals(element)) {
						if (messages != null) {
							final String msg = String.format(
									"Element (%s) is not followed by (%s)",
									prevElement, expected);
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
			final T expected = provider.getNextElement(prevElement);
			// If null, any element is allowed to follow.
			if (expected != null) {
				if (messages != null) {
					final String msg = String.format(
							"Element (%s) is not followed by (%s)",
							prevElement, expected);
					messages.add(msg);
				}

				return false;
			}
		}

		return true;
	}

	public void setProvider(
			final IOrderedSequenceElementsDataComponentProvider<T> provider) {
		this.provider = provider;
	}

	public IOrderedSequenceElementsDataComponentProvider<T> getProvider() {
		return provider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setOptimisationData(final IOptimisationData<T> optimisationData) {
		final IOrderedSequenceElementsDataComponentProvider<T> dataProvider = optimisationData
				.getDataComponentProvider(dataProviderKey,
						IOrderedSequenceElementsDataComponentProvider.class);
		setProvider(dataProvider);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(final T first, final T second,
			final IResource resource) {
		final T afterFirst = provider.getNextElement(first);
		if (afterFirst == null) {
			final T beforeSecond = provider.getPreviousElement(second);
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
	public String explain(final T first, final T second,
			final IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}
}
