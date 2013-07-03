/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * This is an example LNG joint model; each client will have a different implementation of this.
 * 
 * In this simple example, we have a very simple ecore model which is placed in a zip archive, called manifest.xmi.
 * 
 * This in turn contains relative references to each submodel part. It is not all that LNG specific really.
 * 
 * @author hinton
 * 
 */
public class ManifestJointModel {
	private static final String PORT_MODEL_KEY = "port-model";
	private static final String FLEET_MODEL_KEY = "fleet-model";
	private static final String SCENARIO_FLEET_MODEL_KEY = "scenario-fleet-model";
	private static final String CARGO_MODEL_KEY = "cargo-model";
	private static final String PRICING_MODEL_KEY = "pricing-model";
	private static final String ASSIGNMENT_MODEL_KEY = "assignment-model";
	private static final String SCHEDULE_MODEL_KEY = "schedule-model";
	private static final String COMMERCIAL_MODEL_KEY = "commercial-model";
	private static final String ROOT_MODEL_KEY = "root-model";
//	private static final String PARAMETERS_MODEL_KEY = "parameters-model";
//	private static final String ANALYTICS_MODEL_KEY = "analytics-model";
	private static final String SPOT_MARKETS_MODEL_KEY = "spot-markets-model";

	/**
	 * This map lets us know what kind of model class has what key.
	 */
	private static final Map<EClass, String> modelClassKeys = new LinkedHashMap<EClass, String>();

	static {
		modelClassKeys.put(PortPackage.eINSTANCE.getPortModel(), PORT_MODEL_KEY);
		modelClassKeys.put(FleetPackage.eINSTANCE.getFleetModel(), FLEET_MODEL_KEY);
		modelClassKeys.put(FleetPackage.eINSTANCE.getScenarioFleetModel(), SCENARIO_FLEET_MODEL_KEY);
		modelClassKeys.put(CargoPackage.eINSTANCE.getCargoModel(), CARGO_MODEL_KEY);
		modelClassKeys.put(CommercialPackage.eINSTANCE.getCommercialModel(), COMMERCIAL_MODEL_KEY);
		modelClassKeys.put(PricingPackage.eINSTANCE.getPricingModel(), PRICING_MODEL_KEY);
		modelClassKeys.put(AssignmentPackage.eINSTANCE.getAssignmentModel(), ASSIGNMENT_MODEL_KEY);
		modelClassKeys.put(SchedulePackage.eINSTANCE.getScheduleModel(), SCHEDULE_MODEL_KEY);
//		modelClassKeys.put(ParametersPackage.eINSTANCE.getParametersModel(), PARAMETERS_MODEL_KEY);
		// modelClassKeys.put(AnalyticsPackage.eINSTANCE.getAnalyticsModel(), ANALYTICS_MODEL_KEY);
		modelClassKeys.put(SpotMarketsPackage.eINSTANCE.getSpotMarketsModel(), SPOT_MARKETS_MODEL_KEY);

		/*
		 * There is no migration history for MMXCore, but this is not a problem; the joint model will ignore submodels which have no release history and leave them out of the upgrade process.
		 */
		modelClassKeys.put(MMXCorePackage.eINSTANCE.getMMXRootObject(), ROOT_MODEL_KEY);
	}

	/**
	 * @since 3.0
	 */
	public static LNGScenarioModel createEmptyInstance(final List<EObject> models) {
		final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		rootObject.setPortModel(PortFactory.eINSTANCE.createPortModel());
		rootObject.setFleetModel(FleetFactory.eINSTANCE.createFleetModel());
		rootObject.setPricingModel(PricingFactory.eINSTANCE.createPricingModel());
		rootObject.getPricingModel().setFleetCost(PricingFactory.eINSTANCE.createFleetCostModel());
		rootObject.setCommercialModel(CommercialFactory.eINSTANCE.createCommercialModel());
//		rootObject.setParametersModel(ParametersFactory.eINSTANCE.createParametersModel());
		rootObject.setSpotMarketsModel(SpotMarketsFactory.eINSTANCE.createSpotMarketsModel());

		final LNGPortfolioModel portfolioModel = LNGScenarioFactory.eINSTANCE.createLNGPortfolioModel();
		rootObject.setPortfolioModel(portfolioModel);

		portfolioModel.setCargoModel(CargoFactory.eINSTANCE.createCargoModel());
		portfolioModel.setAssignmentModel(AssignmentFactory.eINSTANCE.createAssignmentModel());
		portfolioModel.setScheduleModel(ScheduleFactory.eINSTANCE.createScheduleModel());
		portfolioModel.setScenarioFleetModel(FleetFactory.eINSTANCE.createScenarioFleetModel());

		return rootObject;
	}
}
