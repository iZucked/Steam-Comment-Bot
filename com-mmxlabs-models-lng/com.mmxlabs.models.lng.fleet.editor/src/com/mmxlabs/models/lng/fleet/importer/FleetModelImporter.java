/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class FleetModelImporter implements ISubmodelImporter {
	public static final String VESSELS_KEY = "VESSELS";
	public static final String VESSEL_CLASSES_KEY = "VESSELCLASSES";
	public static final String FUELS_KEY = "BASEFUELS";
	public static final String CURVES_KEY = "CONSUMPTION_CURVES";
	public static final String GROUPS_KEY = "VESSEL-GROUPS";

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter vesselImporter;
	private IClassImporter vesselClassImporter;
	private IClassImporter baseFuelImporter;
	private IClassImporter groupImporter;

	private final FuelCurveImporter fuelCurveImporter = new FuelCurveImporter();

	private static final Map<String, String> inputs = new HashMap<String, String>();

	static {
		inputs.put(VESSELS_KEY, "Vessels");
		inputs.put(VESSEL_CLASSES_KEY, "Vessel Classes");
		inputs.put(GROUPS_KEY, "Vessel Groups");
		inputs.put(FUELS_KEY, "Base Fuels");
		inputs.put(CURVES_KEY, "Consumption Curves");
	}

	/**
	 */
	public FleetModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			vesselImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVessel());
			vesselClassImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVesselClass());
			baseFuelImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getBaseFuel());
			groupImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVesselGroup());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();

		if (inputs.containsKey(VESSELS_KEY))
			fleetModel.getVessels().addAll((Collection<? extends Vessel>) vesselImporter.importObjects(FleetPackage.eINSTANCE.getVessel(), inputs.get(VESSELS_KEY), context));

		if (inputs.containsKey(VESSEL_CLASSES_KEY))
			fleetModel.getVesselClasses()
					.addAll((Collection<? extends VesselClass>) vesselClassImporter.importObjects(FleetPackage.eINSTANCE.getVesselClass(), inputs.get(VESSEL_CLASSES_KEY), context));

		if (inputs.containsKey(FUELS_KEY))
			fleetModel.getBaseFuels().addAll((Collection<? extends BaseFuel>) baseFuelImporter.importObjects(FleetPackage.eINSTANCE.getBaseFuel(), inputs.get(FUELS_KEY), context));

		if (inputs.containsKey(GROUPS_KEY))
			fleetModel.getVesselGroups().addAll((Collection<? extends VesselGroup>) groupImporter.importObjects(FleetPackage.eINSTANCE.getVesselGroup(), inputs.get(GROUPS_KEY), context));

		if (inputs.containsKey(CURVES_KEY))
			fuelCurveImporter.importFuelConsumptions(inputs.get(CURVES_KEY), context);

		context.doLater(new IDeferment() {
			@Override
			public void run(final IImportContext importContext) {
				final IMMXImportContext context = (IMMXImportContext) importContext;
				final MMXRootObject rootObject = context.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

					final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
					final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);

					for (final Route route : portModel.getRoutes()) {
						if (route.getRouteOption() != RouteOption.DIRECT) {
							vessel_classes: for (final VesselClass vc : fleetModel.getVesselClasses()) {
								for (final VesselClassRouteParameters parameters : vc.getRouteParameters()) {
									if (parameters.getRoute() == route)
										continue vessel_classes;
								}

								// construct blank parameters
								final VesselClassRouteParameters parameters = FleetFactory.eINSTANCE.createVesselClassRouteParameters();
								parameters.setRoute(route);
								vc.getRouteParameters().add(parameters);
							}
						}
					}
				}
			}

			@Override
			public int getStage() {
				return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
			}
		});

		return fleetModel;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final FleetModel fleetModel = (FleetModel) model;
		output.put(VESSELS_KEY, vesselImporter.exportObjects(fleetModel.getVessels(), context));
		output.put(VESSEL_CLASSES_KEY, vesselClassImporter.exportObjects(fleetModel.getVesselClasses(), context));
		output.put(FUELS_KEY, baseFuelImporter.exportObjects(fleetModel.getBaseFuels(), context));
		output.put(CURVES_KEY, fuelCurveImporter.exportCurves(fleetModel.getVesselClasses(), context));
		output.put(GROUPS_KEY, groupImporter.exportObjects(fleetModel.getVesselGroups(), context));
	}

	@Override
	public EClass getEClass() {
		return FleetPackage.eINSTANCE.getFleetModel();
	}
}
