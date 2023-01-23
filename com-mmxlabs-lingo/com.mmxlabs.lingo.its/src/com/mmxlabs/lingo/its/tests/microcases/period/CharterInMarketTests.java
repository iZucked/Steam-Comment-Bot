/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.core.ISequences;

@ExtendWith(ShiroRunner.class)
public class CharterInMarketTests extends AbstractMicroTestCase {

	/**
	 * If we have two charter in markets and we remove all cargoes from one used
	 * option, make sure we reduce the market count and renumber spot index
	 * assignments. This test removes cargo after the period.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotCharterInMarketReduction_After() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 2);
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vessel, entity, "100000", 2);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 24), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				// .withLocked(true) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7").build() //
				.withVesselAssignment(charterInMarket_1, 1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create cargo 3, cargo 4
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2016, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.makeDESSale("D3", LocalDate.of(2016, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 0, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 11, 1));
		userSettings.setPeriodEnd(YearMonth.of(2016, 1));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		// Check spot index has been updated
		LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		// Check cargoes removed
		Assertions.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

		// Check correct cargoes remain and spot index has changed.
		Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
		Assertions.assertEquals("L1", optCargo1.getLoadName());
		Assertions.assertEquals(0, optCargo1.getSpotIndex());
		Cargo optCargo2 = optimiserScenario.getCargoModel().getCargoes().get(1);
		Assertions.assertEquals("L2", optCargo2.getLoadName());
		Assertions.assertEquals(0, optCargo2.getSpotIndex());

		// Reduced by one
		Assertions.assertEquals(1, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0).getSpotCharterCount());
		// Stays the same
		Assertions.assertEquals(2, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(1).getSpotCharterCount());

		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

		// Validate the initial sequences are valid
		Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}

	/**
	 * If we have two charter in markets and we remove all cargoes from one used
	 * option, make sure we reduce the market count and renumber spot index
	 * assignments. This test removes cargo before the period.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotCharterInMarketReduction_Before() throws Exception {

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 2);
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vessel, entity, "100000", 2);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 24), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				// .withLocked(true) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7").build() //
				.withVesselAssignment(charterInMarket_1, 1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create cargo 3, cargo 4
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2015, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.makeDESSale("D3", LocalDate.of(2015, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 0, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 11, 1));
		userSettings.setPeriodEnd(YearMonth.of(2016, 1));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		// Check spot index has been updated
		LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		// Check cargoes removed
		Assertions.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

		// Check correct cargoes remain and spot index has changed.
		Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
		Assertions.assertEquals("L1", optCargo1.getLoadName());
		Assertions.assertEquals(0, optCargo1.getSpotIndex());
		Cargo optCargo2 = optimiserScenario.getCargoModel().getCargoes().get(1);
		Assertions.assertEquals("L2", optCargo2.getLoadName());
		Assertions.assertEquals(0, optCargo2.getSpotIndex());

		// Reduced by one
		Assertions.assertEquals(1, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0).getSpotCharterCount());
		// Stays the same
		Assertions.assertEquals(2, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(1).getSpotCharterCount());

		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

		// Validate the initial sequences are valid
		Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
	}

	/**
	 * We have a large > 270 day idle period between cargoes on a spot charter in.
	 * The first cargo is well before the period start and the second cargo is
	 * within the optimisation period. We had a bug were the first cargo was
	 * trimmed, but the mapping state was wrong and threw an AssertionError when
	 * transforming.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLongIdleOverPeriodStartWithinCharter() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		CharterInMarket charterInMarket = spotMarketsModelBuilder.createCharterInMarket("MKT", vessel, entity, "80000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 01, 16), portFinder.findPortById(InternalDataConstants.PORT_ZEEBRUGGE), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 1, 20), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket, 0, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2019, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_ZEEBRUGGE), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2019, 11, 7), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket, 0, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2019, 11, 5));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2020, 1));
		}, null, scenarioRunner -> {

			// Nothing to check.
		}, null);
	}
}