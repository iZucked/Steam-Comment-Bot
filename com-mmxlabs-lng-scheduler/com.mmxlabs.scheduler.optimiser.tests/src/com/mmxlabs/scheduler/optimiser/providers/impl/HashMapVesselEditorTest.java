/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public class HashMapVesselEditorTest {

	@Test
	public void testHashMapVesselEditor() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class, "vessel-1");

		editor.setVesselCharterResource(resource1, vesselCharter1);

		Assertions.assertSame(vesselCharter1, editor.getVesselCharter(resource1));
		Assertions.assertSame(resource1, editor.getResource(vesselCharter1));
	}

	@Test
	public void testGetUnknownVesselCharter() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IVesselCharter vesselCharter = Mockito.mock(IVesselCharter.class, "vessel-1");
		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getResource(vesselCharter));
	}

	@Test
	public void testGetUnknownResource() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");

		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getVesselCharter(resource1));

	}
}
