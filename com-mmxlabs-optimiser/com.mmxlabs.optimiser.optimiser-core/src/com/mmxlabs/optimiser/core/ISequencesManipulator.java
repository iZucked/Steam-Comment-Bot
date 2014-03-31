/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

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
	 * @param data
	 * @since 2.0
	 */
	void init(IOptimisationData data);
	
	/**
	 * Manipulate the given {@link ISequences}
	 * 
	 * @param sequences
	 */
	void manipulate(IModifiableSequences sequences);

	void dispose();
}
