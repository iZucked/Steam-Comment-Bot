/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@SuppressWarnings("null")
public class HashMapPortSlotEditorTest {

	@Test
	public void testGetSetPortSlot() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);

		editor.setPortSlot(element, portSlot);

		Assert.assertSame(portSlot, editor.getPortSlot(element));
		Assert.assertSame(element, editor.getElement(portSlot));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUnknownSequenceElement() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);

		editor.getPortSlot(element);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUnknownPortSlot() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);

		editor.getElement(portSlot);
	}
}
