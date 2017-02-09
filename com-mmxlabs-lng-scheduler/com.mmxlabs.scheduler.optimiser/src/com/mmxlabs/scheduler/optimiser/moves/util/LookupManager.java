package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;

public class LookupManager implements ILookupManager {

	@Inject
	@NonNull
	private IAlternativeElementProvider alternativeElementProvider;

	/**
	 * A reverse lookup table from elements to positions. The {@link Pair} is a item containing {@link Resource} index and position within the {@link ISequence}. There are some special cases here. A
	 * null Resource index means the {@link ISequenceElement} is not part of the main set of sequences. A value of zero or more indicates the position within the {@link ISequences#getUnusedElements()}
	 * list. A negative number means it is not for normal use. Currently -1 means the element is the unused pair of an alternative (@see {@link IAlternativeElementProvider}
	 */
	private final Map<ISequenceElement, Pair<IResource, Integer>> reverseLookup = new HashMap<>();

	private ISequences sequences;

	@Override
	public void createLookup(final @NonNull ISequences sequences) {

		this.sequences = sequences;
		this.reverseLookup.clear();

		// build table for elements in conventional sequences
		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			for (int j = 0; j < sequence.size(); j++) {
				final ISequenceElement element = sequence.get(j);
				reverseLookup.put(element, new Pair<>(resource, j));
				if (alternativeElementProvider != null && alternativeElementProvider.hasAlternativeElement(element)) {
					final ISequenceElement alt = alternativeElementProvider.getAlternativeElement(element);
					// Negative numbers now indicate alternative
					reverseLookup.put(alt, new Pair<>(null, -1));
					// reverseLookup.get(alt).setBoth(null, -1);
				}
			}
		}

		// build table for excluded elements
		if (alternativeElementProvider != null) {
			int x = 0;
			for (final ISequenceElement element : sequences.getUnusedElements()) {
				reverseLookup.put(element, new Pair<>(null, x));
				if (alternativeElementProvider.hasAlternativeElement(element)) {
					final ISequenceElement alt = alternativeElementProvider.getAlternativeElement(element);
					reverseLookup.put(alt, new Pair<>(null, -1));
				}
				x++;
			}
		}
	}

	@Override
	public ISequences getRawSequences() {
		final ISequences pSsequences = sequences;
		if (pSsequences == null) {
			throw new IllegalStateException();
		}
		return pSsequences;
	}

	/**
	 * Returns a {@link Pair} mapping an {@link ISequenceElement} to a {@link IResource} / array index. If the {@link IResource} is null, this indicates the element is in the unused list. If the index
	 * is -1, then this marks the element as the unused half of an alternative element pair (see {@link IAlternativeElementProvider}. Finally if a null Pair is return the element has not been found at
	 * all.
	 */
	@Override
	public Pair<IResource, Integer> lookup(@NonNull final ISequenceElement element) {
		return reverseLookup.get(element);
	}
}
