package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;

@RunWith(JMock.class)
public class VoyageDetailsTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final FuelUnit u = FuelUnit.MT;
		final FuelUnit u2 = FuelUnit.M3;

		final long value = 100l;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getFuelConsumption(c, u));
		Assert.assertEquals(0, details.getFuelConsumption(c, u2));
		details.setFuelConsumption(c, u, value);
		Assert.assertEquals(value, details.getFuelConsumption(c, u));
		Assert.assertEquals(0, details.getFuelConsumption(c, u2));
	}

	@Test
	public void testGetSetIdleTime() {
		final int value = 100;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getIdleTime());
		details.setIdleTime(value);
		Assert.assertEquals(value, details.getIdleTime());
	}

	@Test
	public void testGetSetOptions() {

		final IVoyageOptions options = context.mock(IVoyageOptions.class);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertNull(details.getOptions());
		details.setOptions(options);
		Assert.assertSame(options, details.getOptions());
	}

	@Test
	public void testGetSetSpeed() {
		final int value = 100;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getSpeed());
		details.setSpeed(value);
		Assert.assertEquals(value, details.getSpeed());
	}

	@Test
	public void testGetSetTravelTime() {
		final int value = 100;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getTravelTime());
		details.setTravelTime(value);
		Assert.assertEquals(value, details.getTravelTime());
	}

	@Test
	public void testGetSetStartTime() {
		final int value = 100;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getStartTime());
		details.setStartTime(value);
		Assert.assertEquals(value, details.getStartTime());
	}

	@Test
	public void testGetSetFuelCost() {

		final FuelComponent c = FuelComponent.Base;
		final int value = 100;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getFuelUnitPrice(c));
		details.setFuelUnitPrice(c, value);
		Assert.assertEquals(value, details.getFuelUnitPrice(c));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testEquals() {

		final IVoyageOptions options1 = context
				.mock(IVoyageOptions.class, "o1");
		final IVoyageOptions options2 = context
				.mock(IVoyageOptions.class, "o2");

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;
		
		final FuelUnit unit1 = FuelUnit.MT;
		final FuelUnit unit2 = FuelUnit.M3;

		final VoyageDetails details1 = make(1, 2, 3, 4, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details2 = make(1, 2, 3, 4, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details3 = make(21, 2, 3, 4, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details4 = make(1, 22, 3, 4, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details5 = make(1, 2, 23, 4, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details6 = make(1, 2, 3, 24, options1, fuel1, unit1, 5, 10);
		final VoyageDetails details7 = make(1, 2, 3, 4, options2, fuel1, unit1, 5, 10);
		final VoyageDetails details8 = make(1, 2, 3, 4, options1, fuel2, unit1, 5, 10);
		final VoyageDetails details9 = make(1, 2, 3, 4, options1, fuel1, unit2, 5, 10);
		final VoyageDetails details10 = make(1, 2, 3, 4, options1, fuel1, unit1, 25, 10);
		final VoyageDetails details11 = make(1, 2, 3, 4, options1, fuel1, unit1, 5, 15);

		Assert.assertTrue(details1.equals(details1));
		Assert.assertTrue(details1.equals(details2));
		Assert.assertTrue(details2.equals(details1));

		Assert.assertFalse(details1.equals(details3));
		Assert.assertFalse(details1.equals(details4));
		Assert.assertFalse(details1.equals(details5));
		Assert.assertFalse(details1.equals(details6));
		Assert.assertFalse(details1.equals(details7));
		Assert.assertFalse(details1.equals(details8));
		Assert.assertFalse(details1.equals(details9));
		Assert.assertFalse(details1.equals(details10));
		Assert.assertFalse(details1.equals(details11));

		Assert.assertFalse(details3.equals(details1));
		Assert.assertFalse(details4.equals(details1));
		Assert.assertFalse(details5.equals(details1));
		Assert.assertFalse(details6.equals(details1));
		Assert.assertFalse(details7.equals(details1));
		Assert.assertFalse(details8.equals(details1));
		Assert.assertFalse(details9.equals(details1));
		Assert.assertFalse(details10.equals(details1));
		Assert.assertFalse(details11.equals(details1));

		Assert.assertFalse(details1.equals(new Object()));
	}

	<T> VoyageDetails<T> make(final int idleTime, final int travelTime,
			final int speed, final int startTime, final IVoyageOptions options,
			final FuelComponent fuel, final FuelUnit unit,
			final long consumption, final int unitPrice) {

		final VoyageDetails<T> d = new VoyageDetails<T>();

		d.setIdleTime(idleTime);
		d.setTravelTime(travelTime);
		d.setSpeed(speed);
		d.setStartTime(startTime);
		d.setOptions(options);

		d.setFuelConsumption(fuel, unit, consumption);
		d.setFuelUnitPrice(fuel, unitPrice);

		return d;
	}
}
