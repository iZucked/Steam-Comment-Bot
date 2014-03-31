/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Implementation of {@link IAnnotatedSolution}.
 * 
 * @author Simon Goodall
 * 
 */
public final class AnnotatedSolution implements IAnnotatedSolution {

	private ISequences sequences;

	private IOptimisationContext context;

	private IElementAnnotationsMap elementAnnotations = new HashMapAnnotations();

	private Map<String, Object> generalAnnotations = new HashMap<String, Object>();

	@Override
	public ISequences getSequences() {
		return sequences;
	}

	public void setSequences(final ISequences sequences) {
		this.sequences = sequences;
	}

	@Override
	public IOptimisationContext getContext() {
		return context;
	}

	public void setContext(final IOptimisationContext context) {
		this.context = context;
	}

	@Override
	public void dispose() {
		context = null;
		sequences = null;
		elementAnnotations = null;
		generalAnnotations = null;
	}

	@Override
	public IElementAnnotationsMap getElementAnnotations() {
		return elementAnnotations;
	}

	@Override
	public void setGeneralAnnotation(final String key, final Object value) {
		if (generalAnnotations == null) {
			throw new RuntimeException("Attempted to set an annotation after dispose()");
		}
		generalAnnotations.put(key, value);
	}

	@Override
	public <U> U getGeneralAnnotation(final String key, final Class<U> clz) {
		if (generalAnnotations == null) {
			return null;
		}
		return clz.cast(generalAnnotations.get(key));
	}
}