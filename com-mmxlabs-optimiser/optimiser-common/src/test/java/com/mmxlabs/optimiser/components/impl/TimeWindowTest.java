package com.mmxlabs.optimiser.components.impl;

import junit.framework.Assert;

import org.junit.Test;

public class TimeWindowTest {

	@Test
	public void testTimeWindow() {

		final int start = 10;
		final int end = 15;

		final TimeWindow tw = new TimeWindow(start, end);

		Assert.assertEquals(start, tw.getStart());
		Assert.assertEquals(end, tw.getEnd());
	}

}
