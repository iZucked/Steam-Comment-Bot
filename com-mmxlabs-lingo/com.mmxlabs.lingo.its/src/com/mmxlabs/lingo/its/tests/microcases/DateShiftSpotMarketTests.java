/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.internal.DirtyPerspectiveMarker;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class DateShiftSpotMarketTests extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testSpecificDate_Before() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(true);
					params.setValue(16);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 1), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(2.0, purchaseAllocation.getPrice(), 0.0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpecificDate_After() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(true);
					params.setValue(16);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 17), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(3.0, purchaseAllocation.getPrice(), 0.0);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpecificDate_On() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(true);
					params.setValue(16);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 16), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(3.0, purchaseAllocation.getPrice(), 0.0);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShift_Last_N_Days_Before() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(false);
					params.setValue(10);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 17), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(2.0, purchaseAllocation.getPrice(), 0.0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShift_Last_N_Days_After() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(false);
					params.setValue(10);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(3.0, purchaseAllocation.getPrice(), 0.0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShift_Last_N_Days_On() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(false);
					params.setValue(10);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 19), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(3.0, purchaseAllocation.getPrice(), 0.0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShift_First_N_Days_Before() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(false);
					params.setValue(-10);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 9), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(1.0, purchaseAllocation.getPrice(), 0.0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShift_First_N_Days_After() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(false);
					params.setValue(-10);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 11), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(2.0, purchaseAllocation.getPrice(), 0.0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShift_First_N_Days_On() throws Exception {

		pricingModelBuilder.makeCommodityDataCurve("TEST_CURVE", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 1), 1.0) //
				.addIndexPoint(YearMonth.of(2017, 2), 2.0)//
				.addIndexPoint(YearMonth.of(2017, 3), 3.0)//
				.build();

		final DESPurchaseMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("Market", Lists.newArrayList(portFinder.findPort("Sakai")), entity, "", 22.8) //
				.with(m -> {
					final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
					params.setPriceExpression("TEST_CURVE");
					params.setSpecificDay(false);
					params.setValue(-10);
					m.setPriceInfo(params);
				}) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketDESPurchase("L1", market, YearMonth.of(2017, 2), portFinder.findPort("Sakai")) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 10), portFinder.findPort("Sakai"), null, entity, "7") //
				.withWindowStartTime(23) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
			final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
			Assert.assertEquals(1.0, purchaseAllocation.getPrice(), 0.0);
		});
	}
}