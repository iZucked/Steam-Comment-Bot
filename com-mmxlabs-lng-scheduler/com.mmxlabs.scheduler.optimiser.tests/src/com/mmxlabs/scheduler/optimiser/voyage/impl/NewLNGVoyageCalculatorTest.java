/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class NewLNGVoyageCalculatorTest {

	private static final int VESSEL_NBO_SPEED = 15;
	private static final int VESSEL_MAX_SPEED = 20;
	private static final int VESSEL_MIN_SPEED = 10;
	private static final int HEEL_DURATION = 2 * 24;

	@Test
	public void testCalculateSpeed() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options - no NBO to avoid min NBO trigger
		options.setUseNBOForTravel(false);

		// Set distance/time to give the expected speed exactly
		final int expectedSpeed = VESSEL_NBO_SPEED + 1;
		options.setAvailableTime(48);
		options.setDistance(expectedSpeed * 48);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_MinSpeedClamp() {
		final VoyageOptions options = createSampleVoyageOptions();

		// Min Speed is 10 - See createSampleVesselClass
		final int expectedMinSpeed = VESSEL_MIN_SPEED;

		// Populate options
		options.setUseNBOForTravel(false);

		// Set distance time so that the speed would be below the expected min speed
		options.setAvailableTime(48 * 3);
		options.setDistance(expectedMinSpeed * 48);

		// Sanity check that inputs will yield a speed lower than the expected min speed
		Assert.assertTrue(options.getDistance() / options.getAvailableTime() < expectedMinSpeed);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		// Check speed is clamped to the minumum
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMinSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_MinNBOSpeedClamp() {
		final VoyageOptions options = createSampleVoyageOptions();

		// Min Speed is 15 - See createSampleVesselClass
		final int expectedMinSpeed = VESSEL_NBO_SPEED;

		// Populate options
		options.setUseNBOForTravel(true);

		// Set distance time so that the speed would be below the expected min speed
		options.setAvailableTime(48 * 15);
		options.setDistance(expectedMinSpeed * 48);

		// Sanity check that inputs will yield a speed lower than the expected min speed
		Assert.assertTrue(options.getDistance() / options.getAvailableTime() < expectedMinSpeed);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		// Check speed is clamped to the minumum
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMinSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_MaxSpeedClamp() {
		final VoyageOptions options = createSampleVoyageOptions();

		// Max Speed is 20 - See createSampleVesselClass
		final int expectedMaxSpeed = VESSEL_MAX_SPEED;

		// Populate options
		options.setUseNBOForTravel(false);

		// Set distance time so that the speed would be below the expected min speed
		options.setAvailableTime(48 / 2);
		options.setDistance(expectedMaxSpeed * 48);

		// Sanity check that inputs will yield a speed higher than the expected max speed
		Assert.assertTrue(options.getDistance() / options.getAvailableTime() > expectedMaxSpeed);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		// Check speed is clamped to the minumum
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMaxSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_ZeroDistance() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options - no NBO to avoid min NBO trigger
		options.setUseNBOForTravel(false);

		// No distance, no speed!
		final int expectedSpeed = 0;

		options.setAvailableTime(48);
		options.setDistance(0);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedSpeed), speed);
	}

	@Test
	public void testCalculateSpeed_SmallDistance() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options - no NBO to avoid min NBO trigger
		options.setUseNBOForTravel(false);

		// Small distance, expect to hit min speed!
		// Min Speed is 10 - See createSampleVesselClass
		final int expectedMinSpeed = VESSEL_MIN_SPEED;

		options.setAvailableTime(48);
		options.setDistance(1);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final int speed = calc.calculateSpeed(options, options.getAvailableTime());

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(expectedMinSpeed), speed);
	}

	@Test
	public void testCalculateTravelFuelRequirements_NBOOnly() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);

		final VesselState vesselState = VesselState.Laden;

		final int speed = VESSEL_NBO_SPEED - 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setDistance(speed * travelTime);

		// Sanity check that we are travelling slower than NBO rate so there will be no supplement
		Assert.assertTrue(OptimiserUnitConvertor.convertToInternalSpeed(speed) < options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateTravelFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, travelTime, OptimiserUnitConvertor.convertToInternalSpeed(speed));

		// Check results
		final long nboRate = options.getVessel().getVesselClass().getNBORate(vesselState);
		final long pilotLightRate = options.getVessel().getVesselClass().getPilotLightRate();

		Assert.assertEquals(nboRate * travelTime, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		// Expect some pilot light
		Assert.assertTrue(details.getFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()) > 0);

		Assert.assertEquals(pilotLightRate * travelTime, details.getFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateTravelFuelRequirements_NBO_FBO() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseFBOForSupplement(true);

		final VesselState vesselState = VesselState.Laden;
		final int speed = VESSEL_NBO_SPEED + 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setDistance(speed * travelTime);

		// Sanity check that we are travelling slower than NBO rate so there will be no supplement
		final int internalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(speed);
		Assert.assertTrue(internalSpeed > options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateTravelFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, travelTime, internalSpeed);

		// Expected NBO rate
		final long nboRate = options.getVessel().getVesselClass().getNBORate(vesselState);

		// Expected pilot light rate
		final long pilotLightRate = options.getVessel().getVesselClass().getPilotLightRate();

		// Expected pure base consumption rates
		final long expectedBaseConsumption = options.getVessel().getVesselClass().getConsumptionRate(vesselState).getRate(internalSpeed);

		Assert.assertEquals(nboRate * travelTime, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		// Equivalence is 0.5 so double to convert rate to m3
		Assert.assertEquals((2 * expectedBaseConsumption - nboRate) * travelTime, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertTrue(details.getFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()) > 0);
		// Expect some pilot light
		Assert.assertEquals(pilotLightRate * travelTime, details.getFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateTravelFuelRequirements_NBO_BaseSupplement() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseFBOForSupplement(false);

		final VesselState vesselState = VesselState.Laden;
		final int speed = VESSEL_NBO_SPEED + 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setDistance(speed * travelTime);

		// Sanity check that we are travelling slower than NBO rate so there will be no supplement
		final int internalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(speed);
		Assert.assertTrue(internalSpeed > options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateTravelFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, travelTime, internalSpeed);

		// Expected NBO rate
		final long nboRate = options.getVessel().getVesselClass().getNBORate(vesselState);

		// Expected pure base consumption rates
		final long expectedBaseConsumption = options.getVessel().getVesselClass().getConsumptionRate(vesselState).getRate(internalSpeed);

		Assert.assertEquals(nboRate * travelTime, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		// Some supplement expected
		Assert.assertEquals((expectedBaseConsumption - nboRate / 2) * travelTime, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		// Equivalence is 0.5
		Assert.assertTrue(details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()) > 0);

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateTravelFuelRequirements_Base_Only() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);

		final VesselState vesselState = VesselState.Laden;
		final int speed = VESSEL_NBO_SPEED + 1;
		final int travelTime = 48;
		options.setAvailableTime(travelTime);
		options.setDistance(speed * travelTime);

		// Sanity check that we are travelling slower than NBO rate so there will be no supplement
		final int internalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(speed);
		Assert.assertTrue(internalSpeed > options.getNBOSpeed());

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateTravelFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, travelTime, internalSpeed);

		// Expected pure base consumption rates
		final long expectedBaseConsumption = options.getVessel().getVesselClass().getConsumptionRate(vesselState).getRate(internalSpeed);

		Assert.assertEquals(expectedBaseConsumption * travelTime, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_NBO() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = 48;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		final long nboIdleRate = options.getVessel().getVesselClass().getIdleNBORate(vesselState);
		final long pilotLightRate = options.getVessel().getVesselClass().getIdlePilotLightRate();

		Assert.assertEquals(nboIdleRate * idleTime, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
		// Expect some pilot light
		Assert.assertTrue(details.getFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit()) > 0);
		Assert.assertEquals(pilotLightRate * idleTime, details.getFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_HotelLoad_NoHeel() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = 48;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		final long baseRate = options.getVessel().getVesselClass().getIdleConsumptionRate(vesselState);

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(baseRate * idleTime, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_HotelLoad_MinHeel() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(false);

		final VesselState vesselState = VesselState.Laden;

		// Expect 2 days of heel
		final int heelDuration = HEEL_DURATION;
		final int idleTime = 96;

		// Sanity check input
		Assert.assertTrue(heelDuration < idleTime);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		final long nboIdleRate = options.getVessel().getVesselClass().getIdleNBORate(vesselState);
		final long pilotLightRate = options.getVessel().getVesselClass().getIdlePilotLightRate();
		final long baseRate = options.getVessel().getVesselClass().getIdleConsumptionRate(vesselState);

		Assert.assertEquals(nboIdleRate * heelDuration, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
		Assert.assertEquals(pilotLightRate * heelDuration, details.getFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(baseRate * (idleTime - heelDuration), details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_Required() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getVesselClass().getWarmupTime() * 2;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		final long cooldownVolume = options.getVessel().getVesselClass().getCooldownVolume();

		Assert.assertEquals(cooldownVolume, details.getFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_BelowWarmupTime() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getVesselClass().getWarmupTime() - 1;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		final long cooldownVolume = options.getVessel().getVesselClass().getCooldownVolume();

		Assert.assertEquals(cooldownVolume, details.getFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_NotRequired_IdleNBO() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getVesselClass().getWarmupTime() * 2;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateIdleFuelRequirements_Cooldown_NotRequired_HotelLoad() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(false);

		final VesselState vesselState = VesselState.Laden;

		final int idleTime = options.getVessel().getVesselClass().getWarmupTime() - 1;
		// Set heel to zero to avoid it getting in the way
		((VesselClass) options.getVessel().getVesselClass()).setMinHeel(OptimiserUnitConvertor.convertToInternalVolume(0));

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateIdleFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, idleTime);

		// Check results
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit()));

	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_NBOOnly() {

		// In this test, we check that we only use travel NBO - speed is lower than NBO speed.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseFBOForSupplement(true);

		final VesselState vesselState = VesselState.Laden;

		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalHourlyRate(150);
		final long routeCost = 100000;

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel().getVesselClass())).thenReturn(additionalRouteTime);
		Mockito.when(mockRouteCostProvider.getRouteCost(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(routeCost);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(expectedBaseConsumption);

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, additionalRouteTime);

		// Check results
		final long pilotLightRate = options.getVessel().getVesselClass().getPilotLightRate();

		Assert.assertEquals(nboRate * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals((2 * expectedBaseConsumption - nboRate) * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		// Expect some pilot light
		Assert.assertTrue(details.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()) > 0);

		Assert.assertEquals(pilotLightRate * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(routeCost, details.getRouteCost());

	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_NBO_FBO() {

		// In this test, we check that we only use travel NBO.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseFBOForSupplement(true);

		final VesselState vesselState = VesselState.Laden;
		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long routeCost = 100000;

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel().getVesselClass())).thenReturn(additionalRouteTime);
		Mockito.when(mockRouteCostProvider.getRouteCost(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(routeCost);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(expectedBaseConsumption);

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, additionalRouteTime);
		final long pilotLightRate = options.getVessel().getVesselClass().getPilotLightRate();

		// Expected pure base consumption rates

		Assert.assertEquals(nboRate * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		// Equivalence is 0.5 so double to convert rate to m3
		Assert.assertEquals((2 * expectedBaseConsumption - nboRate) * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertTrue(details.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()) > 0);
		// Expect some pilot light
		Assert.assertEquals(pilotLightRate * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(routeCost, details.getRouteCost());

	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_NBO_BaseSupplement() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseFBOForSupplement(false);
		options.setRoute("ROUTE");

		final VesselState vesselState = VesselState.Laden;
		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long routeCost = 100000;

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel().getVesselClass())).thenReturn(additionalRouteTime);
		Mockito.when(mockRouteCostProvider.getRouteCost(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(routeCost);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(expectedBaseConsumption);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, additionalRouteTime);

		Assert.assertEquals(nboRate * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		// Some supplement expected
		Assert.assertEquals(((expectedBaseConsumption * 2 - nboRate) * additionalRouteTime) / 2,
				details.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		// Equivalence is 0.5
		Assert.assertTrue(details.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()) > 0);

		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(routeCost, details.getRouteCost());
	}

	@Test
	public void testCalculateRouteAdditionalFuelRequirements_Base_Only() {

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);

		final VesselState vesselState = VesselState.Laden;
		final int additionalRouteTime = 48;

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final long nboRate = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long expectedBaseConsumption = OptimiserUnitConvertor.convertToInternalHourlyRate(50);
		final long routeCost = 100000;

		Mockito.when(mockRouteCostProvider.getRouteTransitTime(options.getRoute(), options.getVessel().getVesselClass())).thenReturn(additionalRouteTime);
		Mockito.when(mockRouteCostProvider.getRouteCost(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(routeCost);
		Mockito.when(mockRouteCostProvider.getRouteNBORate(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(nboRate);
		Mockito.when(mockRouteCostProvider.getRouteFuelUsage(options.getRoute(), options.getVessel().getVesselClass(), vesselState)).thenReturn(expectedBaseConsumption);

		calc.calculateRouteAdditionalFuelRequirements(options, details, options.getVessel().getVesselClass(), vesselState, additionalRouteTime);

		Assert.assertEquals(expectedBaseConsumption * additionalRouteTime, details.getRouteAdditionalConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(routeCost, details.getRouteCost());

	}

	VoyageOptions createSampleVoyageOptions() {

		final IPortSlot from = Mockito.mock(IPortSlot.class, "from");
		final IPortSlot to = Mockito.mock(IPortSlot.class, "to");

		final VoyageOptions options = new VoyageOptions();

		options.setFromPortSlot(from);
		options.setToPortSlot(to);

		options.setNBOSpeed(OptimiserUnitConvertor.convertToInternalSpeed(VESSEL_NBO_SPEED));

		final VesselClass vesselClass = createSampleVesselClass();

		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		options.setVessel(vessel);
		options.setVesselState(VesselState.Laden);

		options.setAvailableLNG(Long.MAX_VALUE);

		return options;
	}

	@Test
	public void testCalculatePortFuelRequirements1() {
		testCalculatePortFuelRequirements(PortType.Load, 13, 11, 143);
	}

	@Test
	public void testCalculatePortFuelRequirements2() {
		testCalculatePortFuelRequirements(PortType.Discharge, 22, 101, 2222);
	}

	public void testCalculatePortFuelRequirements(final PortType portType, final int visitDuration, final int consumptionRate, final long target) {

		// In this test, we check that we only use travel NBO.

		final PortOptions options = createSamplePortOptions();

		options.setVisitDuration(visitDuration);

		final PortDetails details = new PortDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final VesselClass vesselClass = (VesselClass) options.getVessel().getVesselClass();

		vesselClass.setInPortConsumptionRate(portType, consumptionRate);

		Mockito.when(options.getPortSlot().getPortType()).thenReturn(portType);

		calc.calculatePortFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());

		Assert.assertEquals(target, details.getFuelConsumption(FuelComponent.Base));
	}

	@Test
	public void testCalculateVoyagePlanFuelConsumptions() {
		final int expectedBasePrice = 99000;

		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vesselClass.getBaseFuelUnitPrice()).thenReturn(expectedBasePrice);

		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());

		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.getOptions().setPortSlot(loadSlot);
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(-1));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(-1));

		final VoyageDetails details = new VoyageDetails();
		final VoyageOptions options = new VoyageOptions();
		options.setVesselState(VesselState.Laden);
		details.setOptions(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object has a x10 multiplier to the previous value -= this makes it easy to add up for the expectations
		loadDetails.setFuelConsumption(FuelComponent.Base, 1);
		loadDetails.setFuelConsumption(FuelComponent.NBO, 2);
		loadDetails.setFuelConsumption(FuelComponent.FBO, 3);
		loadDetails.setFuelConsumption(FuelComponent.Base_Supplemental, 4);
		loadDetails.setFuelConsumption(FuelComponent.IdleNBO, 5);
		loadDetails.setFuelConsumption(FuelComponent.IdleBase, 6);
		loadDetails.setFuelConsumption(FuelComponent.PilotLight, 7);
		loadDetails.setFuelConsumption(FuelComponent.IdlePilotLight, 8);
		loadDetails.setFuelConsumption(FuelComponent.Cooldown, 9);

		details.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 10);
		details.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 20);
		details.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 30);
		details.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 40);
		details.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 50);
		details.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 60);
		details.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 70);
		details.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 80);
		details.setFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit(), 90);

		dischargeDetails.setFuelConsumption(FuelComponent.Base, 100);
		dischargeDetails.setFuelConsumption(FuelComponent.NBO, 200);
		dischargeDetails.setFuelConsumption(FuelComponent.FBO, 300);
		dischargeDetails.setFuelConsumption(FuelComponent.Base_Supplemental, 400);
		dischargeDetails.setFuelConsumption(FuelComponent.IdleNBO, 500);
		dischargeDetails.setFuelConsumption(FuelComponent.IdleBase, 600);
		dischargeDetails.setFuelConsumption(FuelComponent.PilotLight, 700);
		dischargeDetails.setFuelConsumption(FuelComponent.IdlePilotLight, 800);
		dischargeDetails.setFuelConsumption(FuelComponent.Cooldown, 900);

		final long[] result = calc.calculateVoyagePlanFuelConsumptions(vessel, loadDetails, details, dischargeDetails);

		Assert.assertEquals(11, result[FuelComponent.Base.ordinal()]);
		Assert.assertEquals(22, result[FuelComponent.NBO.ordinal()]);
		Assert.assertEquals(33, result[FuelComponent.FBO.ordinal()]);
		Assert.assertEquals(44, result[FuelComponent.Base_Supplemental.ordinal()]);
		Assert.assertEquals(55, result[FuelComponent.IdleNBO.ordinal()]);
		Assert.assertEquals(66, result[FuelComponent.IdleBase.ordinal()]);
		Assert.assertEquals(77, result[FuelComponent.PilotLight.ordinal()]);
		Assert.assertEquals(88, result[FuelComponent.IdlePilotLight.ordinal()]);
		Assert.assertEquals(99, result[FuelComponent.Cooldown.ordinal()]);

		Assert.assertEquals(expectedBasePrice, details.getFuelUnitPrice(FuelComponent.Base));
		Assert.assertEquals(expectedBasePrice, details.getFuelUnitPrice(FuelComponent.Base_Supplemental));
		Assert.assertEquals(expectedBasePrice, details.getFuelUnitPrice(FuelComponent.PilotLight));
		Assert.assertEquals(expectedBasePrice, details.getFuelUnitPrice(FuelComponent.IdlePilotLight));
		Assert.assertEquals(expectedBasePrice, details.getFuelUnitPrice(FuelComponent.IdleBase));

		// Expect no change in LNG price
		Assert.assertEquals(0, details.getFuelUnitPrice(FuelComponent.FBO));
		Assert.assertEquals(0, details.getFuelUnitPrice(FuelComponent.NBO));
		Assert.assertEquals(0, details.getFuelUnitPrice(FuelComponent.IdleNBO));
		Assert.assertEquals(0, details.getFuelUnitPrice(FuelComponent.Cooldown));
	}

	@Test
	public void testCalculateCooldownPrices_NonLoad() {
		final int expectedBasePrice = 99000;

		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vesselClass.getBaseFuelUnitPrice()).thenReturn(expectedBasePrice);

		final PortDetails fromPortDetails = new PortDetails();
		fromPortDetails.setOptions(new PortOptions());

		final PortDetails toPortDetails = new PortDetails();
		toPortDetails.setOptions(new PortOptions());

		final PortSlot fromPortSlot = Mockito.mock(PortSlot.class);
		final PortSlot toPortSlot = Mockito.mock(PortSlot.class);

		fromPortDetails.getOptions().setPortSlot(fromPortSlot);
		toPortDetails.getOptions().setPortSlot(toPortSlot);

		final VoyageDetails voyageDetails = new VoyageDetails();
		final VoyageOptions voyageOptions = new VoyageOptions();
		voyageOptions.setVesselState(VesselState.Ballast);
		voyageDetails.setOptions(voyageOptions);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object has a x10 multiplier to the previous value -= this makes it easy to add up for the expectations
		fromPortDetails.setFuelConsumption(FuelComponent.Cooldown, 9);

		voyageDetails.setFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit(), 90);

		toPortDetails.setFuelConsumption(FuelComponent.Cooldown, 900);

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		arrivalTimes.add(1);
		arrivalTimes.add(2);

		final IPort toPort = Mockito.mock(IPort.class);
		Mockito.when(toPortSlot.getPort()).thenReturn(toPort);

		final ICooldownPriceCalculator cooldownPriceCalculator = Mockito.mock(ICooldownPriceCalculator.class);
		voyageDetails.getOptions().setToPortSlot(toPortSlot);

		Mockito.when(toPort.getCooldownPriceCalculator()).thenReturn(cooldownPriceCalculator);

		Mockito.when(mockPortCVProvider.getPortCV(toPort)).thenReturn(OptimiserUnitConvertor.convertToInternalConversionFactor(1.0));

		final int expectedCooldownPrice = 1000;

		voyageDetails.getOptions().setShouldBeCold(true);
		voyageDetails.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, 1000);

		// Expect the non-load slot branch - time 3 == time of next Port
		Mockito.when(cooldownPriceCalculator.calculateCooldownUnitPrice(2)).thenReturn(expectedCooldownPrice);

		final int cooldownM3Price = calc.calculateCooldownPrices(vessel.getVesselClass(), arrivalTimes, fromPortDetails, voyageDetails, toPortDetails);

		Mockito.verify(cooldownPriceCalculator).calculateCooldownUnitPrice(2);

		Assert.assertEquals(expectedCooldownPrice, cooldownM3Price);
		Assert.assertEquals(expectedCooldownPrice, voyageDetails.getFuelUnitPrice(FuelComponent.Cooldown));

	}

	@Test
	public void testCalculateCooldownPrices_Load() {
		final int expectedBasePrice = 99000;

		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vesselClass.getBaseFuelUnitPrice()).thenReturn(expectedBasePrice);

		final PortDetails fromPortDetails = new PortDetails();
		fromPortDetails.setOptions(new PortOptions());

		final PortDetails toPortDetails = new PortDetails();
		toPortDetails.setOptions(new PortOptions());

		final PortSlot fromPortSlot = Mockito.mock(PortSlot.class);
		final ILoadSlot toPortSlot = Mockito.mock(ILoadSlot.class);

		fromPortDetails.getOptions().setPortSlot(fromPortSlot);
		toPortDetails.getOptions().setPortSlot(toPortSlot);

		final VoyageDetails voyageDetails = new VoyageDetails();
		final VoyageOptions voyageOptions = new VoyageOptions();
		voyageOptions.setVesselState(VesselState.Ballast);
		voyageDetails.setOptions(voyageOptions);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		// Set fuel consumptions with a special pattern - each subsequent details object has a x10 multiplier to the previous value -= this makes it easy to add up for the expectations
		fromPortDetails.setFuelConsumption(FuelComponent.Cooldown, 9);

		voyageDetails.setFuelConsumption(FuelComponent.Cooldown, FuelComponent.Cooldown.getDefaultFuelUnit(), 90);

		toPortDetails.setFuelConsumption(FuelComponent.Cooldown, 900);

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		arrivalTimes.add(1);
		arrivalTimes.add(2);
		arrivalTimes.add(3);

		final IPort toPort = Mockito.mock(IPort.class);
		Mockito.when(toPortSlot.getPort()).thenReturn(toPort);

		final ICooldownPriceCalculator cooldownPriceCalculator = Mockito.mock(ICooldownPriceCalculator.class);
		voyageDetails.getOptions().setToPortSlot(toPortSlot);

		Mockito.when(toPort.getCooldownPriceCalculator()).thenReturn(cooldownPriceCalculator);

		Mockito.when(toPortSlot.getCargoCVValue()).thenReturn(OptimiserUnitConvertor.convertToInternalConversionFactor(1.0));

		final int expectedCooldownPrice = 1000;

		voyageDetails.getOptions().setShouldBeCold(true);
		voyageDetails.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, 1000);

		// Expect the load slot branch - time 3 == time of next Port
		Mockito.when(cooldownPriceCalculator.calculateCooldownUnitPrice(toPortSlot, 2)).thenReturn(expectedCooldownPrice);

		final int cooldownM3Price = calc.calculateCooldownPrices(vessel.getVesselClass(), arrivalTimes, fromPortDetails, voyageDetails, toPortDetails);

		Mockito.verify(cooldownPriceCalculator).calculateCooldownUnitPrice(toPortSlot, 2);

		Assert.assertEquals(expectedCooldownPrice, cooldownM3Price);
		Assert.assertEquals(expectedCooldownPrice, voyageDetails.getFuelUnitPrice(FuelComponent.Cooldown));

	}

	public VesselClass createSampleVesselClass() {
		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(10), (long) OptimiserUnitConvertor.convertToInternalDailyRate(50));
		keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(20), (long) OptimiserUnitConvertor.convertToInternalDailyRate(100));

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);
		final VesselClass vesselClass = new VesselClass();

		vesselClass.setConsumptionRate(VesselState.Laden, calc);
		vesselClass.setConsumptionRate(VesselState.Ballast, calc);
		vesselClass.setMinSpeed(OptimiserUnitConvertor.convertToInternalSpeed(VESSEL_MIN_SPEED));
		vesselClass.setMaxSpeed(OptimiserUnitConvertor.convertToInternalSpeed(VESSEL_MAX_SPEED));

		vesselClass.setNBORate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150));
		vesselClass.setNBORate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150));

		vesselClass.setIdleNBORate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150));
		vesselClass.setIdleNBORate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150));

		vesselClass.setIdleConsumptionRate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(10));
		vesselClass.setIdleConsumptionRate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(10));

		vesselClass.setPilotLightRate(OptimiserUnitConvertor.convertToInternalDailyRate(1));
		vesselClass.setIdlePilotLightRate(OptimiserUnitConvertor.convertToInternalDailyRate(0.5));

		vesselClass.setName("class-1");

		vesselClass.setBaseFuelConversionFactor(OptimiserUnitConvertor.convertToInternalConversionFactor(0.5));

		// 2 days of boil off
		vesselClass.setMinHeel(OptimiserUnitConvertor.convertToInternalVolume(150 * HEEL_DURATION));

		vesselClass.setInPortConsumptionRate(PortType.Load, OptimiserUnitConvertor.convertToInternalDailyRate(35));
		vesselClass.setInPortConsumptionRate(PortType.Discharge, OptimiserUnitConvertor.convertToInternalDailyRate(45));

		// vesselClass.setMinNBOSpeed(VesselState.Laden,OptimiserUnitConvertor.convertToInternalSpeed(15));
		// vesselClass.setMinNBOSpeed(VesselState.Ballast,OptimiserUnitConvertor.convertToInternalSpeed(15));

		// Currently not used by test
		vesselClass.setCargoCapacity(0);

		return vesselClass;
	}

	public PortOptions createSamplePortOptions() {

		final IPortSlot slot = Mockito.mock(IPortSlot.class, "slot");

		final PortOptions options = new PortOptions();

		options.setPortSlot(slot);
		options.setVisitDuration(0);

		final VesselClass vesselClass = createSampleVesselClass();

		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		options.setVessel(vessel);

		return options;
	}
}
