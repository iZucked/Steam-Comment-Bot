/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;

public class ManifestJointModel {

	public static @NonNull LNGScenarioModel createEmptyInstance(final List<EObject> models) {
		final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		// rootObject.setParametersModel(ParametersFactory.eINSTANCE.createParametersModel());
		rootObject.setCargoModel(CargoFactory.eINSTANCE.createCargoModel());
		rootObject.setScheduleModel(ScheduleFactory.eINSTANCE.createScheduleModel());

		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		referenceModel.setPortModel(PortFactory.eINSTANCE.createPortModel());
		referenceModel.setFleetModel(FleetFactory.eINSTANCE.createFleetModel());
		referenceModel.setPricingModel(PricingFactory.eINSTANCE.createPricingModel());
		referenceModel.setCostModel(PricingFactory.eINSTANCE.createCostModel());
		referenceModel.setCommercialModel(CommercialFactory.eINSTANCE.createCommercialModel());
		referenceModel.setSpotMarketsModel(SpotMarketsFactory.eINSTANCE.createSpotMarketsModel());

		rootObject.setReferenceModel(referenceModel);

		return rootObject;
	}
}
