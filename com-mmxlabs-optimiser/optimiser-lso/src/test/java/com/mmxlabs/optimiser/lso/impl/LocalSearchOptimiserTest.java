/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
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
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class LocalSearchOptimiserTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testInit() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit6() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit7() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		lso.setReportInterval(100);

		lso.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit8() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();

		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);

		lso.setNumberOfIterations(1);

		final List<IConstraintChecker<Object>> constraintCheckers = Collections
				.emptyList();
		lso.setConstraintCheckers(constraintCheckers);

		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);

		final ISequencesManipulator<Object> sequenceManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequenceManipulator);

		final IOptimiserProgressMonitor<Object> monitor = context
				.mock(IOptimiserProgressMonitor.class);
		lso.setProgressMonitor(monitor);

		lso.init();
	}

	@Test
	public void testUpdateSequences() {
		IIndexingContext index = new SimpleIndexingContext();
		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		final IModifiableSequence<Integer> seq1 = OptimiserTestUtil
				.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence<Integer> seq2 = OptimiserTestUtil
				.makeSequence(6, 7, 8, 9, 10);
		final IModifiableSequence<Integer> seq3 = OptimiserTestUtil
				.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence<Integer> seq4 = OptimiserTestUtil
				.makeSequence(10, 9, 8, 7, 6);

		final List<IResource> resList = CollectionsUtil.makeArrayList(r1, r2);
		final Map<IResource, IModifiableSequence<Integer>> seqMap1 = CollectionsUtil
				.makeHashMap(r1, seq1, r2, seq2);
		final Map<IResource, IModifiableSequence<Integer>> seqMap2 = CollectionsUtil
				.makeHashMap(r1, seq3, r2, seq4);

		final ModifiableSequences<Integer> source = new ModifiableSequences<Integer>(
				resList, seqMap1);
		final ModifiableSequences<Integer> destination = new ModifiableSequences<Integer>(
				resList, seqMap2);

		final List<IResource> affectedResources = CollectionsUtil
				.makeArrayList(r2);

		final TestLocalSearchOptimiser<Integer> lso = new TestLocalSearchOptimiser<Integer>();

		// Wrap source in unmodifiable wrapper to make sure we do not change it!
		lso.updateSequences(new UnmodifiableSequencesWrapper<Integer>(source),
				destination, affectedResources);

		// Check source and dest sequences contain the same values
		for (int i = 0; i < 5; ++i) {
			Assert.assertEquals(seq1.get(i), seq3.get(i));
			Assert.assertEquals(seq2.get(i), seq4.get(i));
		}

		Assert.assertEquals(Integer.valueOf(6), seq4.get(0));
		Assert.assertEquals(Integer.valueOf(7), seq4.get(1));
		Assert.assertEquals(Integer.valueOf(8), seq4.get(2));
		Assert.assertEquals(Integer.valueOf(9), seq4.get(3));
		Assert.assertEquals(Integer.valueOf(10), seq4.get(4));

	}

	@Test
	public void testUpdateSequences2() {
		IIndexingContext index = new SimpleIndexingContext();
		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		final ISequence<Integer> seq1 = context.mock(ISequence.class, "seq1");
		final ISequence<Integer> seq2 = context.mock(ISequence.class, "seq2");
		final IModifiableSequence<Integer> seq3 = context.mock(
				IModifiableSequence.class, "seq3");
		final IModifiableSequence<Integer> seq4 = context.mock(
				IModifiableSequence.class, "seq4");

		final List<IResource> resList = CollectionsUtil.makeArrayList(r1, r2);
		final Map<IResource, ISequence<Integer>> seqMap1 = CollectionsUtil
				.makeHashMap(r1, seq1, r2, seq2);
		final Map<IResource, IModifiableSequence<Integer>> seqMap2 = CollectionsUtil
				.makeHashMap(r1, seq3, r2, seq4);

		final Sequences<Integer> source = new Sequences<Integer>(resList,
				seqMap1);
		final ModifiableSequences<Integer> destination = new ModifiableSequences<Integer>(
				resList, seqMap2);

		final List<IResource> affectedResources = CollectionsUtil
				.makeArrayList(r2);

		final TestLocalSearchOptimiser<Integer> lso = new TestLocalSearchOptimiser<Integer>();

		context.checking(new Expectations() {
			{
				one(seq4).replaceAll(seq2);

			}
		});

		lso.updateSequences(source, destination, affectedResources);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetSetMoveGenerator() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();
		Assert.assertNull(lso.getMoveGenerator());
		final IMoveGenerator<Object> moveGenerator = context
				.mock(IMoveGenerator.class);
		lso.setMoveGenerator(moveGenerator);
		Assert.assertSame(moveGenerator, lso.getMoveGenerator());
	}

	@Test
	public void testGetSetNumberOfIterations() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();
		Assert.assertEquals(0, lso.getNumberOfIterations());
		lso.setNumberOfIterations(100);
		Assert.assertEquals(100, lso.getNumberOfIterations());
	}

	@Test
	public void testGetSetConstraintCheckers() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();
		final List<IConstraintChecker<Object>> checkers = Collections
				.emptyList();
		Assert.assertNull(lso.getConstraintCheckers());
		lso.setConstraintCheckers(checkers);
		Assert.assertSame(checkers, lso.getConstraintCheckers());

	}

	@Test
	public void testGetSetFitnessEvaluator() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();
		Assert.assertNull(lso.getFitnessEvaluator());
		final IFitnessEvaluator<Object> fitnessEvaluator = context
				.mock(IFitnessEvaluator.class);
		lso.setFitnessEvaluator(fitnessEvaluator);
		Assert.assertSame(fitnessEvaluator, lso.getFitnessEvaluator());
	}

	@Test
	public void testSetSequenceManipulator() {
		final TestLocalSearchOptimiser<Object> lso = new TestLocalSearchOptimiser<Object>();
		Assert.assertNull(lso.getSequenceManipulator());
		final ISequencesManipulator<Object> sequencesManipulator = context
				.mock(ISequencesManipulator.class);
		lso.setSequenceManipulator(sequencesManipulator);
		Assert.assertSame(sequencesManipulator, lso.getSequenceManipulator());

	}

	private class TestLocalSearchOptimiser<T> extends LocalSearchOptimiser<T> {

		/**
		 * Make public rather than protected for the tests
		 */
		@Override
		public void updateSequences(final ISequences<T> source,
				final IModifiableSequences<T> destination,
				final Collection<IResource> affectedResources) {
			super.updateSequences(source, destination, affectedResources);
		}

		/**
		 * This is an abstract method and will not be tested here
		 */
		@Override
		public void optimise(final IOptimisationContext<T> optimisationContext) {
			fail("This is not part of the test!");
		}

	}
}
