/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;

/**
 * Special case for fuel curve data
 * 
 * @author hinton
 *
 */
public class FuelCurveImporter {
	public FuelCurveImporter() {
		
	}
	
	public void importFuelConsumptions(final CSVReader reader, final IImportContext context) {
		Map<String, String> row = null;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow())) {
				final String className = row.get("class");
				final String stateName = row.get("state");
				
				final List<FuelConsumption> consumptions = new LinkedList<FuelConsumption>();
				for (final Map.Entry<String, String> column : row.entrySet()) {
					try {
						final float speed = Float.parseFloat(column.getKey());
						final float consumption = Float.parseFloat(column.getValue());
						final FuelConsumption fcl = FleetFactory.eINSTANCE.createFuelConsumption();
						fcl.setSpeed(speed);
						fcl.setConsumption(consumption);
						consumptions.add(fcl);
					} catch (final NumberFormatException nfe) {

					}
				}
				
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext context) {
						final VesselClass vesselClass = (VesselClass)
								context.getNamedObject(className, FleetPackage.eINSTANCE.getVesselClass());
						if (vesselClass != null) {
							if (stateName.equalsIgnoreCase("laden")) {
								if (vesselClass.getLadenAttributes() == null) {
									vesselClass.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
								}
								vesselClass.getLadenAttributes().getFuelConsumption().addAll(consumptions);
							} else {
								if (vesselClass.getLadenAttributes() == null) {
									vesselClass.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
								}
								vesselClass.getBallastAttributes().getFuelConsumption().addAll(consumptions);
							}
						}
					}
					
					@Override
					public int getStage() {
						return 4;
					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				context.popReader();
				reader.close();
			} catch (IOException e) {
			}
		}
	}
}
