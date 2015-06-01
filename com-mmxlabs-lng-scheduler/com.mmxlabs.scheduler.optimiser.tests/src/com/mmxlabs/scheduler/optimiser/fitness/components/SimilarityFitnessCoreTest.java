/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class SimilarityFitnessCoreTest {

	IIndexingContext context = new CheckingIndexingContext();
	
	@Test
	public void testSimilarityComponent() {
		final String name = "name";
		final SimilarityFitnessCore core = new SimilarityFitnessCore(name);

		Assert.assertSame(name, core.getName());
	}

	private SimilarityFitnessCore createSimilarityFitnessCore(ISequences sequences) {
		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final IPortSlotProvider portSlotProvider = new TestPortSlotEditor();
		
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Provides
			@Named(SimilarityFitnessCore.SIMILARITY_THRESHOLD_NUM_CHANGES)
			private int getSimilarityFitnessThresholdNumChanges() {
				return -1;
			}
			
			@Provides
			@Named(SimilarityFitnessCore.SIMILARITY_THRESHOLD)
			private boolean getSimilarityFitnessThreshold() {
				return false;
			}

			@Override
			protected void configure() {
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
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

		ISequences sequences = createTestISequences();

		SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);
		
		IEvaluationState evaluationState = getIEvaluationState();
		
		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);
	}
	
	@Test
	public void testSwapLoads() {
		registerContext();
		
		ISequences sequences = createTestISequences();
		
		SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);
		
		IEvaluationState evaluationState = getIEvaluationState();
		
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
		
		SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);
		
		IEvaluationState evaluationState = getIEvaluationState();
		
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
		
		SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);
		
		IEvaluationState evaluationState = getIEvaluationState();
		
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
		
		SimilarityFitnessCore c = createSimilarityFitnessCore(sequences);
		
		IEvaluationState evaluationState = getIEvaluationState();
		
		// initial evaluation
		c.evaluate(sequences, evaluationState);
		Assert.assertTrue(c.getFitness() == 0);
		
		sequences = swapFirstNElementsForTwoSequences(sequences, 0, 1, 5);
		c.evaluate(sequences, evaluationState);
		
		Assert.assertTrue("fitness: "+c.getFitness(),c.getFitness() == 8);
	}
	
	public void registerContext() {
		context.registerType(Resource.class);
		context.registerType(IResource.class);
		context.registerType(ISequenceElement.class);
		context.registerType(SequenceElement.class);
	}
	
	private IEvaluationState getIEvaluationState() {
		IEvaluationState evaluationState = new IEvaluationState() {
			
			@Override
			public void setData(String key, Object data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> T getData(String key, Class<T> cls) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return evaluationState;
	}
	
	public List<IResource> createResources(int numberOfResources) {
		List<IResource> resources = new LinkedList<IResource>();
		for (int i = 0; i < numberOfResources; i++) {
			resources.add(new Resource(context));
		}
		return resources;
	}
	
	public ISequences createTestISequences() {
		List<IResource> resources = createResources(2);
		List<ISequenceElement> sList = new LinkedList<ISequenceElement>();
		for (int i = 0; i < 10; i++) {
			sList.add(new SequenceElement(context, String.format("%s-%s", i%2 == 0 ? "L" : "D", i)));
		}
		List<ISequenceElement> sList2 = new LinkedList<ISequenceElement>();
		for (int i = 0; i < 10; i++) {
			sList2.add(new SequenceElement(context, String.format("%s--%s", i%2 == 0 ? "L" : "D", i)));
		}
		ISequence sequence1 = new ListSequence(sList);
		ISequence sequence2 = new ListSequence(sList2);
		
		ISequences sequences = createNewISequence(resources, sequence1, sequence2);
		return sequences;
	}
	
	private ISequences createNewISequence(List<IResource> resources, ISequence sequence1, ISequence sequence2) {
		Map<IResource, ISequence> sMap = new HashMap<IResource, ISequence>();
		sMap.put(resources.get(0), sequence1);
		sMap.put(resources.get(1), sequence2);
		ISequences sequences = new Sequences(resources, sMap);
		return sequences;
	}
		
	private ISequences swapFirstNElementsForTwoSequences(ISequences sequences, int idx1, int idx2, int n) {
		List<ISequenceElement> sequence1List = new LinkedList<ISequenceElement>();
		List<ISequenceElement> sequence2List = new LinkedList<ISequenceElement>();
		createListISequence(sequences.getSequence(idx1), sequence1List, n);
		createListISequence(sequences.getSequence(idx2), sequence2List, n);
		for (int i = 0; i < n; i++) {
			sequence1List.add(i, sequences.getSequence(idx2).get(i));
			sequence2List.add(i, sequences.getSequence(idx1).get(i));
		}
		ISequence sequence1 = new ListSequence(sequence1List);
		ISequence sequence2 = new ListSequence(sequence2List);
		return createNewISequence(sequences.getResources(), sequence1, sequence2);
	}

	
	private void createListISequence(ISequence sequence, List<ISequenceElement> sequenceList, int start) {
		for (int i = start; i < sequence.size(); i++) {
			sequenceList.add(sequence.get(i));
		}
	}
	
		
	private static final class TestPortSlotEditor implements IPortSlotProviderEditor {

		@Override
		public void setPortSlot(final ISequenceElement element, final IPortSlot portSlot) {
		}

		@Override
		public IPortSlot getPortSlot(final ISequenceElement element) {
			return null;
		}

		@Override
		public ISequenceElement getElement(final IPortSlot portSlot) {
			return null;
		}

		@Override
		public PortType getPortTypeFromElement(ISequenceElement element) {
			if (element.getName().contains("L")) {
				return PortType.Load;
			} else if (element.getName().contains("D")) {
				return PortType.Discharge;
			}
			return null;
		}
	}

	
}
