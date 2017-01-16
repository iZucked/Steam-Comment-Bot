/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@RunWith(value = ShiroRunner.class)
public class BreakEvenTests extends AbstractMicroTestCase {

	/**
	 * DES Purchase break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_BE_Purchase() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "?", null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(7.0, cargoAllocation.getLoadAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * DES Purchase break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_BE_Sale() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "?") //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(7.0, cargoAllocation.getDischargeAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * FOB Sale break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_BE_Sale() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.build() //
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "?", null) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(7.0, cargoAllocation.getDischargeAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * FOB Sale break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_BE_Purchase() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "?") //
				.build() //
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(7.0, cargoAllocation.getLoadAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * Shipped break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testShipped_BE_Purchase() throws Exception {

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "?") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(6.558, cargoAllocation.getLoadAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * Shipped break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testShipped_BE_Sale() throws Exception {

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "?") //
				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(7.454, cargoAllocation.getDischargeAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	protected void assertZeroPNL(final SimpleCargoAllocation cargoAllocation) {
		Assert.assertEquals(0, (int) PNLDeltaUtils.getElementProfitAndLoss(cargoAllocation.getCargoAllocation()), 1);
	}
}