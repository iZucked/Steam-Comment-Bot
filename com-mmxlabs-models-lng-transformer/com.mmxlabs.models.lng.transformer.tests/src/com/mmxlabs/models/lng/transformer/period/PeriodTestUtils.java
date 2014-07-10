package com.mmxlabs.models.lng.transformer.period;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class PeriodTestUtils {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	public static Date createDate(final int year, final int month, final int day, final int hour) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.clear();
	
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	static Date createDate(final int year, final int month, final int day) {
		return PeriodTestUtils.createDate(year, month, day, 0);
	}

}
