/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
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
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;

/**
 * Some test cases around period optimisation with vessel events (specifically charter out events which can move between vessels). Original period transformer locks down all vessel events.
 *
 */
@RunWith(value = ShiroRunner.class)
public class PeriodVesselEventsTests extends AbstractMicroTestCase {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void testCharterOutEvent_In() throws Exception {

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

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);
		final Vessel vessel_2 = fleetModelBuilder.createVessel("Vessel-2", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final VesselAvailability vesselAvailability_2 = cargoModelBuilder.makeVesselAvailability(vessel_2, entity) //
				.withStartPort(portFinder.findPort("Bonny Nigeria")) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Bonny Nigeria")) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final CharterOutEvent charter_1 = cargoModelBuilder
				.makeCharterOutEvent("CharterOut1", LocalDateTime.of(2015, 4, 1, 0, 0, 0), LocalDateTime.of(2015, 4, 1, 0, 0, 0), portFinder.findPort("Bonny Nigeria")) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAllowedVessels(vessel_1, vessel_2) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 10));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 10_000);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check locked flags
			final VesselEvent period_event = optimiserScenario.getCargoModel().getVesselEvents().get(0);
			Assert.assertEquals(vessel_1.getName(), ((VesselAvailability) period_event.getVesselAssignmentType()).getVessel().getName());

			Assert.assertFalse(period_event.isLocked());
			Assert.assertFalse(period_event.getAllowedVessels().isEmpty());
			Assert.assertEquals(charter_1.getAllowedVessels().size(), period_event.getAllowedVessels().size());

			scenarioRunner.run();
			Assert.assertSame(vesselAvailability_2, charter_1.getVesselAssignmentType());
		} finally {
			executorService.shutdownNow();
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, MicroTest.class })
	public void testCharterOutEvent_Boundary() throws Exception {

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

		// Create the required basic elements
		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);
		final Vessel vessel_2 = fleetModelBuilder.createVessel("Vessel-2", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final VesselAvailability vesselAvailability_2 = cargoModelBuilder.makeVesselAvailability(vessel_2, entity) //
				.withStartPort(portFinder.findPort("Bonny Nigeria")) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Bonny Nigeria")) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final CharterOutEvent charter_1 = cargoModelBuilder
				.makeCharterOutEvent("CharterOut1", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPort("Bonny Nigeria")) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAllowedVessels(vessel_1, vessel_2) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStart(YearMonth.of(2015, 4));
		userSettings.setPeriodEnd(YearMonth.of(2015, 10));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 10_000);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check locked flags
			final VesselEvent period_event = optimiserScenario.getCargoModel().getVesselEvents().get(0);
			final Vessel period_vessel_1 = ((VesselAvailability) period_event.getVesselAssignmentType()).getVessel();
			Assert.assertEquals(vessel_1.getName(), period_vessel_1.getName());

			Assert.assertFalse(period_event.isLocked());
			Assert.assertFalse(period_event.getAllowedVessels().isEmpty());
			Assert.assertEquals(1, period_event.getAllowedVessels().size());
			Assert.assertTrue(period_event.getAllowedVessels().contains( period_vessel_1));

			scenarioRunner.run();
			Assert.assertSame(vesselAvailability_1, charter_1.getVesselAssignmentType());

		} finally {
			executorService.shutdownNow();
		}
	}
}