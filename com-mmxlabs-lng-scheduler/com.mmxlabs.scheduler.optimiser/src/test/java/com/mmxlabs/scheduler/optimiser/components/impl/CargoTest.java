/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

@RunWith(JMock.class)
public class CargoTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetId() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getId());
		final String id = "id";
		cargo.setId(id);
		Assert.assertSame(id, cargo.getId());
	}

	@Test
	public void testGetSetLoadSlot() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getLoadOption());
		final ILoadSlot slot = context.mock(ILoadSlot.class);
		cargo.setLoadOption(slot);
		Assert.assertSame(slot, cargo.getLoadOption());
	}

	@Test
	public void testGetSetDischargeSlot() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getDischargeOption());
		final IDischargeSlot slot = context.mock(IDischargeSlot.class);
		cargo.setDischargeOption(slot);
		Assert.assertSame(slot, cargo.getDischargeOption());
	}
}
