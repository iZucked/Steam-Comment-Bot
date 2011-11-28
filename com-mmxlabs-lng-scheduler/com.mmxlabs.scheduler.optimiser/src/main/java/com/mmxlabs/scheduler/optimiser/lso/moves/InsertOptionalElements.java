/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * A move to insert some optional elements into a route
 * 
 * @author hinton
 * 
 */
public class InsertOptionalElements<T> implements IMove<T> {
	final IResource resource;
	final int beforeInsertionPoint;
	final int[] insertedElements;

	public InsertOptionalElements(IResource resource, int beforeInsertionPoint, int[] insertedElements) {
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
	public void apply(final IModifiableSequences<T> sequences) {
		final IModifiableSequence<T> sequence = sequences.getModifiableSequence(resource);
		final List<T> unused = sequences.getModifiableUnusedElements();
		int k = 1;
		for (int i : insertedElements) {
			sequence.insert(beforeInsertionPoint + k, unused.get(i));
			k++;
		}

		Arrays.sort(insertedElements);
		k = 0;
		for (int i : insertedElements) {
			unused.remove(i - k);
			k++;
		}
	}

	@Override
	public boolean validate(ISequences<T> sequences) {
		return true;
	}

}
