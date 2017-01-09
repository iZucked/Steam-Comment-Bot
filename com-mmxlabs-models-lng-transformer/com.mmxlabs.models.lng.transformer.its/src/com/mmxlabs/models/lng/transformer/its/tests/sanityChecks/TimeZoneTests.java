/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.transformer.DefaultModelEntityMap;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;

/**
 * Sanity check on Timezone conversion. Particularly important for Asia/Calcutta
 * 
 * @author achurchill
 * 
 */
@RunWith(value = ShiroRunner.class)
public class TimeZoneTests {
	private ModelEntityMap modelEntityMap;
	private DateAndCurveHelper dateHelper;

	public void testTimeZone(int year, int month, int day, String timeZone, boolean isJanEarliestTime, boolean dahejEarliestTime) {
		String earliestTimeString = isJanEarliestTime ? "Jan 2014" : "Jul 2013";
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
			ZonedDateTime dateToTest = createDate(year, month, day, i, timeZone);
			testDate(dateToTest, timeZone);
		}
	}

	public void testTimeZone(int year, int month, int day, String timeZone, boolean isJanEarliestTime) {
		testTimeZone(year, month, day, timeZone, isJanEarliestTime, false);
	}

	public static ZonedDateTime createDate(int year, int month, int day, int hour, String timeZone) {
		return ZonedDateTime.of(year, 1 + month, day, hour, 0, 0, 0, ZoneId.of(timeZone));
	}

	public void testDate(ZonedDateTime dateToTest, String timeZone) {
		int dhToInt = dateHelper.convertTime(dateToTest);
		ZonedDateTime intToDate = modelEntityMap.getDateFromHours(dhToInt, timeZone);
		System.out.println(String.format("In: %s Out: %s", dateToTest, intToDate));
		Assert.assertEquals(dateToTest, intToDate);
	}

	private void setJanuaryEarliestTime(String timeZone) {
		ZonedDateTime jan = createDate(2014, 0, 10, 0, timeZone);
		setEarliestTime(jan);
	}

	private void setJanuaryEarliestTime() {
		setJanuaryEarliestTime("UTC");
	}

	private void setJulyEarliestTime(String timeZone) {
		ZonedDateTime jul = createDate(2013, 6, 10, 0, timeZone);
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

}
