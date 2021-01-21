/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class ResourceAllocationConstraintProviderTest {

	@Test
	public void testGetAllowedResources() {
		final ResourceAllocationConstraintProvider provider = new ResourceAllocationConstraintProvider();

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");

		Assertions.assertNull(provider.getAllowedResources(obj1));

		final @NonNull Collection<@NonNull IResource> resources = Collections.emptyList();

		provider.setAllowedResources(obj1, resources);

		Assertions.assertSame(resources, provider.getAllowedResources(obj1));
	}
}
