/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core;

import java.util.Map;

// TODO: Move into opt-core

public interface IAnnotatedSolution<T> {

	/**
	 * Returns the {@link ISequences} associated with this solution
	 * 
	 * @return
	 */
	ISequences<T> getSequences();

	/**
	 * Returns the {@link IAnnotations} associated with the sequence elements in
	 * this solution
	 * 
	 * @return
	 */
	IAnnotations<T> getElementAnnotations();

	//annotations for other components?
	//IAnnotations<IResource> getResourceAnnotations();
	//IAnnotations<ISequence> getSequenceAnnotations();
	
	/**
	 * Returns the {@link IOptimisationContext} used to create this solution.
	 * 
	 * @return
	 */
	IOptimisationContext<T> getContext();

	/**
	 * Release any internal resources.
	 */
	void dispose();

	/**
	 * Get the general annotations associated with the whole solution.
	 * @return
	 */
	Map<String, Object> getGeneralAnnotations();

	/**
	 * Set a general annotation.
	 * @param key
	 * @param value
	 */
	void setGeneralAnnotation(String key, Object value);
}
