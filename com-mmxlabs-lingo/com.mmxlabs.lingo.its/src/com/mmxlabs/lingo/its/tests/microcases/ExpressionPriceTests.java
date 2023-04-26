/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(value = ShiroRunner.class)
@RequireFeature(value = { KnownFeatures.FEATURE_EXPOSURES })
public class ExpressionPriceTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSplitMonthInSpotMarketFixedPrices() {

		final DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "SPLITMONTH(5,10,15)")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.of(2018, 6), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		final Slot load = testCargo.getSlots().get(0);
		final Slot discharge = testCargo.getSlots().get(1);

		final BiConsumer<LocalDate, Double> test = (date, expectedPrice) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
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

		final DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "SPLITMONTH(HHH1,HHH2,15)")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.of(2018, 6), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		final Slot load = testCargo.getSlots().get(0);
		final Slot discharge = testCargo.getSlots().get(1);

		final BiConsumer<LocalDate, Double> test = (date, expectedPrice) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();

			final Injector injector = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge().getInjector();
			final DateAndCurveHelper dateHelper = injector.getInstance(DateAndCurveHelper.class);
			// dateHelper.

			final ModelEntityMap mem = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge().getDataTransformer().getModelEntityMap();
			// System.out.println(mem.getDateFromHours(-735, "UTC"));
			// System.out.println(mem.getDateFromHours(-399, "UTC"));
			// System.out.println(mem.getDateFromHours(9, "UTC"));
			// System.out.println(mem.getDateFromHours(345, "UTC"));
			// System.out.println(mem.getDateFromHours(7641, "UTC")); // last curve interval
			// System.out.println(mem.getDateFromHours(8664, "UTC")); // Discharge pricing date
			//
			// System.out.println(mem.getDateFromHours(9096, "UTC")); // Last curve price
			// System.out.println(mem.getDateFromHours(9504, "UTC")); // Discharge pricing date

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
			Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
		};
		final Consumer<LocalDate> check = (date) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
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
		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
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

		final Slot load = testCargo.getSlots().get(0);
		final Slot discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedLoadPrice = 10.0;
		Assertions.assertEquals(expectedLoadPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		Assertions.assertEquals(expectedLoadPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

	}

	/**
	 * Data for the shift function tests. These set the pricing date, expression and expected value for the load slot Added to test fix for FB 5310
	 * 
	 * @return
	 */
	public static Iterable<Object[]> generateShiftData() {
		return Arrays.asList(new Object[][] { //

				// Check values for varying month durations. Bug caused the wrong month duration
				// to be used in change point
				{ LocalDate.of(2021, 1, 1), "SHIFT(Test, 1)", 12.0, YearMonth.of(2020, 12) }, //
				{ LocalDate.of(2021, 1, 31), "SHIFT(Test, 1)", 12.0, YearMonth.of(2020, 12) }, //
				{ LocalDate.of(2021, 1, 1), "SHIFT(Test, 2)", 11.0, YearMonth.of(2020, 11) }, //
				{ LocalDate.of(2021, 1, 31), "SHIFT(Test, 2)", 11.0, YearMonth.of(2020, 11) }, //
				{ LocalDate.of(2021, 1, 1), "SHIFT(Test, 3)", 10.0, YearMonth.of(2020, 10) }, //
				{ LocalDate.of(2021, 1, 31), "SHIFT(Test, 3)", 10.0, YearMonth.of(2020, 10) }, //

				// Test data range - bug caused data range to be truncated.
				{ LocalDate.of(2020, 1, 1), "SHIFT(Test, 3)", 0.0, YearMonth.of(2019, 10) }, // Too early
				{ LocalDate.of(2020, 2, 1), "SHIFT(Test, 3)", 0.0, YearMonth.of(2019, 11) }, // Too early
				{ LocalDate.of(2020, 3, 1), "SHIFT(Test, 3)", 0.0, YearMonth.of(2019, 12) }, // Too early
				{ LocalDate.of(2020, 4, 1), "SHIFT(Test, 3)", 1.0, YearMonth.of(2020, 1) }, // First Entry
				{ LocalDate.of(2020, 5, 1), "SHIFT(Test, 3)", 2.0, YearMonth.of(2020, 2) }, // Within data
				{ LocalDate.of(2021, 5, 1), "SHIFT(Test, 3)", 14.0, YearMonth.of(2021, 2) }, // Within data
				{ LocalDate.of(2021, 6, 1), "SHIFT(Test, 3)", 15.0, YearMonth.of(2021, 3) }, // Last Entry
				{ LocalDate.of(2021, 7, 1), "SHIFT(Test, 3)", 15.0, YearMonth.of(2021, 4) }, // Out of data
		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} {1} = {2}")
	@MethodSource("generateShiftData")
	public void testShiftFunction(final LocalDate pricingDate, final String expression, final double expected, YearMonth exposureMonth) {

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

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
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

		final Slot load = testCargo.getSlots().get(0);
		final Slot discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedBuySellPrice = expected;
		Assertions.assertEquals(expectedBuySellPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		// Assertions.assertEquals(expectedBuySellPrice,
		// simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		// Financial + physical
		Assertions.assertEquals(1 + 1, exposures.size());
		{
			final Optional<ExposureDetail> detail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "test")).findFirst();
			Assertions.assertTrue(detail.isPresent());
			Assertions.assertEquals(exposureMonth, detail.get().getDate());
		}

	}

	public static Iterable<Object[]> generateMonthPriceData() {
		return Arrays.asList(new Object[][] { //

				// Pricing date and expression month match
				{ LocalDate.of(2020, 1, 1), "Test[Jan]", 1.0, YearMonth.of(2020, 1) }, //
				{ LocalDate.of(2020, 1, 31), "Test[Jan]", 1.0, YearMonth.of(2020, 1) }, //
				{ LocalDate.of(2021, 1, 1), "Test[Jan]", 13.0, YearMonth.of(2021, 1) }, //
				{ LocalDate.of(2022, 1, 1), "Test[Jan]", 25.0, YearMonth.of(2022, 1) }, //

				// Find the next Jan
				{ LocalDate.of(2019, 2, 1), "Test[Jan]", 0.0, YearMonth.of(2019, 1) }, //
				{ LocalDate.of(2020, 2, 1), "Test[Jan]", 1.0, YearMonth.of(2020, 1) }, //

				/// Check each month is correct
				{ LocalDate.of(2020, 1, 1), "Test[Jan]", 1.0, YearMonth.of(2020, 1) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Feb]", 2.0, YearMonth.of(2020, 2) }, //
				{ LocalDate.of(2020, 1, 1), "Test[MAR]", 3.0, YearMonth.of(2020, 3) }, //
				{ LocalDate.of(2020, 1, 1), "Test[apr]", 4.0, YearMonth.of(2020, 4) }, //
				{ LocalDate.of(2020, 1, 1), "Test[May]", 5.0, YearMonth.of(2020, 5) }, //
				{ LocalDate.of(2020, 1, 1), "Test[JUN]", 6.0, YearMonth.of(2020, 6) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Jul]", 0.0, YearMonth.of(2019, 7) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Aug]", 0.0, YearMonth.of(2019, 8) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Sep]", 0.0, YearMonth.of(2019, 9) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Oct]", 0.0, YearMonth.of(2019, 10) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Nov]", 0.0, YearMonth.of(2019, 11) }, //
				{ LocalDate.of(2020, 1, 1), "Test[Dec]", 0.0, YearMonth.of(2019, 12) }, //

		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} {1} = {2}")
	@MethodSource("generateMonthPriceData")
	public void testSpecificMonthExpressionTest(final LocalDate windowDate, final String expression, final double expectedPrice, YearMonth exposureMonth) {

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
				.addIndexPoint(YearMonth.of(2021, 4), 16.0) //
				.addIndexPoint(YearMonth.of(2021, 5), 17.0) //
				.addIndexPoint(YearMonth.of(2021, 6), 18.0) //
				.addIndexPoint(YearMonth.of(2021, 7), 19.0) //
				.addIndexPoint(YearMonth.of(2021, 8), 20.0) //
				.addIndexPoint(YearMonth.of(2021, 9), 21.0) //
				.addIndexPoint(YearMonth.of(2021, 10), 22.0) //
				.addIndexPoint(YearMonth.of(2021, 11), 23.0) //
				.addIndexPoint(YearMonth.of(2021, 12), 24.0) //

				.addIndexPoint(YearMonth.of(2022, 1), 25.0) //
				.addIndexPoint(YearMonth.of(2022, 2), 26.0) //
				.addIndexPoint(YearMonth.of(2022, 3), 27.0) //
				.addIndexPoint(YearMonth.of(2022, 4), 28.0) //
				.addIndexPoint(YearMonth.of(2022, 5), 29.0) //
				.addIndexPoint(YearMonth.of(2022, 6), 30.0) //
				.addIndexPoint(YearMonth.of(2022, 7), 31.0) //
				.addIndexPoint(YearMonth.of(2022, 8), 32.0) //
				.addIndexPoint(YearMonth.of(2022, 9), 33.0) //
				.addIndexPoint(YearMonth.of(2022, 10), 34.0) //
				.addIndexPoint(YearMonth.of(2022, 11), 35.0) //
				.addIndexPoint(YearMonth.of(2022, 12), 36.0) //

				.build();

		final DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "0")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, windowDate, portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, expression, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.from(windowDate), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		// Financial + physical
		Assertions.assertEquals(1 + 1, exposures.size());
		{
			final Optional<ExposureDetail> detail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "test")).findFirst();
			Assertions.assertTrue(detail.isPresent());
			Assertions.assertEquals(exposureMonth, detail.get().getDate());
		}
	}

	public static Iterable<Object[]> generateShiftPriceData() {
		return Arrays.asList(new Object[][] { //

				// Window/Pricing date, expression, expected value, exposure month
				{ LocalDate.of(2020, 3, 1), "Test[m]", 3, YearMonth.of(2020, Month.MARCH) }, //
				{ LocalDate.of(2020, 3, 1), "Test[m0]", 3, YearMonth.of(2020, Month.MARCH) }, //
				{ LocalDate.of(2020, 3, 1), "Test[m1]", 4, YearMonth.of(2020, Month.APRIL) }, //
				{ LocalDate.of(2020, 3, 1), "Test[m2]", 5, YearMonth.of(2020, Month.MAY) }, //
				{ LocalDate.of(2020, 3, 1), "Test[m-1]", 2, YearMonth.of(2020, Month.FEBRUARY) }, //
				{ LocalDate.of(2020, 3, 1), "Test[m-2]", 1, YearMonth.of(2020, Month.JANUARY) }, //

		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} {1} = {2}")
	@MethodSource("generateShiftPriceData")
	public void testShiftExpressionTest(final LocalDate windowDate, final String expression, final double expectedPrice, final YearMonth exposureMonth) {

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

				.build();

		final DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "0")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, windowDate, portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, expression, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.from(windowDate), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		// Financial + physical
		Assertions.assertEquals(1 + 1, exposures.size());
		{
			final Optional<ExposureDetail> detail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "test")).findFirst();
			Assertions.assertTrue(detail.isPresent());
			Assertions.assertEquals(exposureMonth, detail.get().getDate());
		}

	}

	public static Iterable<Object[]> generateTierPriceData() {
		return Arrays.asList(new Object[][] { //

				// Window/Pricing date, expression, expected value, exposure month
				{ LocalDate.of(2020, 1, 1), "Tier(1, <2, TestLow, <= 13, TestMid, TestHigh)", 10, YearMonth.of(2020, Month.JANUARY) }, //
				{ LocalDate.of(2020, 1, 1), "Tier(2, <2, TestLow, <= 13, TestMid, TestHigh)", 20, YearMonth.of(2020, Month.JANUARY) }, //
				{ LocalDate.of(2020, 1, 1), "Tier(13, <2, TestLow, <= 13, TestMid, TestHigh)", 20, YearMonth.of(2020, Month.JANUARY) }, //
				{ LocalDate.of(2020, 1, 1), "Tier(14, <2, TestLow, <= 13, TestMid, TestHigh)", 30, YearMonth.of(2020, Month.JANUARY) }, //

		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} {1} = {2}")
	@MethodSource("generateTierPriceData")
	public void testTierExpressionTest(final LocalDate windowDate, final String expression, final double expectedPrice, final YearMonth exposureMonth) {

		pricingModelBuilder.makeCommodityDataCurve("TestLow", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 1), 10.0) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("TestMid", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 1), 20.0) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("TestHigh", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 1), 30.0) //
				.build();

		final DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "0")
				.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, windowDate, portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, expression, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.from(windowDate), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		// Financial + physical
		Assertions.assertEquals(1 + 1, exposures.size());
		{
			final String curve;
			if (expectedPrice == 10) {
				curve = "TestLow";
			} else if (expectedPrice == 20) {
				curve = "TestMid";
			} else if (expectedPrice == 30) {
				curve = "TestHigh";
			} else {
				curve = null;
			}
			Assertions.assertNotNull(curve);

			final Optional<ExposureDetail> detail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> curve.equalsIgnoreCase(d.getIndexName())).findFirst();
			Assertions.assertTrue(detail.isPresent());
			Assertions.assertEquals(exposureMonth, detail.get().getDate());
		}

	}

	public static Iterable<Object[]> generateBlendPriceData() {
		return Arrays.asList(new Object[][] { //

				{ LocalDate.of(2020, 1, 1), "TIERBLEND(@VOL_MMBTU, TestLow, 1500000, TestHigh)", 1_000_000, 10, YearMonth.of(2020, Month.JANUARY) }, //
				{ LocalDate.of(2020, 1, 1), "TIERBLEND(@VOL_MMBTU, TestLow, 1500000, TestHigh)", 1_500_000, 10, YearMonth.of(2020, Month.JANUARY) }, //
				{ LocalDate.of(2020, 1, 1), "TIERBLEND(@VOL_MMBTU, TestLow, 1500000, TestHigh)", 2_000_000, 12.5, YearMonth.of(2020, Month.JANUARY) }, //
				{ LocalDate.of(2020, 1, 1), "TIERBLEND(@VOL_MMBTU, TestLow, 1500000, TestHigh)", 3_000_000, 15, YearMonth.of(2020, Month.JANUARY) }, //

		});
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0} {1} = {2}")
	@MethodSource("generateBlendPriceData")
	public void testBlendExpressionTest(final LocalDate windowDate, final String expression, int volumeMMBTU, final double expectedPrice, final YearMonth exposureMonth) {

		pricingModelBuilder.makeCommodityDataCurve("TestLow", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 1), 10.0) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("TestHigh", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 1), 20.0) //
				.build();

		final DESSalesMarket mkt = spotMarketsModelBuilder.makeDESSaleMarket("Market", portFinder.findPortById(InternalDataConstants.PORT_CHITA), entity, "0")
				.withVolumeLimits(volumeMMBTU, volumeMMBTU, VolumeUnits.MMBTU) //
				.build();

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, windowDate, portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, expression, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeMarketDESSale("D1", mkt, YearMonth.from(windowDate), portFinder.findPortById(InternalDataConstants.PORT_CHITA)) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		// Always expect low curve
		{
			final String curve = "TestLow";
			final Optional<ExposureDetail> detail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> curve.equalsIgnoreCase(d.getIndexName())).findFirst();

			Assertions.assertTrue(detail.isPresent());
			Assertions.assertEquals(exposureMonth, detail.get().getDate());
			Assertions.assertEquals(-Math.min(1_500_000, volumeMMBTU), detail.get().getVolumeInMMBTU());
		}
		// Expect high curve
		{
			final String curve = "TestHigh";
			final Optional<ExposureDetail> detail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> curve.equalsIgnoreCase(d.getIndexName())).findFirst();

			if (expectedPrice == 10) {
				// Financial + physical
				Assertions.assertEquals(1 + 1, exposures.size());
				Assertions.assertFalse(detail.isPresent());
			} else if (expectedPrice > 10) {
				// 2 x Financial + physical
				Assertions.assertEquals(1 + 1 + 1, exposures.size());
				Assertions.assertTrue(detail.isPresent());
				Assertions.assertEquals(exposureMonth, detail.get().getDate());
				// Expect excess over 1_500_000
				Assertions.assertEquals(-(volumeMMBTU - 1_500_000), detail.get().getVolumeInMMBTU());
			} else {
				Assertions.fail();
			}
		}

	}
}
