/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl;

import org.junit.Assert;
import org.junit.Test;

public class CapacityEntryTest {

	@Test
	public void testCapacityEntry() {
		final String type = "type";
		final long volume = 123456;
		final CapacityEntry entry = new CapacityEntry(type, volume);

		Assert.assertSame(type, entry.getType());
		Assert.assertEquals(volume, entry.getVolume());
	}
}
