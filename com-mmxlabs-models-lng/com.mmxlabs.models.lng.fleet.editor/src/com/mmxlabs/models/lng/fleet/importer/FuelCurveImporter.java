/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.common.csv.IImportProblem;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.VesselConstants;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.NumberAttributeImporter;

/**
 * Special case for fuel curve data
 * 
 * @author hinton
 * 
 */
public class FuelCurveImporter {

	private static final String KEY_CLASS = "class";
	private static final String KEY_STATE = "state";

	public FuelCurveImporter() {

	}

	public Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> readConsumptions(final @NonNull CSVReader reader, final IImportContext context) throws IOException {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> result = new HashMap<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>>();
		Map<String, String> row = null;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow(true))) {
				final String vesselName = row.get(KEY_CLASS);
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MMX_REFERENCE_VESSELS) && vesselName != null && vesselName.matches(VesselConstants.REGEXP_MMX_PROVIDED_VESSEL_NAME)) {
					// Skip vessels that contain <> in the name since they are reserved characters for MMX reference vessels
					continue;
				}
				final String stateName = row.get(KEY_STATE);

				final List<FuelConsumption> consumptions = new LinkedList<>();
				for (final Map.Entry<String, String> column : row.entrySet()) {
					if (column.getKey() == null || column.getKey().isEmpty()) {
						continue;
					}
					if (KEY_CLASS.equals(column.getKey())) {
						continue;
					}
					if (KEY_STATE.equals(column.getKey())) {
						continue;
					}
					if (column.getValue() == null || column.getValue().isEmpty()) {
						continue;
					}
					try {
						final double speed = nai.stringToDouble(column.getKey(), FleetPackage.Literals.FUEL_CONSUMPTION__SPEED);
						final double consumption = nai.stringToDouble(column.getValue(), FleetPackage.Literals.FUEL_CONSUMPTION__CONSUMPTION);
						final FuelConsumption fcl = FleetFactory.eINSTANCE.createFuelConsumption();
						fcl.setSpeed(speed);
						fcl.setConsumption(consumption);
						consumptions.add(fcl);
					} catch (final NumberFormatException | ParseException e) {
					}
				}

				Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>> p = result.get(vesselName);
				if (p == null) {

					p = new Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>(context.createProblem("Unknown vessel: " + vesselName, true, true, false),
							new Pair<List<FuelConsumption>, List<FuelConsumption>>());
					result.put(vesselName, p);
				}
				if (stateName != null) {
					if (stateName.equalsIgnoreCase("laden")) {
						p.getSecond().setFirst(consumptions);
					} else if (stateName.equalsIgnoreCase("ballast")) {
						p.getSecond().setSecond(consumptions);
					} else {
						context.addProblem(context.createProblem("Unknown vessel state " + stateName, true, true, false));
					}
				}
			}
		} finally {
			context.popReader();
			reader.close();
		}
		return result;
	}

	public void importFuelConsumptions(final @NonNull CSVReader reader, final IMMXImportContext context) {
		try {
			final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> consumptions = readConsumptions(reader, context);

			for (final String s : consumptions.keySet()) {
				final Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>> c = consumptions.get(s);
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext importContext) {
						final IMMXImportContext context = (IMMXImportContext) importContext;
						final Vessel vessel = (Vessel) context.getNamedObject(s, FleetPackage.eINSTANCE.getVessel());
						if (vessel != null) {
							vessel.getLadenAttributes().getFuelConsumption().addAll(c.getSecond().getFirst());
							vessel.getBallastAttributes().getFuelConsumption().addAll(c.getSecond().getSecond());
						} else {
							context.addProblem(c.getFirst());
						}
					}

					@Override
					public int getStage() {
						return IMMXImportContext.STAGE_REFERENCES_RESOLVED;
					}
				});
			}
		} catch (final IOException ex) {
			context.createProblem("IO Exception: " + ex.getMessage(), false, false, false);
		}
	}

	public Collection<Map<String, String>> exportCurves(final EList<Vessel> vessels, final IExportContext context) {
		final List<Map<String, String>> rows = new ArrayList<>(vessels.size() * 2);
		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		// Use a LinkedHashMap to preserve put order, use a TreeMap to sort columns by speed

		for (final Vessel vessel : vessels) {
			final Map<String, String> ladenRow = new LinkedHashMap<>();
			final Map<String, String> ladenRowValues = new TreeMap<>();
			ladenRow.put(KEY_CLASS, vessel.getName());
			ladenRow.put(KEY_STATE, "laden");
			exportConsumptions(vessel.getLadenAttributes().getVesselOrDelegateFuelConsumption(), ladenRowValues, nai);
			ladenRow.putAll(ladenRowValues);
			rows.add(ladenRow);

			final Map<String, String> ballastRow = new LinkedHashMap<>();
			final Map<String, String> ballastRowValues = new TreeMap<>();
			ballastRow.put(KEY_CLASS, vessel.getName());
			ballastRow.put(KEY_STATE, "ballast");
			exportConsumptions(vessel.getBallastAttributes().getVesselOrDelegateFuelConsumption(), ballastRowValues, nai);
			ballastRow.putAll(ballastRowValues);
			rows.add(ballastRow);
		}

		return rows;
	}

	private void exportConsumptions(final EList<FuelConsumption> fuelConsumption, final Map<String, String> ladenRow, final NumberAttributeImporter nai) {
		for (final FuelConsumption fc : fuelConsumption) {
			ladenRow.put(nai.doubleToString(fc.getSpeed(), FleetPackage.Literals.FUEL_CONSUMPTION__SPEED),
					nai.doubleToString(fc.getConsumption(), FleetPackage.Literals.FUEL_CONSUMPTION__CONSUMPTION));
		}
	}
}
