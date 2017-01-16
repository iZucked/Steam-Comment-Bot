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
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Remove a couple of optional elements and fill in a gap by moving another element.
 * 
 * @author hinton
 * 
 */
public class RemoveAndFill implements IMove {
	final IResource resource1;
	final IResource resource2;

	final int element1;
	final int element2;

	/**
	 * Construct a remove-and-fill move. This takes two optional sequence elements which are both sequenced at the moment, and de-sequences them. The first element is replaced by whatever comes before
	 * the second element, and the second element is just taken out.
	 * <p>
	 * In LNG terms, this is intended to be used when element 1 is an optional load slot and element 2 is an optional discharge slot. The element before element 2 is thus presumed to be a load, and
	 * gets moved to replace element 1.
	 * 
	 * @param resource1
	 *            The resource on which the first element lives
	 * @param resource2
	 *            The resource on which the second element lives
	 * @param element1
	 *            The index of the first element to be removed; this is something like a load slot, and will be replaced by sequences[resource2][element2-1]
	 * @param element2
	 *            The index of the second element to be removed.
	 */
	public RemoveAndFill(final IResource resource1, final IResource resource2, final int element1, final int element2) {
		super();
		this.resource1 = resource1;
		this.resource2 = resource2;
		this.element1 = element1;
		this.element2 = element2;
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
		// we are going to kill elements 1 and 2
		// and then put the element before 2 where 1 was
		// so 2 is the "discharge" and 1 is the "load"

		final IModifiableSequence seq1 = sequences.getModifiableSequence(resource1);
		final IModifiableSequence seq2 = sequences.getModifiableSequence(resource2);
		final List<ISequenceElement> spare = sequences.getModifiableUnusedElements();

		final ISequenceElement e1 = seq1.get(element1);
		final ISequenceElement e2 = seq2.get(element2);

		spare.add(e1);
		spare.add(e2);

		seq1.set(element1, seq2.get(element2 - 1));
		seq2.remove(element2);
		seq2.remove(element2 - 1);
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
