/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.pricebasedwindows;

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
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

@RunWith(value = ShiroRunner.class)
public class PriceBasedTimeWindowsInvertedTests extends AbstractMicroTestCase {

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

	/**
	 * Test: Both slots priced on load, choose higher price
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_higher_price_worthwhile_48hours() throws Exception {
	
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double salesPrice = 8.6;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 6, 30), portFinder.findPort("Bonny Nigeria"), null, entity, "JCC", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.withPricingEvent(PricingEvent.START_LOAD, null)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("Henry_Hub");
		assert hh != null;
		@NonNull
		CommodityIndex jcc = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("JCC");
		assert jcc != null;
		pricingModelBuilder.clearPointsOnCommodityIndex(hh);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 6), salesPrice);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		
		pricingModelBuilder.clearPointsOnCommodityIndex(jcc);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 6), 6.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 7), 5.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 8), 7);
		checkDischargePrice(6.0, salesPrice);
	}

	/**
	 * Test: Both slots priced on load, choose lower price
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_higher_price_48_hours_too_costly() throws Exception {
		
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double salesPrice = 8.55;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 6, 30), portFinder.findPort("Bonny Nigeria"), null, entity, "JCC", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.withPricingEvent(PricingEvent.START_LOAD, null)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("Henry_Hub");
		assert hh != null;
		@NonNull
		CommodityIndex jcc = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("JCC");
		assert jcc != null;
		pricingModelBuilder.clearPointsOnCommodityIndex(hh);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 6), salesPrice);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		
		pricingModelBuilder.clearPointsOnCommodityIndex(jcc);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 6), 6.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 7), 5.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 8), 7);
		checkDischargePrice(5.0, 7.5);
	}

	/**
	 * Test: Both slots priced on load, choose lower price
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_higher_price_800hrs_too_costly() throws Exception {
	
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double salesPrice = 8.6;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 6, 30), portFinder.findPort("Bonny Nigeria"), null, entity, "JCC", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(800, TimePeriod.HOURS)
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.withPricingEvent(PricingEvent.START_LOAD, null)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("Henry_Hub");
		assert hh != null;
		@NonNull
		CommodityIndex jcc = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("JCC");
		assert jcc != null;
		pricingModelBuilder.clearPointsOnCommodityIndex(hh);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 6), salesPrice);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		
		pricingModelBuilder.clearPointsOnCommodityIndex(jcc);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 6), 6.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 7), 5.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 8), 7);
		checkDischargePrice(5.0, 7.5);
	}

	/**
	 * Test: Both slots priced on load, choose higher price
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_higher_price_800hrs_worthwhile() throws Exception {
		
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double salesPrice = 8.7;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 6, 30), portFinder.findPort("Bonny Nigeria"), null, entity, "JCC", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(800, TimePeriod.HOURS)
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.withPricingEvent(PricingEvent.START_LOAD, null)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("Henry_Hub");
		assert hh != null;
		@NonNull
		CommodityIndex jcc = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("JCC");
		assert jcc != null;
		pricingModelBuilder.clearPointsOnCommodityIndex(hh);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 6), salesPrice);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), 7.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), 8.5);
		
		pricingModelBuilder.clearPointsOnCommodityIndex(jcc);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 6), 6.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 7), 5.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 8), 7);
		checkDischargePrice(6.0, salesPrice);
	}

	
	/**
	 * Test: Both slots priced on discharge, choose higher price
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_DL_higher_price_800hrs_worthwhile() throws Exception {
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double loadPrice = 6.999;
		double salesPrice = 7.5;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 6, 30), portFinder.findPort("Bonny Nigeria"), null, entity, "JCC", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS)
				.withPricingEvent(PricingEvent.START_DISCHARGE, null)
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("Henry_Hub");
		assert hh != null;
		@NonNull
		CommodityIndex jcc = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("JCC");
		assert jcc != null;
		
		pricingModelBuilder.clearPointsOnCommodityIndex(jcc);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 6), 6.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 7), 7.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 8), loadPrice);

		pricingModelBuilder.clearPointsOnCommodityIndex(hh);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 6), 6.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), salesPrice);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), salesPrice);
		checkDischargePrice(loadPrice, salesPrice);
	}

	/**
	 * Test: Both slots priced on discharge, choose lower price
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testTimeWindows_DL_higher_price_800hrs_too_costly() throws Exception {
		
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability(LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2016, 6, 30, 0, 0, 0), LocalDateTime.of(2018, 1, 1, 0, 0, 0));
		
		// Construct the cargo scenario
		double loadPrice = 6.9999;
		double salesPrice = 7.5;
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 6, 30), portFinder.findPort("Bonny Nigeria"), null, entity, "JCC", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS)
				.withPricingEvent(PricingEvent.START_DISCHARGE, null)
				.build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 7, 31), portFinder.findPort("Dragon LNG"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS)
				.build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));
		EList<CommodityIndex> commodityIndices = lngScenarioModel.getReferenceModel().getPricingModel().getCommodityIndices();
		CommodityIndex hh = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("Henry_Hub");
		assert hh != null;
		@NonNull
		CommodityIndex jcc = scenarioModelFinder.getPricingModelFinder().findCommodityCurve("JCC");
		assert jcc != null;
		
		pricingModelBuilder.clearPointsOnCommodityIndex(jcc);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 6), 6.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 7), 7.0);
		pricingModelBuilder.addDataToCommodityIndex(jcc, YearMonth.of(2016, 8), loadPrice);

		pricingModelBuilder.clearPointsOnCommodityIndex(hh);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 6), 6.5);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 7), salesPrice);
		pricingModelBuilder.addDataToCommodityIndex(hh, YearMonth.of(2016, 8), salesPrice);
		checkDischargePrice(7.0, salesPrice);
	}
	
	public void checkDischargePrice(double loadPrice, double salesPrice) {
		evaluateWithLSOTest(scenarioRunner -> {
	
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
	
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			@NonNull
			ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
	
			// get optimiser objects
			ILoadSlot load = getDefaultOptimiserLoadSlot(scenarioToOptimiserBridge);
			IDischargeSlot discharge = getDefaultOptimiserDischargeSlot(scenarioToOptimiserBridge);
	
			// make sure no objects are null
			assert load != null;
			assert discharge != null;
	
			Assert.assertEquals(loadPrice, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFLoadSlot()), 0.000001);
			Assert.assertEquals(salesPrice, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.000001);
		} ,
				/*
				 * Include correct time windows scheduler
				 */
				MicroCaseUtils.getInjectorServiceWithPriceBasedScheduler()
				);
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