/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
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

	@Test(expected = UnsupportedOperationException.class)
	public void testGetPortCost() {
		final PortDetails details = new PortDetails();
		details.getPortCost(null);
	}

	@Test
	public void testGetSetCapacityViolationType() {
		final CapacityViolationType cvt = CapacityViolationType.FORCED_COOLDOWN;
		final long value = 100l;
		final PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getCapacityViolation(cvt));
		details.setCapacityViolation(cvt, value);
		Assert.assertEquals(value, details.getCapacityViolation(cvt));
	}
	
	@Test
	public void testEquals() {

		final IPortSlot slot1 = context.mock(IPortSlot.class, "s1");
		final IPortSlot slot2 = context.mock(IPortSlot.class, "s2");

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;
		
		final CapacityViolationType cvt1 = CapacityViolationType.FORCED_COOLDOWN;
		final CapacityViolationType cvt2 = CapacityViolationType.VESSEL_CAPACITY;

		final PortDetails details1 = make(1, slot1, fuel1, 5, cvt1, 100);
		final PortDetails details2 = make(1, slot1, fuel1, 5, cvt1, 100);

		final PortDetails details3 = make(21, slot1, fuel1, 5, cvt1, 100);
		final PortDetails details4 = make(1, slot2, fuel1, 5, cvt1, 100);
		final PortDetails details5 = make(1, slot1, fuel2, 5, cvt1, 100);
		final PortDetails details6 = make(1, slot1, fuel1, 25, cvt1, 100);
		final PortDetails details7 = make(1, slot1, fuel1, 25, cvt2, 100);
		final PortDetails details8 = make(1, slot1, fuel1, 25, cvt1, 200);

		Assert.assertTrue(details1.equals(details1));
		Assert.assertTrue(details1.equals(details2));
		Assert.assertTrue(details2.equals(details1));

		Assert.assertFalse(details1.equals(details3));
		Assert.assertFalse(details1.equals(details4));
		Assert.assertFalse(details1.equals(details5));
		Assert.assertFalse(details1.equals(details6));
		Assert.assertFalse(details1.equals(details7));
		Assert.assertFalse(details1.equals(details8));

		Assert.assertFalse(details3.equals(details1));
		Assert.assertFalse(details4.equals(details1));
		Assert.assertFalse(details5.equals(details1));
		Assert.assertFalse(details6.equals(details1));
		Assert.assertFalse(details7.equals(details1));
		Assert.assertFalse(details8.equals(details1));

		Assert.assertFalse(details1.equals(new Object()));
	}

	PortDetails make(final int duration, final IPortSlot portSlot, final FuelComponent fuel, final long consumption, CapacityViolationType cvt, final long cvtQty) {

		final PortDetails d = new PortDetails();
		d.setOptions(new PortOptions());
		
		d.getOptions().setVisitDuration(duration);
		d.getOptions().setPortSlot(portSlot);
		d.setFuelConsumption(fuel, consumption);
		d.setCapacityViolation(cvt, cvtQty);
		return d;
	}

}
