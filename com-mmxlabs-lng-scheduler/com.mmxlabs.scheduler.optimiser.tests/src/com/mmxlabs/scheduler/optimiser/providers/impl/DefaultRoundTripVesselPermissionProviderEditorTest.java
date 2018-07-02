/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class DefaultRoundTripVesselPermissionProviderEditorTest {

	@Test
	public void testDefaultNotPermitted() {

		final DefaultRoundTripVesselPermissionProviderEditor provider = new DefaultRoundTripVesselPermissionProviderEditor();

		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		Assertions.assertFalse(provider.isPermittedOnResource(portSlot, vesselAvailability));
		Assertions.assertFalse(provider.isPermittedOnResource(element, resource));
	}

	@Test
	public void testPermitted() {

		final DefaultRoundTripVesselPermissionProviderEditor provider = new DefaultRoundTripVesselPermissionProviderEditor();

		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		provider.permitElementOnResource(element, portSlot, resource, vesselAvailability);

		Assertions.assertTrue(provider.isPermittedOnResource(portSlot, vesselAvailability));
		Assertions.assertTrue(provider.isPermittedOnResource(element, resource));
	}
}
