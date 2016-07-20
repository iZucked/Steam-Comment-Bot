/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

public interface IAnnotatedSolution {

	/**
	 * Returns the {@link ISequences} associated with this solution
	 * 
	 * @return
	 */
	@NonNull
	ISequences getFullSequences();

	@NonNull
	IEvaluationState getEvaluationState();

	/**
	 * Returns the {@link IElementAnnotationsMap} associated with the sequence elements in this solution
	 * 
	 * @return
	 */
	@NonNull
	IElementAnnotationsMap getElementAnnotations();

	@Nullable
	<U> U getGeneralAnnotation(@NonNull String key, @NonNull Class<U> clz);

	/**
	 * Get all general annotation keys
	 * 
	 * @return
	 */
	List<String> getGeneralAnnotationKeys();

	/**
	 * Set a general annotation.
	 * 
	 * @param key
	 * @param value
	 */
	void setGeneralAnnotation(@NonNull String key, @NonNull Object value);
}
