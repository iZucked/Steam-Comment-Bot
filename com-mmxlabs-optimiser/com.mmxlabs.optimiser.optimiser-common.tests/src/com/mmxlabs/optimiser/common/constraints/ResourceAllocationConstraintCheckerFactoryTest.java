/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.junit.Assert;
import org.junit.Test;

public class ResourceAllocationConstraintCheckerFactoryTest {

	@Test
	public void testGetName() {

		final ResourceAllocationConstraintCheckerFactory factory = new ResourceAllocationConstraintCheckerFactory();
		Assert.assertEquals(ResourceAllocationConstraintCheckerFactory.NAME, factory.getName());
	}

	@Test
	public void testInstantiate() {
		final ResourceAllocationConstraintCheckerFactory factory = new ResourceAllocationConstraintCheckerFactory();
		final ResourceAllocationConstraintChecker checker = factory.instantiate();
		Assert.assertNotNull(checker);
		Assert.assertEquals(ResourceAllocationConstraintCheckerFactory.NAME, checker.getName());
	}
}
