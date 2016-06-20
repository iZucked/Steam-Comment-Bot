/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Calendar;
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
import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.period.AbstractPeriodTestCase;
import com.mmxlabs.models.datetime.importers.YearMonthAttributeImporter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.IntervalData;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.LadenRouteData;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowsTrimming;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@RunWith(value = ShiroRunner.class)
public class TimeWindowsLowerLevelTests extends AbstractPeriodTestCase {

	private LNGScenarioModel lngScenarioModel;
	private ScenarioModelFinder scenarioModelFinder;
	private ScenarioModelBuilder scenarioModelBuilder;
	private CommercialModelFinder commercialModelFinder;
	private FleetModelFinder fleetModelFinder;
	private PortModelFinder portFinder;
	private CargoModelBuilder cargoModelBuilder;
	private FleetModelBuilder fleetModelBuilder;
	private SpotMarketsModelBuilder spotMarketsModelBuilder;
	private PricingModelBuilder pricingModelBuilder;
	private BaseLegalEntity entity;

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

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

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindowsCase1() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2018, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "5", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(0)
				.build() //
				.makeDESSale("D", LocalDate.of(2016, 7, 1), portFinder.findPort("Incheon"), null, entity, "10") //
				.withWindowStartTime(0) //
				.withWindowSize(0)
				.build() //
				.build();

		runTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			PriceIntervalProviderHelper instance = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceIntervalProviderHelper.class);
			/*
			 * Load data and set up model
			 */
			IntervalData purchase = new IntervalData(0, 10, OptimiserUnitConvertor.convertToInternalPrice(5));
			IntervalData sales = new IntervalData(50, 70, OptimiserUnitConvertor.convertToInternalPrice(10));
			LadenRouteData[] lrd = new LadenRouteData[2];
			lrd[0] = new LadenRouteData(52, 71, 0, 1000);
			lrd[1] = new LadenRouteData(26, 35, OptimiserUnitConvertor.convertToInternalDailyCost(500000), 500);
			IVesselAvailability o_vesselAvailability = getIVesselAvailabilityWithName("vessel", optimiserScenario.getCargoModel().getVesselAvailabilities(), scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap());
			@NonNull IVesselClass o_vesselClass = o_vesselAvailability.getVessel().getVesselClass();
			/*
			 * Constants
			 */
			final int equivalenceFactor = 45_600_000;
			final long cv = 23_4_00000;
			int salesPrice = OptimiserUnitConvertor.convertToInternalPrice(10);
			int loadDuration = 0;
			/*
			 * Run code with loaded model
			 */
			ILoadSlot load = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(0), ILoadSlot.class);
			assert load != null;
			long directCost = instance.getTotalEstimatedCostForRoute(purchase, sales, loadDuration, o_vesselClass.getNBORate(VesselState.Laden), o_vesselClass, load.getCargoCVValue(), equivalenceFactor, lrd[0]);
			long canalCost = instance.getTotalEstimatedCostForRoute(purchase, sales, loadDuration, o_vesselClass.getNBORate(VesselState.Laden), o_vesselClass, load.getCargoCVValue(), equivalenceFactor, lrd[1]);
			/*
			 * Test correct values are being calculated
			 */
			Assert.assertEquals(directCost, 15_674_022_0000L);
			Assert.assertEquals(canalCost, 6_874_539_0000L);
			/*
			 * Test correct route chosen
			 */
			NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCost = instance.getTotalEstimatedJourneyCost(purchase, sales, loadDuration, salesPrice, lrd, o_vesselClass.getNBORate(VesselState.Laden), o_vesselClass, load.getCargoCVValue());
			Assert.assertEquals(totalEstimatedJourneyCost.getFirst(), lrd[1]);
			TimeWindowsTrimming timeWindowsTrimming = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(TimeWindowsTrimming.class);
			int[] findBestBucketPairWithRouteAndBoiloffConsiderations = timeWindowsTrimming.findBestBucketPairWithRouteAndBoiloffConsiderations(o_vesselAvailability.getVessel(), load, lrd, loadDuration, new IntervalData[] {purchase}, new IntervalData[] {sales});
			Assert.assertArrayEquals(findBestBucketPairWithRouteAndBoiloffConsiderations, new int[] {10, 10, 50, 50});
		});
	}
	
	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindowsCase2() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2016, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 7, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2018, 7, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario
		String loadName = "Load";
		String dischargeName = "Discharge";
		String vesselName = "vessel";
		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5)
				.build() //
				.makeDESSale("Discharge", LocalDate.of(2016, 8, 21), portFinder.findPort("Incheon"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(5)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1)
				.build();
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
		addDataToCommodity(hh, YearMonth.of(2016, 7), 7.5);
		addDataToCommodity(hh, YearMonth.of(2016, 8), 8.5);
		runTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			PriceIntervalProviderHelper instance = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceIntervalProviderHelper.class);
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceBasedSequenceScheduler.class);
			TimeWindowsTrimming timeWindowsTrimming = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(TimeWindowsTrimming.class);
			priceBasedSequenceScheduler.schedule(initialSequences);
			IResource o_resource = null;
			for (IResource resource : initialSequences.getResources()) {
				if (resource.getName().contains(vesselName)) {
					o_resource = resource;
				}
			}
			assert o_resource != null;
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(0), ILoadSlot.class);
			IDischargeSlot discharge = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(1), IDischargeSlot.class);
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
			Assert.assertEquals(loadFeasibleTimeWindow.getStart(),723);
			Assert.assertEquals(dischargeFeasibleTimeWindow.getStart(),1935);
