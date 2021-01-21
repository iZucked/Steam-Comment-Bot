/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
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
@ExtendWith(ShiroRunner.class)
public class TimeWindowCreationTest extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselEvent_Exact() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
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

			Assertions.assertEquals(0, eventSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, eventSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(0 + 1, eventSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselEvent_OneDay() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
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

			Assertions.assertEquals(0, eventSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, eventSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(23 + 1, eventSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselEvent_OneMonth() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
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
			Assertions.assertEquals(0 + 30 * 24, dateAndCurveHelper.convertTime(event.getStartByAsDateTime()));

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVesselEventPortSlot eventSlot = modelEntityMap.getOptimiserObjectNullChecked(event, IVesselEventPortSlot.class);

			Assertions.assertEquals(0, eventSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, eventSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(30 * 24 + 1, eventSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_Exact() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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

			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(0 + 1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final IPortSlot o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			Assertions.assertEquals(6 * 24, o_dischargeSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, o_dischargeSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(6 * 24 + 1, o_dischargeSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	/**
	 * Does the optimiser created spot slot have the correct window?
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_SpotCreated() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8); //
		loadSlot.setWindowStartTime(0);
		loadSlot.setWindowSize(0);

		spotMarketsModelBuilder.makeDESSaleMarket("desmarket", portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), entity, "7") //
				.withAvailabilityConstant(1) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2015, 11, 1));

		evaluateWithLSOTest(scenarioRunner -> {

			// Run optimisation to generate the cargo
			scenarioRunner.runAndApplyBest();

			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			final Cargo cargo = lngScenarioModel.getCargoModel().getCargoes().get(0);

			Assertions.assertSame(loadSlot, cargo.getSlots().get(0));
			final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

			Assertions.assertEquals(LocalDate.of(2015, 12, 1), dischargeSlot.getWindowStart());
			Assertions.assertEquals(0, dischargeSlot.getWindowStartTime());
			Assertions.assertEquals(31 * 24 - 1, dischargeSlot.getSchedulingTimeWindow().getSizeInHours());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_OneDay() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 5, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 11, 23, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getSchedulingTimeWindow().getEnd());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);

			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(23 + 1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final IPortSlot o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			Assertions.assertEquals(6 * 24, o_dischargeSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, o_dischargeSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(6 * 24 + 23 + 1, o_dischargeSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_OneDay_PlusFlex() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.withWindowFlex(12, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 5, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 11, 23, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getSchedulingTimeWindow().getEnd());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(12, o_loadSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(23 + 12 + 1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final IPortSlot o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			Assertions.assertEquals(6 * 24, o_dischargeSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(18, o_dischargeSlot.getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(6 * 24 + +18 + 23 + 1, o_dischargeSlot.getTimeWindow().getExclusiveEnd());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselAvailability_Specified() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 23, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 2, 1, 0, 0, 0), LocalDateTime.of(2016, 2, 1, 23, 0, 0)) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

			Assertions.assertEquals(0, o_vesselAvailability.getStartRequirement().getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, o_vesselAvailability.getStartRequirement().getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(0 + 23 + 1, o_vesselAvailability.getStartRequirement().getTimeWindow().getExclusiveEnd());

			Assertions.assertEquals(31 * 24, o_vesselAvailability.getEndRequirement().getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0, o_vesselAvailability.getEndRequirement().getTimeWindow().getExclusiveEndFlex());
			Assertions.assertEquals(31 * 24 + 23 + 1, o_vesselAvailability.getEndRequirement().getTimeWindow().getExclusiveEnd());
		});
	}

	/**
	 * Simple test checking the window size and end dates are as expected
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_SlotWindowEnd() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8); //
		loadSlot.setWindowStartTime(0);

		loadSlot.setWindowSize(0);
		Assertions.assertEquals(0, loadSlot.getSchedulingTimeWindow().getSizeInHours());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());

		loadSlot.setWindowSize(23);
		loadSlot.setWindowSizeUnits(TimePeriod.HOURS);
		Assertions.assertEquals(23, loadSlot.getSchedulingTimeWindow().getSizeInHours());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 1, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());

		loadSlot.setWindowSize(1);
		loadSlot.setWindowSizeUnits(TimePeriod.DAYS);
		Assertions.assertEquals(23, loadSlot.getSchedulingTimeWindow().getSizeInHours());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 1, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());

		loadSlot.setWindowSize(2);
		loadSlot.setWindowSizeUnits(TimePeriod.DAYS);
		Assertions.assertEquals(47, loadSlot.getSchedulingTimeWindow().getSizeInHours());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 2, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());

		loadSlot.setWindowSize(1);
		loadSlot.setWindowSizeUnits(TimePeriod.MONTHS);
		Assertions.assertEquals(31 * 24 - 1, loadSlot.getSchedulingTimeWindow().getSizeInHours());
		Assertions.assertEquals(ZonedDateTime.of(2015, 12, 31, 23, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getEnd());

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotWindowTrimmedByToday() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FP-Market", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.3)//
				.withAvailabilityConstant(2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2017, 7), market.getNotionalPort()) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 7, 23), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		Assertions.assertEquals(ZonedDateTime.of(2017, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getStart());
		Assertions.assertEquals(ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, ZoneId.of("UTC")).minusHours(1), loadSlot.getSchedulingTimeWindow().getEnd());
		Assertions.assertEquals(ZonedDateTime.of(2017, 7, 23, 0, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getSchedulingTimeWindow().getEnd());

		evaluateWithLSOTest(plan -> plan.getUserSettings().setWithSpotCargoMarkets(true), scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			// Existing spot is not trimmed
			final IPortSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0 + 31 * 24, o_loadSlot.getTimeWindow().getExclusiveEnd());

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
					Assertions.assertEquals(0 + (12) * 24, o_spot.getTimeWindow().getInclusiveStart());
					Assertions.assertEquals(0 + 744, o_spot.getTimeWindow().getExclusiveEnd());
				}
			}
			Assertions.assertTrue(foundTrimmedOption);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotWindowTrimmedByToday_PeriodStartsBefore() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FP-Market", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.3)//
				.withAvailabilityConstant(2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2017, 7), market.getNotionalPort()) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 7, 23), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		Assertions.assertEquals(ZonedDateTime.of(2017, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getStart());
		Assertions.assertEquals(ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, ZoneId.of("UTC")).minusHours(1), loadSlot.getSchedulingTimeWindow().getEnd());
		Assertions.assertEquals(ZonedDateTime.of(2017, 7, 23, 0, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getSchedulingTimeWindow().getEnd());

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
			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(0 + 31 * 24, o_loadSlot.getTimeWindow().getExclusiveEnd());

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
					Assertions.assertEquals(0 + (12) * 24, o_spot.getTimeWindow().getInclusiveStart());
					Assertions.assertEquals(0 + 744, o_spot.getTimeWindow().getExclusiveEnd());

					// Make output windows are correct - still full month
					final Schedule schedule = scenarioToOptimiserBridge.createSchedule(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), period_vesselAvailability, spot, period_dischargeSlot),
							null);
					Assertions.assertNotNull(schedule);
					final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
					Assertions.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowSize());
					Assertions.assertEquals(TimePeriod.MONTHS, cargoAllocation.getLoadAllocation().getSlot().getWindowSizeUnits());
					Assertions.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowStart().getDayOfMonth());
				}
			}
			Assertions.assertTrue(foundTrimmedOption);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotWindowTrimmedByToday_PeriodStartsAfter() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FP-Market", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.3)//
				.withAvailabilityConstant(2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2017, 7), market.getNotionalPort()) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 7, 23), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		Assertions.assertEquals(ZonedDateTime.of(2017, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC")), loadSlot.getSchedulingTimeWindow().getStart());
		Assertions.assertEquals(ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, ZoneId.of("UTC")).minusHours(1), loadSlot.getSchedulingTimeWindow().getEnd());
		Assertions.assertEquals(ZonedDateTime.of(2017, 7, 23, 0, 0, 0, 0, ZoneId.of("UTC")), dischargeSlot.getSchedulingTimeWindow().getEnd());

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
			Assertions.assertEquals(0, o_loadSlot.getTimeWindow().getInclusiveStart());
			Assertions.assertEquals(1, o_loadSlot.getTimeWindow().getExclusiveEnd());

			final Collection<@NonNull LoadSlot> allModelObjects = modelEntityMap.getAllModelObjects(LoadSlot.class);
			for (final LoadSlot spot : allModelObjects) {
				if (spot == period_loadSlot) {
					continue;
				}
				if (spot.getWindowStart().getMonthValue() == 7 && spot.getWindowStart().getYear() == 2017) {
					Assertions.fail("No extra July spot slots expected");
				}
			}

			// Make output windows are correct
			final Schedule schedule = scenarioToOptimiserBridge
					.createSchedule(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), period_vesselAvailability, period_loadSlot, period_dischargeSlot), null);
			Assertions.assertNotNull(schedule);
			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowSize());
			Assertions.assertEquals(TimePeriod.MONTHS, cargoAllocation.getLoadAllocation().getSlot().getWindowSizeUnits());
			Assertions.assertEquals(1, cargoAllocation.getLoadAllocation().getSlot().getWindowStart().getDayOfMonth());

		});
	}

}