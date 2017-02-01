/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.moves;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Interface defining a Move as used in the Local Search Optimiser.
 * 
 * @author Simon Goodall
 * 
 */
public interface IMove extends IMoveType {

	/**
	 * Returns the collection of {@link IResource}s this move affects.
	 * 
	 * @return
	 */
	@NonNull
	Collection<@NonNull IResource> getAffectedResources();

	/**
	 * Apply the move to the {@link IResources} listed in {@link #getAffectedResources()}.
	 * 
	 * @param sequences
	 */
	void apply(@NonNull IModifiableSequences sequences);

	/**
	 * Validate the move parameters are valid for the given {@link ISequences} object.
	 * 
	 * @return True on valid parameters.
	 */
	boolean validate(@NonNull ISequences sequences);
}
