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
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;

/**
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class TravelTimeConstraintCheckerTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testEventOrder_ExactTravel() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vesselClass.setMaxSpeed(15.0);

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100), LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TravelTimeConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TravelTimeConstraintChecker.class);
			checker.setMaxLateness(0);
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event2, event1), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testEventOrder_WithLateness() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vesselClass.setMaxSpeed(15.0);

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		// Will be 1 hour late
		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99), LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TravelTimeConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TravelTimeConstraintChecker.class);
			checker.setMaxLateness(0);
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
			checker.setMaxLateness(1);
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testEventOrder_WithLateness_WithCanal() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours ( + 36 transit time)
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 3000, 1500, 3000, true);
		vesselClass.setMaxSpeed(15.0);
		for (VesselClassRouteParameters p : vesselClass.getRouteParameters()) {
			p.setExtraTransitTime(36);
		}

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		// Will be 1 hour late
		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 36 + 99), LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 36 + 99), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TravelTimeConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TravelTimeConstraintChecker.class);
			checker.setMaxLateness(0);
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
			checker.setMaxLateness(1);
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, event1, event2), null));
		});
	}
}