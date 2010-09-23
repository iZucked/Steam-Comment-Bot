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

		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessComponents());
		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);
		Assert.assertSame(fitnessComponents, evaluator.getFitnessComponents());
	}

	@Test
	public void testGetSetFitnessHelper() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessHelper());
		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);
		Assert.assertSame(fitnessHelper, evaluator.getFitnessHelper());
	}

	@Test
	public void testGetSetFitnessCombiner() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessCombiner());
		final IFitnessCombiner fitnessCombiner = context
				.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(fitnessCombiner);
		Assert.assertSame(fitnessCombiner, evaluator.getFitnessCombiner());
	}

	@Test
	public void testGetSetThresholder() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getThresholder());
		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);
		Assert.assertSame(thresholder, evaluator.getThresholder());
	}

	@Test
	public void testInit() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
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
		final ISequences<Object> source = new Sequences<Object>(resources);

		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source,
						fitnessComponents);
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
		ISequences<Object> current = evaluator.getCurrentSequences();
		ISequences<Object> best = evaluator.getBestSequences();

		Assert.assertNotNull(current);
		Assert.assertNotNull(best);

		context.setDefaultResultForType(boolean.class, true);
		context.setDefaultResultForType(long.class, 500l);

		final List<IResource> affectedResources = Collections.emptyList();

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source,
						fitnessComponents, affectedResources);
				one(combiner).calculateFitness(fitnessComponents);

				one(thresholder).accept(-500l);

				one(fitnessHelper).acceptFromComponents(fitnessComponents,
						source, affectedResources);

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
				one(fitnessHelper).evaluateSequencesFromComponents(source,
						fitnessComponents, affectedResources);
				one(combiner).calculateFitness(fitnessComponents);

				one(thresholder).accept(200l);

				one(fitnessHelper).acceptFromComponents(fitnessComponents,
						source, affectedResources);

				one(thresholder).step();
			}
		});

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertNotSame(current, evaluator.getCurrentSequences());
		Assert.assertSame(best, evaluator.getBestSequences());
		current = evaluator.getCurrentSequences();

		Assert.fail("This test will fail as we need the thresholder to return false, but the helper to return true!"); 
		context.setDefaultResultForType(boolean.class, false);
		context.setDefaultResultForType(long.class, 600l);

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source,
						fitnessComponents, affectedResources);
				one(combiner).calculateFitness(fitnessComponents);

				one(thresholder).accept(-100l);

				one(thresholder).step();
			}
		});

		evaluator.evaluateSequences(source, affectedResources);

		Assert.assertEquals(500, evaluator.getBestFitness());
		Assert.assertEquals(700, evaluator.getCurrentFitness());

		Assert.assertNotNull(evaluator.getBestSequences());

		Assert.assertSame(current, evaluator.getCurrentSequences());
		Assert.assertSame(best, evaluator.getBestSequences());

		context.assertIsSatisfied();
	}

	@Test
	public void testSetInitialSequences() {

		final List<IResource> resources = Collections.emptyList();
		final ISequences<Object> source = new Sequences<Object>(resources);

		final LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		final List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		final IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		final IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		final IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

		context.checking(new Expectations() {
			{
				one(fitnessHelper).evaluateSequencesFromComponents(source,
						fitnessComponents);
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

