/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class PortTest {
	final IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testGetSetName() {
		final Port port = new Port(index);
		Assert.assertNull(port.getName());
		final String name = "name";
		port.setName(name);
		Assert.assertSame(name, port.getName());
	}

	@Test
	public void testEquals() {

		final Port port1 = new Port(index, "name");
		final Port port2 = new Port(index, "name");

		final Port port3 = new Port(index, "name2");

		Assert.assertTrue(port1.equals(port1));
		Assert.assertTrue(port1.equals(port2));
		Assert.assertTrue(port2.equals(port1));

		Assert.assertFalse(port1.equals(port3));

		Assert.assertFalse(port1.equals(new Object()));
	}

}
