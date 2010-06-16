package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class LoadSlotTest {

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
		final long value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getPurchasePrice());
		slot.setPurchasePrice(value);
		Assert.assertEquals(value, slot.getPurchasePrice());
	}
}
