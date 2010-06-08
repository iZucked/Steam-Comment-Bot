package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;

@RunWith(JMock.class)
public class LinearSimulatedAnnealingFitnessEvaluatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetFitnessComponents() {

		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessComponents());
		List<IFitnessComponent<Object>> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);
		Assert.assertSame(fitnessComponents, evaluator.getFitnessComponents());
	}

	@Test
	public void testGetSetFitnessHelper() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessHelper());
		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);
		Assert.assertSame(fitnessHelper, evaluator.getFitnessHelper());
	}

	@Test
	public void testGetSetTemperature() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertEquals(Double.MAX_VALUE, evaluator.getTemperature(), 0.0);
		evaluator.setTemperature(10);
		Assert.assertEquals(10.0, evaluator.getTemperature(), 0.0);

	}

	@Test
	public void testGetSetFitnessComponentWeights() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessComponentWeights());
		Map<String, Double> fitnessWeights = Collections.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessWeights);
		Assert.assertSame(fitnessWeights, evaluator
				.getFitnessComponentWeights());
	}

	@Test
	public void testGetSetNumberOfIterations() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();
		Assert.assertEquals(Integer.MAX_VALUE, evaluator
				.getNumberOfIterations());
		evaluator.setNumberOfIterations(10);
		Assert.assertEquals(10, evaluator.getNumberOfIterations());

	}

	@Test
	public void testInit() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		Map<String, Double> fitnessWeights = Collections.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessWeights);

		evaluator.setNumberOfIterations(10);
		evaluator.setTemperature(10);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		Map<String, Double> fitnessWeights = Collections.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessWeights);

		evaluator.setNumberOfIterations(10);
		evaluator.setTemperature(10);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		evaluator.setNumberOfIterations(10);
		evaluator.setTemperature(10);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		Map<String, Double> fitnessWeights = Collections.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessWeights);

		evaluator.setTemperature(10);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		Map<String, Double> fitnessWeights = Collections.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessWeights);

		evaluator.setNumberOfIterations(10);

		@SuppressWarnings("unchecked")
		IFitnessHelper<Object> fitnessHelper = context.mock(IFitnessHelper.class);
		evaluator.setFitnessHelper(fitnessHelper);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit6() {
		LinearSimulatedAnnealingFitnessEvaluator<Object> evaluator = new LinearSimulatedAnnealingFitnessEvaluator<Object>();

		List<IFitnessComponent<Object>> fitnessComponents = Collections.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		Map<String, Double> fitnessWeights = Collections.emptyMap();
		evaluator.setFitnessComponentWeights(fitnessWeights);

		evaluator.setNumberOfIterations(10);
		evaluator.setTemperature(10);

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
