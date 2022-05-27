/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;

/**
 * Misc period transformer tests
 *
 */
public class CargoesAcrossBoundaryTests extends AbstractMicroTestCase {

	/**
	 * Load slot is IN but discharge is in the AFTER boundary. We expect the cargo to be rewirable, but the discharge should be locked down.
	 */
	@Test
	public void testCargoINtoAFTER() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		final DESSalesMarket sellMarket = spotMarketsModelBuilder.makeDESSaleMarket("m1", portFinder.findPortById(InternalDataConstants.PORT_SAKAI), entity, "7").build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 8), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				.makeMarketDESSale("D2", sellMarket, YearMonth.of(2022, 2)) //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2021, 1, 28));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 2));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();

			Assertions.assertEquals(2, cargoModel.getCargoes().size());
			final Cargo periodCargo2 = cargoModel.getCargoes().get(1);
			// Make sure we have the right cargo
			Assertions.assertEquals(cargo2.getLoadName(), periodCargo2.getLoadName());

			// Expect no change to cargo flags
			Assertions.assertFalse(periodCargo2.isLocked());
			Assertions.assertTrue(periodCargo2.isAllowRewiring());

			// Check load slot
			final Slot<?> loadSlot = periodCargo2.getSortedSlots().get(0);
			// No change to load slot
			Assertions.assertFalse(loadSlot.isLocked());
			Assertions.assertEquals(1, loadSlot.getWindowSize());
			Assertions.assertTrue(loadSlot.getRestrictedVessels().isEmpty());

			// Check discharge slot
			final Slot<?> dischargeSlot = periodCargo2.getSortedSlots().get(1);
			// Vessel locked
			Assertions.assertTrue(dischargeSlot.isLocked());
			Assertions.assertEquals(((VesselCharter) periodCargo2.getVesselAssignmentType()).getVessel(), dischargeSlot.getRestrictedVessels().get(0));
			// No window change as in the AFTER
			Assertions.assertEquals(1, dischargeSlot.getWindowSize());

		}, null);
	}

	/**
	 * Load slot is IN the BEFORE boundary and the discharge window is partly IN. We expect the cargo to be rewirable, but the load should be locked down.
	 */
	@Test
	public void testCargoBEFOREtoIN() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		final DESSalesMarket sellMarket = spotMarketsModelBuilder.makeDESSaleMarket("m1", portFinder.findPortById(InternalDataConstants.PORT_SAKAI), entity, "7").build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 10, 31), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeMarketDESSale("D2", sellMarket, YearMonth.of(2022, 1)) //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2021, 11, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 2));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();

			Assertions.assertEquals(2, cargoModel.getCargoes().size());
			final Cargo periodCargo1 = cargoModel.getCargoes().get(0);
			// Make sure we have the right cargo
			Assertions.assertEquals(cargo1.getLoadName(), periodCargo1.getLoadName());

			// Cargo should be locked
			Assertions.assertFalse(periodCargo1.isLocked());
			Assertions.assertTrue(periodCargo1.isAllowRewiring());
			// Check load slot

			final Slot<?> loadSlot = periodCargo1.getSortedSlots().get(0);
			// Locked to vessel
			Assertions.assertTrue(loadSlot.isLocked());
			Assertions.assertEquals(((VesselCharter) periodCargo1.getVesselAssignmentType()).getVessel(), loadSlot.getRestrictedVessels().get(0));
			// Window should be "locked"
			Assertions.assertEquals(0, loadSlot.getWindowSize());

			// Check discharge slot
			final Slot<?> dischargeSlot = periodCargo1.getSortedSlots().get(1);
			// Unlocked discharge
			Assertions.assertEquals(2, dischargeSlot.getWindowSize());
			Assertions.assertFalse(dischargeSlot.isLocked());
			Assertions.assertTrue(dischargeSlot.getRestrictedVessels().isEmpty());

		}, null);
	}
}
