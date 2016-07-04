/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

@SuppressWarnings("null")
public class LocalSearchOptimiserTest {

	@Test
	public void testInit() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		final IMoveGenerator moveGenerator = Mockito.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker> constraintCheckers = Collections.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final List<IEvaluatedStateConstraintChecker> evalConstraintCheckers = Collections.emptyList();
		lso.setEvaluatedStateConstraintCheckers(evalConstraintCheckers);

		final IFitnessEvaluator fitnessEvaluator = Mockito.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator sequenceManipulator = Mockito.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor monitor = Mockito.mock(IOptimiserProgressMonitor.class);
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

		final List<IEvaluatedStateConstraintChecker> evalConstraintCheckers = Collections.emptyList();
		lso.setEvaluatedStateConstraintCheckers(evalConstraintCheckers);

		final IFitnessEvaluator fitnessEvaluator = Mockito.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator sequenceManipulator = Mockito.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor monitor = Mockito.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test
	public void testGetSetMoveGenerator() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		Assert.assertNull(lso.getMoveGenerator());
		final IMoveGenerator moveGenerator = Mockito.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);
		Assert.assertSame(moveGenerator, lso.getMoveGenerator());
	}

	private static class TestLocalSearchOptimiser extends LocalSearchOptimiser {

		@Override
		@Nullable
		public IAnnotatedSolution start(@NonNull final IOptimisationContext context, @NonNull ISequences initialRawSequences, @NonNull ISequences inputRawSequences) {
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
