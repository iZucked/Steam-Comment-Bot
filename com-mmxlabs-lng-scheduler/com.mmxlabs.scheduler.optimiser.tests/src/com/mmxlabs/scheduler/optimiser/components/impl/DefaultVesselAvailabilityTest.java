/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertNull(vesselAvailavility.getDailyCharterInRate());
		final ILongCurve curve = Mockito.mock(ILongCurve.class);
		vesselAvailavility.setDailyCharterInRate(curve);
		Assertions.assertSame(curve, vesselAvailavility.getDailyCharterInRate());
	}

	@Test
	public void testGetSetStartRequirement() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);
		Assertions.assertNull(vesselAvailavility.getStartRequirement());
		final IStartRequirement requirement = Mockito.mock(IStartRequirement.class);
		vesselAvailavility.setStartRequirement(requirement);
		Assertions.assertSame(requirement, vesselAvailavility.getStartRequirement());
	}

	@Test
	public void testGetSetEndRequirement() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);
		Assertions.assertNull(vesselAvailavility.getEndRequirement());
		final IEndRequirement requirement = Mockito.mock(IEndRequirement.class);
		vesselAvailavility.setEndRequirement(requirement);
		Assertions.assertSame(requirement, vesselAvailavility.getEndRequirement());
	}

	@Test
	public void testGetVesselAndType() {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselInstanceType vesselInstanceType = VesselInstanceType.FLEET;

		final DefaultVesselAvailability vesselAvailavility = new DefaultVesselAvailability(vessel, vesselInstanceType);

		Assertions.assertSame(vessel, vesselAvailavility.getVessel());
		Assertions.assertSame(vesselInstanceType, vesselAvailavility.getVesselInstanceType());
	}
}
