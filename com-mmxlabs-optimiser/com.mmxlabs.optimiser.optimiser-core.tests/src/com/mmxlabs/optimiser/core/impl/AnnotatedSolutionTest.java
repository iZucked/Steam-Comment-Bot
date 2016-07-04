/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

@SuppressWarnings("null")
public class AnnotatedSolutionTest {

	@Test
	public void testGetSetSequences() {
		final ISequences sequences = Mockito.mock(ISequences.class);
		final IOptimisationContext optContext = Mockito.mock(IOptimisationContext.class);
		final IEvaluationState state = Mockito.mock(IEvaluationState.class);
		final AnnotatedSolution solution = new AnnotatedSolution(sequences, optContext, state);
		Assert.assertSame(sequences, solution.getFullSequences());
		Assert.assertSame(optContext, solution.getContext());
		Assert.assertSame(state, solution.getEvaluationState());
	}

	@Test
	public void getSetGeneralAnnotaion() {
		final Object annotation = new Object();
		final String key = "key";

		final ISequences sequences = Mockito.mock(ISequences.class);
		final IOptimisationContext optContext = Mockito.mock(IOptimisationContext.class);
		final IEvaluationState state = Mockito.mock(IEvaluationState.class);
		final AnnotatedSolution solution = new AnnotatedSolution(sequences, optContext, state);

		solution.setGeneralAnnotation(key, annotation);

		Assert.assertSame(annotation, solution.getGeneralAnnotation(key, Object.class));
	}

	@Test
	public void getElementAnnotations() {

		final ISequences sequences = Mockito.mock(ISequences.class);
		final IOptimisationContext optContext = Mockito.mock(IOptimisationContext.class);
		final IEvaluationState state = Mockito.mock(IEvaluationState.class);
		final AnnotatedSolution solution = new AnnotatedSolution(sequences, optContext, state);
		final IElementAnnotationsMap elementAnnotations = solution.getElementAnnotations();

		Assert.assertNotNull(elementAnnotations);
	}
}
