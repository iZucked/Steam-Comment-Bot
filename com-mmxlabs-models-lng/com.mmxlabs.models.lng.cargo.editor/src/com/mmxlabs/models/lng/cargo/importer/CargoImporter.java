/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.FieldMap;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 2.0
 */
public class CargoImporter extends DefaultClassImporter {
	private static final String KEY_LOADSLOT = "buy";

	private static final String KEY_DISCHARGESLOT = "sell";

	private static final String ASSIGNMENT = "assignment";

	// List of column names to filter out of export. UUID is confusing to the user, othernames is not used by cargo or slots. Fixed Price is deprecated.
	private static final Set<String> filteredColumns = Sets.newHashSet("uuid", "otherNames", "loadSlot.uuid", "loadSlot.fixedPrice", "loadSlot.otherNames", "dischargeSlot.uuid",
			"dischargeSlot.fixedPrice", "dischargeSlot.otherNames");

	private static final Map<String, String> fieldNamesToCsvNames = getFieldNamesToCsvNames();
	private static final Map<String, String> csvNamesToFieldNames = reverseLookup(fieldNamesToCsvNames);

	/**
	 * Returns a map mapping field names to their CSV aliases.
	 * 
	 * @return
	 */
	private static Map<String, String> getFieldNamesToCsvNames() {
		final Map<String, String> result = new HashMap<String, String>();

		result.put("minQuantity", "min");
		result.put("maxQuantity", "max");
		result.put("windowStart", "date");
		result.put("windowStartTime", "time");
		result.put("priceExpression", "price");

		return result;
	}

	protected Map<String, String> exportObject(final EObject object, final MMXRootObject root) {
		final Map<String, String> result = new LinkedHashMap<String, String>();

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
			if (shouldExportFeature(attribute)) {
				exportAttribute(object, attribute, result);
			}
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (shouldExportFeature(reference)) {
				exportReference(object, reference, result, root);
			}
		}

