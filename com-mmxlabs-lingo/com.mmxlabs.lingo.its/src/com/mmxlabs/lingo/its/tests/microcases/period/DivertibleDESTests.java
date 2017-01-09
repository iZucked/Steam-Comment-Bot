/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
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
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Some test cases around divertible DES cargoes.
 *
 */
@RunWith(value = ShiroRunner.class)
public class DivertibleDESTests extends AbstractMicroTestCase {

	/**
	 * This test case originally lead to a scenario with no cargoes as the sale was outside the period and des cargoes are based on sales date.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	@Ignore("This is unexpected as the load is in the window, but as the sale is outside (and locked) and it is a DES Purchase, this is a fixed cargo and it does not matter if it is included or not")
	public void testDivertableDES() throws Exception {

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = importReferenceData();

		// Create finder and builder
		final ScenarioModelFinder scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		final CommercialModelFinder commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		final FleetModelFinder fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		final PortModelFinder portFinder = scenarioModelFinder.getPortModelFinder();

		final CargoModelBuilder cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		SpotMarketsModelBuilder spotMarketsBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", true, LocalDate.of(2015, 4, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "5", vessel_1) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 6, 1), portFinder.findPort("Barcelona LNG"), null, entity, "7") //
				.build() //

				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 5));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

			// Check locked flags
			Assert.assertFalse(optimiserScenario.getCargoModel().getCargoes().get(0).isLocked());
			Assert.assertFalse(optimiserScenario.getCargoModel().getLoadSlots().get(0).isLocked());
			Assert.assertTrue(optimiserScenario.getCargoModel().getDischargeSlots().get(0).isLocked());

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			executorService.shutdownNow();
		}
	}
}