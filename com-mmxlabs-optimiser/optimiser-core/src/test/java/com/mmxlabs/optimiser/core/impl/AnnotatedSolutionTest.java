/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;

@RunWith(JMock.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
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
		final IOptimisationContext optContext = context
				.mock(IOptimisationContext.class);
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

		final Map<String, Object> generalAnnotations = solution
				.getGeneralAnnotations();

		Assert.assertNotNull(generalAnnotations);
		Assert.assertTrue(generalAnnotations.isEmpty());

		solution.setGeneralAnnotation(key, annotation);

		Assert.assertFalse(generalAnnotations.isEmpty());
		Assert.assertTrue(generalAnnotations.containsKey(key));
		Assert.assertSame(annotation, generalAnnotations.get(key));
	}

	@Test
	public void getElementAnnotations() {
		final AnnotatedSolution solution = new AnnotatedSolution();

		final IAnnotations elementAnnotations = solution.getElementAnnotations();

		Assert.assertNotNull(elementAnnotations);
		
		Assert.fail("How to test this further?");
	}

	@Test
	public void testDispose() {
		final ISequences sequences = context.mock(ISequences.class);
		final IOptimisationContext optContext = context
				.mock(IOptimisationContext.class);

		final Object annotation = new Object();
		final String key = "key";

		
		final AnnotatedSolution solution = new AnnotatedSolution();

		Assert.assertNull(solution.getSequences());
		Assert.assertNull(solution.getContext());

		solution.setContext(optContext);
		solution.setSequences(sequences);

		Assert.assertSame(optContext, solution.getContext());
		Assert.assertSame(sequences, solution.getSequences());

		final Map<String, Object> generalAnnotations = solution
		.getGeneralAnnotations();

		Assert.assertNotNull(generalAnnotations);
		Assert.assertTrue(generalAnnotations.isEmpty());

		solution.setGeneralAnnotation(key, annotation);

		Assert.assertFalse(generalAnnotations.isEmpty());
		Assert.assertTrue(generalAnnotations.containsKey(key));
		Assert.assertSame(annotation, generalAnnotations.get(key));

		solution.dispose();

		Assert.assertNull(solution.getSequences());
		Assert.assertNull(solution.getContext());
		
		Assert.assertTrue(generalAnnotations.isEmpty());
		
		Assert.fail("Unable to test disposal of ElementAnnotations");
	}

}
