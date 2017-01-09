/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.junit.Assert;
import org.junit.Test;

public class OrderedSequenceElementsConstraintCheckerFactoryTest {

	@Test
	public void testGetName() {

		final OrderedSequenceElementsConstraintCheckerFactory factory = new OrderedSequenceElementsConstraintCheckerFactory();
		Assert.assertEquals(OrderedSequenceElementsConstraintCheckerFactory.NAME, factory.getName());
	}

	@Test
	public void testInstantiate() {
		final OrderedSequenceElementsConstraintCheckerFactory factory = new OrderedSequenceElementsConstraintCheckerFactory();
		final OrderedSequenceElementsConstraintChecker checker = factory.instantiate();
		Assert.assertNotNull(checker);
		Assert.assertEquals(OrderedSequenceElementsConstraintCheckerFactory.NAME, checker.getName());
	}

}
