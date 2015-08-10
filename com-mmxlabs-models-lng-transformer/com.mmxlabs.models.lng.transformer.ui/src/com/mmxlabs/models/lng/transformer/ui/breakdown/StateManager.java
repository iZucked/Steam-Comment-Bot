package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

public final class StateManager {

	private final Map<ISequenceElement, Pair<IResource, Integer>> lookupTable = new HashMap<>();

	public StateManager(final ISequences rawSequences) {
		for (final IResource r : rawSequences.getResources()) {
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
	public Pair<IResource, Integer> getPositionForElement(final ISequenceElement element) {
		return lookupTable.get(element);
	}

}
