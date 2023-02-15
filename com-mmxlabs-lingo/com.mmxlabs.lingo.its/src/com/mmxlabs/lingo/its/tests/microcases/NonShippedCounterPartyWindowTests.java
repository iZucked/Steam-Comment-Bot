/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.validation.ValidationTestUtil;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.FOBDESCargoDatesConstraint;
import com.mmxlabs.models.lng.cargo.validation.ShippingDaysRestrictionConstraint;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ShippingHoursRestrictionChecker;

@ExtendWith(ShiroRunner.class)
public class NonShippedCounterPartyWindowTests extends AbstractMicroTestCase {

	public static Iterable<Object[]> testBasicDESValidation() {
		return Arrays.asList(new Object[][] { //
				{ LocalDate.of(2023, 2, 2), 1, LocalDate.of(2023, 2, 2), 1, true }, // Buy window exactly covers sell window
				{ LocalDate.of(2023, 2, 1), 1, LocalDate.of(2023, 2, 2), 1, false }, // Buy window is before
				{ LocalDate.of(2023, 2, 3), 1, LocalDate.of(2023, 2, 2), 1, false }, // Buy window is after
				{ LocalDate.of(2023, 2, 2), 2, LocalDate.of(2023, 2, 2), 1, true }, // Buy window is before, but completely overlaps sale
				{ LocalDate.of(2023, 2, 1), 3, LocalDate.of(2023, 2, 2), 1, true }, // Buy window is after, but completely overlaps sale
				{ LocalDate.of(2023, 2, 1), 2, LocalDate.of(2023, 2, 2), 2, false }, // Buy window partly overlaps sale
				{ LocalDate.of(2023, 2, 2), 2, LocalDate.of(2023, 2, 1), 2, false }, // Buy window partly overlaps sale
		});
	}

	public static Iterable<Object[]> testBasicFOBValidation() {
		return Arrays.asList(new Object[][] { //
				{ LocalDate.of(2023, 2, 2), 1, LocalDate.of(2023, 2, 2), 1, true }, // Sell window exactly covers buy window
				{ LocalDate.of(2023, 2, 2), 1, LocalDate.of(2023, 2, 1), 1, false }, // Sell window is before
				{ LocalDate.of(2023, 2, 2), 1, LocalDate.of(2023, 2, 3), 1, false }, // Sell window is after
				{ LocalDate.of(2023, 2, 2), 1, LocalDate.of(2023, 2, 2), 2, true }, // Sell window is before, but completely overlaps buy
				{ LocalDate.of(2023, 2, 2), 1, LocalDate.of(2023, 2, 1), 3, true }, // Sell window is after, but completely overlaps buy
				{ LocalDate.of(2023, 2, 2), 2, LocalDate.of(2023, 2, 1), 2, false }, // Sell window partly overlaps buy
				{ LocalDate.of(2023, 2, 1), 2, LocalDate.of(2023, 2, 2), 2, false }, // Sell window partly overlaps buy
		});
	}

