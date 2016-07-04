/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Take an element out of a sequence and put it in the spares bag
 * 
 * @author hinton
 * 
 */
public class RemoveOptionalElement implements IMove {
	final IResource resource;
	final int[] elementIndices;

	public RemoveOptionalElement(final IResource resource, final int... elementIndices) {
		super();
		this.resource = resource;
		this.elementIndices = elementIndices;
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return Collections.singleton(resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#apply(com.mmxlabs.optimiser.core.IModifiableSequences)
	 */
	@Override
	public void apply(final IModifiableSequences sequences) {
		int offset = 0;
		Arrays.sort(elementIndices);
		for (int elementIndex : elementIndices) {
			sequences.getModifiableUnusedElements().add(sequences.getSequence(resource).get(elementIndex - offset));
			sequences.getModifiableSequence(resource).remove(elementIndex- offset);
			offset++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#validate(com.mmxlabs.optimiser.core.ISequences)
	 */
	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}

}
