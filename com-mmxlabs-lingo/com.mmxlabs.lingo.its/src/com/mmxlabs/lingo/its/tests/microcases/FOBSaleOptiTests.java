/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;

public class FOBSaleOptiTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void divertibleFOBSale_SimpleSwap() throws Exception {

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withOptional(true)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2018, 2, 16), portFinder.findPort("Idku LNG"), null, entity, "5", nominatedVessel) //
				.with(s -> s.setShippingDaysRestriction(32))//
				.build() //

				.build();

		LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		LoadSlot load2 = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "4", 22.6) //
				.withOptional(true)//
				.build();

		optimiseWithLSOTest(runner -> {

			// Assert cargo wiring has changed.
			Assertions.assertNotNull(load2.getCargo());
			Assertions.assertTrue(load2.getCargo().getSlots().contains(discharge1));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void divertibleFOBSale_SimpleSwap_NoDays() throws Exception {

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withOptional(true)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2018, 2, 16), portFinder.findPort("Idku LNG"), null, entity, "5", nominatedVessel) //
				.with(s -> s.setShippingDaysRestriction(1))//
				.build() //

				.build();

		LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		LoadSlot load2 = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "4", 22.6) //
				.withOptional(true)//
				.build();

		optimiseWithLSOTest(runner -> {

			// Assert cargo wiring has not changed.
			Assertions.assertNull(load2.getCargo());
			Assertions.assertNotNull(load1.getCargo());
			Assertions.assertTrue(load1.getCargo().getSlots().contains(discharge1));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void divertibleFOBSale_SimpleSwap_OnlyLadenDays() throws Exception {

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withWindowStartTime(0) //

				.withOptional(true)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2018, 2, 16), portFinder.findPort("Idku LNG"), null, entity, "5", nominatedVessel) //
				.withWindowStartTime(0) //

				.with(s -> s.setShippingDaysRestriction(16))//
				.build() //

				.build();

		LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		LoadSlot load2 = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "4", 22.6) //
				.withOptional(true)//
				.build();

		optimiseWithLSOTest(runner -> {

			// Assert cargo wiring has not changed.
			Assertions.assertNull(load2.getCargo());
			Assertions.assertNotNull(load1.getCargo());
			Assertions.assertTrue(load1.getCargo().getSlots().contains(discharge1));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void divertibleFOBSale_DestWithSourceMismatch() throws Exception {

		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2019, 8, 14), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //

				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2019, 8, 14), portFinder.findPort("Sakai"), null, entity, "5", null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(1) //

				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withUserSettings(userSettings) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			List<IConstraintChecker> checkers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(checkers);
			boolean foundCompat = false;
			for (IConstraintChecker cc : checkers) {
				if (cc instanceof FOBDESCompatibilityConstraintChecker) {
					foundCompat = true;
				}
			}
			Assertions.assertTrue(foundCompat);
		} finally {
			runner.dispose();
		}
	}
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void divertibleFOBSale_DestWithSourceFlex() throws Exception {
		
		cargoModelBuilder.makeCargo()//
		.makeFOBPurchase("L1", LocalDate.of(2019, 8, 14), portFinder.findPort("Point Fortin"), null, entity, "5") //
		.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
		.withWindowStartTime(1) //
		.withWindowSize(0, TimePeriod.HOURS) //
		
		.build() //
		
		.makeFOBSale("D1", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2019, 8, 14), portFinder.findPort("Sakai"), null, entity, "5", null) //
		.withWindowSize(0, TimePeriod.HOURS) //
		.withWindowFlex(1  , TimePeriod.HOURS)
		.withWindowStartTime(0) //

		.build() //
		
		.build();
		
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withUserSettings(userSettings) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();
			
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
			
			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
			

			List<IConstraintChecker> checkers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			if (checkers != null) {
				for (IConstraintChecker cc : checkers) {
					System.out.println(cc);
				}
			}
			Assertions.assertNull(checkers);
		} finally {
			runner.dispose();
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void divertibleFOBSale_DestWithSource() throws Exception {

		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2019, 8, 14), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //

				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2019, 8, 14), portFinder.findPort("Sakai"), null, entity, "5", null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //

				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withUserSettings(userSettings) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			List<IConstraintChecker> checkers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			if (checkers != null) {
				for (IConstraintChecker cc : checkers) {
					System.out.println(cc);
				}
			}
			Assertions.assertNull(checkers);
		} finally {
			runner.dispose();
		}
	}
}
