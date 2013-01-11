/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

@RunWith(JMock.class)
public class PortExclusionConstraintCheckerTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testName() {
		final String name = "checker";
		final PortExclusionConstraintChecker checker = new PortExclusionConstraintChecker(name);

		Assert.assertSame(name, checker.getName());
	}

	@Test
	public void testConstraint() {
		//
		final IPortExclusionProviderEditor exclusionProvider = new HashMapPortExclusionProvider("exclusions");
		final IPortProviderEditor portProvider = new HashMapPortEditor("ports");
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vessels");

		final PortExclusionConstraintChecker checker = createChecker("name", vesselProvider, portProvider, exclusionProvider);

		// check empty behaviour
		Assert.assertTrue(exclusionProvider.hasNoExclusions());
		final ISequenceElement o1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = context.mock(ISequenceElement.class, "4");

		final IPort p1 = context.mock(IPort.class, "p1");
		final IPort p2 = context.mock(IPort.class, "p2");
		final IPort p3 = context.mock(IPort.class, "p3");

		portProvider.setPortForElement(p1, o1);
		portProvider.setPortForElement(p1, o4);
		portProvider.setPortForElement(p2, o2);
		portProvider.setPortForElement(p3, o3);

		final IVessel vessel = context.mock(IVessel.class);
		final IVesselClass vesselClass = context.mock(IVesselClass.class);
		final IResource resource = context.mock(IResource.class);

		context.checking(new Expectations() {
			{
				atLeast(1).of(vessel).getVesselClass();
				will(returnValue(vesselClass));
			}
		});

		vesselProvider.setVesselResource(resource, vessel);

		Assert.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource));
		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o4));

		Assert.assertTrue(checker.checkSequence(sequence, resource));

		exclusionProvider.setExcludedPorts(vessel.getVesselClass(), CollectionsUtil.makeHashSet(p3));
		Assert.assertFalse(exclusionProvider.hasNoExclusions());
		Assert.assertTrue(exclusionProvider.getExcludedPorts(vesselClass).contains(p3));

		Assert.assertTrue(checker.checkSequence(sequence, resource));
		final ISequence sequence2 = new ListSequence(CollectionsUtil.makeArrayList(o1, o3, o4));
		Assert.assertFalse(checker.checkSequence(sequence2, resource));

		Assert.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(o1, o3, resource));

		context.assertIsSatisfied();
	}

	private PortExclusionConstraintChecker createChecker(final String name, final IVesselProvider vesselProvider, final IPortProvider portProvider, final IPortExclusionProvider portExclusionProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IPortProvider.class).toInstance(portProvider);
				bind(IPortExclusionProvider.class).toInstance(portExclusionProvider);
			}

			@Provides
			PortExclusionConstraintChecker create(final Injector injector) {
				final PortExclusionConstraintChecker checker = new PortExclusionConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}

		});
		return injector.getInstance(PortExclusionConstraintChecker.class);
	}
}
