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
		final PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getFuelConsumption(c));
		details.setFuelConsumption(c, value);
		Assert.assertEquals(value, details.getFuelConsumption(c));
	}

	@Test
	public void testGetSetVisitDuration() {

		final int value = 100;
		final PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getVisitDuration());
		details.setVisitDuration(value);
		Assert.assertEquals(value, details.getVisitDuration());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetPortCost() {
		final PortDetails details = new PortDetails();
		details.getPortCost(null);
	}

	@Test
	public void testGetSetPortSlot() {
		final IPortSlot slot = context.mock(IPortSlot.class);

		final PortDetails details = new PortDetails();
		Assert.assertNull(details.getPortSlot());
		details.setPortSlot(slot);
		Assert.assertSame(slot, details.getPortSlot());
	}

	@Test
	public void testGetSetStartTime() {

		final int value = 100;
		final PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getStartTime());
		details.setStartTime(value);
		Assert.assertEquals(value, details.getStartTime());
	}

	@Test
	public void testEquals() {

		final IPortSlot slot1 = context.mock(IPortSlot.class, "s1");
		final IPortSlot slot2 = context.mock(IPortSlot.class, "s2");

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;

		final PortDetails details1 = make(1, 2, slot1, fuel1, 5);
		final PortDetails details2 = make(1, 2, slot1, fuel1, 5);

		final PortDetails details3 = make(21, 2, slot1, fuel1, 5);
		final PortDetails details4 = make(1, 22, slot1, fuel1, 5);
		final PortDetails details5 = make(1, 2, slot2, fuel1, 5);
		final PortDetails details6 = make(1, 2, slot1, fuel2, 5);
		final PortDetails details7 = make(1, 2, slot1, fuel1, 25);

		Assert.assertTrue(details1.equals(details1));
		Assert.assertTrue(details1.equals(details2));
		Assert.assertTrue(details2.equals(details1));

		Assert.assertFalse(details1.equals(details3));
		Assert.assertFalse(details1.equals(details4));
		Assert.assertFalse(details1.equals(details5));
		Assert.assertFalse(details1.equals(details6));
		Assert.assertFalse(details1.equals(details7));

		Assert.assertFalse(details3.equals(details1));
		Assert.assertFalse(details4.equals(details1));
		Assert.assertFalse(details5.equals(details1));
		Assert.assertFalse(details6.equals(details1));
		Assert.assertFalse(details7.equals(details1));

		Assert.assertFalse(details1.equals(new Object()));
	}

	PortDetails make(final int duration, final int startTime,
			final IPortSlot portSlot, final FuelComponent fuel,
			final long consumption) {

		final PortDetails d = new PortDetails();

		d.setVisitDuration(duration);
		d.setStartTime(startTime);
		d.setPortSlot(portSlot);
		d.setFuelConsumption(fuel, consumption);

		return d;
	}

}
