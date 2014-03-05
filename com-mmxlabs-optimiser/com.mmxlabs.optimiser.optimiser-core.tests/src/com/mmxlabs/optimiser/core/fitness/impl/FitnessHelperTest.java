/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

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

	@Test
	public void testEvaluateSequencesFromComponentsISequencesOfTCollectionOfIFitnessComponentOfT() {
		final FitnessHelper helper = new FitnessHelper();

		final ISequences sequences = Mockito.mock(ISequences.class);

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		Mockito.when(core1.evaluate(sequences)).thenReturn(true);
		Mockito.when(core2.evaluate(sequences)).thenReturn(true);
		Mockito.when(core3.evaluate(sequences)).thenReturn(true);

		helper.evaluateSequencesFromComponents(sequences, components);

		Mockito.verify(core1).evaluate(sequences);
		Mockito.verify(core2).evaluate(sequences);
		Mockito.verify(core3).evaluate(sequences);

	}

	@Test
	public void testEvaluateSequencesFromCoresISequencesOfTCollectionOfIFitnessCoreOfT() {
		final FitnessHelper helper = new FitnessHelper();

		final ISequences sequences = Mockito.mock(ISequences.class);

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		Mockito.when(core1.evaluate(sequences)).thenReturn(true);
		Mockito.when(core2.evaluate(sequences)).thenReturn(true);
		Mockito.when(core3.evaluate(sequences)).thenReturn(true);

		helper.evaluateSequencesFromCores(sequences, cores);

		Mockito.verify(core1).evaluate(sequences);
		Mockito.verify(core2).evaluate(sequences);
		Mockito.verify(core3).evaluate(sequences);
	}

	@Test
	public void testEvaluateSequencesFromComponentsISequencesOfTCollectionOfIFitnessComponentOfTListOfIResource() {
		final FitnessHelper helper = new FitnessHelper();
		final Collection<IResource> resources = Collections.emptyList();

		final ISequences sequences = Mockito.mock(ISequences.class);

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		Mockito.when(core1.evaluate(sequences, resources)).thenReturn(true);
		Mockito.when(core2.evaluate(sequences, resources)).thenReturn(true);
		Mockito.when(core3.evaluate(sequences, resources)).thenReturn(true);

		helper.evaluateSequencesFromComponents(sequences, components, resources);

		Mockito.verify(core1).evaluate(sequences, resources);
		Mockito.verify(core2).evaluate(sequences, resources);
		Mockito.verify(core3).evaluate(sequences, resources);
	}

	@Test
	public void testEvaluateSequencesFromCoresISequencesOfTCollectionOfIFitnessCoreOfTListOfIResource() {
		final FitnessHelper helper = new FitnessHelper();
		final Collection<IResource> resources = Collections.emptyList();

		final ISequences sequences = Mockito.mock(ISequences.class);

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		Mockito.when(core1.evaluate(sequences, resources)).thenReturn(true);
		Mockito.when(core2.evaluate(sequences, resources)).thenReturn(true);
		Mockito.when(core3.evaluate(sequences, resources)).thenReturn(true);

		helper.evaluateSequencesFromCores(sequences, cores, resources);

		Mockito.verify(core1).evaluate(sequences, resources);
		Mockito.verify(core2).evaluate(sequences, resources);
		Mockito.verify(core3).evaluate(sequences, resources);
	}

	@Test
	public void testInitFitnessCores() {

		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		helper.initFitnessCores(cores, data);

		Mockito.verify(core1).init(data);
		Mockito.verify(core2).init(data);
		Mockito.verify(core3).init(data);

	}

	@Test
	public void testInitFitnessComponents() {

		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		helper.initFitnessComponents(components, data);

		Mockito.verify(core1).init(data);
		Mockito.verify(core2).init(data);
		Mockito.verify(core3).init(data);
	}

	@Test
	public void testAcceptFromComponents() {

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessComponent> components = CollectionsUtil.makeArrayList((IFitnessComponent) new CoreWrapper(core1), (IFitnessComponent) new CoreWrapper(core2),
				(IFitnessComponent) new CoreWrapper(core3));

		final ISequences sequences = Mockito.mock(ISequences.class);
		final Collection<IResource> affectedResources = Collections.emptyList();

		helper.acceptFromComponents(components, sequences, affectedResources);

		Mockito.verify(core1).accepted(sequences, affectedResources);
		Mockito.verify(core2).accepted(sequences, affectedResources);
		Mockito.verify(core3).accepted(sequences, affectedResources);
	}

	@Test
	public void testAcceptFromCores() {

		final FitnessHelper helper = new FitnessHelper();

		final IFitnessCore core1 = Mockito.mock(IFitnessCore.class, "core-1");
		final IFitnessCore core2 = Mockito.mock(IFitnessCore.class, "core-2");
		final IFitnessCore core3 = Mockito.mock(IFitnessCore.class, "core-3");

		final List<IFitnessCore> cores = CollectionsUtil.makeArrayList(core1, core2, core3);

		final ISequences sequences = Mockito.mock(ISequences.class);
		final Collection<IResource> affectedResources = Collections.emptyList();

		helper.acceptFromCores(cores, sequences, affectedResources);

		Mockito.verify(core1).accepted(sequences, affectedResources);
		Mockito.verify(core2).accepted(sequences, affectedResources);
		Mockito.verify(core3).accepted(sequences, affectedResources);
	}

}
