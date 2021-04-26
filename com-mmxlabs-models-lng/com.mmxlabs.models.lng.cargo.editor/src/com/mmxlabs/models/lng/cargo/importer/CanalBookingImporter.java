/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CanalBookingImporter extends DefaultClassImporter {

	private static final String KEY_MARGIN = "margin";
	private final DefaultAttributeImporter attributeImporter = new DefaultAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final String marginValue = row.get(KEY_MARGIN);
		final String kind = row.get(super.KIND_KEY);
		
		if (marginValue != null && !marginValue.trim().isEmpty()) {
			final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
			attributeImporter.setAttribute(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS, marginValue, context);
			return new ImportResults(canalBookings);
		}
		else if (Objects.equals(kind, VesselGroupCanalParameters.class.getSimpleName())) {
			return super.importObject(parent, CargoPackage.eINSTANCE.getVesselGroupCanalParameters(), row, context);
		}
		else {
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