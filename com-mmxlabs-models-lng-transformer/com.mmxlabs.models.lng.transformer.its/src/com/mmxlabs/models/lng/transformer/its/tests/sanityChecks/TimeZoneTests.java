package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;

import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;

/**
 * Sanity check on Timezone conversion. Particularly important for Asia/Calcutta
 * 
 * @author achurchill
 * 
 */
public class TimeZoneTests {
	ModelEntityMap modelEntityMap = new ModelEntityMap();
	DateAndCurveHelper dateHelper = new DateAndCurveHelper();

	public void testTimeZone(int year, int month, int day, String timeZone, boolean isJanEarliestTime, boolean dahejEarliestTime) {
		String earliestTimeString = isJanEarliestTime ? "Jan 2014" : "Jul 2013";
		System.out.println(String.format("Testing: %s/%s/%s (%s) with earliest time %s", year,month+1,day,timeZone,earliestTimeString));
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
			Date dateToTest = createDate(year, month, day, i, timeZone);
			testDate(dateToTest, timeZone);
		}
	}

	public void testTimeZone(int year, int month, int day, String timeZone, boolean isJanEarliestTime) {
		testTimeZone(year, month, day, timeZone, isJanEarliestTime, false);
	}
	
	public static Date createDate(int year, int month, int day, int hour, String timeZone) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	public void testDate(Date dateToTest, String timeZone) {
		int dhToInt = dateHelper.convertTime(dateToTest);
		Date intToDate = modelEntityMap.getDateFromHours(dhToInt, timeZone);
		System.out.println(String.format("In: %s Out: %s",dateToTest, intToDate));
		Assert.assertEquals(dateToTest, intToDate);
	}

	private void setJanuaryEarliestTime(String timeZone) {
		Date jan = createDate(2014, 0, 10, 0, timeZone);
		setEarliestTime(jan);
	}

	private void setJanuaryEarliestTime() {
		setJanuaryEarliestTime("UTC");
	}

	private void setJulyEarliestTime(String timeZone) {
		Date jul = createDate(2013, 6, 10, 0, timeZone);
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

	
	private void setEarliestTime(Date earliestTime) {
		modelEntityMap.setEarliestDate(earliestTime);
		dateHelper.setEarliestTime(earliestTime);
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
