/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.validation.VesselCharterConstraint;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.validation.MissingScheduledVesselCharterCurveValueConstraint;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Test cases to check that curve data exists for a given vessel charter. This can be the bunker rates, charter rates.
 * 
 * TODO include ballast bonus or repositioning curves.
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class VesselCharterCurveValidDateTests extends AbstractMicroTestCase {

	enum Mode {
		CharterRate, TravelBunkers, IdleBunkers, PilotBunkers, PortBunkers, BallastBonus, Repositioning
	}

	enum CharterDate {
		NONE, StartAfter, StartBy, EndAfter, EndBy
	}

	record TestData(Mode mode, YearMonth indexDate, CharterDate dateMode, LocalDate windowStart, boolean valid) {
		// Used for the test label
		public String toString() {
			return String.format("%s: Idx %s, Charter start: %s %s  valid = %s", mode, indexDate, dateMode, windowStart, valid);
		}
	}

	@SuppressWarnings("null")
	public static Iterable<TestData> generateVesselCharterConstraint() {
		final List<TestData> cases = new LinkedList<>();
		// Expect same behaviour for each of the date types set
		for (final var dateType : CharterDate.values()) {
			// If there is no date, then we expect the constraint to always pass.
			final boolean expectValid = dateType == CharterDate.NONE;
			cases.addAll(Lists.newArrayList( //
					// Charter rate
					new TestData(Mode.CharterRate, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData(Mode.CharterRate, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 1, 1), expectValid), // Invalid - vessel date < index start
					// Each of the bunker fuel types
					new TestData(Mode.TravelBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData(Mode.TravelBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 1, 1), expectValid), // Invalid - vessel date <index start
					new TestData(Mode.IdleBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData(Mode.IdleBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 1, 1), expectValid), // Invalid - vessel date < index start
					new TestData(Mode.PilotBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData(Mode.PilotBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 1, 1), expectValid), // Invalid - vessel date < index start
					new TestData(Mode.PortBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData(Mode.PortBunkers, YearMonth.of(2023, 2), dateType, LocalDate.of(2023, 1, 1), expectValid) // Invalid - vessel date < index start
			));
		}
		return cases;
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateVesselCharterConstraint")
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselCharterConstraint(final TestData data) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_150);
		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity).build(); //
		// Configure test data
		switch (data.mode) {
		case CharterRate: {
			pricingModelBuilder.makeCharterDataCurve("testcurve", "$", "day") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			charter.setTimeCharterRate("testcurve");
			break;
		}
		case TravelBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setBaseFuel(bf);
			break;
		}
		case IdleBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setIdleBaseFuel(bf);
			break;
		}
		case PortBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setInPortBaseFuel(bf);
			break;
		}
		case PilotBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setPilotLightBaseFuel(bf);
			break;
		}
		}
		switch (data.dateMode) {
		case StartAfter -> charter.setStartAfter(data.windowStart.atStartOfDay());
		case StartBy -> charter.setStartBy(data.windowStart.atStartOfDay());
		case EndAfter -> charter.setEndAfter(data.windowStart.atStartOfDay());
		case EndBy -> charter.setEndBy(data.windowStart.atStartOfDay());
		case NONE -> {
			/* Do not set a date */}
		}
		final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and
		// get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

		// Depending on the mode we will expect a different part of the constraint to fail.
		final Object key = switch (data.mode) {
		case CharterRate -> VesselCharterConstraint.KEY_VESSEL_CHARTER_RATE;
		case TravelBunkers, IdleBunkers, PilotBunkers, PortBunkers -> VesselCharterConstraint.KEY_VESSEL_BUNKER_PRICE;
		default -> null;
		};

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, key);
		if (data.valid) {
			Assertions.assertTrue(children.isEmpty());
		} else {
			Assertions.assertFalse(children.isEmpty());
			// Should we assert further on returned state?
		}
	}

	enum CharterType {
		Vessel, Market
	}

	record TestData2(Mode mode, YearMonth indexDate, CharterType charterType, LocalDate windowStart, boolean valid) {
		// Used for the test label
		public String toString() {
			return String.format("%s (%s): Idx %s, Charter start: %s  valid = %s", mode, charterType, indexDate, windowStart, valid);
		}
	}

	public static Iterable<TestData2> generateMissingScheduledVesselCharterCurveValueConstraint() {
		final List<TestData2> cases = new LinkedList<>();
		// Expect same behaviour for each of the date types set
		for (final var charterType : CharterType.values()) {
			// If there is no date, then we expect the constraint to always pass.
			cases.addAll(Lists.newArrayList( //
					// Charter rate
					new TestData2(Mode.CharterRate, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData2(Mode.CharterRate, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 1, 1), false), // Invalid - vessel date < index start
					// Each of the bunker fuel types
					new TestData2(Mode.TravelBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData2(Mode.TravelBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 1, 1), false), // Invalid - vessel date <index start
					new TestData2(Mode.IdleBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData2(Mode.IdleBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 1, 1), false), // Invalid - vessel date < index start
					new TestData2(Mode.PilotBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData2(Mode.PilotBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 1, 1), false), // Invalid - vessel date < index start
					new TestData2(Mode.PortBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 2, 1), true), // Valid - vessel date >= index start
					new TestData2(Mode.PortBunkers, YearMonth.of(2023, 2), charterType, LocalDate.of(2023, 1, 1), false) // Invalid - vessel date < index start
			));
		}
		return cases;
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateMissingScheduledVesselCharterCurveValueConstraint")
	@Tag(TestCategories.MICRO_TEST)
	public void testMissingScheduledVesselCharterCurveValueConstraint(final TestData2 data) {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_150);
		VesselAssignmentType charter;
		if (data.charterType == CharterType.Vessel) {
			charter = cargoModelBuilder.makeVesselCharter(vessel, entity).build(); //
		} else if (data.charterType == CharterType.Market) {
			charter = spotMarketsModelBuilder.createCharterInMarket("market", vessel, entity, "0", 1);
			((CharterInMarket) charter).setEnabled(true);

		} else {
			charter = null;
			assert false;
		}

		// Configure test data
		switch (data.mode) {
		case CharterRate: {
			pricingModelBuilder.makeCharterDataCurve("testcurve", "$", "day") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			if (data.charterType == CharterType.Vessel) {
				((VesselCharter) charter).setTimeCharterRate("testcurve");
			} else if (data.charterType == CharterType.Market) {
				((CharterInMarket) charter).setCharterInRate("testcurve");
			}
			break;
		}
		case TravelBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setBaseFuel(bf);
			break;
		}
		case IdleBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setIdleBaseFuel(bf);
			break;
		}
		case PortBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setInPortBaseFuel(bf);
			break;
		}
		case PilotBunkers: {
			final BaseFuel bf = fleetModelBuilder.createBaseFuel("testfuel", 40.);
			pricingModelBuilder.makeBunkerFuelDataCurve("testcurve", "$", "MT") //
					.addIndexPoint(data.indexDate, 1) //
					.build();
			costModelBuilder.createOrUpdateBaseFuelCost(bf, "testcurve");
			vessel.setPilotLightBaseFuel(bf);
			break;
		}
		}

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", data.windowStart, portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5")
				.build() //
				.makeDESSale("D1", data.windowStart.plusDays(14), portFinder.findPortById(InternalDataConstants.PORT_DAHEJ), null, entity, "7")
				.build() //
				.withAssignmentType(charter) //
				.build();

		// Constraint runs on the evaluated base case
		Schedule schedule = evaluateAndReturnSchedule();
		Assertions.assertNotNull(schedule);

		// MissingScheduledVesselCharterCurveValueConstraint is only run with the background validator
		final IStatus statusOrig = ValidationTestUtil.validateWithAllConstraints(scenarioDataProvider);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and
		// get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

		// Depending on the mode we will expect a different part of the constraint to fail.
		final Object key = switch (data.mode) {
		case CharterRate -> data.charterType == CharterType.Vessel ? MissingScheduledVesselCharterCurveValueConstraint.KEY_CHARTER_COST
				: MissingScheduledVesselCharterCurveValueConstraint.KEY_MARKET_CHARTER_COST;
		case TravelBunkers, IdleBunkers, PilotBunkers, PortBunkers -> MissingScheduledVesselCharterCurveValueConstraint.KEY_BUNKER_COST;
		default -> null;
		};

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, key);
		if (data.valid) {
			Assertions.assertTrue(children.isEmpty());
		} else {
			Assertions.assertFalse(children.isEmpty());
			// Should we assert further on returned state?
		}
	}
}
