/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NextPortType;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(ShiroRunner.class)
public class MonthlyBallastBonusContractTests extends AbstractLegacyMicroTestCase {
	
	protected static final String TEST_CHARTER_CURVE_NAME = "TestCharterCurve";
	
	@Override
	protected int getThreadCount() {
		return 4;
	}

	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {
		return importReferenceData("/referencedata/reference-data-simple-distances/");
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
	public void testMultipleHubsFindCheapest() throws Exception {

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
		Port portSakai = portFinder.findPort("Sakai");
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 12), YearMonth.of(2016, 1), YearMonth.of(2016, 2) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB };
		String[] pctFuelRates = new String[] { "10", "75", "0" };
		String[] pctCharterRates = new String[] { "25", "0", "95" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")),
				 yms, nextPortTypes, pctFuelRates, pctCharterRates);
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			
			final long endEventPNL = -12_499;
			final long actualPnL = end.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(endEventPNL, actualPnL);
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPastCurvesDate() throws Exception {
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
		Port portSakai = portFinder.findPort("Sakai");

		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 9), YearMonth.of(2015, 10), YearMonth.of(2015, 11) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB };
		String[] pctFuelRates = new String[] { "10", "75", "100" };
		String[] pctCharterRates = new String[] { "25", "0", "100" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")),
				yms, nextPortTypes, pctFuelRates, pctCharterRates);
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
			final long actualPnL = end.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(endEventPNL, actualPnL);
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	
	public @NonNull RuleBasedBallastBonusContract createMonthlyBallastBonusContract(final @NonNull Collection<@NonNull APortSet<Port>> redeliveryPorts, final double speed,
			final @NonNull String hireExpression, final @NonNull String fuelExpression, final boolean includeCanalFees, final boolean includeCanalTime,
			final @NonNull Collection<@NonNull APortSet<Port>> returnPorts,
			YearMonth[] months, NextPortType[] nextPortTypes, String[] pctFuelRates, String[] pctCharterRates) {
		final MonthlyBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContract();
		ballastBonusContract.getHubs().addAll(returnPorts);
		
		for (int i = 0; i < months.length; i++) {
			final MonthlyBallastBonusContractLine monthlyBBLine = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContractLine();
			YearMonth ym = months[i];
			NextPortType nextPortType = nextPortTypes[i];
			String pctFuelRate = pctFuelRates[i];
			String pctCharterRate = pctCharterRates[i];
			monthlyBBLine.setMonth(ym);
			monthlyBBLine.setBallastBonusTo(nextPortType);
			monthlyBBLine.setBallastBonusPctFuel(pctFuelRate);
			monthlyBBLine.setBallastBonusPctCharter(pctCharterRate);

			monthlyBBLine.getRedeliveryPorts().addAll(redeliveryPorts);
			monthlyBBLine.getReturnPorts().addAll(returnPorts);
			monthlyBBLine.setFuelPriceExpression(fuelExpression);
			monthlyBBLine.setHirePriceExpression(hireExpression);
			monthlyBBLine.setIncludeCanal(includeCanalFees);
			monthlyBBLine.setIncludeCanalTime(includeCanalTime);
			monthlyBBLine.setSpeed(speed);
			
			ballastBonusContract.getRules().add(monthlyBBLine);
		}

		return ballastBonusContract;
	}

	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testReturnToLoadPort() throws Exception {

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
		Port portSakai = portFinder.findPort("Sakai");
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 9), YearMonth.of(2015, 10), YearMonth.of(2015, 11) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.LOAD_PORT };
		String[] pctFuelRates = new String[] { "10", "75", "100" };
		String[] pctCharterRates = new String[] { "25", "0", "100" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")),
				 yms, nextPortTypes, pctFuelRates, pctCharterRates);
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			
			final long endEventPNL = -62_499;
			final long actualPnL = end.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(endEventPNL, actualPnL);
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMixedBBReturn() throws Exception {

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
		Port portSakai = portFinder.findPort("Sakai");
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 9), YearMonth.of(2015, 10), YearMonth.of(2015, 11) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.NEAREST_HUB };
		String[] pctFuelRates = new String[] { "10", "75", "100" };
		String[] pctCharterRates = new String[] { "25", "0", "100" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPort("Sakai"))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPort("Bonny Nigeria"), portFinder.findPort("Yung An")),
				 yms, nextPortTypes, pctFuelRates, pctCharterRates);
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			
			final long endEventPNL = -62_499;
			final long actualPnL = end.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(endEventPNL, actualPnL);
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
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