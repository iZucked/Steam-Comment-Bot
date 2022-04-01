/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MonthsTest {

	@Test
	public void testZonedDateTime() {

		// Same date
		Assertions.assertEquals(0, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// Less than a month
		Assertions.assertEquals(0, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 1, 31, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// 1 Month exactly
		Assertions.assertEquals(1, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// -1 month
		Assertions.assertEquals(-1, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2014, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// -13 months
		Assertions.assertEquals(-13, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2013, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// +23 Months
		Assertions.assertEquals(23, Months.between(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2016, 12, 30, 0, 0, 0, 0, ZoneId.of("UTC"))));

		// Various Feb related cases (as it is less than 31 days)
		Assertions.assertEquals(0, Months.between(ZonedDateTime.of(2015, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 2, 28, 0, 0, 0, 0, ZoneId.of("UTC"))));
		Assertions.assertEquals(1, Months.between(ZonedDateTime.of(2015, 2, 28, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2015, 3, 28, 0, 0, 0, 0, ZoneId.of("UTC"))));
		Assertions.assertEquals(0, Months.between(ZonedDateTime.of(2016, 2, 29, 0, 0, 0, 0, ZoneId.of("UTC")), ZonedDateTime.of(2016, 3, 28, 0, 0, 0, 0, ZoneId.of("UTC"))));
	}

	@Test
	public void testMonthsBetween() {
		for (int i = 1; i < 12; i++) {
			for (int j = i; j < 12; j++) {
				testMonthsBetween(i, 1, j, 1, j-i);
			}
		}
	}
	
	private void testMonthsBetween(int month1, int day1, int month2, int day2, int expectedMonths) {
		Assertions.assertEquals(expectedMonths, Months.between(ZonedDateTime.of(2020, month1, day1, 0,0,0,0, ZoneId.of("UTC")),
				ZonedDateTime.of(2020, month2, day2, 0,0,0,0, ZoneId.of("UTC"))));		
	}
	
	@Test
	public void testLocalDate() {

		// Same date
		Assertions.assertEquals(0, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 1)));

		// Less than a month
		Assertions.assertEquals(0, Months.between(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 1, 1)));

		// Less than a month
		Assertions.assertEquals(0, Months.between(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 2, 1)));

		// Less than a month
		Assertions.assertEquals(0, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 31)));

		// 1 Month exactly
		Assertions.assertEquals(1, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 2, 1)));

		// -1 month
		Assertions.assertEquals(-1, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2014, 12, 1)));

		// -13 months
		Assertions.assertEquals(-13, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2013, 12, 1)));

		// +23 Months
		Assertions.assertEquals(23, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 30)));
		Assertions.assertEquals(23, Months.between(LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 30)));
	}

	@Test
	public void testYearMonth() {

		// Same date
		Assertions.assertEquals(0, Months.between(YearMonth.of(2015, 1), YearMonth.of(2015, 1)));

		// 1 Month exactly
		Assertions.assertEquals(1, Months.between(YearMonth.of(2015, 1), YearMonth.of(2015, 2)));

		// -1 month
		Assertions.assertEquals(-1, Months.between(YearMonth.of(2015, 1), YearMonth.of(2014, 12)));

		// -13 months
		Assertions.assertEquals(-13, Months.between(YearMonth.of(2015, 1), YearMonth.of(2013, 12)));

		// +23 Months
		Assertions.assertEquals(23, Months.between(YearMonth.of(2015, 1), YearMonth.of(2016, 12)));
	}

}
