/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@RunWith(JMock.class)
public class CargoTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSlots() {

		final ILoadSlot slot = context.mock(ILoadSlot.class);
		final Cargo cargo = new Cargo(Lists.<IPortSlot> newArrayList(slot));
		List<IPortSlot> portSlots = cargo.getPortSlots();
		Assert.assertNotNull(portSlots);
		Assert.assertEquals(1, portSlots.size());
		Assert.assertSame(slot, portSlots.get(0));
	}
}
