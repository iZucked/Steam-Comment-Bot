/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Need to think about this API - not sure if naming is quick correct.")
public class TimeUnitConvertTest {

	@Test
	public void testConvertPerSecondToPerMinute() {

		Assertions.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(0));
		Assertions.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(1));
		Assertions.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(59));
		Assertions.assertEquals(1, TimeUnitConvert.convertPerSecondToPerMinute(60));

		Assertions.assertEquals(0, TimeUnitConvert.convertPerSecondToPerMinute(-1));
	}

	@Test
	public void testConvertPerMinuteToPerHour() {
		Assertions.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(0));
		Assertions.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(1));
		Assertions.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(59));
		Assertions.assertEquals(1, TimeUnitConvert.convertPerMinuteToPerHour(60));

		Assertions.assertEquals(0, TimeUnitConvert.convertPerMinuteToPerHour(-1));
	}

	@Test
	public void testConvertPerHourToPerDay() {
		Assertions.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(0));
		Assertions.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(1));
		Assertions.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(23));
		Assertions.assertEquals(1, TimeUnitConvert.convertPerHourToPerDay(24));

		Assertions.assertEquals(0, TimeUnitConvert.convertPerHourToPerDay(-1));
	}

}
