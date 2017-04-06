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
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class PortSlotTest {

	@Test
	public void testGetSetPort() {

		final IPort port = Mockito.mock(IPort.class);

		final IHeelOptionSupplier heelOptions = Mockito.mock(IHeelOptionSupplier.class);
		final StartPortSlot slot = new StartPortSlot("id", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), heelOptions);
		Assert.assertNotSame(port, slot.getPort());
		slot.setPort(port);
		Assert.assertSame(port, slot.getPort());

	}

	@Test
	public void testGetSetTimeWindow() {
		final ITimeWindow window = Mockito.mock(ITimeWindow.class);
		final IHeelOptionSupplier heelOptions = Mockito.mock(IHeelOptionSupplier.class);
		final StartPortSlot slot = new StartPortSlot("id", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), heelOptions);
		Assert.assertNotNull(slot.getTimeWindow());
		slot.setTimeWindow(window);
		Assert.assertSame(window, slot.getTimeWindow());
	}

	@Test
	public void testGetSetID() {
		final @NonNull String id = "id";

		final IHeelOptionSupplier heelOptions = Mockito.mock(IHeelOptionSupplier.class);
		final StartPortSlot slot = new StartPortSlot(id, Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), heelOptions);

		Assert.assertSame(id, slot.getId());
	}

	@Test
	public void testEquals() {
		final String id1 = "id1";
		final String id2 = "id2";

		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");

		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class, "tw2");

		final IHeelPriceCalculator heelPriceCalculator1 = Mockito.mock(IHeelPriceCalculator.class, "price1");
		final IHeelPriceCalculator heelPriceCalculator2 = Mockito.mock(IHeelPriceCalculator.class, "price2");

		HeelOptionSupplier heelOptions1 = new HeelOptionSupplier(1, 2, 3, heelPriceCalculator1);

		final StartPortSlot slot1 = new StartPortSlot(id1, port1, tw1, heelOptions1);
		final StartPortSlot slot2 = new StartPortSlot(id1, port1, tw1, heelOptions1);

		final StartPortSlot slot3 = new StartPortSlot(id2, port1, tw1, heelOptions1);
		final StartPortSlot slot4 = new StartPortSlot(id1, port2, tw1, heelOptions1);
		final StartPortSlot slot5 = new StartPortSlot(id1, port1, tw2, heelOptions1);

		// This falls back to the PortSlot doEquals.
		Assert.assertTrue(slot1.doEquals(slot1));
		Assert.assertTrue(slot1.doEquals(slot2));
		Assert.assertTrue(slot2.doEquals(slot1));

		Assert.assertFalse(slot1.doEquals(slot3));
		Assert.assertFalse(slot1.doEquals(slot4));
		Assert.assertFalse(slot1.doEquals(slot5));

		Assert.assertFalse(slot1.doEquals(new Object()));
	}
}
