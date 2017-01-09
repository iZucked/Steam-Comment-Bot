/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;

public class MatrixProviderFitnessCoreTest {

	@NonNull
	private final IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testMatrixProviderFitnessCore() {
		final String componentName = "componentName";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName);

		final Collection<IFitnessComponent> fitnessComponents = core.getFitnessComponents();
		Assert.assertNotNull(fitnessComponents);
		Assert.assertEquals(1, fitnessComponents.size());

		final IFitnessComponent component = fitnessComponents.iterator().next();
		Assert.assertNotNull(component);
		Assert.assertSame(componentName, component.getName());
		Assert.assertSame(core, component.getFitnessCore());
	}

	@Test
	public void testAccepted() {
		final HashMapMatrixProvider<ISequenceElement, Number> matrix = new HashMapMatrixProvider<ISequenceElement, Number>();

		final MatrixProviderFitnessCore core = createFitnessCore(matrix);

		final OptimisationData data = new OptimisationData();

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;

		core.init(data);
		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");
		final Sequences sequences;
		{
			final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

			final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj4));

			final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);

			sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);
		}

		matrix.set(obj1, obj2, Double.valueOf(5.0));
		matrix.set(obj2, obj3, Double.valueOf(7.0));
		matrix.set(obj3, obj4, Double.valueOf(10.0));
		matrix.set(obj1, obj4, Double.valueOf(15.0));

		core.evaluate(sequences, evaluationState);
		Assert.assertEquals(15, core.getNewFitness());

		final Collection<IResource> affectedResources1 = Collections.singletonList(r1);
		final Collection<IResource> affectedResources2 = Collections.singletonList(r2);

		core.evaluate(sequences, evaluationState, affectedResources1);

		Assert.assertEquals(15, core.getNewFitness());

		core.evaluate(sequences, evaluationState, affectedResources2);

		Assert.assertEquals(15, core.getNewFitness());

		final Sequences sequences2;
		{
			final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj4));

			final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3));

			final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);

			sequences2 = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);
		}

		// Test acceptance

		core.evaluate(sequences2, evaluationState, affectedResources1);
		Assert.assertEquals(25, core.getNewFitness());

		core.accepted(sequences2, affectedResources1);

		core.evaluate(sequences2, evaluationState, affectedResources2);
		Assert.assertEquals(22, core.getNewFitness());
	}

	@Test
	public void testEvaluateISequencesOfT() {

		final HashMapMatrixProvider<ISequenceElement, Number> matrix = new HashMapMatrixProvider<ISequenceElement, Number>();
		final MatrixProviderFitnessCore core = createFitnessCore(matrix);

		final OptimisationData data = new OptimisationData();

		core.init(data);

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		matrix.set(obj1, obj2, Double.valueOf(5.0));
		matrix.set(obj3, obj4, Double.valueOf(10.0));

		core.evaluate(sequences, evaluationState);

		Assert.assertEquals(15, core.getNewFitness());

		core.setScaleFactor(2.0);
		// Make sure fitness has not changed yet
		Assert.assertEquals(15, core.getNewFitness());

		core.evaluate(sequences, evaluationState);

		Assert.assertEquals(30, core.getNewFitness());
	}

	@Test
	public void testEvaluateISequencesOfTCollectionOfIResource() {
		final HashMapMatrixProvider<ISequenceElement, Number> matrix = new HashMapMatrixProvider<ISequenceElement, Number>();

		final MatrixProviderFitnessCore core = createFitnessCore(matrix);

		final OptimisationData data = new OptimisationData();

		core.init(data);

		final IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		assert evaluationState != null;

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");
		final Sequences sequences;
		{
			final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

			final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj4));

			final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);

			sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);
		}

		matrix.set(obj1, obj2, Double.valueOf(5.0));
		matrix.set(obj2, obj3, Double.valueOf(7.0));
		matrix.set(obj3, obj4, Double.valueOf(10.0));
		matrix.set(obj1, obj4, Double.valueOf(15.0));

		core.evaluate(sequences, evaluationState);
		Assert.assertEquals(15, core.getNewFitness());

		final Collection<IResource> affectedResources1 = Collections.singletonList(r1);
		final Collection<IResource> affectedResources2 = Collections.singletonList(r2);

		core.evaluate(sequences, evaluationState, affectedResources1);

		Assert.assertEquals(15, core.getNewFitness());

		core.evaluate(sequences, evaluationState, affectedResources2);

		Assert.assertEquals(15, core.getNewFitness());

		final Sequences sequences2;
		{
			final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj4));

			final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3));

			final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);

			sequences2 = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);
		}

		// Test just deltas
		core.evaluate(sequences2, evaluationState, affectedResources1);
		Assert.assertEquals(25, core.getNewFitness());

		core.evaluate(sequences2, evaluationState, affectedResources2);
		Assert.assertEquals(12, core.getNewFitness());
	}

	@Test
	public void testGetFitnessComponents() {
		final String componentName = "componentName";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName);

		final Collection<IFitnessComponent> fitnessComponents = core.getFitnessComponents();
		Assert.assertNotNull(fitnessComponents);
		Assert.assertFalse(fitnessComponents.isEmpty());
		Assert.assertEquals(1, fitnessComponents.size());

		final IFitnessComponent component = fitnessComponents.iterator().next();
		Assert.assertNotNull(component);
		Assert.assertSame(componentName, component.getName());
		Assert.assertTrue(component instanceof MatrixProviderFitnessComponent);
	}

	@Test
	public void testInit() {

		final HashMapMatrixProvider<ISequenceElement, Number> matrix = new HashMapMatrixProvider<ISequenceElement, Number>();

		final MatrixProviderFitnessCore core = createFitnessCore(matrix);

		final OptimisationData data = new OptimisationData();

		core.init(data);

		Assert.assertSame(matrix, core.getMatrix());
	}

	@Test
	public void testScaleFactor() {

		final String componentName = "componentName";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName);

		Assert.assertEquals(1.0, core.getScaleFactor(), 0.0);

		final double scale = 5.0;
		core.setScaleFactor(scale);
		Assert.assertEquals(scale, core.getScaleFactor(), 0.0);
	}

	private MatrixProviderFitnessCore createFitnessCore(final IMatrixProvider<ISequenceElement, Number> matrix) {

		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore("componentName");
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(new TypeLiteral<IMatrixProvider<ISequenceElement, Number>>() {
				}).toInstance(matrix);
			}
		});

		injector.injectMembers(core);
		for (final IFitnessComponent c : core.getFitnessComponents()) {
			injector.injectMembers(c);
		}
		return core;
	}

}
