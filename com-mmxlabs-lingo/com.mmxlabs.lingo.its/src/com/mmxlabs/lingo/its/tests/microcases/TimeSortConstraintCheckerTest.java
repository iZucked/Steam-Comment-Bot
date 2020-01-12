/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
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
@ExtendWith(ShiroRunner.class)
public class TimeSortConstraintCheckerTest extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEventOrder_ConsequetiveEvent() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
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

			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, event1, event2), null));
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, event2, event1), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEventOrder_OverlappingEvent() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
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

			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, event1, event2), null));
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, event2, event1), null));
		});
	}
}