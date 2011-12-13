/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

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
	final int elementIndex;

	public RemoveOptionalElement(IResource resource, int elementIndex) {
		super();
		this.resource = resource;
		this.elementIndex = elementIndex;
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return Collections.singleton(resource);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.optimiser.lso.IMove#apply(com.mmxlabs.optimiser.core.IModifiableSequences)
	 */
	@Override
	public void apply(IModifiableSequences sequences) {
		sequences.getModifiableUnusedElements().add(sequences.getSequence(resource).get(elementIndex));
		sequences.getModifiableSequence(resource).remove(elementIndex);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.optimiser.lso.IMove#validate(com.mmxlabs.optimiser.core.ISequences)
	 */
	@Override
	public boolean validate(ISequences sequences) {
		return true;
	}

}
