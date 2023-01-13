/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.util.CharterContractBuilder.BallastBonusMaker;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.NotionalJourneyBallastBonusTermAnnotation;

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
		return importReferenceData("/referencedata/reference-data-2/");
	}

	@BeforeEach
	public void setSimpleDistances() {
		distanceModelBuilder.setAllDistances(RouteOption.DIRECT, 1000.0);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusOff() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();

		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);
		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
			Assertions.assertEquals(0, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		createCharterPriceCurve();

		final GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI),
				TEST_CHARTER_CURVE_NAME);
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
			final long endEventPNL = -1_000_000;
			Assertions.assertEquals(-endEventPNL, end.getBallastBonusFee());

			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	protected CharterCurve createCharterPriceCurve() {
		return pricingModelBuilder.makeCharterDataCurve(TEST_CHARTER_CURVE_NAME, "$", "mmBtu").addIndexPoint(YearMonth.of(2015, 12), 100000).addIndexPoint(YearMonth.of(2016, 1), 500000)
				.addIndexPoint(YearMonth.of(2016, 2), 1000000).build();
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusOn_NotMatching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), "1000000");
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
			Assertions.assertEquals(0, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNotionalJourneyBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
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

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(
				Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))), 20.0, "20000", "100", true, false,
				Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY)));
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
			long endEventPNL = -62_499;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNotionalJourneyBallastBonusOn_Matching_WithLNGPrice() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
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

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(
				Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))), 20.0, "20000", BallastBonusMaker.LNG_ONLY, true, false,
				Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY)));
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);

			boolean foundDetails = false;
			for (var detail : end.getGeneralPNLDetails()) {
				if (detail instanceof CharterContractFeeDetails contractDetails) {
					if (contractDetails.getMatchingContractDetails() instanceof NotionalJourneyBallastBonusTermDetails termDetails) {

						Assertions.assertEquals(7, termDetails.getLngPrice());
						// MT per day @ 20 knots * base fuel equiv * days
						Assertions.assertEquals(100.0 * vessel.getBaseFuel().getEquivalenceFactor() * termDetails.getTotalTimeInDays(), termDetails.getTotalLNGUsed(), 1.0);
						// Prev line can be out-by-one, so here out by 1*7
						Assertions.assertEquals(7.0 * termDetails.getTotalLNGUsed(), termDetails.getTotalLNGCost(), 7);
						
						Assertions.assertEquals(0, termDetails.getFuelPrice());
						Assertions.assertEquals(0, termDetails.getTotalFuelUsed());
						Assertions.assertEquals(0, termDetails.getTotalFuelCost());

						foundDetails = true;
						break;
					}

				}
			}
			Assertions.assertTrue(foundDetails);

//			long endEventPNL = -62_499;
//			
//			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
//			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNotionalJourneyBallastBonusOn_SuezCanal() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
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

		// Make sure curves cross the speed range.
		assert fc1.getSpeed() <= vessel.getMinSpeed();
		assert fc3.getSpeed() >= vessel.getMaxSpeed();

		distanceModelBuilder.setPortToPortDistance(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), portFinder.findPortById(InternalDataConstants.PORT_BONNY), RouteOption.DIRECT, 1008,
				true);
		distanceModelBuilder.setPortToPortDistance(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), portFinder.findPortById(InternalDataConstants.PORT_BONNY), RouteOption.SUEZ, 96, true);
		distanceModelBuilder.setPortToPortDistance(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), portFinder.findPortById(InternalDataConstants.PORT_BONNY), RouteOption.PANAMA, 800, true);

		this.fleetModelBuilder.setRouteParameters(vessel, RouteOption.SUEZ, 90, 90, 90, 90, 36);
		this.fleetModelBuilder.setRouteParameters(vessel, RouteOption.PANAMA, 90, 90, 90, 90, 36);
		vessel.setRouteParametersOverride(true);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(
				Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))), 20.0, "20000", "100", false, true,
				Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY)));

		vesselCharter.setGenericCharterContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final EndEvent end = getEndEvent(vesselCharter);

			for (GeneralPNLDetails details : end.getGeneralPNLDetails()) {
				if (details instanceof CharterContractFeeDetails bbDetails) {
					NotionalJourneyBallastBonusTermDetails cDetails = (NotionalJourneyBallastBonusTermDetails) bbDetails.getMatchingContractDetails();

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

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
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
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(
				Lists.newLinkedList(Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_SAKAI))), 20.0, "20000", "100", true, false,
				Lists.newArrayList(portFinder.findPortById(InternalDataConstants.PORT_BONNY), portFinder.findPortById(InternalDataConstants.PORT_YUNG_AN)));
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
			long endEventPNL = -62_499;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotMarketsLumpSumBallastBonusOn_Matching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setCapacity(140_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);
		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();

		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(charterInMarket_1);

		final GenericCharterContract s = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), "2000000");
		charterInMarket_1.setGenericCharterContract(s);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1, 0);
			long endEventPNL = -2_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotMarketsLumpSumBallastBonusOn_NotMatching() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setCapacity(140_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(charterInMarket_1);

		final GenericCharterContract bb = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), "2000000");
		charterInMarket_1.setGenericCharterContract(bb);

		evaluateTest(null, null, scenarioRunner -> {

			@Nullable
			final Schedule schedule = scenarioRunner.getSchedule();

			assert schedule != null;
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();

			final EndEvent end = getEndEvent(charterInMarket_1, 0);

			Assertions.assertEquals(0, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterContractLumpSumBallastBonusOn_Matching() throws Exception {
		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract s = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), "2000000");
		vesselCharter.setGenericCharterContract(s);

		evaluateTest(null, null, scenarioRunner -> {

			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
			long endEventPNL = -2_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertEquals(cargoPNL + endEventPNL, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterContractLumpSumBallastBonusOn_NotMatching() throws Exception {
		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 2, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		vesselCharter.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");
		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo.setVesselAssignmentType(vesselCharter);

		final GenericCharterContract s = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), "2000000");
		vesselCharter.setGenericCharterContract(s);

		evaluateTest(null, null, scenarioRunner -> {
			final @Nullable Schedule schedule = scenarioRunner.getSchedule();
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			final List<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			final EndEvent end = getEndEvent(vesselCharter);
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
		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		@NonNull
		final GenericCharterContract s = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), "2000000");
		charterInMarket_1.setGenericCharterContract(s);

		optimiseWithLSOTest(scenarioRunner -> {
			final List<SlotAllocation> slotAllocations = scenarioRunner.getSchedule().getSlotAllocations();
			final EndEvent end = getEndEvent(charterInMarket_1, 0);
			int endEventPNL = -2_000_000;
			Assertions.assertEquals(endEventPNL, end.getGroupProfitAndLoss().getProfitAndLoss());
		});
	}

	private StartEvent getStartEvent(final VesselCharter vesselCharter) {
		return ScheduleModelUtils.getStartEvent(vesselCharter, lngScenarioModel.getScheduleModel().getSchedule());
	}

	protected EndEvent getEndEvent(final VesselCharter vesselCharter) {
		return ScheduleModelUtils.getEndEvent(vesselCharter, lngScenarioModel.getScheduleModel().getSchedule());
	}

	private EndEvent getEndEvent(final CharterInMarket charterInMarket, int spotIndex) {
		return ScheduleModelUtils.getEndEvent(charterInMarket, spotIndex, lngScenarioModel.getScheduleModel().getSchedule());
	}

}