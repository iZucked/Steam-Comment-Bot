/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class PanamaCanalTariffImporter extends DefaultClassImporter {

	public class MarkupRate extends EObjectImpl {
		double markupRate;
	}

	public static final String MARKUP_RATE_KEY = "markuprate";

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

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
		
		final Collection<Map<String, String>> exportObjects = new LinkedList<>();
		exportObjects.addAll(exportObjects(tariff.getBands(), context));
		exportObjects.addAll(exportObjects(tariff.getAnnualTariffs(), context));
		
		{
			final Map<String, String> row = new HashMap<>();
			row.put(MARKUP_RATE_KEY, String.format("%0,1f", tariff.getMarkupRate()));
			exportObjects.add(row);
		}

		return exportObjects;

	}
}
