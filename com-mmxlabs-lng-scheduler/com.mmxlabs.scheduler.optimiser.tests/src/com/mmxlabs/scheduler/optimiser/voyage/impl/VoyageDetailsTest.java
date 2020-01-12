/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class VoyageDetailsTest {

	@Test
	public void testGetSetFuelConsumption() {

		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final FuelComponent c = FuelComponent.Base;
		final FuelUnit u = FuelUnit.MT;
		final FuelUnit u2 = FuelUnit.M3;
		final BaseFuel baseFuel = new BaseFuel(indexingContext, "FUEL");

		final long value = 100L;

		FuelKey fk1 = new FuelKey(c, u, baseFuel);
		FuelKey fk2 = new FuelKey(c, u2, baseFuel);

		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assertions.assertEquals(0, details.getFuelConsumption(fk1));
		Assertions.assertEquals(0, details.getFuelConsumption(fk2));
		details.setFuelConsumption(fk1, value);
		Assertions.assertEquals(value, details.getFuelConsumption(fk1));
		Assertions.assertEquals(0, details.getFuelConsumption(fk2));
	}

	@Test
	public void testGetSetIdleTime() {
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assertions.assertEquals(0, details.getIdleTime());
		details.setIdleTime(value);
		Assertions.assertEquals(value, details.getIdleTime());
	}

	@Test
	public void testGetSetOptions() {

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));

		final VoyageDetails details = new VoyageDetails(options);
		Assertions.assertSame(options, details.getOptions());
		details.setOptions(options);
		Assertions.assertSame(options, details.getOptions());
	}

	@Test
	public void testGetSetSpeed() {
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assertions.assertEquals(0, details.getSpeed());
		details.setSpeed(value);
		Assertions.assertEquals(value, details.getSpeed());
	}

	@Test
	public void testGetSetTravelTime() {
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assertions.assertEquals(0, details.getTravelTime());
		details.setTravelTime(value);
		Assertions.assertEquals(value, details.getTravelTime());
	}

	@Test
	public void testGetSetFuelCost() {

		final FuelComponent c = FuelComponent.Base;
		final IBaseFuel bf = IBaseFuel.LNG;
		final int value = 100;
		final VoyageDetails details = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));
		Assertions.assertEquals(0, details.getFuelUnitPrice(c));
		details.setFuelUnitPrice(c, value);
		Assertions.assertEquals(value, details.getFuelUnitPrice(c));
	}

	@Test
	public void testEquals() {

		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final VoyageOptions options1 = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		final VoyageOptions options2 = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		// Set as different so Equals test will fail when comparing these objects
		options1.setAvailableTime(1);
		options2.setAvailableTime(2);

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;

		final FuelUnit unit1 = FuelUnit.MT;
		final FuelUnit unit2 = FuelUnit.M3;

		final IBaseFuel baseFuel1 = new BaseFuel(indexingContext, "TEST");
		final IBaseFuel baseFuel2 = IBaseFuel.LNG;

		final VoyageDetails details1 = make(1, 2, 3, options1, fuel1, unit1, baseFuel1, 5, 10);
		final VoyageDetails details2 = make(1, 2, 3, options1, fuel1, unit1, baseFuel1, 5, 10);
		final VoyageDetails details3 = make(21, 2, 3, options1, fuel1, unit1, baseFuel1, 5, 10);
		final VoyageDetails details4 = make(1, 22, 3, options1, fuel1, unit1, baseFuel1, 5, 10);
		final VoyageDetails details5 = make(1, 2, 23, options1, fuel1, unit1, baseFuel1, 5, 10);
		// TODO: make use of details6 variable or remove it
		final VoyageDetails details6 = make(1, 2, 3, options1, fuel1, unit1, baseFuel1, 5, 10);
		final VoyageDetails details7 = make(1, 2, 3, options2, fuel1, unit1, baseFuel1, 5, 10);
		final VoyageDetails details8 = make(1, 2, 3, options1, fuel2, unit2, baseFuel2, 5, 10);
		// final VoyageDetails details9 = make(1, 2, 3, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details10 = make(1, 2, 3, options1, fuel1, unit1, baseFuel1, 25, 10);
		final VoyageDetails details11 = make(1, 2, 3, options1, fuel1, unit1, baseFuel1, 5, 15);

		Assertions.assertTrue(details1.equals(details1));
		Assertions.assertTrue(details1.equals(details2));
		Assertions.assertTrue(details2.equals(details1));

		Assertions.assertFalse(details1.equals(details3));
		Assertions.assertFalse(details1.equals(details4));
		Assertions.assertFalse(details1.equals(details5));

		Assertions.assertFalse(details1.equals(details7));
		Assertions.assertFalse(details1.equals(details8));
		// Assertions.assertFalse(details1.equals(details9));
		Assertions.assertFalse(details1.equals(details10));
		Assertions.assertFalse(details1.equals(details11));

		Assertions.assertFalse(details3.equals(details1));
		Assertions.assertFalse(details4.equals(details1));
		Assertions.assertFalse(details5.equals(details1));
		Assertions.assertFalse(details7.equals(details1));
		Assertions.assertFalse(details8.equals(details1));
		// Assertions.assertFalse(details9.equals(details1));
		Assertions.assertFalse(details10.equals(details1));
		Assertions.assertFalse(details11.equals(details1));

		Assertions.assertFalse(details1.equals(new Object()));
	}

	VoyageDetails make(final int idleTime, final int travelTime, final int speed, final @NonNull VoyageOptions options, final @NonNull FuelComponent fuel, final @NonNull FuelUnit unit,
			final @NonNull IBaseFuel baseFuel, final long consumption, final int unitPrice) {

		final VoyageDetails d = new VoyageDetails(new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class)));

		d.setIdleTime(idleTime);
		d.setTravelTime(travelTime);
		d.setSpeed(speed);
		// d.setStartTime(startTime);
		d.setOptions(options);

		d.setFuelConsumption(new FuelKey(fuel, unit, baseFuel), consumption);
		d.setFuelUnitPrice(fuel, unitPrice);

		return d;
	}
}
