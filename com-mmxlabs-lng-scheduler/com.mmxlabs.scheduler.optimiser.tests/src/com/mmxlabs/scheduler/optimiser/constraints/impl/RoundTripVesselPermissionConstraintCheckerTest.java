/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class RoundTripVesselPermissionConstraintCheckerTest {

	@Test
	public void testForbiddenOnRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource, new ArrayList<>()));
	}

	@Test
	public void testForbiddenOnOtherRoundTripVessel() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);
		final IResource resource1 = Mockito.mock(IResource.class);

		final IVesselCharter vesselCharter2 = Mockito.mock(IVesselCharter.class);
		final IResource resource2 = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter1)).thenReturn(resource1);
		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselProvider.getResource(vesselCharter2)).thenReturn(resource2);
		Mockito.when(vesselProvider.getVesselCharter(resource2)).thenReturn(vesselCharter2);

		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);
		Mockito.when(vesselCharter2.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter1)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter1)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource1)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource1)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(true);

		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource1, new ArrayList<>()));

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource2, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource2, new ArrayList<>()));
	}

	@Test
	public void testPermittedOnNonRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		for (final VesselInstanceType vesselInstanceType : VesselInstanceType.values()) {
			if (vesselInstanceType == VesselInstanceType.ROUND_TRIP) {
				continue;
			}
			Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(vesselInstanceType);

			Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource, new ArrayList<>()));
			Assertions.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource, new ArrayList<>()));
		}
	}
	@Test
	public void testBothNotPermittedOnRoundTrip_UnboundPair() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(false);

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource, new ArrayList<>()));
	}
	@Test
	public void testBothPermittedOnRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(true);

		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource, new ArrayList<>()));
		// Only one way pairing
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource, new ArrayList<>()));
	}

	@Test
	public void testSinglePermittedOnRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter)).thenReturn(false);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(false);

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource, new ArrayList<>()));

		// Again, swap flags

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter)).thenReturn(false);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(false);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource, new ArrayList<>()));
		Assertions.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource, new ArrayList<>()));
	}

	@Test
	public void testSinglePermittedOnRoundTrip_Sequence() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		ListSequence sequence = new ListSequence(Lists.newArrayList(element1, element2));

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter)).thenReturn(false);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(false);

		Assertions.assertFalse(checker.checkSequence(sequence, resource, new ArrayList<>()));
	}

	@Test
	public void testBothPermittedOnRoundTrip_Sequence() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselCharter)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselCharter(resource)).thenReturn(vesselCharter);

		Mockito.when(vesselCharter.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		ListSequence sequence = new ListSequence(Lists.newArrayList(element1, element2));

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselCharter)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(true);

		Assertions.assertTrue(checker.checkSequence(sequence, resource, new ArrayList<>()));
	}

	private RoundTripVesselPermissionConstraintChecker createChecker(final IVesselProvider vesselProvider, final IRoundTripVesselPermissionProvider roundTripProvider,
			final IPortTypeProvider portTypeProvider) {
		final RoundTripVesselPermissionConstraintChecker checker = new RoundTripVesselPermissionConstraintChecker("checker");
		final Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IRoundTripVesselPermissionProvider.class).toInstance(roundTripProvider);
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
			}
		});

		injector.injectMembers(checker);

		return checker;
	}

}
