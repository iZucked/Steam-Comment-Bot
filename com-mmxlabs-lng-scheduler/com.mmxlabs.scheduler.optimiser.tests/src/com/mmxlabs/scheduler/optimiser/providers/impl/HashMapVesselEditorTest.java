/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class HashMapVesselEditorTest {

	@Test
	public void testHashMapVesselEditor() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class, "vessel-1");

		editor.setVesselAvailabilityResource(resource1, vesselAvailability1);

		Assertions.assertSame(vesselAvailability1, editor.getVesselAvailability(resource1));
		Assertions.assertSame(resource1, editor.getResource(vesselAvailability1));
	}

	@Test
	public void testGetUnknownVesselAvailability() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class, "vessel-1");
		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getResource(vesselAvailability));
	}

	@Test
	public void testGetUnknownResource() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");

		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getVesselAvailability(resource1));

	}
}
