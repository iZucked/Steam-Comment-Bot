/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.impl.TimeWindowDataComponentProvider;

public class TimeWindowDataComponentProviderTest {

	@Test
	public void testTimeWindowDataComponentProvider() {
		final String name = "name";
		final TimeWindowDataComponentProvider<MockIndexedObject> provider = new TimeWindowDataComponentProvider<MockIndexedObject>(
				name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testGetTimeWindows() {
		final TimeWindowDataComponentProvider<MockIndexedObject> provider = new TimeWindowDataComponentProvider<MockIndexedObject>(
				"name");
		final MockIndexedObject obj1 = new MockIndexedObject(1);

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
		final TimeWindowDataComponentProvider<MockIndexedObject> provider = new TimeWindowDataComponentProvider<MockIndexedObject>(
				"name");
		final MockIndexedObject obj1 = new MockIndexedObject(1);

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
