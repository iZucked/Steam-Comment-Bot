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
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintChecker;

/**
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class TimeSortConstraintCheckerTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testEventOrder_ConsequetiveEvent() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 1, 0, 0, 0), dischargePort) //
				.withDurationInDays(0) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();
		final DryDockEvent event2 = cargoModelBuilder.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 1, 0, 0), LocalDateTime.of(2015, 12, 1, 1, 0, 0), dischargePort) //
				.withDurationInDays(0) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TimeSortConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TimeSortConstraintChecker.class);

			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event2, event1), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testEventOrder_OverlappingEvent() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 2, 0, 0, 0), dischargePort) //
				.withDurationInDays(0) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();
		final DryDockEvent event2 = cargoModelBuilder.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 12, 0, 0), LocalDateTime.of(2015, 12, 2, 12, 0, 0), dischargePort) //
				.withDurationInDays(0) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TimeSortConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TimeSortConstraintChecker.class);

			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event2, event1), null));
		});
	}
}