/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

public class LinearFitnessCombinerTest {

	@Test
	public void testGetSetFitnessComponentWeights() {

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		Assert.assertNull(combiner.getFitnessComponentWeights());
		final Map<String, Double> weights = Collections.emptyMap();
		combiner.setFitnessComponentWeights(weights);
		Assert.assertSame(weights, combiner.getFitnessComponentWeights());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCalculateFitness() {

		final String c1Name = "c1";
		final String c2Name = "c2";

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		Assert.assertNull(combiner.getFitnessComponentWeights());

		final Map<String, Double> weights = CollectionsUtil.makeHashMap(c1Name,
				2.0, c2Name, 1.0);
		combiner.setFitnessComponentWeights(weights);
		Assert.assertSame(weights, combiner.getFitnessComponentWeights());

		final IFitnessComponent<Object> c1 = new MockFitnessComponent(c1Name,
				500);
		final IFitnessComponent<Object> c2 = new MockFitnessComponent(c2Name,
				1000);

		final long f = combiner.calculateFitness(CollectionsUtil.makeArrayList(
				c1, c2));

		Assert.assertEquals(2000l, f);
	}

	private static class MockFitnessComponent implements IFitnessComponent<Object> {

		private final String name;

		private final long fitness;

		public MockFitnessComponent(final String name, final long fitness) {
			this.name = name;
			this.fitness = fitness;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public long getFitness() {
			return fitness;
		}

		@Override
		public IFitnessCore<Object> getFitnessCore() {
			throw new UnsupportedOperationException("Unexpected invocation");
		}

	}

}
