package com.mmxlabs.scheduler.optimiser.events.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class JourneyEventImplTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetToPort() {

		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertNull(event.getToPort());
		final IPort port = context.mock(IPort.class);
		event.setToPort(port);
		Assert.assertSame(port, event.getToPort());
	}

	@Test
	public void testGetSetFromPort() {

		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertNull(event.getFromPort());
		final IPort port = context.mock(IPort.class);
		event.setFromPort(port);
		Assert.assertSame(port, event.getFromPort());
	}

	@Test
	public void testGetSetDistance() {
		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertEquals(0, event.getDistance());
		event.setDistance(10);
		Assert.assertEquals(10, event.getDistance());
	}
}
