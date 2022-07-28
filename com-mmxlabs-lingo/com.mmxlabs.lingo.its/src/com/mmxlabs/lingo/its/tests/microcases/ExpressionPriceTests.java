/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(value = ShiroRunner.class)
public class ExpressionPriceTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSplitMonthInSpotMarketFixedPrices() {

		DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "SPLITMONTH(5,10,15)")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.of(2018, 6), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		BiConsumer<LocalDate, Double> test = (date, expectedPrice) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();

			CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
			Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
		};

		// Expect H1
		test.accept(LocalDate.of(2019, 6, 1), 5.0);

		// Expect H2
		test.accept(LocalDate.of(2019, 6, 15), 10.0);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSplitMonthInSpotMarket() {

		pricingModelBuilder.makeCommodityDataCurve("HHH1", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2018, 5), 5.0) //
				.addIndexPoint(YearMonth.of(2018, 6), 5.0) //
				.addIndexPoint(YearMonth.of(2018, 7), 5.0) //
				.build();

		pricingModelBuilder.makeCommodityDataCurve("HHH2", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2018, 5), 10.0) //
				.addIndexPoint(YearMonth.of(2018, 6), 10.0) //
				.addIndexPoint(YearMonth.of(2018, 7), 10.0) //
				.build();

		DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "SPLITMONTH(HHH1,HHH2,15)")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.of(2018, 6), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		BiConsumer<LocalDate, Double> test = (date, expectedPrice) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();

			Injector injector = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge().getInjector();
			DateAndCurveHelper dateHelper = injector.getInstance(DateAndCurveHelper.class);
			// dateHelper.

			ModelEntityMap mem = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge().getDataTransformer().getModelEntityMap();
			System.out.println(mem.getDateFromHours(-735, "UTC"));
			System.out.println(mem.getDateFromHours(-399, "UTC"));
			System.out.println(mem.getDateFromHours(9, "UTC"));
			System.out.println(mem.getDateFromHours(345, "UTC"));
			System.out.println(mem.getDateFromHours(7641, "UTC")); // last curve interval
			System.out.println(mem.getDateFromHours(8664, "UTC")); // Discharge pricing date

			System.out.println(mem.getDateFromHours(9096, "UTC")); // Last curve price
			System.out.println(mem.getDateFromHours(9504, "UTC")); // Discharge pricing date

			CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
			Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
		};
		Consumer<LocalDate> check = (date) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();

			CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
			System.err.printf("%s (%s): %f\n", date, simpleCargoAllocation.getDischargeAllocation().getSlotVisit().getStart().toLocalDate(), simpleCargoAllocation.getDischargeAllocation().getPrice());
		};

		for (int i = 0; i < 40; ++i) {
			// check.accept(LocalDate.of(2019, 5, 28).plusDays(i));

		}

		// Expect H1
		test.accept(LocalDate.of(2019, 6, 1), 5.0);

		// Expect H2
		test.accept(LocalDate.of(2019, 6, 15), 10.0);
	}

	@Disabled("This test fails due to half hour timezone offset. Midnight Darwin becomes 11:30PM day before in ITC equiv")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test1() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();
		// final CharterInMarket charterInMarket_1 =
		// spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000",
		// 0);

		pricingModelBuilder.makeCommodityDataCurve("Test", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 12), 0.0) //
				.addIndexPoint(YearMonth.of(2018, 1), 10.0) //
				.build();
		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeFOBPurchase("F1", LocalDate.of(2018, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, entity, "Test")//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withPricingEvent(PricingEvent.START_LOAD, null) //
				//
				.build() //
				.makeDESSale("D1", LocalDate.of(2018, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "Test") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.withPricingEvent(PricingEvent.START_LOAD, null) //
				.build() //
				//
				// .withVesselAssignment(charterInMarket_1, -1, 1) //
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		double expectedLoadPrice = 10.0;
		Assertions.assertEquals(expectedLoadPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		Assertions.assertEquals(expectedLoadPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

	}

	/**
	 * Data for the shift function tests. These set the pricing date, expression and
	 * expected value for the load slot Added to test fix for FB 5310
	 * 
	 * @return
	 */
	public static Iterable<Object[]> generateShiftData() {
		return Arrays.asList(new Object[][] { //

				// Check values for varying month durations. Bug caused the wrong month duration
				// to be used in change point
				{ LocalDate.of(2021, 1, 1), "SHIFT(Test, 1)", 12.0 }, //
				{ LocalDate.of(2021, 1, 31), "SHIFT(Test, 1)", 12.0 }, //
				{ LocalDate.of(2021, 1, 1), "SHIFT(Test, 2)", 11.0 }, //
				{ LocalDate.of(2021, 1, 31), "SHIFT(Test, 2)", 11.0 }, //
				{ LocalDate.of(2021, 1, 1), "SHIFT(Test, 3)", 10.0 }, //
				{ LocalDate.of(2021, 1, 31), "SHIFT(Test, 3)", 10.0 }, //

				// Test data range - bug caused data range to be truncated.
				{ LocalDate.of(2020, 1, 1), "SHIFT(Test, 3)", 0.0 }, // Too early
				{ LocalDate.of(2020, 2, 1), "SHIFT(Test, 3)", 0.0 }, // Too early
				{ LocalDate.of(2020, 3, 1), "SHIFT(Test, 3)", 0.0 }, // Too early
				{ LocalDate.of(2020, 4, 1), "SHIFT(Test, 3)", 1.0 }, // First Entry
				{ LocalDate.of(2020, 5, 1), "SHIFT(Test, 3)", 2.0 }, // Within data
				{ LocalDate.of(2021, 5, 1), "SHIFT(Test, 3)", 14.0 }, // Within data
				{ LocalDate.of(2021, 6, 1), "SHIFT(Test, 3)", 15.0 }, // Last Entry
				{ LocalDate.of(2021, 7, 1), "SHIFT(Test, 3)", 15.0 }, // Out of data
		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} {1} = {2}")
	@MethodSource("generateShiftData")
	public void testShiftFunction(LocalDate pricingDate, String expression, double expected) {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		pricingModelBuilder.makeCommodityDataCurve("Test", "$", "mmBtu") //

				.addIndexPoint(YearMonth.of(2020, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2020, 2), 2.0) //
				.addIndexPoint(YearMonth.of(2020, 3), 3.0) //
				.addIndexPoint(YearMonth.of(2020, 4), 4.0) //
				.addIndexPoint(YearMonth.of(2020, 5), 5.0) //
				.addIndexPoint(YearMonth.of(2020, 6), 6.0) //
				.addIndexPoint(YearMonth.of(2020, 7), 7.0) //
				.addIndexPoint(YearMonth.of(2020, 8), 8.0) //
				.addIndexPoint(YearMonth.of(2020, 9), 9.0) //
				.addIndexPoint(YearMonth.of(2020, 10), 10.0) //
				.addIndexPoint(YearMonth.of(2020, 11), 11.0) //
				.addIndexPoint(YearMonth.of(2020, 12), 12.0) //

				.addIndexPoint(YearMonth.of(2021, 1), 13.0) //
				.addIndexPoint(YearMonth.of(2021, 2), 14.0) //
				.addIndexPoint(YearMonth.of(2021, 3), 15.0) //

				.build();

		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeFOBPurchase("F1", LocalDate.of(2021, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, entity, expression)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withPricingEvent(PricingEvent.START_LOAD, pricingDate) //
				//
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 1, 31), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, expression) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.withPricingEvent(PricingEvent.START_LOAD, null) //
				.build() //
				//
				// .withVesselAssignment(charterInMarket_1, -1, 1) //
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		double expectedBuySellPrice = expected;
		Assertions.assertEquals(expectedBuySellPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		// Assertions.assertEquals(expectedBuySellPrice,
		// simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

	}

}
