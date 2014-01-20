/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class HashMapPortTypeEditorTest {

	@Test
	public void testHashMapPortTypeEditor() {

		final HashMapPortTypeEditor editor = new HashMapPortTypeEditor();

		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		Assert.assertSame(PortType.Unknown, editor.getPortType(element));

		editor.setPortType(element, PortType.Waypoint);

		Assert.assertSame(PortType.Waypoint, editor.getPortType(element));

		editor.dispose();

		Assert.assertSame(PortType.Unknown, editor.getPortType(element));

	}
}