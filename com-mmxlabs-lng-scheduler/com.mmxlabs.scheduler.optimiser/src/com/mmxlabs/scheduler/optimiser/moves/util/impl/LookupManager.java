/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	private IAlternativeElementProvider alternativeElementProvider;

	/**
	 * A reverse lookup table from elements to positions. The {@link Pair} is a item
	 * containing {@link Resource} index and position within the {@link ISequence}.
	 * There are some special cases here. A null Resource index means the
	 * {@link ISequenceElement} is not part of the main set of sequences. A value of
	 * zero or more indicates the position within the
	 * {@link ISequences#getUnusedElements()} list. A negative number means it is
	 * not for normal use. Currently -1 means the element is the unused pair of an
	 * alternative (@see {@link IAlternativeElementProvider}
	 */
	private final Map<ISequenceElement, Pair<IResource, Integer>> reverseLookup = new HashMap<>();

	private ISequences rawSequences;

	public LookupManager() {

	}

	public LookupManager(final @NonNull ISequences rawSequences) {
		createLookup(rawSequences);
	}

	@Override
	public void createLookup(final @NonNull ISequences rawSequences) {
		updateLookup(rawSequences, (Collection<IResource>) null);
	}

	@Override
	public void updateLookup(final @NonNull ISequences rawSequences, @Nullable Collection<@NonNull IResource> changedResources) {

		this.rawSequences = rawSequences;
		// Currently this is always true. In future update IMove API/usage to include
		// null for unused list
		boolean lookAtUnused = true;

		final Collection<IResource> loopResources;
		if (changedResources == null) {
			loopResources = rawSequences.getResources();
			this.reverseLookup.clear();
			lookAtUnused = true;
		} else {
			loopResources = changedResources;
		}

		// build table for elements in conventional sequences
		for (final IResource resource : loopResources) {
			if (resource == null) {
				lookAtUnused = true;
				continue;
			}

			updateSequence(rawSequences, resource);
		}

		if (true || lookAtUnused) {
			updateUnusedElements(rawSequences);
		}
	}

	@Override
	public void updateLookup(final @NonNull ISequences rawSequences, IResource... changedResources) {
		createLookup(rawSequences);
//		return;
//		if (changedResources == null || changedResources.length == 0) {
//			createLookup(rawSequences);
//			return;
//		}
//
//		this.rawSequences = rawSequences;
//
//		boolean lookAtUnused = false;
//
//		// build table for elements in conventional sequences
//		for (final IResource resource : changedResources) {
//			if (resource == null) {
//				lookAtUnused = true;
//				continue;
//			}
//
//			updateSequence(rawSequences, resource);
//		}
//
//		if (lookAtUnused) {
//			updateUnusedElements(rawSequences);
//		}
	}

	private void updateUnusedElements(final @NonNull ISequences rawSequences) {
		// build table for excluded elements
		int x = 0;
		for (final ISequenceElement element : rawSequences.getUnusedElements()) {
			reverseLookup.put(element, new Pair<>(null, x));
			if (alternativeElementProvider != null) {
				if (alternativeElementProvider.hasAlternativeElement(element)) {
					final ISequenceElement alt = alternativeElementProvider.getAlternativeElement(element);
					reverseLookup.put(alt, new Pair<>(null, -1));
				}
			}
			x++;
		}
	}

	private void updateSequence(final @NonNull ISequences rawSequences, final @NonNull IResource resource) {
		final ISequence sequence = rawSequences.getSequence(resource);
		for (int j = 0; j < sequence.size(); j++) {
			final ISequenceElement element = sequence.get(j);
			reverseLookup.put(element, new Pair<>(resource, j));
			if (alternativeElementProvider != null && alternativeElementProvider.hasAlternativeElement(element)) {
				final ISequenceElement alt = alternativeElementProvider.getAlternativeElement(element);
				// Negative numbers now indicate alternative
				reverseLookup.put(alt, new Pair<>(null, -1));
			}
		}
	}

	@Override
	public ISequences getRawSequences() {
		final ISequences pSsequences = rawSequences;
		if (pSsequences == null) {
			throw new IllegalStateException();
		}
		return pSsequences;
	}

	/**
	 * Returns a {@link Pair} mapping an {@link ISequenceElement} to a
	 * {@link IResource} / array index. If the {@link IResource} is null, this
	 * indicates the element is in the unused list. If the index is -1, then this
	 * marks the element as the unused half of an alternative element pair (see
	 * {@link IAlternativeElementProvider}. Finally if a null Pair is return the
	 * element has not been found at all.
	 */
	@Override
	public Pair<IResource, Integer> lookup(@NonNull final ISequenceElement element) {
		return reverseLookup.get(element);
	}
}
