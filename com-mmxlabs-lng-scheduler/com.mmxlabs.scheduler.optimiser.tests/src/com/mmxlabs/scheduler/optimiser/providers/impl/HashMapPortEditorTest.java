/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class HashMapPortEditorTest {

	@Test
	public void testGetSetPortForElement1() {

		final HashMapElementPortEditor editor = new HashMapElementPortEditor();

		final ISequenceElement element = Mockito.mock(ISequenceElement.class);

		Assertions.assertThrows(IllegalArgumentException.class, () -> editor.getPortForElement(element));

	}

	@Test
	public void testGetSetPortForElement2() {

		final HashMapElementPortEditor editor = new HashMapElementPortEditor();

		final IPort port = Mockito.mock(IPort.class);
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);

		editor.setPortForElement(port, element);

		Assertions.assertSame(port, editor.getPortForElement(element));
	}

}
