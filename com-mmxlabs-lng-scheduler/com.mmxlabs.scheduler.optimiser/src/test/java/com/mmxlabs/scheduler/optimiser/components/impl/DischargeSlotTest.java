/**
 * Copyright (C) Minimax Labs Ltd., 2010
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

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class DischargeSlotTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testDischargeSlot() {
		final String id = "id";
		final IPort port = context.mock(IPort.class);
		final ITimeWindow tw = context.mock(ITimeWindow.class);
		final long minDischargeVolume = 10l;
		final long maxDischargeVolume = 20l;
		final ICurve priceCurve = context.mock(ICurve.class);

		final DischargeSlot slot = new DischargeSlot(id, port, tw,
				minDischargeVolume, maxDischargeVolume, priceCurve);
		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());

		Assert.assertEquals(minDischargeVolume, slot.getMinDischargeVolume());
		Assert.assertEquals(maxDischargeVolume, slot.getMaxDischargeVolume());
		Assert.assertSame(priceCurve, slot.getSalesPriceCurve());

	}

	@Test
	public void testGetSetMinDischargeVolume() {
		final long value = 10;
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertEquals(0, slot.getMinDischargeVolume());
		slot.setMinDischargeVolume(value);
		Assert.assertEquals(value, slot.getMinDischargeVolume());
	}

	@Test
	public void testGetSetMaxDischargeVolume() {
		final long value = 10;
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertEquals(0, slot.getMaxDischargeVolume());
		slot.setMaxDischargeVolume(value);
		Assert.assertEquals(value, slot.getMaxDischargeVolume());
	}

	@Test
	public void testGetSetSalesPriceCurve() {
		final int value = 10;
		final ConstantValueCurve curve = new ConstantValueCurve(value);
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertNull(slot.getSalesPriceCurve());
		slot.setSalesPriceCurve(curve);
		Assert.assertSame(curve, slot.getSalesPriceCurve());
	}

	@Test
	public void testGetSetSalesPriceCurveAtTime() {
		final DischargeSlot slot = new DischargeSlot();
		
		final ICurve curve = context.mock(ICurve.class);
		slot.setSalesPriceCurve(curve);

		final int time = 1234;
		
		context.checking(new Expectations() {
			{
				one(curve).getValueAtPoint(time);
			}
		});

		slot.getSalesPriceAtTime(time);

		context.assertIsSatisfied();
	}

	@Test
	public void testEquals() {

		final String id1 = "id1";
		final String id2 = "id2";
		final IPort port1 = context.mock(IPort.class, "port1");
		final IPort port2 = context.mock(IPort.class, "port2");
		final ITimeWindow tw1 = context.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = context.mock(ITimeWindow.class, "tw2");
		final ICurve curve1 = context.mock(ICurve.class, "curve1");
		final ICurve curve2 = context.mock(ICurve.class, "curve2");

		final DischargeSlot slot1 = new DischargeSlot(id1, port1, tw1, 10l,
				20l, curve1);
		final DischargeSlot slot2 = new DischargeSlot(id1, port1, tw1, 10l,
				20l, curve1);

		final DischargeSlot slot3 = new DischargeSlot(id2, port1, tw1, 10l,
				20l, curve1);
		final DischargeSlot slot4 = new DischargeSlot(id1, port2, tw1, 10l,
				20l, curve1);
		final DischargeSlot slot5 = new DischargeSlot(id1, port1, tw2, 10l,
				20l, curve1);
		final DischargeSlot slot6 = new DischargeSlot(id1, port1, tw1, 210l,
				20l, curve1);
		final DischargeSlot slot7 = new DischargeSlot(id1, port1, tw1, 10l,
				220l, curve1);
		final DischargeSlot slot8 = new DischargeSlot(id1, port1, tw1, 10l,
				20l, curve2);

		Assert.assertTrue(slot1.equals(slot1));
		Assert.assertTrue(slot1.equals(slot2));
		Assert.assertTrue(slot2.equals(slot1));

		Assert.assertFalse(slot1.equals(slot3));
		Assert.assertFalse(slot1.equals(slot4));
		Assert.assertFalse(slot1.equals(slot5));
		Assert.assertFalse(slot1.equals(slot6));
		Assert.assertFalse(slot1.equals(slot7));
		Assert.assertFalse(slot1.equals(slot8));

		Assert.assertFalse(slot3.equals(slot1));
		Assert.assertFalse(slot4.equals(slot1));
		Assert.assertFalse(slot5.equals(slot1));
		Assert.assertFalse(slot6.equals(slot1));
		Assert.assertFalse(slot7.equals(slot1));
		Assert.assertFalse(slot8.equals(slot1));

		Assert.assertFalse(slot1.equals(new Object()));
	}
}
