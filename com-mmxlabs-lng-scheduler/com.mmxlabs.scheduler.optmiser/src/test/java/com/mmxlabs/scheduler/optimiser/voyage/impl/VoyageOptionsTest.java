package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

@RunWith(JMock.class)
public class VoyageOptionsTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetAvailableTime() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions();
		Assert.assertEquals(0, options.getAvailableTime());
		options.setAvailableTime(value);
		Assert.assertEquals(value, options.getAvailableTime());
	}

	@Test
	public void testGetSetDistance() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions();
		Assert.assertEquals(0, options.getDistance());
		options.setDistance(value);
		Assert.assertEquals(value, options.getDistance());
	}

	@Test
	public void testGetSetFromPortSlot() {
		final IPortSlot slot = context.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions();
		Assert.assertNull(options.getFromPortSlot());
		options.setFromPortSlot(slot);
		Assert.assertSame(slot, options.getFromPortSlot());
	}

	@Test
	public void testGetSetToPortSlot() {
		final IPortSlot slot = context.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions();
		Assert.assertNull(options.getToPortSlot());
		options.setToPortSlot(slot);
		Assert.assertSame(slot, options.getToPortSlot());
	}

	@Test
	public void testGetSetNBOSpeed() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions();
		Assert.assertEquals(0, options.getNBOSpeed());
		options.setNBOSpeed(value);
		Assert.assertEquals(value, options.getNBOSpeed());
	}

	@Test
	public void testGetSetRoute() {

		String route = "route";
		final VoyageOptions options = new VoyageOptions();
		Assert.assertNull(options.getRoute());
		options.setRoute(route);
		Assert.assertSame(route, options.getRoute());
	}

	@Test
	public void testGetSetVessel() {
		final IVessel vessel = context.mock(IVessel.class);

		final VoyageOptions options = new VoyageOptions();
		Assert.assertNull(options.getVessel());
		options.setVessel(vessel);
		Assert.assertSame(vessel, options.getVessel());

	}

	@Test
	public void testGetVesselState() {
		final VesselState state = VesselState.Laden;

		final VoyageOptions options = new VoyageOptions();
		Assert.assertNull(options.getVesselState());
		options.setVesselState(state);
		Assert.assertSame(state, options.getVesselState());

	}

	@Test
	public void testUseFBOForSupplement() {
		final VoyageOptions options = new VoyageOptions();
		Assert.assertFalse(options.useFBOForSupplement());
		options.setUseFBOForSupplement(true);
		Assert.assertTrue(options.useFBOForSupplement());
	}

	@Test
	public void testUseNBOForIdle() {
		final VoyageOptions options = new VoyageOptions();
		Assert.assertFalse(options.useNBOForIdle());
		options.setUseNBOForIdle(true);
		Assert.assertTrue(options.useNBOForIdle());

	}

	@Test
	public void testUseNBOForTravel() {
		final VoyageOptions options = new VoyageOptions();
		Assert.assertFalse(options.useNBOForTravel());
		options.setUseNBOForTravel(true);
		Assert.assertTrue(options.useNBOForTravel());

	}
}
