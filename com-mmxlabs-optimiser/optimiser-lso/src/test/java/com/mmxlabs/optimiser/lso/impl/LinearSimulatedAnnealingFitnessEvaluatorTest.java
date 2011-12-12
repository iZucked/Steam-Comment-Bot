/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;

@RunWith(JMock.class)
public class LinearSimulatedAnnealingFitnessEvaluatorTest {

	Mockery context = new JUnit4Mockery();

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
		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);
		Assert.assertSame(fitnessHelper, evaluator.getFitnessHelper());
	}

	@Test
	public void testGetSetFitnessCombiner() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		Assert.assertNull(evaluator.getFitnessCombiner());
		final IFitnessCombiner fitnessCombiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(fitnessCombiner);
		Assert.assertSame(fitnessCombiner, evaluator.getFitnessCombiner());
	}

	@Test
	public void testGetSetThresholder() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		Assert.assertNull(evaluator.getThresholder());
		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);
		Assert.assertSame(thresholder, evaluator.getThresholder());
	}

	@Test
	public void testInit() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
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

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source, fitnessComponents);
				one(combiner).calculateFitness(fitnessComponents);
				one(thresholder).init();
			}
		});

		context.setDefaultResultForType(boolean.class, true);
		context.setDefaultResultForType(long.class, 1000l);

		Assert.assertEquals(Long.MAX_VALUE, evaluator.getBestFitness());
		Assert.assertEquals(Long.MAX_VALUE, evaluator.getCurrentFitness());

		evaluator.setInitialSequences(source);

		context.assertIsSatisfied();

		Assert.assertEquals(1000, evaluator.getBestFitness());
		Assert.assertEquals(1000, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());
		ISequences current = evaluator.getCurrentSequences();
		ISequences best = evaluator.getBestSequences();

		Assert.assertNotNull(current);
		Assert.assertNotNull(best);

		context.setDefaultResultForType(boolean.class, true);
		context.setDefaultResultForType(long.class, 500l);

		final List<IResource> affectedResources = Collections.emptyList();

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source, fitnessComponents, affectedResources);
				one(combiner).calculateFitness(fitnessComponents);

				one(thresholder).accept(-500l);

				one(fitnessHelper).acceptFromComponents(fitnessComponents, source, affectedResources);

				one(thresholder).step();
			}
		});

		evaluator.evaluateSequences(source, affectedResources);

		context.assertIsSatisfied();

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(500, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertNotSame(best, evaluator.getBestSequences());
		current = evaluator.getCurrentSequences();
		best = evaluator.getBestSequences();

		context.setDefaultResultForType(long.class, 700l);

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source, fitnessComponents, affectedResources);
				one(combiner).calculateFitness(fitnessComponents);

				one(thresholder).accept(200l);

				one(fitnessHelper).acceptFromComponents(fitnessComponents, source, affectedResources);

				one(thresholder).step();
			}
		});

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertSame(best, evaluator.getBestSequences());

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source, fitnessComponents, affectedResources);
				will(returnValue(true));

				one(combiner).calculateFitness(fitnessComponents);
				will(returnValue(600l));

				one(thresholder).accept(-100l);
				will(returnValue(false));

				one(thresholder).step();
			}
		});

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertEquals(current, evaluator.getCurrentSequences());
		Assert.assertEquals(best, evaluator.getBestSequences());

		context.assertIsSatisfied();
	}

	@Test
	public void testSetInitialSequences() {

		final List<IResource> resources = Collections.emptyList();
		final ISequences source = new Sequences(resources);

		final LinearSimulatedAnnealingFitnessEvaluator evaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		final List<IFitnessComponent> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		final IFitnessHelper fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source, fitnessComponents);
				one(combiner).calculateFitness(fitnessComponents);
				one(thresholder).init();
			}
		});

		context.setDefaultResultForType(long.class, 1000l);
		context.setDefaultResultForType(boolean.class, true);

		Assert.assertEquals(Long.MAX_VALUE, evaluator.getBestFitness());
		Assert.assertEquals(Long.MAX_VALUE, evaluator.getCurrentFitness());

		evaluator.setInitialSequences(source);

		Assert.assertEquals(1000, evaluator.getBestFitness());
		Assert.assertEquals(1000, evaluator.getCurrentFitness());

		// These should be copies of the input, but hard to test...
		// TODO: Use/Implement .equals?
		// Assert.assertSame(source, evaluator.getCurrentSequences());
		// Assert.assertSame(source, evaluator.getBestSequences());

		context.assertIsSatisfied();
	}

}
