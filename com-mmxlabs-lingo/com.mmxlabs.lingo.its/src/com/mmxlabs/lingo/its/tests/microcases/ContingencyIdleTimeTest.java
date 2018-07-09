/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IExtraIdleTimeProviderEditor;

/**
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class ContingencyIdleTimeTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testDataProviderIdleTimeIncrease() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check no initial delay
			{
				final Schedule schedule = scenarioRunner.evaluateInitialState();
				final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
				final Idle ladenIdle = cargoAllocation.getLadenIdle();
				final Idle ballastIdle = cargoAllocation.getBallastIdle();
				Assert.assertEquals(0, ladenIdle.getDuration());
				Assert.assertEquals(0, ballastIdle.getDuration());
			}
			final IExtraIdleTimeProviderEditor extraIdleTimeProviderEditor = scenarioToOptimiserBridge.getInjector().getInstance(IExtraIdleTimeProviderEditor.class);
			final ModelEntityMap modelEntityMap = scenarioRunner.getScenarioToOptimiserBridge().getDataTransformer().getModelEntityMap();

			{
				final IPort fromPort = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0).getPort(), IPort.class);
				final IPort toPort = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(1).getPort(), IPort.class);
				extraIdleTimeProviderEditor.setExtraIdleTimeOnVoyage(fromPort, toPort, 48);

				final Schedule schedule = scenarioRunner.evaluateInitialState();
				final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
				final Idle ladenIdle = cargoAllocation.getLadenIdle();
				final Idle ballastIdle = cargoAllocation.getBallastIdle();
				Assert.assertEquals(48, ladenIdle.getDuration());
				Assert.assertEquals(0, ballastIdle.getDuration());
			}
			{
				final IPort fromPort = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(1).getPort(), IPort.class);
				final IPort toPort = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0).getPort(), IPort.class);
				extraIdleTimeProviderEditor.setExtraIdleTimeOnVoyage(fromPort, toPort, 48);

				final Schedule schedule = scenarioRunner.evaluateInitialState();
				Assert.assertNotNull("Unable to evaluate schedule", schedule);
				final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
				final Idle ladenIdle = cargoAllocation.getLadenIdle();
				final Idle ballastIdle = cargoAllocation.getBallastIdle();
				Assert.assertEquals(48, ladenIdle.getDuration());
				Assert.assertEquals(48, ballastIdle.getDuration());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testMatrixValue_1() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		portModelBuilder.setContingencyDelay(portFinder.findPort("Point Fortin"), portFinder.findPort("Dominion Cove Point LNG"), 1);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check no initial delay
			{
				final Schedule schedule = scenarioRunner.evaluateInitialState();
				final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
				final Idle ladenIdle = cargoAllocation.getLadenIdle();
				final Idle ballastIdle = cargoAllocation.getBallastIdle();
				Assert.assertEquals(24, ladenIdle.getDuration());
				Assert.assertEquals(0, ballastIdle.getDuration());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testMatrixValue_2() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		portModelBuilder.setContingencyDelayForAllVoyages(1);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check no initial delay
			{
				final Schedule schedule = scenarioRunner.evaluateInitialState();
				final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
				final Idle ladenIdle = cargoAllocation.getLadenIdle();
				final Idle ballastIdle = cargoAllocation.getBallastIdle();
				Assert.assertEquals(24, ladenIdle.getDuration());
				Assert.assertEquals(24, ballastIdle.getDuration());
			}
		});
	}
}