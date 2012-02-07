/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class ResourceTest {
	IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testResource() {
		final Resource resource = new Resource(index);
		Assert.assertNull(resource.getName());
	}

	@Test
	public void testResourceString() {
		final String name = "name";
		final Resource resource = new Resource(index, name);
		Assert.assertSame(name, resource.getName());
	}

	@Test
	public void testGetSetName() {
		final Resource resource = new Resource(index);
		Assert.assertNull(resource.getName());
		final String name = "name";
		resource.setName(name);
		Assert.assertSame(name, resource.getName());
	}

}
