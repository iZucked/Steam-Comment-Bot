/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class TimeWindowDataComponentProviderTest {


	@Test
	public void testGetTimeWindows() {
		final TimeWindowDataComponentProvider provider = new TimeWindowDataComponentProvider();
		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");

		final List<ITimeWindow> timeWindows = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows);
		Assert.assertTrue(timeWindows.isEmpty());

		final List<ITimeWindow> windows = new LinkedList<ITimeWindow>();
		provider.setTimeWindows(obj1, windows);

		final List<ITimeWindow> timeWindows2 = provider.getTimeWindows(obj1);
		Assert.assertNotNull(timeWindows2);
		Assert.assertSame(windows, timeWindows2);
	}

}
