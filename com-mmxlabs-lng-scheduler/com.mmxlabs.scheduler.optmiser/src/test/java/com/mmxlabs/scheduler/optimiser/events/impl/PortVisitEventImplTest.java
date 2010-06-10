package com.mmxlabs.scheduler.optimiser.events.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class PortVisitEventImplTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetPort() {

		final PortVisitEventImpl<Object> event = new PortVisitEventImpl<Object>();
		Assert.assertNull(event.getPort());
		final IPort port = context.mock(IPort.class);
		event.setPort(port);
		Assert.assertSame(port, event.getPort());
	}
}
