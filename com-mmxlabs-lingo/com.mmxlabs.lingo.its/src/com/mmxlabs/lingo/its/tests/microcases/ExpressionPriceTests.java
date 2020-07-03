/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
			try {
				runnerBuilder.evaluateInitialState();
			} finally {
				runnerBuilder.dispose();
			}
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
			try {
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

			} finally {
				runnerBuilder.dispose();
			}
			CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
			SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
			Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
		};
		Consumer<LocalDate> check = (date) -> {
			load.setWindowStart(date);

			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withThreadCount(1) //
					.buildDefaultRunner();
			try {
				runnerBuilder.evaluateInitialState();
			} finally {
				runnerBuilder.dispose();
			}
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
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

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
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
		} finally {
			runnerBuilder.dispose();
		}
		CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		double expectedLoadPrice = 10.0;
		Assertions.assertEquals(expectedLoadPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		Assertions.assertEquals(expectedLoadPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

	}
}
