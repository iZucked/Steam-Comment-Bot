/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

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

	/**
	 * Attempt to parse the given string into a date using all known
	 * date / date+time formats in the current locale.
	 * @param dateString
	 * @return
	 */
	public Date parseDate(final String dateString) {
		final Locale locale = Locale.getDefault();

		//try parsing as a date and time
		for (int style = DateFormat.FULL; style <= DateFormat.SHORT; style++) {
			for (int timeStyle = DateFormat.FULL; timeStyle <= DateFormat.SHORT; timeStyle++) {
				DateFormat df = DateFormat.getDateTimeInstance(style, timeStyle, locale);
				try {
					Date result = df.parse(dateString);
					// either return "true", or return the Date obtained Date object
					return result;
				} catch (ParseException ex) {
					continue;
				}
			}
		}
		
		//maybe it's just a date?
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

		// last try - use ECore
		try {
			return (Date) EcoreFactory.eINSTANCE.createFromString(
					EcorePackage.eINSTANCE.getEDate(), dateString);
		} catch (final Exception ex) {
			return null;
		}
	}
}
