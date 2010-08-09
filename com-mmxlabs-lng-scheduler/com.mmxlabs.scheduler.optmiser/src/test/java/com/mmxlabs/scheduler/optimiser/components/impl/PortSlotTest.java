package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class PortSlotTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testPortSlot() {
		final String id = "id";
		final IPort port = context.mock(IPort.class);
		final ITimeWindow tw = context.mock(ITimeWindow.class);

		final PortSlot slot = new PortSlot(id, port, tw);

		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());
	}

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
	public void testGetSetID() {
		final String id = "id";
		final PortSlot slot = new PortSlot();
		Assert.assertNull(slot.getId());
		slot.setId(id);
		Assert.assertSame(id, slot.getId());
	}

	@Test
	public void testEquals() {
		final String id1 = "id1";
		final String id2 = "id2";

		final IPort port1 = context.mock(IPort.class, "port1");
		final IPort port2 = context.mock(IPort.class, "port2");

		final ITimeWindow tw1 = context.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = context.mock(ITimeWindow.class, "tw2");

		final PortSlot slot1 = new PortSlot(id1, port1, tw1);
		final PortSlot slot2 = new PortSlot(id1, port1, tw1);

		final PortSlot slot3 = new PortSlot(id2, port1, tw1);
		final PortSlot slot4 = new PortSlot(id1, port2, tw1);
		final PortSlot slot5 = new PortSlot(id1, port1, tw2);

		Assert.assertTrue(slot1.equals(slot1));
		Assert.assertTrue(slot1.equals(slot2));
		Assert.assertTrue(slot2.equals(slot1));

		Assert.assertFalse(slot1.equals(slot3));
		Assert.assertFalse(slot1.equals(slot4));
		Assert.assertFalse(slot1.equals(slot5));

		Assert.assertFalse(slot1.equals(new Object()));
	}
}
