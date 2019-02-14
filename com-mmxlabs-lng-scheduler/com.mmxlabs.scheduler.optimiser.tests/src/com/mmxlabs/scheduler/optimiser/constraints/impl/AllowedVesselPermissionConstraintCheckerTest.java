/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class AllowedVesselPermissionConstraintCheckerTest {

	@Ignore("Not finished")
	@Test
	public void testNoRestrictions() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final INominatedVesselProvider nominatedVesselProvider = Mockito.mock(INominatedVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final IPortSlotProvider portSlotProvider = Mockito.mock(IPortSlotProvider.class);
		final IAllowedVesselProvider allowedVesselProvider = Mockito.mock(IAllowedVesselProvider.class);

		final AllowedVesselPermissionConstraintChecker checker = createChecker(vesselProvider, nominatedVesselProvider, allowedVesselProvider, portTypeProvider, portSlotProvider);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(vesselProvider.getResource(vesselAvailability)).thenReturn(resource);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vesselAvailability.getVessel()).thenReturn(vessel);

		final Pair<@NonNull ISequenceElement, @NonNull IPortSlot> slot1 = createSequenceElement(portSlotProvider);
		final Pair<@NonNull ISequenceElement, @NonNull IPortSlot> slot2 = createSequenceElement(portSlotProvider);

		Assert.assertFalse(checker.checkPairwiseConstraint(slot1.getFirst(), slot2.getFirst(), resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(slot2.getFirst(), slot1.getFirst(), resource));
	}

	private AllowedVesselPermissionConstraintChecker createChecker(final @NonNull IVesselProvider vesselProvider, final @NonNull INominatedVesselProvider nominatedVesselProvider,
			final @NonNull IAllowedVesselProvider allowedVesselProvider, final @NonNull IPortTypeProvider portTypeProvider, @NonNull final IPortSlotProvider portSlotProvider) {
		final AllowedVesselPermissionConstraintChecker checker = new AllowedVesselPermissionConstraintChecker("checker");
		final Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(INominatedVesselProvider.class).toInstance(nominatedVesselProvider);
				bind(IAllowedVesselProvider.class).toInstance(allowedVesselProvider);
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
			}
		});

		injector.injectMembers(checker);

		return checker;
	}

	@NonNull
	Pair<@NonNull ISequenceElement, @NonNull IPortSlot> createSequenceElement(final IPortSlotProvider mockedProvider) {

		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);

		Mockito.when(mockedProvider.getElement(portSlot)).thenReturn(element);
		Mockito.when(mockedProvider.getPortSlot(element)).thenReturn(portSlot);

		return new Pair<>(element, portSlot);
	}
}
