/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class ResourceTest {
	@NonNull
	IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testResourceString() {
		final String name = "name";
		final Resource resource = new Resource(index, name);
		Assertions.assertSame(name, resource.getName());
	}
}
