package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class VesselTest {

	@Test
	public void testGetSetName() {
		Vessel vessel = new Vessel();
		Assert.assertNull(vessel.getName());
		String name = "name";
		vessel.setName(name);
		Assert.assertSame(name, vessel.getName());
	}

}
