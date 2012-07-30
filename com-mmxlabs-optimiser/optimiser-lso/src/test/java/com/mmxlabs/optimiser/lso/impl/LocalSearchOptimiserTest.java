/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.impl.UnmodifiableSequencesWrapper;
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

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

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
	public void testInit4() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

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
	public void testInit5() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker> constraintCheckers = Collections.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final ISequencesManipulator sequenceManipulator = context.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor monitor = context.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit6() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker> constraintCheckers = Collections.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator fitnessEvaluator = context.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final IOptimiserProgressMonitor monitor = context.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit7() {
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

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit8() {
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

		lso.init();
	}

	@Test
	public void testUpdateSequences() {
		final IIndexingContext index = new SimpleIndexingContext();
		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		final IModifiableSequence seq1 = OptimiserTestUtil.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence seq2 = OptimiserTestUtil.makeSequence(6, 7, 8, 9, 10);
		final IModifiableSequence seq3 = OptimiserTestUtil.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence seq4 = OptimiserTestUtil.makeSequence(10, 9, 8, 7, 6);

		final List<IResource> resList = CollectionsUtil.makeArrayList(r1, r2);
		final Map<IResource, IModifiableSequence> seqMap1 = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Map<IResource, IModifiableSequence> seqMap2 = CollectionsUtil.makeHashMap(r1, seq3, r2, seq4);

		final ModifiableSequences source = new ModifiableSequences(resList, seqMap1);
		final ModifiableSequences destination = new ModifiableSequences(resList, seqMap2);

		final List<IResource> affectedResources = CollectionsUtil.makeArrayList(r2);

		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		// Wrap source in unmodifiable wrapper to make sure we do not change it!
		lso.callUpdateSequences(new UnmodifiableSequencesWrapper(source), destination, affectedResources);

		// Check source and dest sequences contain the same values
		for (int i = 0; i < 5; ++i) {
			Assert.assertEquals(seq1.get(i), seq3.get(i));
			Assert.assertEquals(seq2.get(i), seq4.get(i));
		}

		Assert.assertEquals(6, seq4.get(0).getIndex());
		Assert.assertEquals(7, seq4.get(1).getIndex());
		Assert.assertEquals(8, seq4.get(2).getIndex());
		Assert.assertEquals(9, seq4.get(3).getIndex());
		Assert.assertEquals(10, seq4.get(4).getIndex());

	}

	@Test
	public void testUpdateSequences2() {
		final IIndexingContext index = new SimpleIndexingContext();
		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		final ISequence seq1 = context.mock(ISequence.class, "seq1");
		final ISequence seq2 = context.mock(ISequence.class, "seq2");
		final IModifiableSequence seq3 = context.mock(IModifiableSequence.class, "seq3");
		final IModifiableSequence seq4 = context.mock(IModifiableSequence.class, "seq4");

		final List<IResource> resList = CollectionsUtil.makeArrayList(r1, r2);
		final Map<IResource, ISequence> seqMap1 = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Map<IResource, IModifiableSequence> seqMap2 = CollectionsUtil.makeHashMap(r1, seq3, r2, seq4);

		final Sequences source = new Sequences(resList, seqMap1);
		final ModifiableSequences destination = new ModifiableSequences(resList, seqMap2);

		final List<IResource> affectedResources = CollectionsUtil.makeArrayList(r2);

		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();

		context.checking(new Expectations() {
			{
				one(seq4).replaceAll(seq2);

			}
		});

		lso.callUpdateSequences(source, destination, affectedResources);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetSetMoveGenerator() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		Assert.assertNull(lso.getMoveGenerator());
		final IMoveGenerator moveGenerator = context.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);
		Assert.assertSame(moveGenerator, lso.getMoveGenerator());
	}

	@Test
	public void testGetSetNumberOfIterations() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		Assert.assertEquals(0, lso.getNumberOfIterations());
		lso.setNumberOfIterations(100);
		Assert.assertEquals(100, lso.getNumberOfIterations());
	}

	@Test
	public void testGetSetConstraintCheckers() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		final List<IConstraintChecker> checkers = Collections.emptyList();
		Assert.assertNull(lso.getConstraintCheckers());
		lso.setConstraintCheckers(checkers);
		Assert.assertSame(checkers, lso.getConstraintCheckers());

	}

	@Test
	public void testGetSetFitnessEvaluator() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		Assert.assertNull(lso.getFitnessEvaluator());
		final IFitnessEvaluator fitnessEvaluator = context.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);
		Assert.assertSame(fitnessEvaluator, lso.getFitnessEvaluator());
	}

	@Test
	public void testSetSequenceManipulator() {
		final TestLocalSearchOptimiser lso = new TestLocalSearchOptimiser();
		Assert.assertNull(lso.getSequenceManipulator());
		final ISequencesManipulator sequencesManipulator = context.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequencesManipulator);
		Assert.assertSame(sequencesManipulator, lso.getSequenceManipulator());

	}

	private static class TestLocalSearchOptimiser extends LocalSearchOptimiser {

		/**
		 * Make public rather than protected for the tests
		 */
		public void callUpdateSequences(final ISequences source, final IModifiableSequences destination, final Collection<IResource> affectedResources) {
			super.updateSequences(source, destination, affectedResources);
		}

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
