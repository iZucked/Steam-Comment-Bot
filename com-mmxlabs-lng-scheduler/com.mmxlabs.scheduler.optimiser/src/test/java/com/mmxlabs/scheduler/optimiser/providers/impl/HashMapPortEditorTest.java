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
import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class HashMapPortEditorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testHashMapPortEditor() {
		final String name = "name";
		final HashMapPortEditor editor = new HashMapPortEditor(name);
		Assert.assertSame(name, editor.getName());
	}

	@Test
	public void testGetSetPortForElement() {

		final String name = "name";
		final HashMapPortEditor editor = new HashMapPortEditor(name);

		final IPort port = context.mock(IPort.class);
		final ISequenceElement element = context.mock(ISequenceElement.class);

		Assert.assertNull(editor.getPortForElement(element));

		editor.setPortForElement(port, element);

		Assert.assertSame(port, editor.getPortForElement(element));
	}

	@Test
	public void testDispose() {

		final String name = "name";
		final HashMapPortEditor editor = new HashMapPortEditor(name);

		final IPort port = context.mock(IPort.class);
		final ISequenceElement element = context.mock(ISequenceElement.class);

		Assert.assertNull(editor.getPortForElement(element));

		editor.setPortForElement(port, element);

		Assert.assertSame(port, editor.getPortForElement(element));

		editor.dispose();

		Assert.assertNull(editor.getPortForElement(element));
	}
}
