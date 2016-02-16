/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

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

	private final @NonNull IResource resource;

	private final int beforeInsertionPoint;

	private final int @NonNull [] insertedElements;

	public InsertOptionalElements(@NonNull final IResource resource, final int beforeInsertionPoint, final int @NonNull [] insertedElements) {
		super();
		this.resource = resource;
		this.beforeInsertionPoint = beforeInsertionPoint;
		this.insertedElements = insertedElements;
	}

	@SuppressWarnings("null")
	@Override
	public Collection<IResource> getAffectedResources() {
		return Collections.singleton(resource);
	}

	@Override
	public void apply(@NonNull final IModifiableSequences sequences) {
		final IModifiableSequence sequence = sequences.getModifiableSequence(resource);
		final @NonNull List<@NonNull ISequenceElement> unused = sequences.getModifiableUnusedElements();
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
	public boolean validate(@NonNull final ISequences sequences) {
		return true;
	}
}
