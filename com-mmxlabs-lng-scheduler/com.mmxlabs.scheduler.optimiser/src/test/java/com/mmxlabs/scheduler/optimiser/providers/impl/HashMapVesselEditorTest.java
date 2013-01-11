/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

@RunWith(JMock.class)
public class HashMapVesselEditorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testHashMapVesselEditor() {
		final String name = "name";
		final HashMapVesselEditor editor = new HashMapVesselEditor(name);
		Assert.assertSame(name, editor.getName());

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IVessel vessel1 = context.mock(IVessel.class, "vessel-1");

		Assert.assertNull(editor.getResource(vessel1));
		Assert.assertNull(editor.getVessel(resource1));

		editor.setVesselResource(resource1, vessel1);

		Assert.assertSame(vessel1, editor.getVessel(resource1));
		Assert.assertSame(resource1, editor.getResource(vessel1));
	}

	@Test
	public void testDispose() {
		final String name = "name";
		final HashMapVesselEditor editor = new HashMapVesselEditor(name);

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IVessel vessel1 = context.mock(IVessel.class, "vessel-1");

		Assert.assertNull(editor.getResource(vessel1));
		Assert.assertNull(editor.getVessel(resource1));

		editor.setVesselResource(resource1, vessel1);

		Assert.assertSame(vessel1, editor.getVessel(resource1));
		Assert.assertSame(resource1, editor.getResource(vessel1));

		editor.dispose();

		Assert.assertNull(editor.getResource(vessel1));
		Assert.assertNull(editor.getVessel(resource1));

	}

}
