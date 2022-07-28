/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
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
@ExtendWith(ShiroRunner.class)
public class TravelTimeConstraintCheckerTest extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEventOrder_ExactTravel() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", 
				LocalDateTime.of(2015, 12, 1, 0, 0, 0), 
				LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", 
						LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100), 
						LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselCharter, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TravelTimeConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TravelTimeConstraintChecker.class);
			checker.setMaxLateness(0);
			Assertions.assertTrue(checker.checkConstraints(
					SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, event1, event2),
					null, new ArrayList<>()));
			Assertions.assertFalse(checker.checkConstraints(
					SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, event2, event1),
					null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEventOrder_WithLateness() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", 
				LocalDateTime.of(2015, 12, 1, 0, 0, 0), 
				LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		// Will be 1 hour late
		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", 
						LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99), 
						LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselCharter, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TravelTimeConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TravelTimeConstraintChecker.class);
			checker.setMaxLateness(0);
			Assertions.assertFalse(checker.checkConstraints(
					SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, event1, event2),
					null, new ArrayList<>()));
			checker.setMaxLateness(1);
			Assertions.assertTrue(checker.checkConstraints(
					SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, event1, event2),
					null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEventOrder_WithLateness_WithCanal() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours ( + 36 transit time)
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 3000, 1500, 3000, true);
		vessel.setMaxSpeed(15.0);
		for (VesselRouteParameters p : vessel.getVesselOrDelegateRouteParameters()) {
			p.setExtraTransitTime(36);
		}

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", 
				LocalDateTime.of(2015, 12, 1, 0, 0, 0), 
				LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		// Will be 1 hour late
		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", 
						LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 36 + 99), 
						LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 36 + 99), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselCharter, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final TravelTimeConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, TravelTimeConstraintChecker.class);
			checker.setMaxLateness(0);
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(
					scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, event1, event2), null, new ArrayList<>()));
			checker.setMaxLateness(1);
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(
					scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, event1, event2), null, new ArrayList<>()));
		});
	}
}