/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

/**
 * Test cases to ensure time windows are created correctly.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class TimeWindowCreationTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testVesselEvent_Exact() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final DryDockEvent event = cargoModelBuilder.makeDryDockEvent("drydock", LocalDateTime.of(2015, 12, 11, 0, 0, 0), LocalDateTime.of(2015, 12, 11, 0, 0, 0), dischargePort) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVesselEventPortSlot eventSlot = modelEntityMap.getOptimiserObjectNullChecked(event, IVesselEventPortSlot.class);

			Assert.assertEquals(0, eventSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, eventSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(0 + 1, eventSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testVesselEvent_OneDay() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final DryDockEvent event = cargoModelBuilder.makeDryDockEvent("drydock", LocalDateTime.of(2015, 12, 11, 0, 0, 0), LocalDateTime.of(2015, 12, 11, 23, 0, 0), dischargePort) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVesselEventPortSlot eventSlot = modelEntityMap.getOptimiserObjectNullChecked(event, IVesselEventPortSlot.class);

			Assert.assertEquals(0, eventSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, eventSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(23 + 1, eventSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testVesselEvent_OneMonth() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final DryDockEvent event = cargoModelBuilder.makeDryDockEvent("drydock", LocalDateTime.of(2015, 11, 1, 0, 0, 0), LocalDateTime.of(2015, 11, 30, 23, 0, 0), dischargePort) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			// Validate time
			final DateAndCurveHelper dateAndCurveHelper = dataTransformer.getInjector().getInstance(DateAndCurveHelper.class);
			Assert.assertEquals(0 + 30 * 24, dateAndCurveHelper.convertTime(event.getStartByAsDateTime()));

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVesselEventPortSlot eventSlot = modelEntityMap.getOptimiserObjectNullChecked(event, IVesselEventPortSlot.class);

			Assert.assertEquals(0, eventSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, eventSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(30 * 24 + 1, eventSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_Exact() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// map into same timezone to make expectations easier
		portFinder.findPort("Point Fortin").setTimeZone("UTC");
		portFinder.findPort("Dominion Cove Point LNG").setTimeZone("UTC");

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1)//
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
			final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);

			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(0 + 1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final IPortSlot o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			Assert.assertEquals(6 * 24, o_dischargeSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_dischargeSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(6 * 24 + 1, o_dischargeSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	/**
	 * Does the optimiser created spot slot have the correct window?
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCargo_SpotCreated() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// map into same timezone to make expectations easier
		portFinder.findPort("Point Fortin").setTimeZone("UTC");
		portFinder.findPort("Dominion Cove Point LNG").setTimeZone("UTC");

		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8); //
		loadSlot.setWindowStartTime(0);
		loadSlot.setWindowSize(0);

		spotMarketsModelBuilder.makeDESSaleMarket("desmarket", portFinder.findPort("Dominion Cove Point LNG"), entity, "7") //
				.withAvailabilityConstant(1) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2015, 11, 1));

		evaluateWithLSOTest(scenarioRunner -> {

			// Run optimisation to generate the cargo
			scenarioRunner.runAndApplyBest();

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			final Cargo cargo = lngScenarioModel.getCargoModel().getCargoes().get(0);

			Assert.assertSame(loadSlot, cargo.getSlots().get(0));
			final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

			Assert.assertEquals(LocalDate.of(2015, 12, 1), dischargeSlot.getWindowStart());
			Assert.assertEquals(0, dischargeSlot.getWindowStartTime());
			Assert.assertEquals(31 * 24 - 1, dischargeSlot.getWindowSizeInHours());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_OneDay() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// map into same timezone to make expectations easier
		portFinder.findPort("Point Fortin").setTimeZone("UTC");
		portFinder.findPort("Dominion Cove Point LNG").setTimeZone("UTC");

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1)//
				.withAssignmentFlags(false, false) //
				.build();

		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

		// Check expected window end date
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 5, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 11, 23, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getWindowEndWithSlotOrPortTime());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);

			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(23 + 1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final IPortSlot o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			Assert.assertEquals(6 * 24, o_dischargeSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_dischargeSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(6 * 24 + 23 + 1, o_dischargeSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_OneDay_PlusFlex() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// map into same timezone to make expectations easier
		portFinder.findPort("Point Fortin").setTimeZone("UTC");
		portFinder.findPort("Dominion Cove Point LNG").setTimeZone("UTC");

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.withWindowFlex(12, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.withWindowFlex(18, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1)//
				.withAssignmentFlags(false, false) //
				.build();

		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

		// Check expected window end date
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 5, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 11, 23, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getWindowEndWithSlotOrPortTime());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(12, o_loadSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(23 + 12 + 1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final IPortSlot o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			Assert.assertEquals(6 * 24, o_dischargeSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(18, o_dischargeSlot.getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(6 * 24 + +18 + 23 + 1, o_dischargeSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testVesselAvailability_Specified() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 23, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 2, 1, 0, 0, 0), LocalDateTime.of(2016, 2, 1, 23, 0, 0)) //
				.build();

		// map into same timezone to make expectations easier
		portFinder.findPort("Point Fortin").setTimeZone("UTC");
		portFinder.findPort("Dominion Cove Point LNG").setTimeZone("UTC");

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

			Assert.assertEquals(0, o_vesselAvailability.getStartRequirement().getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_vesselAvailability.getStartRequirement().getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(0 + 23 + 1, o_vesselAvailability.getStartRequirement().getTimeWindow().getExclusiveEnd());

			Assert.assertEquals(31 * 24, o_vesselAvailability.getEndRequirement().getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_vesselAvailability.getEndRequirement().getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(31 * 24 + 23 + 1, o_vesselAvailability.getEndRequirement().getTimeWindow().getExclusiveEnd());
		});
	}

	/**
	 * Simple test checking the window size and end dates are as expected
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCargo_SlotWindowEnd() throws Exception {
		portFinder.findPort("Point Fortin").setTimeZone("UTC");
		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8); //
		loadSlot.setWindowStartTime(0);

		loadSlot.setWindowSize(0);
		Assert.assertEquals(0, loadSlot.getWindowSizeInHours());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());

		loadSlot.setWindowSize(23);
		loadSlot.setWindowSizeUnits(TimePeriod.HOURS);
		Assert.assertEquals(23, loadSlot.getWindowSizeInHours());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 1, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());

		loadSlot.setWindowSize(1);
		loadSlot.setWindowSizeUnits(TimePeriod.DAYS);
		Assert.assertEquals(23, loadSlot.getWindowSizeInHours());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 1, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());

		loadSlot.setWindowSize(2);
		loadSlot.setWindowSizeUnits(TimePeriod.DAYS);
		Assert.assertEquals(47, loadSlot.getWindowSizeInHours());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 2, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());

		loadSlot.setWindowSize(1);
		loadSlot.setWindowSizeUnits(TimePeriod.MONTHS);
		Assert.assertEquals(31 * 24 - 1, loadSlot.getWindowSizeInHours());
		Assert.assertEquals(ZonedDateTime.of(2015, 12, 31, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowEndWithSlotOrPortTime());

	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotWindowTrimmedByToday() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FP-Market", portFinder.findPort("Point Fortin"), entity, "5", 22.3)//
				.withAvailabilityConstant(2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2017, 7), market.getNotionalPort()) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 7, 23), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1)//
				.withAssignmentFlags(false, false) //
				.build();

		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 13));

		// Check expected window end date
		Assert.assertEquals(ZonedDateTime.of(2017, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowStartWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, ZoneId.of("UTC")).minusHours(1), loadSlot.getWindowEndWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2017, 7, 23, 0, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getWindowEndWithSlotOrPortTime());

		evaluateWithLSOTest(plan -> plan.getUserSettings().setWithSpotCargoMarkets(true), scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			// Existing spot is not trimmed
			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0 + 31 * 24, o_loadSlot.getTimeWindow().getExclusiveEnd());

			// Check other july spots are trimmed.
			final Collection<@NonNull LoadSlot> allModelObjects = modelEntityMap.getAllModelObjects(LoadSlot.class);
			// Found trimmed option
			boolean foundTrimmedOption = false;
			for (final LoadSlot spot : allModelObjects) {
				if (spot == loadSlot) {
					continue;
				}
				if (spot.getWindowStart().getMonthValue() == 7 && spot.getWindowStart().getYear() == 2017) {
					foundTrimmedOption = true;
					final IPortSlot o_spot = modelEntityMap.getOptimiserObjectNullChecked(spot, IPortSlot.class);
					// Expect start on 12th
					Assert.assertEquals(0 + (12) * 24, o_spot.getTimeWindow().getInclusiveStart());
					Assert.assertEquals(0 + 744, o_spot.getTimeWindow().getExclusiveEnd());
				}
			}
			Assert.assertTrue(foundTrimmedOption);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotWindowTrimmedByToday_PeriodStartsBefore() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FP-Market", portFinder.findPort("Point Fortin"), entity, "5", 22.3)//
				.withAvailabilityConstant(2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2017, 7), market.getNotionalPort()) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 7, 23), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1)//
				.withAssignmentFlags(false, false) //
				.build();

		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 13));

		// Check expected window end date
		Assert.assertEquals(ZonedDateTime.of(2017, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowStartWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, ZoneId.of("UTC")).minusHours(1), loadSlot.getWindowEndWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2017, 7, 23, 0, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getWindowEndWithSlotOrPortTime());

		evaluateWithLSOTest(plan -> {
			plan.getUserSettings().setWithSpotCargoMarkets(true);
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 7, 1));
		}, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			@NonNull
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioToOptimiserBridge.getOptimiserScenario());
			final VesselAvailability period_vesselAvailability = cargoModel.getVesselAvailabilities().get(0);
			final LoadSlot period_loadSlot = cargoModel.getLoadSlots().get(0);
			final DischargeSlot period_dischargeSlot = cargoModel.getDischargeSlots().get(0);

			// Existing spot is not trimmed
			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(period_loadSlot, IPortSlot.class);
			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0 + 31 * 24, o_loadSlot.getTimeWindow().getExclusiveEnd());

			// Check other july spots are trimmed.
			final Collection<@NonNull LoadSlot> allModelObjects = modelEntityMap.getAllModelObjects(LoadSlot.class);
			// Found trimmed option
			boolean foundTrimmedOption = false;
			for (final LoadSlot spot : allModelObjects) {
				if (spot == period_loadSlot) {
					continue;
				}
				if (spot.getWindowStart().getMonthValue() == 7 && spot.getWindowStart().getYear() == 2017) {
					foundTrimmedOption = true;
					final IPortSlot o_spot = modelEntityMap.getOptimiserObjectNullChecked(spot, IPortSlot.class);
					// Expect start on 13th
					Assert.assertEquals(0 + (12) * 24, o_spot.getTimeWindow().getInclusiveStart());
					Assert.assertEquals(0 + 744, o_spot.getTimeWindow().getExclusiveEnd());

					// Make output windows are correct - still full month
					final Schedule schedule = scenarioToOptimiserBridge.createSchedule(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer(), period_vesselAvailability, spot, period_dischargeSlot),
							null);
					Assert.assertNotNull(schedule);
					final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
					Assert.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowSize());
					Assert.assertEquals(TimePeriod.MONTHS, cargoAllocation.getLoadAllocation().getSlot().getWindowSizeUnits());
					Assert.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowStart().getDayOfMonth());
				}
			}
			Assert.assertTrue(foundTrimmedOption);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotWindowTrimmedByToday_PeriodStartsAfter() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FP-Market", portFinder.findPort("Point Fortin"), entity, "5", 22.3)//
				.withAvailabilityConstant(2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2017, 7), market.getNotionalPort()) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 7, 23), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1)//
				.withAssignmentFlags(false, false) //
				.build();

		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 13));

		// Check expected window end date
		Assert.assertEquals(ZonedDateTime.of(2017, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getWindowStartWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, ZoneId.of("UTC")).minusHours(1), loadSlot.getWindowEndWithSlotOrPortTime());
		Assert.assertEquals(ZonedDateTime.of(2017, 7, 23, 0, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getWindowEndWithSlotOrPortTime());

		evaluateWithLSOTest(plan -> {
			plan.getUserSettings().setWithSpotCargoMarkets(true);
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 8, 1));
		}, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			@NonNull
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioToOptimiserBridge.getOptimiserScenario());
			final VesselAvailability period_vesselAvailability = cargoModel.getVesselAvailabilities().get(0);
			final LoadSlot period_loadSlot = cargoModel.getLoadSlots().get(0);
			final DischargeSlot period_dischargeSlot = cargoModel.getDischargeSlots().get(0);

			// Existing spot is trimmed via period transformer
			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(period_loadSlot, IPortSlot.class);
			Assert.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assert.assertEquals(1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final Collection<@NonNull LoadSlot> allModelObjects = modelEntityMap.getAllModelObjects(LoadSlot.class);
			for (final LoadSlot spot : allModelObjects) {
				if (spot == period_loadSlot) {
					continue;
				}
				if (spot.getWindowStart().getMonthValue() == 7 && spot.getWindowStart().getYear() == 2017) {
					Assert.fail("No extra July spot slots expected");
				}
			}

			// Make output windows are correct
			final Schedule schedule = scenarioToOptimiserBridge
					.createSchedule(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer(), period_vesselAvailability, period_loadSlot, period_dischargeSlot), null);
			Assert.assertNotNull(schedule);
			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowSize());
			Assert.assertEquals(TimePeriod.MONTHS, cargoAllocation.getLoadAllocation().getSlot().getWindowSizeUnits());
			Assert.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowStart().getDayOfMonth());

		});
	}

}