//			List<int[]> loadPriceIntervalsIndependentOfDischarge = timeWindowsTrimming.getLoadPriceIntervalsIndependentOfDischarge(loadPortTimeWindowsRecord, load);
		});
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindowsCase3() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2016, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 7, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2018, 7, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario
		String loadName = "Load";
		String dischargeName = "Discharge";
		String vesselName = "vessel";
		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5)
				.build() //
				.makeDESSale("Discharge", LocalDate.of(2016, 8, 21), portFinder.findPort("Incheon"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(2000)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1)
				.build();
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
		addDataToCommodity(hh, YearMonth.of(2016, 7), 7.5);
		addDataToCommodity(hh, YearMonth.of(2016, 8), 8.5);
		addDataToCommodity(hh, YearMonth.of(2016, 9), 13.5);
		runTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			PriceIntervalProviderHelper instance = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceIntervalProviderHelper.class);
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceBasedSequenceScheduler.class);
			TimeWindowsTrimming timeWindowsTrimming = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(TimeWindowsTrimming.class);
			priceBasedSequenceScheduler.schedule(initialSequences);
			IResource o_resource = null;
			for (IResource resource : initialSequences.getResources()) {
				if (resource.getName().contains(vesselName)) {
					o_resource = resource;
				}
			}
			assert o_resource != null;
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(0), ILoadSlot.class);
			IDischargeSlot discharge = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(1), IDischargeSlot.class);
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
			Assert.assertEquals(loadFeasibleTimeWindow.getStart(),723);
			Assert.assertEquals(dischargeFeasibleTimeWindow.getStart(),2199);
		});
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindowsCase4() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2016, 6, 1, 0, 0, 0), LocalDateTime.of(2018, 7, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2018, 7, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario
		String loadName = "Load";
		String dischargeName = "Discharge";
		String vesselName = "vessel";
		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5)
				.build() //
				.makeDESSale("Discharge", LocalDate.of(2016, 8, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(24)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1)
				.build();
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
		addDataToCommodity(hh, YearMonth.of(2016, 7), 7.5);
		addDataToCommodity(hh, YearMonth.of(2016, 8), 8.5);
		addDataToCommodity(hh, YearMonth.of(2016, 9), 13.5);
		runTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			PriceIntervalProviderHelper instance = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceIntervalProviderHelper.class);
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			PriceBasedSequenceScheduler priceBasedSequenceScheduler = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(PriceBasedSequenceScheduler.class);
			TimeWindowsTrimming timeWindowsTrimming = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(TimeWindowsTrimming.class);
			priceBasedSequenceScheduler.schedule(initialSequences);
			IResource o_resource = null;
			for (IResource resource : initialSequences.getResources()) {
				if (resource.getName().contains(vesselName)) {
					o_resource = resource;
				}
			}
			assert o_resource != null;
			IPortTimeWindowsRecord loadPortTimeWindowsRecord = getIPortTimeWindowsRecord(loadName, priceBasedSequenceScheduler);
			IPortTimeWindowsRecord dischargePortTimeWindowsRecord = getIPortTimeWindowsRecord(dischargeName, priceBasedSequenceScheduler);
			ILoadSlot load = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(0), ILoadSlot.class);
			IDischargeSlot discharge = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getOptimiserObject(cargo1.getSortedSlots().get(1), IDischargeSlot.class);
			assert load != null;
			assert discharge != null;
			assert loadPortTimeWindowsRecord != null;
			assert dischargePortTimeWindowsRecord != null;
			ITimeWindow loadFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
			ITimeWindow dischargeFeasibleTimeWindow = loadPortTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
