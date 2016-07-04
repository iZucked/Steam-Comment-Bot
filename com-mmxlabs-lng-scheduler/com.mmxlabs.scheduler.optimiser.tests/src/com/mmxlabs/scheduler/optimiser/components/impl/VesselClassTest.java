/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public class VesselClassTest {

	@Test
	public void testGetSetName() {

		final String name = "name";
		final VesselClass vesselClass = new VesselClass();
		Assert.assertNull(vesselClass.getName());
		vesselClass.setName(name);
		Assert.assertSame(name, vesselClass.getName());
	}

	@Test
	public void testGetSetCargoCapacity() {
		final long value = 100L;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getCargoCapacity());
		vesselClass.setCargoCapacity(value);
		Assert.assertEquals(value, vesselClass.getCargoCapacity());
	}

	@Test
	public void testGetSetMinSpeed() {
		final int value = 100;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getMinSpeed());
		vesselClass.setMinSpeed(value);
		Assert.assertEquals(value, vesselClass.getMinSpeed());
	}

	@Test
	public void testGetSetMaxSpeed() {
		final int value = 100;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getMaxSpeed());
		vesselClass.setMaxSpeed(value);
		Assert.assertEquals(value, vesselClass.getMaxSpeed());
	}

	@Test
	public void testGetSetIdleConsumptionRate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final long value = 100L;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getIdleConsumptionRate(state1));
		Assert.assertEquals(0, vesselClass.getIdleConsumptionRate(state2));
		vesselClass.setIdleConsumptionRate(state1, value);
		Assert.assertEquals(value, vesselClass.getIdleConsumptionRate(state1));
		Assert.assertEquals(0, vesselClass.getIdleConsumptionRate(state2));
	}

	@Test
	public void testGetSetSafetyHeel() {
		final long value = 100L;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getSafetyHeel());
		vesselClass.setSafetyHeel(value);
		Assert.assertEquals(value, vesselClass.getSafetyHeel());
	}

	@Test
	public void testGetSetNBORate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final long value = 100L;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getNBORate(state1));
		Assert.assertEquals(0, vesselClass.getNBORate(state2));
		vesselClass.setNBORate(state1, value);
		Assert.assertEquals(value, vesselClass.getNBORate(state1));
		Assert.assertEquals(0, vesselClass.getNBORate(state2));
	}

	@Test
	public void testGetSetIdleNBORate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final long value = 100L;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getIdleNBORate(state1));
		Assert.assertEquals(0, vesselClass.getIdleNBORate(state2));
		vesselClass.setIdleNBORate(state1, value);
		Assert.assertEquals(value, vesselClass.getIdleNBORate(state1));
		Assert.assertEquals(0, vesselClass.getIdleNBORate(state2));

	}

	@Test
	public void testGetSetConsumptionRate() {
		final VesselState state1 = VesselState.Laden;
		final VesselState state2 = VesselState.Ballast;

		final IConsumptionRateCalculator calc = Mockito.mock(IConsumptionRateCalculator.class);

		final VesselClass vesselClass = new VesselClass();
		Assert.assertNull(vesselClass.getConsumptionRate(state1));
		Assert.assertNull(vesselClass.getConsumptionRate(state2));
		vesselClass.setConsumptionRate(state1, calc);
		Assert.assertSame(calc, vesselClass.getConsumptionRate(state1));
		Assert.assertNull(vesselClass.getConsumptionRate(state2));

	}

	@Test
	public void testGetSetBaseFuel() {
		final IBaseFuel b = new BaseFuel("test");
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(null, vesselClass.getBaseFuel());
		vesselClass.setBaseFuel(b);
		Assert.assertEquals(b.getName(), vesselClass.getBaseFuel().getName());
	}

	@Test
	public void testGetSetBaseFuelConversionFactor() {
		final int value = 100;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(null, vesselClass.getBaseFuel());
		IBaseFuel baseFuel = new BaseFuel("test");
		baseFuel.setEquivalenceFactor(100);
		vesselClass.setBaseFuel(baseFuel);

		Assert.assertEquals(value, vesselClass.getBaseFuel().getEquivalenceFactor());
	}

	@Test
	public void testGetSetPilotLightRate() {
		final long value = 100;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getPilotLightRate());
		vesselClass.setPilotLightRate(value);
		Assert.assertEquals(value, vesselClass.getPilotLightRate());
	}

	@Test
	public void testGetSetIdlePilotLightRate() {
		final long value = 100;
		final VesselClass vesselClass = new VesselClass();
		Assert.assertEquals(0, vesselClass.getIdlePilotLightRate());
		vesselClass.setIdlePilotLightRate(value);
		Assert.assertEquals(value, vesselClass.getIdlePilotLightRate());
	}
}
