/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.IdleFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;

public class VoyageOptionsTest {

	@Test
	public void testGetSetAvailableTime() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertEquals(0, options.getAvailableTime());
		options.setAvailableTime(value);
		Assertions.assertEquals(value, options.getAvailableTime());
	}

	@Test
	public void testGetSetExtraIdleTime() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertEquals(0, options.getExtraIdleTime());
		options.setExtraIdleTime(value);
		Assertions.assertEquals(value, options.getExtraIdleTime());
	}

	@Test
	public void testGetSetFromPortSlot() {
		final IPortSlot slot = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(slot, Mockito.mock(IPortSlot.class));
		Assertions.assertSame(slot, options.getFromPortSlot());
	}

	@Test
	public void testGetSetToPortSlot() {
		final IPortSlot slot = Mockito.mock(IPortSlot.class);

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), slot);
		Assertions.assertSame(slot, options.getToPortSlot());
	}

	@Test
	public void testGetSetNBOSpeed() {
		final int value = 100;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertEquals(0, options.getNBOSpeed());
		options.setNBOSpeed(value);
		Assertions.assertEquals(value, options.getNBOSpeed());
	}

	@Test
	public void testGetSetRoute() {

		final ERouteOption route = ERouteOption.PANAMA;
		final int distance = 100;
		final long cost = 2000L;
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertNull(options.getRoute());
		options.setRoute(route, distance, cost);
		Assertions.assertSame(route, options.getRoute());
		Assertions.assertEquals(distance, options.getDistance());
		Assertions.assertEquals(cost, options.getRouteCost());
	}

	@Test
	public void testGetSetVessel() {
		final IVessel vessel = Mockito.mock(IVessel.class);

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertNull(options.getVessel());
		options.setVessel(vessel);
		Assertions.assertSame(vessel, options.getVessel());

	}

	@Test
	public void testGetVesselState() {
		final VesselState state = VesselState.Laden;

		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertNull(options.getVesselState());
		options.setVesselState(state);
		Assertions.assertSame(state, options.getVesselState());

	}

	@Test
	public void testTravelFuelChoice() {
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertSame(TravelFuelChoice.BUNKERS, options.getTravelFuelChoice()); // Default value
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_FBO);
		Assertions.assertSame(TravelFuelChoice.NBO_PLUS_FBO, options.getTravelFuelChoice());
	}

	@Test
	public void testIdleFuelChoice() {
		final VoyageOptions options = new VoyageOptions(Mockito.mock(IPortSlot.class), Mockito.mock(IPortSlot.class));
		Assertions.assertSame(IdleFuelChoice.BUNKERS, options.getIdleFuelChoice()); // Default value
		options.setIdleFuelChoice(IdleFuelChoice.NBO);
		Assertions.assertSame(IdleFuelChoice.NBO, options.getIdleFuelChoice());
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

		final VoyageOptions options1 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);

		final VoyageOptions options2 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);

		final VoyageOptions options3 = make(21, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options4 = make(1, 22, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options5 = make(1, 2, vessel2, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options6 = make(1, 2, vessel1, portSlot3, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options7 = make(1, 2, vessel1, portSlot1, portSlot4, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options8 = make(1, 2, vessel1, portSlot1, portSlot2, 23, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options9 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_FBO, IdleFuelChoice.NBO, route1, routeCost1, state1);
		final VoyageOptions options10 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.BUNKERS, route1, routeCost1, state1);
		final VoyageOptions options12 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route2, routeCost1, state1);
		final VoyageOptions options13 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost2, state1);
		final VoyageOptions options14 = make(1, 2, vessel1, portSlot1, portSlot2, 3, TravelFuelChoice.NBO_PLUS_BUNKERS, IdleFuelChoice.NBO, route1, routeCost1, state2);

		Assertions.assertTrue(options1.equals(options1));
		Assertions.assertTrue(options1.equals(options2));
		Assertions.assertTrue(options2.equals(options1));

		Assertions.assertFalse(options1.equals(options3));
		Assertions.assertFalse(options1.equals(options4));
		Assertions.assertFalse(options1.equals(options5));
		Assertions.assertFalse(options1.equals(options6));
		Assertions.assertFalse(options1.equals(options7));
		Assertions.assertFalse(options1.equals(options8));
		Assertions.assertFalse(options1.equals(options9));
		Assertions.assertFalse(options1.equals(options10));
		Assertions.assertFalse(options1.equals(options12));
		Assertions.assertFalse(options1.equals(options13));

		Assertions.assertFalse(options3.equals(options1));
		Assertions.assertFalse(options4.equals(options1));
		Assertions.assertFalse(options5.equals(options1));
		Assertions.assertFalse(options6.equals(options1));
		Assertions.assertFalse(options7.equals(options1));
		Assertions.assertFalse(options8.equals(options1));
		Assertions.assertFalse(options9.equals(options1));
		Assertions.assertFalse(options10.equals(options1));
		Assertions.assertFalse(options12.equals(options1));
		Assertions.assertFalse(options13.equals(options1));
		Assertions.assertFalse(options14.equals(options1));

		Assertions.assertFalse(options1.equals(new Object()));
	}

	VoyageOptions make(final int availableTime, final int distance, final IVessel vessel, final IPortSlot fromPortSlot, final IPortSlot toPortSlot, final int nboSpeed,
			final TravelFuelChoice travelFuelChoice, final IdleFuelChoice idleFuelChoice, @NonNull final ERouteOption route, long routeCost, final VesselState vesselState) {

		final VoyageOptions o = new VoyageOptions(fromPortSlot, toPortSlot);
		o.setAvailableTime(availableTime);
		o.setVessel(vessel);
		o.setNBOSpeed(nboSpeed);
		o.setTravelFuelChoice(travelFuelChoice);
		o.setIdleFuelChoice(idleFuelChoice);
		o.setRoute(route, distance, routeCost);
		o.setVesselState(vesselState);

		return o;

	}

}
