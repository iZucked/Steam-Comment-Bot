/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.HolidayCalendarEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.AbstractClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

public class HolidayCalendarImporter extends AbstractClassImporter {

	private static final String NAME = "name";
	private static final String DATE = "date";
	private static final String DESCRIPTION = "description";
	
	private final LocalDateAttributeImporter dateParser = new LocalDateAttributeImporter();
	
	private final Map<String, HolidayCalendar> calendars;
	
	public HolidayCalendarImporter() {
		calendars = new LinkedHashMap<>();
	}
	
	public List<HolidayCalendar> importObjects(@NonNull final CSVReader reader, @NonNull final IMMXImportContext context) {
		try {
			try {
				context.pushReader(reader);
				Map<String, String> row;
				
				while ((row = reader.readRow(true)) != null) {
					String calendarName = row.get(NAME);
					if(calendarName == null) {
						continue;
					}
					final HolidayCalendar hc;
					if (seen(calendarName)) {
						hc = calendars.get(calendarName);
					} else {
						hc = PricingFactory.eINSTANCE.createHolidayCalendar();
						hc.setName(calendarName);
						calendars.put(calendarName, hc);
						context.registerNamedObject(hc);
					}
					if (row.get(DATE).length() == 0) {
						final String desc = row.get(DESCRIPTION);
						hc.setDescription(desc == null ? "" : desc);
					} else {
						final HolidayCalendarEntry hce = createFromDateComment(row.get(DATE), row.get(DESCRIPTION));
						if (!hc.getEntries().contains(hce))
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

	private HolidayCalendarEntry createFromDateComment(String date, String comment) {
		HolidayCalendarEntry ih = PricingFactory.eINSTANCE.createHolidayCalendarEntry();
		LocalDate ld = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date));
		ih.setDate(ld);
		ih.setComment(comment);
		return ih;
	}

	public @NonNull Collection<Map<String, String>> exportObjects(@NonNull final Collection<? extends EObject> objects, @NonNull final IMMXExportContext context) {
		final List<Map<String, String>> result = new ArrayList<>();
		for (final EObject obj : objects) {
			if (obj instanceof HolidayCalendar) {
				final HolidayCalendar hc = (HolidayCalendar) obj;
				final String name = hc.getName();
				final Map<String, String> firstRow = new LinkedHashMap<>();
				
				firstRow.put(NAME, name);
				firstRow.put(DESCRIPTION, hc.getDescription());
				result.add(new LinkedHashMap<>(firstRow));
				for (final HolidayCalendarEntry hce : hc.getEntries()) {
					final Map<String, String> row = new LinkedHashMap<>();
					row.put(NAME, name);
					row.put(DATE, dateParser.formatLocalDate(hce.getDate()));
					row.put(DESCRIPTION, hce.getComment());
					result.add(new LinkedHashMap<>(row));
				}
			}
		}
		return result;
	}

	@Override
	public @NonNull ImportResults importObject(@Nullable EObject parent, @NonNull EClass targetClass, @NonNull Map<String, String> row, @NonNull IMMXImportContext context) {
		
		String calendarName = row.get(NAME);
		if(calendarName == null) {
			context.addProblem(context.createProblem(String.format("Holiday calendar name is missing"), true, true, true));
		}
		final HolidayCalendar hc;
		if (seen(calendarName)) {
			hc = calendars.get(calendarName);
		} else {
			hc = PricingFactory.eINSTANCE.createHolidayCalendar();
			hc.setName(calendarName);
			calendars.put(calendarName, hc);
			context.registerNamedObject(hc);
		}
		if (row.get(DATE).length() == 0) {
			final String desc = row.get(DESCRIPTION);
			hc.setDescription(desc == null ? "" : desc);
		} else {
			final HolidayCalendarEntry hce = createFromDateComment(row.get(DATE), row.get(DESCRIPTION));
			if (!hc.getEntries().contains(hce))
				hc.getEntries().add(hce);
		}
		
		return new ImportResults(hc, true);
	}

}
