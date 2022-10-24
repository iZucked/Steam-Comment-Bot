/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCooldownDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IdleFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;

public class NewLNGVoyageCalculatorTest {

	private static final int VESSEL_NBO_SPEED = 15;
	private static final int VESSEL_MAX_SPEED = 20;
	private static final int VESSEL_MIN_SPEED = 10;
	private static final int HEEL_DURATION = 2 * 24;

	@Test
	public void testCalculateSpeed() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options - no NBO to avoid min NBO trigger
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		// Set distance/time to give the expected speed exactly
		final int expectedSpeed = VESSEL_NBO_SPEED + 1;
		options.setAvailableTime(48);
		options.setRoute(ERouteOption.DIRECT, expectedSpeed * 48, 0L, 0);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		Assertions.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_MinSpeedClamp() {
		final VoyageOptions options = createSampleVoyageOptions();

		// Min Speed is 10 - See createSampleVesselClass
		final int expectedMinSpeed = VESSEL_MIN_SPEED;

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		// Set distance time so that the speed would be below the expected min speed
		options.setAvailableTime(48 * 3);
		options.setRoute(ERouteOption.DIRECT, expectedMinSpeed * 48, 0L, 0);

		// Sanity check that inputs will yield a speed lower than the expected min speed
		Assertions.assertTrue(options.getDistance() / options.getAvailableTime() < expectedMinSpeed);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		// Check speed is clamped to the minumum
		Assertions.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMinSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_MinNBOSpeedClamp() {
		final VoyageOptions options = createSampleVoyageOptions();

		// Min Speed is 15 - See createSampleVesselClass
		final int expectedMinSpeed = VESSEL_NBO_SPEED;

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);

		// Set distance time so that the speed would be below the expected min speed
		options.setAvailableTime(48 * 15);
		options.setRoute(ERouteOption.DIRECT, expectedMinSpeed * 48, 0L, 0);

		// Sanity check that inputs will yield a speed lower than the expected min speed
		Assertions.assertTrue(options.getDistance() / options.getAvailableTime() < expectedMinSpeed);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		// Check speed is clamped to the minumum
		Assertions.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMinSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_MaxSpeedClamp() {
		final VoyageOptions options = createSampleVoyageOptions();

		// Max Speed is 20 - See createSampleVesselClass
		final int expectedMaxSpeed = VESSEL_MAX_SPEED;

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		// Set distance time so that the speed would be below the expected min speed
		options.setAvailableTime(48 / 2);
		options.setRoute(ERouteOption.DIRECT, expectedMaxSpeed * 48, 0L, 0);

		// Sanity check that inputs will yield a speed higher than the expected max
		// speed
		Assertions.assertTrue(options.getDistance() / options.getAvailableTime() > expectedMaxSpeed);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		// Check speed is clamped to the minumum
		Assertions.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMaxSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_ZeroDistance() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options - no NBO to avoid min NBO trigger
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		// No distance, no speed!
		final int expectedSpeed = 0;

		options.setAvailableTime(48);
		options.setRoute(ERouteOption.DIRECT, 0, 0L, 0);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		Assertions.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_SmallDistance() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options - no NBO to avoid min NBO trigger
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		// Small distance, expect to hit min speed!
		// Min Speed is 10 - See createSampleVesselClass
		final int expectedMinSpeed = VESSEL_MIN_SPEED;

		options.setAvailableTime(48);
		options.setRoute(ERouteOption.DIRECT, 1, 0L, 0);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		Assertions.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMinSpeed), speed);
	}

	@Test
	public void testCalculateTravelFuelRequirements_NBOOnly() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);

		final VesselState vesselState = VesselState.Laden;

		final int speed = VESSEL_NBO_SPEED - 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setRoute(ERouteOption.DIRECT, speed * travelTime, 0L, 0);

		// Sanity check that we are travelling slower than NBO rate so there will be no
		// supplement
		Assertions.assertTrue(OptimiserUnitConvertor.convertToInternalSpeed(speed) < options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		IVessel vessel = options.getVessel();
		calc.calculateTravelFuelRequirements(options, details, vessel, vesselState, travelTime, OptimiserUnitConvertor.convertToInternalSpeed(speed), Long.MAX_VALUE);

		// Check results
		final long nboRate = vessel.getNBORate(vesselState);
		final long pilotLightRate = vessel.getPilotLightRate();

		Assertions.assertEquals(nboRate * travelTime / 24, details.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
		// Expect some pilot light
		Assertions.assertTrue(details.getFuelConsumption(vessel.getPilotLightFuelInMT()) > 0);

		Assertions.assertEquals(pilotLightRate * travelTime / 24, details.getFuelConsumption(vessel.getPilotLightFuelInMT()));

		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getTravelBaseFuelInMT()));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT()));
		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
	}

	@Test
	public void testCalculateTravelFuelRequirements_NBO_FBO() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_FBO);

		final VesselState vesselState = VesselState.Laden;
		final int speed = VESSEL_NBO_SPEED + 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setRoute(ERouteOption.DIRECT, speed * travelTime, 0L, 0);

		// Sanity check that we are travelling slower than NBO rate so there will be no
		// supplement
		final int internalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(speed);
		Assertions.assertTrue(internalSpeed > options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		IVessel vessel = options.getVessel();
		calc.calculateTravelFuelRequirements(options, details, vessel, vesselState, travelTime, internalSpeed, Long.MAX_VALUE);

		// Expected NBO rate
		final long nboRate = vessel.getNBORate(vesselState);

		// Expected pilot light rate
		final long pilotLightRate = vessel.getPilotLightRate();

		// Expected pure base consumption rates
		final long expectedBaseConsumption = vessel.getConsumptionRate(vesselState).getRate(internalSpeed);

		final IBaseFuel baseFuel = vessel.getTravelBaseFuel();
		final IBaseFuel pilotLightBaseFuel = vessel.getPilotLightBaseFuel();

		Assertions.assertEquals(nboRate * travelTime / 24, details.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
		// Equivalence is 0.5 so double to convert rate to m3
		Assertions.assertEquals((2 * expectedBaseConsumption - nboRate) * travelTime / 24, details.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
		Assertions.assertTrue(details.getFuelConsumption(vessel.getPilotLightFuelInMT()) > 0);
		// Expect some pilot light
		Assertions.assertEquals(pilotLightRate * travelTime / 24, details.getFuelConsumption(vessel.getPilotLightFuelInMT()));

		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getTravelBaseFuelInMT()));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT()));

	}

	@Test
	public void testCalculateTravelFuelRequirements_NBO_BaseSupplement() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);

		final VesselState vesselState = VesselState.Laden;
		final int speed = VESSEL_NBO_SPEED + 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setRoute(ERouteOption.DIRECT, speed * travelTime, 0L, 0);

		// Sanity check that we are travelling slower than NBO rate so there will be no
		// supplement
		final int internalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(speed);
		Assertions.assertTrue(internalSpeed > options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		IVessel vessel = options.getVessel();
		calc.calculateTravelFuelRequirements(options, details, vessel, vesselState, travelTime, internalSpeed, Long.MAX_VALUE);

		// Expected NBO rate
		final long nboRate = vessel.getNBORate(vesselState);

		// Expected pure base consumption rates
		final long expectedBaseConsumption = vessel.getConsumptionRate(vesselState).getRate(internalSpeed);
		final IBaseFuel baseFuel = vessel.getTravelBaseFuel();
		final IBaseFuel pilotLightBaseFuel = vessel.getPilotLightBaseFuel();

		Assertions.assertEquals(nboRate * travelTime / 24, details.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
		// Some supplement expected
		Assertions.assertEquals((expectedBaseConsumption - nboRate / 2) * travelTime / 24, details.getFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT()));
		// Equivalence is 0.5
		Assertions.assertTrue(details.getFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT()) > 0);

		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getPilotLightFuelInMT()));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getTravelBaseFuelInMT()));

	}

	@Test
	public void testCalculateTravelFuelRequirements_Base_Only() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		final VesselState vesselState = VesselState.Laden;
		final int speed = VESSEL_NBO_SPEED + 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setRoute(ERouteOption.DIRECT, speed * travelTime, 0L, 0);

		// Sanity check that we are travelling slower than NBO rate so there will be no
		// supplement
		final int internalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(speed);
		Assertions.assertTrue(internalSpeed > options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		IVessel vessel = options.getVessel();
		calc.calculateTravelFuelRequirements(options, details, vessel, vesselState, travelTime, internalSpeed, Long.MAX_VALUE);

		// Expected pure base consumption rates
		final long expectedBaseConsumption = vessel.getConsumptionRate(vesselState).getRate(internalSpeed);

		final IBaseFuel baseFuel = vessel.getTravelBaseFuel();
		final IBaseFuel pilotLightBaseFuel = vessel.getPilotLightBaseFuel();

		Assertions.assertEquals(expectedBaseConsumption * travelTime / 24, details.getFuelConsumption(vessel.getTravelBaseFuelInMT()));

		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.NBO_In_m3));
		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.FBO_In_m3));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getPilotLightFuelInMT()));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT()));
	}

	@Test
	public void testCalculateIdleFuelRequirements_NBO() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);
		options.setIdleFuelChoice(IdleFuelChoice.NBO);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = 48;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		IVessel vessel = options.getVessel();
		calc.calculateIdleFuelRequirements(options, details, vessel, vesselState, idleTime, Long.MAX_VALUE);

		// Check results
		final long nboIdleRate = vessel.getIdleNBORate(vesselState);
		final long pilotLightRate = vessel.getPilotLightRate();
		final IBaseFuel idleBaseFuel = vessel.getIdleBaseFuel();
		final IBaseFuel pilotLightBaseFuel = vessel.getPilotLightBaseFuel();

		Assertions.assertEquals(nboIdleRate * idleTime / 24, details.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
		// Expect some pilot light
		Assertions.assertTrue(details.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()) > 0);
		Assertions.assertEquals(pilotLightRate * idleTime / 24, details.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()));

		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getIdleBaseFuelInMT()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_HotelLoad_NoHeel() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);
		options.setIdleFuelChoice(IdleFuelChoice.BUNKERS);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = 48;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		IVessel vessel = options.getVessel();
		calc.calculateIdleFuelRequirements(options, details, vessel, vesselState, idleTime, Long.MAX_VALUE);

		// Check results
		final long baseRate = vessel.getIdleConsumptionRate(vesselState);
		final IBaseFuel idleBaseFuel = vessel.getIdleBaseFuel();
		final IBaseFuel pilotLightBaseFuel = vessel.getPilotLightBaseFuel();

		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3));
		Assertions.assertEquals(0, details.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()));

		Assertions.assertEquals(baseRate * idleTime / 24, details.getFuelConsumption(vessel.getIdleBaseFuelInMT()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_Required() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);
		options.setIdleFuelChoice(IdleFuelChoice.BUNKERS);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getWarmupTime() * 2;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel(), vesselState, idleTime, Long.MAX_VALUE);

		// Check results
		final long cooldownVolume = options.getVessel().getCooldownVolume();

		Assertions.assertEquals(cooldownVolume, details.getFuelConsumption(LNGFuelKeys.Cooldown_In_m3));

	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_BelowWarmupTime() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);
		options.setIdleFuelChoice(IdleFuelChoice.BUNKERS);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getWarmupTime() - 1;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel(), vesselState, idleTime, Long.MAX_VALUE);

		// Check results
		final long cooldownVolume = options.getVessel().getCooldownVolume();

		Assertions.assertEquals(cooldownVolume, details.getFuelConsumption(LNGFuelKeys.Cooldown_In_m3));

	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_NotRequired_IdleNBO() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);
		options.setIdleFuelChoice(IdleFuelChoice.NBO);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getWarmupTime() * 2;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel(), vesselState, idleTime, Long.MAX_VALUE);

		// Check results
		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.Cooldown_In_m3));
	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_NotRequired_HotelLoad() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);
		options.setIdleFuelChoice(IdleFuelChoice.BUNKERS);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getWarmupTime() - 1;
		// Set heel to zero to avoid it getting in the way
		((Vessel) options.getVessel()).setSafetyHeel(OptimiserUnitConvertor.convertToInternalVolume(0));

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel(), vesselState, idleTime, Long.MAX_VALUE);

		// Check results
		Assertions.assertEquals(0, details.getFuelConsumption(LNGFuelKeys.Cooldown_In_m3));

	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_NBOOnly() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO
		// speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_FBO);

		final VesselState vesselState = VesselState.Laden;
		final IRouteCostProvider.CostType costType = IRouteCostProvider.CostType.Laden;

		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalDailyRate(150);
		final long routeCost = 100000;
		options.setRoute(ERouteOption.DIRECT, 0, routeCost, 0);

		final IVessel vessel = options.getVessel();
		final IBaseFuel baseFuel = vessel.getTravelBaseFuel();
		final IBaseFuel idleBaseFuel = vessel.getIdleBaseFuel();
		final IBaseFuel pilotLightBaseFuel = vessel.getPilotLightBaseFuel();

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel())).thenReturn(additionalRouteTime);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel(), vesselState)).thenReturn(expectedBaseConsumption);

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel(), vesselState, additionalRouteTime, Long.MAX_VALUE);

		// Check results
		final long pilotLightRate = options.getVessel().getPilotLightRate();

		Assertions.assertEquals(nboRate * additionalRouteTime / 24, details.getRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3));
		Assertions.assertEquals((2 * expectedBaseConsumption - nboRate) * additionalRouteTime / 24, details.getRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3));
		// Expect some pilot light
		Assertions.assertTrue(details.getRouteAdditionalConsumption(vessel.getPilotLightFuelInMT()) > 0);

		Assertions.assertEquals(pilotLightRate * additionalRouteTime / 24, details.getRouteAdditionalConsumption(vessel.getPilotLightFuelInMT()));

		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT()));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()));

	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_NBO_FBO() {

		// In this test, we check that we only use travel NBO.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_FBO);

		final VesselState vesselState = VesselState.Laden;
		final IRouteCostProvider.CostType costType = IRouteCostProvider.CostType.Laden;

		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long routeCost = 100000;
		options.setRoute(ERouteOption.DIRECT, 0, routeCost, 0);

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel())).thenReturn(additionalRouteTime);
		// Mockito.when(mockRouteCostProvider.getRouteCost(options.getRoute(),
		// options.getVessel(), costType)).thenReturn(routeCost);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel(), vesselState)).thenReturn(expectedBaseConsumption);

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel(), vesselState, additionalRouteTime, Long.MAX_VALUE);

		final IVessel vessel = options.getVessel();
		final long pilotLightRate = vessel.getPilotLightRate();
		// Expected pure base consumption rates

		Assertions.assertEquals(nboRate * additionalRouteTime / 24, details.getRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3));
		// Equivalence is 0.5 so double to convert rate to m3
		Assertions.assertEquals((2 * expectedBaseConsumption - nboRate) * additionalRouteTime / 24, details.getRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3));
		Assertions.assertTrue(details.getRouteAdditionalConsumption(vessel.getPilotLightFuelInMT()) > 0);
		// Expect some pilot light
		Assertions.assertEquals(pilotLightRate * additionalRouteTime / 24, details.getRouteAdditionalConsumption(vessel.getPilotLightFuelInMT()));

		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT()));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()));

	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_NBO_BaseSupplement() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.NBO_PLUS_BUNKERS);

		final VesselState vesselState = VesselState.Laden;
		final IRouteCostProvider.CostType costType = IRouteCostProvider.CostType.Laden;

		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long routeCost = 100000;

		options.setRoute(ERouteOption.SUEZ, 0, routeCost, 0);

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel())).thenReturn(additionalRouteTime);
		// Mockito.when(mockRouteCostProvider.getRouteCost(options.getRoute(),
		// options.getVessel(), costType)).thenReturn(routeCost);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel(), vesselState)).thenReturn(expectedBaseConsumption);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);
		final IVessel vessel = options.getVessel();

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel(), vesselState, additionalRouteTime, Long.MAX_VALUE);

		Assertions.assertEquals(nboRate * additionalRouteTime / 24, details.getRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3));
		// Some supplement expected
		Assertions.assertEquals(((expectedBaseConsumption * 2 - nboRate) * additionalRouteTime) / 2 / 24, details.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()));
		// Equivalence is 0.5
		Assertions.assertTrue(details.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()) > 0);

		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getPilotLightFuelInMT()));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT()));
	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_Base_Only() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setTravelFuelChoice(TravelFuelChoice.BUNKERS);

		final VesselState vesselState = VesselState.Laden;
		final IRouteCostProvider.CostType costType = IRouteCostProvider.CostType.Laden;

		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalDailyRate(50);
		final long routeCost = 100000;

		options.setRoute(ERouteOption.DIRECT, 0, routeCost, 0);

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel())).thenReturn(additionalRouteTime);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel(), vesselState)).thenReturn(expectedBaseConsumption);
		final IVessel vessel = options.getVessel();

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel(), vesselState, additionalRouteTime, Long.MAX_VALUE);

		Assertions.assertEquals(expectedBaseConsumption * additionalRouteTime / 24, details.getRouteAdditionalConsumption(vessel.getTravelBaseFuelInMT()));

		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(LNGFuelKeys.NBO_In_m3));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(LNGFuelKeys.FBO_In_m3));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getPilotLightFuelInMT()));
		Assertions.assertEquals(0, details.getRouteAdditionalConsumption(vessel.getSupplementalTravelBaseFuelInMT()));

	}

	VoyageOptions createSampleVoyageOptions() {

		final IPortSlot from = Mockito.mock(IPortSlot.class, "from");
		final IPortSlot to = Mockito.mock(IPortSlot.class, "to");

		final VoyageOptions options = new VoyageOptions(from, to);

		options.setNBOSpeed(OptimiserUnitConvertor.convertToInternalSpeed(VESSEL_NBO_SPEED));

		final Vessel vessel = createSampleVessel();

		options.setVessel(vessel);
		options.setVesselState(VesselState.Laden);

		options.setCargoCVValue(OptimiserUnitConvertor.convertToInternalConversionFactor(22.8));

		return options;
	}

	@Test
	public void testCalculatePortFuelRequirements1() {
		testCalculatePortFuelRequirements(PortType.Load, 13, 11 * 24, 143);
	}

	@Test
	public void testCalculatePortFuelRequirements2() {
		testCalculatePortFuelRequirements(PortType.Discharge, 22, 101 * 24, 2222);
	}

	public void testCalculatePortFuelRequirements(final PortType portType, final int visitDuration, final int consumptionRate, final long target) {

		// In this test, we check that we only use travel NBO.

		final PortOptions options = createSamplePortOptions();

		options.setVisitDuration(visitDuration);

		final PortDetails details = new PortDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final Vessel vessel = (Vessel) options.getVessel();

		vessel.setInPortConsumptionRateInMTPerDay(portType, consumptionRate);

		Mockito.when(options.getPortSlot().getPortType()).thenReturn(portType);

		calc.calculatePortFuelRequirements(options, details);

		// Check results
		Assertions.assertSame(options, details.getOptions());

		Assertions.assertEquals(target, details.getFuelConsumption(vessel.getInPortBaseFuelInMT()));
	}

	@Test
	public void testCalculateVoyagePlanFuelConsumptions() {
		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final IVessel vessel = new Vessel("vessel", Long.MAX_VALUE);
		vessel.setTravelBaseFuel(new BaseFuel(indexingContext, "FUEL"));
		vessel.setIdleBaseFuel(new BaseFuel(indexingContext, "IDLE"));
		vessel.setPilotLightBaseFuel(new BaseFuel(indexingContext, "PILOT"));
		vessel.setInPortBaseFuel(new BaseFuel(indexingContext, "PORT"));

		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));

		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(-1));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(-1));

		final VoyageOptions options = new VoyageOptions(loadSlot, dischargeSlot);
		options.setVesselState(VesselState.Laden);

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object
		// has a x10 multiplier to the previous value -= this makes it easy to add up
		// for the expectations
		loadDetails.setFuelConsumption(vessel.getTravelBaseFuelInMT(), 1);
		loadDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 2);
		loadDetails.setFuelConsumption(LNGFuelKeys.FBO_In_m3, 3);
		loadDetails.setFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT(), 4);
		loadDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 5);
		loadDetails.setFuelConsumption(vessel.getIdleBaseFuelInMT(), 6);
		loadDetails.setFuelConsumption(vessel.getPilotLightFuelInMT(), 7);
		loadDetails.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), 8);
		loadDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 9);

		details.setFuelConsumption(vessel.getTravelBaseFuelInMT(), 10);
		details.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 20);
		details.setFuelConsumption(LNGFuelKeys.FBO_In_m3, 30);
		details.setFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT(), 40);
		details.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 50);
		details.setFuelConsumption(vessel.getIdleBaseFuelInMT(), 60);
		details.setFuelConsumption(vessel.getPilotLightFuelInMT(), 70);
		details.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), 80);
		details.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 90);

		dischargeDetails.setFuelConsumption(vessel.getTravelBaseFuelInMT(), 100);
		dischargeDetails.setFuelConsumption(LNGFuelKeys.NBO_In_m3, 200);
		dischargeDetails.setFuelConsumption(LNGFuelKeys.FBO_In_m3, 300);
		dischargeDetails.setFuelConsumption(vessel.getSupplementalTravelBaseFuelInMT(), 400);
		dischargeDetails.setFuelConsumption(LNGFuelKeys.IdleNBO_In_m3, 500);
		dischargeDetails.setFuelConsumption(vessel.getIdleBaseFuelInMT(), 600);
		dischargeDetails.setFuelConsumption(vessel.getPilotLightFuelInMT(), 700);
		dischargeDetails.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), 800);
		dischargeDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 900);

		final long result = calc.calculateLNGFuelConsumptions(vessel, loadDetails, details, dischargeDetails);

		Assertions.assertEquals(22 + 33 + 55, result);

		// Expect no change in LNG price
		// Assertions.assertEquals(0, details.getFuelUnitPrice(FuelComponent.FBO));
		// Assertions.assertEquals(0, details.getFuelUnitPrice(FuelComponent.NBO));
		// Assertions.assertEquals(0, details.getFuelUnitPrice(FuelComponent.IdleNBO));
		// Assertions.assertEquals(0, details.getFuelUnitPrice(FuelComponent.Cooldown));
	}

	@Test
	public void testCalculateCooldownCost_NonLoad() {
		final IVessel vessel = Mockito.mock(IVessel.class);

		final PortSlot fromPortSlot = Mockito.mock(PortSlot.class);
		final PortSlot toPortSlot = Mockito.mock(PortSlot.class);

		final PortDetails fromPortDetails = new PortDetails(new PortOptions(fromPortSlot));

		final PortDetails toPortDetails = new PortDetails(new PortOptions(toPortSlot));

		final VoyageOptions voyageOptions = new VoyageOptions(fromPortSlot, toPortSlot);
		voyageOptions.setVesselState(VesselState.Ballast);
		final VoyageDetails voyageDetails = new VoyageDetails(voyageOptions);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IPortCooldownDataProvider portCooldownDataProvider = Mockito.mock(IPortCooldownDataProvider.class);
		calc.setPortCooldownProvider(portCooldownDataProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object
		// has a x10 multiplier to the previous value -= this makes it easy to add up
		// for the expectations
		fromPortDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 9);

		voyageDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 90);

		toPortDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 900);

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(fromPortSlot)).thenReturn(1);
		Mockito.when(portTimesRecord.getSlotTime(toPortSlot)).thenReturn(2);

		final IPort toPort = Mockito.mock(IPort.class);
		Mockito.when(toPortSlot.getPort()).thenReturn(toPort);

		final ICooldownCalculator cooldownCalculator = Mockito.mock(ICooldownCalculator.class);

		Mockito.when(portCooldownDataProvider.getCooldownCalculator(toPort)).thenReturn(cooldownCalculator);

		Mockito.when(mockPortCVProvider.getPortCV(toPort)).thenReturn(OptimiserUnitConvertor.convertToInternalConversionFactor(1.0));

		final long expectedCooldownCost = 1000;

		voyageDetails.getOptions().setShouldBeCold(VesselTankState.MUST_BE_COLD);
		voyageDetails.setCooldownPerformed(true);

		// Expect the non-load slot branch - time 3 == time of next Port
		Mockito.when(cooldownCalculator.calculateCooldownCost(vessel, toPort, OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), 2)).thenReturn(expectedCooldownCost);

		final long cooldownCost = calc.calculateCooldownCost(vessel, portTimesRecord, fromPortDetails, voyageDetails, toPortDetails);

		Mockito.verify(cooldownCalculator).calculateCooldownCost(vessel, toPort, OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), 2);

		Assertions.assertEquals(expectedCooldownCost, cooldownCost);
	}

	@Test
	public void testCalculateCooldownCost_Load() {
		final IVessel vessel = Mockito.mock(IVessel.class);

		final PortSlot fromPortSlot = Mockito.mock(PortSlot.class);
		final ILoadSlot toPortSlot = Mockito.mock(ILoadSlot.class);

		final PortDetails fromPortDetails = new PortDetails(new PortOptions(fromPortSlot));

		final PortDetails toPortDetails = new PortDetails(new PortOptions(toPortSlot));

		final VoyageOptions voyageOptions = new VoyageOptions(fromPortSlot, toPortSlot);
		voyageOptions.setVesselState(VesselState.Ballast);

		final VoyageDetails voyageDetails = new VoyageDetails(voyageOptions);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IPortCooldownDataProvider portCooldownDataProvider = Mockito.mock(IPortCooldownDataProvider.class);
		calc.setPortCooldownProvider(portCooldownDataProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object
		// has a x10 multiplier to the previous value -= this makes it easy to add up
		// for the expectations
		fromPortDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 9);

		voyageDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 90);

		toPortDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 900);

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(fromPortSlot)).thenReturn(1);
		Mockito.when(portTimesRecord.getSlotTime(toPortSlot)).thenReturn(2);

		final IPort toPort = Mockito.mock(IPort.class);
		Mockito.when(toPortSlot.getPort()).thenReturn(toPort);

		final ICooldownCalculator cooldownCalculator = Mockito.mock(ICooldownCalculator.class);

		Mockito.when(portCooldownDataProvider.getCooldownCalculator(toPort)).thenReturn(cooldownCalculator);

		Mockito.when(toPortSlot.getCargoCVValue()).thenReturn(OptimiserUnitConvertor.convertToInternalConversionFactor(1.0));

		final long expectedCooldownCost = 1000;

		voyageDetails.getOptions().setShouldBeCold(VesselTankState.MUST_BE_COLD);
		voyageDetails.setCooldownPerformed(true);

		// Expect the load slot branch - time 3 == time of next Port
		Mockito.when(cooldownCalculator.calculateCooldownCost(vessel, toPort, OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), 2)).thenReturn(expectedCooldownCost);

		final long cooldownCost = calc.calculateCooldownCost(vessel, portTimesRecord, fromPortDetails, voyageDetails, toPortDetails);

		Mockito.verify(cooldownCalculator).calculateCooldownCost(vessel, toPort, OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), 2);

		Assertions.assertEquals(expectedCooldownCost, cooldownCost);
	}

	@Test
	public void testCalculateCooldownNotCalled_Load() {
		final IVessel vessel = Mockito.mock(IVessel.class);

		final PortSlot fromPortSlot = Mockito.mock(PortSlot.class);
		final ILoadSlot toPortSlot = Mockito.mock(ILoadSlot.class);

		final PortDetails fromPortDetails = new PortDetails(new PortOptions(fromPortSlot));

		final PortDetails toPortDetails = new PortDetails(new PortOptions(toPortSlot));

		final VoyageOptions voyageOptions = new VoyageOptions(fromPortSlot, toPortSlot);
		voyageOptions.setVesselState(VesselState.Ballast);
		final VoyageDetails voyageDetails = new VoyageDetails(voyageOptions);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IPortCooldownDataProvider portCooldownDataProvider = Mockito.mock(IPortCooldownDataProvider.class);
		calc.setPortCooldownProvider(portCooldownDataProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object
		// has a x10 multiplier to the previous value -= this makes it easy to add up
		// for the expectations
		fromPortDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 9);

		voyageDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 90);

		toPortDetails.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 900);

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(fromPortSlot)).thenReturn(1);
		Mockito.when(portTimesRecord.getSlotTime(toPortSlot)).thenReturn(2);

		final IPort toPort = Mockito.mock(IPort.class);
		Mockito.when(toPortSlot.getPort()).thenReturn(toPort);

		final ICooldownCalculator cooldownCalculator = Mockito.mock(ICooldownCalculator.class);

		Mockito.when(portCooldownDataProvider.getCooldownCalculator(toPort)).thenReturn(cooldownCalculator);

		Mockito.when(toPortSlot.getCargoCVValue()).thenReturn(OptimiserUnitConvertor.convertToInternalConversionFactor(1.0));

		final long expectedCooldownCost = 1000;

		voyageDetails.getOptions().setShouldBeCold(VesselTankState.MUST_BE_COLD);
		voyageDetails.setCooldownPerformed(false);

		// Expect the load slot branch - time 3 == time of next Port
		Mockito.when(cooldownCalculator.calculateCooldownCost(vessel, toPort, OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), 2)).thenReturn(expectedCooldownCost);

		final long cooldownCost = calc.calculateCooldownCost(vessel, portTimesRecord, fromPortDetails, voyageDetails, toPortDetails);

		Mockito.verifyNoInteractions(cooldownCalculator);

		Assertions.assertEquals(0, cooldownCost);

	}

	public Vessel createSampleVessel() {

		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(10), (long) OptimiserUnitConvertor.convertToInternalDailyRate(50));
		keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(20), (long) OptimiserUnitConvertor.convertToInternalDailyRate(100));

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);
		// Capacity is currently not used by test
		final Vessel vessel = new Vessel("class-1", 0);

		vessel.setConsumptionRate(VesselState.Laden, calc);
		vessel.setConsumptionRate(VesselState.Ballast, calc);
		vessel.setMinSpeed(OptimiserUnitConvertor.convertToInternalSpeed(VESSEL_MIN_SPEED));
		vessel.setMaxSpeed(OptimiserUnitConvertor.convertToInternalSpeed(VESSEL_MAX_SPEED));

		vessel.setNBORate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150));
		vessel.setNBORate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150));

		vessel.setIdleNBORate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150));
		vessel.setIdleNBORate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150));

		vessel.setIdleConsumptionRate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(10));
		vessel.setIdleConsumptionRate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(10));

		vessel.setPilotLightRate(OptimiserUnitConvertor.convertToInternalDailyRate(1));

		final IBaseFuel baseFuel = new BaseFuel(indexingContext, "test");
		baseFuel.setEquivalenceFactor(OptimiserUnitConvertor.convertToInternalConversionFactor(45.6));
		vessel.setTravelBaseFuel(baseFuel);
		vessel.setIdleBaseFuel(new BaseFuel(indexingContext, "IDLE"));
		vessel.setPilotLightBaseFuel(new BaseFuel(indexingContext, "PILOT"));
		vessel.setInPortBaseFuel(new BaseFuel(indexingContext, "PORT"));

		// 2 days of boil off
		vessel.setSafetyHeel(OptimiserUnitConvertor.convertToInternalVolume(150 * HEEL_DURATION));

		vessel.setInPortConsumptionRateInMTPerDay(PortType.Load, OptimiserUnitConvertor.convertToInternalDailyRate(35));
		vessel.setInPortConsumptionRateInMTPerDay(PortType.Discharge, OptimiserUnitConvertor.convertToInternalDailyRate(45));

		// vesselClass.setMinNBOSpeed(VesselState.Laden,OptimiserUnitConvertor.convertToInternalSpeed(15));
		// vesselClass.setMinNBOSpeed(VesselState.Ballast,OptimiserUnitConvertor.convertToInternalSpeed(15));

		return vessel;
	}

	public PortOptions createSamplePortOptions() {

		final IPortSlot slot = Mockito.mock(IPortSlot.class, "slot");

		final PortOptions options = new PortOptions(slot);

		options.setVisitDuration(0);

		final Vessel vessel = createSampleVessel();

		options.setVessel(vessel);

		return options;
	}
}
