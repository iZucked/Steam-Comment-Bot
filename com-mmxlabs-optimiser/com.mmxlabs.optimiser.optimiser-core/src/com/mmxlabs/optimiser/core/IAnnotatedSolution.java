/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IAnnotatedSolution {

	/**
	 * Returns the {@link ISequences} associated with this solution
	 * 
	 * @return
	 */
	@NonNull 
	ISequences getSequences();

	/**
	 * Returns the {@link IElementAnnotationsMap} associated with the sequence elements in this solution
	 * 
	 * @return
	 */
	@NonNull 
	IElementAnnotationsMap getElementAnnotations();

	// annotations for other components?
	// IAnnotations<IResource> getResourceAnnotations();
	// IAnnotations<ISequence> getSequenceAnnotations();

	/**
	 * Returns the {@link IOptimisationContext} used to create this solution.
	 * 
	 * @return
	 */
	@NonNull 
	IOptimisationContext getContext();

	/**
	 * Release any internal resources.
	 */
	void dispose();

	@Nullable
	<U> U getGeneralAnnotation(@NonNull String key,@NonNull  Class<U> clz);

	/**
	 * Set a general annotation.
	 * 
	 * @param key
	 * @param value
	 */
	void setGeneralAnnotation(@NonNull String key,@NonNull  Object value);
}
