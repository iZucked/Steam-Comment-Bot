/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Need to think about this API - not sure if naming is quick correct.")
public class TimeUnitConvertTest {

	@Test
	public void testConvertPerSecondToPerMinute() {

		Assert.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(0));
		Assert.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(1));
		Assert.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(59));
		Assert.assertEquals(1, TimeUnitConvert.convertPerSecondToPerMinute(60));

		Assert.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(-1));
	}

	@Test
	public void testConvertPerMinuteToPerHour() {
		Assert.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(0));
		Assert.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(1));
		Assert.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(59));
		Assert.assertEquals(1, TimeUnitConvert.convertPerMinuteToPerHour(60));

		Assert.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(-1));
	}

	@Test
	public void testConvertPerHourToPerDay() {
		Assert.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(0));
		Assert.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(1));
		Assert.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(23));
		Assert.assertEquals(1, TimeUnitConvert.convertPerHourToPerDay(24));

		Assert.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(-1));
	}

}
