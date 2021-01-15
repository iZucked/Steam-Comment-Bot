/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.AbstractClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;
import com.mmxlabs.models.util.importer.impl.NumberAttributeImporter;

public class DatePointImporter extends AbstractClassImporter {

	private final LocalDateAttributeImporter dateParser = new LocalDateAttributeImporter();

	private NumberImporterHelper numberImporterHelper = new NumberImporterHelper();

	private List<DatePoint> importDatePoints(@NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore, @NonNull final IImportContext context) {
		final List<DatePoint> points = new LinkedList<>();
		final Set<LocalDate> seenDates = new HashSet<>();

		for (final String s : row.keySet()) {
			if (columnsToIgnore.contains(s)) {
				continue;
			}

			final LocalDate date;
			try {
				date = parseDateString(s);
			} catch (final ParseException ex) {
				context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
				continue;
			}
			if (date == null) {
				continue;
			}
			final String valueStr = row.get(s);
			if (valueStr.isEmpty())
				continue;
			try {
				final Number n = numberImporterHelper.parseDoubleString(valueStr);
				if (n == null) {
					context.addProblem(context.createProblem("The field " + s + " is not a number", true, false, true));
					continue;
				}

				if (!seenDates.add(date)) {
					context.addProblem(context.createProblem("The date " + s + " is defined multiple times", true, true, true));
					continue;
				}

				final DatePoint point = PricingFactory.eINSTANCE.createDatePoint();
				point.setDate(date);
				point.setValue(n.doubleValue());
				points.add(point);
			} catch (final NumberFormatException | ParseException nfe) {
				context.addProblem(context.createProblem("The value " + valueStr + " is not a number", true, true, true));
			}
		}

		return points;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context) {
		DatePointContainer result = PricingFactory.eINSTANCE.createDatePointContainer();
		if (row.containsKey("name")) {
			result.setName(row.get("name"));
		} else {
			context.addProblem(context.createProblem(String.format("Settled priced name is missing"), true, true, true));
		}
		result.getPoints().addAll(importDatePoints(row, Collections.singleton("name"), context));

		return new ImportResults(result, true);
	}

	private Comparator<String> getFieldNameOrderComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(final String arg0, final String arg1) {
				return arg0.compareTo(arg1);
			}
		};
	}

	private Map<String, String> getNonDateFields(final DatePointContainer index) {
		final HashMap<String, String> result = new HashMap<>();
		result.put("name", index.getName());
		return result;
	}

	private Map<String, String> getDateFields(final IExportContext context, final DatePointContainer datePointContainer) {
		final Map<String, String> map = new HashMap<String, String>();
		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		for (final DatePoint pt : datePointContainer.getPoints()) {
			final String value = nai.doubleToString(pt.getValue(), PricingPackage.Literals.DATE_POINT__VALUE);
			map.put(dateParser.formatLocalDate(pt.getDate()), value);
		}

		return map;
	}

	@Nullable
	private LocalDate parseDateString(final String s) throws ParseException {
		try {
			return dateParser.parseLocalDate(s);
		} catch (final Exception e) {
			return null;
		}
	}

	@Override
	public @NonNull Collection<Map<String, String>> exportObjects(@NonNull final Collection<? extends EObject> objects, @NonNull final IMMXExportContext context) {
		final List<Map<String, String>> result = new ArrayList<>();
		for (final EObject obj : objects) {

			if (obj instanceof DatePointContainer) {
				final DatePointContainer datePointContainer = (DatePointContainer) obj;
				final Map<String, String> row = new LinkedHashMap<>();
				row.putAll(getNonDateFields(datePointContainer));
				// Sorted dates
				final Map<String, String> dateFields = new TreeMap<>(getFieldNameOrderComparator());
				dateFields.putAll(getDateFields(context, datePointContainer));
				// Convert to linked hash map now data is sorted.
				row.putAll(dateFields);
				result.add(new LinkedHashMap<>(row));
			}
		}
		return result;
	}

}