	@ParameterizedTest(name = "Buy={0} - {1} Sell={2} - {3}, Valid? {4}")
	@MethodSource("testBasicDESValidation")
	@Tag(TestCategories.MICRO_TEST)
	public void testDestOnlyDESValidation(final LocalDate buyWindowStart, final int buyWindowDays, final LocalDate saleWindowStart, final int saleWindowDays, final boolean pass) {

		final var l1 = cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, buyWindowStart, portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "5", 22.6, null) //
				.withWindowSize(buyWindowDays, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeDESSale("D1", saleWindowStart, portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(saleWindowDays, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_INCOMPATIBLE_WINDOWS);
			if (pass) {
				Assertions.assertTrue(children.isEmpty());
			} else {
				Assertions.assertFalse(children.isEmpty());
			}
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDualCPDESValidation() {

		final var l1 = cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "5", 22.6, null) //
				.withWindowCounterParty(true) //

				.withWindowSize(1, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_DUAL_COUNTERPART_WINDOWS);
			Assertions.assertFalse(children.isEmpty());
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDualCPFOBValidation() {

		final var l1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(2, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "7", null) //
				.withWindowCounterParty(true) //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_DUAL_COUNTERPART_WINDOWS);
			Assertions.assertFalse(children.isEmpty());
		}
	}

	@ParameterizedTest(name = "Buy={0} - {1} Sell={2} - {3}, Valid? {4}")
	@MethodSource("testBasicDESValidation")
	@Tag(TestCategories.MICRO_TEST)
	public void testDestWithSourceDESValidation(final LocalDate buyWindowStart, final int buyWindowDays, final LocalDate saleWindowStart, final int saleWindowDays, final boolean pass) {

		final var l1 = cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, buyWindowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6, null) //
				.withWindowSize(buyWindowDays, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeDESSale("D1", saleWindowStart, portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(saleWindowDays, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_INCOMPATIBLE_WINDOWS);
			if (pass) {
				Assertions.assertTrue(children.isEmpty());
			} else {
				Assertions.assertFalse(children.isEmpty());
			}
		}
	}

	@ParameterizedTest(name = "Buy={0} - {1} Sell={2} - {3}, Valid? {4}")
	@MethodSource("testBasicFOBValidation")
	@Tag(TestCategories.MICRO_TEST)
	public void testSourceOnlyFOBValidation(final LocalDate buyWindowStart, final int buyWindowDays, final LocalDate saleWindowStart, final int saleWindowDays, final boolean pass) {

		final var l1 = cargoModelBuilder //
				.makeFOBPurchase("L1", buyWindowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(buyWindowDays, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, saleWindowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "7", null) //
				.withWindowSize(saleWindowDays, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_INCOMPATIBLE_WINDOWS);
			if (pass) {
				Assertions.assertTrue(children.isEmpty());
			} else {
				Assertions.assertFalse(children.isEmpty());
			}
		}
	}

	@ParameterizedTest(name = "Buy={0} - {1} Sell={2} - {3}, Valid? {4}")
	@MethodSource("testBasicFOBValidation")
	@Tag(TestCategories.MICRO_TEST)
	public void testSourceWithDestDESValidation(final LocalDate buyWindowStart, final int buyWindowDays, final LocalDate saleWindowStart, final int saleWindowDays, final boolean pass) {

		final var l1 = cargoModelBuilder //
				.makeFOBPurchase("L1", buyWindowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(buyWindowDays, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.SOURCE_WITH_DEST, saleWindowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "7", null) //
				.withWindowSize(saleWindowDays, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_INCOMPATIBLE_WINDOWS);
			if (pass) {
				Assertions.assertTrue(children.isEmpty());
			} else {
				Assertions.assertFalse(children.isEmpty());
			}
		}
	}

	public static Iterable<Object[]> testDivertFromSourceDESValidation() {
		return Arrays.asList(new Object[][] { //
				{ LocalDate.of(2023, 1, 1), 1, LocalDate.of(2023, 2, 1), 1, 90, true }, // Should be ok
				{ LocalDate.of(2023, 1, 1), 1, LocalDate.of(2023, 2, 2), 60, 90, false }, // Large CP window, not ok
				// { LocalDate.of(2023, 2, 3), 1, LocalDate.of(2023, 2, 2), 1, 90, false }, //
				// { LocalDate.of(2023, 2, 2), 2, LocalDate.of(2023, 2, 2), 1, 90, true }, //
				// { LocalDate.of(2023, 2, 1), 3, LocalDate.of(2023, 2, 2), 1, 90, true }, //
				// { LocalDate.of(2023, 2, 1), 2, LocalDate.of(2023, 2, 2), 2, 90, false }, //
				// { LocalDate.of(2023, 2, 2), 2, LocalDate.of(2023, 2, 1), 2, 90, false }, //
		});
	}

	@ParameterizedTest(name = "Buy={0} - {1} Sell={2} - {3}, Valid? {4}")
	@MethodSource("testDivertFromSourceDESValidation")
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertFromSourceDESValidation(final LocalDate buyWindowStart, final int buyWindowDays, final LocalDate saleWindowStart, final int saleWindowDays, final int shippingDays,
			final boolean pass) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final var l1 = cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, buyWindowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6, null) //
				.withWindowSize(buyWindowDays, TimePeriod.DAYS)//
				.with(s -> ((LoadSlot) s).setShippingDaysRestriction(shippingDays)) //
				.with(s -> ((LoadSlot) s).setNominatedVessel(vessel)) //
				.build();

