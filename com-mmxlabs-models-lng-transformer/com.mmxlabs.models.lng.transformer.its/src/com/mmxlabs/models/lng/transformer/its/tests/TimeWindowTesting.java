/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;

public class TimeWindowTesting {

	@Test
	public void testTimeZero() {
		ZonedDateTime earlyTime = ZonedDateTime.of(2016, 6, 13, 0, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime lateTime = ZonedDateTime.of(2016, 7, 13, 0, 0, 0, 0, ZoneId.of("UTC"));

		DateAndCurveHelper helper = new DateAndCurveHelper(earlyTime, lateTime);

		// Sanity check time zero
		Assert.assertEquals(0, helper.convertTime(earlyTime));

	}

	@Test
	public void testWindowSize_ExplicitDates() {
		ZonedDateTime earlyTime = ZonedDateTime.of(2016, 6, 13, 0, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime lateTime = ZonedDateTime.of(2016, 7, 13, 0, 0, 0, 0, ZoneId.of("UTC"));

		ZonedDateTime startTime = ZonedDateTime.of(2016, 6, 13, 0, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime endTime = ZonedDateTime.of(2016, 6, 14, 0, 0, 0, 0, ZoneId.of("UTC"));

		DateAndCurveHelper helper = new DateAndCurveHelper(earlyTime, lateTime);

		// Sanity check time zero
		Assert.assertEquals(0, helper.convertTime(startTime));
		Assert.assertEquals(24, helper.convertTime(endTime));

	}

	@Test
	public void testWindowSize_AddedTime() {
		ZonedDateTime earlyTime = ZonedDateTime.of(2016, 6, 13, 0, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime lateTime = ZonedDateTime.of(2016, 7, 13, 0, 0, 0, 0, ZoneId.of("UTC"));

		ZonedDateTime startTime = ZonedDateTime.of(2016, 6, 13, 0, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime endTime = startTime.plusHours(24);

		DateAndCurveHelper helper = new DateAndCurveHelper(earlyTime, lateTime);

		// Sanity check time zero
		Assert.assertEquals(0, helper.convertTime(startTime));
		Assert.assertEquals(24, helper.convertTime(endTime));
	}

}
