/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;

public class AnnotatedSolutionTest {

	@Test
	public void testGetSetSequences() {
		final ISequences sequences = Mockito.mock(ISequences.class);
		final AnnotatedSolution solution = new AnnotatedSolution();
		Assert.assertNull(solution.getSequences());
		solution.setSequences(sequences);
		Assert.assertSame(sequences, solution.getSequences());
	}

	@Test
	public void testGetSetContext() {
		final IOptimisationContext optContext = Mockito.mock(IOptimisationContext.class);
		final AnnotatedSolution solution = new AnnotatedSolution();
		Assert.assertNull(solution.getContext());
		solution.setContext(optContext);
		Assert.assertSame(optContext, solution.getContext());
	}

	@Test
	public void getSetGeneralAnnotaion() {
		final Object annotation = new Object();
		final String key = "key";

		final AnnotatedSolution solution = new AnnotatedSolution();

		solution.setGeneralAnnotation(key, annotation);

		Assert.assertSame(annotation, solution.getGeneralAnnotation(key, Object.class));
	}

	@Test
	public void getElementAnnotations() {
		final AnnotatedSolution solution = new AnnotatedSolution();

		final IElementAnnotationsMap elementAnnotations = solution.getElementAnnotations();

		Assert.assertNotNull(elementAnnotations);
	}

	@Test
	public void testDispose() {
		final ISequences sequences = Mockito.mock(ISequences.class);
		final IOptimisationContext optContext = Mockito.mock(IOptimisationContext.class);

		final Object annotation = new Object();
		final String key = "key";

		final AnnotatedSolution solution = new AnnotatedSolution();

		Assert.assertNull(solution.getSequences());
		Assert.assertNull(solution.getContext());

		solution.setContext(optContext);
		solution.setSequences(sequences);

		Assert.assertSame(optContext, solution.getContext());
		Assert.assertSame(sequences, solution.getSequences());

		solution.setGeneralAnnotation(key, annotation);

		solution.dispose();

		Assert.assertNull(solution.getSequences());
		Assert.assertNull(solution.getContext());

		Assert.assertNull(solution.getGeneralAnnotation(key, Object.class));
		Assert.assertNull(solution.getElementAnnotations());
	}

}
