package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class PortTest {

	@Test
	public void testGetSetName() {
		final Port port = new Port();
		Assert.assertNull(port.getName());
		final String name = "name";
		port.setName(name);
		Assert.assertSame(name, port.getName());
	}
}
