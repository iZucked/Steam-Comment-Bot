/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * 
 * @author achurchill
 *
 */
@ExtendWith(ShiroRunner.class)
public class RepositioningFeeContractTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_CargoOnOptionalVesselNoRepositioning() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setMaxSpeed(15.0);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;
			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			StartEvent start = getStartEvent(vesselCharter);
			Assertions.assertEquals(0, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_CargoOnOptionalVesselRepositioning() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withOptionality(true) //
				.withRepositioning("1000000") //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			StartEvent start = getStartEvent(vesselCharter);
			Assertions.assertEquals(-1_000_000, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL - 1_000_000, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_LumpSumRepositioningOnMatchSingleStart() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI))
				.withStartWindow(LocalDateTime.of(2015, 11, 1, 0, 0, 0, 0), LocalDateTime.of(2015, 11, 15, 0, 0, 0, 0))//
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2016, 1, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();
		
		final GenericCharterContract repositioningContract = commercialModelBuilder
				.createSimpleLumpSumRepositioningContract(portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI),
				"100000");
		vesselCharter.setGenericCharterContract(repositioningContract);

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			StartEvent start = getStartEvent(vesselCharter);
			Assertions.assertEquals(-1_535_733, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL - 1_535_733, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
			
			if (start.getGeneralPNLDetails().get(0) instanceof CharterContractFeeDetails gcfd){
				Assertions.assertEquals(100_000, gcfd.getFee());
			}
		});
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_LumpSumRepositioningOnMatchMultiplePortsStart() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI))
				.withStartWindow(LocalDateTime.of(2015, 11, 1, 0, 0, 0, 0), LocalDateTime.of(2015, 11, 15, 0, 0, 0, 0))//
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2016, 1, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();
		
		final GenericCharterContract repositioningContract = commercialModelBuilder
				.createSimpleLumpSumRepositioningContract(Lists.newLinkedList(Lists.newArrayList(//
				portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI), //
				portFinder.findPortById(InternalDataConstants.PORT_BONNY))),
				"100000");
		vesselCharter.setGenericCharterContract(repositioningContract);

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			StartEvent start = getStartEvent(vesselCharter);
			Assertions.assertEquals(-1_535_733, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL - 1_535_733, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
			
			if (start.getGeneralPNLDetails().get(0) instanceof CharterContractFeeDetails gcfd){
				Assertions.assertEquals(100_000, gcfd.getFee());
			}
		});
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_LumpSumRepositioningOnNoMatch() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI))
				.withStartWindow(LocalDateTime.of(2015, 11, 1, 0, 0, 0, 0), LocalDateTime.of(2015, 11, 15, 0, 0, 0, 0))//
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2016, 1, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();
		
		final GenericCharterContract repositioningContract = commercialModelBuilder
				.createSimpleLumpSumRepositioningContract("100000");
		vesselCharter.setGenericCharterContract(repositioningContract);

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			StartEvent start = getStartEvent(vesselCharter);
			Assertions.assertEquals(-1_535_733, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL - 1_535_733, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
			
			if (start.getGeneralPNLDetails().get(0) instanceof CharterContractFeeDetails gcfd){
				Assertions.assertEquals(100_000, gcfd.getFee());
			}
		});
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_OriginRepositioningOnMatch() throws Exception {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI))
				.withStartWindow(LocalDateTime.of(2015, 11, 1, 0, 0, 0, 0), LocalDateTime.of(2015, 11, 15, 0, 0, 0, 0))//
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2016, 1, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();
		
		final GenericCharterContract repositioningContract = commercialModelBuilder
				.createSimpleOriginRepositioningContract(Lists.newLinkedList(Lists.newArrayList(//
				portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI), //
				portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN))), portFinder.findPortById(InternalDataConstants.PORT_BONNY), //
				15.0, "100000", "500", false, false, "0");
				
		vesselCharter.setGenericCharterContract(repositioningContract);

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			StartEvent start = getStartEvent(vesselCharter);
			Assertions.assertEquals(-1_435_733 - 2_762_812, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL - (1_435_733 + 2_762_812), ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
			
			if (start.getGeneralPNLDetails().get(0) instanceof CharterContractFeeDetails gcfd){
				Assertions.assertEquals(2_762_812, gcfd.getFee());
			}
		});
	}

	private StartEvent getStartEvent(final VesselCharter vesselCharter) {
		return ScheduleModelUtils.getStartEvent(vesselCharter, lngScenarioModel.getScheduleModel().getSchedule());
	}

}