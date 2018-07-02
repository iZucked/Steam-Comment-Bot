/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertSame(id, slot.getId());
		Assertions.assertSame(port, slot.getPort());
		Assertions.assertSame(tw, slot.getTimeWindow());

		Assertions.assertEquals(minLoadVolume, slot.getMinLoadVolume());
		Assertions.assertEquals(maxLoadVolume, slot.getMaxLoadVolume());
		Assertions.assertSame(contract, slot.getLoadPriceCalculator());
		Assertions.assertEquals(cargoCVValue, slot.getCargoCVValue());

		Assertions.assertFalse(slot.isCooldownSet());
		Assertions.assertTrue(slot.isCooldownForbidden());
	}

	@Test
	public void testSetVolumes_1a() {
		final LoadOption slot = instantiateSlot();

		int cv1 = 20_000_000;
		// CV Before vol limits
		slot.setCargoCVValue(cv1);
		slot.setVolumeLimits(true, 5_000_000, 10_000_000);

		Assertions.assertEquals(5_000_000, slot.getMinLoadVolume());
		Assertions.assertEquals(10_000_000, slot.getMaxLoadVolume());
		Assertions.assertEquals(20 * 5_000_000, slot.getMinLoadVolumeMMBTU());
		Assertions.assertEquals(20 * 10_000_000, slot.getMaxLoadVolumeMMBTU());
	}

	@Test
	public void testSetVolumes_1b() {
		final LoadOption slot = instantiateSlot();

		int cv1 = 20_000_000;
		slot.setVolumeLimits(true, 5_000_000, 10_000_000);
		// CV After vol limits
		slot.setCargoCVValue(cv1);

		Assertions.assertEquals(5_000_000, slot.getMinLoadVolume());
		Assertions.assertEquals(10_000_000, slot.getMaxLoadVolume());
		Assertions.assertEquals(20 * 5_000_000, slot.getMinLoadVolumeMMBTU());
		Assertions.assertEquals(20 * 10_000_000, slot.getMaxLoadVolumeMMBTU());
	}

	@Test
	public void testSetVolumes_2() {
		final LoadOption slot = instantiateSlot();

		slot.setVolumeLimits(true, 5_000_000, Long.MAX_VALUE);
		int cv1 = 20_000_000;
		slot.setCargoCVValue(cv1);

		Assertions.assertEquals(5_000_000, slot.getMinLoadVolume());
		Assertions.assertEquals(Long.MAX_VALUE, slot.getMaxLoadVolume());
		Assertions.assertEquals(20 * 5_000_000, slot.getMinLoadVolumeMMBTU());
		Assertions.assertEquals(Long.MAX_VALUE, slot.getMaxLoadVolumeMMBTU());
	}

	private LoadSlot instantiateSlot() {
		return new LoadSlot("slot", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 10, false, true);
	}

	@Test
	public void testGetSetPurchasePriceCurve() {
		final ILoadPriceCalculator contract = Mockito.mock(ILoadPriceCalculator.class);
		final LoadSlot slot = instantiateSlot();
		Assertions.assertNotNull(slot.getLoadPriceCalculator());
		slot.setLoadPriceCalculator(contract);
		Assertions.assertSame(contract, slot.getLoadPriceCalculator());
	}

	@Test
	public void testGetSetCargoCVValue() {
		final int value = 20;
		final LoadSlot slot = instantiateSlot();
		Assertions.assertEquals(10, slot.getCargoCVValue());
		slot.setCargoCVValue(value);
		Assertions.assertEquals(value, slot.getCargoCVValue());
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

		final NotionalLoadSlot slot1 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 40, false, false);
		final NotionalLoadSlot slot2 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 40, false, false);

		final NotionalLoadSlot slot3 = new NotionalLoadSlot(id2, port1, tw1, true, 10L, 20L, curve1, 40, false, false);
		final NotionalLoadSlot slot4 = new NotionalLoadSlot(id1, port2, tw1, true, 10L, 20L, curve1, 40, false, false);
		final NotionalLoadSlot slot5 = new NotionalLoadSlot(id1, port1, tw2, true, 10L, 20L, curve1, 40, false, false);
		final NotionalLoadSlot slot6 = new NotionalLoadSlot(id1, port1, tw1, true, 210L, 20L, curve1, 40, false, false);
		final NotionalLoadSlot slot7 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 220L, curve1, 40, false, false);
		final NotionalLoadSlot slot8 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve2, 40, false, false);
		final NotionalLoadSlot slot9 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, false, false);

		final NotionalLoadSlot slot10 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, false, true);

		final NotionalLoadSlot slot11 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, true, true);

		final NotionalLoadSlot slot12 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, true, false);

		final NotionalLoadSlot slot13 = new NotionalLoadSlot(id1, port1, tw1, true, 10L, 20L, curve1, 240, true, true);
		final NotionalLoadSlot slot14 = new NotionalLoadSlot(id1, port1, tw1, false, 10L, 20L, curve1, 40, false, false);

		Assertions.assertTrue(slot1.equals(slot1));
		Assertions.assertTrue(slot1.equals(slot2));
		Assertions.assertTrue(slot2.equals(slot1));

		Assertions.assertTrue(slot13.equals(slot11));
		Assertions.assertTrue(slot11.equals(slot13));

		Assertions.assertFalse(slot9.equals(slot10));
		Assertions.assertFalse(slot9.equals(slot11));
		Assertions.assertFalse(slot9.equals(slot12));
		Assertions.assertFalse(slot9.equals(slot13));

		Assertions.assertFalse(slot10.equals(slot11));
		Assertions.assertFalse(slot10.equals(slot12));
		Assertions.assertFalse(slot11.equals(slot12));
		Assertions.assertFalse(slot12.equals(slot11));

		Assertions.assertFalse(slot1.equals(slot3));
		Assertions.assertFalse(slot1.equals(slot4));
		Assertions.assertFalse(slot1.equals(slot5));
		Assertions.assertFalse(slot1.equals(slot6));
		Assertions.assertFalse(slot1.equals(slot7));
		Assertions.assertFalse(slot1.equals(slot8));
		Assertions.assertFalse(slot1.equals(slot9));

		Assertions.assertFalse(slot3.equals(slot1));
		Assertions.assertFalse(slot4.equals(slot1));
		Assertions.assertFalse(slot5.equals(slot1));
		Assertions.assertFalse(slot6.equals(slot1));
		Assertions.assertFalse(slot7.equals(slot1));
		Assertions.assertFalse(slot8.equals(slot1));
		Assertions.assertFalse(slot9.equals(slot1));

		Assertions.assertFalse(slot14.equals(slot1));

		Assertions.assertFalse(slot1.equals(new Object()));
	}

}
