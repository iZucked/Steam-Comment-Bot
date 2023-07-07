/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.impl.OptimiserTestUtil;

public class SortingFitnessCoreTest {

	private IFitnessComponent component;
	private IFitnessCore core;

	@BeforeEach
	public void initPieces() {

		final SortingFitnessFactory factory = new SortingFitnessFactory();
		core = factory.instantiate();
		final IPhaseOptimisationData data = Mockito.mock(IPhaseOptimisationData.class);
		assert data != null;
		core.init(data);

		final Collection<IFitnessComponent> fitnessComponents = core.getFitnessComponents();

		Assertions.assertEquals(1, fitnessComponents.size());
		component = fitnessComponents.iterator().next();
	}

	@Test
	public void testSortingFitnessCore1() {

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;
		final ISequences sequences = OptimiserTestUtil.makeSequences(OptimiserTestUtil.makeResource("r1"), 1, 2, 3, 4);

		core.evaluate(sequences, evaluationState);

		Assertions.assertEquals(0, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore2() {
		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;
		final ISequences sequences = OptimiserTestUtil.makeSequences(OptimiserTestUtil.makeResource("r1"), 1, 2, 4, 3);

		core.evaluate(sequences, evaluationState);

		Assertions.assertEquals(1, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore3() {

		final ISequences sequences = OptimiserTestUtil.makeSequences(OptimiserTestUtil.makeResource("r1"), 4, 3, 2, 1);
		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;
		core.evaluate(sequences, evaluationState);

		Assertions.assertEquals(3, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore4() {
		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;
		final ISequences sequences = OptimiserTestUtil.makeSequences(OptimiserTestUtil.makeResource("r1"), new int[0]);

		core.evaluate(sequences, evaluationState);

		Assertions.assertEquals(0, component.getFitness());
	}

	@Test
	public void testSortingFitnessCore5() {

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;

		final IResource r1 = OptimiserTestUtil.makeResource("r1");
		final IResource r2 = OptimiserTestUtil.makeResource("r2");

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(r1, OptimiserTestUtil.makeSequence(1, 3, 2, 4), r2, OptimiserTestUtil.makeSequence(5, 8, 7, 6));

		final IModifiableSequences sequences = new ModifiableSequences(CollectionsUtil.makeArrayList(r1, r2), map, new SequencesAttributesProviderImpl());

		core.evaluate(sequences, evaluationState);

		Assertions.assertEquals(3, component.getFitness());
	}

}
