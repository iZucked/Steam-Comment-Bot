/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class HashMapPortSlotEditorTest {

	@Test
	public void testGetSetPortSlot() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);

		Assert.assertNull(editor.getPortSlot(element));
		Assert.assertNull(editor.getElement(portSlot));

		editor.setPortSlot(element, portSlot);

		Assert.assertSame(portSlot, editor.getPortSlot(element));
		Assert.assertSame(element, editor.getElement(portSlot));

	}
}
