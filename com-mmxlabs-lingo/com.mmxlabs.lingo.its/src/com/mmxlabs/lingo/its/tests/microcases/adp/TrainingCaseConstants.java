/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp;

import java.io.InputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lingo.its.training.TrainingShippingITests;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public class TrainingCaseConstants {

	public static final String VESSEL_SMALL_SHIP = "Steam_138";
	public static final String VESSEL_MEDIUM_SHIP = "Steam_145";
	public static final String VESSEL_LARGE_SHIP = "TFDE_160";

	// Which scenario data to import
	public static IScenarioDataProvider importTrainingData() throws Exception {

		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();

		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I/Commodity Curves.csv")) {
			sb.importCommodityCurves(is);
		}
		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I/Vessel Charters.csv")) {
			sb.importVesselCharters(is);
		}
		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I/Cargoes.csv")) {
			sb.importCargoes(is);
		}
		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I/Assignments.csv")) {
			sb.importAssignments(is);
		}
		return sb.getScenarioDataProvider();
	}
}
