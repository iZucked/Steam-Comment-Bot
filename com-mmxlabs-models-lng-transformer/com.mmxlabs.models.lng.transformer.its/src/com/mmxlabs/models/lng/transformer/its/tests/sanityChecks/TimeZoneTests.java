/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.models.lng.transformer.DefaultModelEntityMap;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;

/**
 * Sanity check on Timezone conversion. Particularly important for Asia/Calcutta
 * 
 * @author achurchill
 * 
 */
@ExtendWith(ShiroRunner.class)
public class TimeZoneTests {
	private ModelEntityMap modelEntityMap;
	private DateAndCurveHelper dateHelper;

	public void testTimeZone(final int year, final int month, final int day, final String timeZone, final boolean isJanEarliestTime, final boolean dahejEarliestTime) {
		final String earliestTimeString = isJanEarliestTime ? "Jan 2014" : "Jul 2013";
		System.out.println(String.format("Testing: %s/%s/%s (%s) with earliest time %s", year, month + 1, day, timeZone, earliestTimeString));
		if (isJanEarliestTime) {
			if (dahejEarliestTime)
				setJanuaryEarliestTimeDahej();
			else
				setJanuaryEarliestTime();
		} else {
			if (dahejEarliestTime)
				setJulyEarliestTimeDahej();
			else
				setJulyEarliestTime();
		}
		for (int i = 0; i < 24; i++) {
			final ZonedDateTime dateToTest = createDate(year, month, day, i, timeZone);
			testDate(dateToTest, timeZone);
		}
	}

	public void testTimeZone(final int year, final int month, final int day, final String timeZone, final boolean isJanEarliestTime) {
		testTimeZone(year, month, day, timeZone, isJanEarliestTime, false);
	}

	public static ZonedDateTime createDate(final int year, final int month, final int day, final int hour, final String timeZone) {
		return ZonedDateTime.of(year, 1 + month, day, hour, 0, 0, 0, ZoneId.of(timeZone));
	}

	public void testDate(final ZonedDateTime dateToTest, final String timeZone) {
		final int dhToInt = dateHelper.convertTime(dateToTest);
		final ZonedDateTime intToDate = modelEntityMap.getDateFromHours(dhToInt, timeZone);
		System.out.println(String.format("In: %s Out: %s", dateToTest, intToDate));
		Assertions.assertEquals(dateToTest, intToDate);
	}

	private void setJanuaryEarliestTime(final String timeZone) {
		final ZonedDateTime jan = createDate(2014, 0, 10, 0, timeZone);
		setEarliestTime(jan);
	}

	private void setJanuaryEarliestTime() {
		setJanuaryEarliestTime("UTC");
	}

	private void setJulyEarliestTime(final String timeZone) {
		final ZonedDateTime jul = createDate(2013, 6, 10, 0, timeZone);
		setEarliestTime(jul);
	}

	private void setJulyEarliestTime() {
		setJulyEarliestTime("UTC");
	}

	private void setJanuaryEarliestTimeDahej() {
		setJanuaryEarliestTime("Asia/Calcutta");
	}

	private void setJulyEarliestTimeDahej() {
		setJulyEarliestTime("Asia/Calcutta");
	}

	private void setEarliestTime(final ZonedDateTime earliestTime) {
		dateHelper = new DateAndCurveHelper(earliestTime, earliestTime.plusYears(2));
		modelEntityMap = new DefaultModelEntityMap(dateHelper);
	}

	@Test
	public void testUTC() {
		testTimeZone(2015, 0, 1, "UTC", true);
		testTimeZone(2015, 0, 1, "UTC", true, true);
		testTimeZone(2015, 0, 1, "UTC", false);
		testTimeZone(2015, 0, 1, "UTC", false, true);
		testTimeZone(2014, 6, 1, "UTC", true);
		testTimeZone(2014, 6, 1, "UTC", true, true);
		testTimeZone(2014, 6, 1, "UTC", false);
		testTimeZone(2014, 6, 1, "UTC", false, true);
	}

	@Test
	public void testTokyo() {
		testTimeZone(2015, 0, 1, "Asia/Tokyo", true);
		testTimeZone(2015, 0, 1, "Asia/Tokyo", false);
		testTimeZone(2015, 6, 1, "Asia/Tokyo", true);
		testTimeZone(2015, 6, 1, "Asia/Tokyo", false);
	}

