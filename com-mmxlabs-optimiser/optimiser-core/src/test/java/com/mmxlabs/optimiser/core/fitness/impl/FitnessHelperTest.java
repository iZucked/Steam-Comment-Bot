/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

@RunWith(JMock.class)
public class FitnessHelperTest {

	static class CoreWrapper implements IFitnessComponent {

		private final IFitnessCore core;

		public CoreWrapper(final IFitnessCore core) {
			this.core = core;
		}

		@Override
		public long getFitness() {
			fail("Unexpected method invocation");
			return -1;
		}

		@Override
		public IFitnessCore getFitnessCore() {
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
		final FitnessHelper helper = new FitnessHelper();

		final ISequences sequences = context.mock(ISequences.class);

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences);
				will(returnValue(true));
				one(core2).evaluate(sequences);
				will(returnValue(true));
				one(core3).evaluate(sequences);
				will(returnValue(true));
			}
		});

		helper.evaluateSequencesFromComponents(sequences, components);

		context.assertIsSatisfied();

	}

	@Test
	public void testEvaluateSequencesFromCoresISequencesOfTCollectionOfIFitnessCoreOfT() {
		final FitnessHelper helper = new FitnessHelper();

		final ISequences sequences = context.mock(ISequences.class);

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences);
				will(returnValue(true));
				one(core2).evaluate(sequences);
				will(returnValue(true));
				one(core3).evaluate(sequences);
				will(returnValue(true));
			}
		});

		helper.evaluateSequencesFromCores(sequences, cores);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequencesFromComponentsISequencesOfTCollectionOfIFitnessComponentOfTListOfIResource() {
		final FitnessHelper helper = new FitnessHelper();
		final Collection<IResource> resources = Collections.emptyList();

		final ISequences sequences = context.mock(ISequences.class);

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences, resources);
				will(returnValue(true));
				one(core2).evaluate(sequences, resources);
				will(returnValue(true));
				one(core3).evaluate(sequences, resources);
				will(returnValue(true));
			}
		});

		helper.evaluateSequencesFromComponents(sequences, components, resources);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequencesFromCoresISequencesOfTCollectionOfIFitnessCoreOfTListOfIResource() {
		final FitnessHelper helper = new FitnessHelper();
		final Collection<IResource> resources = Collections.emptyList();

		final ISequences sequences = context.mock(ISequences.class);

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		context.checking(new Expectations() {
			{
				one(core1).evaluate(sequences, resources);
				will(returnValue(true));
				one(core2).evaluate(sequences, resources);
				will(returnValue(true));
				one(core3).evaluate(sequences, resources);
				will(returnValue(true));
			}
		});

		helper.evaluateSequencesFromCores(sequences, cores, resources);

		context.assertIsSatisfied();
	}

	@Test
	public void testInitFitnessCores() {

		final IOptimisationData data = context.mock(IOptimisationData.class);

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

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

		final IOptimisationData data = context.mock(IOptimisationData.class);

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

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

	@Test
	public void testAcceptFromComponents() {

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		final ISequences sequences = context.mock(ISequences.class);
		final Collection<IResource> affectedResources = Collections.emptyList();

		context.checking(new Expectations() {
			{
				one(core1).accepted(sequences, affectedResources);
				one(core2).accepted(sequences, affectedResources);
				one(core3).accepted(sequences, affectedResources);
			}
		});

		helper.acceptFromComponents(components, sequences, affectedResources);

		context.assertIsSatisfied();
	}

	@Test
	public void testAcceptFromCores() {

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = context.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = context.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = context.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		final ISequences sequences = context.mock(ISequences.class);
		final Collection<IResource> affectedResources = Collections.emptyList();

		context.checking(new Expectations() {
			{
				one(core1).accepted(sequences, affectedResources);
				one(core2).accepted(sequences, affectedResources);
				one(core3).accepted(sequences, affectedResources);
			}
		});

		helper.acceptFromCores(cores, sequences, affectedResources);

		context.assertIsSatisfied();
	}

}
