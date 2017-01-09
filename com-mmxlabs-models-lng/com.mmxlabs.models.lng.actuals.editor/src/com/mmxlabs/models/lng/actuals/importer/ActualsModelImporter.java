/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class ActualsModelImporter extends DefaultClassImporter {
	private static final String KEY_LOADACTUAL = "buy";

	private static final String KEY_DISCHARGEACTUAL = "sell";

	@Override
	protected boolean shouldExportFeature(final EStructuralFeature feature) {

		if (feature == ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS) {
			return false;
		}
		return super.shouldExportFeature(feature);
	}

	public Collection<Map<String, String>> exportActuals(final Collection<CargoActuals> cargoes, final IMMXExportContext context) {

		final List<Map<String, String>> data = new LinkedList<Map<String, String>>();

		{
			for (final CargoActuals cargo : cargoes) {

				final List<LoadActuals> cLoadSlots = new ArrayList<LoadActuals>();
				final List<DischargeActuals> cDischargeSlots = new ArrayList<DischargeActuals>();
				// Slot Slots
				for (final SlotActuals s : cargo.getActuals()) {
					if (s instanceof LoadActuals) {
						cLoadSlots.add((LoadActuals) s);
					} else if (s instanceof DischargeActuals) {
						cDischargeSlots.add((DischargeActuals) s);
					}
				}

				// How many rows?
				final int rowCount = Math.max(cLoadSlots.size(), cDischargeSlots.size());

				// Export several rows of data depending on number of slots
				for (int i = 0; i < rowCount; ++i) {
					final Map<String, String> result = exportObject(cargo, context);

					if (i < cLoadSlots.size()) {
						exportSlot(context, result, cLoadSlots.get(i), KEY_LOADACTUAL);
					}
					if (i < cDischargeSlots.size()) {
						exportSlot(context, result, cDischargeSlots.get(i), KEY_DISCHARGEACTUAL);
					}

					result.put(KIND_KEY, cargo.eClass().getName());
					data.add(result);
				}
			}
		}

		return data;
	}

	/**
	 */
	protected void exportSlot(final IMMXExportContext context, final Map<String, String> result, final SlotActuals slot, final String referenceName) {
		final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(slot.eClass());
		if (importer != null) {
			final Map<String, String> subMap = importer.exportObjects(Collections.singleton(slot), context).iterator().next();
			for (final Map.Entry<String, String> e : subMap.entrySet()) {
				result.put(referenceName + DOT + e.getKey(), e.getValue());
			}
		}
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

		ImportResults importResult = super.importObject(parent, eClass, row, context);
		CargoActuals importedObject = (CargoActuals) importResult.importedObject;

		// Special case for load and discharge slots. These are not under the correct reference type string - so fake it here
		final IFieldMap fieldMap = new FieldMap(row);
		{
			final String lcrn = KEY_LOADACTUAL;
			final IFieldMap subKeys = fieldMap.getSubMap(lcrn + DOT);
			// final IFieldMap subKeys = renameCsvMapWithFieldNames((FieldMap) subMap);

			final EClass referenceType = ActualsPackage.eINSTANCE.getLoadActuals();
			final IClassImporter classImporter = importerRegistry.getClassImporter(referenceType);
			final Collection<EObject> values = classImporter.importObject(parent, referenceType, subKeys, context).getCreatedObjects();
			importedObject.getActuals().addAll((Collection<? extends SlotActuals>) values);
		}
		{
			final String lcrn = KEY_DISCHARGEACTUAL;
			final IFieldMap subKeys = fieldMap.getSubMap(lcrn + DOT);

			final EClass referenceType = ActualsPackage.eINSTANCE.getDischargeActuals();
			final IClassImporter classImporter = importerRegistry.getClassImporter(referenceType);
			final Collection<EObject> values = classImporter.importObject(parent, referenceType, subKeys, context).getCreatedObjects();
			importedObject.getActuals().addAll((Collection<? extends SlotActuals>) values);
		}
		return importResult;
	}

	@Override
	public Collection<EObject> importObjects(final EClass importClass, final CSVReader reader, final IMMXImportContext context) {
		final LinkedList<EObject> results = new LinkedList<EObject>();
		try {
			try {
				context.pushReader(reader);
				Map<String, String> row;
				final Map<String, CargoActuals> cargoMap = new HashMap<>();
				while ((row = reader.readRow(true)) != null) {

					// Import Row Data
					final Collection<EObject> result = importObject(null, importClass, row, context).getCreatedObjects();

					// Find the individual objects
					CargoActuals tmpCargo = null;
					for (final EObject o : result) {
						if (o instanceof CargoActuals) {
							tmpCargo = (CargoActuals) o;
						}
					}
					final List<EObject> newResults = new ArrayList<EObject>(3);

					if (tmpCargo == null) {
						continue;
					}
					if (tmpCargo.getCargoReference() == null || tmpCargo.getCargoReference().isEmpty()) {
						continue;
					}

					// Look up the "real cargo" - the first one found with this name/ID. Cargoes with multiple load or multiple discharges will be exported across multiple rows.
					CargoActuals realCargo = null;

					if (tmpCargo != null) {
						if (cargoMap.containsKey(tmpCargo.getCargoReference())) {
							realCargo = cargoMap.get(tmpCargo.getCargoReference());
						}
					}
					if (realCargo == null) {
						// Keep the cargo and this is the first time we have seen the cargo ID
						realCargo = tmpCargo;
						cargoMap.put(tmpCargo.getCargoReference(), tmpCargo);
						newResults.add(realCargo);
					} else {
						realCargo.getActuals().addAll(tmpCargo.getActuals());
					}

					results.addAll(newResults);
				}
			} finally {
				reader.close();
				context.popReader();
			}
		} catch (final IOException e) {

		}
		for (final EObject o : results) {
			if (o instanceof NamedObject) {
				context.registerNamedObject((NamedObject) o);
			}
		}
		return results;
	}
}