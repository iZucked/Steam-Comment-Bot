package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

@RunWith(JMock.class)
public class VesselTest {

	Mockery context = new JUnit4Mockery();

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
		IVesselClass vesselClass = context.mock(IVesselClass.class);
		vessel.setVesselClass(vesselClass);
		Assert.assertSame(vesselClass, vessel.getVesselClass());
	}

}
