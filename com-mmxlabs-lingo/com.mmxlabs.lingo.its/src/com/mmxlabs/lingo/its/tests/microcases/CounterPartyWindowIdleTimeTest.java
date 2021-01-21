/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.TimePeriod;

@ExtendWith(ShiroRunner.class)
public class CounterPartyWindowIdleTimeTest extends AbstractIdleTimeTests {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESSaleCounterPartyWindow() {

		// Set all times for ports to UTC, so we don't end up with -2 hours off expected durations.
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7").withWindowSize(100, TimePeriod.DAYS)
				.withWindowCounterParty(true).withVisitDuration(36).build() //
				.withVesselAssignment(charterInMarket1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			validateSlotVisitDuration(scenarioRunner, "D1", (24 * 100) + 36 - 1);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBPurchaseCounterPartyWindow() {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(100, TimePeriod.DAYS).withWindowCounterParty(true).withVisitDuration(36).build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7").build() //
				.withVesselAssignment(charterInMarket1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			// TODO: check why this is 1 hours less than expected?
			validateSlotVisitDuration(scenarioRunner, "L1", (24 * 100) + 36 - 1);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCounterPartyWindowInPeriod() {
		// This test it to make sure a counterpart cargo in the period boundary still keeps it's window setting.

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability charter1 = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2020, 7, 14), portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, entity, "5") //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2020, 8, 1), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(4, TimePeriod.DAYS) //
				.build() //

				.withVesselAssignment(charter1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setMode(OptimisationMode.SHORT_TERM);

		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2020, 8, 1));
		userSettings.setPeriodEnd(YearMonth.of(2021, 4));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 10);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			// Check locked flags
			boolean foundD1 = false;
			final Cargo period_cargo = optimiserScenario.getCargoModel().getCargoes().get(0);
			for (Slot<?> slot : period_cargo.getSlots()) {
				Assertions.assertTrue(slot.isLocked());
				if ("D1".equals(slot.getName())) {
					Assertions.assertTrue(slot.isWindowCounterParty());
					Assertions.assertEquals(4, slot.getWindowSize());
					Assertions.assertEquals(TimePeriod.DAYS, slot.getWindowSizeUnits());
					foundD1 = true;
				}

			}
			Assertions.assertTrue(foundD1);

		} finally {
			runner.dispose();
		}
	}
}