/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class CooldownTests extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testStartEventCooldown() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setWarmingTime(0);
		vessel.setCoolingVolume(1000);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getBaseFuel(), "0");
		costModelBuilder.setAllExistingCooldownCosts(false, "5");

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 2), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule().getSequences().get(0);
			final StartEvent event1 = (StartEvent) sequence.getEvents().get(0);

			// = 1000m3 * 22.8 * $5.0
			Assert.assertEquals(-114_000, ScheduleModelKPIUtils.getElementPNL(event1));
			Assert.assertEquals(114_000, ScheduleModelKPIUtils.calculateEventShippingCost(event1, false, true, ShippingCostType.ALL).longValue());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartEventCooldownLumpsum() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setWarmingTime(0);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getBaseFuel(), "0.0");
		costModelBuilder.setAllExistingCooldownCosts(true, "75000");

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 2), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final IScenarioDataProvider optimiserScenarioDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenarioDataProvider).getSchedule().getSequences().get(0);
			final StartEvent event1 = (StartEvent) sequence.getEvents().get(0);

			Assert.assertEquals(-75_000, ScheduleModelKPIUtils.getElementPNL(event1));
			Assert.assertEquals(75_000, ScheduleModelKPIUtils.calculateEventShippingCost(event1, false, true, ShippingCostType.ALL).longValue());

		});
	}
}
