/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

@ExtendWith(value = ShiroRunner.class)
public class PortShipSizeEndToEndConstraintTests extends AbstractMicroTestCase {

	private List<IConstraintChecker> failedConstraints;
	
	private static final int vesselCapacity = 15_000;
	
	public static Collection<?> getTestParameters() {
		return Arrays.asList(new Object[][] {
			{ 0, 0, 0, 0, true },
			{ 10000, 0, 0, 0, true },
			{ 0, 20000, 0, 0, true },
			{ 10000, 20000, 0, 0, true },
			{ 20000, 0, 0, 0, false },
			{ 0, 10000, 0, 0, false },
			{ 20000, 0, 0, 10000, false }			
		});
	}
	
	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testVesselCharter(int port1MinVesselSize, int port1MaxVesselSize, int port2MinVesselSize, int port2MaxVesselSize, boolean expectedConstraintCheckResult) {
		Port port1 = getPort(InternalDataConstants.PORT_POINT_FORTIN, port1MinVesselSize, port1MaxVesselSize);
		Port port2 = getPort(InternalDataConstants.PORT_COVE_POINT, port2MinVesselSize, port2MaxVesselSize);
		
		Vessel vessel = createVessel(vesselCapacity);
		
		VesselCharter vesselCharter = createVesselCharter(vessel);
	
		createCargo(port1, port2, vesselCharter);		
			
		validateConstraintChecks(expectedConstraintCheckResult);
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCharterIn(int port1MinVesselSize, int port1MaxVesselSize, int port2MinVesselSize, int port2MaxVesselSize, boolean expectedConstraintCheckResult) {
		Port port1 = getPort(InternalDataConstants.PORT_POINT_FORTIN, port1MinVesselSize, port1MaxVesselSize);
		Port port2 = getPort(InternalDataConstants.PORT_COVE_POINT, port2MinVesselSize, port2MaxVesselSize);
		
		Vessel vessel = createVessel(vesselCapacity);
		
		CharterInMarket charterInMarket = createCharterInMarket(vessel);
	
		Cargo cargo = createCargo(port1, port2, charterInMarket);		
			
		validateConstraintChecks(expectedConstraintCheckResult);
	}
	
	private Cargo createCargo(Port port1, Port port2, VesselAssignmentType vesselAssignmentType) {
		Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 10), port1, null, entity, "5")
				.build()
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), port2, null, entity, "7")
				.build()
				.withAssignmentType(vesselAssignmentType)
				.build();
		return cargo;
	}

	private Vessel createVessel(int capacity) {
		Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setCapacity(capacity);
		return vessel;
	}
	
	private VesselCharter createVesselCharter(Vessel vessel) {
		VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel, entity)
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 8, 0, 0, 0))
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0))
				.build();
		return vesselCharter1;
	}

	private CharterInMarket createCharterInMarket(final Vessel vessel) {
		// Set up a charter index curve
		final int charterRatePerDay = 240;
		
		// Create a charter-in market object
		final SpotMarketsModel sportMarketsModel = ScenarioModelUtil.getSpotMarketsModel(this.lngScenarioModel);
		final EList<CharterInMarket> charteringSpotMarkets = sportMarketsModel.getCharterInMarkets();

		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charteringSpotMarkets.add(charterInMarket);
		charterInMarket.setName("market-" + vessel.getName());

		charterInMarket.setVessel(vessel);
		charterInMarket.setEnabled(true);
		charterInMarket.setSpotCharterCount(1);
		charterInMarket.setCharterInRate("" + charterRatePerDay);
		
		return charterInMarket;
	}
	
	private Port getPort(String portId, int minVesselSize, int maxVesselSize) {
		Port port = portFinder.findPortById(portId);
		if (minVesselSize != 0) port.setMinVesselSize(minVesselSize);
		if (maxVesselSize != 0) port.setMaxVesselSize(maxVesselSize);
		return port;
	}
	
	private void validateConstraintChecks(boolean expectedConstaintCheckResult) {
		optimiseWithLSOTest(scenarioRunner -> {
			final List<IConstraintChecker> failedConstraintCheckers = getFailedConstraints();
			if (expectedConstaintCheckResult) { 
				Assertions.assertNull(failedConstraintCheckers);
			}
			else {
				this.validatePortShipSizeConstraintViolated(failedConstraintCheckers);
			}
		});
	}

	private List<IConstraintChecker> getFailedConstraints() {
		
		optimiseWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			failedConstraints = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
		});
		
		return failedConstraints;
	}

	private void validatePortShipSizeConstraintViolated(final List<IConstraintChecker> failedConstraintCheckers) {
		// Check that port ship size constraint is violated.
		boolean portShipSizeConstraintCheckerViolated = false;
		Assertions.assertNotNull(failedConstraintCheckers);
		for (IConstraintChecker c : failedConstraintCheckers) {
			if (c instanceof PortShipSizeConstraintChecker) {
				portShipSizeConstraintCheckerViolated = true;
			}
		}
		Assertions.assertTrue(portShipSizeConstraintCheckerViolated);
	}
}
