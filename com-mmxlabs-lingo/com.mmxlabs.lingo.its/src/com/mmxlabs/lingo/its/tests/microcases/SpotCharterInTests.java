/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.ToLongFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * Test computation of a single cargo on the different vessel type produces the
 * same results. Note, the nominal/spot mode combines the P&L of the start and
 * end events into the cargo.
 * 
 * @author Simon Goodall
 *
 */
public class SpotCharterInTests extends AbstractMicroTestCase {

	private static ToLongFunction<Schedule> totalPNL = s -> {
		return // Part 1, event P&L
		s.getSequences().stream() //
				.flatMap(seq -> seq.getEvents().stream()) //
				.filter(ProfitAndLossContainer.class::isInstance) //
				.map(ProfitAndLossContainer.class::cast) //
				.map(ProfitAndLossContainer::getGroupProfitAndLoss) //
				.mapToLong(GroupProfitAndLoss::getProfitAndLoss) //
				.sum() //
				//
				+
		// Part 2, cargo P&L
		s.getCargoAllocations().stream() //
				.map(ProfitAndLossContainer.class::cast) //
				.map(ProfitAndLossContainer::getGroupProfitAndLoss) //
				.mapToLong(GroupProfitAndLoss::getProfitAndLoss) //
				.sum();
	};

	/**
	 * The basic test with the minimal data required to test the equivalence. This
	 * should be a simple return to load, no start or end event P&L, with the safety
	 * heel for start and end conditions.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSimpleCase() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6);
		
		VesselAvailability charter = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				.withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match default spot market rules
				.build();

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);

		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

		Assertions.assertEquals(pnlForSpot, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForSpot);

	}

	/**
	 * Simple case but ending at the discharge port rather than return to load.
	 */
	@Disabled
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEndAtDischargeCase() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6); // To match vessel start safety heel

		VesselAvailability charter = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				.withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match default spot market rules
				.build();

		// End at any discharge port
		market.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));
		charter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

		Assertions.fail("Fails as market end port is ignored if there is no contract");
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForSpot);
	}

	/**
	 * Simple case but with a specific start port. Note spot/nominal options will
	 * ignore the start port.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartAtOtherCase() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6);

		VesselAvailability charter = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				.withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match default spot market rules
				.build();

		// Start at an alternative port
		market.setStartAt(portFinder.findPortById(InternalDataConstants.PORT_FUTTSU));
		charter.setStartAt(portFinder.findPortById(InternalDataConstants.PORT_FUTTSU));

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		market.unsetStartAt();
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		Schedule scheduleForSpotNoStart = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpotNoStart);

		long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		long pnlForSpotNoStart = totalPNL.applyAsLong(scheduleForSpotNoStart);
		long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

		// Spot case should be the same as no start port at all
		Assertions.assertEquals(pnlForSpotNoStart, pnlForSpot);
		// Spot(nominal) will ignore the start port and thus we expect a different P&L
		Assertions.assertNotEquals(pnlForCharter, pnlForSpot);

		// Term charters and real charters should be equivalent
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
	}

	@Disabled
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithContractCase() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final GenericCharterContract charterContract = CommercialFactory.eINSTANCE.createGenericCharterContract();

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);

		market.setGenericCharterContract(charterContract);

		VesselAvailability charter = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				.withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match default spot market rules
				.build();

		charter.setGenericCharterContract(charterContract);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.fail("Fails as market end ports defaults to all discharge ports if there is a contract");
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForSpot);
	}
	
	@Disabled
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithContractCaseAndEndPort() {
		
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final GenericCharterContract charterContract = CommercialFactory.eINSTANCE.createGenericCharterContract();
		
		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		
		market.setGenericCharterContract(charterContract);
		
		VesselAvailability charter = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				.withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match default spot market rules
				.build();
		
		charter.setGenericCharterContract(charterContract);
		

		// End at any discharge port
		market.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));
		charter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		
		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		
		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		
		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);
		
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);
		
		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);
		
		long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);
		
		Assertions.assertEquals(pnlForCharter, pnlForTerm);

		Assertions.fail("Fails as market end ports defaults to all discharge ports if there is a contract");
		Assertions.assertEquals(pnlForCharter, pnlForSpot);
	}
}