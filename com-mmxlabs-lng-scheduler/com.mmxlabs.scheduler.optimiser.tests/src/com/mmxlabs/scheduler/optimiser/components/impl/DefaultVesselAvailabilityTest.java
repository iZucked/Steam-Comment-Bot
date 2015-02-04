/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

public class DefaultVesselAvailabilityTest {

	@Test
	public void testGetSetDailyCharterInRate() {
		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability();
		Assert.assertNull(vesselAvailavility.getDailyCharterInRate());
		final ICurve curve = Mockito.mock(ICurve.class);
		vesselAvailavility.setDailyCharterInRate(curve);
		Assert.assertSame(curve, vesselAvailavility.getDailyCharterInRate());
	}

	@Test
	public void testGetSetStartRequirement() {
		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability();
		Assert.assertNull(vesselAvailavility.getStartRequirement());
		final IStartEndRequirement requirement = Mockito.mock(IStartEndRequirement.class);
		vesselAvailavility.setStartRequirement(requirement);
		Assert.assertSame(requirement, vesselAvailavility.getStartRequirement());
	}

	@Test
	public void testGetSetEndRequirement() {
		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability();
		Assert.assertNull(vesselAvailavility.getEndRequirement());
		final IStartEndRequirement requirement = Mockito.mock(IStartEndRequirement.class);
		vesselAvailavility.setEndRequirement(requirement);
		Assert.assertSame(requirement, vesselAvailavility.getEndRequirement());
	}

	@Test
	public void testGetSetVessel() {
		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability();
		Assert.assertNull(vesselAvailavility.getVessel());
		final IVessel vessel = Mockito.mock(IVessel.class);
		vesselAvailavility.setVessel(vessel);
		Assert.assertSame(vessel, vesselAvailavility.getVessel());
	}

	@Test
	public void testGetSetVesselInstanceType() {
		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability();
		Assert.assertSame(VesselInstanceType.UNKNOWN, vesselAvailavility.getVesselInstanceType());
		final VesselInstanceType value = VesselInstanceType.FLEET;
		vesselAvailavility.setVesselInstanceType(value);
		Assert.assertSame(value, vesselAvailavility.getVesselInstanceType());
	}
}
