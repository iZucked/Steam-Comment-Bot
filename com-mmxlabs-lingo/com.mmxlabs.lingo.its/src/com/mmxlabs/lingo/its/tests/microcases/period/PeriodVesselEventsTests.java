/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;

/**
 * Some test cases around period optimisation with vessel events (specifically
 * charter out events which can move between vessels). Original period
 * transformer locks down all vessel events.
 *
 */
@ExtendWith(ShiroRunner.class)
public class PeriodVesselEventsTests extends AbstractMicroTestCase {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterOutEvent_In() throws Exception {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final Vessel vessel_1 = fleetModelBuilder.createVesselFrom("Vessel-1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel_2 = fleetModelBuilder.createVesselFrom("Vessel-2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final VesselAvailability vesselAvailability_2 = cargoModelBuilder.makeVesselAvailability(vessel_2, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_BONNY)) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_BONNY)) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final CharterOutEvent charter_1 = cargoModelBuilder
				.makeCharterOutEvent("CharterOut1", LocalDateTime.of(2015, 4, 1, 0, 0, 0), LocalDateTime.of(2015, 4, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_BONNY)) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAllowedVessels(vessel_1, vessel_2) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 1, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 10));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 10_000);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		// Check locked flags
		final VesselEvent period_event = optimiserScenario.getCargoModel().getVesselEvents().get(0);
		Assertions.assertEquals(vessel_1.getName(), ((VesselAvailability) period_event.getVesselAssignmentType()).getVessel().getName());

		Assertions.assertFalse(period_event.isLocked());
		Assertions.assertFalse(period_event.getAllowedVessels().isEmpty());
		Assertions.assertEquals(charter_1.getAllowedVessels().size(), period_event.getAllowedVessels().size());

		runner.run(true);
		Assertions.assertSame(vesselAvailability_2, charter_1.getVesselAssignmentType());
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.QUICK_TEST)
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterOutEvent_Boundary() throws Exception {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final Vessel vessel_1 = fleetModelBuilder.createVesselFrom("Vessel-1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel_2 = fleetModelBuilder.createVesselFrom("Vessel-2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final VesselAvailability vesselAvailability_2 = cargoModelBuilder.makeVesselAvailability(vessel_2, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_BONNY)) //
				.withStartWindow(LocalDateTime.of(2015, 2, 1, 0, 0, 0)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_BONNY)) //
				.withEndWindow(LocalDateTime.of(2015, 9, 1, 0, 0, 0)) //
				.build();

		final CharterOutEvent charter_1 = cargoModelBuilder
				.makeCharterOutEvent("CharterOut1", LocalDateTime.of(2015, 3, 30, 0, 0, 0), LocalDateTime.of(2015, 3, 30, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_BONNY)) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAllowedVessels(vessel_1, vessel_2) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
		// period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2015, 4, 1));
		userSettings.setPeriodEnd(YearMonth.of(2015, 10));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 10_000);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		// Check locked flags
		final VesselEvent period_event = optimiserScenario.getCargoModel().getVesselEvents().get(0);
		final Vessel period_vessel_1 = ((VesselAvailability) period_event.getVesselAssignmentType()).getVessel();
		Assertions.assertEquals(vessel_1.getName(), period_vessel_1.getName());

		Assertions.assertTrue(period_event.isLocked());
		Assertions.assertFalse(period_event.getAllowedVessels().isEmpty());
		Assertions.assertEquals(1, period_event.getAllowedVessels().size());
		Assertions.assertTrue(period_event.getAllowedVessels().contains(period_vessel_1));

		runner.run(true);
		Assertions.assertSame(vesselAvailability_1, charter_1.getVesselAssignmentType());
	}
}