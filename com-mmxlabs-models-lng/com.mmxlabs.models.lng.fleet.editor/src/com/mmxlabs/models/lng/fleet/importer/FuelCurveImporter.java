/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.NumberAttributeImporter;

/**
 * Special case for fuel curve data
 * 
 * @author hinton
 * 
 */
public class FuelCurveImporter {
	public FuelCurveImporter() {

	}

	public Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> readConsumptions(final @NonNull CSVReader reader, final IImportContext context) throws IOException {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> result = new HashMap<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>>();
		Map<String, String> row = null;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow(true))) {
				final String className = row.get("class");
				final String stateName = row.get("state");

				final List<FuelConsumption> consumptions = new LinkedList<FuelConsumption>();
				for (final Map.Entry<String, String> column : row.entrySet()) {
					if (column.getKey() == null || column.getKey().isEmpty()) {
						continue;
					}
					if ("class".equals(column.getKey())) {
						continue;
					}
					if ("state".equals(column.getKey())) {
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

				Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>> p = result.get(className);
				if (p == null) {

					p = new Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>(context.createProblem("Unknown vessel class: " + className, true, true, false),
							new Pair<List<FuelConsumption>, List<FuelConsumption>>());
					result.put(className, p);
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
						final VesselClass vesselClass = (VesselClass) context.getNamedObject(s, FleetPackage.eINSTANCE.getVesselClass());
						if (vesselClass != null) {
							vesselClass.getLadenAttributes().getFuelConsumption().addAll(c.getSecond().getFirst());
							vesselClass.getBallastAttributes().getFuelConsumption().addAll(c.getSecond().getSecond());
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

	public Collection<Map<String, String>> exportCurves(final EList<VesselClass> vesselClasses, final IExportContext context) {
		final List<Map<String, String>> rows = new ArrayList<Map<String, String>>(vesselClasses.size() * 2);
		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		// Use a LinkedHashMap to preserve put order, use a TreeMap to sort columns by speed

		for (final VesselClass vc : vesselClasses) {
			final Map<String, String> ladenRow = new LinkedHashMap<String, String>();
			final Map<String, String> ladenRowValues = new TreeMap<String, String>();
			ladenRow.put("class", vc.getName());
			ladenRow.put("state", "laden");
			exportConsumptions(vc.getLadenAttributes().getFuelConsumption(), ladenRowValues, nai);
			ladenRow.putAll(ladenRowValues);
			rows.add(ladenRow);

			final Map<String, String> ballastRow = new LinkedHashMap<String, String>();
			final Map<String, String> ballastRowValues = new TreeMap<String, String>();
			ballastRow.put("class", vc.getName());
			ballastRow.put("state", "ballast");
			exportConsumptions(vc.getBallastAttributes().getFuelConsumption(), ballastRowValues, nai);
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