	@Test
	public void testSydney() {
		testTimeZone(2015, 0, 1, "Australia/Sydney", true);
		testTimeZone(2015, 0, 1, "Australia/Sydney", false);
		testTimeZone(2015, 6, 1, "Australia/Sydney", true);
		testTimeZone(2015, 6, 1, "Australia/Sydney", false);
	}

	@Test
	public void testLondon() {
		testTimeZone(2015, 0, 1, "Europe/London", true);
		testTimeZone(2015, 0, 1, "Europe/London", false);
		testTimeZone(2015, 6, 1, "Europe/London", true);
		testTimeZone(2015, 6, 1, "Europe/London", false);
	}

	@Test
	public void testNairobi() {
		testTimeZone(2015, 0, 1, "Africa/Nairobi", true);
		testTimeZone(2015, 0, 1, "Africa/Nairobi", false);
		testTimeZone(2015, 6, 1, "Africa/Nairobi", true);
		testTimeZone(2015, 6, 1, "Africa/Nairobi", false);
	}

	@Test
	public void testTrinidad() {
		testTimeZone(2015, 0, 1, "America/Port_of_Spain", true);
		testTimeZone(2015, 0, 1, "America/Port_of_Spain", false);
		testTimeZone(2015, 6, 1, "America/Port_of_Spain", true);
		testTimeZone(2015, 6, 1, "America/Port_of_Spain", false);
	}

	@Test
	public void testCalcutta() {
		testTimeZone(2015, 0, 1, "Asia/Calcutta", true);
		testTimeZone(2015, 0, 1, "Asia/Calcutta", true, true);
		testTimeZone(2015, 0, 1, "Asia/Calcutta", false);
		testTimeZone(2015, 0, 1, "Asia/Calcutta", false, true);
		testTimeZone(2015, 6, 1, "Asia/Calcutta", true);
		testTimeZone(2015, 6, 1, "Asia/Calcutta", true, true);
		testTimeZone(2015, 6, 1, "Asia/Calcutta", false);
		testTimeZone(2015, 6, 1, "Asia/Calcutta", false, true);
	}

	@Test
	public void testChathamIslands() {
		testTimeZone(2015, 0, 1, "Pacific/Chatham", true);
		testTimeZone(2015, 0, 1, "Pacific/Chatham", true, true);
		testTimeZone(2015, 0, 1, "Pacific/Chatham", false);
		testTimeZone(2015, 0, 1, "Pacific/Chatham", false, true);
		testTimeZone(2015, 6, 1, "Pacific/Chatham", true);
		testTimeZone(2015, 6, 1, "Pacific/Chatham", true, true);
		testTimeZone(2015, 6, 1, "Pacific/Chatham", false);
		testTimeZone(2015, 6, 1, "Pacific/Chatham", false, true);
	}

	@Test
	public void testNepal() {
		testTimeZone(2015, 0, 1, "Asia/Kathmandu", true);
		testTimeZone(2015, 0, 1, "Asia/Kathmandu", false);
		testTimeZone(2015, 6, 1, "Asia/Kathmandu", true);
		testTimeZone(2015, 6, 1, "Asia/Kathmandu", false);
	}

	@Test
	public void testEucla() {
		testTimeZone(2015, 0, 1, "Australia/Eucla", true);
		testTimeZone(2015, 0, 1, "Australia/Eucla", false);
		testTimeZone(2015, 6, 1, "Australia/Eucla", true);
		testTimeZone(2015, 6, 1, "Australia/Eucla", false);
	}

	@Test
	public void testAdelaide() {
		testTimeZone(2015, 0, 1, "Australia/Adelaide", true);
		testTimeZone(2015, 0, 1, "Australia/Adelaide", false);
		testTimeZone(2015, 6, 1, "Australia/Adelaide", true);
		testTimeZone(2015, 6, 1, "Australia/Adelaide", false);
	}

