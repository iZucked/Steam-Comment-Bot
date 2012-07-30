/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

@RunWith(JMock.class)
public class LocalSearchOptimiserTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testInit() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker> constraintCheckers = Collections.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator fitnessEvaluator = context.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator sequenceManipulator = context.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor monitor = context.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker> constraintCheckers = Collections.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator fitnessEvaluator = context.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator sequenceManipulator = context.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor monitor = context.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test
	public void testGetSetMoveGenerator() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		Assert.assertNull(lso.getMoveGenerator());
		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);
		Assert.assertSame(moveGenerator, lso.getMoveGenerator());
	}

	private static class TestLocalSearchOptimiser extends LocalSearchOptimiser {

		@Override
		public IAnnotatedSolution start(final IOptimisationContext context) {
			fail("This is not part of the test.");
			return null;
		}

		@Override
		public int step(final int percentage) {
			fail("This is not part of the test.");
			return 0;
		}
	}
}
