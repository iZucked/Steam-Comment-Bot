/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.time.DMYUtil;

@NonNullByDefault
public class DateTimeFormatsProvider {

	public static final DateTimeFormatsProvider INSTANCE = new DateTimeFormatsProvider();
	
	private static final String MONTH_YEAR_FORMAT = "MM/yyyy";

	private String timeStringEdit = "HH:00";
	private String timeStringDisplay = "HH:mm";

	// Defaults are changed in constructor. Needed here to avoid Null analysis error
	private String dateStringEdit = "dd/MM/yyyy";
	private String dateStringDisplay = "dd/MM/yy";
	private String dateTimeStringEdit = "dd/MM/yyyy HH:00";
	private String dateTimeStringDisplay = "dd/MM/yy HH:mm";
	private String yearMonthStringEdit = MONTH_YEAR_FORMAT;
	private String yearMonthStringDisplay = MONTH_YEAR_FORMAT;

	private DateTimeFormatsProvider() {
		switch (DMYUtil.getDayMonthYearOrder()) {
		case DAY_MONTH_YEAR:
			dateStringEdit = "dd/MM/yyyy";
			dateStringDisplay = "dd/MM/yy";
			dateTimeStringEdit = "dd/MM/yyyy HH:00";
			dateTimeStringDisplay = "dd/MM/yy HH:mm";
			yearMonthStringEdit = MONTH_YEAR_FORMAT;
			yearMonthStringDisplay = MONTH_YEAR_FORMAT;
			break;
		case MONTH_DAY_YEAR:
			dateStringEdit = "MM/dd/yyyy";
			dateStringDisplay = "MM/dd/yy";
			dateTimeStringEdit = "MM/dd/yyyy HH:00";
			dateTimeStringDisplay = "MM/dd/yy HH:mm";
			yearMonthStringEdit = MONTH_YEAR_FORMAT;
			yearMonthStringDisplay = MONTH_YEAR_FORMAT;
			break;
		case YEAR_MONTH_DAY:
			dateStringEdit = "yyyy/MM/dd";
			dateStringDisplay = "yy/MM/dd";
			dateTimeStringEdit = "yyyy/MM/dd HH:00";
			dateTimeStringDisplay = "yy/MM/dd HH:mm";
			yearMonthStringEdit = "yyyy/MM";
			yearMonthStringDisplay = "yyyy/MM";
			break;
		default:
			break;
		}
	}

	public void setDefaultDayMonthFormats() {
		dateStringEdit = "dd/MM/yyyy";
		dateStringDisplay = "dd/MM/yy";
		dateTimeStringEdit = "dd/MM/yyyy HH:00";
		dateTimeStringDisplay = "dd/MM/yy HH:mm";
	}

	public void setDefaultMonthDayFormats() {
		dateStringEdit = "MM/dd/yyyy";
		dateStringDisplay = "MM/dd/yy";
		dateTimeStringEdit = "MM/dd/yyyy HH:00";
		dateTimeStringDisplay = "MM/dd/yy HH:mm";
	}

	public String getDisplayTimezone(final ZoneId zoneId) {
		return zoneId.getDisplayName(TextStyle.SHORT, Locale.getDefault());
	}

	public String getDisplayTimezone(final ZonedDateTime zonedDateTime) {
		return getDisplayTimezone(zonedDateTime.getZone());
	}

	public String getYearMonthStringEdit() {
		return yearMonthStringEdit;
	}

	public void setYearMonthStringEdit(final String yearMonthStringEdit) {
		this.yearMonthStringEdit = yearMonthStringEdit;
	}

	public String getYearMonthStringDisplay() {
		return yearMonthStringDisplay;
	}

	public void setYearMonthStringDisplay(final String yearMonthStringDisplay) {
		this.yearMonthStringDisplay = yearMonthStringDisplay;
	}

	public String getDateStringEdit() {
		return dateStringEdit;
	}

	public void setDateStringEdit(final String dateStringEdit) {
		this.dateStringEdit = dateStringEdit;
	}

	public String getDateStringDisplay() {
		return dateStringDisplay;
	}

	public void setDateStringDisplay(final String dateStringDisplay) {
		this.dateStringDisplay = dateStringDisplay;
	}

	public String getDateTimeStringEdit() {
		return dateTimeStringEdit;
	}

	public void setDateTimeStringEdit(final String dateTimeStringEdit) {
		this.dateTimeStringEdit = dateTimeStringEdit;
	}

	public String getDateTimeStringDisplay() {
		return dateTimeStringDisplay;
	}

	public void setDateTimeStringDisplay(final String dateTimeStringDisplay) {
		this.dateTimeStringDisplay = dateTimeStringDisplay;
	}

	public String getTimeStringEdit() {
		return timeStringEdit;
	}

	public void setTimeStringEdit(final String timeStringEdit) {
		this.timeStringEdit = timeStringEdit;
	}

	public String getTimeStringDisplay() {
		return timeStringDisplay;
	}

	public void setTimeStringDisplay(final String timeStringDisplay) {
		this.timeStringDisplay = timeStringDisplay;
	}

	public DateTimeFormatter createDateStringDisplayFormatter() {
		return DateTimeFormatter.ofPattern(dateStringDisplay);
	}

	public DateTimeFormatter createDateTimeStringDisplayFormatter() {
		return DateTimeFormatter.ofPattern(dateTimeStringDisplay);
	}

	public DateTimeFormatter createTimeStringDisplayFormatter() {
		return DateTimeFormatter.ofPattern(timeStringDisplay);
	}
}
