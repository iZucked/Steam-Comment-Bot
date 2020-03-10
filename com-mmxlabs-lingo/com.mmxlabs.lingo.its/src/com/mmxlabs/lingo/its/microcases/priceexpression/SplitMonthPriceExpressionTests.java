/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.priceexpression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseUtils;
import com.mmxlabs.lingo.its.tests.microcases.TimeWindowsTestsUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@ExtendWith(ShiroRunner.class)
public class SplitMonthPriceExpressionTests extends AbstractMicroTestCase {

	private static String loadName = "load";
	private static String dischargeName = "discharge";

	@Override
	protected void setPromptDates() {
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
	}

	private int daysToHours(final int days) {
		return (days - 1) * 24;
	}

	private VesselAvailability createTestVesselAvailability(final LocalDateTime startStart, final LocalDateTime startEnd, final LocalDateTime endStart) {
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(startStart, startEnd) //
				.withEndWindow(endStart) //
				.build();
	}

	/**
	 * Test: Simple price expression with constants value. Vessel Availability
	 * lasting a month. Load and Discharge are unbounded during that period. Price
	 * in the first half negligible and really high in the second half. Expected
	 * result: Load during the first half and discharge during the second half
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSplitMonthExpressionSimpleConstant() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability = createTestVesselAvailability(LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		// Create cargo 1, cargo 2
		final int splitDay = 15;

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, String.format("SPLITMONTH(1, 10, %d)", splitDay)) //
				.withWindowStartTime(0) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2018, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_DRAGON), null, entity, String.format("SPLITMONTH(1, 10, %d)", splitDay)) //
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.withVesselAssignment(vesselAvailability, 1).build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			MicroCaseUtils.withEvaluationInjectorPerChainScope(scenarioToOptimiserBridge, injector -> {
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				final TimeWindowScheduler priceBasedSequenceScheduler = injector.getInstance(TimeWindowScheduler.class);
				// Ensure scheduling with price window trimming is enabled.
				priceBasedSequenceScheduler.setUsePriceBasedWindowTrimming(true);

				final ScheduledTimeWindows scheduledTimeWindows = priceBasedSequenceScheduler.calculateTrimmedWindows(initialSequences);

				// get optimiser objects
				final IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, scheduledTimeWindows);
				final IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, scheduledTimeWindows);
				final ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
				final IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

				// make sure no objects are null
				assert load != null;
				assert discharge != null;
				assert loadPortTimeWindowsRecord != null;
				assert dischargePortTimeWindowsRecord != null;

				// get optimised time windows
				final ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
				final ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

				// tests
				System.out.println(ScheduleTools.getPrice(optimiserScenario, getDefaultEMFLoadSlot()));
				Assertions.assertEquals(1, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFLoadSlot()), 0.0001);
				Assertions.assertEquals(10, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
				Assertions.assertTrue(loadFeasibleTimeWindow.getInclusiveStart() < daysToHours(splitDay));
				Assertions.assertTrue(dischargeFeasibleTimeWindow.getInclusiveStart() >= daysToHours(splitDay));
			});
		});
	}

	/**
	 * Test: Simple price expression with two index. Vessel Availability lasting a
	 * month. Load and Discharge are unbounded during that period. Price in the
	 * first half negligible and really high in the second half. Expected result:
	 * Load during the first half and discharge during the second half
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSplitMonthExpressionIndex() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability = createTestVesselAvailability(LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		// Create cargo 1, cargo 2

		final int splitDay = 15;

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, String.format("SPLITMONTH(Henry_Hub, JCC, %d)", splitDay),
						23.4) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2018, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_DRAGON), null, entity,
						String.format("SPLITMONTH(Henry_Hub, JCC, %d)", splitDay)) //
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.withVesselAssignment(vesselAvailability, 1).build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		pricingModelBuilder.makeCommodityDataCurve("Henry_Hub", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2018, 5), 5) //
				.addIndexPoint(YearMonth.of(2018, 6), 5) //
				.addIndexPoint(YearMonth.of(2018, 7), 5) //
				.build();

		pricingModelBuilder.makeCommodityDataCurve("JCC", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2018, 5), 10) //
				.addIndexPoint(YearMonth.of(2018, 6), 10) //
				.addIndexPoint(YearMonth.of(2018, 7), 10) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			MicroCaseUtils.withEvaluationInjectorPerChainScope(scenarioToOptimiserBridge, injector -> {
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				final TimeWindowScheduler priceBasedSequenceScheduler = injector.getInstance(TimeWindowScheduler.class);
				// Ensure scheduling with price window trimming is enabled.
				priceBasedSequenceScheduler.setUsePriceBasedWindowTrimming(true);

				final ScheduledTimeWindows scheduledTimeWindows = priceBasedSequenceScheduler.calculateTrimmedWindows(initialSequences);

				// get optimiser objects
				final IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, scheduledTimeWindows);
				final IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, scheduledTimeWindows);
				final ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
				final IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

				// make sure no objects are null
				assert load != null;
				assert discharge != null;
				assert loadPortTimeWindowsRecord != null;
				assert dischargePortTimeWindowsRecord != null;

				// get optimised time windows
				final ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
				final ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

				// tests
				Assertions.assertEquals(5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFLoadSlot()), 0.0001);
				Assertions.assertEquals(10, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
				Assertions.assertTrue(loadFeasibleTimeWindow.getInclusiveStart() < daysToHours(splitDay));
				Assertions.assertTrue(dischargeFeasibleTimeWindow.getInclusiveStart() >= daysToHours(splitDay));
			});
		});
	}

	public IDischargeSlot getDefaultOptimiserDischargeSlot(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		final IDischargeSlot discharge = MicroCaseUtils.getOptimiserObjectFromEMF(scenarioToOptimiserBridge, scenarioModelFinder.getCargoModelFinder().findDischargeSlot(dischargeName),
				IDischargeSlot.class);
		return discharge;
	}

	public ILoadSlot getDefaultOptimiserLoadSlot(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		final ILoadSlot load = MicroCaseUtils.getOptimiserObjectFromEMF(scenarioToOptimiserBridge, scenarioModelFinder.getCargoModelFinder().findLoadSlot(loadName), ILoadSlot.class);
		return load;
	}

	public @NonNull DischargeSlot getDefaultEMFDischargeSlot() {
		return scenarioModelFinder.getCargoModelFinder().findDischargeSlot(dischargeName);
	}

	public @NonNull LoadSlot getDefaultEMFLoadSlot() {
		return scenarioModelFinder.getCargoModelFinder().findLoadSlot(loadName);
	}

	/**
	 * Test: Simple price expression with two index. Vessel Availability lasting a
	 * month. Load and Discharge are unbounded during that period. Price in the
	 * first half negligible and really high in the second half. Expected result:
	 * Load during the first half and discharge during the second half
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNestedSplitMonth() throws Exception {
		portModelBuilder.setAllExistingPortsToUTC();
		// Create the required basic elements
		final VesselAvailability vesselAvailability = createTestVesselAvailability(LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 6, 1, 0, 0, 0),
				LocalDateTime.of(2018, 11, 1, 0, 0, 0));
		vesselAvailability.getStartHeel().setCvValue(22.3);
		vesselAvailability.getStartHeel().setPriceExpression("0");
		vesselAvailability.getStartHeel().setMaxVolumeAvailable(100000);
		vesselAvailability.getEndHeel().setPriceExpression("0");
		vesselAvailability.getEndHeel().setMinimumEndHeel(300);
		vesselAvailability.getEndHeel().setMaximumEndHeel(300);
		vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);

		// Construct the cargo scenario
		// Create cargo 1, cargo 2
		final CommodityCurve[] wc = new CommodityCurve[4];
		wc[0] = pricingModelBuilder.makeCommodityDataCurve("WEEK_W1", "$", "mmBtu").build();
		wc[1] = pricingModelBuilder.makeCommodityDataCurve("WEEK_W2", "$", "mmBtu").build();
		wc[2] = pricingModelBuilder.makeCommodityDataCurve("WEEK_W3", "$", "mmBtu").build();
		wc[3] = pricingModelBuilder.makeCommodityDataCurve("WEEK_W4", "$", "mmBtu").build();

		final String expression = "SPLITMONTH(SPLITMONTH(WEEK_W1,WEEK_W2,8),SPLITMONTH(WEEK_W3,WEEK_W4,22),15)";

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5", 23.4) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2018, 8, 1), portFinder.findPortById(InternalDataConstants.PORT_DRAGON), null, entity, expression) //
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.withVesselAssignment(vesselAvailability, 1).build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		// Loop over each of the weeks in the month and make sure we pick the best price
		// as we shift the high price through the month.
		for (int i = 0; i < 4; ++i) {
			// reset expression
			for (int j = 0; j < 4; ++j) {
				wc[j].setExpression("10");
			}
			wc[i].setExpression("40");

			evaluateWithLSOTest(scenarioRunner -> {
			});

			// Should be the same as the updateScenario as we have only called
			// ScenarioRunner#init()
			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(loadName, schedule);
			final SlotAllocation sa = cargoAllocation.getSlotAllocations().get(1);
			Assertions.assertEquals(40.0, sa.getPrice());
			Assertions.assertEquals(LocalDate.of(2018, 8, 1 + 7 * i), sa.getSlotVisit().getStart().toLocalDate());
		}
	}

}