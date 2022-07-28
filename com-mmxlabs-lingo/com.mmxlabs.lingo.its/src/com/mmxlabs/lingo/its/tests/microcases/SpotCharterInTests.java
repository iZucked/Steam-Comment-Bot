/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.ToLongFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
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

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);
		final CargoAllocation caForSpot = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);
		final CargoAllocation caForTerm = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);
		final CargoAllocation caForCharter = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForCharter);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

		Assertions.assertEquals(pnlForSpot, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForSpot);

	}

	/**
	 * The basic test with the minimal data required to test the equivalence. This
	 * should be a simple return to load, no start or end event P&L, with the safety
	 * heel for start and end conditions.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSimpleCaseExtraCargo() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);
		final CargoAllocation caForSpot = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);
		final CargoAllocation caForTerm = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);
		final CargoAllocation caForCharter = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForCharter);

		final LoadSlot loadFOB2 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final Cargo cargo2 = cargoModelBuilder.createCargo(loadFOB2, dischargeDES2);

		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		cargo2.setVesselAssignmentType(market);
		cargo2.setSpotIndex(-1);
		final Schedule scheduleForSpot2 = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot2);
		final CargoAllocation caForSpot2 = ScheduleTools.findCargoAllocation(cargo2.getLoadName(), scheduleForSpot2);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForSpot2 = totalPNL.applyAsLong(scheduleForSpot2);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

		Assertions.assertEquals(pnlForSpot * 2, pnlForSpot2);
		Assertions.assertEquals(pnlForSpot, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForSpot);

	}

	/**
	 * Simple case but ending at the discharge port rather than return to load.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEndAtDischargeCase() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6); // To match vessel start safety heel

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

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

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);
		market.setStartHeelCV(22.6);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		market.unsetStartAt();
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		final Schedule scheduleForSpotNoStart = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpotNoStart);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForSpotNoStart = totalPNL.applyAsLong(scheduleForSpotNoStart);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertNotEquals(0, pnlForCharter);

		// Spot case should be the same as no start port at all
		Assertions.assertEquals(pnlForSpotNoStart, pnlForSpot);
		// Spot(nominal) will ignore the start port and thus we expect a different P&L
		Assertions.assertNotEquals(pnlForCharter, pnlForSpot);

		// Term charters and real charters should be equivalent
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithContractCase() throws IOException {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.build(true);
//		market.setGenericCharterContract(charterContract);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);

		market.setGenericCharterContract(charterContract);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				// .withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match
				// default spot market rules
				.build();

		charter.setGenericCharterContract(charterContract);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 200000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withVolumeLimits(2_000_000, 2_100_000, VolumeUnits.MMBTU) //
				.build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case, return to load
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);
//
		Assertions.assertEquals(pnlForCharter, pnlForTerm);
		Assertions.assertEquals(pnlForCharter, pnlForSpot);
		Assertions.assertEquals(pnlForTerm, pnlForSpot);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithContractCaseAndEndPort() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);

		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.build(true);
		market.setGenericCharterContract(charterContract);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				// .withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match
				// default spot market rules
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		Assertions.assertEquals(pnlForCharter, pnlForTerm);

		Assertions.assertEquals(pnlForCharter, pnlForSpot);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithContractCaseAndEndPortWithBB() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);

		final long ballastBonus = 1_000_000;
		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardBallastBonus() //
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000)) //
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(700_000)) //
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), Long.toString(ballastBonus)) //
				.addLumpSumRule((Port) null, Long.toString(2_000_000)) //
				.build() // BB
				.build(true);
		market.setGenericCharterContract(charterContract);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				// .withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match
				// default spot market rules
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		{
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForSpot);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(ballastBonus, cargoAllocation.getBallastBonusFee());
		}
		{
			EndEvent evt = ScheduleModelUtils.getEndEvent(market, 0, scheduleForTerm);
			Assertions.assertEquals(ballastBonus, evt.getBallastBonusFee());
		}
		{
			EndEvent evt = ScheduleModelUtils.getEndEvent(charter, scheduleForCharter);
			Assertions.assertEquals(ballastBonus, evt.getBallastBonusFee());
		}

		Assertions.assertEquals(pnlForSpot, pnlForTerm);

		Assertions.assertEquals(pnlForCharter, pnlForSpot);
		Assertions.assertEquals(pnlForCharter, pnlForTerm);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithContractCaseAndEndPortWithReposAndBB() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 1);
		market.setNominal(true);

		final long repositioningFee = 1_750_000;
		final long ballastBonus = 1_000_000;
		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardRepositioning() //
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000))
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_COLON), Long.toString(700_000))
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), Long.toString(900_000))
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(repositioningFee))
				.addLumpSumRuleForSingleStart((Port) null, Long.toString(2_000_000)) //
				.build() // Repos
				//
				.withStandardBallastBonus() //
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000)) //
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(700_000)) //
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), Long.toString(ballastBonus)) //
				.addLumpSumRule((Port) null, Long.toString(2_000_000)) //
				.build() // BB
				.build(true);
		market.setGenericCharterContract(charterContract);

		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
				.withCharterRate("80000") //
				.withEndWindow(LocalDateTime.of(2015, 1, 1, 0, 0, 0), null) // Set an end after to override the optimiser derived end date
				// .withSafeyStartHeel().withSafeyEndHeel() // Set the safety heel to match
				// default spot market rules
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
		final Schedule scheduleForSpot = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForSpot);

		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(0);
		final Schedule scheduleForTerm = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForTerm);

		cargo.setVesselAssignmentType(charter);
		cargo.setSpotIndex(0);
		final Schedule scheduleForCharter = evaluateAndReturnSchedule();
		Assertions.assertNotNull(scheduleForCharter);

		final long pnlForCharter = totalPNL.applyAsLong(scheduleForCharter);
		final long pnlForSpot = totalPNL.applyAsLong(scheduleForSpot);
		final long pnlForTerm = totalPNL.applyAsLong(scheduleForTerm);

		{
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), scheduleForSpot);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(repositioningFee, cargoAllocation.getRepositioningFee());
			Assertions.assertEquals(ballastBonus, cargoAllocation.getBallastBonusFee());
		}
		{
			StartEvent start = ScheduleModelUtils.getStartEvent(market, 0, scheduleForTerm);
			EndEvent end = ScheduleModelUtils.getEndEvent(market, 0, scheduleForTerm);
			Assertions.assertEquals(repositioningFee, start.getRepositioningFee());
			Assertions.assertEquals(ballastBonus, end.getBallastBonusFee());
		}
		{
			StartEvent start = ScheduleModelUtils.getStartEvent(charter, scheduleForCharter);
			EndEvent end = ScheduleModelUtils.getEndEvent(charter, scheduleForCharter);
			Assertions.assertEquals(repositioningFee, start.getRepositioningFee());
			Assertions.assertEquals(ballastBonus, end.getBallastBonusFee());
		}

		// Delta of $1 - seems to be some rounding with all the P&L in the same object
		// vs split start,cargo,end
		Assertions.assertEquals(pnlForSpot, pnlForTerm, 1);

		Assertions.assertEquals(pnlForCharter, pnlForSpot, 1);
		Assertions.assertEquals(pnlForCharter, pnlForTerm);

	}
}