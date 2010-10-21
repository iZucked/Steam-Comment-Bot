/**
 * Copyright (C) Minimax Labs Ltd., 2010
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
public class LoadSlotTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testLoadSlot() {
		final String id = "id";
		final IPort port = context.mock(IPort.class);
		final ITimeWindow tw = context.mock(ITimeWindow.class);
		final long minLoadVolume = 10l;
		final long maxLoadVolume = 20l;
		final int purchasePrice = 30;
		final int cargoCVValue = 40;

		final LoadSlot slot = new LoadSlot(id, port, tw, minLoadVolume,
				maxLoadVolume, purchasePrice, cargoCVValue);
		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());

		Assert.assertEquals(minLoadVolume, slot.getMinLoadVolume());
		Assert.assertEquals(maxLoadVolume, slot.getMaxLoadVolume());
		Assert.assertEquals(purchasePrice, slot.getPurchasePrice());
		Assert.assertEquals(cargoCVValue, slot.getCargoCVValue());
	}

	@Test
	public void testGetSetMinLoadVolume() {
		final long value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getMinLoadVolume());
		slot.setMinLoadVolume(value);
		Assert.assertEquals(value, slot.getMinLoadVolume());
	}

	@Test
	public void testGetSetMaxLoadVolume() {
		final long value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getMaxLoadVolume());
		slot.setMaxLoadVolume(value);
		Assert.assertEquals(value, slot.getMaxLoadVolume());
	}

	@Test
	public void testGetSetPurchasePrice() {
		final int value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getPurchasePrice());
		slot.setPurchasePrice(value);
		Assert.assertEquals(value, slot.getPurchasePrice());
	}

	@Test
	public void testGetSetCargoCVValue() {
		final int value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getCargoCVValue());
		slot.setCargoCVValue(value);
		Assert.assertEquals(value, slot.getCargoCVValue());
	}
	
	@Test
	public void testEquals() {
	
		final String id1 = "id1";
		final String id2 = "id2";
		final IPort port1 = context.mock(IPort.class, "port1");
		final IPort port2 = context.mock(IPort.class, "port2");
		final ITimeWindow tw1 = context.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = context.mock(ITimeWindow.class, "tw2");
		
		
		final LoadSlot slot1 = new LoadSlot(id1, port1, tw1, 10l, 20l, 30, 40);
		final LoadSlot slot2 = new LoadSlot(id1, port1, tw1, 10l, 20l, 30, 40);
		
		final LoadSlot slot3 = new LoadSlot(id2, port1, tw1, 10l, 20l, 30, 40);
		final LoadSlot slot4 = new LoadSlot(id1, port2, tw1, 10l, 20l, 30, 40);
		final LoadSlot slot5 = new LoadSlot(id1, port1, tw2, 10l, 20l, 30, 40);
		final LoadSlot slot6 = new LoadSlot(id1, port1, tw1, 210l, 20l, 30, 40);
		final LoadSlot slot7 = new LoadSlot(id1, port1, tw1, 10l, 220l, 30, 40);
		final LoadSlot slot8 = new LoadSlot(id1, port1, tw1, 10l, 20l, 230, 40);
		final LoadSlot slot9 = new LoadSlot(id1, port1, tw1, 10l, 20l, 30, 240);
	
		
		Assert.assertTrue(slot1.equals(slot1));
		Assert.assertTrue(slot1.equals(slot2));
		Assert.assertTrue(slot2.equals(slot1));
		
		Assert.assertFalse(slot1.equals(slot3));
		Assert.assertFalse(slot1.equals(slot4));
		Assert.assertFalse(slot1.equals(slot5));
		Assert.assertFalse(slot1.equals(slot6));
		Assert.assertFalse(slot1.equals(slot7));
		Assert.assertFalse(slot1.equals(slot8));
		Assert.assertFalse(slot1.equals(slot9));
	
		Assert.assertFalse(slot3.equals(slot1));
		Assert.assertFalse(slot4.equals(slot1));
		Assert.assertFalse(slot5.equals(slot1));
		Assert.assertFalse(slot6.equals(slot1));
		Assert.assertFalse(slot7.equals(slot1));
		Assert.assertFalse(slot8.equals(slot1));
		Assert.assertFalse(slot9.equals(slot1));
		
		Assert.assertFalse(slot1.equals(new Object()));
	}
	
}
