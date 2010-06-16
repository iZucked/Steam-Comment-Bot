package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
@RunWith(JMock.class)
public class PortSlotTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetPort() {

		final IPort port = context.mock(IPort.class);

		final PortSlot slot = new PortSlot();
		Assert.assertNull(slot.getPort());
		slot.setPort(port);
		Assert.assertSame(port, slot.getPort());
	}

	@Test
	public void testGetSetTimeWindow() {
		final ITimeWindow window = context.mock(ITimeWindow.class);
		final PortSlot slot = new PortSlot();
		Assert.assertNull(slot.getTimeWindow());
		slot.setTimeWindow(window);
		Assert.assertSame(window, slot.getTimeWindow());
	}

	@Test
	public void testGetSetDischargeID() {
		final String id = "id";
		final PortSlot slot = new PortSlot();
		Assert.assertNull(slot.getId());
		slot.setId(id);
		Assert.assertSame(id, slot.getId());
	}

}
