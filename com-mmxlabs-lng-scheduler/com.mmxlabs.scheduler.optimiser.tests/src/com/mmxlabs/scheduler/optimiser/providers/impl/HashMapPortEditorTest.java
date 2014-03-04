/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class HashMapPortEditorTest {

	@Test
	public void testGetSetPortForElement() {

		final HashMapPortEditor editor = new HashMapPortEditor();

		final IPort port = Mockito.mock(IPort.class);
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);

		Assert.assertNull(editor.getPortForElement(element));

		editor.setPortForElement(port, element);

		Assert.assertSame(port, editor.getPortForElement(element));
	}

}