		return result;
	}

	private static Map<String, String> reverseLookup(final Map<String, String> map) {
		final Map<String, String> result = new HashMap<String, String>();
		for (final Entry<String, String> entry : map.entrySet()) {
			result.put(entry.getValue(), entry.getKey());
		}
		return result;
	}

	@Override
	protected boolean shouldExportFeature(final EStructuralFeature feature) {

		if (feature == CargoPackage.eINSTANCE.getCargo_Slots()) {
			return false;
		}
		if (feature == CargoPackage.eINSTANCE.getSlot_Cargo()) {
			return false;
		}
		return super.shouldExportFeature(feature);
	}

	public Collection<Map<String, String>> exportObjects(final Collection<Cargo> cargoes, final Collection<LoadSlot> loadSlots, final Collection<DischargeSlot> dischargeSlots, final MMXRootObject root) {

		final List<Map<String, String>> data = new LinkedList<Map<String, String>>();

		{
			for (final Cargo cargo : cargoes) {

				final List<LoadSlot> cLoadSlots = new ArrayList<LoadSlot>();
				final List<DischargeSlot> cDischargeSlots = new ArrayList<DischargeSlot>();
				// Slot Slots
				for (final Slot s : cargo.getSlots()) {
					if (s instanceof LoadSlot) {
						cLoadSlots.add((LoadSlot) s);
					} else if (s instanceof DischargeSlot) {
						cDischargeSlots.add((DischargeSlot) s);
					}
				}

				// How many rows?
				final int rowCount = Math.max(cLoadSlots.size(), cDischargeSlots.size());

				// Export several rows of data depending on number of slots
				for (int i = 0; i < rowCount; ++i) {
					final Map<String, String> result = exportObject(cargo, root);

					if (i < cLoadSlots.size()) {
						exportSlot(root, result, cLoadSlots.get(i), KEY_LOADSLOT);
					}
					if (i < cDischargeSlots.size()) {
						exportSlot(root, result, cDischargeSlots.get(i), KEY_DISCHARGESLOT);
					}

					result.put(KIND_KEY, cargo.eClass().getName());
					data.add(result);
				}
			}
		}

		{
			for (final LoadSlot slot : loadSlots) {
				if (slot.getCargo() == null) {
					final Map<String, String> result = new LinkedHashMap<String, String>();
					exportSlot(root, result, slot, KEY_LOADSLOT);
					/*
					*/
					data.add(result);
				}
			}
		}
		{
			for (final DischargeSlot slot : dischargeSlots) {
				if (slot.getCargo() == null) {
					final Map<String, String> result = new LinkedHashMap<String, String>();
					exportSlot(root, result, slot, KEY_DISCHARGESLOT);
					/*
					*/
					data.add(result);
				}
			}
		}

		// Filter Data
		for (final Map<String, String> map : data) {
			// Remove any matching columns
			for (final String filteredKey : filteredColumns) {
				map.remove(filteredKey);
			}
		}

		return data;
	}

	/**
	 * @since 3.1
	 */
	protected void exportSlot(final MMXRootObject rootObject, final Map<String, String> result, final Slot slot, final String referenceName) {
		final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(slot.eClass());
		if (importer != null) {
			final Map<String, String> subMap = importer.exportObjects(Collections.singleton(slot), rootObject).iterator().next();
			for (final Map.Entry<String, String> e : subMap.entrySet()) {
				result.put(referenceName + DOT + csvNameFromFieldName(e.getKey(), slot), e.getValue());
			}
		}
	}

	private String csvNameFromFieldName(final String fieldName, final Slot slot) {
		// special cases
		if (slot instanceof LoadSlot && fieldName.equals("DESPurchase")) {
			return "DES";
		}
		if (slot instanceof DischargeSlot && fieldName.equals("FOBSale")) {
			return "FOB";
		}

		final String result = fieldNamesToCsvNames.get(fieldName);
		if (result == null)
			return fieldName;
		return result;
	}

	private IFieldMap renameCsvMapWithFieldNames(final FieldMap map) {
		final Map<String, String> resultMap = new HashMap<String, String>();
		for (final Entry<String, String> entry : map.entrySet()) {
			resultMap.put(fieldNameFromCsvName(entry.getKey()), entry.getValue());
		}

		return new FieldMap(resultMap, map.getPrefix(), map.getSuperMap());
	}

	@Override
	public Collection<EObject> importObject(final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final Collection<EObject> result = importRawObject(eClass, row, context);
		LoadSlot load = null;
		DischargeSlot discharge = null;
		Cargo cargo = null;
		for (final EObject o : result) {
			if (o instanceof Cargo) {
				cargo = (Cargo) o;
			} else if (o instanceof LoadSlot) {
				load = (LoadSlot) o;
			} else if (o instanceof DischargeSlot) {
				discharge = (DischargeSlot) o;
			}
		}

		// fix missing names

		final List<EObject> newResults = new ArrayList<EObject>(3);
		boolean keepCargo = true;
		if (load == null || load.getWindowStart() == null) {
			keepCargo = false;
		} else {
			newResults.add(load);
		}
		if (discharge == null || discharge.getWindowStart() == null) {
			keepCargo = false;
		} else {
			newResults.add(discharge);
		}

		if (!keepCargo) {
			cargo.getSlots().clear();
		} else {
			cargo.getSlots().add(load);
			cargo.getSlots().add(discharge);
		}
		// Always return cargo object for LDD style cargo import
		newResults.add(cargo);

		return newResults;
	}

	@Override
	public Collection<EObject> importObjects(final EClass importClass, final CSVReader reader, final IImportContext context) {
		final LinkedList<EObject> results = new LinkedList<EObject>();
		try {
			try {
				context.pushReader(reader);
				Map<String, String> row;
				final Map<String, Cargo> cargoMap = new HashMap<String, Cargo>();
				while ((row = reader.readRow()) != null) {
					// Import Row Data
					final Collection<EObject> result = importObject(importClass, row, context);

					// Find the individual objects
					LoadSlot load = null;
					DischargeSlot discharge = null;
					Cargo tmpCargo = null;
					for (final EObject o : result) {
						if (o instanceof Cargo) {
							tmpCargo = (Cargo) o;
						} else if (o instanceof LoadSlot) {
							load = (LoadSlot) o;
						} else if (o instanceof DischargeSlot) {
							discharge = (DischargeSlot) o;
						}
					}
					final List<EObject> newResults = new ArrayList<EObject>(3);

					boolean keepCargo = true;
					if (load == null || load.getWindowStart() == null) {
						load = null;
						keepCargo = false;
					} else {
						newResults.add(load);
					}
					if (discharge == null || discharge.getWindowStart() == null) {
						discharge = null;
						keepCargo = false;
					} else {
						newResults.add(discharge);
					}

					// Look up the "real cargo" - the first one found with this name/ID. Cargoes with multiple load or multiple discharges will be exported across multiple rows.
					Cargo realCargo = null;
					if (tmpCargo != null) {
						if (cargoMap.containsKey(tmpCargo.getName())) {
							realCargo = cargoMap.get(tmpCargo.getName());
						}
						// Always clear slots at this information will be updated below
						tmpCargo.getSlots().clear();
					}

					if (!keepCargo) {
						// Do nothing
					} else if (realCargo == null) {
						// Keep the cargo and this is the first time we have seen the cargo ID
						realCargo = tmpCargo;
						cargoMap.put(tmpCargo.getName(), tmpCargo);
						newResults.add(realCargo);
					}

					// Register Slots
					if (realCargo != null) {
						if (load != null) {
							realCargo.getSlots().add(load);
						}
						if (discharge != null) {
							realCargo.getSlots().add(discharge);
						}
					}

					// fix missing names
					if (realCargo != null && realCargo.getName() != null && realCargo.getName().trim().isEmpty() == false) {
						final String cargoName = realCargo.getName().trim();
						if (load != null && (load.getName() == null || load.getName().trim().isEmpty())) {
							final LoadSlot load2 = load;
							load2.setName(cargoName);
						}

						if (discharge != null && (discharge.getName() == null || discharge.getName().trim().isEmpty())) {
							final DischargeSlot discharge2 = discharge;
							discharge2.setName("d-" + cargoName);
						}
					}

					// Setup vessel assignments
					if (keepCargo && row.containsKey(ASSIGNMENT)) {
						final Cargo cargo_ = realCargo;
						final String assignedTo = row.get(ASSIGNMENT);
						final IImportProblem noVessel = context.createProblem("Cannot find vessel " + assignedTo, true, true, true);
						context.doLater(new IDeferment() {
							@Override
							public void run(final IImportContext context) {
								final MMXRootObject rootObject = context.getRootObject();
								if (rootObject instanceof LNGScenarioModel) {
									final AssignmentModel assignmentModel = ((LNGScenarioModel) rootObject).getPortfolioModel().getAssignmentModel();
									if (assignmentModel != null) {
										ElementAssignment existing = null;
										for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
											if (ea.getAssignedObject() == cargo_) {
												existing = ea;
												break;
											}
										}
										if (existing == null) {
											existing = AssignmentFactory.eINSTANCE.createElementAssignment();
											assignmentModel.getElementAssignments().add(existing);
											existing.setAssignedObject(cargo_);
										}

										// attempt to find vessel
										final NamedObject vessel = context.getNamedObject(assignedTo, FleetPackage.eINSTANCE.getVessel());
										if (vessel instanceof Vessel) {
											existing.setAssignment((Vessel) vessel);
										} else {
											context.addProblem(noVessel);
										}
									}
								}
							}

							@Override
							public int getStage() {
								return IImportContext.STAGE_MODIFY_SUBMODELS + 10;
							}
						});
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

	private String fieldNameFromCsvName(final String csvName) {
		// special cases

		if (csvName.equalsIgnoreCase("FOB")) {
			return "fobsale";
		}
		if (csvName.equalsIgnoreCase("DES")) {
			return "despurchase";
		}
		String result = csvNamesToFieldNames.get(csvName);
		if (result == null) {
			result = csvName;
		}
		return result.toLowerCase();

	}

	public Collection<EObject> importRawObject(final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final List<EObject> objects = new LinkedList<EObject>();
		objects.addAll(super.importObject(eClass, row, context));

		// Special case for load and discharge slots. These are not under the correct reference type string - so fake it here
		final IFieldMap fieldMap = new FieldMap(row);
		{
			final String lcrn = KEY_LOADSLOT;
			final IFieldMap subMap = fieldMap.getSubMap(lcrn + DOT);
			final IFieldMap subKeys = renameCsvMapWithFieldNames((FieldMap) subMap);

			final EClass referenceType = CargoPackage.eINSTANCE.getLoadSlot();
			final IClassImporter classImporter = importerRegistry.getClassImporter(referenceType);
			final Collection<EObject> values = classImporter.importObject(referenceType, subKeys, context);
			objects.addAll(values);
		}
		{
			final String lcrn = KEY_DISCHARGESLOT;
			final IFieldMap subKeys = renameCsvMapWithFieldNames((FieldMap) fieldMap.getSubMap(lcrn + DOT));

			final EClass referenceType = CargoPackage.eINSTANCE.getDischargeSlot();
			final IClassImporter classImporter = importerRegistry.getClassImporter(referenceType);
			final Collection<EObject> values = classImporter.importObject(referenceType, subKeys, context);
			objects.addAll(values);
		}
		return objects;
	}
}