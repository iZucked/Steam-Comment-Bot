/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
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

		final StartPortSlot slot = new StartPortSlot(id, port, tw, 1, 2, 3);

		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());

		Assert.assertEquals(1, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(2, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(3, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testGetSetPort() {

		final IPort port = context.mock(IPort.class);

		final StartPortSlot slot = new StartPortSlot(1, 2, 3);
		Assert.assertNull(slot.getPort());
		slot.setPort(port);
		Assert.assertSame(port, slot.getPort());

		Assert.assertEquals(1, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(2, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(3, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testGetSetTimeWindow() {
		final ITimeWindow window = context.mock(ITimeWindow.class);
		final StartPortSlot slot = new StartPortSlot(1, 2, 3);
		Assert.assertNull(slot.getTimeWindow());
		slot.setTimeWindow(window);
		Assert.assertSame(window, slot.getTimeWindow());

		Assert.assertEquals(1, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(2, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(3, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testGetSetID() {
		final String id = "id";
		final StartPortSlot slot = new StartPortSlot(1, 2, 3);
		Assert.assertNull(slot.getId());
		slot.setId(id);

		Assert.assertSame(id, slot.getId());
		Assert.assertEquals(1, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(2, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(3, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testEquals() {
		final String id1 = "id1";
		final String id2 = "id2";

		final IPort port1 = context.mock(IPort.class, "port1");
		final IPort port2 = context.mock(IPort.class, "port2");

		final ITimeWindow tw1 = context.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = context.mock(ITimeWindow.class, "tw2");

		final StartPortSlot slot1 = new StartPortSlot(id1, port1, tw1, 1, 2, 3);
		final StartPortSlot slot2 = new StartPortSlot(id1, port1, tw1, 1, 2, 3);

		final StartPortSlot slot3 = new StartPortSlot(id2, port1, tw1, 1, 2, 3);
		final StartPortSlot slot4 = new StartPortSlot(id1, port2, tw1, 1, 2, 3);
		final StartPortSlot slot5 = new StartPortSlot(id1, port1, tw2, 1, 2, 3);

		Assert.assertTrue(slot1.equals(slot1));
		Assert.assertTrue(slot1.equals(slot2));
		Assert.assertTrue(slot2.equals(slot1));

		Assert.assertFalse(slot1.equals(slot3));
		Assert.assertFalse(slot1.equals(slot4));
		Assert.assertFalse(slot1.equals(slot5));

		Assert.assertFalse(slot1.equals(new Object()));
	}
}
