/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceAllocationConstraintCheckerFactoryTest {

	@Test
	public void testGetName() {

		final ResourceAllocationConstraintCheckerFactory factory = new ResourceAllocationConstraintCheckerFactory();
		Assertions.assertEquals(ResourceAllocationConstraintCheckerFactory.NAME, factory.getName());
	}

	@Test
	public void testInstantiate() {
		final ResourceAllocationConstraintCheckerFactory factory = new ResourceAllocationConstraintCheckerFactory();
		final ResourceAllocationConstraintChecker checker = factory.instantiate();
		Assertions.assertNotNull(checker);
		Assertions.assertEquals(ResourceAllocationConstraintCheckerFactory.NAME, checker.getName());
	}
}
