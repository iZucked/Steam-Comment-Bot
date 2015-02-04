/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class VesselTest {

	@Test
	public void testGetSetName() {
		final Vessel vessel = new Vessel();
		Assert.assertNull(vessel.getName());
		final String name = "name";
		vessel.setName(name);
		Assert.assertSame(name, vessel.getName());
	}

	@Test
	public void testGetSetVesselClass() {
		final Vessel vessel = new Vessel();
		Assert.assertNull(vessel.getVesselClass());
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		vessel.setVesselClass(vesselClass);
		Assert.assertSame(vesselClass, vessel.getVesselClass());
	}

	@Test
	public void testGetSetVesselInstanceType() {
		final Vessel vessel = new Vessel();
		Assert.assertEquals(0, vessel.getCargoCapacity());
		vessel.setCargoCapacity(123456);
		Assert.assertEquals(123456, vessel.getCargoCapacity());
	}
}
