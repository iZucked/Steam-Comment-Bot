/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertSame(name, port.getName());
		Assertions.assertEquals(x, port.getX(), 0.001f);
		Assertions.assertEquals(y, port.getY(), 0.001f);
	}

	@Test
	public void testGetSetName() {
		final XYPort port = new XYPort(index);
		Assertions.assertNull(port.getName());
		final String name = "name";
		port.setName(name);
		Assertions.assertSame(name, port.getName());
	}

	@Test
	public void testGetSetX() {
		final XYPort port = new XYPort(index);
		Assertions.assertEquals(0.0f, port.getX(), 0.001);
		port.setX(10.0f);
		Assertions.assertEquals(10.0f, port.getX(), 0.001);
	}

	@Test
	public void testGetSetY() {
		final XYPort port = new XYPort(index);
		Assertions.assertEquals(0.0f, port.getY(), 0.001);
		port.setY(10.0f);
		Assertions.assertEquals(10.0f, port.getY(), 0.001);
	}

	@Test
	public void testEquals() {

		final XYPort port1 = new XYPort(index, "name", 1.0f, 2.0f);
		final XYPort port2 = new XYPort(index, "name", 1.0f, 2.0f);

		final XYPort port3 = new XYPort(index, "name2", 1.0f, 2.0f);
		final XYPort port4 = new XYPort(index, "name", 2.0f, 2.0f);
		final XYPort port5 = new XYPort(index, "name", 1.0f, 1.0f);

		Assertions.assertTrue(port1.equals(port1));
		Assertions.assertTrue(port1.equals(port2));
		Assertions.assertTrue(port2.equals(port1));

		Assertions.assertFalse(port1.equals(port3));
		Assertions.assertFalse(port1.equals(port4));
		Assertions.assertFalse(port1.equals(port5));

		Assertions.assertFalse(port1.equals(new Object()));
	}
}
