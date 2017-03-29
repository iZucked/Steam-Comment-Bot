/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoMaker;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.SlotInsertionOptimiserUnit;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
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
	public LNGScenarioModel importReferenceData() throws MalformedURLException {
		return importReferenceData("/referencedata/reference-data-simple-distances/");
	}

	@Test
	@Category({ MicroTest.class })
	public void testLumpSumBallastBonusOff() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p->p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		evaluateTest(null, null, scenarioRunner -> {
			EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), 0);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLumpSumBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p->p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		BallastBonusContract ballastBonusContract = createLumpSumBallastBonusContract(lngScenarioModel, portFinder.findPort("Sakai"));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), -1_000_000);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 1_000_000);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLumpSumBallastBonusOn_NotMatching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p->p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		BallastBonusContract ballastBonusContract = createLumpSumBallastBonusContract(lngScenarioModel, portFinder.findPort("Point Fortin"));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(end.getGroupProfitAndLoss().getProfitAndLoss(), 0);
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 0);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNotionalJourneyBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		
		VesselStateAttributes ballastAttributes = vesselClass.getBallastAttributes();
		EList<FuelConsumption> fuelConsumption = ballastAttributes.getFuelConsumption();
		fuelConsumption.clear();
		FuelConsumption fc1 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc1.setSpeed(10);
		fc1.setConsumption(50);
		FuelConsumption fc2 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc2.setSpeed(15);
		fc2.setConsumption(80);
		FuelConsumption fc3 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc3.setSpeed(20);
		fc3.setConsumption(100);
		
		fuelConsumption.add(fc1); fuelConsumption.add(fc2); fuelConsumption.add(fc3);
		vesselClass.setMaxSpeed(20);
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p->p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p->System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		BallastBonusContract ballastBonusContract = createNotionalJourneyBallastBonusContract(lngScenarioModel, Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))), 20.0, "20000", "100", true, Lists.newArrayList(portFinder.findPort("Bonny Nigeria")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(-62_499, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 62_499);
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void testNotionalJourneyBallastBonusOn_Matching_FindBestOption() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		
		VesselStateAttributes ballastAttributes = vesselClass.getBallastAttributes();
		EList<FuelConsumption> fuelConsumption = ballastAttributes.getFuelConsumption();
		fuelConsumption.clear();
		FuelConsumption fc1 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc1.setSpeed(10);
		fc1.setConsumption(50);
		FuelConsumption fc2 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc2.setSpeed(15);
		fc2.setConsumption(80);
		FuelConsumption fc3 = FleetFactory.eINSTANCE.createFuelConsumption();
		fc3.setSpeed(20);
		fc3.setConsumption(100);
		
		fuelConsumption.add(fc1); fuelConsumption.add(fc2); fuelConsumption.add(fc3);
		vesselClass.setMaxSpeed(20);
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();
		CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p->p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p->System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		BallastBonusContract ballastBonusContract = createNotionalJourneyBallastBonusContract(lngScenarioModel, Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))), 20.0, "20000", "100", true, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")));
		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		evaluateTest(null, null, scenarioRunner -> {
			EList<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			EndEvent end = getEndEvent(vesselAvailability);
			Assert.assertEquals(-62_499, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals(ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()), 4_988_173 - 62_499);
		});
	}
	private StartEvent getStartEvent(final VesselAvailability vesselAvailability) {
		Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getVesselAvailability().equals(vesselAvailability)).findFirst().get();
		Event event = sequence.getEvents().get(0);
		assert event instanceof StartEvent;
		return (StartEvent) event;
	}
	
	private EndEvent getEndEvent(final VesselAvailability vesselAvailability) {
		Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getVesselAvailability().equals(vesselAvailability)).findFirst().get();
		Event event = sequence.getEvents().get(sequence.getEvents().size()-1);
		assert event instanceof EndEvent;
		return (EndEvent) event;
	}

	
	RuleBasedBallastBonusContract createLumpSumBallastBonusContract(LNGScenarioModel model, Port redeliveryPort) {
		RuleBasedBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
		LumpSumBallastBonusContractLine lumpSumBallastBonusContractLine = CommercialFactory.eINSTANCE.createLumpSumBallastBonusContractLine();
		lumpSumBallastBonusContractLine.getRedeliveryPorts().add(redeliveryPort);
		lumpSumBallastBonusContractLine.setPriceExpression("1000000");
		ballastBonusContract.getRules().add(lumpSumBallastBonusContractLine);
		return ballastBonusContract;
	}
	
	RuleBasedBallastBonusContract createNotionalJourneyBallastBonusContract(LNGScenarioModel model, Collection<APortSet<Port>> redeliveryPorts, double speed, String hireExpression, String fuelExpression, boolean includeCanal, Collection<APortSet<Port>> returnPorts) {
		RuleBasedBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
		NotionalJourneyBallastBonusContractLine notionalJourneyBallastBonusContractLine = CommercialFactory.eINSTANCE.createNotionalJourneyBallastBonusContractLine();
		notionalJourneyBallastBonusContractLine.getRedeliveryPorts().addAll(redeliveryPorts);
		notionalJourneyBallastBonusContractLine.getReturnPorts().addAll(returnPorts);
		notionalJourneyBallastBonusContractLine.setFuelPriceExpression(fuelExpression);
		notionalJourneyBallastBonusContractLine.setHirePriceExpression(hireExpression);
		notionalJourneyBallastBonusContractLine.setIncludeCanal(includeCanal);
		notionalJourneyBallastBonusContractLine.setSpeed(speed);
		ballastBonusContract.getRules().add(notionalJourneyBallastBonusContractLine);
		return ballastBonusContract;
	}

}