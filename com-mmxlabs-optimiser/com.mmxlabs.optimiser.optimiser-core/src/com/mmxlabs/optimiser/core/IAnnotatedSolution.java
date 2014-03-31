/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

public interface IAnnotatedSolution {

	/**
	 * Returns the {@link ISequences} associated with this solution
	 * 
	 * @return
	 */
	ISequences getSequences();

	/**
	 * Returns the {@link IElementAnnotationsMap} associated with the sequence elements in this solution
	 * 
	 * @return
	 */
	IElementAnnotationsMap getElementAnnotations();

	// annotations for other components?
	// IAnnotations<IResource> getResourceAnnotations();
	// IAnnotations<ISequence> getSequenceAnnotations();

	/**
	 * Returns the {@link IOptimisationContext} used to create this solution.
	 * 
	 * @return
	 */
	IOptimisationContext getContext();

	/**
	 * Release any internal resources.
	 */
	void dispose();

	<U> U getGeneralAnnotation(String key, Class<U> clz);

	/**
	 * Set a general annotation.
	 * 
	 * @param key
	 * @param value
	 */
	void setGeneralAnnotation(String key, Object value);
}
