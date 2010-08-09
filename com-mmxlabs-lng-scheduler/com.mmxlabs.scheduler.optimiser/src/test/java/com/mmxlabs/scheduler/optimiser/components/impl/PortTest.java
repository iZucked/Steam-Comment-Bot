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
	

	@Test
	public void testEquals() {
		
		Port port1 = new Port("name");
		Port port2 = new Port("name");
		
		Port port3 = new Port("name2");
		
		Assert.assertTrue(port1.equals(port1));
		Assert.assertTrue(port1.equals(port2));
		Assert.assertTrue(port2.equals(port1));
		
		Assert.assertFalse(port1.equals(port3));
		
		Assert.assertFalse(port1.equals(new Object()));
	}

}
