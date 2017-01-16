/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.NullSequencesManipulator;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class SimilarityFitnessCoreTest {

	IIndexingContext context = new CheckingIndexingContext();

	@Test
	public void testSimilarityComponent() {
		final String name = "name";
		final SimilarityFitnessCore core = new SimilarityFitnessCore(name);

		Assert.assertSame(name, core.getName());
	}

	private SimilarityFitnessCore createSimilarityFitnessCore(final ISequences sequences) {
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		Mockito.when(portTypeProvider.getPortType(Matchers.<ISequenceElement> any())).then(new Answer<PortType>() {

			@Override
			public PortType answer(final InvocationOnMock invocation) throws Throwable {

				final ISequenceElement element = (ISequenceElement) invocation.getArguments()[0];

				if (element.getName().contains("L")) {
					return PortType.Load;
				} else if (element.getName().contains("D")) {
					return PortType.Discharge;
				}

				return PortType.Unknown;
			}
		});

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Provides
			@Singleton
			@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
			private ISequences provideInitialSequences() {
				return sequences;
			}


			@Provides
			@Singleton
			@Named(OptimiserConstants.SEQUENCE_TYPE_INPUT)
			private ISequences provideInputSequences() {

				return sequences;
			}

			@Provides
			@Singleton
			private ISimilarityComponentParameters provideSimilarityComponentParameters() {

				final SimilarityComponentParameters scp = new SimilarityComponentParameters();

				scp.setThreshold(ISimilarityComponentParameters.Interval.LOW, 0);
				scp.setWeight(ISimilarityComponentParameters.Interval.LOW, 1);
				scp.setThreshold(ISimilarityComponentParameters.Interval.MEDIUM, 0);
				scp.setWeight(ISimilarityComponentParameters.Interval.MEDIUM, 1);
				scp.setThreshold(ISimilarityComponentParameters.Interval.HIGH, 0);
				scp.setWeight(ISimilarityComponentParameters.Interval.HIGH, 1);
				scp.setOutOfBoundsWeight(1);

				return scp;
			}

			@Override
			protected void configure() {
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
				bind(ISequencesManipulator.class).to(NullSequencesManipulator.class);
			}
		});

		final SimilarityFitnessCoreFactory simFactory = new SimilarityFitnessCoreFactory();
		final SimilarityFitnessCore c = (SimilarityFitnessCore) simFactory.instantiate();

		injector.injectMembers(c);

		Mockito.when(data.getResources()).thenReturn(sequences.getResources());

		c.init(data);

		return c;
	}

	@Test
	public void testNoChanges() {
		registerContext();

		final ISequences sequences = createTestISequences();

		final SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);

		final IEvaluationState evaluationState = getIEvaluationState();

		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);
	}

	@Test
	public void testSwapLoads() {
		registerContext();

		ISequences sequences = createTestISequences();

		final SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);

		final IEvaluationState evaluationState = getIEvaluationState();

		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);

		sequences = swapFirstNElementsForTwoSequences(sequences, 0, 1, 1);
		c.evaluate(sequences, evaluationState);

		Assert.assertTrue(c.getFitness() == 4);
	}

	@Test
	public void testSwapCargo() {
		registerContext();

		ISequences sequences = createTestISequences();

		final SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);

		final IEvaluationState evaluationState = getIEvaluationState();

		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);

		sequences = swapFirstNElementsForTwoSequences(sequences, 0, 1, 2);
		c.evaluate(sequences, evaluationState);

		Assert.assertTrue(c.getFitness() == 2);
	}

	@Test
	public void testSwap2Cargoes() {
		registerContext();

		ISequences sequences = createTestISequences();

		final SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);

		final IEvaluationState evaluationState = getIEvaluationState();

		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);

		sequences = swapFirstNElementsForTwoSequences(sequences, 0, 1, 4);
		c.evaluate(sequences, evaluationState);

		Assert.assertTrue(c.getFitness() == 4);
	}

	@Test
	public void testSwap2CargoesAnd1Load() {
		registerContext();

		ISequences sequences = createTestISequences();

		final SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);

		final IEvaluationState evaluationState = getIEvaluationState();

		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);

		sequences = swapFirstNElementsForTwoSequences(sequences, 0, 1, 5);
		c.evaluate(sequences, evaluationState);

		Assert.assertTrue("fitness: " + c.getFitness(), c.getFitness() == 8);
	}

	public void registerContext() {
		context.registerType(Resource.class);
		context.registerType(IResource.class);
		context.registerType(ISequenceElement.class);
		context.registerType(SequenceElement.class);
	}

	private IEvaluationState getIEvaluationState() {
		final IEvaluationState evaluationState = new IEvaluationState() {

			@Override
			public void setData(final String key, final Object data) {
				// TODO Auto-generated method stub

			}

			@Override
			public <T> T getData(final String key, final Class<T> cls) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return evaluationState;
	}

	public List<IResource> createResources(final int numberOfResources) {
		final List<IResource> resources = new LinkedList<IResource>();
		for (int i = 0; i < numberOfResources; i++) {
			resources.add(new Resource(context, String.format("Resource-%d", i)));
		}
		return resources;
	}

	public ISequences createTestISequences() {
		final List<IResource> resources = createResources(2);
		final List<ISequenceElement> sList = new LinkedList<ISequenceElement>();
		for (int i = 0; i < 10; i++) {
			sList.add(new SequenceElement(context, String.format("%s-%s", i % 2 == 0 ? "L" : "D", i)));
		}
		final List<ISequenceElement> sList2 = new LinkedList<ISequenceElement>();
		for (int i = 0; i < 10; i++) {
			sList2.add(new SequenceElement(context, String.format("%s--%s", i % 2 == 0 ? "L" : "D", i)));
		}
		final ISequence sequence1 = new ListSequence(sList);
		final ISequence sequence2 = new ListSequence(sList2);

		final ISequences sequences = createNewISequence(resources, sequence1, sequence2);
		return sequences;
	}

	private ISequences createNewISequence(final List<IResource> resources, final ISequence sequence1, final ISequence sequence2) {
		final Map<IResource, ISequence> sMap = new HashMap<IResource, ISequence>();
		sMap.put(resources.get(0), sequence1);
		sMap.put(resources.get(1), sequence2);
		final ISequences sequences = new Sequences(resources, sMap);
		return sequences;
	}

	private ISequences swapFirstNElementsForTwoSequences(final ISequences sequences, final int idx1, final int idx2, final int n) {
		final List<ISequenceElement> sequence1List = new LinkedList<ISequenceElement>();
		final List<ISequenceElement> sequence2List = new LinkedList<ISequenceElement>();
		createListISequence(sequences.getSequence(idx1), sequence1List, n);
		createListISequence(sequences.getSequence(idx2), sequence2List, n);
		for (int i = 0; i < n; i++) {
			sequence1List.add(i, sequences.getSequence(idx2).get(i));
			sequence2List.add(i, sequences.getSequence(idx1).get(i));
		}
		final ISequence sequence1 = new ListSequence(sequence1List);
		final ISequence sequence2 = new ListSequence(sequence2List);
		return createNewISequence(sequences.getResources(), sequence1, sequence2);
	}

	private void createListISequence(final ISequence sequence, final List<ISequenceElement> sequenceList, final int start) {
		for (int i = start; i < sequence.size(); i++) {
			sequenceList.add(sequence.get(i));
		}
	}
}
