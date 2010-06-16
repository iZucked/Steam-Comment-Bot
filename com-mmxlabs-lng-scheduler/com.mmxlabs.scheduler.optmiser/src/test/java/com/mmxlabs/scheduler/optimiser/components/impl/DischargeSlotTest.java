package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class DischargeSlotTest {

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
	public void testGetSetSalesPrice() {
		final long value = 10;
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertEquals(0, slot.getSalesPrice());
		slot.setSalesPrice(value);
		Assert.assertEquals(value, slot.getSalesPrice());
	}

}
