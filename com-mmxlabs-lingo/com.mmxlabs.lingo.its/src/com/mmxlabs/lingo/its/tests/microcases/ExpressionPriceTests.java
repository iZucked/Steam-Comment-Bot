/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.TimePeriod;

@SuppressWarnings({ "unused", "null" })
@RunWith(value = ShiroRunner.class)
public class ExpressionPriceTests extends AbstractMicroTestCase {
	//
	// @Override
	// public @NonNull IScenarioDataProvider importReferenceData() throws MalformedURLException {
	// final IScenarioDataProvider scenarioDataProvider = importReferenceData("/trainingcases/Shipping_I/");
	//
	// return scenarioDataProvider;
	// }
	// @Override
	// protected BaseLegalEntity importDefaultEntity() {
	// return commercialModelFinder.findEntity("Entity");
	// }

	@Ignore("This test fails due to half hour timezone offset. Midnight Darwin becomes 11:30PM day before in ITC equiv")
	@Test
	@Category({ MicroTest.class })
	public void test1() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		pricingModelBuilder.makeCommodityDataCurve("Test", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 12), 0.0) //
				.addIndexPoint(YearMonth.of(2018, 1), 10.0) //
				.build();
		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeFOBPurchase("F1", LocalDate.of(2018, 1, 1), portFinder.findPort("Darwin LNG"), null, entity, "Test")//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withPricingEvent(PricingEvent.START_LOAD, null) //
				//
				.build() //
				.makeDESSale("D1", LocalDate.of(2018, 2, 1), portFinder.findPort("Chita LNG"), null, entity, "Test") //
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
		Assert.assertEquals(expectedLoadPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		Assert.assertEquals(expectedLoadPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

	}
}
