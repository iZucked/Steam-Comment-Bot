/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Swap one optional element for another.
 * 
 * @author hinton
 * 
 */
public class SwapOptionalElements implements IMove {
	final IResource resource;
	final int usedElementIndex;
	final int unusedElementIndex;

	public SwapOptionalElements(@NonNull final IResource resource, final int usedElementIndex, final int unusedElementIndex) {
		super();
		this.resource = resource;
		this.usedElementIndex = usedElementIndex;
		this.unusedElementIndex = unusedElementIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#getAffectedResources()
	 */
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
	public void apply(@NonNull final IModifiableSequences sequences) {
		final ISequenceElement liveElement = sequences.getSequence(resource).get(usedElementIndex);
		final ISequenceElement deadElement = sequences.getUnusedElements().get(unusedElementIndex);
		sequences.getModifiableSequence(resource).set(usedElementIndex, deadElement);
		sequences.getModifiableUnusedElements().set(unusedElementIndex, liveElement);
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
