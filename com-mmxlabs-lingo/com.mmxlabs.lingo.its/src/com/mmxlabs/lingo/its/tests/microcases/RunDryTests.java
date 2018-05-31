/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;

/**
 * Test cases for when we run out of heel on a non-cargo voyage.
 * 
 * @author Simon Goodall
 *
 */
@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class RunDryTests extends AbstractMicroTestCase {

	public @NonNull Vessel configureVessel() {

		for (final BaseFuel baseFuel : fleetModelFinder.getFleetModel().getBaseFuels()) {
			baseFuel.setEquivalenceFactor(20.0);
		}

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		fleetModelBuilder.setVesselStateAttributes(vessel, true, 100, 50, 20, 10);
		fleetModelBuilder.setVesselStateAttributes(vessel, false, 100, 50, 20, 10);

		vessel.setPilotLightRate(1);
		vessel.setMinSpeed(15.0);
		vessel.setMaxSpeed(15.0);

		vessel.getLadenAttributes().getFuelConsumption().clear();
		vessel.getBallastAttributes().getFuelConsumption().clear();
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 15.0, 100);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 15.0, 100);

		return vessel;
	}

	@Test
	@Category({ MicroTest.class })
	public void testPlentyOfHeel() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		distanceModelBuilder.setPortToPortDistance(port1, port2, 10 * 15 * 24, Integer.MAX_VALUE, Integer.MAX_VALUE, true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(1000, 1000, 20, "1") //
				.withEndPort(port2) //
				.withEndWindow(LocalDateTime.of(2017, 11, 30, 0, 0)) //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Journey journey = (Journey) sequence.getEvents().get(1);

		// 10 days * 100m3/day
		Assert.assertEquals(10 * 100, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, journey));
		Assert.assertEquals(10 * 1, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, journey));
	}

	@Test
	@Category({ MicroTest.class })
	public void testFifyPercentHeelInTravel() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		distanceModelBuilder.setPortToPortDistance(port1, port2, 10 * 15 * 24, Integer.MAX_VALUE, Integer.MAX_VALUE, true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(500, 500, 20, "1") //
				.withEndPort(port2) //
				.withEndWindow(LocalDateTime.of(2017, 11, 30, 0, 0)) //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Journey journey = (Journey) sequence.getEvents().get(1);

		// 10 days * 100m3/day
		Assert.assertEquals(10 * 100 / 2, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, journey));
		Assert.assertEquals(10 * 1 / 2, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, journey));
		Assert.assertEquals(10 * 100 / 2, ScheduleTools.getFuelQuantity(Fuel.BASE_FUEL, FuelUnit.MT, journey));
	}

	@Test
	@Category({ MicroTest.class })
	public void testFifyPercentHeelInTravel_FBO() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();
		vessel.setHasReliqCapability(true); // Forces FBO
		// NBO is 50 per day now
		fleetModelBuilder.setVesselStateAttributes(vessel, true, 50, 50, 20, 10);
		fleetModelBuilder.setVesselStateAttributes(vessel, false, 50, 50, 20, 10);
		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getBaseFuel(), "1000");

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		distanceModelBuilder.setPortToPortDistance(port1, port2, 10 * 15 * 24, Integer.MAX_VALUE, Integer.MAX_VALUE, true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(500, 500, 20, "1") //
				.withEndPort(port2) //
				.withEndWindow(LocalDateTime.of(2017, 11, 30, 0, 0)) //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Journey journey = (Journey) sequence.getEvents().get(1);

		// 5 days nbo/fbo, 5 days base
		Assert.assertEquals(5 * 50, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, journey));
		Assert.assertEquals(5 * 50, ScheduleTools.getFuelQuantity(Fuel.FBO, FuelUnit.M3, journey));
		Assert.assertEquals(5 * 1, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, journey));
		// 5 days full
		Assert.assertEquals(5 * 100, ScheduleTools.getFuelQuantity(Fuel.BASE_FUEL, FuelUnit.MT, journey));
	}

	@Test
	@Category({ MicroTest.class })
	public void testFifyPercentHeelInTravel_BaseSupplemental() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();
		// NBO is 50 per day now
		fleetModelBuilder.setVesselStateAttributes(vessel, true, 50, 50, 20, 10);
		fleetModelBuilder.setVesselStateAttributes(vessel, false, 50, 50, 20, 10);

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		distanceModelBuilder.setPortToPortDistance(port1, port2, 10 * 15 * 24, Integer.MAX_VALUE, Integer.MAX_VALUE, true);
		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getBaseFuel(), "1");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(400, 400, 20, "50") // High heel price
				.withEndPort(port2) //
				.withEndWindow(LocalDateTime.of(2017, 11, 30, 0, 0)) //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Journey journey = (Journey) sequence.getEvents().get(1);

		// 10 days * 100m3/day
		Assert.assertEquals(8 * 50, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, journey));
		Assert.assertEquals(0, ScheduleTools.getFuelQuantity(Fuel.FBO, FuelUnit.M3, journey));
		Assert.assertEquals(0, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, journey));
		Assert.assertEquals(8 * 50 + 2 * 100, ScheduleTools.getFuelQuantity(Fuel.BASE_FUEL, FuelUnit.MT, journey));
	}

	@Test
	@Category({ MicroTest.class })
	public void testFifyPercentHeelInIdle() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		distanceModelBuilder.setPortToPortDistance(port1, port2, 10 * 15 * 24, Integer.MAX_VALUE, Integer.MAX_VALUE, true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(250, 250, 20, "1") //
				.withEndPort(port1) //
				.withEndWindow(LocalDateTime.of(2017, 11, 30, 0, 0)) //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Idle idle = (Idle) sequence.getEvents().get(1);

		// 10 days * 50m3/day
		Assert.assertEquals(10 * 50 / 2, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, idle));
		Assert.assertEquals(10 * 1 / 2, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, idle));

		// 10 days * 20MT/day
		Assert.assertEquals(10 * 20 / 2, ScheduleTools.getFuelQuantity(Fuel.BASE_FUEL, FuelUnit.MT, idle));
	}

	@Test
	@Category({ MicroTest.class })
	public void testFullInTravelFifyPercentInIdle() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		distanceModelBuilder.setPortToPortDistance(port1, port2, 10 * 15 * 24, Integer.MAX_VALUE, Integer.MAX_VALUE, true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(1250, 1250, 20, "1") //
				.withEndPort(port2) //
				.withEndWindow(LocalDateTime.of(2017, 12, 10, 0, 0)) //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Journey journey = (Journey) sequence.getEvents().get(1);

		// 10 days * 100m3/day
		Assert.assertEquals(10 * 100, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, journey));
		Assert.assertEquals(10 * 1, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, journey));

		final Idle idle = (Idle) sequence.getEvents().get(2);
		// 10 days * 50m3/day
		Assert.assertEquals(10 * 50 / 2, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, idle));
		// 10 days * 20MT/day
		Assert.assertEquals(10 * 20 / 2, ScheduleTools.getFuelQuantity(Fuel.BASE_FUEL, FuelUnit.MT, idle));
		Assert.assertEquals(10 * 1 / 2, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, idle));

	}

	@Test
	@Category({ MicroTest.class })
	public void testNBOOnlyForCanal() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = configureVessel();

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Sakai");

		final Route route = portFinder.findCanal(RouteOption.SUEZ);

		fleetModelBuilder.setRouteParameters(vessel, RouteOption.SUEZ, 50, 50, 50, 50, 24);

		distanceModelBuilder.setPortToPortDistance(port1, port2, 50 * 15 * 24, 10 * 15 * 24, Integer.MAX_VALUE, true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartPort(port1) //
				.withStartWindow(LocalDateTime.of(2017, 11, 20, 0, 0)) //
				.withStartHeel(50, 50, 20, "1") //
				.withEndPort(port2) //
				.withEndWindow(LocalDateTime.of(2017, 12, 1, 0, 0)) //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "0") //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assert.assertNotNull(schedule);

		final Sequence sequence = schedule.getSequences().get(0);
		final Journey journey = (Journey) sequence.getEvents().get(1);

		// 1 days * 50 m3/day
		Assert.assertEquals(1 * 50, ScheduleTools.getFuelQuantity(Fuel.NBO, FuelUnit.M3, journey));
		Assert.assertEquals(1 * 1, ScheduleTools.getFuelQuantity(Fuel.PILOT_LIGHT, FuelUnit.MT, journey));

		// 10 days * 100 mt/day
		Assert.assertEquals(10 * 100, ScheduleTools.getFuelQuantity(Fuel.BASE_FUEL, FuelUnit.MT, journey));
	}
}
