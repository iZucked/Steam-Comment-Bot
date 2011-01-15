/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;


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
	
	<U> U getGeneralAnnotation(String key, Class<U> clz);
	
	/**
	 * Set a general annotation.
	 * @param key
	 * @param value
	 */
	void setGeneralAnnotation(String key, Object value);
}
