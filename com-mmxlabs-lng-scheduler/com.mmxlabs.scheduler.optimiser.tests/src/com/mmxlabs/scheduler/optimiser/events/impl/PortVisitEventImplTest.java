/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class PortVisitEventImplTest {


	@Test
	public void testGetSetPort() {

		final PortVisitEventImpl event = new PortVisitEventImpl();
		Assert.assertNull(event.getPortSlot());
		final IPortSlot portSlot = Mockito.mock(IPortSlot.class);
		event.setPortSlot(portSlot);
		Assert.assertSame(portSlot, event.getPortSlot());
	}
}
