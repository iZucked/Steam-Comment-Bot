/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@RunWith(value = ShiroRunner.class)
public class CharterInMarketTests extends AbstractMicroTestCase {

	/**
	 * If we have two charter in markets and we remove all cargoes from one used option, make sure we reduce the market count and renumber spot index assignments. This test removes cargo after the
	 * period.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void testSpotCharterInMarketReduction_After() throws Exception {

		// Load in the basic scenario from CSV
		final IScenarioDataProvider scenarioDataProvider = importReferenceData();
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		final SpotMarketsModelBuilder spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 2);
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vessel, "100000", 2);

		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 24), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) //
				// .withLocked(true) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7").build() //
				.withVesselAssignment(charterInMarket_1, 1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create cargo 3, cargo 4
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2016, 4, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) //
				.build() //
				.makeDESSale("D3", LocalDate.of(2016, 6, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 0, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 11, 1));
		userSettings.setPeriodEnd(YearMonth.of(2016, 1));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			// Check spot index has been updated
			LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
			Assert.assertEquals("L1", optCargo1.getLoadName());
			Assert.assertEquals(0, optCargo1.getSpotIndex());
			Cargo optCargo2 = optimiserScenario.getCargoModel().getCargoes().get(1);
			Assert.assertEquals("L2", optCargo2.getLoadName());
			Assert.assertEquals(0, optCargo2.getSpotIndex());

			// Reduced by one
			Assert.assertEquals(1, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0).getSpotCharterCount());
			// Stays the same
			Assert.assertEquals(2, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(1).getSpotCharterCount());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

	/**
	 * If we have two charter in markets and we remove all cargoes from one used option, make sure we reduce the market count and renumber spot index assignments. This test removes cargo before the
	 * period.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void testSpotCharterInMarketReduction_Before() throws Exception {

		// Load in the basic scenario from CSV
		final IScenarioDataProvider scenarioDataProvider = importReferenceData();
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		final SpotMarketsModelBuilder spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 2);
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vessel, "100000", 2);

		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 1, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 24), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) //
				// .withLocked(true) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7").build() //
				.withVesselAssignment(charterInMarket_1, 1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create cargo 3, cargo 4
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2015, 4, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) //
				.build() //
				.makeDESSale("D3", LocalDate.of(2015, 6, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel) //
				.build() //
				.withVesselAssignment(charterInMarket_1, 0, 0) //
				.withAssignmentFlags(true, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 11, 1));
		userSettings.setPeriodEnd(YearMonth.of(2016, 1));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
			final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService(1);

			// Check spot index has been updated
			LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
			Assert.assertEquals("L1", optCargo1.getLoadName());
			Assert.assertEquals(0, optCargo1.getSpotIndex());
			Cargo optCargo2 = optimiserScenario.getCargoModel().getCargoes().get(1);
			Assert.assertEquals("L2", optCargo2.getLoadName());
			Assert.assertEquals(0, optCargo2.getSpotIndex());

			// Reduced by one
			Assert.assertEquals(1, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0).getSpotCharterCount());
			// Stays the same
			Assert.assertEquals(2, optimiserScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(1).getSpotCharterCount());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}
}