package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
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

		Assert.assertFalse(provider.isPermittedOnResource(portSlot, vesselAvailability));
		Assert.assertFalse(provider.isPermittedOnResource(element, resource));
	}

	@Test
	public void testPermitted() {

		final DefaultRoundTripVesselPermissionProviderEditor provider = new DefaultRoundTripVesselPermissionProviderEditor();

		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IResource resource = Mockito.mock(IResource.class);

		provider.permitElementOnResource(element, portSlot, resource, vesselAvailability);

		Assert.assertTrue(provider.isPermittedOnResource(portSlot, vesselAvailability));
		Assert.assertTrue(provider.isPermittedOnResource(element, resource));
	}
}
