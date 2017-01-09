/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator.EndLocationRule;

public class EndLocationSequenceManipulatorTest {

	@Test
	public void testGetSetEndLocationRule() {
		final IIndexingContext index = new SimpleIndexingContext();
		final EndLocationSequenceManipulator manipulator = new EndLocationSequenceManipulator();

		final IResource resource1 = new Resource(index, "r1");
		final IResource resource2 = new Resource(index, "r2");
		final IResource resource3 = new Resource(index, "r3");

		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource3));

		manipulator.setEndLocationRule(resource1, EndLocationRule.NONE);
		manipulator.setEndLocationRule(resource2, EndLocationRule.RETURN_TO_FIRST_LOAD);
		manipulator.setEndLocationRule(resource3, EndLocationRule.RETURN_TO_LAST_LOAD);

		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.RETURN_TO_FIRST_LOAD, manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.RETURN_TO_LAST_LOAD, manipulator.getEndLocationRule(resource3));
	}
}
