/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

/**
 * Interface defining an object to manipulate an {@link IModifiableSequences}. Instances of this interface are likely to replace sequence elements with alternative elements or insert new elements into
 * the sequence. Some examples cases are to change start or end elements based upon the other elements (e.g. change start or end port in a ship scheduling problem) or to replace a group of elements
 * with a single element (e.g. interleaved elements).
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequencesManipulator {

	/**
	 * Manipulate the given {@link ISequences}
	 * 
	 * @param sequences
	 */
	void manipulate(@NonNull IModifiableSequences sequences);

	/**
	 * Create and return a manipulated sequences object.
	 * 
	 * @param rawSequences
	 * @return
	 */
	default @NonNull IModifiableSequences createManipulatedSequences(@NonNull final ISequences rawSequences) {
		final IModifiableSequences fullSequences = new ModifiableSequences(rawSequences);
		manipulate(fullSequences);
		return fullSequences;
	}
}
