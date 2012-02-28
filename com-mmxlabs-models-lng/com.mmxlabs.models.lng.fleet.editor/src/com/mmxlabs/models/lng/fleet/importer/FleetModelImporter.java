/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public class FleetModelImporter implements ISubmodelImporter {
	public static final String VESSELS_KEY = "VESSELS";
	public static final String VESSEL_CLASSES_KEY = "VESSELCLASSES";
	public static final String FUELS_KEY = "BASEFUELS";
	public static final String EVENTS_KEY = "EVENTS";
	private static final String CURVES_KEY = "CONSUMPTION_CURVES";
	
	private IClassImporter vesselImporter = Activator.getDefault().getImporterRegistry().getClassImporter(FleetPackage.eINSTANCE.getVessel());
	private IClassImporter vesselClassImporter = Activator.getDefault().getImporterRegistry().getClassImporter(FleetPackage.eINSTANCE.getVesselClass());
	private IClassImporter vesselEventImporter = Activator.getDefault().getImporterRegistry().getClassImporter(FleetPackage.eINSTANCE.getVesselEvent());
	private IClassImporter baseFuelImporter = Activator.getDefault().getImporterRegistry().getClassImporter(FleetPackage.eINSTANCE.getBaseFuel());
	private FuelCurveImporter fuelCurveImporter = new FuelCurveImporter();
	
	private static final Map<String, String> inputs = new HashMap<String, String>();
	static {
		inputs.put(VESSELS_KEY, "Vessels");
		inputs.put(VESSEL_CLASSES_KEY, "Vessel Classes");
		inputs.put(FUELS_KEY, "Base Fuels");
		inputs.put(EVENTS_KEY, "Events");
		inputs.put(CURVES_KEY, "Consumption Curves");
	}
	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();
		
		if  (inputs.containsKey(VESSELS_KEY))
			fleetModel.getVessels().addAll((Collection<? extends Vessel>) vesselImporter.importObjects(FleetPackage.eINSTANCE.getVessel(), inputs.get(VESSELS_KEY), context));
		
		if  (inputs.containsKey(VESSEL_CLASSES_KEY))
			fleetModel.getVesselClasses().addAll((Collection<? extends VesselClass>) vesselClassImporter.importObjects(FleetPackage.eINSTANCE.getVesselClass(), inputs.get(VESSEL_CLASSES_KEY), context));
		
		if  (inputs.containsKey(EVENTS_KEY))
			fleetModel.getVesselEvents().addAll((Collection<? extends VesselEvent>) vesselEventImporter.importObjects(FleetPackage.eINSTANCE.getVesselEvent(), inputs.get(EVENTS_KEY), context));
		
		if  (inputs.containsKey(FUELS_KEY))
			fleetModel.getBaseFuels().addAll((Collection<? extends BaseFuel>) baseFuelImporter.importObjects(FleetPackage.eINSTANCE.getBaseFuel(), inputs.get(FUELS_KEY), context));
		
		if (inputs.containsKey(CURVES_KEY)) fuelCurveImporter.importFuelConsumptions(inputs.get(CURVES_KEY), context);
		
		return fleetModel;
	}

	@Override
	public void exportModel(UUIDObject model,
			Map<String, Collection<Map<String, String>>> output) {
		final FleetModel fleetModel = (FleetModel) model;
		output.put(VESSELS_KEY, vesselImporter.exportObjects(fleetModel.getVessels()));
		output.put(VESSEL_CLASSES_KEY, vesselClassImporter.exportObjects(fleetModel.getVesselClasses()));
		output.put(EVENTS_KEY, vesselEventImporter.exportObjects(fleetModel.getVesselEvents()));
		output.put(FUELS_KEY, baseFuelImporter.exportObjects(fleetModel.getBaseFuels()));
	}

}
