package com.mmxlabs.optimiser.core.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.impl.Resource;

public class ResourceTest {

	@Test
	public void testResource() {
		final Resource resource = new Resource();
		Assert.assertNull(resource.getName());
	}

	@Test
	public void testResourceString() {
		final String name = "name";
		final Resource resource = new Resource(name);
		Assert.assertSame(name, resource.getName());
	}

	@Test
	public void testGetSetName() {
		final Resource resource = new Resource();
		Assert.assertNull(resource.getName());
		final String name = "name";
		resource.setName(name);
		Assert.assertSame(name, resource.getName());
	}

}
