/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class PanamaCanalTariffImporter extends DefaultClassImporter {

	public class AvailableFrom extends EObjectImpl {
		LocalDate availableFrom;
	}

	public class MarkupRate extends EObjectImpl {
		double markupRate;
	}

	public static final String AVAILABLE_FROM_KEY = "availablefrom";

	public static final String MARKUP_RATE_KEY = "markuprate";

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

		if (row.containsKey(AVAILABLE_FROM_KEY)) {
			final String dateString = row.get(AVAILABLE_FROM_KEY);
			if (dateString != null && !dateString.isEmpty()) {
				LocalDate localDate;
				try {
					localDate = new LocalDateAttributeImporter().parseLocalDate(dateString);

					final AvailableFrom data = new AvailableFrom();
					data.availableFrom = localDate;
					return new ImportResults(data);
				} catch (final ParseException e) {
					context.createProblem("Unable to parse available from date", true, true, true);
					return new ImportResults(null);
				}
			}
		}

		if (row.containsKey(MARKUP_RATE_KEY)) {
			final String rateString = row.get(MARKUP_RATE_KEY);
			if (rateString != null && !rateString.isEmpty()) {
				Double rate;
				try {
					rate = Double.parseDouble(rateString);

					final MarkupRate data = new MarkupRate();
					data.markupRate = rate;
					return new ImportResults(data);
				} catch (final NumberFormatException e) {
					context.createProblem("Unable to parse markup rate", true, true, true);
					return new ImportResults(null);
				}
			}
		}

		return super.importObject(parent, eClass, row, context);
	}

	public Collection<Map<String, String>> exportTariff(final PanamaCanalTariff tariff, final IMMXExportContext context) {
		final Collection<Map<String, String>> exportObjects = exportObjects(tariff.getBands(), context);
		if (tariff.isSetAvailableFrom()) {
			final String dateString = new LocalDateAttributeImporter().formatLocalDate(tariff.getAvailableFrom());
			final Map<String, String> row = new HashMap<>();
			row.put(AVAILABLE_FROM_KEY, dateString);
			exportObjects.add(row);
		}
		{
			final Map<String, String> row = new HashMap<>();
			row.put(MARKUP_RATE_KEY, String.format("%0,1f", tariff.getMarkupRate()));
			exportObjects.add(row);
		}

		return exportObjects;

	}
}
