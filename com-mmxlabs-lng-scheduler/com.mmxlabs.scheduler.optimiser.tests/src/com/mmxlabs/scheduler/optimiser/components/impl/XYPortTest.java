/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class XYPortTest {
	final IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testXYPort() {
		final String name = "name";
		final float x = 1.0f;
		final float y = 2.0f;

		final XYPort port = new XYPort(index, name, x, y);
		Assert.assertSame(name, port.getName());
		Assert.assertEquals(x, port.getX(), 0.0f);
		Assert.assertEquals(y, port.getY(), 0.0f);
	}

	@Test
	public void testGetSetName() {
		final XYPort port = new XYPort(index);
		Assert.assertNull(port.getName());
		final String name = "name";
		port.setName(name);
		Assert.assertSame(name, port.getName());
	}

	@Test
	public void testGetSetX() {
		final XYPort port = new XYPort(index);
		Assert.assertEquals(0.0f, port.getX(), 0.0);
		port.setX(10.0f);
		Assert.assertEquals(10.0f, port.getX(), 0.0);
	}

	@Test
	public void testGetSetY() {
		final XYPort port = new XYPort(index);
		Assert.assertEquals(0.0f, port.getY(), 0.0);
		port.setY(10.0f);
		Assert.assertEquals(10.0f, port.getY(), 0.0);
	}

	@Test
	public void testEquals() {

		final XYPort port1 = new XYPort(index, "name", 1.0f, 2.0f);
		final XYPort port2 = new XYPort(index, "name", 1.0f, 2.0f);

		final XYPort port3 = new XYPort(index, "name2", 1.0f, 2.0f);
		final XYPort port4 = new XYPort(index, "name", 2.0f, 2.0f);
		final XYPort port5 = new XYPort(index, "name", 1.0f, 1.0f);

		Assert.assertTrue(port1.equals(port1));
		Assert.assertTrue(port1.equals(port2));
		Assert.assertTrue(port2.equals(port1));

		Assert.assertFalse(port1.equals(port3));
		Assert.assertFalse(port1.equals(port4));
		Assert.assertFalse(port1.equals(port5));

		Assert.assertFalse(port1.equals(new Object()));
	}
}
