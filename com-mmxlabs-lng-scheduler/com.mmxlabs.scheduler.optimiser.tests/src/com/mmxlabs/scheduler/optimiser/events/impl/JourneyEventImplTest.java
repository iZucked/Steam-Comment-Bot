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

public class JourneyEventImplTest {


	@Test
	public void testGetSetToPort() {

		final JourneyEventImpl event = new JourneyEventImpl();
		Assert.assertNull(event.getToPort());
		final IPort port = Mockito.mock(IPort.class);
		event.setToPort(port);
		Assert.assertSame(port, event.getToPort());
	}

	@Test
	public void testGetSetFromPort() {

		final JourneyEventImpl event = new JourneyEventImpl();
		Assert.assertNull(event.getFromPort());
		final IPort port = Mockito.mock(IPort.class);
		event.setFromPort(port);
		Assert.assertSame(port, event.getFromPort());
	}

	@Test
	public void testGetSetDistance() {
		final JourneyEventImpl event = new JourneyEventImpl();
		Assert.assertEquals(0, event.getDistance());
		event.setDistance(10);
		Assert.assertEquals(10, event.getDistance());
	}

	@Test
	public void testGetSetSpeed() {
		final JourneyEventImpl event = new JourneyEventImpl();
		Assert.assertEquals(0, event.getSpeed());
		event.setSpeed(10);
		Assert.assertEquals(10, event.getSpeed());
	}

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final FuelUnit u = FuelUnit.MT;
		final FuelUnit u2 = FuelUnit.M3;

		final long value = 100L;
		final JourneyEventImpl details = new JourneyEventImpl();
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
		final JourneyEventImpl details = new JourneyEventImpl();
		Assert.assertEquals(0, details.getFuelCost(c));
		details.setFuelCost(c, value);
		Assert.assertEquals(value, details.getFuelCost(c));
	}

	@Test
	public void testGetSetRoute() {

		final JourneyEventImpl event = new JourneyEventImpl();
		Assert.assertNull(event.getRoute());
		final String route = "route";
		event.setRoute(route);
		Assert.assertSame(route, event.getRoute());
	}

	@Test
	public void testGetSetRouteCost() {

		final JourneyEventImpl event = new JourneyEventImpl();
		Assert.assertEquals(0, event.getRouteCost());
		final long cost = 1234567890L;
		event.setRouteCost(cost);
		Assert.assertEquals(cost, event.getRouteCost());
	}
}
