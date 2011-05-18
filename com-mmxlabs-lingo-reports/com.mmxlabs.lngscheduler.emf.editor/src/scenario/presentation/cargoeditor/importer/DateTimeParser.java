/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Attempts to parse a date/time automatically
 * 
 * @author Tom Hinton
 * 
 */
public class DateTimeParser {
	private final static DateTimeParser INSTANCE = new DateTimeParser();

	protected DateTimeParser() {

	}

	public static DateTimeParser getInstance() {
		return INSTANCE;
	}

	public Date parseDate(final String dateString) {
		final Locale locale = Locale.getDefault();

		for (int style = DateFormat.FULL; style <= DateFormat.SHORT; style++) {
			DateFormat df = DateFormat.getDateInstance(style, locale);
			try {
				Date result = df.parse(dateString);
				// either return "true", or return the Date obtained Date object
				return result;
			} catch (ParseException ex) {
				continue;
			}
		}

		return null;
	}
}
