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
public class JourneyEventImplTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetToPort() {

		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertNull(event.getToPort());
		final IPort port = context.mock(IPort.class);
		event.setToPort(port);
		Assert.assertSame(port, event.getToPort());
	}

	@Test
	public void testGetSetFromPort() {

		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertNull(event.getFromPort());
		final IPort port = context.mock(IPort.class);
		event.setFromPort(port);
		Assert.assertSame(port, event.getFromPort());
	}

	@Test
	public void testGetSetDistance() {
		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertEquals(0, event.getDistance());
		event.setDistance(10);
		Assert.assertEquals(10, event.getDistance());
	}
	
	@Test
	public void testGetSetSpeed() {
		final JourneyEventImpl<Object> event = new JourneyEventImpl<Object>();
		Assert.assertEquals(0, event.getSpeed());
		event.setSpeed(10);
		Assert.assertEquals(10, event.getSpeed());
	}
	
	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final JourneyEventImpl<Object> details = new JourneyEventImpl<Object>();
		Assert.assertEquals(0, details.getFuelConsumption(c));
		details.setFuelConsumption(c, value);
		Assert.assertEquals(value, details.getFuelConsumption(c));
	}

	@Test
	public void testGetSetFuelCost() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final JourneyEventImpl<Object> details = new JourneyEventImpl<Object>();
		Assert.assertEquals(0, details.getFuelCost(c));
		details.setFuelCost(c, value);
		Assert.assertEquals(value, details.getFuelCost(c));
	}
	
}
