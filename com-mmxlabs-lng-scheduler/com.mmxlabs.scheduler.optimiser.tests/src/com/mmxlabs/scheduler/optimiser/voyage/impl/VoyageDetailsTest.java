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
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class VoyageDetailsTest {

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final FuelUnit u = FuelUnit.MT;
		final FuelUnit u2 = FuelUnit.M3;

		final long value = 100L;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assert.assertEquals(0, details.getFuelConsumption(c, u));
		Assert.assertEquals(0, details.getFuelConsumption(c, u2));
		details.setFuelConsumption(c, u, value);
		Assert.assertEquals(value, details.getFuelConsumption(c, u));
		Assert.assertEquals(0, details.getFuelConsumption(c, u2));
	}

	@Test
	public void testGetSetIdleTime() {
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assert.assertEquals(0, details.getIdleTime());
		details.setIdleTime(value);
		Assert.assertEquals(value, details.getIdleTime());
	}

	@Test
	public void testGetSetOptions() {

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));

		final VoyageDetails details = new VoyageDetails(options);
		Assert.assertSame(options, details.getOptions());
		details.setOptions(options);
		Assert.assertSame(options, details.getOptions());
	}

	@Test
	public void testGetSetSpeed() {
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assert.assertEquals(0, details.getSpeed());
		details.setSpeed(value);
		Assert.assertEquals(value, details.getSpeed());
	}

	@Test
	public void testGetSetTravelTime() {
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assert.assertEquals(0, details.getTravelTime());
		details.setTravelTime(value);
		Assert.assertEquals(value, details.getTravelTime());
	}

	@Test
	public void testGetSetFuelCost() {

		final FuelComponent c = FuelComponent.Base;
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assert.assertEquals(0, details.getFuelUnitPrice(c));
		details.setFuelUnitPrice(c, value);
		Assert.assertEquals(value, details.getFuelUnitPrice(c));
	}

	@Test
	public void testEquals() {

		final VoyageOptions options1 = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		final VoyageOptions options2 = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		// Set as different so Equals test will fail when comparing these objects
		options1.setAvailableTime(1);
		options2.setAvailableTime(2);

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;

		final FuelUnit unit1 = FuelUnit.MT;
		final FuelUnit unit2 = FuelUnit.M3;

		final VoyageDetails details1 = make(1, 2, 3, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details2 = make(1, 2, 3, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details3 = make(21, 2, 3, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details4 = make(1, 22, 3, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details5 = make(1, 2, 23, options1, fuel1, unit1, 5, 10);
		// TODO: make use of details6 variable or remove it
		final VoyageDetails details6 = make(1, 2, 3, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details7 = make(1, 2, 3, options2, fuel1, unit1, 5, 10);
		final VoyageDetails details8 = make(1, 2, 3, options1, fuel2, unit1, 5, 10);
		final VoyageDetails details9 = make(1, 2, 3, options1, fuel1, unit2, 5, 10);
		final VoyageDetails details10 = make(1, 2, 3, options1, fuel1, unit1, 25, 10);
		final VoyageDetails details11 = make(1, 2, 3, options1, fuel1, unit1, 5, 15);

		Assert.assertTrue(details1.equals(details1));
		Assert.assertTrue(details1.equals(details2));
		Assert.assertTrue(details2.equals(details1));

		Assert.assertFalse(details1.equals(details3));
		Assert.assertFalse(details1.equals(details4));
		Assert.assertFalse(details1.equals(details5));

		Assert.assertFalse(details1.equals(details7));
		Assert.assertFalse(details1.equals(details8));
		Assert.assertFalse(details1.equals(details9));
		Assert.assertFalse(details1.equals(details10));
		Assert.assertFalse(details1.equals(details11));

		Assert.assertFalse(details3.equals(details1));
		Assert.assertFalse(details4.equals(details1));
		Assert.assertFalse(details5.equals(details1));
		Assert.assertFalse(details7.equals(details1));
		Assert.assertFalse(details8.equals(details1));
		Assert.assertFalse(details9.equals(details1));
		Assert.assertFalse(details10.equals(details1));
		Assert.assertFalse(details11.equals(details1));

		Assert.assertFalse(details1.equals(new Object()));
	}

	VoyageDetails make(final int idleTime, final int travelTime, final int speed, final VoyageOptions options, final FuelComponent fuel, final FuelUnit unit, final long consumption,
			final int unitPrice) {

		final VoyageDetails d = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));

		d.setIdleTime(idleTime);
		d.setTravelTime(travelTime);
		d.setSpeed(speed);
		// d.setStartTime(startTime);
		d.setOptions(options);

		d.setFuelConsumption(fuel, unit, consumption);
		d.setFuelUnitPrice(fuel, unitPrice);

		return d;
	}
}
