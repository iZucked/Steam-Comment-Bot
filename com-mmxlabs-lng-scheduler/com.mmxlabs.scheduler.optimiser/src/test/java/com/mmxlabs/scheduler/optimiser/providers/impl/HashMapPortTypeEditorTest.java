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

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@RunWith(JMock.class)
public class HashMapPortTypeEditorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testHashMapPortTypeEditor() {

		final String name = "name";
		final HashMapPortTypeEditor editor = new HashMapPortTypeEditor(name);
		Assert.assertSame(name, editor.getName());

		final ISequenceElement element = context.mock(ISequenceElement.class);
		Assert.assertSame(PortType.Unknown, editor.getPortType(element));

		editor.setPortType(element, PortType.Waypoint);

		Assert.assertSame(PortType.Waypoint, editor.getPortType(element));

		editor.dispose();

		Assert.assertSame(PortType.Unknown, editor.getPortType(element));

	}
}