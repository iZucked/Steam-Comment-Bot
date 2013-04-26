/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * A move to insert some optional elements into a route
 * 
 * @author hinton
 * 
 */
public class InsertOptionalElements implements IMove {
	final IResource resource;
	final int beforeInsertionPoint;
	final int[] insertedElements;

	public InsertOptionalElements(final IResource resource, final int beforeInsertionPoint, final int[] insertedElements) {
		super();
		this.resource = resource;
		this.beforeInsertionPoint = beforeInsertionPoint;
		this.insertedElements = insertedElements;
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return Collections.singleton(resource);
	}

	@Override
	public void apply(final IModifiableSequences sequences) {
		final IModifiableSequence sequence = sequences.getModifiableSequence(resource);
		final List<ISequenceElement> unused = sequences.getModifiableUnusedElements();
		int k = 1;
		for (final int i : insertedElements) {
			sequence.insert(beforeInsertionPoint + k, unused.get(i));
			k++;
		}

		Arrays.sort(insertedElements);
		k = 0;
		for (final int i : insertedElements) {
			unused.remove(i - k);
			k++;
		}
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}
}
