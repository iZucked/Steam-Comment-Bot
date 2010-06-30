package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;

@RunWith(JMock.class)
public class VoyageDetailsTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetFuelConsumption() {

		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		Assert.assertEquals(0, details.getFuelConsumption(c));
		details.setFuelConsumption(c, value);
		Assert.assertEquals(value, details.getFuelConsumption(c));
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
}
