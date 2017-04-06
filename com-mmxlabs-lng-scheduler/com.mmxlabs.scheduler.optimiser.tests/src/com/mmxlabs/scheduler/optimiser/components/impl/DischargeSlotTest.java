/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

public class DischargeSlotTest {

	@Test
	public void testDischargeSlot() {
		final String id = "id";
		final IPort port = Mockito.mock(IPort.class);
		final ITimeWindow tw = Mockito.mock(ITimeWindow.class);
		final long minDischargeVolume = 10L;
		final long maxDischargeVolume = 20L;

		final long minCvValue = 30L;
		final long maxCvValue = 40L;
		final ISalesPriceCalculator calculator = Mockito.mock(ISalesPriceCalculator.class);

		final DischargeSlot slot = new DischargeSlot(id, port, tw, true, minDischargeVolume, maxDischargeVolume, calculator, minCvValue, maxCvValue);
		Assert.assertSame(id, slot.getId());
		Assert.assertSame(port, slot.getPort());
		Assert.assertSame(tw, slot.getTimeWindow());

		Assert.assertEquals(minDischargeVolume, slot.getMinDischargeVolume(-1));
		Assert.assertEquals(maxDischargeVolume, slot.getMaxDischargeVolume(-1));
		Assert.assertSame(calculator, slot.getDischargePriceCalculator());

	}

	@Test
	public void testSetVolumes_1() {
		final DischargeSlot slot = instantiateSlot();

		slot.setVolumeLimits(true, 5_000_000, 10_000_000);
		int cv1 = 20_000_000;

		Assert.assertEquals(5_000_000, slot.getMinDischargeVolume(cv1));
		Assert.assertEquals(10_000_000, slot.getMaxDischargeVolume(cv1));
		Assert.assertEquals(20 * 5_000_000, slot.getMinDischargeVolumeMMBTU(cv1));
		Assert.assertEquals(20 * 10_000_000, slot.getMaxDischargeVolumeMMBTU(cv1));
	}

	@Test
	public void testSetVolumes_2() {
		final DischargeSlot slot = instantiateSlot();

		slot.setVolumeLimits(true, 5_000_000, Long.MAX_VALUE);
		int cv1 = 20_000_000;

		Assert.assertEquals(5_000_000, slot.getMinDischargeVolume(cv1));
		Assert.assertEquals(Long.MAX_VALUE, slot.getMaxDischargeVolume(cv1));
		Assert.assertEquals(20 * 5_000_000, slot.getMinDischargeVolumeMMBTU(cv1));
		Assert.assertEquals(Long.MAX_VALUE, slot.getMaxDischargeVolumeMMBTU(cv1));
	}

	private DischargeSlot instantiateSlot() {
		return new DischargeSlot("slot", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);
	}

	@Test
	public void testGetSetSalesPriceCurve() {
		final ISalesPriceCalculator curve = Mockito.mock(ISalesPriceCalculator.class);
		final DischargeSlot slot = instantiateSlot();
		Assert.assertNotNull(slot.getDischargePriceCalculator());
		slot.setDischargePriceCalculator(curve);
		Assert.assertSame(curve, slot.getDischargePriceCalculator());
	}

	@Test
	public void testEquals() {

		final String id1 = "id1";
		final String id2 = "id2";
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");
		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class, "tw2");
		final ISalesPriceCalculator curve1 = Mockito.mock(ISalesPriceCalculator.class, "curve1");
		final ISalesPriceCalculator curve2 = Mockito.mock(ISalesPriceCalculator.class, "curve2");

		final NotionalDischargeSlot slot1 = new NotionalDischargeSlot(id1, port1, tw1, true, 10L, 20L, curve1, 30L, 40L);
		final NotionalDischargeSlot slot2 = new NotionalDischargeSlot(id1, port1, tw1, true, 10L, 20L, curve1, 30L, 40L);

		final NotionalDischargeSlot slot3 = new NotionalDischargeSlot(id2, port1, tw1, true, 10L, 20L, curve1, 30L, 40L);
		final NotionalDischargeSlot slot4 = new NotionalDischargeSlot(id1, port2, tw1, true, 10L, 20L, curve1, 30L, 40L);
		final NotionalDischargeSlot slot5 = new NotionalDischargeSlot(id1, port1, tw2, true, 10L, 20L, curve1, 30L, 40L);
		final NotionalDischargeSlot slot6 = new NotionalDischargeSlot(id1, port1, tw1, true, 210L, 20L, curve1, 30L, 40L);
		final NotionalDischargeSlot slot7 = new NotionalDischargeSlot(id1, port1, tw1, true, 10L, 220L, curve1, 30L, 40L);
		final NotionalDischargeSlot slot8 = new NotionalDischargeSlot(id1, port1, tw1, true, 10L, 20L, curve2, 30L, 40L);
		final NotionalDischargeSlot slot9 = new NotionalDischargeSlot(id1, port1, tw1, false, 10L, 20L, curve1, 30L, 40L);

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
		Assert.assertFalse(slot9.equals(slot1));

		Assert.assertFalse(slot1.equals(new Object()));
	}
}
