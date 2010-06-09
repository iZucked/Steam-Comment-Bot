package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class LinearFitnessCombinerTest {

	@Test
	public void testGetSetFitnessComponentWeights() {

		LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		Assert.assertNull(combiner.getFitnessComponentWeights());
		Map<String, Double> weights = Collections.emptyMap();
		combiner.setFitnessComponentWeights(weights);
		Assert.assertSame(weights, combiner.getFitnessComponentWeights());
	}

	@Test
	public void testCalculateFitness() {
		fail("Not yet implemented");
	}

}
