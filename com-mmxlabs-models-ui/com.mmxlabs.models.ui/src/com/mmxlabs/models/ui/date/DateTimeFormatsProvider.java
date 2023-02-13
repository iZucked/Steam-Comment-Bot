/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DateTimeFormatsProvider {

	public static final DateTimeFormatsProvider INSTANCE = new DateTimeFormatsProvider();

	private String yearMonthStringEdit = "MM/yyyy";
	private String yearMonthStringDisplay = "MM/yyyy";
	private String timeStringEdit = "HH:00";
	private String timeStringDisplay = "HH:mm";

	// Defaults are changed in constructor. Needed here to avoid Null analysis error
	private String dateStringEdit = "dd/MM/yyyy";
	private String dateStringDisplay = "dd/MM/yy";
	private String dateTimeStringEdit = "dd/MM/yyyy HH:00";
	private String dateTimeStringDisplay = "dd/MM/yy HH:mm";

	private DateTimeFormatsProvider() {
		// Get the system date format and determine if system is2 month/day or day/month
		final SimpleDateFormat sdf = new SimpleDateFormat();
		final String pattern = sdf.toPattern();
		final int monthIdx = pattern.indexOf('M');
		final int dayIdx = pattern.indexOf('d');
		if (dayIdx < monthIdx) {
			setDefaultDayMonthFormats();
		} else {
			setDefaultMonthDayFormats();
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
