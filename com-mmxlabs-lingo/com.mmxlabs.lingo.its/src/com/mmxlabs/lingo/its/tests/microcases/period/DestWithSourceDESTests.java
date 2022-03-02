/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;

/**
 * Some test cases around Dest with source DES cargoes.
 *
 */
@ExtendWith(ShiroRunner.class)
public class DestWithSourceDESTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestOnlyDES() {

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //

				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //

				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
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

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
		//
		// final LNGScenarioModel optimiserScenario =
		// scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		// Assertions.assertEquals(1,
		// optimiserScenario.getCargoModel().getCargoes().size());
		// Assertions.assertEquals(1,
		// optimiserScenario.getCargoModel().getLoadSlots().size());
		// Assertions.assertEquals(1,
		// optimiserScenario.getCargoModel().getDischargeSlots().size());
		//
		// // Check locked flags
		// Assertions.assertFalse(optimiserScenario.getCargoModel().getCargoes().get(0).isLocked());
		// Assertions.assertFalse(optimiserScenario.getCargoModel().getLoadSlots().get(0).isLocked());
		// Assertions.assertTrue(optimiserScenario.getCargoModel().getDischargeSlots().get(0).isLocked());

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
	}

	/**
	 * Optimise the unpaired slots together
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestOnlyDESOpti() {

		cargoModelBuilder.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 23.0, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.build();//

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1000);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.buildDefaultRunner();

		runner.evaluateInitialState();
		runner.run(true);

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

		// Make sure a cargo has been created
		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestOnlyDESMismatch() {

		// Windows mismatch by 1 hour, fail to validate.

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(1) //

				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
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
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestWithSourceOnlyDES() throws Exception {

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
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
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestWithSourceOnlyDESOpti() throws Exception {

		cargoModelBuilder
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 23.0, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.build();

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1000);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		runner.run(true);

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

		// Make sure a cargo has been created
		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestWithSourceOnlyDESOptiPositiveFlex() throws Exception {

		cargoModelBuilder
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 23.0, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.withWindowFlex(1, TimePeriod.HOURS) //
				.build();

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(1) //
				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1000);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		runner.run(true);

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

		// Make sure a cargo has been created
		Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDestWithSourceOnlyDESMismatch() throws Exception {

		// Windows mismatch by 1 hour, fail

		// Load in the basic scenario from CSV
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8, null) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(1) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2019, 8, 14), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
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
	}

	/**
	 * Test to ensure that the localised time windows are used in scheduling and
	 * passed through to the period transformer
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLocalisedWindowUsed_DestWithSource() throws Exception {

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, LocalDate.of(2020, 8, 11), portFinder.findPortById(InternalDataConstants.PORT_SAKHALIN), null, entity, "5", 22.8, null) //
				.withWindowSize(2, TimePeriod.DAYS) //
				.withWindowStartTime(7) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2020, 8, 11), portFinder.findPortById(InternalDataConstants.PORT_SENDAI), null, entity, "7") //
				.withWindowSize(4, TimePeriod.DAYS) //
				.withWindowStartTime(6) //
				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2020, 9, 1));
		userSettings.setPeriodEnd(YearMonth.of(2020, 12));

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withUserSettings(userSettings) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		LoadSlot periodLoad = optimiserScenario.getCargoModel().getLoadSlots().get(0);
		DischargeSlot periodDischarge = optimiserScenario.getCargoModel().getDischargeSlots().get(0);

		// Expect start time to move to 7AM rather than 6AM to match load window start.
		Assertions.assertEquals(7, periodDischarge.getWindowStartTime());
		Assertions.assertEquals(periodLoad.getWindowStartTime(), periodDischarge.getWindowStartTime());
	}

	/**
	 * Test to ensure that the localised time windows are used in scheduling and
	 * passed through to the period transformer
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLocalisedWindowUsed_Divertible() throws Exception {

		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERTIBLE, LocalDate.of(2020, 8, 11), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "5", 22.8, null) //
				.withWindowSize(2, TimePeriod.DAYS) //
				.withWindowStartTime(7) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2020, 8, 11), portFinder.findPortById(InternalDataConstants.PORT_SENDAI), null, entity, "7") //
				.withWindowSize(4, TimePeriod.DAYS) //
				.withWindowStartTime(6) //
				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2020, 9, 1));
		userSettings.setPeriodEnd(YearMonth.of(2020, 12));

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withUserSettings(userSettings) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		LoadSlot periodLoad = optimiserScenario.getCargoModel().getLoadSlots().get(0);
		DischargeSlot periodDischarge = optimiserScenario.getCargoModel().getDischargeSlots().get(0);

		// Expect start time to move to 7AM rather than 6AM to match load window start.
		Assertions.assertEquals(7, periodDischarge.getWindowStartTime());
		Assertions.assertEquals(periodLoad.getWindowStartTime(), periodDischarge.getWindowStartTime());
	}

	/**
	 * Test to ensure that the localised time windows are used in scheduling and
	 * passed through to the period transformer
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLocalisedWindowUsed_SpotMarket_DestOnly() throws Exception {

		List<APortSet<Port>> l = new LinkedList<>();
		l.add(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN));
		l.add(portFinder.findPortById(InternalDataConstants.PORT_SENDAI));
		DESPurchaseMarket sm = spotMarketsModelBuilder.makeDESPurchaseMarket("MKT2", l, entity, "5", 22.8) //

				.build();

		cargoModelBuilder.makeCargo()//
				// .makeDESPurchase("L1", DESPurchaseDealType.DIVERTIBLE, LocalDate.of(2020, 8,
				// 11), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null,
				// entity, "5", 22.8, null)
				// //
				.makeMarketDESPurchase("L1", sm, YearMonth.of(2020, 9), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				// .withShippingDaysRestriction() //
				.build() //

				.makeDESSale("D1", LocalDate.of(2020, 8, 28), portFinder.findPortById(InternalDataConstants.PORT_SENDAI), null, entity, "7") //
				.withWindowSize(4, TimePeriod.DAYS) //
				.withWindowStartTime(6) //
				.build() //

				.build();

		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2020, 9, 1));
		userSettings.setPeriodEnd(YearMonth.of(2020, 12));

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withUserSettings(userSettings) //
				// .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();

		runner.evaluateInitialState();

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

		LoadSlot periodLoad = optimiserScenario.getCargoModel().getLoadSlots().get(0);
		DischargeSlot periodDischarge = optimiserScenario.getCargoModel().getDischargeSlots().get(0);

		// Expect start time to move to 7AM rather than 6AM to match load window start.
		Assertions.assertEquals(0, periodDischarge.getWindowStartTime());
		Assertions.assertEquals(periodLoad.getWindowStartTime(), periodDischarge.getWindowStartTime());
		Assertions.assertEquals(periodLoad.getWindowStart(), periodDischarge.getWindowStart());
	}
}