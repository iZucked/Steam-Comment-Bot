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
 * Move a couple of optional elements and fill in a gap by moving another element.
 * 
 * @author Simon Goodall
 * 
 */
public class MoveAndFill implements IMove {
	final IResource resource1;
	final IResource resource2;

	final int usedElementIndex;
	final int unusedElementIndex;
	final int insertionIndex;

	final boolean unusedFirst;

	/**
	 * Construct a move-and-fill move. This takes one sequence element which is currently sequenced at the moment, and it them. A currently unused element is also inserted into the solution.
	 * <p>
	 * 
	 * 
	 * @param resource1
	 *            The resource on which the currently used element lives
	 * @param resource2
	 *            The resource on which to move the element
	 * @param usedElementIndex
	 *            The index of the used element to be moved
	 * @param unusedElementIndex
	 *            The index of the unused element to be moved
	 * @param insertionIndex
	 *            The index of the position to insert into
	 * @param usuedFirst
	 *            If true, insert the as [unusued, used] otherwise as [used, unused]
	 */
	public MoveAndFill(final IResource resource1, final IResource resource2, final int usedElementIndex, final int unusedElementIndex, final int insertionIndex, final boolean unusedFirst) {
		super();
		this.resource1 = resource1;
		this.resource2 = resource2;
		this.usedElementIndex = usedElementIndex;
		this.unusedElementIndex = unusedElementIndex;
		this.insertionIndex = insertionIndex;
		this.unusedFirst = unusedFirst;
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return CollectionsUtil.makeArrayList(resource1, resource2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#apply(com.mmxlabs.optimiser.core.IModifiableSequences)
	 */
	@Override
	public void apply(final IModifiableSequences sequences) {

		final IModifiableSequence seq1 = sequences.getModifiableSequence(resource1);
		final IModifiableSequence seq2 = sequences.getModifiableSequence(resource2);
		final List<ISequenceElement> spare = sequences.getModifiableUnusedElements();

		final ISequenceElement usedElement = seq1.get(usedElementIndex);
		final ISequenceElement unusedElement = spare.get(unusedElementIndex);

		// Remove elements from current position
		spare.remove(unusedElementIndex);
		seq1.remove(usedElement);
		// Insert them into the new root
		if (unusedFirst) {
			seq2.insert(insertionIndex, unusedElement);
			seq2.insert(insertionIndex, usedElement);
		} else {
			seq2.insert(insertionIndex, usedElement);
			seq2.insert(insertionIndex, unusedElement);
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
