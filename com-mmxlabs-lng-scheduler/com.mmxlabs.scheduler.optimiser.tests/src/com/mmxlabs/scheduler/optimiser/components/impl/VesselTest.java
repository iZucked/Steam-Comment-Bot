/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class VesselTest {

	@SuppressWarnings("null")
	@Test
	public void testVessel() {
		final String name = "name";
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		final Vessel vessel = new Vessel(name, vesselClass, 123456);
		Assert.assertSame(name, vessel.getName());
		Assert.assertSame(vesselClass, vessel.getVesselClass());
		Assert.assertEquals(123456, vessel.getCargoCapacity());
	}
}
