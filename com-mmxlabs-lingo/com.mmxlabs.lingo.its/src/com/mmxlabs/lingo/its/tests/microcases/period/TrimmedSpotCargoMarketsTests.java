/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@RunWith(value = ShiroRunner.class)
public class TrimmedSpotCargoMarketsTests extends AbstractMicroTestCase {

	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void basicMarketTrim() throws Exception {

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

		SpotMarketsModelBuilder spotMarketsBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel("STEAM-145");

		// Build some data to evaluate the scenario with
		// FIXME: Fix issue to avoid needing this.
		{

			final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
					.build();

			// Create a single charter out event
			cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 1, 1, 0, 0, 0), LocalDateTime.of(2015, 1, 1, 0, 0, 0), portFinder.findPort("Point Fortin")) //
					.withRelocatePort(portFinder.findPort("Isle of Grain")) //
					.withDurationInDays(10) //
					.withVesselAssignment(vesselAvailability_1, 0) //
					.build(); //

		}
		SpotMarket market = spotMarketsBuilder.makeDESSaleMarket("DES-SaleMarket", portFinder.findPort("Point Fortin"), entity, "5") //
				.withAvailabilityConstant(5)//
				.withAvailabilityDate(YearMonth.of(2015, 12), 6) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setWithSpotCargoMarkets(true);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 1, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 2));

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

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assert.assertEquals(1, optimiserScenario.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets().size());

			SpotMarket opt_market = optimiserScenario.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets().get(0);

			// Constant should be zero, and instead new options created
			Assert.assertEquals(0, opt_market.getAvailability().getConstant());
			// Expect 1 (this should be the newly created entry)
			Assert.assertEquals(1, opt_market.getAvailability().getCurve().getPoints().size());
			boolean foundNewOne = false;
			for (IndexPoint<Integer> pt : opt_market.getAvailability().getCurve().getPoints()) {
				if (pt.getDate().isBefore(userSettings.getPeriodEnd())
						&& (pt.getDate().isAfter(YearMonth.from(userSettings.getPeriodStartDate())) || pt.getDate().equals(YearMonth.from(userSettings.getPeriodStartDate())))) {
					Assert.assertEquals(market.getAvailability().getConstant(), pt.getValue().intValue());
				} else {
					Assert.assertEquals(0, pt.getValue().intValue());
				}

				if (pt.getDate().equals(YearMonth.of(2015, 1))) {
					// A new entry should have been created
					foundNewOne = true;
				}
				if (pt.getDate().equals(YearMonth.of(2015, 12))) {
					// This should have been removed as outside of period
					Assert.fail();
				}
			}
			Assert.assertTrue(foundNewOne);

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}

	/**
	 * Expect same result as for {@link #basicMarketTrim()}
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void basicMarketTrim_PartMonth() throws Exception {

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

		SpotMarketsModelBuilder spotMarketsBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelFinder.findVessel("STEAM-145");

		// Build some data to evaluate the scenario with
		// FIXME: Fix issue to avoid needing this.
		{

			final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
					.build();

			// Create a single charter out event
			cargoModelBuilder.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 1, 1, 0, 0, 0), LocalDateTime.of(2015, 1, 1, 0, 0, 0), portFinder.findPort("Point Fortin")) //
					.withRelocatePort(portFinder.findPort("Isle of Grain")) //
					.withDurationInDays(10) //
					.withVesselAssignment(vesselAvailability_1, 0) //
					.build(); //

		}
		SpotMarket market = spotMarketsBuilder.makeDESSaleMarket("DES-SaleMarket", portFinder.findPort("Point Fortin"), entity, "5") //
				.withAvailabilityConstant(5)//
				.withAvailabilityDate(YearMonth.of(2015, 12), 6) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setWithSpotCargoMarkets(true);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 1, 30));
		userSettings.setPeriodEnd(YearMonth.of(2015, 2));

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

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assert.assertEquals(1, optimiserScenario.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets().size());

			SpotMarket opt_market = optimiserScenario.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets().get(0);

			// Constant should be zero, and instead new options created
			Assert.assertEquals(0, opt_market.getAvailability().getConstant());
			// Expect 1 (this should be the newly created entry)
			Assert.assertEquals(1, opt_market.getAvailability().getCurve().getPoints().size());
			boolean foundNewOne = false;
			for (IndexPoint<Integer> pt : opt_market.getAvailability().getCurve().getPoints()) {
				if (pt.getDate().isBefore(userSettings.getPeriodEnd())
						&& (pt.getDate().isAfter(YearMonth.from(userSettings.getPeriodStartDate())) || pt.getDate().equals(YearMonth.from(userSettings.getPeriodStartDate())))) {
					Assert.assertEquals(market.getAvailability().getConstant(), pt.getValue().intValue());
				} else {
					Assert.assertEquals(0, pt.getValue().intValue());
				}

				if (pt.getDate().equals(YearMonth.of(2015, 1))) {
					// A new entry should have been created
					foundNewOne = true;
				}
				if (pt.getDate().equals(YearMonth.of(2015, 12))) {
					// This should have been removed as outside of period
					Assert.fail();
				}
			}
			Assert.assertTrue(foundNewOne);

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		} finally {
			runner.dispose();
		}
	}
}