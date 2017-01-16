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
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

public class LoadSlotTest {

	@Test
	public void testLoadSlot() {
		final String id = "id";
		final IPort port = Mockito.mock(IPort.class);
		final ITimeWindow tw = Mockito.mock(ITimeWindow.class);
		final long minLoadVolume = 10L;
		final long maxLoadVolume = 20L;
		final int cargoCVValue = 40;
		final ILoadPriceCalculator contract = Mockito.mock(ILoadPriceCalculator.class);

		final LoadSlot slot = new LoadSlot(id, port, tw, true, minLoadVolume, maxLoadVolume, contract, cargoCVValue, false, true);
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
	public void testSetVolumes_1a() {
		final LoadOption slot = instantiateSlot();

		int cv1 = 20_000_000;
		// CV Before vol limits
		slot.setCargoCVValue(cv1);
		slot.setVolumeLimits(true, 5_000_000, 10_000_000);

		Assert.assertEquals(5_000_000, slot.getMinLoadVolume());
		Assert.assertEquals(10_000_000, slot.getMaxLoadVolume());
		Assert.assertEquals(20 * 5_000_000, slot.getMinLoadVolumeMMBTU());
		Assert.assertEquals(20 * 10_000_000, slot.getMaxLoadVolumeMMBTU());
	}

	@Test
	public void testSetVolumes_1b() {
		final LoadOption slot = instantiateSlot();

		int cv1 = 20_000_000;
		slot.setVolumeLimits(true, 5_000_000, 10_000_000);
		// CV After vol limits
		slot.setCargoCVValue(cv1);

		Assert.assertEquals(5_000_000, slot.getMinLoadVolume());
		Assert.assertEquals(10_000_000, slot.getMaxLoadVolume());
		Assert.assertEquals(20 * 5_000_000, slot.getMinLoadVolumeMMBTU());
		Assert.assertEquals(20 * 10_000_000, slot.getMaxLoadVolumeMMBTU());
	}

	@Test
	public void testSetVolumes_2() {
		final LoadOption slot = instantiateSlot();

		slot.setVolumeLimits(true, 5_000_000, Long.MAX_VALUE);
		int cv1 = 20_000_000;
		slot.setCargoCVValue(cv1);

		Assert.assertEquals(5_000_000, slot.getMinLoadVolume());
		Assert.assertEquals(Long.MAX_VALUE, slot.getMaxLoadVolume());
		Assert.assertEquals(20 * 5_000_000, slot.getMinLoadVolumeMMBTU());
		Assert.assertEquals(Long.MAX_VALUE, slot.getMaxLoadVolumeMMBTU());
	}

	private LoadSlot instantiateSlot() {
		return new LoadSlot("slot", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 10, false, true);
	}

	@Test
	public void testGetSetPurchasePriceCurve() {
		final ILoadPriceCalculator contract = Mockito.mock(ILoadPriceCalculator.class);
		final LoadSlot slot = instantiateSlot();
		Assert.assertNotNull(slot.getLoadPriceCalculator());
		slot.setLoadPriceCalculator(contract);
		Assert.assertSame(contract, slot.getLoadPriceCalculator());
	}

	@Test
	public void testGetSetCargoCVValue() {
		final int value = 20;
		final LoadSlot slot = instantiateSlot();
		Assert.assertEquals(10, slot.getCargoCVValue());
		slot.setCargoCVValue(value);
		Assert.assertEquals(value, slot.getCargoCVValue());
	}

	@Test
	public void testEquals() {

		final String id1 = "id1";
		final String id2 = "id2";
		final IPort port1 = Mockito.mock(IPort.class, "port1");
		final IPort port2 = Mockito.mock(IPort.class, "port2");
		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class, "tw1");
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class, "tw2");

		final ILoadPriceCalculator curve1 = Mockito.mock(ILoadPriceCalculator.class, "curve1");
		final ILoadPriceCalculator curve2 = Mockito.mock(ILoadPriceCalculator.class, "curve2");

		final LoadSlot slot1 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 40, false, false);
		final LoadSlot slot2 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 40, false, false);

		final LoadSlot slot3 = new LoadSlot(id2, port1, tw1, true, 10L, 20L, curve1, 40, false, false);
		final LoadSlot slot4 = new LoadSlot(id1, port2, tw1, true, 10L, 20L, curve1, 40, false, false);
		final LoadSlot slot5 = new LoadSlot(id1, port1, tw2, true, 10L, 20L, curve1, 40, false, false);
		final LoadSlot slot6 = new LoadSlot(id1, port1, tw1, true, 210L, 20L, curve1, 40, false, false);
		final LoadSlot slot7 = new LoadSlot(id1, port1, tw1, true, 10L, 220L, curve1, 40, false, false);
		final LoadSlot slot8 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve2, 40, false, false);
		final LoadSlot slot9 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, false, false);

		final LoadSlot slot10 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, false, true);

		final LoadSlot slot11 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, true, true);

		final LoadSlot slot12 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, true, false);

		final LoadSlot slot13 = new LoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, true, true);
		final LoadSlot slot14 = new LoadSlot(id1, port1, tw1, false, 10L, 20L, curve1, 40, false, false);

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

		Assert.assertFalse(slot14.equals(slot1));

		Assert.assertFalse(slot1.equals(new Object()));
	}

}
