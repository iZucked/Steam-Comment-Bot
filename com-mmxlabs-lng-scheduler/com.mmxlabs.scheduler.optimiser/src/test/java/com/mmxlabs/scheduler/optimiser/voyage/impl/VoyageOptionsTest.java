/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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

	@Test
	public void testEquals() {

		IVessel vessel1 = context.mock(IVessel.class, "v1");
		IVessel vessel2 = context.mock(IVessel.class, "v2");
		IPortSlot portSlot1 = context.mock(IPortSlot.class, "s1");
		IPortSlot portSlot2 = context.mock(IPortSlot.class, "s2");
		IPortSlot portSlot3 = context.mock(IPortSlot.class, "s3");
		IPortSlot portSlot4 = context.mock(IPortSlot.class, "s4");

		String route1 = "r1";
		String route2 = "r2";

		VesselState state1 = VesselState.Laden;
		VesselState state2 = VesselState.Ballast;

		VoyageOptions options1 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				true, true, true, route1, state1);
		
		VoyageOptions options2 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				true, true, true, route1, state1);
		
		VoyageOptions options3 = make(21, 2, vessel1, portSlot1, portSlot2, 3,
				true, true, true, route1, state1);
		VoyageOptions options4 = make(1, 22, vessel1, portSlot1, portSlot2, 3,
				true, true, true, route1, state1);
		VoyageOptions options5 = make(1, 2, vessel2, portSlot1, portSlot2, 3,
				true, true, true, route1, state1);
		VoyageOptions options6 = make(1, 2, vessel1, portSlot3, portSlot2, 3,
				true, true, true, route1, state1);
		VoyageOptions options7 = make(1, 2, vessel1, portSlot1, portSlot4, 3,
				true, true, true, route1, state1);
		VoyageOptions options8 = make(1, 2, vessel1, portSlot1, portSlot2, 23,
				true, true, true, route1, state1);
		VoyageOptions options9 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				false, true, true, route1, state1);
		VoyageOptions options10 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				true, false, true, route1, state1);
		VoyageOptions options11 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				true, true, false, route1, state1);
		VoyageOptions options12 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				true, true, true, route2, state1);
		VoyageOptions options13 = make(1, 2, vessel1, portSlot1, portSlot2, 3,
				true, true, true, route1, state2);
		
		Assert.assertTrue(options1.equals(options1));
		Assert.assertTrue(options1.equals(options2));
		Assert.assertTrue(options2.equals(options1));
		
		Assert.assertFalse(options1.equals(options3));
		Assert.assertFalse(options1.equals(options4));
		Assert.assertFalse(options1.equals(options5));
		Assert.assertFalse(options1.equals(options6));
		Assert.assertFalse(options1.equals(options7));
		Assert.assertFalse(options1.equals(options8));
		Assert.assertFalse(options1.equals(options9));
		Assert.assertFalse(options1.equals(options10));
		Assert.assertFalse(options1.equals(options11));
		Assert.assertFalse(options1.equals(options12));
		Assert.assertFalse(options1.equals(options13));
		
		Assert.assertFalse(options3.equals(options1));
		Assert.assertFalse(options4.equals(options1));
		Assert.assertFalse(options5.equals(options1));
		Assert.assertFalse(options6.equals(options1));
		Assert.assertFalse(options7.equals(options1));
		Assert.assertFalse(options8.equals(options1));
		Assert.assertFalse(options9.equals(options1));
		Assert.assertFalse(options10.equals(options1));
		Assert.assertFalse(options11.equals(options1));
		Assert.assertFalse(options12.equals(options1));
		Assert.assertFalse(options13.equals(options1));
		
		Assert.assertFalse(options1.equals(new Object()));
	}

	VoyageOptions make(int availableTime, int distance, IVessel vessel,
			IPortSlot fromPortSlot, IPortSlot toPortSlot, int nboSpeed,
			boolean useNBOForIdle, boolean useNBOForTravel,
			boolean useFBOForSupplement, String route, VesselState vesselState) {

		VoyageOptions o = new VoyageOptions();
		o.setAvailableTime(availableTime);
		o.setDistance(distance);
		o.setVessel(vessel);
		o.setFromPortSlot(fromPortSlot);
		o.setToPortSlot(toPortSlot);
		o.setNBOSpeed(nboSpeed);
		o.setUseNBOForIdle(useNBOForIdle);
		o.setUseNBOForTravel(useNBOForTravel);
		o.setUseFBOForSupplement(useFBOForSupplement);
		o.setRoute(route);
		o.setVesselState(vesselState);

		return o;

	}

}
