/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;

@RunWith(JMock.class)
public class MatrixProviderFitnessCoreTest {

	Mockery context = new JUnit4Mockery();

	final IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testMatrixProviderFitnessCore() {
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

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
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		final HashMapMatrixProvider<Object, Number> matrix = new HashMapMatrixProvider<Object, Number>(matrixProviderKey);

		final OptimisationData data = new OptimisationData();
		data.addDataComponentProvider(matrixProviderKey, matrix);

		core.init(data);
		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);
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

		core.evaluate(sequences);
		Assert.assertEquals(15, core.getNewFitness());

		final Collection<IResource> affectedResources1 = Collections.singletonList(r1);
		final Collection<IResource> affectedResources2 = Collections.singletonList(r2);

		core.evaluate(sequences, affectedResources1);

		Assert.assertEquals(15, core.getNewFitness());

		core.evaluate(sequences, affectedResources2);

		Assert.assertEquals(15, core.getNewFitness());

		final Sequences sequences2;
		{
			final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj4));

			final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3));

			final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);

			sequences2 = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);
		}

		// Test acceptance

		core.evaluate(sequences2, affectedResources1);
		Assert.assertEquals(25, core.getNewFitness());

		core.accepted(sequences2, affectedResources1);

		core.evaluate(sequences2, affectedResources2);
		Assert.assertEquals(22, core.getNewFitness());
	}

	@Test
	public void testEvaluateISequencesOfT() {
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		final HashMapMatrixProvider<Object, Number> matrix = new HashMapMatrixProvider<Object, Number>(matrixProviderKey);

		final OptimisationData data = new OptimisationData();
		data.addDataComponentProvider(matrixProviderKey, matrix);

		core.init(data);

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		matrix.set(obj1, obj2, Double.valueOf(5.0));
		matrix.set(obj3, obj4, Double.valueOf(10.0));

		core.evaluate(sequences);

		Assert.assertEquals(15, core.getNewFitness());

		core.setScaleFactor(2.0);
		// Make sure fitness has not changed yet
		Assert.assertEquals(15, core.getNewFitness());

		core.evaluate(sequences);

		Assert.assertEquals(30, core.getNewFitness());
	}

	@Test
	public void testEvaluateISequencesOfTCollectionOfIResource() {
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		final HashMapMatrixProvider<Object, Number> matrix = new HashMapMatrixProvider<Object, Number>(matrixProviderKey);

		final OptimisationData data = new OptimisationData();
		data.addDataComponentProvider(matrixProviderKey, matrix);

		core.init(data);

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);
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

		core.evaluate(sequences);
		Assert.assertEquals(15, core.getNewFitness());

		final Collection<IResource> affectedResources1 = Collections.singletonList(r1);
		final Collection<IResource> affectedResources2 = Collections.singletonList(r2);

		core.evaluate(sequences, affectedResources1);

		Assert.assertEquals(15, core.getNewFitness());

		core.evaluate(sequences, affectedResources2);

		Assert.assertEquals(15, core.getNewFitness());

		final Sequences sequences2;
		{
			final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj4));

			final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3));

			final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);

			sequences2 = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);
		}

		// Test just deltas
		core.evaluate(sequences2, affectedResources1);
		Assert.assertEquals(25, core.getNewFitness());

		core.evaluate(sequences2, affectedResources2);
		Assert.assertEquals(12, core.getNewFitness());
	}

	@Test
	public void testGetFitnessComponents() {
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

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
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		final HashMapMatrixProvider<Object, Number> matrix = new HashMapMatrixProvider<Object, Number>(matrixProviderKey);

		final OptimisationData data = new OptimisationData();
		data.addDataComponentProvider(matrixProviderKey, matrix);

		core.init(data);

		Assert.assertSame(matrix, core.getMatrix());
	}

	@Test
	public void testScaleFactor() {

		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		Assert.assertEquals(1.0, core.getScaleFactor(), 0.0);

		final double scale = 5.0;
		core.setScaleFactor(scale);
		Assert.assertEquals(scale, core.getScaleFactor(), 0.0);
	}

	@Test
	public void testGetSetMatrix() {

		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		Assert.assertNull(core.getMatrix());

		final IMatrixProvider<ISequenceElement, Number> matrix = new HashMapMatrixProvider<ISequenceElement, Number>(matrixProviderKey);

		core.setMatrix(matrix);

		Assert.assertSame(matrix, core.getMatrix());

	}

	@Test
	public void testDispose() {
		final String componentName = "componentName";
		final String matrixProviderKey = "matrixProviderKey";
		final MatrixProviderFitnessCore core = new MatrixProviderFitnessCore(componentName, matrixProviderKey);

		final HashMapMatrixProvider<Object, Number> matrix = new HashMapMatrixProvider<Object, Number>(matrixProviderKey);

		final OptimisationData data = new OptimisationData();
		data.addDataComponentProvider(matrixProviderKey, matrix);

		core.init(data);

		Assert.assertSame(matrix, core.getMatrix());

		core.dispose();

		Assert.assertNull(core.getMatrix());
	}

}
