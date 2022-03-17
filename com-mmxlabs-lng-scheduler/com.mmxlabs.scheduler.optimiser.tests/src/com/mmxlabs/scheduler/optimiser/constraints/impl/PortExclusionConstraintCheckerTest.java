/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapElementPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapNominatedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

public class PortExclusionConstraintCheckerTest {

	@Test
	public void testName() {
		final String name = "checker";
		final PortExclusionConstraintChecker checker = new PortExclusionConstraintChecker(name);

		Assertions.assertSame(name, checker.getName());
	}

	@Test
	public void testConstraint() {
		//
		final IPortExclusionProviderEditor exclusionProvider = new HashMapPortExclusionProvider();
		final IElementPortProviderEditor portProvider = new HashMapElementPortEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final INominatedVesselProviderEditor nominatedVesselProviderEditor = new HashMapNominatedVesselProviderEditor();

		final PortExclusionConstraintChecker checker = createChecker("name", vesselProvider, nominatedVesselProviderEditor, portProvider, exclusionProvider);

		// check empty behaviour
		Assertions.assertTrue(exclusionProvider.hasNoExclusions());
		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");

		final IPort p1 = Mockito.mock(IPort.class, "p1");
		final IPort p2 = Mockito.mock(IPort.class, "p2");
		final IPort p3 = Mockito.mock(IPort.class, "p3");

		portProvider.setPortForElement(p1, o1);
		portProvider.setPortForElement(p1, o4);
		portProvider.setPortForElement(p2, o2);
		portProvider.setPortForElement(p3, o3);

		final IVessel vessel = Mockito.mock(IVessel.class);
		final IResource resource = Mockito.mock(IResource.class);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability.getVessel()).thenReturn(vessel);

		vesselProvider.setVesselAvailabilityResource(resource, vesselAvailability);

		Assertions.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource, new ArrayList<>()));
		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o4));

		Assertions.assertTrue(checker.checkSequence(sequence, resource, new ArrayList<>()));

		exclusionProvider.setExcludedPorts(vessel, CollectionsUtil.makeHashSet(p3));
		Assertions.assertFalse(exclusionProvider.hasNoExclusions());
		Assertions.assertTrue(exclusionProvider.getExcludedPorts(vessel).contains(p3));

		Assertions.assertTrue(checker.checkSequence(sequence, resource, new ArrayList<>()));
		final ISequence sequence2 = new ListSequence(CollectionsUtil.makeArrayList(o1, o3, o4));
		Assertions.assertFalse(checker.checkSequence(sequence2, resource, new ArrayList<>()));

		Assertions.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(o1, o3, resource, new ArrayList<>()));

	}

	@Test
	public void testConstraintNominatedVessel() {
		//
		final IPortExclusionProviderEditor exclusionProvider = new HashMapPortExclusionProvider();
		final IElementPortProviderEditor portProvider = new HashMapElementPortEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final INominatedVesselProviderEditor nominatedVesselProviderEditor = new HashMapNominatedVesselProviderEditor();

		final PortExclusionConstraintChecker checker = createChecker("name", vesselProvider, nominatedVesselProviderEditor, portProvider, exclusionProvider);

		// check empty behaviour
		Assertions.assertTrue(exclusionProvider.hasNoExclusions());
		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");

		final IPort p1 = Mockito.mock(IPort.class, "p1");
		final IPort p2 = Mockito.mock(IPort.class, "p2");
		final IPort p3 = Mockito.mock(IPort.class, "p3");

		portProvider.setPortForElement(p1, o1);
		portProvider.setPortForElement(p1, o4);
		portProvider.setPortForElement(p2, o2);
		portProvider.setPortForElement(p3, o3);

		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVessel nominatedVessel = Mockito.mock(IVessel.class);
		final IResource resource = Mockito.mock(IResource.class);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability.getVessel()).thenReturn(vessel);

		vesselProvider.setVesselAvailabilityResource(resource, vesselAvailability);
		nominatedVesselProviderEditor.setNominatedVessel(o2, resource, nominatedVessel);

		Assertions.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource, new ArrayList<>()));
		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o4));

		Assertions.assertTrue(checker.checkSequence(sequence, resource, new ArrayList<>()));

		exclusionProvider.setExcludedPorts(nominatedVessel, CollectionsUtil.makeHashSet(p3));
		Assertions.assertFalse(exclusionProvider.hasNoExclusions());
		Assertions.assertTrue(exclusionProvider.getExcludedPorts(nominatedVessel).contains(p3));

		Assertions.assertTrue(checker.checkSequence(sequence, resource, new ArrayList<>()));
		final ISequence sequence2 = new ListSequence(CollectionsUtil.makeArrayList(o1, o3, o4));
		Assertions.assertFalse(checker.checkSequence(sequence2, resource, new ArrayList<>()));

		Assertions.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(o1, o3, resource, new ArrayList<>()));

	}

	@SuppressWarnings("null")
	@NonNull
	private PortExclusionConstraintChecker createChecker(@NonNull final String name, @NonNull final IVesselProvider vesselProvider, @NonNull final INominatedVesselProvider nominatedVesselProvider,
			@NonNull final IElementPortProvider portProvider, final IPortExclusionProvider portExclusionProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IElementPortProvider.class).toInstance(portProvider);
				bind(IPortExclusionProvider.class).toInstance(portExclusionProvider);
				bind(INominatedVesselProvider.class).toInstance(nominatedVesselProvider);
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
