/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * Attempts to parse a date/time automatically
 * 
 * @author Tom Hinton
 * 
 */
public class DateTimeParser {
	private final static DateTimeParser INSTANCE = new DateTimeParser();
	final SimpleDateFormat consistentDate = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat consistentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:00");
	final SimpleDateFormat dateWithShortTime = new SimpleDateFormat("yyyy-MM-dd HH");

	protected DateTimeParser() {
		final TimeZone utc = TimeZone.getTimeZone("UTC");
		consistentDate.setTimeZone(utc);
		consistentDateTime.setTimeZone(utc);
		dateWithShortTime.setTimeZone(utc);
	}

	public static DateTimeParser getInstance() {
		return INSTANCE;
	}

	/**
	 * Try and parse it as one of the date time strings above, or as an ecore date.
	 * 
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public Date parseDate(final String dateString) throws ParseException {
		// try parsing as a date and time
		try {
			final Date result = consistentDateTime.parse(dateString);
			return result;
		} catch (final ParseException ex) {
		}

		try {
			final Date result = dateWithShortTime.parse(dateString);
			return result;
		} catch (final ParseException ex) {
		}

		try {
			final Date result = consistentDate.parse(dateString);
			return result;
		} catch (final ParseException ex) {
		}

		// last try - use ECore
		try {
			return (Date) EcoreFactory.eINSTANCE.createFromString(EcorePackage.eINSTANCE.getEDate(), dateString);
		} catch (final Exception ex) {
			throw new ParseException("Could not parse date value " + dateString, 0);
		}
	}

	public String formatDate(final Date date, final String zone) {
		SimpleDateFormat c = (SimpleDateFormat) consistentDateTime.clone();

		if (date instanceof DateAndOptionalTime) {
			if (((DateAndOptionalTime) date).isOnlyDate()) {
				c = (SimpleDateFormat) consistentDate.clone();
			}
		}

		c.setTimeZone(TimeZone.getTimeZone(zone));
		return c.format(date);
	}

	public DateAndOptionalTime parseDateAndOptionalTime(final String dateString) {
		// try parsing as a date and time
		try {
			final Date result = consistentDateTime.parse(dateString);
			return new DateAndOptionalTime(result, false);
		} catch (final ParseException ex) {
		}

		try {
			final Date result = dateWithShortTime.parse(dateString);
			return new DateAndOptionalTime(result, false);
		} catch (final ParseException ex) {
		}

		try {
			final Date result = consistentDate.parse(dateString);
			return new DateAndOptionalTime(result, true);
		} catch (final ParseException ex) {
		}

		// last try - use ECore
		try {
			return new DateAndOptionalTime((Date) EcoreFactory.eINSTANCE.createFromString(EcorePackage.eINSTANCE.getEDate(), dateString), false);
		} catch (final Exception ex) {
			return null;
		}
	}
}
