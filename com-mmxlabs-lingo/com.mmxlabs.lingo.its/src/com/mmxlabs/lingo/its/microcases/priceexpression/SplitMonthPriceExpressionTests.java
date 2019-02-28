/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.priceexpression;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.After;
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
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseUtils;
import com.mmxlabs.lingo.its.tests.microcases.TimeWindowsTestsUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
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

@RunWith(value = ShiroRunner.class)
public class SplitMonthPriceExpressionTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

	private static String vesselName = "vessel";
	private static String loadName = "load";
	private static String dischargeName = "discharge";

	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	@Before
	public void constructor() throws Exception {

		scenarioDataProvider = importReferenceData();
		lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		portFinder = scenarioModelFinder.getPortModelFinder();

		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		entity = commercialModelFinder.findEntity("Shipping");

		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
	}

	@After
	public void destructor() {
		lngScenarioModel = null;
		scenarioModelFinder = null;
		scenarioModelBuilder = null;
		commercialModelFinder = null;
		fleetModelFinder = null;
		portFinder = null;
		cargoModelBuilder = null;
		fleetModelBuilder = null;
		spotMarketsModelBuilder = null;
		pricingModelBuilder = null;
		entity = null;
	}

	private int daysToHours(int days) {
		return (days - 1) * 24;
	}

	private VesselAvailability createTestVesselAvailability(final LocalDateTime startStart, final LocalDateTime startEnd, final LocalDateTime endStart) {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(startStart, startEnd) //
				.withEndWindow(endStart) //
				.build();
	}

	/**
	 * Test: Simple price expression with constants value. Vessel Availability lasting a month. Load and Discharge are unbounded during that period. Price in the first half negligible and really high
	 * in the second half. Expected result: Load during the first half and discharge during the second half
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSplitMonthExpressionSimpleConstant() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability = createTestVesselAvailability(LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		// Create cargo 1, cargo 2
		final int splitDay = 15;

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2018, 6, 1), portFinder.findPort("Bonny Nigeria"), null, entity, String.format("SPLITMONTH(1, 10, %d)", splitDay)) //
				.withWindowStartTime(0) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2018, 6, 2), portFinder.findPort("Dragon LNG"), null, entity, String.format("SPLITMONTH(1, 10, %d)", splitDay)) //
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.withVesselAssignment(vesselAvailability, 1).build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			MicroCaseUtils.withInjectorPerChainScope(scenarioToOptimiserBridge, () -> {

				@NonNull
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
				final TimeWindowScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromChildInjector(scenarioToOptimiserBridge, TimeWindowScheduler.class);
				// Ensure scheduling with price window trimming is enabled.
				priceBasedSequenceScheduler.setUsePriceBasedWindowTrimming(true);
				ScheduledTimeWindows scheduledTimeWindows = priceBasedSequenceScheduler.schedule(initialSequences);
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
				Assert.assertEquals(1, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFLoadSlot()), 0.0001);
				Assert.assertEquals(10, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
				Assert.assertTrue(loadFeasibleTimeWindow.getInclusiveStart() < daysToHours(splitDay));
				Assert.assertTrue(dischargeFeasibleTimeWindow.getInclusiveStart() > daysToHours(splitDay));
			});
		});
	}

	/**
	 * Test: Simple price expression with two index. Vessel Availability lasting a month. Load and Discharge are unbounded during that period. Price in the first half negligible and really high in the
	 * second half. Expected result: Load during the first half and discharge during the second half
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSplitMonthExpressionIndex() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability = createTestVesselAvailability(LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		// Create cargo 1, cargo 2

		final int splitDay = 15;

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2018, 6, 1), portFinder.findPort("Bonny Nigeria"), null, entity, String.format("SPLITMONTH(Henry_Hub, JCC, %d)", splitDay), 23.4) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2018, 6, 2), portFinder.findPort("Dragon LNG"), null, entity, String.format("SPLITMONTH(Henry_Hub, JCC, %d)", splitDay)) //
				.withWindowStartTime(0) //
				.withWindowSize(30, TimePeriod.DAYS).build() //
				.withVesselAssignment(vesselAvailability, 1).build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		// Create custom index prices
		final EList<CommodityCurve> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityCurves();
		CommodityCurve hh = null;
		CommodityCurve jcc = null;
		for (final CommodityCurve commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
			if (commodityIndex.getName().equals("JCC")) {
				jcc = commodityIndex;
			}
		}

		// HH prices
		assert hh != null;
		hh.getPoints().clear();

		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2018, 5), 5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2018, 6), 5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2018, 7), 5);

		// JCC prices
		assert jcc != null;
		jcc.getPoints().clear();

		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2018, 5), 10);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2018, 6), 10);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2018, 7), 10);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			MicroCaseUtils.withInjectorPerChainScope(scenarioToOptimiserBridge, () -> {

				@NonNull
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
				final TimeWindowScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromChildInjector(scenarioToOptimiserBridge, TimeWindowScheduler.class);
				// Ensure scheduling with price window trimming is enabled.
				priceBasedSequenceScheduler.setUsePriceBasedWindowTrimming(true);
				ScheduledTimeWindows scheduledTimeWindows = priceBasedSequenceScheduler.schedule(initialSequences);
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
				Assert.assertEquals(5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFLoadSlot()), 0.0001);
				Assert.assertEquals(10, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
				Assert.assertTrue(loadFeasibleTimeWindow.getInclusiveStart() < daysToHours(splitDay));
				Assert.assertTrue(dischargeFeasibleTimeWindow.getInclusiveStart() > daysToHours(splitDay));
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

}