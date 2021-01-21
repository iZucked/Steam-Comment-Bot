/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertNotNull(portSlots);
		Assertions.assertEquals(1, portSlots.size());
		Assertions.assertSame(slot, portSlots.get(0));
	}
}
