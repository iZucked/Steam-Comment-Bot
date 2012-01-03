/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

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
		final ResourceAllocationConstraintProvider provider = new ResourceAllocationConstraintProvider(name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testGetAllowedResources() {
		final ResourceAllocationConstraintProvider provider = new ResourceAllocationConstraintProvider("name");

		final MockSequenceElement obj1 = new MockSequenceElement(1);

		Assert.assertNull(provider.getAllowedResources(obj1));

		final Collection<IResource> resources = Collections.emptyList();

		provider.setAllowedResources(obj1, resources);

		Assert.assertSame(resources, provider.getAllowedResources(obj1));

	}

	@Test
	public void testDispose() {

		final ResourceAllocationConstraintProvider provider = new ResourceAllocationConstraintProvider("name");
		final MockSequenceElement obj1 = new MockSequenceElement(1);

		Assert.assertNull(provider.getAllowedResources(obj1));

		final Collection<IResource> resources = Collections.emptyList();

		provider.setAllowedResources(obj1, resources);

		Assert.assertSame(resources, provider.getAllowedResources(obj1));

		provider.dispose();

		Assert.assertNull(provider.getAllowedResources(obj1));

	}

}
