/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
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
				.withWindowSize(0) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
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

		evaluateWithLSOTest(scenarioRunner -> {

			// Run optimisation to generate the cargo
			scenarioRunner.run();

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			final Cargo cargo = lngScenarioModel.getCargoModel().getCargoes().get(0);

			Assert.assertSame(loadSlot, cargo.getSlots().get(0));
			final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);

			Assert.assertEquals(LocalDate.of(2015, 12, 1), dischargeSlot.getWindowStart());
			Assert.assertEquals(0, dischargeSlot.getWindowStartTime());
			Assert.assertEquals(31 * 24 - 1, dischargeSlot.getWindowSize());
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
				.withWindowSize(23) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(23) //
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
				.withWindowSize(23) //
				.withWindowFlex(12) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(23) //
				.withWindowFlex(18) //
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

			IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

			Assert.assertEquals(0, o_vesselAvailability.getStartRequirement().getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_vesselAvailability.getStartRequirement().getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(0 + 23 + 1, o_vesselAvailability.getStartRequirement().getTimeWindow().getExclusiveEnd());

			Assert.assertEquals(31 * 24, o_vesselAvailability.getEndRequirement().getTimeWindow().getInclusiveStart());
			Assert.assertEquals(0, o_vesselAvailability.getEndRequirement().getTimeWindow().getExclusiveEndFlex());
			Assert.assertEquals(31 * 24 + 23 + 1, o_vesselAvailability.getEndRequirement().getTimeWindow().getExclusiveEnd());
		});
	}

}