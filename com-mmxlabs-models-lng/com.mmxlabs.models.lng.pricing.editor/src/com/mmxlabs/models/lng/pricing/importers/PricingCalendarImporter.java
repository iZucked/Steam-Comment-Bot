/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.datetime.importers.YearMonthAttributeImporter;
import com.mmxlabs.models.lng.pricing.PricingCalendarEntry;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class PricingCalendarImporter {

	private static final String NAME = "name";
	private static final String START = "start";
	private static final String END = "end";
	private static final String MONTH = "month";
	private static final String DESCRIPTION = "description";
	
	private final LocalDateAttributeImporter dateParser = new LocalDateAttributeImporter();
	protected final YearMonthAttributeImporter dateParser2 = new YearMonthAttributeImporter();
	
	private final Map<String, PricingCalendar> calendars;
	
	public PricingCalendarImporter() {
		calendars = new LinkedHashMap<>();
	}
	
	public List<PricingCalendar> importObjects(@NonNull final CSVReader reader, @NonNull final IMMXImportContext context) {
		try {
			try {
				context.pushReader(reader);
				Map<String, String> row;
				
				while ((row = reader.readRow(true)) != null) {
					String calendarName = row.get(NAME);
					if(calendarName == null) {
						continue;
					}
					final PricingCalendar hc;
					if (seen(calendarName)) {
						hc = calendars.get(calendarName);
					} else {
						hc = PricingFactory.eINSTANCE.createPricingCalendar();
						hc.setName(calendarName);
						calendars.put(calendarName, hc);
						context.registerNamedObject(hc);
					}
					if (row.get(START).length() == 0) {
						final String desc = row.get(DESCRIPTION);
						hc.setDescription(desc == null ? "" : desc);
					} else {
						final PricingCalendarEntry hce = createFromDateComment(context, row.get(START), row.get(END), row.get(MONTH),row.get(DESCRIPTION));
						if (hce != null && !hc.getEntries().contains(hce))
							hc.getEntries().add(hce);
					}
				}
				
			} finally {
				reader.close();
				context.popReader();
			}
		} catch (final IOException e) {

		}
		return new ArrayList(calendars.values());
	}
	
	private boolean seen(String calendarName) {
		return calendars.containsKey(calendarName);
	}

	private PricingCalendarEntry createFromDateComment(@NonNull final IMMXImportContext context, String start, String end, String month, String comment) {
		PricingCalendarEntry ih = PricingFactory.eINSTANCE.createPricingCalendarEntry();
		LocalDate ls = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(start));
		LocalDate le = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(end));
		
		YearMonth date = null;
		try {
			date = parseDateString(month);
		} catch (final ParseException ex) {
			context.addProblem(context.createProblem("The field " + month + " is not a date", true, false, true));
			return null;
		}
		
		ih.setStart(ls);
		ih.setEnd(le);
		ih.setMonth(date);
		ih.setComment(comment);
		return ih;
	}

	public @NonNull Collection<Map<String, String>> exportObjects(@NonNull final Collection<? extends EObject> objects, @NonNull final IMMXExportContext context) {
		final List<Map<String, String>> result = new ArrayList<>();
		for (final EObject obj : objects) {
			if (obj instanceof PricingCalendar) {
				final PricingCalendar hc = (PricingCalendar) obj;
				final String name = hc.getName();
				final Map<String, String> firstRow = new LinkedHashMap<>();
				
				firstRow.put(NAME, name);
				firstRow.put(DESCRIPTION, hc.getDescription());
				result.add(new LinkedHashMap<>(firstRow));
				for (final PricingCalendarEntry hce : hc.getEntries()) {
					final Map<String, String> row = new LinkedHashMap<>();
					row.put(NAME, name);
					row.put(START, dateParser.formatLocalDate(hce.getStart()));
					row.put(END, dateParser.formatLocalDate(hce.getEnd()));
					row.put(MONTH, dateParser2.formatYearMonth(hce.getMonth()));
					row.put(DESCRIPTION, hce.getComment());
					result.add(new LinkedHashMap<>(row));
				}
			}
		}
		return result;
	}
	
	@Nullable
	private YearMonth parseDateString(final String s) throws ParseException {
		try {
			return dateParser2.parseYearMonth(s);
		} catch (final Exception e) {
			return YearMonth.of(dateParser.parseLocalDate(s).getYear(), dateParser.parseLocalDate(s).getMonthValue());
		}
	}

}
