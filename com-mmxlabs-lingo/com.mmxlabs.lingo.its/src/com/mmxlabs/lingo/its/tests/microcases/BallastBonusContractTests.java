/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.SlotInsertionOptimiserUnit;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class BallastBonusContractTests extends AbstractMicroTestCase {
	@Override
	protected @NonNull ExecutorService createExecutorService() {
		return Executors.newFixedThreadPool(4);
	}

	@Override
	public IScenarioDataProvider importReferenceData() throws MalformedURLException {
		return importReferenceData("/referencedata/reference-data-simple-distances/");
	}

	@Test
	@Category({ MicroTest.class })
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
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), 0);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173);
		});
	}

	@Test
	@Category({ MicroTest.class })
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
		final BallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "1000000");
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), -1_000_000);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 1_000_000);
		});
	}

	@Test
	@Category({ MicroTest.class })
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
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), 0);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 0);
		});
	}

	@Test
	@Category({ MicroTest.class })
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
				20.0, "20000", "100", true, Lists.newArrayList(portFinder.findPort("Bonny Nigeria")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(-62_499, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 62_499);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNotionalJourneyBallastBonusOn_Matching_FindBestOption() throws Exception {

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
				20.0, "20000", "100", true, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(-62_499, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 62_499);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotMarketsLumpSumBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setCapacity(140_000);
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 1);		
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
		s.setEntity(entity);
		charterInMarket_1.setCharterContract(s);
		evaluateTest(null, null, scenarioRunner -> {
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), -2_000_000);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 3_927_658 - 2_000_000);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotMarketsLumpSumBallastBonusOn_NotMatching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setCapacity(140_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 1);
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
		s.setEntity(entity);
		charterInMarket_1.setCharterContract(s);
		evaluateTest(null, null, scenarioRunner -> {
			final EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), 0_000_000);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 3_927_658);
		});
	}

	private StartEvent getStartEvent(final VesselAvailability vesselAvailability) {
		final Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getVesselAvailability().equals(vesselAvailability)).findFirst().get();
		final Event event = sequence.getEvents().get(0);
		assert event instanceof StartEvent;
		return (StartEvent) event;
	}

	private EndEvent getEndEvent(final VesselAvailability vesselAvailability) {
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