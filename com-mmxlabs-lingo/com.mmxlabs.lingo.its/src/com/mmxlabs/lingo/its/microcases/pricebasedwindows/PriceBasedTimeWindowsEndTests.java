/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@RunWith(value = ShiroRunner.class)
public class PriceBasedTimeWindowsEndTests extends AbstractMicroTestCase {

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

		final Vessel vessel = fleetModelBuilder.createVessel("vesselName", vesselClass);

		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(startStart, startEnd) //
				// .withEndWindow(endStart) //
				.withEndWindow(null, null).build();
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
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final CharterInMarket charterInMarket = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "20000", 1);
		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2016, 7, 1), portFinder.findPort("Bonny Nigeria"), null, entity, "Henry_Hub", 23.4) //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 8, 21), portFinder.findPort("Incheon"), null, entity, "Henry_Hub") //
				.withWindowStartTime(0) //
				.withWindowSize(5, TimePeriod.HOURS).build() //
				.withVesselAssignment(charterInMarket, 0, 0).build();
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
			IModifiableSequences initialSequences = new ModifiableSequences(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences());
			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			// ISequencesManipulator sequencesManipulator = MicroCaseUtils.getClassFromInjector(scenarioToOptimiserBridge, ISequencesManipulator.class);
			sequencesManipulator.manipulate(initialSequences);
			MicroCaseUtils.withInjectorPerChainScope(scenarioToOptimiserBridge, () -> {
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

				// Tests
				Assert.assertEquals(loadFeasibleTimeWindow.getInclusiveStart(), 5);
				Assert.assertEquals(dischargeFeasibleTimeWindow.getInclusiveStart(), 1216);
				Assert.assertEquals(8.5, ScheduleTools.getPrice(optimiserScenario, getDefaultEMFDischargeSlot()), 0.0001);
//				try {
//					MicroCaseUtils.storeToFile(optimiserScenario, "alex_test");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			});
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