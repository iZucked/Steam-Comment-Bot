/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

public class DefaultVesselAvailabilityTest {

	@Test
	public void testGetSetDailyCharterInRate() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);
		Assert.assertNull(vesselAvailavility.getDailyCharterInRate());
		final ILongCurve curve = Mockito.mock(ILongCurve.class);
		vesselAvailavility.setDailyCharterInRate(curve);
		Assert.assertSame(curve, vesselAvailavility.getDailyCharterInRate());
	}

	@Test
	public void testGetSetStartRequirement() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);
		Assert.assertNull(vesselAvailavility.getStartRequirement());
		final IStartRequirement requirement = Mockito.mock(IStartRequirement.class);
		vesselAvailavility.setStartRequirement(requirement);
		Assert.assertSame(requirement, vesselAvailavility.getStartRequirement());
	}

	@Test
	public void testGetSetEndRequirement() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);
		Assert.assertNull(vesselAvailavility.getEndRequirement());
		final IEndRequirement requirement = Mockito.mock(IEndRequirement.class);
		vesselAvailavility.setEndRequirement(requirement);
		Assert.assertSame(requirement, vesselAvailavility.getEndRequirement());
	}

	@Test
	public void testGetVesselAndType() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);

		Assert.assertSame(vessel, vesselAvailavility.getVessel());
		Assert.assertSame(vesselInstanceType, vesselAvailavility.getVesselInstanceType());
	}
}
