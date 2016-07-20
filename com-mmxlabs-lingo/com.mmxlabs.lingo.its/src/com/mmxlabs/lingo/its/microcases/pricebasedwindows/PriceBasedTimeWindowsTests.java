/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.pricebasedwindows;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseDateUtils;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.IntervalData;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.LadenRouteData;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowsTrimming;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@RunWith(value = ShiroRunner.class)
public class PriceBasedTimeWindowsTests extends AbstractMicroTestCase {

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
	public void constructor() throws MalformedURLException {

		lngScenarioModel = importReferenceData();

		scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

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

	private VesselAvailability createTestVesselAvailability(LocalDateTime startStart, LocalDateTime startEnd, LocalDateTime endStart) {
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVessel("vesselName", vesselClass);

		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(startStart, startEnd) //
				.withEndWindow(endStart) //
				.build();
	}

	/**
	 * Test: Make sure costs are correct
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testLowLevelCosts() throws Exception {
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		// Construct the cargo scenario

		// Create cargo 1
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "5", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 1), portFinder.findPort("Incheon"), null, entity, "10") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS).build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			PriceIntervalProviderHelper priceIntervalProviderHelper = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, PriceIntervalProviderHelper.class);
			/*
			 * Load data and set up model
			 */
			IntervalData purchase = new IntervalData(0, 10, OptimiserUnitConvertor.convertToInternalPrice(5));
			IntervalData sales = new IntervalData(50, 70, OptimiserUnitConvertor.convertToInternalPrice(10));
			LadenRouteData[] lrd = new LadenRouteData[2];
			lrd[0] = new LadenRouteData(52, 71, 0, 1000);
			lrd[1] = new LadenRouteData(26, 35, OptimiserUnitConvertor.convertToInternalDailyCost(500000), 500);
			IVesselAvailability o_vesselAvailability = TimeWindowsTestsUtils.getIVesselAvailabilityWithName(vesselName, optimiserScenario.getCargoModel().getVesselAvailabilities(),
					scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap());
			@NonNull
			IVesselClass o_vesselClass = o_vesselAvailability.getVessel().getVesselClass();
			/*
			 * Constants
			 */
			final int equivalenceFactor = 45_600_000;
			final long cv = 23_4_00000;
			int salesPrice = OptimiserUnitConvertor.convertToInternalPrice(10);
			int loadDuration = 0;
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			assert load != null;
			long directCost = priceIntervalProviderHelper.getTotalEstimatedCostForRoute(purchase, sales, salesPrice, loadDuration, o_vesselClass.getNBORate(VesselState.Laden), o_vesselClass,
					load.getCargoCVValue(), equivalenceFactor, lrd[0]);
			long canalCost = priceIntervalProviderHelper.getTotalEstimatedCostForRoute(purchase, sales, salesPrice, loadDuration, o_vesselClass.getNBORate(VesselState.Laden), o_vesselClass,
					load.getCargoCVValue(), equivalenceFactor, lrd[1]);
			/*
			 * Test correct values are being calculated
			 */
			Assert.assertEquals(directCost, 15_674_022_0000L);
			Assert.assertEquals(canalCost, 7_752_039_0000L);
			/*
			 * Test correct route chosen
			 */
			NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCost = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchase, sales, loadDuration, salesPrice, lrd,
					o_vesselClass.getNBORate(VesselState.Laden), o_vesselClass, load.getCargoCVValue());
			Assert.assertEquals(totalEstimatedJourneyCost.getFirst(), lrd[1]);
			TimeWindowsTrimming timeWindowsTrimming = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, TimeWindowsTrimming.class);
			int[] findBestBucketPairWithRouteAndBoiloffConsiderations = timeWindowsTrimming.findBestBucketPairWithRouteAndBoiloffConsiderations(o_vesselAvailability.getVessel(), load, lrd,
					loadDuration, new IntervalData[] { purchase }, new IntervalData[] { sales }, new IntervalData[] { sales });
			Assert.assertArrayEquals(findBestBucketPairWithRouteAndBoiloffConsiderations, new int[] { 10, 11, 50, 51 });
		});
	}

	/**
	 * Test: Expected time windows = [5, 1216]
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSimpleWindowsCase() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));

		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 8, 21), portFinder.findPort("Incheon"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			// Tests
			Assert.assertEquals(loadFeasibleTimeWindow.getInclusiveStart(), 5);
			Assert.assertEquals(dischargeFeasibleTimeWindow.getInclusiveStart(), 1216);
			Assert.assertEquals(8.5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
		});
	}

	/**
	 * Test: Expected time windows = [5, 1216], higher sales price not chosen
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSimpleWindowsCase2() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));

		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 8, 21), portFinder.findPort("Incheon"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(24, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 9), 13.5);
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			// tests
			Assert.assertEquals(loadFeasibleTimeWindow.getInclusiveStart(), 5);
			Assert.assertEquals(dischargeFeasibleTimeWindow.getInclusiveStart(), 1216);
			Assert.assertEquals(8.5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
		});
	}

	/**
	 * Test: Expected time windows = [5, 1464], higher sales price not chosen
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_23hours_lower_price() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));

		// Construct the cargo scenario
		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 8, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 9), 13.5);
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			// tests
			Assert.assertEquals(loadFeasibleTimeWindow.getInclusiveStart(), 5);
			Assert.assertEquals(dischargeFeasibleTimeWindow.getInclusiveStart(), 1464);
			Assert.assertEquals(8.5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
		});
	}

	/**
	 * Test: Expected time windows = [5, 1488], higher sales price chosen
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_24hours_higher_price() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 8, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(24, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 9), 13.5);
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromChildInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			Assert.assertEquals(loadFeasibleTimeWindow.getInclusiveStart(), 5);
			Assert.assertEquals(dischargeFeasibleTimeWindow.getInclusiveStart(), 1488);

			Assert.assertEquals(13.5, ScheduleTools.getPrice(optimiserScenario, cargo1.getSortedSlots().get(1)), 0.0001);
			Assert.assertEquals(
					MicroCaseDateUtils.getDateTimeFromHour(scenarioToOptimiserBridge, dischargeFeasibleTimeWindow.getInclusiveStart(), getDefaultEMFDischargeSlot().getPort().getTimeZone()),
					MicroCaseDateUtils.getZonedDateTime(2016, 9, 01, 0, getDefaultEMFDischargeSlot().getPort()));
		} ,
				/*
				 * Include correct time windows scheduler
				 */
				MicroCaseUtils.getInjectorServiceWithPriceBasedScheduler());
	}

	/**
	 * Test: higher sales price chosen (8.87)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_higher_price_worthwhile() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double salesPrice = 8.87;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(24000, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 9), salesPrice);
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromChildInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			Assert.assertEquals(salesPrice, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.000001);
			Assert.assertEquals(
					MicroCaseDateUtils.getDateTimeFromHour(scenarioToOptimiserBridge, dischargeFeasibleTimeWindow.getInclusiveStart(), getDefaultEMFDischargeSlot().getPort().getTimeZone()),
					MicroCaseDateUtils.getZonedDateTime(2016, 9, 1, 0, getDefaultEMFDischargeSlot().getPort()));
		} ,
				/*
				 * Include correct time windows scheduler
				 */
				MicroCaseUtils.getInjectorServiceWithPriceBasedScheduler());
	}

	/**
	 * Test: Lower sales price chosen (8.5)
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_higher_price_too_costly() throws Exception {

		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double salesPrice = 8.86;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(24000, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 9), salesPrice);
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromChildInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			Assert.assertEquals(8.5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.000001);
			Assert.assertEquals(
					MicroCaseDateUtils.getDateTimeFromHour(scenarioToOptimiserBridge, dischargeFeasibleTimeWindow.getInclusiveStart(), getDefaultEMFDischargeSlot().getPort().getTimeZone()),
					MicroCaseDateUtils.getZonedDateTime(2016, 8, 1, 0, getDefaultEMFDischargeSlot().getPort()));
		} ,
				/*
				 * Include correct time windows scheduler
				 */
				MicroCaseUtils.getInjectorServiceWithPriceBasedScheduler());
	}

	/**
	 * Test: The exact time window specified is chosen - [(2016, 7, 1), (2016, 8, 21)]
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testExactTimeWindows() throws Exception {
	
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2016, 6, 30, 23, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
	
		// Construct the cargo scenario
		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 8, 21), portFinder.findPort("Incheon"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
	
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = null;
		for (CommodityIndex commodityIndex : commodityIndices) {
			if (commodityIndex.getName().equals("Henry_Hub")) {
				hh = commodityIndex;
			}
		}
		assert hh != null;
		DataIndex<Double> hh_data = (DataIndex<Double>) hh.getData();
		hh_data.getPoints().clear();
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		evaluateWithLSOTest(scenarioRunner -> {
	
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, PriceBasedSequenceScheduler.class);
			priceBasedSequenceScheduler.schedule(initialSequences);

			// get optimiser objects
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = TimeWindowsTestsUtils.getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);

			// make sure no objects are null
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;

			// get optimised time windows
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);

			Assert.assertEquals(MicroCaseDateUtils.getDateTimeFromHour(scenarioToOptimiserBridge, loadFeasibleTimeWindow.getInclusiveStart(), getDefaultEMFLoadSlot().getPort().getTimeZone()),
					MicroCaseDateUtils.getZonedDateTime(2016, 07, 01, 0, getDefaultEMFLoadSlot().getPort()));
			Assert.assertEquals(MicroCaseDateUtils.getDateTimeFromHour(scenarioToOptimiserBridge, dischargeFeasibleTimeWindow.getInclusiveStart(), getDefaultEMFDischargeSlot().getPort().getTimeZone()),
					MicroCaseDateUtils.getZonedDateTime(2016, 8, 21, 0, getDefaultEMFDischargeSlot().getPort()));
			Assert.assertEquals(8.5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
		});
	}

	public IDischargeSlot getDefaultOptimiserDischargeSlot(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		IDischargeSlot discharge = MicroCaseUtils.getOptimiserObjectFromEMF(scenarioToOptimiserBridge, scenarioModelFinder.getCargoModelFinder().findDischargeSlot(dischargeName),
				IDischargeSlot.class);
		return discharge;
	}

	public ILoadSlot getDefaultOptimiserLoadSlot(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		ILoadSlot load = MicroCaseUtils.getOptimiserObjectFromEMF(scenarioToOptimiserBridge, scenarioModelFinder.getCargoModelFinder().findLoadSlot(loadName), ILoadSlot.class);
		return load;
	}

	public @NonNull DischargeSlot getDefaultEMFDischargeSlot() {
		return scenarioModelFinder.getCargoModelFinder().findDischargeSlot(dischargeName);
	}

	public @NonNull LoadSlot getDefaultEMFLoadSlot() {
		return scenarioModelFinder.getCargoModelFinder().findLoadSlot(loadName);
	}

}