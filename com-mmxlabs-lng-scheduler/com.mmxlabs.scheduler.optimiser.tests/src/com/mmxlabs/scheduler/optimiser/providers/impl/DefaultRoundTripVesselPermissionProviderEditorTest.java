/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public class DefaultRoundTripVesselPermissionProviderEditorTest {

	@Test
	public void testDefaultNotPermitted() {

		final DefaultRoundTripVesselPermissionProviderEditor provider = new DefaultRoundTripVesselPermissionProviderEditor();

		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		Assertions.assertFalse(provider.isPermittedOnResource(element, resource));
	}

	@Test
	public void testPermitted() {

		final DefaultRoundTripVesselPermissionProviderEditor provider = new DefaultRoundTripVesselPermissionProviderEditor();

		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class);
		final IResource resource = Mockito.mock(IResource.class);

		provider.permitElementOnResource(element, resource, vesselCharter);

		Assertions.assertTrue(provider.isPermittedOnResource(element, resource));
	}
}
