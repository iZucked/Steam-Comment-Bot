package com.mmxlabs.common.time;

import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Lists;
import com.mmxlabs.common.time.DMYUtil.DayMonthOrder;
import com.mmxlabs.common.time.DMYUtil.DayMonthYearOrder;

public class DMYUtilTests {

	record DayMonthData(Locale locale, DMYUtil.DayMonthOrder order) {
	};

	private static Collection<DayMonthData> daymonthdata() {
		return Lists.newArrayList( //
				new DayMonthData(Locale.UK, DayMonthOrder.DAY_MONTH), //
				new DayMonthData(Locale.US, DayMonthOrder.MONTH_DAY), //
				new DayMonthData(Locale.JAPAN, DayMonthOrder.MONTH_DAY), //
				new DayMonthData(Locale.forLanguageTag("en-AU"), DayMonthOrder.DAY_MONTH), // Australia
				new DayMonthData(Locale.forLanguageTag("zh-SG"), DayMonthOrder.DAY_MONTH) // Singapore
		);
	}

	@ParameterizedTest
	@MethodSource("daymonthdata")
	void testDayMonth(DayMonthData data) {
		Assertions.assertEquals(data.order, DMYUtil.getDayMonthOrder(data.locale));
	}

	record DayMonthYearData(Locale locale, DMYUtil.DayMonthYearOrder order) {
	};

	private static Collection<DayMonthYearData> daymonthyeardata() {
		return Lists.newArrayList( //
				new DayMonthYearData(Locale.UK, DayMonthYearOrder.DAY_MONTH_YEAR), //
				new DayMonthYearData(Locale.US, DayMonthYearOrder.MONTH_DAY_YEAR), //
				new DayMonthYearData(Locale.JAPAN, DayMonthYearOrder.YEAR_MONTH_DAY), //
				new DayMonthYearData(Locale.forLanguageTag("en-AU"), DayMonthYearOrder.DAY_MONTH_YEAR), // Australia
				new DayMonthYearData(Locale.forLanguageTag("zh-SG"), DayMonthYearOrder.DAY_MONTH_YEAR) // Singapore
		);
	}

	@ParameterizedTest
	@MethodSource("daymonthyeardata")
	void testDayMonth(DayMonthYearData data) {
		Assertions.assertEquals(data.order, DMYUtil.getDayMonthYearOrder(data.locale));
	}

}
