/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Interface defining a Move as used in the Local Search Optimiser.
 * 
 * @author Simon Goodall
 * 
 */
public interface IMove {

	/**
	 * Returns the collection of {@link IResource}s this move affects.
	 * 
	 * @return
	 */
	Collection<IResource> getAffectedResources();

	/**
	 * Apply the move to the {@link IResources} listed in {@link #getAffectedResources()}.
	 * 
	 * @param sequences
	 */
	void apply(IModifiableSequences sequences);

	/**
	 * Validate the move parameters are valid for the given {@link ISequences} object.
	 * 
	 * @return True on valid parameters.
	 */
	boolean validate(ISequences sequences);
}
