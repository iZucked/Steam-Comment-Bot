/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;

@RunWith(JMock.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AnnotatedSolutionTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetAnnotatedSequence() {

		final IResource resource = context.mock(IResource.class);
		final IAnnotatedSequence sequence = context
				.mock(IAnnotatedSequence.class);

		final AnnotatedSolution solution = new AnnotatedSolution();
		Assert.assertNull(solution.getAnnotatedSequence(resource));
		solution.setAnnotatedSequence(resource, sequence);
		Assert.assertSame(sequence, solution.getAnnotatedSequence(resource));
	}

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
	public void testDispose() {
		final ISequences sequences = context.mock(ISequences.class);
		final IResource resource = context.mock(IResource.class);
		final IAnnotatedSequence sequence = context
				.mock(IAnnotatedSequence.class);
		final IOptimisationContext optContext = context
				.mock(IOptimisationContext.class);

		final AnnotatedSolution solution = new AnnotatedSolution();

		Assert.assertNull(solution.getSequences());
		Assert.assertNull(solution.getContext());
		Assert.assertNull(solution.getAnnotatedSequence(resource));

		solution.setContext(optContext);
		solution.setSequences(sequences);
		solution.setAnnotatedSequence(resource, sequence);

		Assert.assertSame(optContext, solution.getContext());
		Assert.assertSame(sequences, solution.getSequences());
		Assert.assertSame(sequence, solution.getAnnotatedSequence(resource));

		solution.dispose();

		Assert.assertNull(solution.getSequences());
		Assert.assertNull(solution.getContext());
		Assert.assertNull(solution.getAnnotatedSequence(resource));

	}

}
