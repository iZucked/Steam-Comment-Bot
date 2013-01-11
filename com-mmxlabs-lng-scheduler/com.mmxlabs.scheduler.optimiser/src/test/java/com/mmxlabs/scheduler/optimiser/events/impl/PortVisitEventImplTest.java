/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@RunWith(JMock.class)
public class PortVisitEventImplTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetPort() {

		final PortVisitEventImpl event = new PortVisitEventImpl();
		Assert.assertNull(event.getPortSlot());
		final IPortSlot portSlot = context.mock(IPortSlot.class);
		event.setPortSlot(portSlot);
		Assert.assertSame(portSlot, event.getPortSlot());
	}
}
