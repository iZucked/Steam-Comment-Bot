package com.mmxlabs.optimiser.constraints;

import org.junit.Assert;
import org.junit.Test;

public class ResourceAllocationConstraintCheckerFactoryTest {

	@Test
	public void testGetName() {

		final ResourceAllocationConstraintCheckerFactory factory = new ResourceAllocationConstraintCheckerFactory(
				"key");
		Assert.assertEquals(ResourceAllocationConstraintCheckerFactory.NAME,
				factory.getName());
	}

	@Test
	public void testInstantiate() {
		final ResourceAllocationConstraintCheckerFactory factory = new ResourceAllocationConstraintCheckerFactory(
				"key");
		final ResourceAllocationConstraintChecker<?> checker = factory
				.instantiate();
		Assert.assertNotNull(checker);
		Assert.assertEquals(ResourceAllocationConstraintCheckerFactory.NAME,
				checker.getName());
	}
}
