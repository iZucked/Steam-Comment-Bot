/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * @since 4.0
 */
public class ScenarioFleetModelImporter implements ISubmodelImporter {
	public static final String VESSEL_AVAILABILITY_KEY = "VESSELSAVAILABILITIES";
	public static final String EVENTS_KEY = "EVENTS";

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter vesselAvailabilityImporter;
	private IClassImporter vesselEventImporter;

	private static final Map<String, String> inputs = new HashMap<String, String>();
	static {
		inputs.put(VESSEL_AVAILABILITY_KEY, "Vessel Availability");
		inputs.put(EVENTS_KEY, "Events");
	}

	public ScenarioFleetModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			vesselAvailabilityImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVesselAvailability());
			vesselEventImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVesselEvent());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final ScenarioFleetModel scenarioFleetModel = FleetFactory.eINSTANCE.createScenarioFleetModel();

		if (inputs.containsKey(VESSEL_AVAILABILITY_KEY))
			scenarioFleetModel.getVesselAvailabilities().addAll(
					(Collection<? extends VesselAvailability>) vesselAvailabilityImporter.importObjects(FleetPackage.eINSTANCE.getVesselAvailability(), inputs.get(VESSEL_AVAILABILITY_KEY), context));

		if (inputs.containsKey(EVENTS_KEY))
			scenarioFleetModel.getVesselEvents()
					.addAll((Collection<? extends VesselEvent>) vesselEventImporter.importObjects(FleetPackage.eINSTANCE.getVesselEvent(), inputs.get(EVENTS_KEY), context));

		return scenarioFleetModel;
	}

	@Override
	public void exportModel(final MMXRootObject root, final UUIDObject model, final Map<String, Collection<Map<String, String>>> output) {
		final ScenarioFleetModel scenarioFleetModel = (ScenarioFleetModel) model;
		output.put(VESSEL_AVAILABILITY_KEY, vesselAvailabilityImporter.exportObjects(scenarioFleetModel.getVesselAvailabilities(), root));
		output.put(EVENTS_KEY, vesselEventImporter.exportObjects(scenarioFleetModel.getVesselEvents(), root));
	}

	@Override
	public EClass getEClass() {
		return FleetPackage.eINSTANCE.getFleetModel();
	}
}
