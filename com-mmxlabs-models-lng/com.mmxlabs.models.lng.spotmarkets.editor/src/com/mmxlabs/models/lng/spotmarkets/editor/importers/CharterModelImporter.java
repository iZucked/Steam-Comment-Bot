/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CharterModelImporter extends DefaultClassImporter {

	private static final String START_DATE_KEY = "charteroutstartdate";
	private final DateAttributeImporter dateAttributeImporter = new DateAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IImportContext context) {
		if (row.get(START_DATE_KEY) != null && !row.get(START_DATE_KEY).trim().isEmpty()) {
			final CharterOutStartDate charterOutStartDate = SpotMarketsFactory.eINSTANCE.createCharterOutStartDate();
			try {
				charterOutStartDate.setCharterOutStartDate(dateAttributeImporter.parseDate(row.get(START_DATE_KEY)));
			} catch (final ParseException e) {
				context.addProblem(context.createProblem("Unable to parse date " + row.get(START_DATE_KEY), true, true, true));
				return new ImportResults(null);
			}
			final ImportResults results = new ImportResults(charterOutStartDate);
			return results;
		} else {
			return super.importObject(parent, eClass, row, context);
		}
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IExportContext context) {

		final Collection<Map<String, String>> exportedObjects = new LinkedList<>();

		final List<EObject> generalExportObject = new LinkedList<>();
		for (final EObject obj : objects) {
			if (obj instanceof CharterOutStartDate) {
				final CharterOutStartDate charterOutStartDate = (CharterOutStartDate) obj;
				final Map<String, String> dateRow = new HashMap<String, String>();

				dateRow.put(START_DATE_KEY, dateAttributeImporter.formatDate(charterOutStartDate.getCharterOutStartDate(), TimeZone.getTimeZone("UTC"), false));
				exportedObjects.add(dateRow);
			} else {
				generalExportObject.add(obj);
			}
		}

		exportedObjects.addAll(super.exportObjects(generalExportObject, context));
		return exportedObjects;
	}
}
