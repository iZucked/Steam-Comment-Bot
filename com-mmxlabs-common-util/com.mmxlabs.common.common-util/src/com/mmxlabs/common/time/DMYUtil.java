package com.mmxlabs.common.time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Utility class to determine locale day / month /year order.
 * 
 * @ See https://stackoverflow.com/questions/57205910/determine-if-users-locale-is-day-month-year-or-month-day-year
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class DMYUtil {

	public enum DayMonthOrder {
		DAY_MONTH, MONTH_DAY
	}

	public enum DayMonthYearOrder {
		DAY_MONTH_YEAR, MONTH_DAY_YEAR, YEAR_MONTH_DAY
	}

	private static String getDMYString(final Locale locale) {
		final String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.SHORT, LocalDate.now().getChronology(), locale);
		return pattern.replaceAll("[^yMd]|(?<=(.))\\1", "").toUpperCase();
	}

	public static DayMonthOrder getDayMonthOrder() {
		return getDayMonthOrder(Locale.getDefault());
	}

	public static DayMonthOrder getDayMonthOrder(final Locale locale) {
		final String dmy = getDMYString(locale);
		if (dmy.contains("DM")) {
			return DayMonthOrder.DAY_MONTH;
		} else if (dmy.contains("MD")) {
			return DayMonthOrder.MONTH_DAY;
		} else {
			throw new IllegalStateException();
		}
	}

	public static DayMonthYearOrder getDayMonthYearOrder() {
		return getDayMonthYearOrder(Locale.getDefault());
	}

	public static DayMonthYearOrder getDayMonthYearOrder(final Locale locale) {
		final String dmy = getDMYString(locale);
		if (dmy.equals("DMY")) {
			return DayMonthYearOrder.DAY_MONTH_YEAR;
		} else if (dmy.equals("MDY")) {
			return DayMonthYearOrder.MONTH_DAY_YEAR;
		} else if (dmy.equals("YMD")) {
			return DayMonthYearOrder.YEAR_MONTH_DAY;
		} else {
			throw new IllegalStateException();
		}
	}
}
