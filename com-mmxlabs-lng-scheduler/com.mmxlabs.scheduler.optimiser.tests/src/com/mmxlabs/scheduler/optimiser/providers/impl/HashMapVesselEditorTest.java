/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public class HashMapVesselEditorTest {


	@Test
	public void testHashMapVesselEditor() {
		final HashMapVesselEditor editor = new HashMapVesselEditor();

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IVessel vessel1 = Mockito.mock(IVessel.class, "vessel-1");

		Assert.assertNull(editor.getResource(vessel1));
		Assert.assertNull(editor.getVessel(resource1));

		editor.setVesselResource(resource1, vessel1);

		Assert.assertSame(vessel1, editor.getVessel(resource1));
		Assert.assertSame(resource1, editor.getResource(vessel1));
	}
}
