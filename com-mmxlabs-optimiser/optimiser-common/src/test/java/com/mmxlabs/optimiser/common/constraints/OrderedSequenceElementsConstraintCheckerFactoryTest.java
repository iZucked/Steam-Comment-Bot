/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintChecker;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;

public class OrderedSequenceElementsConstraintCheckerFactoryTest {

	@Test
	public void testGetName() {

		final OrderedSequenceElementsConstraintCheckerFactory factory = new OrderedSequenceElementsConstraintCheckerFactory(
				"key");
		Assert.assertEquals(
				OrderedSequenceElementsConstraintCheckerFactory.NAME, factory
						.getName());
	}

	@Test
	public void testInstantiate() {
		final OrderedSequenceElementsConstraintCheckerFactory factory = new OrderedSequenceElementsConstraintCheckerFactory(
				"key");
		final OrderedSequenceElementsConstraintChecker<?> checker = factory
				.instantiate();
		Assert.assertNotNull(checker);
		Assert.assertEquals(
				OrderedSequenceElementsConstraintCheckerFactory.NAME, checker
						.getName());
	}

}
