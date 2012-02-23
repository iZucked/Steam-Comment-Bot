package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public class FleetModelImporter implements ISubmodelImporter {
	public static final String VESSELS_KEY = "VESSELS";
	public static final String VESSEL_CLASSES_KEY = "VESSELCLASSES";
	public static final String FUELS_KEY = "BASEFUELS";
	public static final String EVENTS_KEY = "EVENTS";
	private static final Map<String, String> inputs = new HashMap<String, String>();
	static {
		inputs.put(VESSELS_KEY, "Vessels");
		inputs.put(VESSEL_CLASSES_KEY, "Vessel Classes");
		inputs.put(FUELS_KEY, "Base fuels");
		inputs.put(EVENTS_KEY, "Events");
	}
	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();
		
		return fleetModel;
	}

	@Override
	public void exportModel(UUIDObject model,
			Map<String, Collection<Map<String, String>>> output) {
		
	}

}
