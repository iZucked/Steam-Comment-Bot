/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;

public class LinearSimulatedAnnealingFitnessEvaluatorTest {

	@Test
	public void testGetSetFitnessComponents() {

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		Assert.assertNull(evaluator.getFitnessComponents());
		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);
		Assert.assertSame(fitnessComponents, evaluator.getFitnessComponents());
	}

	@Test
	public void testGetSetFitnessHelper() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		Assert.assertNull(evaluator.getFitnessHelper());
		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);
		Assert.assertSame(fitnessHelper, evaluator.getFitnessHelper());
	}

	@Test
	public void testGetSetFitnessCombiner() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		Assert.assertNull(evaluator.getFitnessCombiner());
		final IFitnessCombiner fitnessCombiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(fitnessCombiner);
		Assert.assertSame(fitnessCombiner, evaluator.getFitnessCombiner());
	}

	@Test
	public void testGetSetThresholder() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		Assert.assertNull(evaluator.getThresholder());
		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);
		Assert.assertSame(thresholder, evaluator.getThresholder());
	}

	@Test
	public void testInit() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IFitnessCombiner combiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		evaluator.init();

	}

	@Test
	public void testCheckSequences() {

		final List<IResource> resources = Collections.emptyList();
		final ISequences source = new Sequences(resources);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents)).thenReturn(true);
		Mockito.when(combiner.calculateFitness(fitnessComponents)).thenReturn(1000l);

		evaluator.init();

		Assert.assertEquals(Long.MAX_VALUE, evaluator.getBestFitness());
		Assert.assertEquals(Long.MAX_VALUE, evaluator.getCurrentFitness());

		evaluator.setInitialSequences(source);

		Assert.assertEquals(1000, evaluator.getBestFitness());
		Assert.assertEquals(1000, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());
		ISequences current = evaluator.getCurrentSequences();
		ISequences best = evaluator.getBestSequences();

		Assert.assertNotNull(current);
		Assert.assertNotNull(best);

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents)).thenReturn(true);
		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents, resources)).thenReturn(true);

		Mockito.when(combiner.calculateFitness(fitnessComponents)).thenReturn(500l);
		Mockito.when(thresholder.accept(Matchers.anyLong())).thenReturn(true);

		final List<IResource> affectedResources = Collections.emptyList();

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(500, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertNotSame(best, evaluator.getBestSequences());
		current = evaluator.getCurrentSequences();
		best = evaluator.getBestSequences();

		Mockito.when(combiner.calculateFitness(fitnessComponents)).thenReturn(700l);
		Mockito.when(thresholder.accept(-200l)).thenReturn(true);

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertSame(best, evaluator.getBestSequences());

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents, affectedResources)).thenReturn(true);
		Mockito.when(combiner.calculateFitness(fitnessComponents)).thenReturn(600l);
		Mockito.when(thresholder.accept(-100l)).thenReturn(false);

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertEquals(current, evaluator.getCurrentSequences());
		Assert.assertEquals(best, evaluator.getBestSequences());
	}

	@Test
	public void testSetInitialSequences() {

		final List<IResource> resources = Collections.emptyList();
		final ISequences source = new Sequences(resources);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = Mockito.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents)).thenReturn(true);
		Mockito.when(combiner.calculateFitness(fitnessComponents)).thenReturn(1000l);

		Assert.assertEquals(Long.MAX_VALUE, evaluator.getBestFitness());
		Assert.assertEquals(Long.MAX_VALUE, evaluator.getCurrentFitness());

		evaluator.setInitialSequences(source);

		Assert.assertEquals(1000, evaluator.getBestFitness());
		Assert.assertEquals(1000, evaluator.getCurrentFitness());

		// These should be copies of the input, but hard to test...
		// TODO: Use/Implement .equals?
		// Assert.assertSame(source, evaluator.getCurrentSequences());
		// Assert.assertSame(source, evaluator.getBestSequences());
	}

}
