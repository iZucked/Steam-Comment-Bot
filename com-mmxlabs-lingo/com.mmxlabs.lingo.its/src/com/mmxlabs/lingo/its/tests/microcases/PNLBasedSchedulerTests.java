/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.time.Days;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@ExtendWith(ShiroRunner.class)
public class PNLBasedSchedulerTests extends AbstractMicroTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
	}

	/**
	 * The price based time scheduler would pick the highest sales price as the discharge date, not considering the tradeoff in charter cost savings by discharging and returning earlier with a
	 * slightly lower sales price.
	 * 
	 * Based on R issues - see fogbugz 2830 and 2906
	 */
	@Test
	public void testSplitMonthReturnEarlyOverHigherPrice() {

		final Vessel vessel = fleetModelFinder.findVessel("<TFDE_165>");
		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getVesselOrDelegateBaseFuel(), "400");

		final CharterInMarket charter = spotMarketsModelBuilder.createCharterInMarket("SpotCharter", vessel, entity, "105000", 1);

		final Port dampier = portFinder.findPortById("L_AU_Dampi");
		portModelBuilder.configureLoadPort(dampier, 23.88, 36);

		final Port sodegaura = portFinder.findPortById("L_JP_Sodeg");
		portModelBuilder.configureDischargePort(sodegaura, 24, null, null);

		pricingModelBuilder.makeCommodityDataCurve("S_1", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.055) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();
		// We want this pricing time interval
		pricingModelBuilder.makeCommodityDataCurve("S_2", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.093) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("S_3", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.170) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();

		// Price based scheduler picks this price.
		pricingModelBuilder.makeCommodityDataCurve("S_4", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.295) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();

		final String sellExpression = "SPLITMONTH(SPLITMONTH(S_1,S_2,8),SPLITMONTH(S_3,S_4,24),16)";

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2020, 11, 27), dampier, null, entity, "5.2") //
				.withWindowSize(3, TimePeriod.DAYS) //
				.withWindowStartTime(6) //
				.build() //
				//
				.makeDESSale("Sell", LocalDate.of(2020, 12, 1), sodegaura, null, entity, sellExpression) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.build() //
				//
				.withVesselAssignment(charter, 0, 1) //
				.build();

		evaluateTestWith(OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UsePNLBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make()//
		);

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);

		final SlotAllocation loadAllocation = cargoAllocation.getSlotAllocations().get(0);
		final SlotAllocation dischargeAllocation = cargoAllocation.getSlotAllocations().get(1);

		// Check December
		Assertions.assertEquals(Month.DECEMBER, dischargeAllocation.getSlotVisit().getStart().toLocalDate().getMonth());
		// Check we selected the second price bucket
		final int dayOfMonth = dischargeAllocation.getSlotVisit().getStart().toLocalDate().getDayOfMonth();
		Assertions.assertTrue(dayOfMonth >= 8 && dayOfMonth < 16);
		// Check the price.
		Assertions.assertEquals(7.093, dischargeAllocation.getPrice(), 0.01);
	}

	/**
	 * The price based time scheduler would pick the highest sales price as the discharge date, not considering the tradeoff in charter cost savings by discharging and returning earlier with a
	 * slightly lower sales price.
	 * 
	 * Based on R issues - see fogbugz 2830 and 2906
	 */
	@Test
	public void testSplitMonthReturnEarlyOverHigherPrice_Case2() {

		final Vessel vessel = fleetModelFinder.findVessel("<TFDE_165>");
		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getVesselOrDelegateBaseFuel(), "400");

		final CharterInMarket charter = spotMarketsModelBuilder.createCharterInMarket("SpotCharter", vessel, entity, "105000", 1);

		final Port cameron = portFinder.findPortById("L_US_Camer");
		portModelBuilder.configureLoadPort(cameron, 23.88, 36);

		final Port isleOfGrain = portFinder.findPortById("L_GB_Isleo");
		portModelBuilder.configureDischargePort(isleOfGrain, 24, null, null);

		// We want this pricing time interval
		pricingModelBuilder.makeCommodityDataCurve("S_1", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 5), 2.822) //
				.build();
		// Price based scheduler picks this price.
		pricingModelBuilder.makeCommodityDataCurve("S_2", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 5), 2.823) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("S_3", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 5), 2.823) //
				.build();

		pricingModelBuilder.makeCommodityDataCurve("S_4", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(202, 5), 2.822) //
				.build();

		final String sellExpression = "SPLITMONTH(SPLITMONTH(S_1,S_2,8),SPLITMONTH(S_3,S_4,24),16)";

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2020, 4, 15), cameron, null, entity, "5.2") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withWindowStartTime(0) //
				.build() //
				//
				.makeDESSale("Sell", LocalDate.of(2020, 5, 1), isleOfGrain, null, entity, sellExpression) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.build() //
				//
				.withVesselAssignment(charter, 0, 1) //
				.build();

		evaluateTestWith(OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UsePNLBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make()//
		);

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);

		final SlotAllocation loadAllocation = cargoAllocation.getSlotAllocations().get(0);
		final SlotAllocation dischargeAllocation = cargoAllocation.getSlotAllocations().get(1);

		// Check December
		Assertions.assertEquals(Month.MAY, dischargeAllocation.getSlotVisit().getStart().toLocalDate().getMonth());
		// Check we selected the second price bucket
		final int dayOfMonth = dischargeAllocation.getSlotVisit().getStart().toLocalDate().getDayOfMonth();
		Assertions.assertTrue(dayOfMonth >= 1 && dayOfMonth < 8);
	}

	/**
	 * In this case the price based scheduler creates a long laden leg rather than speed up the vessel and reduce overall charter cost.
	 * 
	 * This case needs the efficient vessel to really highlight the difference.
	 * 
	 * Based on P issue fogbugz 2887
	 */
	@Test
	public void testSpeedUpOfFirstLeg() {

		final Port portArthur = portFinder.findPortById("L_US_PortA");
		portModelBuilder.configureLoadPort(portArthur, 22.72, 48);

		final Port onslow = portFinder.findPortById("L_AU_Onslo");
		portModelBuilder.configureLoadPort(onslow, 23.3, 30);

		final Port futtsu = portFinder.findPortById("L_JP_Futts");
		portModelBuilder.configureDischargePort(futtsu, 32, null, null);

		final Port tianjiin = portFinder.findPortById("L_CN_Tianj");
		portModelBuilder.configureDischargePort(tianjiin, 24, null, null);

		final Port boryeong = portFinder.findPortById("L_KR_Borye");
		portModelBuilder.configureDischargePort(boryeong, 32, null, null);

		final Port zeebrugge = portFinder.findPortById("L_BE_Zeebr");
		portModelBuilder.configureDischargePort(zeebrugge, 32, null, null);

		final Port barrowIsland = portFinder.findPortById("L_AU_Barro");

		costModelBuilder.setAllBaseFuelCost(fleetModelBuilder.getFleetModel(), "650");

		BaseFuel bf = fleetModelBuilder.createBaseFuel("LSMGO", 42.6);
		costModelBuilder.createOrUpdateBaseFuelCost(bf, "650");

		// Replicate similar attribs to real vessel
		final String newVesselName = "Asia Endeavour";

		final boolean vesselAlreadyExists = fleetModelFinder.getFleetModel().getVessels().stream().anyMatch(v -> v.getName().equalsIgnoreCase(newVesselName));
		Assertions.assertFalse(vesselAlreadyExists);

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		fleetModelFinder.getFleetModel().getVessels().add(vessel);
		vessel.setName(newVesselName);
		vessel.setReference(null);

		vessel.setCapacity(173590);
		vessel.setFillCapacity(0.985);

		vessel.setScnt(100000);
		vessel.setSafetyHeel(500);
		vessel.setMinSpeed(16);
		vessel.setMaxSpeed(18);

		vessel.setBaseFuel(bf);
		vessel.setIdleBaseFuel(bf);
		vessel.setInPortBaseFuel(bf);
		vessel.setPilotLightBaseFuel(bf);

		vessel.setPilotLightRate(5);

		final VesselStateAttributes ladenAttributes = FleetFactory.eINSTANCE.createVesselStateAttributes();
		vessel.setLadenAttributes(ladenAttributes);
		vessel.getLadenAttributes().setInPortBaseRate(21);
		vessel.getLadenAttributes().setInPortNBORate(130);
		vessel.getLadenAttributes().setNboRate(130);
		vessel.getLadenAttributes().setIdleNBORate(130);
		vessel.getLadenAttributes().setIdleBaseRate(12);
		vessel.getLadenAttributes().getFuelConsumption().clear();

		final VesselStateAttributes ballastAttributes = FleetFactory.eINSTANCE.createVesselStateAttributes();
		vessel.setBallastAttributes(ballastAttributes);
		vessel.getBallastAttributes().setInPortBaseRate(34);
		vessel.getBallastAttributes().setInPortNBORate(95);
		vessel.getBallastAttributes().setNboRate(95);
		vessel.getBallastAttributes().setIdleNBORate(95);
		vessel.getBallastAttributes().setIdleBaseRate(12);
		vessel.getBallastAttributes().getFuelConsumption().clear();

		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 16.0, 52);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 17.0, 61);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 18.0, 72);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 19.0, 84);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 19.5, 91);

		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 16.0, 53);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 17.0, 62);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 18.0, 72);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 19.0, 83);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 19.5, 90);

		vessel.getRouteParameters().clear();
		double rate = 100;
		fleetModelBuilder.setRouteParameters(vessel, RouteOption.SUEZ, rate, rate, rate, rate, 24);
		fleetModelBuilder.setRouteParameters(vessel, RouteOption.PANAMA, rate, rate, rate, rate, 24);

		GenericCharterContract charterContract = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Collections.emptyList(), 16.0, "80000", "650", false, true,
				Collections.singletonList(barrowIsland));

		final CharterInMarket charter = spotMarketsModelBuilder.createCharterInMarket("SpotCharter", vessel, entity, "80000", 1);
		charter.setMinDuration(12);
		charter.setGenericCharterContract(charterContract);

		final String sellExpression = "10";

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load1", LocalDate.of(2019, 8, 9), portArthur, null, entity, "5") //
				.withWindowSize(26, TimePeriod.DAYS) //
				.withWindowStartTime(6) //
				.withVisitDuration(48) //
				.build() //
				//
				.makeDESSale("Sell1", LocalDate.of(2019, 9, 30), futtsu, null, entity, sellExpression) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(7) //
				.withVisitDuration(32) //
				.build() //
				//
				.withVesselAssignment(charter, 0, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load2", LocalDate.of(2019, 10, 11), onslow, null, entity, "5") //
				.withWindowStartTime(8) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withVisitDuration(27) //
				.with(s -> ((LoadSlot) s).setArriveCold(true)) //
				.build() //
				//
				.makeDESSale("Sell2", LocalDate.of(2019, 10, 25), futtsu, null, entity, sellExpression) //
				.withWindowStartTime(7) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVisitDuration(32) //
				.build() //
				//
				.withVesselAssignment(charter, 0, 2) //
				.build();

		evaluateTestWith(OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UsePNLBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make()//
		);

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);

		final SlotAllocation loadAllocation = cargoAllocation.getSlotAllocations().get(0);

		Journey j = (Journey) loadAllocation.getSlotVisit().getNextEvent();

		// Make sure voyage duration is small.
		Assertions.assertTrue(j.getDuration() < 37 * 24); // ~37 days is the price based scheduler's choice
		Assertions.assertTrue(j.getDuration() < 23 * 24); // Should be less than this
	}

	/**
	 * 
	 * Speeding up the laden leg gives better P&L due to reduced charter cost. Based on K issue from email. Note: This requires two points in the price curve, with the month prior having the lower
	 * price, despite not arriving that month.
	 */
	@Test
	public void testKReportedIssue() {

		costModelBuilder.setAllBaseFuelCost(fleetModelBuilder.getFleetModel(), "422");

		BaseFuel bf = fleetModelBuilder.createBaseFuel("HFO2", 51.32);
		costModelBuilder.createOrUpdateBaseFuelCost(bf, "422");

		BaseFuel bf2 = fleetModelBuilder.createBaseFuel("MGO2", 50);
		costModelBuilder.createOrUpdateBaseFuelCost(bf2, "400");

		// Replicate similar attribs to real vessel
		final String newVesselName = "Asia Endeavour";

		final boolean vesselAlreadyExists = fleetModelFinder.getFleetModel().getVessels().stream().anyMatch(v -> v.getName().equalsIgnoreCase(newVesselName));
		Assertions.assertFalse(vesselAlreadyExists);

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		fleetModelFinder.getFleetModel().getVessels().add(vessel);
		vessel.setName(newVesselName);
		vessel.setReference(null);

		vessel.setCapacity(159867);
		vessel.setFillCapacity(0.985);

		vessel.setScnt(100000);
		vessel.setSafetyHeel(200);
		vessel.setMinSpeed(12);
		vessel.setMaxSpeed(19.5);

		vessel.setBaseFuel(bf);
		vessel.setIdleBaseFuel(bf);
		vessel.setInPortBaseFuel(bf);
		vessel.setPilotLightBaseFuel(bf2);

		vessel.setPilotLightRate(1);

		final VesselStateAttributes ladenAttributes = FleetFactory.eINSTANCE.createVesselStateAttributes();
		vessel.setLadenAttributes(ladenAttributes);
		vessel.getLadenAttributes().setInPortBaseRate(0);
		vessel.getLadenAttributes().setInPortNBORate(0);
		vessel.getLadenAttributes().setNboRate(70);
		vessel.getLadenAttributes().setIdleNBORate(70);
		vessel.getLadenAttributes().setIdleBaseRate(20);
		vessel.getLadenAttributes().getFuelConsumption().clear();

		final VesselStateAttributes ballastAttributes = FleetFactory.eINSTANCE.createVesselStateAttributes();
		vessel.setBallastAttributes(ballastAttributes);
		vessel.getBallastAttributes().setInPortBaseRate(0);
		vessel.getBallastAttributes().setInPortNBORate(0);
		vessel.getBallastAttributes().setNboRate(50);
		vessel.getBallastAttributes().setIdleNBORate(50);
		vessel.getBallastAttributes().setIdleBaseRate(20);
		vessel.getBallastAttributes().getFuelConsumption().clear();

		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 12.0, 71);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, true, 19.5, 117);

		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 12.0, 45.6);
		fleetModelBuilder.setVesselStateAttributesCurve(vessel, false, 19.5, 114);

		vessel.getRouteParameters().clear();
		double rate = 0;
		fleetModelBuilder.setRouteParameters(vessel, RouteOption.SUEZ, rate, rate, rate, rate, 24);
		fleetModelBuilder.setRouteParameters(vessel, RouteOption.PANAMA, rate, rate, rate, rate, 24);

		final CharterInMarket charter = spotMarketsModelBuilder.createCharterInMarket("SpotCharter", vessel, entity, "55000", 1);
		charter.setMaxDuration(25);

		final Port pluto = portFinder.findPortById("L_AU_Pluto");
		portModelBuilder.configureLoadPort(pluto, 23.25, 36);

		final Port futtsu = portFinder.findPortById("L_JP_Futts");
		portModelBuilder.configureDischargePort(futtsu, 36, null, null);

		//
		// // Price based scheduler picks this price.
		pricingModelBuilder.makeCommodityDataCurve("SELL", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2019, 8), 4.50) // This price
				.addIndexPoint(YearMonth.of(2019, 9), 4.94) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2019, 8, 22), pluto, null, entity, "5.53") //
				.withVolumeLimits(140_000, 200_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withWindowStartTime(5) //
				.build() //
				//
				.makeDESSale("Sell", LocalDate.of(2019, 8, 22), futtsu, null, entity, "SELL") //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withWindowStartTime(7) //
				.build() //
				//
				.withVesselAssignment(charter, 0, 1) //
				.build();

		distanceModelBuilder.setPortToPortDistance(pluto, futtsu, RouteOption.DIRECT, 3730, false);
		distanceModelBuilder.setPortToPortDistance(futtsu, pluto, RouteOption.DIRECT, 3737, false);

		evaluateTestWith(OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UsePNLBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make()//
		);

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);

		final SlotAllocation loadAllocation = cargoAllocation.getSlotAllocations().get(0);

		Journey j = (Journey) loadAllocation.getSlotVisit().getNextEvent();

		System.out.println(j.getSpeed());

		// Price based scheduler preferred min speed.
		Assertions.assertTrue(j.getSpeed() > 17.0); // We want to travel faster.

		// Make sure voyage duration is small.
		Assertions.assertTrue(j.getDuration() < 13 * 24); // ~13 days is the price based scheduler's choice
		Assertions.assertTrue(j.getDuration() < 11 * 24); // Should be less than this
	}

	/**
	 * 
	 * 
	 * Based on K issue
	 */
	@Test
	public void testKBallastSpeedIssue() {

		final Vessel vessel = fleetModelFinder.findVessel("<TFDE_165>");
		costModelBuilder.createOrUpdateBaseFuelCost(vessel.getVesselOrDelegateBaseFuel(), "400");

		final CharterInMarket charter = spotMarketsModelBuilder.createCharterInMarket("SpotCharter", vessel, entity, "105000", 1);

		final Port cameron = portFinder.findPortById("L_AU_Dampi");
		portModelBuilder.configureLoadPort(cameron, 23.88, 36);

		final Port sodegaura = portFinder.findPortById("L_JP_Sodeg");
		portModelBuilder.configureDischargePort(sodegaura, 24, null, null);

		pricingModelBuilder.makeCommodityDataCurve("S_1", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.055) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();
		// We want this pricing time interval
		pricingModelBuilder.makeCommodityDataCurve("S_2", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.093) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("S_3", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.170) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();

		// Price based scheduler picks this price.
		pricingModelBuilder.makeCommodityDataCurve("S_4", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2020, 12), 7.295) //
				.addIndexPoint(YearMonth.of(2021, 1), 4) //
				.build();

		final String sellExpression = "SPLITMONTH(SPLITMONTH(S_1,S_2,8),SPLITMONTH(S_3,S_4,24),16)";

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2020, 11, 27), cameron, null, entity, "5.2") //
				.withWindowSize(3, TimePeriod.DAYS) //
				.withWindowStartTime(6) //
				.build() //
				//
				.makeDESSale("Sell", LocalDate.of(2020, 12, 1), sodegaura, null, entity, sellExpression) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.build() //
				//
				.withVesselAssignment(charter, 0, 1) //
				.build();

		evaluateTestWith(OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UsePNLBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make()//
		);

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);

		final SlotAllocation loadAllocation = cargoAllocation.getSlotAllocations().get(0);
		final SlotAllocation dischargeAllocation = cargoAllocation.getSlotAllocations().get(1);

		// Check December
		Assertions.assertEquals(Month.DECEMBER, dischargeAllocation.getSlotVisit().getStart().toLocalDate().getMonth());
		// Check we selected the second price bucket
		final int dayOfMonth = dischargeAllocation.getSlotVisit().getStart().toLocalDate().getDayOfMonth();
		Assertions.assertTrue(dayOfMonth >= 8 && dayOfMonth < 16);
		// Check the price.
		Assertions.assertEquals(7.093, dischargeAllocation.getPrice(), 0.01);
	}

	/**
	 * 
	 * 
	 * Based on 1-1 base where the min duration was not respected. Several issues feeding into this in the PNL scheduler. Main issue was min duration check is only applied to the last plan *AFTER* we
	 * have picked the return times - thus better P&L with violations could have replaced all the no min-violation plans. Secondary issues were the time interval creation did not explicitly add in
	 * values for min/max duration and it did not update the earliest feasible arrival time. Thus we had some enforced idle time, (minimising the min duration violation) but not enought to meet the
	 * constraint.
	 */
	@Test
	public void testMinDurationIssue() {

		final Vessel vessel = fleetModelFinder.findVessel("<TFDE_165>");

		VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("35000") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withStartWindow(LocalDateTime.of(2017, 4, 1, 0, 0, 0), LocalDateTime.of(2071, 4, 20, 0, 0, 0))//
				.withMinDuration(30) //
				.withMaxDuration(70) //
				.withEndPorts(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE)) //

				.build();

		// Ballast bonus
		GenericCharterContract gcc = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Collections.emptySet(), 16.0, "35000", "200", false, false,
				Collections.singleton(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)));
		vesselAvailability.setCharterContractOverride(true);
		vesselAvailability.setContainedCharterContract(gcc);

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2017, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS), null, entity, "3") //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withWindowStartTime(0) //
				.build() //
				//
				.makeDESSale("Sell", LocalDate.of(2017, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_OGISHIMA), null, entity, "8.5") //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withWindowStartTime(0) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 0) //
				.build();

		evaluateTestWith(OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UsePNLBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make()//
		);

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		EList<Event> events = cargoAllocation.getSequence().getEvents();
		Event first = events.get(0);
		Event last = events.get(events.size() - 1);
		// This is the main check
		Assertions.assertTrue(Days.between(first.getStart(), last.getEnd()) >= vesselAvailability.getMinDuration());
		// Sanity check the max bound is still respected.
		Assertions.assertTrue(Days.between(first.getStart(), last.getEnd()) <= vesselAvailability.getMaxDuration());
	}

}
