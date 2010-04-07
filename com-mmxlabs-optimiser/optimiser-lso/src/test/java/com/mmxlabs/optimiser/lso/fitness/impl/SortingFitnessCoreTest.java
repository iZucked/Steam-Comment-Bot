package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;

public class SortingFitnessCoreTest {

	private IFitnessComponent<Integer> component;
	private SortingFitnessCore<Integer> core;

	@Before
	public void initPieces() {

		core = new SortingFitnessCore<Integer>();
		core.init();
		final Collection<IFitnessComponent<Integer>> fitnessComponents = core
				.getFitnessComponents();

		Assert.assertEquals(1, fitnessComponents.size());
		component = fitnessComponents.iterator().next();

	}

	@Test
	public void testSortingFitnessCore1() {
		final ISequences<Integer> sequences = makeSequences(makeResource(), 1,
				2, 3, 4);

		core.evaluate(sequences);

		Assert.assertEquals(0, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore2() {

		final ISequences<Integer> sequences = makeSequences(makeResource(), 1,
				2, 4, 3);

		core.evaluate(sequences);

		Assert.assertEquals(1, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore3() {

		final ISequences<Integer> sequences = makeSequences(makeResource(), 4,
				3, 2, 1);

		core.evaluate(sequences);

		Assert.assertEquals(3, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore4() {

		final ISequences<Integer> sequences = makeSequences(makeResource(),
				new Integer[0]);

		core.evaluate(sequences);

		Assert.assertEquals(0, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore5() {

		final IResource r1 = makeResource();
		final IResource r2 = makeResource();

		final Map<IResource, IModifiableSequence<Integer>> map = CollectionsUtil
				.makeHashMap(r1, makeSequence(1, 3, 2, 4), r2, makeSequence(5,
						8, 7, 6));

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				CollectionsUtil.makeArrayList(r1, r2), map);

		core.evaluate(sequences);

		Assert.assertEquals(3, component.getFitness());
	}

	private IModifiableSequence<Integer> makeSequence(final Integer... elements) {
		final List<Integer> elementsList = CollectionsUtil
				.makeArrayList(elements);
		return new ListModifiableSequence<Integer>(elementsList);
	}

	private IResource makeResource() {
		return new IResource() {
		};
	}

	private IModifiableSequences<Integer> makeSequences(final IResource res,
			final Integer... elements) {

		final Map<IResource, IModifiableSequence<Integer>> map = CollectionsUtil
				.makeHashMap(res, makeSequence(elements));

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				Collections.singletonList(res), map);

		return sequences;
	}
}
