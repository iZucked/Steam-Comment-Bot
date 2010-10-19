/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class HashMapPortTypeEditorTest {

	@Test
	public void testHashMapPortTypeEditor() {

		final String name = "name";
		final HashMapPortTypeEditor<Object> editor = new HashMapPortTypeEditor<Object>(
				name);
		Assert.assertSame(name, editor.getName());

		final Object element = new Object();
		Assert.assertSame(PortType.Unknown, editor.getPortType(element));

		editor.setPortType(element, PortType.Waypoint);

		Assert.assertSame(PortType.Waypoint, editor.getPortType(element));

		editor.dispose();

		Assert.assertSame(PortType.Unknown, editor.getPortType(element));

	}
}