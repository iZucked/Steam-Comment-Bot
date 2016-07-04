/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

public class MonthsTest {

	@Test
	public void testZonedDateTime() {

		// Same date
		Assert.assertEquals(0, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// Less than a month
		Assert.assertEquals(0, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 1, 31, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// 1 Month exactly
		Assert.assertEquals(1, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// -1 month
		Assert.assertEquals(-1, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2014, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// -13 months
		Assert.assertEquals(-13, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2013, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// +23 Months
		Assert.assertEquals(23, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2016, 12, 30, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// Various Feb related cases (as it is less than 31 days)
		Assert.assertEquals(0, Months.between(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 2, 28, 0, 0, 0, 0, ZoneId.of("UTC"))));
		Assert.assertEquals(1, Months.between(ZonedDateTime.of(2015, 2, 28, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 3, 28, 0, 0, 0, 0, ZoneId.of("UTC"))));
		Assert.assertEquals(0, Months.between(ZonedDateTime.of(2016, 2, 29, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2016, 3, 28, 0, 0, 0, 0, ZoneId.of("UTC"))));
	}

	@Test
	public void testLocalDate() {

		// Same date
		Assert.assertEquals(0, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 1)));

		// Less than a month
		Assert.assertEquals(0, Months.between(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 1, 1)));

		// Less than a month
		Assert.assertEquals(0, Months.between(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 2, 1)));

		// Less than a month
		Assert.assertEquals(0, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 31)));

		// 1 Month exactly
		Assert.assertEquals(1, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 2, 1)));

		// -1 month
		Assert.assertEquals(-1, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2014, 12, 1)));

		// -13 months
		Assert.assertEquals(-13, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2013, 12, 1)));

		// +23 Months
		Assert.assertEquals(23, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 30)));
		Assert.assertEquals(23, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 30)));
	}

	@Test
	public void testYearMonth() {

		// Same date
		Assert.assertEquals(0, Months.between(YearMonth.of(2015, 1), YearMonth.of(2015, 1)));

		// 1 Month exactly
		Assert.assertEquals(1, Months.between(YearMonth.of(2015, 1), YearMonth.of(2015, 2)));

		// -1 month
		Assert.assertEquals(-1, Months.between(YearMonth.of(2015, 1), YearMonth.of(2014, 12)));

		// -13 months
		Assert.assertEquals(-13, Months.between(YearMonth.of(2015, 1), YearMonth.of(2013, 12)));

		// +23 Months
		Assert.assertEquals(23, Months.between(YearMonth.of(2015, 1), YearMonth.of(2016, 12)));
	}

}
