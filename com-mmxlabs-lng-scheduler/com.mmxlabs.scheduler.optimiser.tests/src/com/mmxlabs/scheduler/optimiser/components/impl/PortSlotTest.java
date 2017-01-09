/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class PortSlotTest {

	@Test
	public void testPortSlot() {
		final String id = "id";
		final IPort port = Mockito.mock(IPort.class);
		final ITimeWindow tw = Mockito.mock(ITimeWindow.class);

		final long heelLimit = 1;
		final int heelUnitPrice = 2;
		final int heelCVValue = 3;

		HeelOptions heelOptions = new HeelOptions(heelLimit, heelCVValue, heelUnitPrice);
		final StartPortSlot slot = new StartPortSlot(id, port, tw, heelOptions);

		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());

		Assert.assertEquals(heelLimit, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(heelUnitPrice, slot.getHeelOptions().getHeelUnitPrice());
		Assert.assertEquals(heelCVValue, slot.getHeelOptions().getHeelCVValue());
	}

	@Test
	public void testGetSetPort() {

		final IPort port = Mockito.mock(IPort.class);

		final long heelLimit = 1;
		final int heelUnitPrice = 2;
		final int heelCVValue = 3;

		final HeelOptions heelOptions = new HeelOptions(heelLimit, heelCVValue, heelUnitPrice);
		final StartPortSlot slot = new StartPortSlot("id", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), heelOptions);
		Assert.assertNotNull(slot.getPort());
		slot.setPort(port);
		Assert.assertSame(port, slot.getPort());

		Assert.assertEquals(heelLimit, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(heelCVValue, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(heelUnitPrice, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testGetSetTimeWindow() {
		final ITimeWindow window = Mockito.mock(ITimeWindow.class);
		final long heelLimit = 1;
		final int heelUnitPrice = 2;
		final int heelCVValue = 3;

		final HeelOptions heelOptions = new HeelOptions(heelLimit, heelCVValue, heelUnitPrice);
		final StartPortSlot slot = new StartPortSlot("id", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), heelOptions);
		Assert.assertNotNull(slot.getTimeWindow());
		slot.setTimeWindow(window);
		Assert.assertSame(window, slot.getTimeWindow());

		Assert.assertEquals(heelLimit, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(heelCVValue, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(heelUnitPrice, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testGetSetID() {
		final @NonNull String id = "id";
		final long heelLimit = 1;
		final int heelUnitPrice = 2;
		final int heelCVValue = 3;

		final HeelOptions heelOptions = new HeelOptions(heelLimit, heelCVValue, heelUnitPrice);
		final StartPortSlot slot = new StartPortSlot(id, Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), heelOptions);

		Assert.assertSame(id, slot.getId());
		Assert.assertEquals(heelLimit, slot.getHeelOptions().getHeelLimit());
		Assert.assertEquals(heelCVValue, slot.getHeelOptions().getHeelCVValue());
		Assert.assertEquals(heelUnitPrice, slot.getHeelOptions().getHeelUnitPrice());
	}

	@Test
	public void testEquals() {
		final String id1 = "id1";
		final String id2 = "id2";

		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class, "tw2");

		HeelOptions heelOptions1 = new HeelOptions(1, 2, 3);
		HeelOptions heelOptions2 = new HeelOptions(1, 2, 4);

		final StartPortSlot slot1 = new StartPortSlot(id1, port1, tw1, heelOptions1);
		final StartPortSlot slot2 = new StartPortSlot(id1, port1, tw1, heelOptions1);

		final StartPortSlot slot3 = new StartPortSlot(id2, port1, tw1, heelOptions1);
		final StartPortSlot slot4 = new StartPortSlot(id1, port2, tw1, heelOptions1);
		final StartPortSlot slot5 = new StartPortSlot(id1, port1, tw2, heelOptions1);
		final StartPortSlot slot6 = new StartPortSlot(id1, port1, tw1, heelOptions2);

		Assert.assertTrue(slot1.equals(slot1));
		Assert.assertTrue(slot1.equals(slot2));
		Assert.assertTrue(slot2.equals(slot1));

		Assert.assertFalse(slot1.equals(slot3));
		Assert.assertFalse(slot1.equals(slot4));
		Assert.assertFalse(slot1.equals(slot5));
		Assert.assertFalse(slot1.equals(slot6));

		Assert.assertFalse(slot1.equals(new Object()));
	}
}
