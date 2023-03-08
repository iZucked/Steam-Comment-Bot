/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class RestrictedElementsConstraintCheckerTest {

	@Test
	public void test1() {

		final String name = "name";
		final IRestrictedElementsProvider restrictedElementsProvider = Mockito.mock(IRestrictedElementsProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final RestrictedElementsConstraintChecker checker = createChecker(name, restrictedElementsProvider, vesselProvider, portTypeProvider);

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.emptySet());

		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));

		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element1);
		Mockito.verify(restrictedElementsProvider).getRestrictedPrecedingElements(element2);

		Mockito.verifyNoMoreInteractions(restrictedElementsProvider);
	}

	@Test
	public void test2() {

		final String name = "name";
		final IRestrictedElementsProvider restrictedElementsProvider = Mockito.mock(IRestrictedElementsProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final RestrictedElementsConstraintChecker checker = createChecker(name, restrictedElementsProvider, vesselProvider, portTypeProvider);

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);
		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.singleton(element2));
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.emptySet());

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1, new ArrayList<>()));

		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element1);
		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element2);
		Mockito.verify(restrictedElementsProvider).getRestrictedPrecedingElements(element1);
		// Early fail in && means we should not get this invocation
		// Mockito.verify(provider).getRestrictedPrecedingElements(element2)

		Mockito.verifyNoMoreInteractions(restrictedElementsProvider);
	}

	@Test
	public void test3() {

		final String name = "name";
		final IRestrictedElementsProvider restrictedElementsProvider = Mockito.mock(IRestrictedElementsProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final RestrictedElementsConstraintChecker checker = createChecker(name, restrictedElementsProvider, vesselProvider, portTypeProvider);

		Assertions.assertEquals(name, checker.getName());

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);
		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.singleton(element1));

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1, new ArrayList<>()));

		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element1);
		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element2);
		Mockito.verify(restrictedElementsProvider).getRestrictedPrecedingElements(element1);
		Mockito.verify(restrictedElementsProvider).getRestrictedPrecedingElements(element2);

		Mockito.verifyNoMoreInteractions(restrictedElementsProvider);
	}

	/**
	 * Check difference between cargo shorts and normal routes
	 */
	@Test
	public void test4() {

		final String name = "name";
		final IRestrictedElementsProvider restrictedElementsProvider = Mockito.mock(IRestrictedElementsProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final RestrictedElementsConstraintChecker checker = createChecker(name, restrictedElementsProvider, vesselProvider, portTypeProvider);

		Assertions.assertEquals(name, checker.getName());

		final IResource resource1 = Mockito.mock(IResource.class);
		final IResource resource2 = Mockito.mock(IResource.class);

		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVessel vessel2 = Mockito.mock(IVessel.class);

		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);
		final IVesselCharter vesselCharter2 = Mockito.mock(IVesselCharter.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);
		Mockito.when(vesselCharter2.getVessel()).thenReturn(vessel2);

		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(vesselProvider.getVesselCharter(resource2)).thenReturn(vesselCharter2);
		Mockito.when(vesselCharter2.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.singleton(element1));

		// First case, Discharge -> Load does not apply on cargo shorts
		Mockito.when(portTypeProvider.getPortType(element1)).thenReturn(PortType.Discharge);
		Mockito.when(portTypeProvider.getPortType(element2)).thenReturn(PortType.Load);

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource2, new ArrayList<>()));

		// First case, Load -> Discharge does apply on cargo shorts
		Mockito.when(portTypeProvider.getPortType(element1)).thenReturn(PortType.Load);
		Mockito.when(portTypeProvider.getPortType(element2)).thenReturn(PortType.Discharge);

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource2, new ArrayList<>()));

	}

	private RestrictedElementsConstraintChecker createChecker(final String name, final IRestrictedElementsProvider restrictedElementsProvider, final IVesselProvider vesselProvider,
			final IPortTypeProvider portTypeProvider) {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IRestrictedElementsProvider.class).toInstance(restrictedElementsProvider);
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
			}

			@Provides
			RestrictedElementsConstraintChecker create(Injector injector) {
				RestrictedElementsConstraintChecker checker = new RestrictedElementsConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}
		};
		final Injector injector = Guice.createInjector(module);
		return injector.getInstance(RestrictedElementsConstraintChecker.class);
	}
}
