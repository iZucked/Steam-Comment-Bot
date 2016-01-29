/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

public class CapacityEntryTest {

	@Test
	public void testCapacityEntry() {
		final CapacityViolationType type = CapacityViolationType.FORCED_COOLDOWN;
		final long volume = 123456;
		final CapacityEntry entry = new CapacityEntry(type, volume);

		Assert.assertSame(type, entry.getType());
		Assert.assertEquals(volume, entry.getVolume());
	}
}