		final var d1 = cargoModelBuilder //
				.makeDESSale("D1", saleWindowStart, portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(saleWindowDays, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);

		// This should alway pass for this kind of DES deal
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_INCOMPATIBLE_WINDOWS);
			Assertions.assertTrue(children.isEmpty());
		}
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, true, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ShippingDaysRestrictionConstraint.KEY_ROUND_TRIP_TIME_LARGER_THAN_RESTRICTION);
			if (pass) {
				Assertions.assertTrue(children.isEmpty());
			} else {
				Assertions.assertFalse(children.isEmpty());
			}
		}
	}

	@ParameterizedTest(name = "Buy={0} - {1} Sell={2} - {3}, Valid? {4}")
	@MethodSource("testBasicDESValidation")
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertibleDESValidation(final LocalDate buyWindowStart, final int buyWindowDays, final LocalDate saleWindowStart, final int saleWindowDays, final boolean pass) {

		final var l1 = cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERTIBLE, buyWindowStart, portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "5", 22.6, null) //
				.withWindowSize(buyWindowDays, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeDESSale("D1", saleWindowStart, portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(saleWindowDays, TimePeriod.DAYS) //
				.build();

		cargoModelBuilder.createCargo(l1, d1);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, FOBDESCargoDatesConstraint.KEY_INCOMPATIBLE_WINDOWS);
			if (pass) {
				Assertions.assertTrue(children.isEmpty());
			} else {
				Assertions.assertFalse(children.isEmpty());
			}
		}
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDestOnlyDES_1(final boolean withPeriod) {

		cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "5", 22.6, null) //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build();
		cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.any()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);

		});
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testSourceOnlyFOB_1(final boolean withPeriod) {

		final var l1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "7", null) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.any()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);

		});
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDestWithSourceDES_1(final boolean withPeriod) {

		cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_WITH_SOURCE, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6, null) //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build();
		cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.any()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);

		});
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertFromSourceDES_1(final boolean withPeriod) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6, null) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.with(s -> ((LoadSlot) s).setShippingDaysRestriction(90)) //
				.with(s -> ((LoadSlot) s).setNominatedVessel(vessel)) //
				.build();
		cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2023, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.isNonShipped()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);

		});
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertFromSourceDES_2(final boolean withPeriod) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final var loadSlot = cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6, null) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.with(s -> ((LoadSlot) s).setShippingDaysRestriction(90)) //
				.with(s -> ((LoadSlot) s).setNominatedVessel(vessel)) //
				.build();
		final var dischargeSlot = cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2023, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(60, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		final var scenarioRunner = runnerBuilder.getScenarioRunner();
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

		// Find the slots used in optimiser
		var l1 = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getAllModelObjects(LoadSlot.class).stream().filter(s -> loadSlot.getName().equals(s.getName())).findFirst().get();
		var d1 = scenarioToOptimiserBridge.getDataTransformer()
				.getModelEntityMap()
				.getAllModelObjects(DischargeSlot.class)
				.stream()
				.filter(s -> dischargeSlot.getName().equals(s.getName()))
				.findFirst()
				.get();

		// The counterparty window is too big, expect the constraint checker to fail.
		final ISequences rawSequences = SequenceHelper.createFOBDESSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), l1, d1);

		// Validate the initial sequences are invalid
		final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), rawSequences);
		Assertions.assertNotNull(failedConstraintCheckers);

		// Expect just this one to fail
		Assertions.assertEquals(1, failedConstraintCheckers.size());
		Assertions.assertTrue(failedConstraintCheckers.get(0) instanceof ShippingHoursRestrictionChecker);
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertToDestFOB_1(final boolean withPeriod) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final var l1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "7", null) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.with(s -> ((DischargeSlot) s).setShippingDaysRestriction(90)) //
				.with(s -> ((DischargeSlot) s).setNominatedVessel(vessel)) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.isNonShipped()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);

		});
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertToDestFOB_2(final boolean withPeriod) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final var loadSlot = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(60, TimePeriod.DAYS)//
				.build();

		final var dischargeSlot = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7", null) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.with(s -> ((DischargeSlot) s).setShippingDaysRestriction(90)) //
				.with(s -> ((DischargeSlot) s).setNominatedVessel(vessel)) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		final var scenarioRunner = runnerBuilder.getScenarioRunner();
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		// Find the slots used in optimiser
		var l1 = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getAllModelObjects(LoadSlot.class).stream().filter(s -> loadSlot.getName().equals(s.getName())).findFirst().get();
		var d1 = scenarioToOptimiserBridge.getDataTransformer()
				.getModelEntityMap()
				.getAllModelObjects(DischargeSlot.class)
				.stream()
				.filter(s -> dischargeSlot.getName().equals(s.getName()))
				.findFirst()
				.get();

		// The counterparty window is too big, expect the constraint checker to fail.
		final ISequences rawSequences = SequenceHelper.createFOBDESSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), l1, d1);

		// Validate the initial sequences are invalid
		final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), rawSequences);
		Assertions.assertNotNull(failedConstraintCheckers);

		// Expect just this one to fail
		Assertions.assertEquals(1, failedConstraintCheckers.size());
		Assertions.assertTrue(failedConstraintCheckers.get(0) instanceof ShippingHoursRestrictionChecker);
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertibleDES_1(final boolean withPeriod) {

		cargoModelBuilder //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERTIBLE, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "5", 22.6, null) //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build();
		cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.any()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);
		});
	}

	@ParameterizedTest(name = "Period ? {0}")
	@ValueSource(booleans = { false, true })
	@Tag(TestCategories.MICRO_TEST)
	public void testSourceWithDestFOB_1(final boolean withPeriod) {

		final var l1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 22.6) //
				.withWindowCounterParty(true) //
				.withWindowSize(1, TimePeriod.DAYS)//
				.build();

		final var d1 = cargoModelBuilder //
				.makeFOBSale("D1", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7", null) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build();

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (withPeriod) {
			userSettings.setPeriodStartDate(LocalDate.of(2022, 12, 1));
			userSettings.setPeriodEnd(YearMonth.of(2023, 4));
		}
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker() //
					.withCargo("L1", "D1")
					.any()
					.build();
			//
			final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
			Assertions.assertNotNull(solution);
		});
	}
	//
	// @Test
	// @Tag(TestCategories.MICRO_TEST)
	// public void testCounterPartyWindowInPeriod() {
	// // This test it to make sure a counterpart cargo in the period boundary still
	// // keeps it's window setting.
	//
	// final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
	//
	// final VesselCharter charter1 = cargoModelBuilder.makeVesselCharter(vessel, entity).build();
	//
	// cargoModelBuilder.makeCargo() //
	// .makeFOBPurchase("L1", LocalDate.of(2020, 7, 14), portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, entity, "5") //
	// .withWindowSize(1, TimePeriod.DAYS)//
	// .build() //
	//
	// .makeDESSale("D1", LocalDate.of(2020, 8, 1), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN), null, entity, "7") //
	// .withWindowCounterParty(true) //
	// .withWindowSize(4, TimePeriod.DAYS) //
	// .build() //
	//
	// .withVesselAssignment(charter1, 1) //
	// .withAssignmentFlags(false, false) //
	// .build();
	//
	// // Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in
	// // period.
	// final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
	// userSettings.setMode(OptimisationMode.SHORT_TERM);
	//
	// userSettings.setGenerateCharterOuts(false);
	//
	// userSettings.setShippingOnly(false);
	// userSettings.setSimilarityMode(SimilarityMode.OFF);
	//
	// userSettings.setPeriodStartDate(LocalDate.of(2020, 8, 1));
	// userSettings.setPeriodEnd(YearMonth.of(2021, 4));
	//
	// final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);
	//
	// ScenarioUtils.setLSOStageIterations(optimisationPlan, 10);
	//
	// // Generate internal data
	// LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
	// .withOptimisationPlan(optimisationPlan) //
	// // .withExtraModule(new TransformerExtensionTestBootstrapModule()) //
	// .withThreadCount(1)//
	// .withOptimiseHint() //
	// .buildDefaultRunner();
	//
	// runner.evaluateInitialState();
	//
	// final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();
	//
	// final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
	//
	// // Check locked flags
	// boolean foundD1 = false;
	// final Cargo period_cargo = optimiserScenario.getCargoModel().getCargoes().get(0);
	// for (Slot<?> slot : period_cargo.getSlots()) {
	// Assertions.assertTrue(slot.isLocked());
	// if ("D1".equals(slot.getName())) {
	// Assertions.assertTrue(slot.isWindowCounterParty());
	// Assertions.assertEquals(4, slot.getWindowSize());
	// Assertions.assertEquals(TimePeriod.DAYS, slot.getWindowSizeUnits());
	// foundD1 = true;
	// }
	//
	// }
	// Assertions.assertTrue(foundD1);
	//
	// }
}