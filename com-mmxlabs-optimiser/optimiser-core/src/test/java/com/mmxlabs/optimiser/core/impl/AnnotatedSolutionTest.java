/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;

@RunWith(JMock.class)
public class AnnotatedSolutionTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetSequences() {
		final ISequences sequences = context.mock(ISequences.class);
		final AnnotatedSolution solution = new AnnotatedSolution();
		Assert.assertNull(solution.getSequences());
		solution.setSequences(sequences);
		Assert.assertSame(sequences, solution.getSequences());
	}

	@Test
	public void testGetSetContext() {
		final IOptimisationContext optContext = context.mock(IOptimisationContext.class);
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
		final ISequences sequences = context.mock(ISequences.class);
		final IOptimisationContext optContext = context.mock(IOptimisationContext.class);

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
