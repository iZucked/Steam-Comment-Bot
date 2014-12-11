/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
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
		Assert.assertTrue(evaluator.getFitnessComponents().isEmpty());
		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;
		evaluator.setFitnessComponents(fitnessComponents);
		Assert.assertSame(fitnessComponents, evaluator.getFitnessComponents());
	}

	@Test
	public void testInit() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;
		evaluator.setFitnessComponents(fitnessComponents);

		evaluator.init();

	}

//	@Test(expected = IllegalStateException.class)
//	public void testInit2() {
//		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
//
//		evaluator.init();
//	}

	@Test
	public void testCheckSequences() {

		final List<IResource> resources = Collections.emptyList();
		final ISequences source = new Sequences(resources);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		final IFitnessCombiner fitnessCombiner = Mockito.mock(IFitnessCombiner.class);
		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;
		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents)).thenReturn(true);
		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(1000l);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = createFitnessEvaluator(fitnessCombiner, thresholder, fitnessHelper);
		evaluator.setFitnessComponents(fitnessComponents);
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

		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(500l);
		Mockito.when(thresholder.accept(Matchers.anyLong())).thenReturn(true);

		final List<IResource> affectedResources = Collections.emptyList();
		assert affectedResources != null;

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(500, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertNotSame(best, evaluator.getBestSequences());
		current = evaluator.getCurrentSequences();
		best = evaluator.getBestSequences();

		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(700l);
		Mockito.when(thresholder.accept(-200l)).thenReturn(true);

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertSame(best, evaluator.getBestSequences());

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents, affectedResources)).thenReturn(true);
		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(600l);
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

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		final IFitnessCombiner fitnessCombiner = Mockito.mock(IFitnessCombiner.class);
		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, fitnessComponents)).thenReturn(true);
		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(1000l);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = createFitnessEvaluator(fitnessCombiner, thresholder, fitnessHelper);
		evaluator.setFitnessComponents(fitnessComponents);

		evaluator.init();

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

	private LinearSimulatedAnnealingFitnessEvaluator createFitnessEvaluator(final IFitnessCombiner fitnessCombiner, final IThresholder thresholder, final IFitnessHelper fitnessHelper) {
		return Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IFitnessCombiner.class).toInstance(fitnessCombiner);
				bind(IThresholder.class).toInstance(thresholder);
				bind(IFitnessHelper.class).toInstance(fitnessHelper);

			}
		}).getInstance(LinearSimulatedAnnealingFitnessEvaluator.class);
	}
}
