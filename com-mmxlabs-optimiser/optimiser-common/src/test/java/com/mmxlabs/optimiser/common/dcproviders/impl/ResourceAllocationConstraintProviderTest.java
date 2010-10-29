/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.dcproviders.impl.ResourceAllocationConstraintProvider;
import com.mmxlabs.optimiser.core.IResource;

public class ResourceAllocationConstraintProviderTest {

	@Test
	public void testResourceAllocationConstraintProvider() {

		final String name = "name";
		final ResourceAllocationConstraintProvider<Object> provider = new ResourceAllocationConstraintProvider<Object>(
				name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testGetAllowedResources() {
		final ResourceAllocationConstraintProvider<Object> provider = new ResourceAllocationConstraintProvider<Object>(
				"name");

		final Object obj1 = new Object();

		Assert.assertNull(provider.getAllowedResources(obj1));

		final Collection<IResource> resources = Collections.emptyList();

		provider.setAllowedResources(obj1, resources);

		Assert.assertSame(resources, provider.getAllowedResources(obj1));

	}

	@Test
	public void testDispose() {

		final ResourceAllocationConstraintProvider<Object> provider = new ResourceAllocationConstraintProvider<Object>(
				"name");
		final Object obj1 = new Object();

		Assert.assertNull(provider.getAllowedResources(obj1));

		final Collection<IResource> resources = Collections.emptyList();

		provider.setAllowedResources(obj1, resources);

		Assert.assertSame(resources, provider.getAllowedResources(obj1));

		provider.dispose();

		Assert.assertNull(provider.getAllowedResources(obj1));

	}

}
