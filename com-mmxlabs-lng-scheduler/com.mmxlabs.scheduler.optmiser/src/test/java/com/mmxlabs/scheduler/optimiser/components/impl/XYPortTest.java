package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class XYPortTest {

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

}
