/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.LinkedList;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;

@RunWith(JMock.class)
public class TimeWindowDataComponentProviderTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testTimeWindowDataComponentProvider() {
		final String name = "name";
		final TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider(name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testGetTimeWindows() {
		final TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider("name");
		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");

		final List<ITimeWindow> timeWindows = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows);
		Assert.assertTrue(timeWindows.isEmpty());

		final List<ITimeWindow> windows = new LinkedList<ITimeWindow>();
		provider.setTimeWindows(obj1, windows);

		final List<ITimeWindow> timeWindows2 = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows2);
		Assert.assertSame(windows, timeWindows2);
	}

	@Test
	public void testDispose() {
		final TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider("name");
		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");

		final List<ITimeWindow> timeWindows = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows);
		Assert.assertTrue(timeWindows.isEmpty());

		final List<ITimeWindow> windows = new LinkedList<ITimeWindow>();
		provider.setTimeWindows(obj1, windows);

		final List<ITimeWindow> timeWindows2 = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows2);
		Assert.assertSame(windows, timeWindows2);

		provider.dispose();

		final List<ITimeWindow> timeWindows3 = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows3);
		Assert.assertTrue(timeWindows3.isEmpty());

	}

}
