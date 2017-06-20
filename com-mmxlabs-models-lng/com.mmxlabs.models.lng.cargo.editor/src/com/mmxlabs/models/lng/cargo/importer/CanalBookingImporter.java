/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CanalBookingImporter extends DefaultClassImporter {

	private static final String KEY_STRICT_DAYS = "strictdays";
	private static final String KEY_RELAXED_DAYS = "relaxeddays";
	private static final String KEY_FLEX_COUNT = "flexibility";
	private static final String KEY_MARGIN = "margin";

	private final DefaultAttributeImporter attributeImporter = new DefaultAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

		final String strictDaysValue = row.get(KEY_STRICT_DAYS);
		if (strictDaysValue != null && !strictDaysValue.trim().isEmpty()) {
			final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
			attributeImporter.setAttribute(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS, strictDaysValue, context);

			final String relaxedDaysValue = row.get(KEY_RELAXED_DAYS);
			if (relaxedDaysValue != null && !relaxedDaysValue.trim().isEmpty()) {
				attributeImporter.setAttribute(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS, relaxedDaysValue, context);
			}

			final String flexCountValue = row.get(KEY_FLEX_COUNT);
			if (flexCountValue != null && !flexCountValue.trim().isEmpty()) {
				attributeImporter.setAttribute(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT, flexCountValue, context);
			}

			final String marginValue = row.get(KEY_MARGIN);
			if (marginValue != null && !marginValue.trim().isEmpty()) {
				attributeImporter.setAttribute(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS, marginValue, context);
			}

			return new ImportResults(canalBookings);

		} else {
			return super.importObject(parent, eClass, row, context);
		}
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IMMXExportContext context) {

		final Collection<Map<String, String>> exportedObjects = new LinkedList<>();

		final List<EObject> generalExportObject = new LinkedList<>();
		for (final EObject obj : objects) {

			if (obj instanceof CanalBookings) {
				final CanalBookings canalBookings = (CanalBookings) obj;
				final Map<String, String> rowData = new LinkedHashMap<String, String>();
				rowData.put(KEY_STRICT_DAYS, Integer.toString(canalBookings.getStrictBoundaryOffsetDays()));
				rowData.put(KEY_RELAXED_DAYS, Integer.toString(canalBookings.getRelaxedBoundaryOffsetDays()));
				rowData.put(KEY_FLEX_COUNT, Integer.toString(canalBookings.getFlexibleBookingAmount()));
				rowData.put(KEY_MARGIN, Integer.toString(canalBookings.getArrivalMarginHours()));
				exportedObjects.add(rowData);
			} else {
				generalExportObject.add(obj);
			}
		}

		exportedObjects.addAll(super.exportObjects(generalExportObject, context));
		return exportedObjects;
	}
}