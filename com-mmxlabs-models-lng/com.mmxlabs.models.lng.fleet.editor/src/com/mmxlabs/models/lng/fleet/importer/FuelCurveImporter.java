/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;

/**
 * Special case for fuel curve data
 * 
 * @author hinton
 * @since 2.0
 *
 */
public class FuelCurveImporter {
	public FuelCurveImporter() {
		
	}
	
	public Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> readConsumptions(final CSVReader reader, final IImportContext context) throws IOException {
		final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> result = new HashMap<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>>();
		Map<String, String> row = null;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow())) {
				final String className = row.get("class");
				final String stateName = row.get("state");
				
				final List<FuelConsumption> consumptions = new LinkedList<FuelConsumption>();
				for (final Map.Entry<String, String> column : row.entrySet()) {
					try {
						final double speed = Double.parseDouble(column.getKey());
						final double consumption = Double.parseDouble(column.getValue());
						final FuelConsumption fcl = FleetFactory.eINSTANCE.createFuelConsumption();
						fcl.setSpeed(speed);
						fcl.setConsumption((int) consumption);
						consumptions.add(fcl);
					} catch (final NumberFormatException nfe) {

					}
				}
				
				Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>> p = result.get(className);
				if (p == null) {
					
					p = new Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>(
							context.createProblem("Unknown vessel class: " + className, true, true, false),
							new Pair<List<FuelConsumption>, List<FuelConsumption>>()
							);
					result.put(className, p);
				}
				if (stateName.equalsIgnoreCase("laden")) {
					p.getSecond().setFirst(consumptions);
				} else if (stateName.equalsIgnoreCase("ballast")) {
					p.getSecond().setSecond(consumptions);
				} else {
					context.addProblem(context.createProblem("Unknown vessel state " + stateName, true, true, false));
				}
			}
		} finally {
			context.popReader();
			reader.close();
		}
		return result;
	}
	
	public void importFuelConsumptions(final CSVReader reader, final IImportContext context) {
		try {
			final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> consumptions = readConsumptions(reader, context);
		
			for (final String s : consumptions.keySet()) {
				final Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>> c = consumptions.get(s);
				context.doLater(new IDeferment() {
					@Override
					public void run(IImportContext context) {
						final VesselClass vesselClass = (VesselClass)
								context.getNamedObject(s, FleetPackage.eINSTANCE.getVesselClass());
						if (vesselClass != null) {
							vesselClass.getLadenAttributes().getFuelConsumption().addAll(c.getSecond().getFirst());
							vesselClass.getBallastAttributes().getFuelConsumption().addAll(c.getSecond().getSecond());
						} else {
							context.addProblem(c.getFirst());
						}
					}
					
					@Override
					public int getStage() {
						return IImportContext.STAGE_REFERENCES_RESOLVED;
					}
				});
			}
		} catch (final IOException ex) {
			context.createProblem("IO Exception: " + ex.getMessage(), false, false, false);
		}
	}

	public Collection<Map<String, String>> exportCurves(final EList<VesselClass> vesselClasses) {
		final List<Map<String, String>> rows = new ArrayList<Map<String, String>>(vesselClasses.size() * 2);
		
		// Use a LinkedHashMap to preserve put order, use a TreeMap to sort columns by speed
		
		for (final VesselClass vc : vesselClasses) {
			final Map<String, String> ladenRow = new LinkedHashMap<String, String>();
			final Map<String, String> ladenRowValues = new TreeMap<String, String>();
			ladenRow.put("class", vc.getName());
			ladenRow.put("state", "laden");
			exportConsumptions(vc.getLadenAttributes().getFuelConsumption(), ladenRowValues);
			ladenRow.putAll(ladenRowValues);
			rows.add(ladenRow);
			
			final Map<String, String> ballastRow = new LinkedHashMap<String, String>();
			final Map<String, String> ballastRowValues = new TreeMap<String, String>();
			ballastRow.put("class", vc.getName());
			ballastRow.put("state", "ballast");
			exportConsumptions(vc.getBallastAttributes().getFuelConsumption(), ballastRowValues);
			ballastRow.putAll(ballastRowValues);
			rows.add(ballastRow);
		}
		
		return rows;
	}

	private void exportConsumptions(EList<FuelConsumption> fuelConsumption, Map<String, String> ladenRow) {
		for (final FuelConsumption fc : fuelConsumption) {
			ladenRow.put(String.format("%.2f", fc.getSpeed()), Integer.toString(fc.getConsumption()));
		}
	}
}
