/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import java.util.function.BiConsumer;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Sets;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class CargoImporter extends DefaultClassImporter {
	private static final String KEY_CARGONAME = "name";
	private static final String KEY_LOADSLOT = "buy";
	private static final String KEY_DISCHARGESLOT = "sell";

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
		final Map<String, String> result = new HashMap<>();

		result.put("minQuantity", "min");
		result.put("maxQuantity", "max");
		result.put("windowStart", "date");
		result.put("windowStartTime", "time");
		result.put("priceExpression", "price");

		return result;
	}

	@Override
	protected boolean shouldImportReference(final EReference reference) {
		return reference != CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE;
	}

	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = new LinkedHashMap<>();

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {

			if (object instanceof AssignableElement) {
				// yes yes both attribute and reference here, but easier to copy paste....
				if (attribute == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX || attribute == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT
						|| attribute == CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED) {
					continue;
				}
			}

			if (shouldExportFeature(attribute)) {
				exportAttribute(object, attribute, result, context);
			}
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {

			if (object instanceof AssignableElement) {
				// yes yes both attribute and reference here, but easier to copy paste....
				if (reference == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
					continue;
				}
			}

			if (shouldExportFeature(reference)) {
				exportReference(object, reference, result, context);
			}
		}

		return result;
	}

	private static Map<String, String> reverseLookup(final Map<String, String> map) {
		final Map<String, String> result = new HashMap<>();
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

	public Collection<Map<String, String>> exportObjects(final Collection<Cargo> cargoes, final Collection<LoadSlot> loadSlots, final Collection<DischargeSlot> dischargeSlots,
			final IMMXExportContext context) {

		final List<Map<String, String>> data = new LinkedList<>();

		{
			for (final Cargo cargo : cargoes) {

				final List<LoadSlot> cLoadSlots = new ArrayList<>();
				final List<DischargeSlot> cDischargeSlots = new ArrayList<>();
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
					final Map<String, String> result = exportObject(cargo, context);

					if (i < cLoadSlots.size()) {
						exportSlot(context, result, cLoadSlots.get(i), KEY_LOADSLOT);
					}
					if (i < cDischargeSlots.size()) {
						exportSlot(context, result, cDischargeSlots.get(i), KEY_DISCHARGESLOT);
					}

					result.put(KIND_KEY, cargo.eClass().getName());
					result.put(KEY_CARGONAME, cargo.getLoadName());
					data.add(result);
				}
			}
		}

		{
			for (final LoadSlot slot : loadSlots) {
				if (slot.getCargo() == null) {
					final Map<String, String> result = new LinkedHashMap<>();
					exportSlot(context, result, slot, KEY_LOADSLOT);

					data.add(result);
				}
			}
		}
		{
			for (final DischargeSlot slot : dischargeSlots) {
				if (slot.getCargo() == null) {
					final Map<String, String> result = new LinkedHashMap<>();
					exportSlot(context, result, slot, KEY_DISCHARGESLOT);

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
	 */
	protected void exportSlot(final IMMXExportContext context, final Map<String, String> result, final Slot slot, final String referenceName) {
		final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(slot.eClass());
		if (importer != null) {
			final Map<String, String> subMap = importer.exportObjects(Collections.singleton(slot), context).iterator().next();
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
		final Map<String, String> resultMap = new HashMap<>();
		for (final Entry<String, String> entry : map.entrySet()) {
			resultMap.put(fieldNameFromCsvName(entry.getKey()), entry.getValue());
		}

		return new FieldMap(resultMap, map.getPrefix(), map.getSuperMap());
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final Collection<EObject> result = importRawObject(parent, eClass, row, context);
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
		
		//Both slots can't be spot
		if (load instanceof SpotSlot && discharge instanceof SpotSlot) {
			return new ImportResults(cargo, false);
		}

		// fix missing names

		// TODO: sort out this hack. Is the identity of the "row object" important
		// when returning from this method?
		// Always return cargo object for LDD style cargo import
		final ImportResults newResult = new ImportResults(cargo);

		boolean keepCargo = true;
		if (load == null || load.getWindowStart() == null || load.getName() == null) {
			keepCargo = false;
		} else {
			newResult.add(load);
		}
		if (discharge == null || discharge.getWindowStart() == null || discharge.getName() == null) {
			keepCargo = false;
		} else {
			newResult.add(discharge);
		}

		if (!keepCargo) {
			cargo.getSlots().clear();
		} else {
			cargo.getSlots().add(load);
			cargo.getSlots().add(discharge);
		}

		final LoadSlot fLoad = load;
		final DischargeSlot fDischarge = discharge;

		final String buyMarket = row.get("buy.market");
		final String sellMarket = row.get("sell.market");

		context.doLater(new IDeferment() {

			@Override
			public void run(final IImportContext importContext) {
				final IMMXImportContext context = (IMMXImportContext) importContext;
				if (fLoad instanceof SpotLoadSlot) {

					final SpotLoadSlot sls = (SpotLoadSlot) fLoad;
					SpotMarket market = null;
					if (sls.isDESPurchase()) {
						market = (SpotMarket) context.getNamedObject(buyMarket, SpotMarketsPackage.Literals.DES_PURCHASE_MARKET);
					} else {
						market = (SpotMarket) context.getNamedObject(buyMarket, SpotMarketsPackage.Literals.FOB_PURCHASES_MARKET);
					}
					sls.setMarket(market);

				}
				if (fDischarge instanceof SpotDischargeSlot) {
					final SpotDischargeSlot sds = (SpotDischargeSlot) fDischarge;
					SpotMarket market = null;
					if (sds.isFOBSale()) {
						market = (SpotMarket) context.getNamedObject(sellMarket, SpotMarketsPackage.Literals.FOB_SALES_MARKET);

					} else {
						market = (SpotMarket) context.getNamedObject(sellMarket, SpotMarketsPackage.Literals.DES_SALES_MARKET);
					}
					sds.setMarket(market);
				}
			}

			@Override
			public int getStage() {
				return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
			}
		});

		final Cargo fCargo = cargo;
		final String vesselName = row.get("vessel");
		if (keepCargo && vesselName != null && !vesselName.isEmpty()) {
			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext importContext) {
					// Do not replace value from assignments import
					if (fCargo.getVesselAssignmentType() == null) {
						final IMMXImportContext context = (IMMXImportContext) importContext;
						final MMXRootObject rootObject = context.getRootObject();
						if (rootObject instanceof LNGScenarioModel) {
							final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
							final CargoModel cargoModel = scenarioModel.getCargoModel();
							for (final VesselCharter va : cargoModel.getVesselCharters()) {
								// Only use charter index of 1.
								if (va.getCharterNumber() == 1) {
									final Vessel vessel = va.getVessel();
									if (vessel != null && vesselName.equalsIgnoreCase(vessel.getName())) {
										fCargo.setVesselAssignmentType(va);
										break;
									}
								}
							}
						}
					}
				}

				@Override
				public int getStage() {
					// Delay slightly as assigment importer works in this stage
					return IMMXImportContext.STAGE_MODIFY_SUBMODELS + 1;
				}
			});
		}
		return newResult;
	}

	@Override
	public Collection<EObject> importObjects(final EClass importClass, final CSVReader reader, final IMMXImportContext context) {
		final LinkedList<EObject> results = new LinkedList<>();
		try {
			try {
				context.pushReader(reader);
				Map<String, String> row;
				final Map<String, Cargo> cargoMap = new HashMap<>();
				while ((row = reader.readRow(true)) != null) {

					// Import Row Data
					final Collection<EObject> result = importObject(null, importClass, row, context).getCreatedObjects();

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
					final List<EObject> newResults = new ArrayList<>(3);

					// the row data has a "name" field even though cargo objects do not have names
					// this is used to allow multi-line specifications of e.g. LDD cargoes
					String realCargoName = row.get(KEY_CARGONAME);
					if (realCargoName == null || realCargoName.isEmpty()) {
						realCargoName = row.get("buy.name");
					}

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
						if (cargoMap.containsKey(realCargoName)) {
							realCargo = cargoMap.get(realCargoName);
						}
						// Always clear slots at this information will be updated below
						tmpCargo.getSlots().clear();
					}

					if (!keepCargo) {
						// Do nothing
					} else if (realCargo == null) {
						// Keep the cargo and this is the first time we have seen the cargo ID
						realCargo = tmpCargo;
						cargoMap.put(realCargoName, tmpCargo);
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
					if (realCargo != null && realCargoName != null && !realCargoName.trim().isEmpty()) {
						final String cargoName = realCargoName.trim();
						if (load != null && (load.getName() == null || load.getName().trim().isEmpty())) {
							final LoadSlot load2 = load;
							load2.setName(cargoName);
						}

						if (discharge != null && (discharge.getName() == null || discharge.getName().trim().isEmpty())) {
							final DischargeSlot discharge2 = discharge;
							discharge2.setName("d-" + cargoName);
						}
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

	/**
	 */
	public Collection<EObject> importRawObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final List<EObject> objects = new LinkedList<>();
		objects.addAll(super.importObject(parent, eClass, row, context).getCreatedObjects());

		// Special case for load and discharge slots. These are not under the correct reference type string - so fake it here
		final IFieldMap fieldMap = new FieldMap(row);
		BiConsumer<String, EClass> func = (lcrn, referenceType) -> {
			final IFieldMap subMap = fieldMap.getSubMap(lcrn + DOT);
			final IFieldMap subKeys = renameCsvMapWithFieldNames((FieldMap) subMap);

			final IClassImporter classImporter = importerRegistry.getClassImporter(referenceType);
			final Collection<EObject> values = classImporter.importObject(parent, referenceType, subKeys, context).getCreatedObjects();
			objects.addAll(values);
		};
		func.accept(KEY_LOADSLOT, CargoPackage.eINSTANCE.getLoadSlot());
		func.accept(KEY_DISCHARGESLOT, CargoPackage.eINSTANCE.getDischargeSlot());

		return objects;
	}
}