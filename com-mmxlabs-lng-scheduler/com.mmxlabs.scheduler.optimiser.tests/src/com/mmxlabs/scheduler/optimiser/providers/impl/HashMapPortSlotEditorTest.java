/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class HashMapPortSlotEditorTest {

	@Test
	public void testGetSetPortSlot() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);

		editor.setPortSlot(element, portSlot);

		Assertions.assertSame(portSlot, editor.getPortSlot(element));
		Assertions.assertSame(element, editor.getElement(portSlot));

	}

	@Test
	public void testGetUnknownSequenceElement() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);

		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getPortSlot(element));
	}

	@Test
	public void testGetUnknownPortSlot() {

		final HashMapPortSlotEditor editor = new HashMapPortSlotEditor();
		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);

		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getElement(portSlot));
	}
}
