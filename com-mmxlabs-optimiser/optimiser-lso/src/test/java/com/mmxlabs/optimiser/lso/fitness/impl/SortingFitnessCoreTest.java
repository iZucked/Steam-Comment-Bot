package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.impl.OptimiserTestUtil;

public class SortingFitnessCoreTest {

	private IFitnessComponent<Integer> component;
	private IFitnessCore<Integer> core;

	@Before
	public void initPieces() {

		final SortingFitnessFactory factory = new SortingFitnessFactory();
		core = factory.instantiate();
		core.init(null);
		final Collection<IFitnessComponent<Integer>> fitnessComponents = core
				.getFitnessComponents();

		Assert.assertEquals(1, fitnessComponents.size());
		component = fitnessComponents.iterator().next();
	}

	@Test
	public void testSortingFitnessCore1() {
		final ISequences<Integer> sequences = OptimiserTestUtil.makeSequences(
				OptimiserTestUtil.makeResource(), 1, 2, 3, 4);

		core.evaluate(sequences);

		Assert.assertEquals(0, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore2() {

		final ISequences<Integer> sequences = OptimiserTestUtil.makeSequences(
				OptimiserTestUtil.makeResource(), 1, 2, 4, 3);

		core.evaluate(sequences);

		Assert.assertEquals(1, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore3() {

		final ISequences<Integer> sequences = OptimiserTestUtil.makeSequences(
				OptimiserTestUtil.makeResource(), 4, 3, 2, 1);

		core.evaluate(sequences);

		Assert.assertEquals(3, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore4() {

		final ISequences<Integer> sequences = OptimiserTestUtil.makeSequences(
				OptimiserTestUtil.makeResource(), new Integer[0]);

		core.evaluate(sequences);

		Assert.assertEquals(0, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore5() {

		final IResource r1 = OptimiserTestUtil.makeResource();
		final IResource r2 = OptimiserTestUtil.makeResource();

		final Map<IResource, IModifiableSequence<Integer>> map = CollectionsUtil
				.makeHashMap(r1, OptimiserTestUtil.makeSequence(1, 3, 2, 4),
						r2, OptimiserTestUtil.makeSequence(5, 8, 7, 6));

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				CollectionsUtil.makeArrayList(r1, r2), map);

		core.evaluate(sequences);

		Assert.assertEquals(3, component.getFitness());
	}

}
