/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class IdleEventImplTest {


	@Test
	public void testGetSetPort() {

		final IdleEventImpl event = new IdleEventImpl();
		Assert.assertNull(event.getPort());
		final IPort port = Mockito.mock(IPort.class);
		event.setPort(port);
		Assert.assertSame(port, event.getPort());
	}

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final FuelUnit u = FuelUnit.MT;
		final FuelUnit u2 = FuelUnit.M3;

		final long value = 100L;
		final IdleEventImpl details = new IdleEventImpl();
		Assert.assertEquals(0, details.getFuelConsumption(c, u));
		Assert.assertEquals(0, details.getFuelConsumption(c, u2));
		details.setFuelConsumption(c, u, value);
		Assert.assertEquals(value, details.getFuelConsumption(c, u));
		Assert.assertEquals(0, details.getFuelConsumption(c, u2));
	}

	@Test
	public void testGetSetFuelCost() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100L;
		final IdleEventImpl details = new IdleEventImpl();
		Assert.assertEquals(0, details.getFuelCost(c));
		details.setFuelCost(c, value);
		Assert.assertEquals(value, details.getFuelCost(c));
	}

}
