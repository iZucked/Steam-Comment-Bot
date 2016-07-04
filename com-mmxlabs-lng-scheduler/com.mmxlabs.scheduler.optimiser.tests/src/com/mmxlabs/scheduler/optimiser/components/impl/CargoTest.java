/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class CargoTest {

	@Test
	public void testGetSlots() {

		final ILoadSlot slot = Mockito.mock(ILoadSlot.class);
		final Cargo cargo = new Cargo(Lists.<IPortSlot> newArrayList(slot));
		List<IPortSlot> portSlots = cargo.getPortSlots();
		Assert.assertNotNull(portSlots);
		Assert.assertEquals(1, portSlots.size());
		Assert.assertSame(slot, portSlots.get(0));
	}
}
