/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

@RunWith(JMock.class)
public class VesselTest {
	final IIndexingContext index = new SimpleIndexingContext();
	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetName() {
		final Vessel vessel = new Vessel(index);
		Assert.assertNull(vessel.getName());
		final String name = "name";
		vessel.setName(name);
		Assert.assertSame(name, vessel.getName());
	}

	@Test
	public void testGetSetVesselClass() {
		final Vessel vessel = new Vessel(index);
		Assert.assertNull(vessel.getVesselClass());
		final IVesselClass vesselClass = context.mock(IVesselClass.class);
		vessel.setVesselClass(vesselClass);
		Assert.assertSame(vesselClass, vessel.getVesselClass());
	}

	@Test
	public void testGetSetVesselInstanceType() {
		final Vessel vessel = new Vessel(index);
		Assert.assertSame(VesselInstanceType.UNKNOWN, vessel.getVesselInstanceType());
		final VesselInstanceType value = VesselInstanceType.FLEET;
		vessel.setVesselInstanceType(value);
		Assert.assertSame(value, vessel.getVesselInstanceType());
	}
}
