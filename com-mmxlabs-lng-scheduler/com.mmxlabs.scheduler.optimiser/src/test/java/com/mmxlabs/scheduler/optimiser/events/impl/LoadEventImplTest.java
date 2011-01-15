/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import org.junit.Assert;
import org.junit.Test;

public class LoadEventImplTest {

	@Test
	public void testGetLoadVolume() {
		final long value = 100l;
		final LoadEventImpl<Object> event = new LoadEventImpl<Object>();
		Assert.assertEquals(0, event.getLoadVolume());
		event.setLoadVolume(value);
		Assert.assertEquals(value, event.getLoadVolume());
	}

	@Test
	public void testGetPurchasePrice() {
		final long value = 100l;
		final LoadEventImpl<Object> event = new LoadEventImpl<Object>();
		Assert.assertEquals(0, event.getPurchasePrice());
		event.setPurchasePrice(value);
		Assert.assertEquals(value, event.getPurchasePrice());
	}
}
