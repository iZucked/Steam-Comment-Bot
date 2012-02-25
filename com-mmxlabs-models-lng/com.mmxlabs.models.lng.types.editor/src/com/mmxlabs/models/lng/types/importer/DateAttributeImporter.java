package com.mmxlabs.models.lng.types.importer;

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
import com.mmxlabs.models.lng.types.dates.LocalDateUtil;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;

public class DateAttributeImporter extends DefaultAttributeImporter {
	final SimpleDateFormat consistentDate = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat consistentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:00");
	final SimpleDateFormat dateWithShortTime = new SimpleDateFormat("yyyy-MM-dd HH");

	public DateAttributeImporter() {
		final TimeZone utc = TimeZone.getTimeZone("UTC");
		consistentDate.setTimeZone(utc);
		consistentDateTime.setTimeZone(utc);
		dateWithShortTime.setTimeZone(utc);
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

	public String formatDate(final Date date, final TimeZone zone) {
		SimpleDateFormat c = (SimpleDateFormat) consistentDateTime.clone();
		c.setTimeZone(zone);
		return c.format(date);
	}

	
	@Override
	public void setAttribute(final EObject container, final EAttribute attribute,
			String value, IImportContext context) {
		super.setAttribute(container, attribute, value, context);
		// add deferred action to fix localization
		if (container instanceof ITimezoneProvider) {
		context.doLater(new IDeferment() {			
			@Override
			public void run(final IImportContext context) {
				if (attribute.isMany()) {
					final List<Date> dates = (List<Date>) container.eGet(attribute);
					final List<Date> fixedDates = new ArrayList<Date>(dates.size());
					for (final Date date : dates) fixedDates.add(fixDate(date));
					dates.clear();
					dates.addAll(fixedDates);
				} else {
					final Date date = (Date) container.eGet(attribute);
					container.eSet(attribute, fixDate(date));
				}
			}
			
			private Date fixDate(Date date) {
				if (date == null) return date;
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
				return 5;
			}
		});
		}
	}

	@Override
	protected Object attributeFromString(final EObject container,
			final EAttribute attribute, final String value, final IImportContext context) {
		try {
			final Date date = parseDate(value);
			return date;
		} catch (ParseException ex) {
			context.addProblem(context.createProblem("Could not parse date " + value, true, true, true));
			return null;
		}
	}

	@Override
	protected String stringFromAttribute(EObject container,
			EAttribute attribute, Object value) {
		if (attribute.isUnsettable() && container.eIsSet(attribute) == false) return "";
		return formatDate((Date) value, LocalDateUtil.getTimeZone(container, attribute));
	}
}
