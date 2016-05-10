/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);

		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> emptySet());

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));

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
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> singleton(element2));
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> emptySet());

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1));

		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element1);
		Mockito.verify(restrictedElementsProvider).getRestrictedFollowerElements(element2);
		Mockito.verify(restrictedElementsProvider).getRestrictedPrecedingElements(element1);
		// Early fail in && means we should not get this invocation
		// Mockito.verify(provider).getRestrictedPrecedingElements(element2);

		Mockito.verifyNoMoreInteractions(restrictedElementsProvider);
	}

	@Test
	public void test3() {

		final String name = "name";
		final IRestrictedElementsProvider restrictedElementsProvider = Mockito.mock(IRestrictedElementsProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final RestrictedElementsConstraintChecker checker = createChecker(name, restrictedElementsProvider, vesselProvider, portTypeProvider);

		Assert.assertEquals(name, checker.getName());

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);

		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);
		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> singleton(element1));

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1));

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

		Assert.assertEquals(name, checker.getName());

		final IResource resource1 = Mockito.mock(IResource.class);
		final IResource resource2 = Mockito.mock(IResource.class);

		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVessel vessel2 = Mockito.mock(IVessel.class);

		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		final IVesselAvailability vesselAvailability2 = Mockito.mock(IVesselAvailability.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);
		Mockito.when(vesselAvailability2.getVessel()).thenReturn(vessel2);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(vesselProvider.getVesselAvailability(resource2)).thenReturn(vesselAvailability2);
		Mockito.when(vesselAvailability2.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> singleton(element1));

		// First case, Discharge -> Load does not apply on cargo shorts
		Mockito.when(portTypeProvider.getPortType(element1)).thenReturn(PortType.Discharge);
		Mockito.when(portTypeProvider.getPortType(element2)).thenReturn(PortType.Load);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource2));

		// First case, Load -> Discharge does apply on cargo shorts
		Mockito.when(portTypeProvider.getPortType(element1)).thenReturn(PortType.Load);
		Mockito.when(portTypeProvider.getPortType(element2)).thenReturn(PortType.Discharge);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource2));

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
