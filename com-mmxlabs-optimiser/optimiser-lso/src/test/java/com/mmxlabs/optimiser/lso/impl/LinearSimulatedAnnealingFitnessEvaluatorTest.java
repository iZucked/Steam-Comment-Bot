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

import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;

@RunWith(JMock.class)
public class LinearSimulatedAnnealingFitnessEvaluatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetFitnessComponents() {

		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessComponents());
		List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);
		Assert.assertSame(fitnessComponents, evaluator.getFitnessComponents());
	}

	@Test
	public void testGetSetFitnessHelper() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessHelper());
		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);
		Assert.assertSame(fitnessHelper, evaluator.getFitnessHelper());
	}

	@Test
	public void testGetSetFitnessCombiner() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessCombiner());
		IFitnessCombiner fitnessCombiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(fitnessCombiner);
		Assert.assertSame(fitnessCombiner, evaluator.getFitnessCombiner());
	}

	@Test
	public void testGetSetThresholder() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getThresholder());
		IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);
		Assert.assertSame(thresholder, evaluator.getThresholder());
	}

	@Test
	public void testInit() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context
				.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		IThresholder thresholder = context.mock(IThresholder.class);
		evaluator.setThresholder(thresholder);

		IFitnessCombiner combiner = context.mock(IFitnessCombiner.class);
		evaluator.setFitnessCombiner(combiner);

		evaluator.init();

	}

	@Test
	public void testGetBestSequences() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBestFitness() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCurrentSequences() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckSequences() {
		fail("Not yet implemented");
	}

}