//			Assert.assertEquals(loadFeasibleTimeWindow.getStart(),723);
//			Assert.assertEquals(dischargeFeasibleTimeWindow.getStart(),2199);
			System.out.println("date:"+scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap().getDateFromHours(dischargeFeasibleTimeWindow.getStart(), "UTC"));
		});
	}

	public IPortTimeWindowsRecord getIPortTimeWindowsRecord(String loadName, PriceBasedSequenceScheduler priceBasedSequenceScheduler) {
		IPortTimeWindowsRecord interestingPortTimeWindowsRecord = null;
					List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords = priceBasedSequenceScheduler.getPortTimeWindowsRecords();
					for (List<IPortTimeWindowsRecord> upperList : portTimeWindowsRecords) {
						for (IPortTimeWindowsRecord portTimeWindowsRecord : upperList) {
							for (IPortSlot slot : portTimeWindowsRecord.getSlots()) {
								if (slot.getId().contains(loadName)) {
									interestingPortTimeWindowsRecord = portTimeWindowsRecord;
								}
							}
						}
					}
		return interestingPortTimeWindowsRecord;
	}

	public void addDataToCommodity(final CommodityIndex ci, final YearMonth date, final double value) {
		final DataIndex<Double> di = (DataIndex<Double>) ci.getData();
		final List<IndexPoint<Double>> points = di.getPoints();
		final IndexPoint<Double> ip = PricingFactory.eINSTANCE.createIndexPoint();
		ip.setDate(date);
		ip.setValue(value);
		points.add(ip);
	}

	private IVesselAvailability getIVesselAvailabilityWithName(String name, EList<VesselAvailability> namedObjects, ModelEntityMap mem) {
		for (VesselAvailability object : namedObjects) {
			if (object.getVessel().getName().contains(name))
				return mem.getOptimiserObject(object, IVesselAvailability.class);
		}
		return null;
	}

	private void runTest(@NonNull final Consumer<LNGScenarioRunner> checker) {
		runTest(true, null, null, checker);
	}

	private void runTest(final boolean optimise, @Nullable final Consumer<OptimiserSettings> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {
		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (tweaker != null) {
			tweaker.accept(optimiserSettings);
		} else {
			optimiserSettings.getAnnealingSettings().setIterations(10_000);
		}

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);

			if (runnerHookFactory != null) {
				final IRunnerHook runnerHook = runnerHookFactory.apply(scenarioRunner);
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
			}

			scenarioRunner.evaluateInitialState();
			if (optimise) {
				scenarioRunner.run();
			}

			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}
}