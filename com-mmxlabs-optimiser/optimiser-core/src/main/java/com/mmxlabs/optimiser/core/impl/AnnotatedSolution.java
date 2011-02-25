/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Implementation of {@link IAnnotatedSolution}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class AnnotatedSolution<T> implements IAnnotatedSolution<T> {

	private ISequences<T> sequences;

	private IOptimisationContext<T> context;

	private IAnnotations<T> elementAnnotations = new HashMapAnnotations<T>();
	
	private Map<String, Object> generalAnnotations = new HashMap<String, Object>();
	
	@Override
	public ISequences<T> getSequences() {
		return sequences;
	}

	public void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;
	}

	@Override
	public IOptimisationContext<T> getContext() {
		return context;
	}

	public void setContext(final IOptimisationContext<T> context) {
		this.context = context;
	}

	@Override
	public void dispose() {
		context = null;
		sequences = null;
		elementAnnotations = null;
	}

	@Override
	public IAnnotations<T> getElementAnnotations() {
		return elementAnnotations;
	}
	
	@Override
	public void setGeneralAnnotation(final String key, final Object value) {
		generalAnnotations.put(key, value);
	}


	@Override
	public <U> U getGeneralAnnotation(String key, Class<U> clz) {
		return clz.cast(generalAnnotations.get(key));
	}
}