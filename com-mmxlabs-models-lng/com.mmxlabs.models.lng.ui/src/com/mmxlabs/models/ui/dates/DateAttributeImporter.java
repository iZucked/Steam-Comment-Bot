/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;

public class DateAttributeImporter extends DefaultAttributeImporter {
	private final SimpleDateFormat consistentDate = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat consistentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:00");
	private final SimpleDateFormat dateWithShortTime = new SimpleDateFormat("yyyy-MM-dd HH");

	private final SimpleDateFormat consistentSlashDate = new SimpleDateFormat("yyyy/MM/dd");
	private final SimpleDateFormat consistentSlashDateTime = new SimpleDateFormat("yyyy/MM/dd HH:00");
	private final SimpleDateFormat slashDateWithShortTime = new SimpleDateFormat("yyyy/MM/dd HH");

	private final SimpleDateFormat dayMonthYearDate = new SimpleDateFormat("dd-MM-yyyy");
	private final SimpleDateFormat dayMonthYearSlashDate = new SimpleDateFormat("dd/MM/yyyy");

	private final DateFormat localeDate = DateFormat.getDateInstance();
	private final DateFormat localeDateTime = DateFormat.getDateTimeInstance();

	private final DateFormat[] tryOrder = new DateFormat[] { consistentDateTime, dateWithShortTime, consistentDate, consistentSlashDateTime, slashDateWithShortTime, consistentSlashDate,
			dayMonthYearDate, dayMonthYearSlashDate, localeDateTime, localeDate };

	public DateAttributeImporter() {
		final TimeZone utc = TimeZone.getTimeZone("UTC");
		for (final DateFormat df : tryOrder) {
			df.setTimeZone(utc);
			df.setLenient(false);
		}
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
		for (final DateFormat df : tryOrder) {
			try {
				final Date result = df.parse(dateString.trim());
				return result;
			} catch (final ParseException ex) {
			}
		}

		// last try - use ECore
		try {
			return (Date) EcoreFactory.eINSTANCE.createFromString(EcorePackage.eINSTANCE.getEDate(), dateString);
		} catch (final Exception ex) {
			throw new ParseException("Could not parse date value " + dateString, 0);
		}
	}

	public String formatDate(final Date date, final TimeZone zone) {
		final SimpleDateFormat c = (SimpleDateFormat) consistentDateTime.clone();
		c.setTimeZone(zone);
		return c.format(date);
	}

	@Override
	public void setAttribute(final EObject container, final EAttribute attribute, final String value, final IImportContext context) {
		super.setAttribute(container, attribute, value, context);
		// add deferred action to fix localization
		if (container instanceof ITimezoneProvider) {
			context.doLater(new IDeferment() {
				@Override
				public void run(final IImportContext context) {
					if (attribute.isMany()) {
						final List<Date> dates = (List<Date>) container.eGet(attribute);
						final List<Date> fixedDates = new ArrayList<Date>(dates.size());
						for (final Date date : dates)
							fixedDates.add(fixDate(date));
						dates.clear();
						dates.addAll(fixedDates);
					} else {
						final Date date = (Date) container.eGet(attribute);
						container.eSet(attribute, fixDate(date));
					}
				}

				private Date fixDate(final Date date) {
					if (date == null)
						return date;
					final TimeZone zone = LocalDateUtil.getTimeZone(container, attribute);
					final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00");
					format.setTimeZone(TimeZone.getTimeZone("UTC"));
					final String s = format.format(date);

					format.setTimeZone(zone);
					try {
						final Date d2 = format.parse(s);
						return d2;
					} catch (final ParseException e) {
						return date;
					}
				}

				@Override
				public int getStage() {
					return IImportContext.STAGE_REFERENCES_RESOLVED;
				}
			});
		}
	}

	@Override
	protected Object attributeFromString(final EObject container, final EAttribute attribute, final String value, final IImportContext context) {
		try {
			final Date date = parseDate(value);
			return date;
		} catch (final ParseException ex) {
			context.addProblem(context.createProblem("Could not parse date " + value, true, true, true));
			return null;
		}
	}

	@Override
	protected String stringFromAttribute(final EObject container, final EAttribute attribute, final Object value) {
		if (attribute.isUnsettable() && container.eIsSet(attribute) == false)
			return "";
		return formatDate((Date) value, LocalDateUtil.getTimeZone(container, attribute));
	}
}
