package com.mmxlabs.optimiser.constraints;

import org.junit.Assert;
import org.junit.Test;

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
