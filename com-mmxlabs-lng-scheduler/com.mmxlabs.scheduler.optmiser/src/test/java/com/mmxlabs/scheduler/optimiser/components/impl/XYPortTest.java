package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class XYPortTest {

	@Test
	public void testXYPort() {
		final String name = "name";
		final float x = 1.0f;
		final float y = 2.0f;
		
		final XYPort port = new XYPort(name, x, y);
		Assert.assertSame(name, port.getName());
		Assert.assertEquals(x, port.getX(), 0.0f);
		Assert.assertEquals(y, port.getY(), 0.0f);
	}
	
	@Test
	public void testGetSetName() {
		final XYPort port = new XYPort();
		Assert.assertNull(port.getName());
		final String name = "name";
		port.setName(name);
		Assert.assertSame(name, port.getName());
	}

	@Test
	public void testGetSetX() {
		final XYPort port = new XYPort();
		Assert.assertEquals(0.0f, port.getX(), 0.0);
		port.setX(10.0f);
		Assert.assertEquals(10.0f, port.getX(), 0.0);
	}

	@Test
	public void testGetSetY() {
		final XYPort port = new XYPort();
		Assert.assertEquals(0.0f, port.getY(), 0.0);
		port.setY(10.0f);
		Assert.assertEquals(10.0f, port.getY(), 0.0);
	}

	@Test
	public void testEquals() {
		
		XYPort port1 = new XYPort("name", 1.0f, 2.0f);
		XYPort port2 = new XYPort("name", 1.0f, 2.0f);
		
		XYPort port3 = new XYPort("name2", 1.0f, 2.0f);
		XYPort port4 = new XYPort("name", 2.0f, 2.0f);
		XYPort port5 = new XYPort("name", 1.0f, 1.0f);
		
		Assert.assertTrue(port1.equals(port1));
		Assert.assertTrue(port1.equals(port2));
		Assert.assertTrue(port2.equals(port1));
		
		Assert.assertFalse(port1.equals(port3));
		Assert.assertFalse(port1.equals(port4));
		Assert.assertFalse(port1.equals(port5));
		
		Assert.assertFalse(port1.equals(new Object()));
	}
}
