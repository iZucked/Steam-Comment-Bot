/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import org.junit.Assert;
import org.junit.Test;

public class DischargeEventImplTest {

	@Test
	public void testGetDischargeVolume() {
		final long value = 100l;
		final DischargeEventImpl event = new DischargeEventImpl();
		Assert.assertEquals(0, event.getDischargeVolume());
		event.setDischargeVolume(value);
		Assert.assertEquals(value, event.getDischargeVolume());
	}

	@Test
	public void testGetSalesPrice() {
		final long value = 100l;
		final DischargeEventImpl event = new DischargeEventImpl();
		Assert.assertEquals(0, event.getSalesPrice());
		event.setSalesPrice(value);
		Assert.assertEquals(value, event.getSalesPrice());
	}

}
