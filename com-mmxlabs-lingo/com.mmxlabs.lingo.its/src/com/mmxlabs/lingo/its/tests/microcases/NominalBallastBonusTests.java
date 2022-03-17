/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.LumpSumBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.LumpSumRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.MatchingContractDetails;
import com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.VolumeUnits;

//@SuppressWarnings({ "unused", "null" })
//@ExtendWith(ShiroRunner.class)
public class NominalBallastBonusTests extends AbstractMicroTestCase {
//
//	protected static final String TEST_CHARTER_CURVE_NAME = "TestCharterCurve";
//
	private void setSimpleDistances() {
		distanceModelBuilder.setAllDistances(RouteOption.DIRECT, 1000.0);

		distanceModelBuilder.setAllDistances(RouteOption.SUEZ, 2000.0);
		distanceModelBuilder.setAllDistances(RouteOption.PANAMA, 2000.0);
	}

//	
//	TODO: Rename the class
//	TODO: Test min duration works as epecxted.
//	
//	

	/**
	 * Simple test case, just change the redelivery ports and make sure these are
	 * correctly accounted.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testEndAtPorts() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case, return to load
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation simpleAllocation = new SimpleCargoAllocation(cargoAllocation);
			final SlotAllocation loadAllocation = simpleAllocation.getLoadAllocation();
			final SlotVisit slotVisit = loadAllocation.getSlotVisit();

			final PortVisit endEvent = findEndEvent(cargoAllocation);
			Assertions.assertNotNull(endEvent);

			// Check the end port is the same as the load
			Assertions.assertSame(slotVisit.getPort(), endEvent.getPort());
		}

		// Now set the redelivery as any discharge

		// End at any discharge port
		market.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));
		// After case
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation simpleAllocation = new SimpleCargoAllocation(cargoAllocation);
			final SlotAllocation dischargeAllocation = simpleAllocation.getDischargeAllocation();
			final SlotVisit slotVisit = dischargeAllocation.getSlotVisit();
			final PortVisit endEvent = findEndEvent(cargoAllocation);
			Assertions.assertNotNull(endEvent);

			// Check the end port is the same as the discharge
			Assertions.assertSame(slotVisit.getPort(), endEvent.getPort());

		}

		// Finally set as a specific port
		market.getEndAt().clear();
		market.getEndAt().add(portFinder.findPortById(InternalDataConstants.PORT_COLON));

		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			final PortVisit endEvent = findEndEvent(cargoAllocation);
			Assertions.assertNotNull(endEvent);

			// Check the end port is the specified end port
			Assertions.assertSame(portFinder.findPortById(InternalDataConstants.PORT_COLON), endEvent.getPort());
		}
	}

	/**
	 * Simple test case, just change the start port. This should be ignored for
	 * nominal cargoes.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartAtPorts() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case - no start at
		long beforeCargoPNL;
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			Assertions.assertEquals(0, cargoAllocation.getRepositioningFee());

			beforeCargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}

		// Set a start at port
		market.setStartAt(portFinder.findPortById(InternalDataConstants.PORT_COLON));
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);

			Assertions.assertEquals(0, cargoAllocation.getRepositioningFee());

			final PortVisit endEvent = findEndEvent(cargoAllocation);
			Assertions.assertNotNull(endEvent);

			// Check the end port is still the load port
			Assertions.assertSame(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), endEvent.getPort());

			// No P&L change expected
			Assertions.assertEquals(beforeCargoPNL, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());

		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusWithEndPorts() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		// End at any discharge port
		market.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case
		final long beforeCargoPNL;
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0, cargoAllocation.getBallastBonusFee());

			beforeCargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}

		// Now set the contract and BB
		final long ballastBonus = 1_000_000;

		// Setup multiple rules to make sure we pick the correct one
		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardBallastBonus() //
				// Add alternative port here
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000)) //
				// Add load port to see if that catches it out
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(700_000)) //
				// The correct rule
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), Long.toString(ballastBonus)) //
				// Catch all case
				.addLumpSumRule((Port) null, Long.toString(2_000_000)) //
				.build() // BB
				.build(true);
		market.setGenericCharterContract(charterContract);

		// After case
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(ballastBonus, cargoAllocation.getBallastBonusFee());

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(beforeCargoPNL - ballastBonus, cargoPNL);

			Assertions.assertEquals(beforeCargoPNL - ballastBonus, cargoPNL);

			// Check annotations
			final CharterContractFeeDetails details = getDetails(cargoAllocation, LumpSumBallastBonusTermDetails.class);
			Assertions.assertNotNull(details);

			Assertions.assertEquals(ballastBonus, details.getFee());

			Assertions.assertEquals(portFinder.findPortById(InternalDataConstants.PORT_SAKAI).getName(), ((LumpSumBallastBonusTermDetails) details.getMatchingContractDetails()).getMatchedPort());
		}
	}

	/**
	 * Test the fallback lumpsum case is picked up.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusWithFallbackMatchAndEndPorts() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		// End at any discharge port
		market.getEndAt().add(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE));

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case
		final long beforeCargoPNL;
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0, cargoAllocation.getBallastBonusFee());

			beforeCargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}

		// Now set the contract and BB

		final long ballastBonus = 1_000_000;
		// Setup multiple rules to make sure we pick the correct one
		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardBallastBonus() //
				// Add alternative port here
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000)) //
				// Add load port to see if that catches it out
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(700_000)) //
				// The correct rule - catch all case
				.addLumpSumRule((Port) null, Long.toString(ballastBonus)) //
				.build() // BB
				.build(true);
		market.setGenericCharterContract(charterContract);

		// After case
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(ballastBonus, cargoAllocation.getBallastBonusFee());

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();

			Assertions.assertEquals(beforeCargoPNL - ballastBonus, cargoPNL);

			// Check annotations
			final CharterContractFeeDetails details = getDetails(cargoAllocation, LumpSumBallastBonusTermDetails.class);
			Assertions.assertNotNull(details);

			Assertions.assertEquals(ballastBonus, details.getFee());

			// Expect Sakai as it is the discharge we used.
			Assertions.assertEquals(portFinder.findPortById(InternalDataConstants.PORT_SAKAI).getName(), ((LumpSumBallastBonusTermDetails) details.getMatchingContractDetails()).getMatchedPort());
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumBallastBonusWithReturnToLoad() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		@NonNull
		final Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Before case
		final long beforeCargoPNL;
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0, cargoAllocation.getBallastBonusFee());

			beforeCargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}

		// Now set the contract and BB
		final long ballastBonus = 1_000_000;
		// Setup multiple rules to make sure we pick the correct one
		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardBallastBonus() //
				// Add alternative port here
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000)) //
				// Add discharge in even though it is return to load just in case
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), Long.toString(700_000)) //
				// The correct rule
				.addLumpSumRule(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(ballastBonus)) //
				// catch all case
				.addLumpSumRule((Port) null, Long.toString(2_000_000)) //
				.build() // BB
				.build(true);
		market.setGenericCharterContract(charterContract);

		// After case
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(ballastBonus, cargoAllocation.getBallastBonusFee());

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(beforeCargoPNL - ballastBonus, cargoPNL);

			// Check annotations
			final CharterContractFeeDetails details = getDetails(cargoAllocation, LumpSumBallastBonusTermDetails.class);
			Assertions.assertNotNull(details);

			Assertions.assertEquals(ballastBonus, details.getFee());

			Assertions.assertEquals(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN).getName(),
					((LumpSumBallastBonusTermDetails) details.getMatchingContractDetails()).getMatchedPort());
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLumpSumRepositioning() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Set a start at which should be ignored.
		market.setStartAt(portFinder.findPortById(InternalDataConstants.PORT_COLON));

		// Before case
		final long beforeCargoPNL;
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0, cargoAllocation.getRepositioningFee());

			beforeCargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}

		// Now set the contract and positioning
		final long repositioningFee = 1_000_000;

		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardRepositioning() //
				// Add alternative port here
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), Long.toString(500_000))
				// Add the start at port which should also be ignored
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_COLON), Long.toString(700_000))
				// Add the discharge port which is not correct either
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), Long.toString(900_000))
				// The correct rule
				.addLumpSumRuleForSingleStart(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), Long.toString(repositioningFee))
				// Catch all case
				.addLumpSumRuleForSingleStart((Port) null, Long.toString(2_000_000)) //
				.build() // Repos

				.build(true);
		market.setGenericCharterContract(charterContract);

		// After case
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(repositioningFee, cargoAllocation.getRepositioningFee());

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(beforeCargoPNL - repositioningFee, cargoPNL);

			// Check annotations
			final CharterContractFeeDetails details = getDetails(cargoAllocation, LumpSumRepositioningFeeTermDetails.class);
			Assertions.assertNotNull(details);

			Assertions.assertEquals(repositioningFee, details.getFee());

			Assertions.assertEquals(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN).getName(),
					((LumpSumRepositioningFeeTermDetails) details.getMatchingContractDetails()).getMatchedPort());

		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOriginPortRepositioning() throws Exception {

		// Use simple distance to make calculations easier
		setSimpleDistances();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("mkt1", vessel, entity, "80000", 0);
		market.setNominal(true);

		final LoadSlot loadFOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.build();
		final DischargeSlot dischargeDES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final @NonNull Cargo cargo = cargoModelBuilder.createCargo(loadFOB1, dischargeDES1);
		cargo.setVesselAssignmentType(market);
		cargo.setSpotIndex(-1);

		// Set a start at which should be ignored.
		market.setStartAt(portFinder.findPortById(InternalDataConstants.PORT_COLON));

		// Before case
		final long beforeCargoPNL;
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0, cargoAllocation.getRepositioningFee());

			beforeCargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}

		// Now set the contract and positioning
		final long repositioningFee = 1_000_000;

		final GenericCharterContract charterContract = commercialModelBuilder.makeCharterContract() //
				.withStandardRepositioning() //
				// Add alternative port here
				.addOriginRule(portFinder.findPortById(InternalDataConstants.PORT_BALBOA), portFinder.findPortById(InternalDataConstants.PORT_BALBOA), 15.0, "10000", "0", false, false, null) //
				// Add the start at port which should also be ignored

				.addOriginRule(portFinder.findPortById(InternalDataConstants.PORT_COLON), portFinder.findPortById(InternalDataConstants.PORT_COLON), 15.0, "10000", "0", false, false, null) //
				// Add the discharge port which is not correct either

				.addOriginRule(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), 15.0, "10000", "0", false, false, null) //
				// The correct rule (note origin and start is the same, so only expect lump sum
				// to come in.
				.addOriginRule(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), 15.0, "10000", "0", false, false,
						Long.toString(repositioningFee)) //

				// Catch all case
				.addOriginRule(Collections.emptyList(), null, 15.0, "10000", "0", false, false, Long.toString(repositioningFee)) //

				.build() // Repos

				.build(true);
		market.setGenericCharterContract(charterContract);

		// After case
		{
			evaluateTest(null, null, scenarioRunner -> {
			});

			final @Nullable Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			assert schedule != null;

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), schedule);
			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(repositioningFee, cargoAllocation.getRepositioningFee());

			final long cargoPNL = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
			Assertions.assertEquals(beforeCargoPNL - repositioningFee, cargoPNL);

			// Check annotations
			final CharterContractFeeDetails details = getDetails(cargoAllocation, OriginPortRepositioningFeeTermDetails.class);
			Assertions.assertNotNull(details);

			Assertions.assertEquals(repositioningFee, details.getFee());

			Assertions.assertEquals(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN).getName(),
					((OriginPortRepositioningFeeTermDetails) details.getMatchingContractDetails()).getOriginPort());

		}
	}

	private @Nullable PortVisit findEndEvent(final CargoAllocation cargoAllocation) {
		final SimpleCargoAllocation simpleAllocation = new SimpleCargoAllocation(cargoAllocation);
		final SlotAllocation dischargeAllocation = simpleAllocation.getDischargeAllocation();
		final SlotVisit slotVisit = dischargeAllocation.getSlotVisit();
		Event evt = slotVisit.getNextEvent();
		PortVisit endEvent = null;
		while (evt != null) {
			if (evt instanceof final PortVisit pv) {
				endEvent = pv;
				break;
			}
			evt = evt.getNextEvent();
		}
		return endEvent;
	}

	private CharterContractFeeDetails getDetails(final CargoAllocation cargoAllocation, final Class<? extends MatchingContractDetails> cls) {

		if (cargoAllocation != null) {

			for (final GeneralPNLDetails generalPNLDetails : cargoAllocation.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof final CharterContractFeeDetails contractDetails) {
					if (cls.isInstance(contractDetails.getMatchingContractDetails())) {
						return contractDetails;
					}
				}
			}
		}

		return null;

	}

}