	@Test
	public void testNewfoundland() {
		testTimeZone(2015, 0, 1, "Canada/Newfoundland", true);
		testTimeZone(2015, 0, 1, "Canada/Newfoundland", true, true);
		testTimeZone(2015, 0, 1, "Canada/Newfoundland", false);
		testTimeZone(2015, 0, 1, "Canada/Newfoundland", false, true);
		testTimeZone(2015, 6, 1, "Canada/Newfoundland", true);
		testTimeZone(2015, 6, 1, "Canada/Newfoundland", true, true);
		testTimeZone(2015, 6, 1, "Canada/Newfoundland", false);
		testTimeZone(2015, 6, 1, "Canada/Newfoundland", false, true);
	}

	@Test
	public void testCalculattaWithIntervalTimeZoneProvider() {
		// Initialise date strucutres
		setEarliestTime(ZonedDateTime.of(2018, 9, 1, 0, 0, 0, 0, ZoneId.of("Etc/UTC")));

		final LocalDateTime localDateTime = LocalDateTime.of(2018, 9, 30, 0, 0, 0);
		final ZonedDateTime calcuttaDateTime = localDateTime.atZone(ZoneId.of("Asia/Calcutta"));
		final ZonedDateTime calcuttaDateTimeAsUTC = calcuttaDateTime.withZoneSameLocal(ZoneId.of("Etc/UTC"));

		final int dhInternalDate = dateHelper.convertTime(calcuttaDateTime);
		final ZonedDateTime modelEntityMapCalcuttaTime = modelEntityMap.getDateFromHours(dhInternalDate, "Asia/Calcutta");

		// Check we get the original time back.
		Assertions.assertEquals(calcuttaDateTime, modelEntityMapCalcuttaTime);

		// Initialise internal helepr
		final TimeZoneToUtcOffsetProvider helper = new TimeZoneToUtcOffsetProvider();
		helper.setTimeZeroInMillis(Instant.from(dateHelper.getEarliestTime()).toEpochMilli());

		// Convert to UTC, then back again.
		final int utcEquiv = helper.UTC(dhInternalDate, "Asia/Calcutta");
		final int newLocalTime = helper.localTime(utcEquiv, "Asia/Calcutta");

		// We should match
		Assertions.assertEquals(dhInternalDate, newLocalTime);

		final ZonedDateTime modelEntityMapCalcuttaUTCTime = modelEntityMap.getDateFromHours(utcEquiv, "Etc/UTC");
		// Ahead of UTC - Expect to loose an hour
		Assertions.assertEquals(calcuttaDateTimeAsUTC.minusHours(1), modelEntityMapCalcuttaUTCTime);

	}

	@Test
	public void testMinusHourHourWithIntervalTimeZoneProvider() {
		// Initialise date structures
		setEarliestTime(ZonedDateTime.of(2018, 9, 1, 0, 0, 0, 0, ZoneId.of("Etc/UTC")));

		final LocalDateTime localDateTime = LocalDateTime.of(2018, 9, 30, 0, 0, 0);
		final ZonedDateTime newfoundLandDateTime = localDateTime.atZone(ZoneId.of("America/St_Johns"));
		final ZonedDateTime newfoundLandDateTimeAsUTC = newfoundLandDateTime.withZoneSameLocal(ZoneId.of("Etc/UTC"));

		final int dhInternalDate = dateHelper.convertTime(newfoundLandDateTime);
		final ZonedDateTime modelEntityMapnewfoundLandTime = modelEntityMap.getDateFromHours(dhInternalDate, "America/St_Johns");

		// Check we get the original time back.
		Assertions.assertEquals(newfoundLandDateTime, modelEntityMapnewfoundLandTime);

		// Initialise internal helper
		final TimeZoneToUtcOffsetProvider helper = new TimeZoneToUtcOffsetProvider();
		helper.setTimeZeroInMillis(Instant.from(dateHelper.getEarliestTime()).toEpochMilli());

		// Convert to UTC, then back again.
		final int utcEquiv = helper.UTC(dhInternalDate, "America/St_Johns");
		final int newLocalTime = helper.localTime(utcEquiv, "America/St_Johns");

		// We should match
		Assertions.assertEquals(dhInternalDate, newLocalTime);

		final ZonedDateTime modelEntityMapNewfoundLandUTCTime = modelEntityMap.getDateFromHours(utcEquiv, "Etc/UTC");
		// Behind UTC, should match
		Assertions.assertEquals(newfoundLandDateTimeAsUTC, modelEntityMapNewfoundLandUTCTime);

	}

}
