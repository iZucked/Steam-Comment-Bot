/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

public class VoyageOptionsTest {

	@Test
	public void testGetSetAvailableTime() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertEquals(0, options.getAvailableTime());
		options.setAvailableTime(value);
		Assert.assertEquals(value, options.getAvailableTime());
	}

	@Test
	public void testGetSetFromPortSlot() {
		final IPortSlot slot = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(slot, Mockito.mock(IPortSlot.class));
		Assert.assertSame(slot, options.getFromPortSlot());
	}

	@Test
	public void testGetSetToPortSlot() {
		final IPortSlot slot = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), slot);
		Assert.assertSame(slot, options.getToPortSlot());
	}

	@Test
	public void testGetSetNBOSpeed() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertEquals(0, options.getNBOSpeed());
		options.setNBOSpeed(value);
		Assert.assertEquals(value, options.getNBOSpeed());
	}

	@Test
	public void testGetSetRoute() {

		final ERouteOption route = ERouteOption.PANAMA;
		final int distance = 100;
		final long cost = 2000L;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertNull(options.getRoute());
		options.setRoute(route, distance, cost);
		Assert.assertSame(route, options.getRoute());
		Assert.assertEquals(distance, options.getDistance());
		Assert.assertEquals(cost, options.getRouteCost());
	}

	@Test
	public void testGetSetVessel() {
		final IVessel vessel = Mockito.mock(IVessel.class);

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertNull(options.getVessel());
		options.setVessel(vessel);
		Assert.assertSame(vessel, options.getVessel());

	}

	@Test
	public void testGetVesselState() {
		final VesselState state = VesselState.Laden;

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertNull(options.getVesselState());
		options.setVesselState(state);
		Assert.assertSame(state, options.getVesselState());

	}

	@Test
	public void testUseFBOForSupplement() {
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertFalse(options.useFBOForSupplement());
		options.setUseFBOForSupplement(true);
		Assert.assertTrue(options.useFBOForSupplement());
	}

	@Test
	public void testUseNBOForIdle() {
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertFalse(options.useNBOForIdle());
		options.setUseNBOForIdle(true);
		Assert.assertTrue(options.useNBOForIdle());

	}

	@Test
	public void testUseNBOForTravel() {
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assert.assertFalse(options.useNBOForTravel());
		options.setUseNBOForTravel(true);
		Assert.assertTrue(options.useNBOForTravel());

	}

	@Test
	public void testEquals() {

		final IVessel vessel1 = Mockito.mock(IVessel.class, "v1");
		final IVessel vessel2 = Mockito.mock(IVessel.class, "v2");
		final IPortSlot portSlot1 = Mockito.mock(IPortSlot.class, "s1");
		final IPortSlot portSlot2 = Mockito.mock(IPortSlot.class, "s2");
		final IPortSlot portSlot3 = Mockito.mock(IPortSlot.class, "s3");
		final IPortSlot portSlot4 = Mockito.mock(IPortSlot.class, "s4");

		final ERouteOption route1 = ERouteOption.DIRECT;
		final ERouteOption route2 = ERouteOption.SUEZ;

		long routeCost1 = 10L;
		long routeCost2 = 20L;

		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final VoyageOptions options1 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, true, true, route1, routeCost1, state1);

		final VoyageOptions options2 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, true, true, route1, routeCost1, state1);

		final VoyageOptions options3 = make(21, 2, vessel1, portSlot1, portSlot2, 3, true, true, true, route1, routeCost1, state1);
		final VoyageOptions options4 = make(1, 22, vessel1, portSlot1, portSlot2, 3, true, true, true, route1, routeCost1, state1);
		final VoyageOptions options5 = make(1, 2, vessel2, portSlot1, portSlot2, 3, true, true, true, route1, routeCost1, state1);
		final VoyageOptions options6 = make(1, 2, vessel1, portSlot3, portSlot2, 3, true, true, true, route1, routeCost1, state1);
		final VoyageOptions options7 = make(1, 2, vessel1, portSlot1, portSlot4, 3, true, true, true, route1, routeCost1, state1);
		final VoyageOptions options8 = make(1, 2, vessel1, portSlot1, portSlot2, 23, true, true, true, route1, routeCost1, state1);
		final VoyageOptions options9 = make(1, 2, vessel1, portSlot1, portSlot2, 3, false, true, true, route1, routeCost1, state1);
		final VoyageOptions options10 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, false, true, route1, routeCost1, state1);
		final VoyageOptions options11 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, true, false, route1, routeCost1, state1);
		final VoyageOptions options12 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, true, true, route2, routeCost1, state1);
		final VoyageOptions options13 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, true, true, route1, routeCost2, state1);
		final VoyageOptions options14 = make(1, 2, vessel1, portSlot1, portSlot2, 3, true, true, true, route1, routeCost1, state2);

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
		Assert.assertFalse(options14.equals(options1));

		Assert.assertFalse(options1.equals(new Object()));
	}

	VoyageOptions make(final int availableTime, final int distance, final IVessel vessel, final IPortSlot fromPortSlot, final IPortSlot toPortSlot, final int nboSpeed, final boolean useNBOForIdle,
			final boolean useNBOForTravel, final boolean useFBOForSupplement, @NonNull final ERouteOption route, long routeCost, final VesselState vesselState) {

		final VoyageOptions o = new VoyageOptions(fromPortSlot, toPortSlot);
		o.setAvailableTime(availableTime);
		o.setVessel(vessel);
		o.setNBOSpeed(nboSpeed);
		o.setUseNBOForIdle(useNBOForIdle);
		o.setUseNBOForTravel(useNBOForTravel);
		o.setUseFBOForSupplement(useFBOForSupplement);
		o.setRoute(route, distance, routeCost);
		o.setVesselState(vesselState);

		return o;

	}

}
