/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

public final class StateManager {

	private final Map<ISequenceElement, Pair<IResource, Integer>> lookupTable = new HashMap<>();

	@NonNull
	private final ISequences rawSequences;

	@NonNull
	private final ISequences fullSequences;

	public StateManager(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences) {
		this.rawSequences = rawSequences;
		this.fullSequences = fullSequences;
		for (final IResource r : rawSequences.getResources()) {
			assert r != null;
			final ISequence s = rawSequences.getSequence(r);
			int idx = 0;
			for (final ISequenceElement e : s) {
				final Pair<IResource, Integer> p = new Pair<>(r, idx++);
				lookupTable.put(e, p);
			}
		}
		int idx = 0;
		for (final ISequenceElement e : rawSequences.getUnusedElements()) {
			final Pair<IResource, Integer> p = new Pair<>(null, idx++);
			lookupTable.put(e, p);

		}
	}

	/**
	 * Returns the current resource (or null for unused) and the index in the sequence (or unused element list) the element is at.
	 * 
	 * @param element
	 * @return
	 */
	@NonNull
	public Pair<IResource, Integer> getPositionForElement(final ISequenceElement element) {
		Pair<IResource, Integer> pair = lookupTable.get(element);
		assert pair != null;
		return pair;
	}

	@NonNull
	public ISequences getRawSequences() {
		return rawSequences;
	}

	@NonNull
	public ISequences getFullSequences() {
		return fullSequences;
	}

}
