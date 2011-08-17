/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

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
		final int cargoCVValue = 40;
		final ILoadPriceCalculator contract = context.mock(ILoadPriceCalculator.class);

		final LoadSlot slot = new LoadSlot(id, port, tw, minLoadVolume,
				maxLoadVolume, contract, cargoCVValue, false, true);
		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());

		Assert.assertEquals(minLoadVolume, slot.getMinLoadVolume());
		Assert.assertEquals(maxLoadVolume, slot.getMaxLoadVolume());
		Assert.assertSame(contract, slot.getLoadPriceCalculator());
		Assert.assertEquals(cargoCVValue, slot.getCargoCVValue());
		
		Assert.assertFalse(slot.isCooldownSet());
		Assert.assertTrue(slot.isCooldownForbidden());
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
	public void testGetSetPurchasePriceCurve() {
		final ILoadPriceCalculator contract = context.mock(ILoadPriceCalculator.class);
		final LoadSlot slot = new LoadSlot();
		Assert.assertNull(slot.getLoadPriceCalculator());
		slot.setLoadPriceCalculator(contract);
		Assert.assertSame(contract, slot.getLoadPriceCalculator());
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

		final ILoadPriceCalculator curve1 = context.mock(ILoadPriceCalculator.class, "curve1");
		final ILoadPriceCalculator curve2 = context.mock(ILoadPriceCalculator.class, "curve2");

		final LoadSlot slot1 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				40, false, false);
		final LoadSlot slot2 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				40, false, false);

		final LoadSlot slot3 = new LoadSlot(id2, port1, tw1, 10l, 20l, curve1,
				40, false, false);
		final LoadSlot slot4 = new LoadSlot(id1, port2, tw1, 10l, 20l, curve1,
				40, false, false);
		final LoadSlot slot5 = new LoadSlot(id1, port1, tw2, 10l, 20l, curve1,
				40, false, false);
		final LoadSlot slot6 = new LoadSlot(id1, port1, tw1, 210l, 20l, curve1,
				40, false, false);
		final LoadSlot slot7 = new LoadSlot(id1, port1, tw1, 10l, 220l, curve1,
				40, false, false);
		final LoadSlot slot8 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve2,
				40, false, false);
		final LoadSlot slot9 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				240, false, false);
		
		final LoadSlot slot10 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				240, false, true);

		final LoadSlot slot11 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				240, true, true);
		
		final LoadSlot slot12 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				240, true, false);
		
		final LoadSlot slot13 = new LoadSlot(id1, port1, tw1, 10l, 20l, curve1,
				240, true, true);
		
		Assert.assertTrue(slot1.equals(slot1));
		Assert.assertTrue(slot1.equals(slot2));
		Assert.assertTrue(slot2.equals(slot1));
		
		Assert.assertTrue(slot13.equals(slot11));
		Assert.assertTrue(slot11.equals(slot13));

		Assert.assertFalse(slot9.equals(slot10));
		Assert.assertFalse(slot9.equals(slot11));
		Assert.assertFalse(slot9.equals(slot12));
		Assert.assertFalse(slot9.equals(slot13));
		
		Assert.assertFalse(slot10.equals(slot11));
		Assert.assertFalse(slot10.equals(slot12));
		Assert.assertFalse(slot11.equals(slot12));
		Assert.assertFalse(slot12.equals(slot11));
		
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
