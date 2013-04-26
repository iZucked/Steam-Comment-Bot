/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator.EndLocationRule;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

public class EndLocationSequenceManipulatorTest {

	@Test
	public void testGetSetPortTypeProvider() {
		final EndLocationSequenceManipulator manipulator = new EndLocationSequenceManipulator();

		Assert.assertNull(manipulator.getPortTypeProvider());

		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);

		manipulator.setPortTypeProvider(portTypeProvider);

		Assert.assertSame(portTypeProvider, manipulator.getPortTypeProvider());
	}

	@Test
	public void testGetSetEndLocationRule() {
		final IIndexingContext index = new SimpleIndexingContext();
		final EndLocationSequenceManipulator manipulator = new EndLocationSequenceManipulator();

		final IResource resource1 = new Resource(index);
		final IResource resource2 = new Resource(index);
		final IResource resource3 = new Resource(index);

		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource3));

		manipulator.setEndLocationRule(resource1, EndLocationRule.NONE);
		manipulator.setEndLocationRule(resource2, EndLocationRule.RETURN_TO_FIRST_LOAD);
		manipulator.setEndLocationRule(resource3, EndLocationRule.RETURN_TO_LAST_LOAD);

		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.RETURN_TO_FIRST_LOAD, manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.RETURN_TO_LAST_LOAD, manipulator.getEndLocationRule(resource3));

		manipulator.dispose();

		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.NONE, manipulator.getEndLocationRule(resource3));
	}
}
