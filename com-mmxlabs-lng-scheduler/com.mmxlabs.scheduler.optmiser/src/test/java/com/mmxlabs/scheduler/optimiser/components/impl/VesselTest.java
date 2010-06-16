package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class VesselTest {

	@Test
	public void testGetSetName() {
		final Vessel vessel = new Vessel();
		Assert.assertNull(vessel.getName());
		final String name = "name";
		vessel.setName(name);
		Assert.assertSame(name, vessel.getName());
	}

	@Test
	public void testGetSetLadenNBORate() {
		final long value = 10;
		final Vessel vessel = new Vessel();
		Assert.assertEquals(0, vessel.getLadenNBORate());
		vessel.setLadenNBORate(value);
		Assert.assertEquals(value, vessel.getLadenNBORate());

	}

	@Test
	public void testGetSetBallastNBORate() {
		final long value = 10;
		final Vessel vessel = new Vessel();
		Assert.assertEquals(0, vessel.getBallastNBORate());
		vessel.setBallastNBORate(value);
		Assert.assertEquals(value, vessel.getBallastNBORate());
	}

	@Test
	public void testGetSetCargoCapacity() {
		final long value = 10;
		final Vessel vessel = new Vessel();
		Assert.assertEquals(0, vessel.getCargoCapacity());
		vessel.setCargoCapacity(value);
		Assert.assertEquals(value, vessel.getCargoCapacity());
	}

	@Test
	public void testGetSetMinSpeed() {
		final int value = 10;
		final Vessel vessel = new Vessel();
		Assert.assertEquals(0, vessel.getMinSpeed());
		vessel.setMinSpeed(value);
		Assert.assertEquals(value, vessel.getMinSpeed());
	}

	@Test
	public void testGetSetMaxnSpeed() {
		final int value = 10;
		final Vessel vessel = new Vessel();
		Assert.assertEquals(0, vessel.getMaxSpeed());
		vessel.setMaxSpeed(value);
		Assert.assertEquals(value, vessel.getMaxSpeed());
	}

}
