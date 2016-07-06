/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource));
	}

	@Test
	public void testForbiddenOnOtherRoundTripVessel() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		final IResource resource1 = Mockito.mock(IResource.class);

		final IVesselAvailability vesselAvailability2 = Mockito.mock(IVesselAvailability.class);
		final IResource resource2 = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability1)).thenReturn(resource1);
		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselProvider.getResource(vesselAvailability2)).thenReturn(resource2);
		Mockito.when(vesselProvider.getVesselAvailability(resource2)).thenReturn(vesselAvailability2);

		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);
		Mockito.when(vesselAvailability2.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability1)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability1)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource1)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource1)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(true);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource1));

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource2));
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource2));
	}

	@Test
	public void testPermittedOnNonRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		for (final VesselInstanceType vesselInstanceType : VesselInstanceType.values()) {
			if (vesselInstanceType == VesselInstanceType.ROUND_TRIP) {
				continue;
			}
			Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(vesselInstanceType);

			Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource));
			Assert.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource));
		}
	}
	@Test
	public void testBothNotPermittedOnRoundTrip_UnboundPair() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(false);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource));
	}
	@Test
	public void testBothPermittedOnRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(true);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource));
		// Only one way pairing
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource));
	}

	@Test
	public void testSinglePermittedOnRoundTrip() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability)).thenReturn(false);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(false);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource));

		// Again, swap flags

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability)).thenReturn(false);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(false);

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(element2, element1, resource));
	}

	@Test
	public void testSinglePermittedOnRoundTrip_Sequence() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		ListSequence sequence = new ListSequence(Lists.newArrayList(element1, element2));

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability)).thenReturn(false);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(false);

		Assert.assertFalse(checker.checkSequence(sequence, resource));
	}

	@Test
	public void testBothPermittedOnRoundTrip_Sequence() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		final IRoundTripVesselPermissionProvider roundTripProvider = Mockito.mock(IRoundTripVesselPermissionProvider.class);

		final RoundTripVesselPermissionConstraintChecker checker = createChecker(vesselProvider, roundTripProvider, portTypeProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);

		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		ListSequence sequence = new ListSequence(Lists.newArrayList(element1, element2));

		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot1, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(portSlot2, vesselAvailability)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element1, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isPermittedOnResource(element2, resource)).thenReturn(true);
		Mockito.when(roundTripProvider.isBoundPair(element1, element2)).thenReturn(true);

		Assert.assertTrue(checker.checkSequence(sequence, resource));
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
