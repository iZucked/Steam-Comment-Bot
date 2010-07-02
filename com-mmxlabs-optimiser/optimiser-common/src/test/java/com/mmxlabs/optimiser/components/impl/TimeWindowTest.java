package com.mmxlabs.optimiser.components.impl;

import org.junit.Assert;
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

	@Test
	public void testEquals() {
		final TimeWindow tw1 = new TimeWindow(10, 20);
		final TimeWindow tw2 = new TimeWindow(10, 20);
		final TimeWindow tw3 = new TimeWindow(20, 20);
		final TimeWindow tw4 = new TimeWindow(20, 10);

		Assert.assertTrue(tw1.equals(tw1));
		
		Assert.assertTrue(tw1.equals(tw2));
		Assert.assertTrue(tw2.equals(tw1));
		
		Assert.assertFalse(tw1.equals(tw3));
		Assert.assertFalse(tw1.equals(tw4));
		
		Assert.assertFalse(tw1.equals(new Object()));
		
		Assert.assertFalse(tw3.equals(tw1));
		Assert.assertFalse(tw4.equals(tw1));
		
	}
	
}
