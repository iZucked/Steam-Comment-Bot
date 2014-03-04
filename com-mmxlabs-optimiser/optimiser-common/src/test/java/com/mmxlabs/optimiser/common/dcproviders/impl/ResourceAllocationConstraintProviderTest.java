/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collection;
import java.util.Collections;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

@RunWith(JMock.class)
public class ResourceAllocationConstraintProviderTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetAllowedResources() {
		final ResourceAllocationConstraintProvider provider = new ResourceAllocationConstraintProvider();

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");

		Assert.assertNull(provider.getAllowedResources(obj1));

		final Collection<IResource> resources = Collections.emptyList();

		provider.setAllowedResources(obj1, resources);

		Assert.assertSame(resources, provider.getAllowedResources(obj1));
	}
}
