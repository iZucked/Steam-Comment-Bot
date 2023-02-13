/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public class PortOptionsTest {

	@Test
	public void testGetSetVisitDuration() {

		final int value = 100;
		final PortOptions options = new PortOptions(Mockito.mock(IPortSlot.class));
		Assertions.assertEquals(0, options.getVisitDuration());
		options.setVisitDuration(value);
		Assertions.assertEquals(value, options.getVisitDuration());
	}

	@Test
	public void testGetSetPortSlot() {
		final IPortSlot slot = Mockito.mock(IPortSlot.class);

		final PortOptions options = new PortOptions(slot);
		Assertions.assertSame(slot, options.getPortSlot());
	}

	@Test
	public void testGetSetVessel() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final IPortSlot slot = Mockito.mock(IPortSlot.class);
		final PortOptions options = new PortOptions(slot);
		Assertions.assertNull(options.getVessel());
		options.setVessel(vessel);
		Assertions.assertSame(vessel, options.getVessel());
	}

}
