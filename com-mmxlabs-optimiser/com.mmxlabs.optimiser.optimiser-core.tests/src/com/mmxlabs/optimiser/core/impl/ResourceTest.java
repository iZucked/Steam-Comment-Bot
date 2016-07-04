/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class ResourceTest {
	@NonNull
	IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testResourceString() {
		final String name = "name";
		final Resource resource = new Resource(index, name);
		Assert.assertSame(name, resource.getName());
	}
}
