/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Swap one element for another within a single {@link ISequence}.
 * 
 * @author Simon Goodall
 * 
 */
public class SwapSingleSequenceElements implements IMove {
	private final IResource resource;
	private final int indexA;
	private final int indexB;

	public SwapSingleSequenceElements(final IResource resource, final int indexA, final int indexB) {
		super();
		this.resource = resource;
		this.indexA = indexA;
		this.indexB = indexB;
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
		final IModifiableSequence seq = sequences.getModifiableSequence(resource);
		final ISequenceElement elementA = seq.get(indexA);
		final ISequenceElement elementB = seq.get(indexB);
		seq.set(indexA, elementB);
		seq.set(indexB, elementA);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#validate(com.mmxlabs.optimiser.core.ISequences)
	 */
	@Override
	public boolean validate(@NonNull final ISequences sequences) {

		if (indexA == indexB) {
			return false;
		}
		if (indexA < 0 || indexB < 0) {
			return false;
		}
		ISequence sequence = sequences.getSequence(resource);
		if (sequence == null) {
			return false;
		}
		if (indexA >= sequence.size() || indexB >= sequence.size()) {
			return false;
		}
		return true;
	}

	public IResource getResource() {
		return resource;
	}

	public int getIndexA() {
		return indexA;
	}

	public int getIndexB() {
		return indexB;
	}
}
