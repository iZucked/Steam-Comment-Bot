/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.pricebasedwindows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

@ExtendWith(value = ShiroRunner.class)
public class PriceBasedIdleTimeWindowsTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

	private static String vesselName = "vessel";
	private static String loadName = "load";
	private static String dischargeName = "discharge";

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}
	
	@Override
	@NonNull
	public IScenarioDataProvider importReferenceData() throws Exception {
		return importReferenceData("/referencedata/idle-time-when-laden/");
	}
	
	@BeforeEach
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

		entity = commercialModelFinder.findEntity("Entity");

		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 3, 1));
	}

	@AfterEach
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
	
	private VesselAvailability createTestVesselAvailability_ForNBOLaden(final LocalDateTime startStart, final LocalDateTime startEnd, final LocalDateTime endStart) {
		final Vessel vessel = fleetModelFinder.findVessel("Medium ship");

		//final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "70000", 0);

		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("70000") //
				.withStartWindow(startStart, startEnd) //
				.withEndWindow(endStart) //
				.build();
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
	 * Create a case when we must not have an idle when Laden
	 * 
	 * @throws Exception
	 * */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testScheduleProviderCreatesIdleTime_whenLadenOnNBO() throws Exception {
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability_ForNBOLaden(LocalDateTime.of(2017, 1, 1, 0, 0, 0), LocalDateTime.of(2017, 1, 1, 0, 0, 0),
						LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
						.makeFOBPurchase(loadName, LocalDate.of(2017, 1, 7), portFinder.findPort("Bonny"), null, entity, "5.32", 23.47) //
						.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
						.withWindowStartTime(0) //
						.withWindowSize(1, TimePeriod.MONTHS).build() //
						.makeDESSale(dischargeName, LocalDate.of(2016, 2, 1), portFinder.findPort("Dragon"), null, entity, "7.46") //
						.withWindowStartTime(0) //
						.withWindowSize(1, TimePeriod.MONTHS).build() //
						.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 3, 1));
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			Idle idle = null;
			// check that we have cargo allocations
			Assertions.assertTrue(scenarioRunner.getSchedule().getCargoAllocations().size() > 0);
			
			for (final CargoAllocation cargoA : scenarioRunner.getSchedule().getCargoAllocations()) {
				SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoA);
				idle = simpleCargoAllocation.getLadenIdle();
				// check that idle time is 0ß
				Assertions.assertEquals(0, idle.getDuration());
			}

		});
				
	}
	
	/**
	 * Create a case with high CV = 30.00
	 * 
	 * @throws Exception
	 * */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testScheduleProviderCreatesIdleTime_whenLadenOnNBO_HighPortCV() throws Exception {
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability_ForNBOLaden(LocalDateTime.of(2017, 1, 1, 0, 0, 0), LocalDateTime.of(2017, 1, 1, 0, 0, 0),
						LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
						.makeFOBPurchase(loadName, LocalDate.of(2017, 1, 7), portFinder.findPort("Bonny"), null, entity, "5.32", 30.00) //
						.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
						.withWindowStartTime(0) //
						.withWindowSize(1, TimePeriod.MONTHS).build() //
						.makeDESSale(dischargeName, LocalDate.of(2016, 2, 1), portFinder.findPort("Dragon"), null, entity, "7.46") //
						.withWindowStartTime(0) //
						.withWindowSize(1, TimePeriod.MONTHS).build() //
						.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 3, 1));
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			Idle idle = null;
			// check that we have cargo allocations
			Assertions.assertTrue(scenarioRunner.getSchedule().getCargoAllocations().size() > 0);
			
			for (final CargoAllocation cargoA : scenarioRunner.getSchedule().getCargoAllocations()) {
				SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoA);
				idle = simpleCargoAllocation.getLadenIdle();
				// check that existing idle is 0
				Assertions.assertEquals(0, idle.getDuration());
			}

		});
				
	}

}
