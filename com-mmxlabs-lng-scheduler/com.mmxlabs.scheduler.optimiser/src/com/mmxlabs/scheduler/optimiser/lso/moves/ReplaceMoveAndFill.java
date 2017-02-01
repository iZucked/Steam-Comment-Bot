/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ReplaceMoveAndFill implements IMove {
	final IResource candidateResource;
	final IResource followerResource;

	final int candidateElementIndex;
	final int fillerElementIndex;
	final int followerElementIndex;
	final int unusedElementIndex;

	final boolean unusedFirst;

	public ReplaceMoveAndFill(final IResource candidateResource, final IResource followerResource, final int candidateElementIndex, final int followerElementIndex, final int fillerElementIndex,
			final int unusedElementIndex, final boolean unusedFirst) {
		super();
		this.candidateResource = candidateResource;
		this.followerResource = followerResource;
		this.candidateElementIndex = candidateElementIndex;
		this.followerElementIndex = followerElementIndex;
		this.fillerElementIndex = fillerElementIndex;
		this.unusedElementIndex = unusedElementIndex;
		this.unusedFirst = unusedFirst;
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return CollectionsUtil.makeArrayList(followerResource, candidateResource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#apply(com.mmxlabs.optimiser.core.IModifiableSequences)
	 */
	@Override
	public void apply(final IModifiableSequences sequences) {
		// we are going to kill elements 1 and 2
		// and then put the element before 2 where 1 was
		// so 2 is the "discharge" and 1 is the "load"

		final IModifiableSequence canditateR = sequences.getModifiableSequence(candidateResource);
		final IModifiableSequence followerR = sequences.getModifiableSequence(followerResource);

		final List<ISequenceElement> spare = sequences.getModifiableUnusedElements();

		final ISequenceElement candidateElement = canditateR.get(candidateElementIndex);
		final ISequenceElement fillerElement = spare.get(fillerElementIndex);
		final ISequenceElement unusedElement = spare.get(unusedElementIndex);

		// Remove elements from current position
		spare.remove(unusedElement);
		spare.remove(fillerElement);

		canditateR.set(candidateElementIndex, fillerElement);
		// Insert them into the new root
		if (unusedFirst) {
			followerR.insert(followerElementIndex, candidateElement);
			followerR.insert(followerElementIndex, unusedElement);
		} else {
			followerR.insert(followerElementIndex, unusedElement);
			followerR.insert(followerElementIndex, candidateElement);
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
