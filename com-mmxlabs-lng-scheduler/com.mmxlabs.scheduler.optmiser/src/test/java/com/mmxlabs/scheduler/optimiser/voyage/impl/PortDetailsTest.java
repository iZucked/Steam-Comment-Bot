package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

@RunWith(JMock.class)
public class PortDetailsTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getFuelConsumption(c));
		details.setFuelConsumption(c, value);
		Assert.assertEquals(value, details.getFuelConsumption(c));
	}

	@Test
	public void testGetSetVisitDuration() {

		final int value = 100;
		PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getVisitDuration());
		details.setVisitDuration(value);
		Assert.assertEquals(value, details.getVisitDuration());

	}

	@Test(expected=UnsupportedOperationException.class)
	public void testGetPortCost() {
		PortDetails details = new PortDetails();
		details.getPortCost(null);
	}

	@Test
	public void testGetSetPortSlot() {
		final IPortSlot slot = context.mock(IPortSlot.class);

		PortDetails details = new PortDetails();
		Assert.assertNull(details.getPortSlot());
		details.setPortSlot(slot);
		Assert.assertSame(slot, details.getPortSlot());

	}
}
