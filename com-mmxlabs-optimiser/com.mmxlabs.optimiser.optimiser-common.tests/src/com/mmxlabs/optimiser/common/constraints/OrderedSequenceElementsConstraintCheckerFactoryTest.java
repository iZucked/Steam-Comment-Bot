/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderedSequenceElementsConstraintCheckerFactoryTest {

	@Test
	public void testGetName() {

		final OrderedSequenceElementsConstraintCheckerFactory factory = new OrderedSequenceElementsConstraintCheckerFactory();
		Assertions.assertEquals(OrderedSequenceElementsConstraintCheckerFactory.NAME, factory.getName());
	}

	@Test
	public void testInstantiate() {
		final OrderedSequenceElementsConstraintCheckerFactory factory = new OrderedSequenceElementsConstraintCheckerFactory();
		final OrderedSequenceElementsConstraintChecker checker = factory.instantiate();
		Assertions.assertNotNull(checker);
		Assertions.assertEquals(OrderedSequenceElementsConstraintCheckerFactory.NAME, checker.getName());
	}

}
