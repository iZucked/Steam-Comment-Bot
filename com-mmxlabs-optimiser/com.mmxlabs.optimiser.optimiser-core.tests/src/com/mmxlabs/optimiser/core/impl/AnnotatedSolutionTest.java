/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

public class AnnotatedSolutionTest {

	@Test
	public void testGetSetSequences() {
		final ISequences sequences = Mockito.mock(ISequences.class);
		final IEvaluationState state = Mockito.mock(IEvaluationState.class);
		final AnnotatedSolution solution = new AnnotatedSolution(sequences, state);
		Assertions.assertSame(sequences, solution.getFullSequences());
		Assertions.assertSame(state, solution.getEvaluationState());
	}

	@Test
	public void getSetGeneralAnnotaion() {
		final Object annotation = new Object();
		final String key = "key";

		final ISequences sequences = Mockito.mock(ISequences.class);
		final IEvaluationState state = Mockito.mock(IEvaluationState.class);
		final AnnotatedSolution solution = new AnnotatedSolution(sequences, state);

		solution.setGeneralAnnotation(key, annotation);

		Assertions.assertSame(annotation, solution.getGeneralAnnotation(key, Object.class));
	}

	@Test
	public void getElementAnnotations() {

		final ISequences sequences = Mockito.mock(ISequences.class);
		final IEvaluationState state = Mockito.mock(IEvaluationState.class);
		final AnnotatedSolution solution = new AnnotatedSolution(sequences, state);
		final IElementAnnotationsMap elementAnnotations = solution.getElementAnnotations();

		Assertions.assertNotNull(elementAnnotations);
	}
}
