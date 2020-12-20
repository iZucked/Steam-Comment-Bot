/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(ShiroRunner.class)
public class BallastBonusContractTests extends AbstractLegacyMicroTestCase {

	protected static final String TEST_CHARTER_CURVE_NAME = "TestCharterCurve";
	
	@Override
	protected int getThreadCount() {
		return 4;
	}

	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {
		return importReferenceData("/referencedata/reference-data-simple-distances/");
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusOff() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assertions.assertEquals(0, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		
		createCharterPriceCurve();
		
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), TEST_CHARTER_CURVE_NAME);
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			final long endEventPNL = -1_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	protected CharterCurve createCharterPriceCurve() {
		return pricingModelBuilder.makeCharterDataCurve(TEST_CHARTER_CURVE_NAME, "$", "mmBtu") 
				.addIndexPoint(YearMonth.of(2015, 12), 100000) 
				.addIndexPoint(YearMonth.of(2016, 1), 500000) 
				.addIndexPoint(YearMonth.of(2016, 2), 1000000)
				.build();
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusOn_NotMatching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Point Fortin"), "1000000");
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assertions.assertEquals(0, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNotionalJourneyBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselStateAttributes ballastAttributes = vessel.getBallastAttributes();
		final EList<FuelConsumption> fuelConsumption = ballastAttributes.getVesselOrDelegateFuelConsumption();
		fuelConsumption.clear();
		final FuelConsumption fc1 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc1.setSpeed(10);
		fc1.setConsumption(50);
		final FuelConsumption fc2 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc2.setSpeed(15);
		fc2.setConsumption(80);
		final FuelConsumption fc3 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc3.setSpeed(20);
		fc3.setConsumption(100);

		fuelConsumption.add(fc1);
		fuelConsumption.add(fc2);
		fuelConsumption.add(fc3);
		vessel.setMaxSpeed(20);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPort("Bonny Nigeria")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			long endEventPNL = -62_499;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNotionalJourneyBallastBonusOn_SuezCanal() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselStateAttributes ballastAttributes = vessel.getBallastAttributes();
		final EList<FuelConsumption> fuelConsumption = ballastAttributes.getVesselOrDelegateFuelConsumption();
		fuelConsumption.clear();
		final FuelConsumption fc1 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc1.setSpeed(10);
		fc1.setConsumption(50);
		final FuelConsumption fc2 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc2.setSpeed(15);
		fc2.setConsumption(80);
		final FuelConsumption fc3 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc3.setSpeed(20);
		fc3.setConsumption(100);

		fuelConsumption.add(fc1);
		fuelConsumption.add(fc2);
		fuelConsumption.add(fc3);
		vessel.setMaxSpeed(20);

		distanceModelBuilder.setPortToPortDistance(portFinder.findPort("Sakai"), portFinder.findPort("Bonny Nigeria"), RouteOption.DIRECT, 1008, true);
		distanceModelBuilder.setPortToPortDistance(portFinder.findPort("Sakai"), portFinder.findPort("Bonny Nigeria"), RouteOption.SUEZ, 96, true);
		distanceModelBuilder.setPortToPortDistance(portFinder.findPort("Sakai"), portFinder.findPort("Bonny Nigeria"), RouteOption.PANAMA, 800, true);

		this.fleetModelBuilder.setRouteParameters(vessel, RouteOption.SUEZ, 90, 90, 90, 90, 36);
		this.fleetModelBuilder.setRouteParameters(vessel, RouteOption.PANAMA, 90, 90, 90, 90, 36);
		vessel.setRouteParametersOverride(true);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", false, true, Lists.newArrayList(portFinder.findPort("Bonny Nigeria")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {

			final EndEvent end = getEndEvent(vesselAvailability);
			
			for (GeneralPNLDetails details : end.getGeneralPNLDetails()) {
				if (details instanceof BallastBonusFeeDetails) {
					
					BallastBonusFeeDetails bbDetails = (BallastBonusFeeDetails)details;
					NotionalJourneyContractDetails cDetails = (NotionalJourneyContractDetails)bbDetails.getMatchingBallastBonusContractDetails();
					
					int totalFuelCost = cDetails.getTotalFuelCost();
					Assertions.assertEquals(15166, totalFuelCost);
				}
			}

			
			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			long endEventPNL = -48_499;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));			
			
		});
	}

	
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNotionalJourneyBallastBonusOn_Matching_FindBestOption() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselStateAttributes ballastAttributes = vessel.getBallastAttributes();
		final List<FuelConsumption> fuelConsumption = ballastAttributes.getVesselOrDelegateFuelConsumption();
		fuelConsumption.clear();
		final FuelConsumption fc1 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc1.setSpeed(10);
		fc1.setConsumption(50);
		final FuelConsumption fc2 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc2.setSpeed(15);
		fc2.setConsumption(80);
		final FuelConsumption fc3 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc3.setSpeed(20);
		fc3.setConsumption(100);

		fuelConsumption.add(fc1);
		fuelConsumption.add(fc2);
		fuelConsumption.add(fc3);
		vessel.setMaxSpeed(20);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			long endEventPNL = -62_499;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotMarketsLumpSumBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setCapacity(140_000);
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);
		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(charterInMarket_1);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "2000000");
		SimpleBallastBonusCharterContract s = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		s.setBallastBonusContract(ballastBonusContract);
		charterInMarket_1.setCharterContract(s);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1);
			long endEventPNL = -2_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotMarketsLumpSumBallastBonusOn_NotMatching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setCapacity(140_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);
		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(charterInMarket_1);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Point Fortin"), "2000000");
		SimpleBallastBonusCharterContract s = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		s.setBallastBonusContract(ballastBonusContract);
		charterInMarket_1.setCharterContract(s);
		evaluateTest(null, null, scenarioRunner -> {

			@Nullable
			final Schedule schedule = scenarioRunner.getSchedule();

			assert schedule != null;
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1);
			Assertions.assertEquals(0, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterContractLumpSumBallastBonusOn_Matching() throws Exception {
		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "2000000");
		SimpleBallastBonusCharterContract s = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		s.setBallastBonusContract(ballastBonusContract);
		vesselAvailability.setCharterContract(s);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			long endEventPNL = -2_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterContractLumpSumBallastBonusOn_NotMatching() throws Exception {
		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Point Fortin"), "2000000");
		SimpleBallastBonusCharterContract s = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		s.setBallastBonusContract(ballastBonusContract);
		vesselAvailability.setCharterContract(s);
		evaluateTest(null, null, scenarioRunner -> {
			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assertions.assertEquals(0L, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	/**
	 * Show that cargo is optimised on to charter with charter in contract
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void sanityCheckTestCharterContractOptimises() throws Exception {
		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");
		@NonNull
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "2000000");
		SimpleBallastBonusCharterContract s = CommercialFactory.eINSTANCE.createSimpleBallastBonusCharterContract();
		s.setBallastBonusContract(ballastBonusContract);
		charterInMarket_1.setCharterContract(s);
		optimiseWithLSOTest(scenarioRunner -> {
			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1);
			int endEventPNL = -2_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
		});
	}

	private StartEvent getStartEvent(final VesselAvailability vesselAvailability) {
		final Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getVesselAvailability().equals(vesselAvailability)).findFirst().get();
		final Event event = sequence.getEvents().get(0);
		assert event instanceof StartEvent;
		return (StartEvent) event;
	}

	protected EndEvent getEndEvent(final VesselAvailability vesselAvailability) {
		final Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getVesselAvailability().equals(vesselAvailability)).findFirst().get();
		final Event event = sequence.getEvents().get(sequence.getEvents().size() - 1);
		assert event instanceof EndEvent;
		return (EndEvent) event;
	}

	private EndEvent getEndEvent(final CharterInMarket charterInMarket) {
		final Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getCharterInMarket().equals(charterInMarket)).findFirst().get();
		final Event event = sequence.getEvents().get(sequence.getEvents().size() - 1);
		assert event instanceof EndEvent;
		return (EndEvent) event;
	}

}