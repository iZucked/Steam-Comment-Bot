/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

public class DaysTest {

	@Test
	public void testBetween_LocalDate() {

		final LocalDate start = LocalDate.of(2015, 1, 1);
		final LocalDate end = LocalDate.of(2015, 1, 2);

		Assert.assertEquals(1, Days.between(start, end));
	}

	@Test
	public void testBetween_YearMonth() {

		final YearMonth start = YearMonth.of(2015, 1);
		final YearMonth end = YearMonth.of(2015, 2);

		Assert.assertEquals(31, Days.between(start, end));
	}

	@Test
	public void testBetween_LocalDateTime() {
		{
			final LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0, 0);
			final LocalDateTime end = LocalDateTime.of(2015, 1, 2, 0, 0, 0);

			Assert.assertEquals(1, Days.between(start, end));
		}
		{
			final LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0, 0);
			final LocalDateTime end = LocalDateTime.of(2015, 1, 2, 12, 0, 0);

			Assert.assertEquals(1, Days.between(start, end));
		}
	}

	@Test
	public void testBetween_ZonedDateTime() {
		{
			final ZonedDateTime start = ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/London"));
			final ZonedDateTime end = ZonedDateTime.of(2015, 1, 2, 0, 0, 0, 0, ZoneId.of("Europe/London"));

			Assert.assertEquals(1, Days.between(start, end));
		}
		{
			// UK Clock change
			final ZonedDateTime start = ZonedDateTime.of(2015, 10, 24, 0, 0, 0, 0, ZoneId.of("Europe/London"));
			final ZonedDateTime end = ZonedDateTime.of(2015, 10, 26, 0, 0, 0, 0, ZoneId.of("Europe/London"));

			Assert.assertEquals(2, Days.between(start, end));
		}
	}
}
