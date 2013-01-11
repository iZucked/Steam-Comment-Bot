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
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@RunWith(JMock.class)
public class HashMapPortSlotEditorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testHashMapPortSlotEditor() {

		final String name = "name";
		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor(name);
		Assert.assertSame(name, editor.getName());
	}

	@Test
	public void testGetSetPortSlot() {

		final String name = "name";
		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor(name);
		final ISequenceElement element = context.mock(ISequenceElement.class);
		final IPortSlot portSlot = context.mock(IPortSlot.class);

		Assert.assertNull(editor.getPortSlot(element));
		Assert.assertNull(editor.getElement(portSlot));

		editor.setPortSlot(element, portSlot);

		Assert.assertSame(portSlot, editor.getPortSlot(element));
		Assert.assertSame(element, editor.getElement(portSlot));

	}

	@Test
	public void testDispose() {

		final String name = "name";
		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor(name);
		final ISequenceElement element = context.mock(ISequenceElement.class);
		final IPortSlot portSlot = context.mock(IPortSlot.class);

		Assert.assertNull(editor.getPortSlot(element));
		Assert.assertNull(editor.getElement(portSlot));

		editor.setPortSlot(element, portSlot);

		Assert.assertSame(portSlot, editor.getPortSlot(element));
		Assert.assertSame(element, editor.getElement(portSlot));

		editor.dispose();

		Assert.assertNull(editor.getPortSlot(element));
		Assert.assertNull(editor.getElement(portSlot));
	}

}
