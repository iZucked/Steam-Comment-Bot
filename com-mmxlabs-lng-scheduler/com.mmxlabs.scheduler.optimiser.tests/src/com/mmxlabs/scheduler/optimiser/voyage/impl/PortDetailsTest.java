/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

public class PortDetailsTest {

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100L;
		final PortDetails details = new PortDetails();
		Assert.assertEquals(0, details.getFuelConsumption(c));
		details.setFuelConsumption(c, value);
		Assert.assertEquals(value, details.getFuelConsumption(c));
	}

	@Test
	public void testEquals() {

		final IPortSlot slot1 = Mockito.mock(IPortSlot.class, "s1");
		final IPortSlot slot2 = Mockito.mock(IPortSlot.class, "s2");

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;

		final CapacityViolationType cvt1 = CapacityViolationType.FORCED_COOLDOWN;
		final CapacityViolationType cvt2 = CapacityViolationType.VESSEL_CAPACITY;

		final PortDetails details1 = make(1, slot1, fuel1, 5);
		final PortDetails details2 = make(1, slot1, fuel1, 5);

		final PortDetails details3 = make(21, slot1, fuel1, 5);
		final PortDetails details4 = make(1, slot2, fuel1, 5);
		final PortDetails details5 = make(1, slot1, fuel2, 5);
		final PortDetails details6 = make(1, slot1, fuel1, 25);

		Assert.assertTrue(details1.equals(details1));
		Assert.assertTrue(details1.equals(details2));
		Assert.assertTrue(details2.equals(details1));

		Assert.assertFalse(details1.equals(details3));
		Assert.assertFalse(details1.equals(details4));
		Assert.assertFalse(details1.equals(details5));
		Assert.assertFalse(details1.equals(details6));

		Assert.assertFalse(details3.equals(details1));
		Assert.assertFalse(details4.equals(details1));
		Assert.assertFalse(details5.equals(details1));
		Assert.assertFalse(details6.equals(details1));

		Assert.assertFalse(details1.equals(new Object()));
	}

	PortDetails make(final int duration, final IPortSlot portSlot, final FuelComponent fuel, final long consumption) {

		final PortDetails d = new PortDetails();
		d.setOptions(new PortOptions());

		d.getOptions().setVisitDuration(duration);
		d.getOptions().setPortSlot(portSlot);
		d.setFuelConsumption(fuel, consumption);
		return d;
	}

}
