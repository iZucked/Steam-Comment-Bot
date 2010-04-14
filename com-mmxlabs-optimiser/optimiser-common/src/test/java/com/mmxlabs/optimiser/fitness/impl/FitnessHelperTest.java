package com.mmxlabs.optimiser.fitness.impl;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

@RunWith(JMock.class)
@SuppressWarnings("unchecked")
public class FitnessHelperTest {

	class CoreWrapper<T> implements IFitnessComponent<T> {

		private final IFitnessCore<T> core;

		public CoreWrapper(final IFitnessCore<T> core) {
			this.core = core;
		}

		@Override
		public long getFitness() {
			fail("Unexpected method invocation");
			return -1;
		}

		@Override
		public IFitnessCore<T> getFitnessCore() {
			return core;
		}

		@Override
		public String getName() {
			fail("Unexpected method invocation");
			return null;
		}

	}

	Mockery context = new JUnit4Mockery();

	@Test
	public void testEvaluateSequencesFromComponentsISequencesOfTCollectionOfIFitnessComponentOfT() {
		final FitnessHelper<Object> helper = new FitnessHelper<Object>();

		final ISequences<Object> sequences = context.mock(ISequences.class);

		final IFitnessCore<Object> core1 = context.mock(IFitnessCore.class,
				"core-1");
		final IFitnessCore<Object> core2 = context.mock(IFitnessCore.class,
				"core-2");
		final IFitnessCore<Object> core3 = context.mock(IFitnessCore.class,
				"core-3");

		final List<IFitnessComponent<Object>> components = CollectionsUtil
				.makeArrayList(
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core1),
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core2),
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core3));

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences);
				one(core2).evaluate(sequences);
				one(core3).evaluate(sequences);
			}
		});

		helper.evaluateSequencesFromComponents(sequences, components);

		context.assertIsSatisfied();

	}

	@Test
	public void testEvaluateSequencesFromCoresISequencesOfTCollectionOfIFitnessCoreOfT() {
		final FitnessHelper<Object> helper = new FitnessHelper<Object>();

		final ISequences<Object> sequences = context.mock(ISequences.class);

		final IFitnessCore<Object> core1 = context.mock(IFitnessCore.class,
				"core-1");
		final IFitnessCore<Object> core2 = context.mock(IFitnessCore.class,
				"core-2");
		final IFitnessCore<Object> core3 = context.mock(IFitnessCore.class,
				"core-3");

		final List<IFitnessCore<Object>> cores = CollectionsUtil.makeArrayList(
				core1, core2, core3);

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences);
				one(core2).evaluate(sequences);
				one(core3).evaluate(sequences);
			}
		});

		helper.evaluateSequencesFromCores(sequences, cores);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequencesFromComponentsISequencesOfTCollectionOfIFitnessComponentOfTListOfIResource() {
		final FitnessHelper<Object> helper = new FitnessHelper<Object>();
		final Collection<IResource> resources = Collections.emptyList();

		final ISequences<Object> sequences = context.mock(ISequences.class);

		final IFitnessCore<Object> core1 = context.mock(IFitnessCore.class,
				"core-1");
		final IFitnessCore<Object> core2 = context.mock(IFitnessCore.class,
				"core-2");
		final IFitnessCore<Object> core3 = context.mock(IFitnessCore.class,
				"core-3");

		final List<IFitnessComponent<Object>> components = CollectionsUtil
				.makeArrayList(
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core1),
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core2),
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core3));

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences, resources);
				one(core2).evaluate(sequences, resources);
				one(core3).evaluate(sequences, resources);
			}
		});

		helper
				.evaluateSequencesFromComponents(sequences, components,
						resources);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequencesFromCoresISequencesOfTCollectionOfIFitnessCoreOfTListOfIResource() {
		final FitnessHelper<Object> helper = new FitnessHelper<Object>();
		final Collection<IResource> resources = Collections.emptyList();

		final ISequences<Object> sequences = context.mock(ISequences.class);

		final IFitnessCore<Object> core1 = context.mock(IFitnessCore.class,
				"core-1");
		final IFitnessCore<Object> core2 = context.mock(IFitnessCore.class,
				"core-2");
		final IFitnessCore<Object> core3 = context.mock(IFitnessCore.class,
				"core-3");

		final List<IFitnessCore<Object>> cores = CollectionsUtil.makeArrayList(
				core1, core2, core3);

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences, resources);
				one(core2).evaluate(sequences, resources);
				one(core3).evaluate(sequences, resources);
			}
		});

		helper.evaluateSequencesFromCores(sequences, cores, resources);

		context.assertIsSatisfied();
	}

	@Test
	public void testInitFitnessCores() {

		final IOptimisationData<Object> data= context
				.mock(IOptimisationData.class);

		final FitnessHelper<Object> helper = new FitnessHelper<Object>();

		final IFitnessCore<Object> core1 = context.mock(IFitnessCore.class,
				"core-1");
		final IFitnessCore<Object> core2 = context.mock(IFitnessCore.class,
				"core-2");
		final IFitnessCore<Object> core3 = context.mock(IFitnessCore.class,
				"core-3");

		final List<IFitnessCore<Object>> cores = CollectionsUtil.makeArrayList(
				core1, core2, core3);

		context.checking(new Expectations() {
			{
				one(core1).init(data);
				one(core2).init(data);
				one(core3).init(data);
			}
		});

		helper.initFitnessCores(cores, data);

		context.assertIsSatisfied();
	}

	@Test
	public void testInitFitnessComponents() {

		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);

		final FitnessHelper<Object> helper = new FitnessHelper<Object>();

		final IFitnessCore<Object> core1 = context.mock(IFitnessCore.class,
				"core-1");
		final IFitnessCore<Object> core2 = context.mock(IFitnessCore.class,
				"core-2");
		final IFitnessCore<Object> core3 = context.mock(IFitnessCore.class,
				"core-3");

		final List<IFitnessComponent<Object>> components = CollectionsUtil
				.makeArrayList(
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core1),
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core2),
						(IFitnessComponent<Object>) new CoreWrapper<Object>(
								core3));

		context.checking(new Expectations() {
			{
				one(core1).init(data);
				one(core2).init(data);
				one(core3).init(data);
			}
		});

		helper.initFitnessComponents(components, data);

		context.assertIsSatisfied();

	}
}
