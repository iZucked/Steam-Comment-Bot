/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;

public class LinearSimulatedAnnealingFitnessEvaluatorTest {

	@Test
	public void testGetSetFitnessComponents() {

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;

		final List<IEvaluationProcess> evaluationProcesses = Collections.emptyList();
		assert evaluationProcesses != null;

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		assert thresholder != null;

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator(thresholder, fitnessComponents, evaluationProcesses);
		Assert.assertTrue(evaluator.getFitnessComponents().isEmpty());
		Assert.assertTrue(evaluator.getEvaluationProcesses().isEmpty());
	}

	@Test
	public void testCheckSequences() {

		final List<IResource> resources = Collections.emptyList();
		final ISequences source = new Sequences(resources);

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		final IFitnessCombiner fitnessCombiner = Mockito.mock(IFitnessCombiner.class);
		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;

		final List<IEvaluationProcess> evaluationProcesses = Collections.emptyList();
		assert evaluationProcesses != null;

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, evaluationState, fitnessComponents)).thenReturn(true);
		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(1000l);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = createFitnessEvaluatorInstance(fitnessCombiner, thresholder, fitnessHelper, fitnessComponents, evaluationProcesses);

		Assert.assertEquals(Long.MAX_VALUE, evaluator.getBestFitness());
		Assert.assertEquals(Long.MAX_VALUE, evaluator.getCurrentFitness());

		evaluator.setInitialSequences(source, source, evaluationState);

		Assert.assertEquals(1000, evaluator.getBestFitness());
		Assert.assertEquals(1000, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());
		Triple<ISequences, ISequences, IEvaluationState> current = evaluator.getCurrentSequences();
		Triple<ISequences, ISequences, IEvaluationState> best = evaluator.getBestSequences();

		Assert.assertNotNull(current);
		Assert.assertNotNull(best);
		Assert.assertNotNull(current.getFirst());
		Assert.assertNotNull(best.getFirst());

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, evaluationState, fitnessComponents)).thenReturn(true);
		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, evaluationState, fitnessComponents, resources)).thenReturn(true);

		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(500l);
		Mockito.when(thresholder.accept(Matchers.anyLong())).thenReturn(true);

		final List<IResource> affectedResources = Collections.emptyList();
		assert affectedResources != null;

		evaluator.evaluateSequences(source, source, evaluationState, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(500, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertNotSame(best, evaluator.getBestSequences());
		current = evaluator.getCurrentSequences();
		best = evaluator.getBestSequences();

		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(700l);
		Mockito.when(thresholder.accept(-200l)).thenReturn(true);

		evaluator.evaluateSequences(source, source, evaluationState, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertSame(best, evaluator.getBestSequences());

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, evaluationState, fitnessComponents, affectedResources)).thenReturn(true);
		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(600l);
		Mockito.when(thresholder.accept(-100l)).thenReturn(false);

		evaluator.evaluateSequences(source, source, evaluationState, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertEquals(current, evaluator.getCurrentSequences());
		Assert.assertEquals(best, evaluator.getBestSequences());
	}

	@Test
	public void testSetInitialSequences() {

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;

		final List<IResource> resources = Collections.emptyList();
		final ISequences source = new Sequences(resources);

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		assert fitnessComponents != null;

		final List<IEvaluationProcess> evaluationProcesses = Collections.emptyList();
		assert evaluationProcesses != null;

		final IThresholder thresholder = Mockito.mock(IThresholder.class);
		final IFitnessCombiner fitnessCombiner = Mockito.mock(IFitnessCombiner.class);
		final IFitnessHelper fitnessHelper = Mockito.mock(IFitnessHelper.class);

		Mockito.when(fitnessHelper.evaluateSequencesFromComponents(source, evaluationState, fitnessComponents)).thenReturn(true);
		Mockito.when(fitnessCombiner.calculateFitness(fitnessComponents)).thenReturn(1000l);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = createFitnessEvaluatorInstance(fitnessCombiner, thresholder, fitnessHelper, fitnessComponents, evaluationProcesses);

		Assert.assertEquals(Long.MAX_VALUE, evaluator.getBestFitness());
		Assert.assertEquals(Long.MAX_VALUE, evaluator.getCurrentFitness());

		evaluator.setInitialSequences(source, source, evaluationState);

		Assert.assertEquals(1000, evaluator.getBestFitness());
		Assert.assertEquals(1000, evaluator.getCurrentFitness());

		// These should be copies of the input, but hard to test...
		// TODO: Use/Implement .equals?
		// Assert.assertSame(source, evaluator.getCurrentSequences());
		// Assert.assertSame(source, evaluator.getBestSequences());
	}

	private static LinearSimulatedAnnealingFitnessEvaluator createFitnessEvaluatorInstance(@NonNull final IFitnessCombiner fitnessCombiner, @NonNull final IThresholder thresholder,
			@NonNull final IFitnessHelper fitnessHelper, @NonNull final List<IFitnessComponent> fitnessComponents, @NonNull final List<IEvaluationProcess> evaluationProcesses) {
		return Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IFitnessCombiner.class).toInstance(fitnessCombiner);
				bind(IThresholder.class).toInstance(thresholder);
				bind(IFitnessHelper.class).toInstance(fitnessHelper);
				bind(IOptimisationData.class).toInstance(Mockito.mock(IOptimisationData.class));
			}

			@Provides
			LinearSimulatedAnnealingFitnessEvaluator providerFitnessEvaluator(@NonNull final Injector injector) {
				final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator(thresholder, fitnessComponents, evaluationProcesses);
				injector.injectMembers(evaluator);
				return evaluator;
			}

		}).getInstance(LinearSimulatedAnnealingFitnessEvaluator.class);
	}
}
