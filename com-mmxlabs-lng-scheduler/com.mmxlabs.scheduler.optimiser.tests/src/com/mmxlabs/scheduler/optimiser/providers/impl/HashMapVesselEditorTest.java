/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class HashMapVesselEditorTest {


	@Test
	public void testHashMapVesselEditor() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class, "vessel-1");

		Assert.assertNull(editor.getResource(vesselAvailability1));
		Assert.assertNull(editor.getVesselAvailability(resource1));

		editor.setVesselAvailabilityResource(resource1, vesselAvailability1);

		Assert.assertSame(vesselAvailability1, editor.getVesselAvailability(resource1));
		Assert.assertSame(resource1, editor.getResource(vesselAvailability1));
	}
}
