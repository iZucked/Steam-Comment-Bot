/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
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
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
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
		return importReferenceData("/referencedata/reference-data-2/");
	}	
	
	@BeforeEach
	public void setSimpleDistances() {
		distanceModelBuilder.setAllDistances(RouteOption.DIRECT, 1000.0);
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

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		Port portSakai = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 12), YearMonth.of(2016, 1), YearMonth.of(2016, 2) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB };
		String[] pctFuelRates = new String[] { "10", "75", "0" };
		String[] pctCharterRates = new String[] { "25", "0", "95" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)),
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
	public void testPeriodOptimisation() throws Exception {

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
		vessel.setMaxSpeed(19.5);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 11, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 11, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 4, 6, 0, 0, 0, 0))//
				.build();
		
		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 11, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final LoadSlot load_FOB2 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2016, 2, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 3, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		final Cargo cargo2 = cargoModelBuilder.createCargo(load_FOB2, discharge_DES2);

		cargo.setVesselAssignmentType(vesselAvailability);
		cargo2.setVesselAssignmentType(vesselAvailability);
		Port portSakai = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 11), YearMonth.of(2015, 12), YearMonth.of(2016, 1), YearMonth.of(2016, 2) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.LOAD_PORT };
		String[] pctFuelRates = new String[] { "100", "10", "75", "0" };
		String[] pctCharterRates = new String[] { "100", "25", "0", "95" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)),
				 yms, nextPortTypes, pctFuelRates, pctCharterRates);
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);

		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setPeriodStartDate(LocalDate.of(2016, 2, 5));
		userSettings.setPeriodEnd(YearMonth.of(2016, 6));

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 10_000);

		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.withOptimiseHint() //
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = runner.getScenarioRunner().getScenarioToOptimiserBridge();

			final LNGScenarioModel scenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final @Nullable Schedule schedule = runner.getScenarioRunner().getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			final CargoAllocation cargoAllocation2 = ScheduleTools.findCargoAllocation(cargo2.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation2);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss() +
					cargoAllocation2.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselAvailability);
			
			final long endEventPNL = -62_499;
			final long actualPnL = end.getGroupProfitAndLoss().getProfitAndLoss();

			System.out.println("Actual PnL period opti = "+actualPnL);
			Assertions.assertEquals(endEventPNL, actualPnL);
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		} finally {
			runner.dispose();
		}
	}


	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMultiCargoes() throws Exception {

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
		vessel.setMaxSpeed(19.5);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 11, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 11, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 4, 6, 0, 0, 0, 0))//
				.build();
		
		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 11, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final LoadSlot load_FOB2 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2016, 2, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 3, 5), 
				portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		final Cargo cargo2 = cargoModelBuilder.createCargo(load_FOB2, discharge_DES2);

		cargo.setVesselAssignmentType(vesselAvailability);
		cargo2.setVesselAssignmentType(vesselAvailability);
		Port portSakai = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 11), YearMonth.of(2015, 12), YearMonth.of(2016, 1), YearMonth.of(2016, 2) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.LOAD_PORT };
		String[] pctFuelRates = new String[] { "100", "10", "75", "0" };
		String[] pctCharterRates = new String[] { "100", "25", "0", "95" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)),
				 yms, nextPortTypes, pctFuelRates, pctCharterRates);
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			final CargoAllocation cargoAllocation2 = ScheduleTools.findCargoAllocation(cargo2.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation2);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss() +
					cargoAllocation2.getGroupProfitAndLoss().getProfitAndLoss();

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

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		Port portSakai = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);

		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 9), YearMonth.of(2015, 10), YearMonth.of(2015, 11) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB, NextPortType.NEAREST_HUB };
		String[] pctFuelRates = new String[] { "10", "75", "100" };
		String[] pctCharterRates = new String[] { "25", "0", "100" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)),
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

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		Port portSakai = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 9), YearMonth.of(2015, 10), YearMonth.of(2015, 11) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.LOAD_PORT };
		String[] pctFuelRates = new String[] { "10", "75", "100" };
		String[] pctCharterRates = new String[] { "25", "0", "100" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)),
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

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		final CapabilityGroup allDischarge = portFinder.getPortModel().getSpecialPortGroups().stream().filter(p -> p.getName().equals("All DISCHARGE Ports")).findFirst().get();
		portFinder.getPortModel().getPorts().forEach(p -> System.out.println(p.getName()));
		vesselAvailability.getEndAt().add(allDischarge);
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselAvailability);
		Port portSakai = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);
		
		YearMonth[] yms = new YearMonth[] { YearMonth.of(2015, 9), YearMonth.of(2015, 10), YearMonth.of(2015, 11) };
		NextPortType[] nextPortTypes = new NextPortType[] { NextPortType.LOAD_PORT, NextPortType.LOAD_PORT, NextPortType.NEAREST_HUB };
		String[] pctFuelRates = new String[] { "10", "75", "100" };
		String[] pctCharterRates = new String[] { "25", "0", "100" };
		final BallastBonusContract ballastBonusContract = this.createMonthlyBallastBonusContract(Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))),
				20.0, "20000", "100", true, false, Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)),
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