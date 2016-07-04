/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * The {@link SlotGroup} links a {@link Collection} of {@link ISequenceElement}s and a number representing the maximum number of elements within this collection which may be present in the
 * {@link ISequences} solution. For example, if there are 10 elements in the collection and the count is 5, then only 5 of these elements may be present in any of the {@link ISequence} instances in
 * the {@link ISequences} object. The other 5 must be in the {@link ISequences#getUnusedElements()} list.
 * 
 * @author Simon Goodall
 * 
 */
public final class SlotGroup {
	private final Collection<ISequenceElement> elements;

	private final int count;

	public SlotGroup(final Collection<ISequenceElement> elements, final int count) {
		this.elements = Collections.unmodifiableCollection(elements);
		this.count = count;
	}

	public Collection<ISequenceElement> getElements() {
		return elements;
	}

	public int getCount() {
		return count;
	}
}
