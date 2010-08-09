package com.mmxlabs.scheduler.optimiser.events.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

@RunWith(JMock.class)
public class IdleEventImplTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetPort() {

		final IdleEventImpl<Object> event = new IdleEventImpl<Object>();
		Assert.assertNull(event.getPort());
		final IPort port = context.mock(IPort.class);
		event.setPort(port);
		Assert.assertSame(port, event.getPort());
	}

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final IdleEventImpl<Object> details = new IdleEventImpl<Object>();
		Assert.assertEquals(0, details.getFuelConsumption(c));
		details.setFuelConsumption(c, value);
		Assert.assertEquals(value, details.getFuelConsumption(c));
	}

	@Test
	public void testGetSetFuelCost() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final IdleEventImpl<Object> details = new IdleEventImpl<Object>();
		Assert.assertEquals(0, details.getFuelCost(c));
		details.setFuelCost(c, value);
		Assert.assertEquals(value, details.getFuelCost(c));
	}

}
