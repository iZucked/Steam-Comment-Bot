/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

/**
 * Implementation of {@link IAnnotatedSolution}.
 * 
 * @author Simon Goodall
 * 
 */
public final class AnnotatedSolution implements IAnnotatedSolution {

	@NonNull
	private final ISequences fullSequences;

	private final IEvaluationState evaluationState;

	@NonNull
	private final IElementAnnotationsMap elementAnnotations = new HashMapAnnotations();

	@NonNull
	private final Map<String, Object> generalAnnotations = new HashMap<String, Object>();

	public AnnotatedSolution(@NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState) {
		this.fullSequences = fullSequences;
		this.evaluationState = evaluationState;
	}

	@Override
	@NonNull
	public ISequences getFullSequences() {
		return fullSequences;
	}

	@Override
	@NonNull
	public IEvaluationState getEvaluationState() {
		return evaluationState;
	}

	@Override
	@NonNull
	public IElementAnnotationsMap getElementAnnotations() {
		return elementAnnotations;
	}

	@Override
	public void setGeneralAnnotation(@NonNull final String key, @NonNull final Object value) {
		generalAnnotations.put(key, value);
	}

	@Override
	public <U> U getGeneralAnnotation(final String key, final Class<U> clz) {
		return clz.cast(generalAnnotations.get(key));
	}

	@Override
	public List<String> getGeneralAnnotationKeys() {
		final List<String> keys = new LinkedList<String>(generalAnnotations.keySet());
		Collections.sort(keys);
		return keys;
	}
}