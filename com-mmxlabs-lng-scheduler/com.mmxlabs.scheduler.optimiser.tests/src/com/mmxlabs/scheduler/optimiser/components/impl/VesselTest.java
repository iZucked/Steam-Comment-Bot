/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public class VesselTest {

	@Test
	public void testVessel() {
		final String name = "name";
		final Vessel vessel = new Vessel(name, 123456);
		Assertions.assertSame(name, vessel.getName());
		Assertions.assertEquals(123456, vessel.getCargoCapacity());
	}

	@Test
	public void testGetSetMinSpeed() {
		final int value = 100;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getMinSpeed());
		vessel.setMinSpeed(value);
		Assertions.assertEquals(value, vessel.getMinSpeed());
	}

	@Test
	public void testGetSetMaxSpeed() {
		final int value = 100;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getMaxSpeed());
		vessel.setMaxSpeed(value);
		Assertions.assertEquals(value, vessel.getMaxSpeed());
	}

	@Test
	public void testGetSetIdleConsumptionRate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final long value = 100L;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getIdleConsumptionRate(state1));
		Assertions.assertEquals(0, vessel.getIdleConsumptionRate(state2));
		vessel.setIdleConsumptionRate(state1, value);
		Assertions.assertEquals(value, vessel.getIdleConsumptionRate(state1));
		Assertions.assertEquals(0, vessel.getIdleConsumptionRate(state2));
	}

	@Test
	public void testGetSetSafetyHeel() {
		final long value = 100L;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getSafetyHeel());
		vessel.setSafetyHeel(value);
		Assertions.assertEquals(value, vessel.getSafetyHeel());
	}

	@Test
	public void testGetSetNBORate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final long value = 100L;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getNBORate(state1));
		Assertions.assertEquals(0, vessel.getNBORate(state2));
		vessel.setNBORate(state1, value);
		Assertions.assertEquals(value, vessel.getNBORate(state1));
		Assertions.assertEquals(0, vessel.getNBORate(state2));
	}

	@Test
	public void testGetSetIdleNBORate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final long value = 100L;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getIdleNBORate(state1));
		Assertions.assertEquals(0, vessel.getIdleNBORate(state2));
		vessel.setIdleNBORate(state1, value);
		Assertions.assertEquals(value, vessel.getIdleNBORate(state1));
		Assertions.assertEquals(0, vessel.getIdleNBORate(state2));

	}

	@Test
	public void testGetSetConsumptionRate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final IConsumptionRateCalculator calc1 = Mockito.mock(IConsumptionRateCalculator.class);
		final IConsumptionRateCalculator calc2 = Mockito.mock(IConsumptionRateCalculator.class);

		final Vessel vessel = new Vessel("name", 123456);
		vessel.setConsumptionRate(state1, calc1);
		vessel.setConsumptionRate(state2, calc2);
		Assertions.assertSame(calc1, vessel.getConsumptionRate(state1));
		Assertions.assertSame(calc2, vessel.getConsumptionRate(state2));

	}

	@Test
	public void testGetSetBaseFuel() {
		SimpleIndexingContext context = new SimpleIndexingContext();
		context.registerType(IBaseFuel.class);
		final IBaseFuel b = new BaseFuel(context, "test");
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(null, vessel.getTravelBaseFuel());
		vessel.setTravelBaseFuel(b);
		Assertions.assertEquals(b.getName(), vessel.getTravelBaseFuel().getName());
	}

	@Test
	public void testGetSetBaseFuelConversionFactor() {
		SimpleIndexingContext context = new SimpleIndexingContext();
		context.registerType(IBaseFuel.class);
		final int value = 100;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(null, vessel.getTravelBaseFuel());
		IBaseFuel baseFuel = new BaseFuel(context, "test");
		baseFuel.setEquivalenceFactor(100);
		vessel.setTravelBaseFuel(baseFuel);

		Assertions.assertEquals(value, vessel.getTravelBaseFuel().getEquivalenceFactor());
	}

	@Test
	public void testGetSetPilotLightRate() {
		final long value = 100;
		final Vessel vessel = new Vessel("name", 123456);
		Assertions.assertEquals(0, vessel.getPilotLightRate());
		vessel.setPilotLightRate(value);
		Assertions.assertEquals(value, vessel.getPilotLightRate());
	}
}
