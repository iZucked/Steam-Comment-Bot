/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

public class LNGVoyageCalculatorTest {

	@Test
	public void testCalculateVoyageFuelRequirements1() {

		// In this test, we check that we only use travel NBO.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements2() {

		// In this test, we check that we only use travel Base.

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(75 * 48), details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements3() {

		// In this test, we check that we use travel and idle NBO

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(96);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(48, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48), details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements4() {

		// In this test, we check that we use travel and idle base

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(96);
		options.setDistance(15 * 48);
		final String route = "route";
		options.setRoute(route);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(10), details.getSpeed());
		Assert.assertEquals(72, details.getTravelTime());
		Assert.assertEquals(24, details.getIdleTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(50 * 72), details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(10 * 24), details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements5() {

		// In this test, we check that we use travel NBO and idle base
		// This implies min heel run down

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(120);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(48, details.getTravelTime());
		Assert.assertEquals(72, details.getIdleTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(10 * 24), details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48), details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements6() {

		// In this test, we check that we only use travel NBO - however due to
		// decreased time, expect FBO
		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(36);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(20), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 36), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(50 * 36), details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements7() {

		// In this test, we check that we travel on NBO and base supplement
		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(36);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(20), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(25 * 36), details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 36), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements8() {

		// In this test, there is not enough travel time, expect us to travel as
		// fast as possible to get to our destination.
		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(20);
		options.setDistance(15 * 48);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(20), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 36), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(50 * 36), details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	VoyageOptions createSampleVoyageOptions() {

		final IPortSlot from = Mockito.mock(IPortSlot.class, "from");
		final IPortSlot to = Mockito.mock(IPortSlot.class, "to");

		final VoyageOptions options = new VoyageOptions();

		options.setFromPortSlot(from);
		options.setToPortSlot(to);

		options.setNBOSpeed(OptimiserUnitConvertor.convertToInternalSpeed(15));

		final VesselClass vesselClass = createSampleVesselClass();

		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		options.setVessel(vessel);
		options.setVesselState(VesselState.Laden);

		options.setAvailableLNG(Long.MAX_VALUE);

		return options;
	}

	@Test
	public void testCalculateVoyageFuelRequirements9() {

		// In this test, we check zero distance is handled correctly

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(0);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(0, details.getSpeed());
		Assert.assertEquals(48, details.getIdleTime());
		Assert.assertEquals(0, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48), details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements10() {

		// In this test, we check that we only use travel NBO.
		// A canal route has been added and should be costed

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(15 * 24);
		final String routeName = "Canal";
		options.setRoute(routeName);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor("Name", "Default");
		calc.setRouteCostDataComponentProvider(routeCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		routeCostProvider.setDefaultRouteCost(routeName, 200000);
		routeCostProvider.setRouteTransitTime(routeName, vesselClass, 24);
		routeCostProvider.setRouteFuel(routeName, vesselClass, VesselState.Laden, 1000, 50000);
		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		/* standard consumption */
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 24), details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

		/* route cost consumption */
		Assert.assertEquals((50000 * 24), details.getRouteAdditionalConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements11() {

		// In this test, we check that we only use travel NBO.
		// A canal route has been added and should be costed

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(48);
		options.setDistance(15 * 24);
		final String routeName = "Canal";
		options.setRoute(routeName);

		final VoyageDetails details = new VoyageDetails();

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor("Name", "Default");
		calc.setRouteCostDataComponentProvider(routeCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		routeCostProvider.setDefaultRouteCost(routeName, 200000);
		routeCostProvider.setRouteTransitTime(routeName, vesselClass, 24);
		routeCostProvider.setRouteFuel(routeName, vesselClass, VesselState.Laden, 1000, 50000);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		/* standard consumption */
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(75 * 24), details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

		/* route cost consumption */
		Assert.assertEquals((1000 * 24), details.getRouteAdditionalConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyagePlan1() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

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

		final Object[] sequence = new Object[] { loadDetails, details, dischargeDetails };

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		for (int i = 0; i < 1 + (sequence.length / 2); ++i) {
			arrivalTimes.add(0);
		}

		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);
		for (final FuelComponent fuel : FuelComponent.values()) {
			expectedPlan.setFuelConsumption(fuel, 0);
			expectedPlan.setTotalFuelCost(fuel, 0);
		}

		Assert.assertEquals(expectedPlan, plan);
	}

	@Test
	public void testCalculateVoyagePlan2() {

		final VoyagePlan plan = new VoyagePlan();

		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setBaseFuelUnitPrice(OptimiserUnitConvertor.convertToInternalPrice(2));
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vessel.getCargoCapacity()).thenReturn(Long.MAX_VALUE);


		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());
		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.getOptions().setPortSlot(loadSlot);
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(150000l);
		dischargeSlot.setMaxDischargeVolume(30000l);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));
		loadSlot.setCargoCVValue(OptimiserUnitConvertor.convertToInternalConversionFactor(2));

		final VoyageDetails details = new VoyageDetails();
		final VoyageOptions options = new VoyageOptions();
		options.setVesselState(VesselState.Laden);
		details.setOptions(options);

		details.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 10000);
		details.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20000);
		details.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 30000);
		details.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 40000);
		details.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 50000);
		details.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 60000);
		details.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 70000);
		details.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 80000);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final Object[] sequence = new Object[] { loadDetails, details, dischargeDetails };

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		for (int i = 0; i < 1 + (sequence.length / 2); ++i) {
			arrivalTimes.add(0);
		}
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);

		expectedPlan.setFuelConsumption(FuelComponent.Base, 10000);
		expectedPlan.setFuelConsumption(FuelComponent.Base_Supplemental, 20000);
		expectedPlan.setFuelConsumption(FuelComponent.NBO, 30000);
		expectedPlan.setFuelConsumption(FuelComponent.FBO, 40000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleNBO, 50000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleBase, 60000);
		expectedPlan.setFuelConsumption(FuelComponent.PilotLight, 70000);
		expectedPlan.setFuelConsumption(FuelComponent.IdlePilotLight, 80000);

		expectedPlan.setTotalFuelCost(FuelComponent.Base, 20000);
		expectedPlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 40000);
		expectedPlan.setTotalFuelCost(FuelComponent.NBO, 60000);
		expectedPlan.setTotalFuelCost(FuelComponent.FBO, 80000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleNBO, 100000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleBase, 120000);
		expectedPlan.setTotalFuelCost(FuelComponent.PilotLight, 140000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdlePilotLight, 160000);
		expectedPlan.setLNGFuelVolume(120000);

		Assert.assertEquals(expectedPlan, plan);
	}

	@Ignore("No longer works after LNGVoyageCalculator#init() removal")
	@Test(expected = RuntimeException.class)
	public void testCalculateVoyagePlan3() {

		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vessel.getCargoCapacity()).thenReturn(Long.MAX_VALUE);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);

		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());
		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.getOptions().setPortSlot(loadSlot);
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(119l);
		dischargeSlot.setMaxDischargeVolume(30l);

		final VoyageDetails details = new VoyageDetails();
		final VoyageOptions options = new VoyageOptions();
		details.setOptions(options);

		details.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 10);
		details.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20);
		details.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 30);
		details.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 40);
		details.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 50);
		details.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 60);
		details.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 70);
		details.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 80);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final Object[] sequence = new Object[] { loadDetails, details, dischargeDetails };

		// Expect to throw a RuntimeException here for a capacity violation
		fail("Better to return object, recording the error");
		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		for (int i = 0; i < 1 + (sequence.length / 2); ++i) {
			arrivalTimes.add(0);
		}
		final VoyagePlan plan = new VoyagePlan();
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

	}

	@Test
	public void testCalculateVoyagePlan4() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setBaseFuelUnitPrice(OptimiserUnitConvertor.convertToInternalPrice(2));
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vessel.getCargoCapacity()).thenReturn(Long.MAX_VALUE);


		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());
		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());
		final PortDetails otherDetails = new PortDetails();
		otherDetails.setOptions(new PortOptions());

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();
		final IPortSlot otherSlot = new EndPortSlot();

		loadDetails.getOptions().setPortSlot(loadSlot);
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);
		otherDetails.getOptions().setPortSlot(otherSlot);

		loadSlot.setMaxLoadVolume(150000000l);
		dischargeSlot.setMaxDischargeVolume(3000000l);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));

		loadSlot.setCargoCVValue(OptimiserUnitConvertor.convertToInternalConversionFactor(2));

		final VoyageDetails details1 = new VoyageDetails();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setVesselState(VesselState.Laden);
		details1.setOptions(options1);

		details1.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 10000);
		details1.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20000);
		details1.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 30000);
		details1.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 40000);
		details1.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 50000);
		details1.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 60000);
		details1.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 70000);
		details1.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 80000);

		final VoyageDetails details2 = new VoyageDetails();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setVesselState(VesselState.Ballast);
		details2.setOptions(options2);

		details2.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 70000);
		details2.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 80000);
		details2.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 90000);
		details2.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 100000);
		details2.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 110000);
		details2.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 120000);
		details2.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 130000);
		details2.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 140000);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final Object[] sequence = new Object[] { loadDetails, details1, dischargeDetails, details2, otherDetails };

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		for (int i = 0; i < 1 + (sequence.length / 2); ++i) {
			arrivalTimes.add(0);
		}
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);
		expectedPlan.setFuelConsumption(FuelComponent.Base, 80000);
		expectedPlan.setFuelConsumption(FuelComponent.Base_Supplemental, 100000);
		expectedPlan.setFuelConsumption(FuelComponent.NBO, 120000);
		expectedPlan.setFuelConsumption(FuelComponent.FBO, 140000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleNBO, 160000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleBase, 180000);
		expectedPlan.setFuelConsumption(FuelComponent.PilotLight, 200000);
		expectedPlan.setFuelConsumption(FuelComponent.IdlePilotLight, 220000);

		expectedPlan.setTotalFuelCost(FuelComponent.Base, 160000);
		expectedPlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 200000);
		expectedPlan.setTotalFuelCost(FuelComponent.NBO, 240000);
		expectedPlan.setTotalFuelCost(FuelComponent.FBO, 280000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleNBO, 320000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleBase, 360000);
		expectedPlan.setTotalFuelCost(FuelComponent.PilotLight, 400000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdlePilotLight, 440000);
		expectedPlan.setLNGFuelVolume(420000);

		Assert.assertEquals(expectedPlan, plan);
	}

	@Test
	public void testCalculateVoyagePlan5() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		final PortDetails otherDetails = new PortDetails();
		otherDetails.setOptions(new PortOptions());
		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());
		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());

		final PortSlot otherSlot = new StartPortSlot(0, 0, 0);
		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		otherDetails.getOptions().setPortSlot(otherSlot);
		loadDetails.getOptions().setPortSlot(loadSlot);
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(150l);
		dischargeSlot.setMaxDischargeVolume(30l);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(1000));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(1000));

		final VoyageDetails details1 = new VoyageDetails();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setVesselState(VesselState.Ballast);
		details1.setOptions(options1);

		final VoyageDetails details2 = new VoyageDetails();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setVesselState(VesselState.Laden);
		details2.setOptions(options2);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final Object[] sequence = new Object[] { otherDetails, details1, loadDetails, details2, dischargeDetails };

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		for (int i = 0; i < 1 + (sequence.length / 2); ++i) {
			arrivalTimes.add(0);
		}
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);

		expectedPlan.setFuelConsumption(FuelComponent.Base, 0);
		expectedPlan.setFuelConsumption(FuelComponent.Base_Supplemental, 0);
		expectedPlan.setFuelConsumption(FuelComponent.NBO, 0);
		expectedPlan.setFuelConsumption(FuelComponent.FBO, 0);
		expectedPlan.setFuelConsumption(FuelComponent.IdleNBO, 0);
		expectedPlan.setFuelConsumption(FuelComponent.IdleBase, 0);

		expectedPlan.setTotalFuelCost(FuelComponent.Base, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.NBO, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.FBO, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleNBO, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleBase, 0);

		Assert.assertEquals(expectedPlan, plan);
	}

	@Test
	public void testCalculateVoyagePlan6() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		int baseFuelPrice = 2;
		vesselClass.setBaseFuelUnitPrice(OptimiserUnitConvertor.convertToInternalPrice(baseFuelPrice));
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		final PortDetails otherDetails = new PortDetails();
		otherDetails.setOptions(new PortOptions());
		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());
		loadDetails.getOptions().setVisitDuration(48);
		loadDetails.setFuelConsumption(FuelComponent.Base, 13000);

		// NOTE: this discharge slot is the tail of the sequence, and
		// should not be evaluated in the VoyagePlan (to avoid double-counting
		// when it occurs as the head of the next sequence)
		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());
		dischargeDetails.setFuelConsumption(FuelComponent.Base, 5000);

		final PortSlot otherSlot = new StartPortSlot(0, 0, 0);
		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		otherDetails.getOptions().setPortSlot(otherSlot);
		loadDetails.getOptions().setPortSlot(loadSlot);
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(150l);
		dischargeSlot.setMaxDischargeVolume(30l);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(1000));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(1000));

		final VoyageDetails details1 = new VoyageDetails();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setVesselState(VesselState.Ballast);
		details1.setOptions(options1);

		final VoyageDetails details2 = new VoyageDetails();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setVesselState(VesselState.Laden);
		details2.setOptions(options2);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final Object[] sequence = new Object[] { otherDetails, details1, loadDetails, details2, dischargeDetails };

		final List<Integer> arrivalTimes = new ArrayList<Integer>();
		for (int i = 0; i < 1 + (sequence.length / 2); ++i) {
			arrivalTimes.add(0);
		}
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);

		// Port fuel consumption should ignore discharge port
		expectedPlan.setFuelConsumption(FuelComponent.Base, 13000);
		expectedPlan.setFuelConsumption(FuelComponent.Base_Supplemental, 0);
		expectedPlan.setFuelConsumption(FuelComponent.NBO, 0);
		expectedPlan.setFuelConsumption(FuelComponent.FBO, 0);
		expectedPlan.setFuelConsumption(FuelComponent.IdleNBO, 0);
		expectedPlan.setFuelConsumption(FuelComponent.IdleBase, 0);

		expectedPlan.setTotalFuelCost(FuelComponent.Base, 26000);
		expectedPlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.NBO, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.FBO, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleNBO, 0);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleBase, 0);

		Assert.assertEquals(expectedPlan, plan);
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

	public VesselClass createSampleVesselClass() {
		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(10), (long) OptimiserUnitConvertor.convertToInternalDailyRate(50));
		keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(20), (long) OptimiserUnitConvertor.convertToInternalDailyRate(100));

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(keypoints);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setConsumptionRate(VesselState.Laden, calc);
		vesselClass.setConsumptionRate(VesselState.Ballast, calc);
		vesselClass.setMinSpeed(OptimiserUnitConvertor.convertToInternalSpeed(10));
		vesselClass.setMaxSpeed(OptimiserUnitConvertor.convertToInternalSpeed(20));

		vesselClass.setNBORate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150));
		vesselClass.setNBORate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150));

		vesselClass.setIdleNBORate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(150));
		vesselClass.setIdleNBORate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(150));

		vesselClass.setIdleConsumptionRate(VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(10));
		vesselClass.setIdleConsumptionRate(VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(10));

		vesselClass.setName("class-1");

		vesselClass.setBaseFuelConversionFactor(OptimiserUnitConvertor.convertToInternalConversionFactor(0.5));

		// 2 days of boil off
		vesselClass.setMinHeel(OptimiserUnitConvertor.convertToInternalVolume(300 * 24));

		vesselClass.setInPortConsumptionRate(PortType.Load, OptimiserUnitConvertor.convertToInternalDailyRate(35));
		vesselClass.setInPortConsumptionRate(PortType.Discharge, OptimiserUnitConvertor.convertToInternalDailyRate(45));

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
	//
	// TODO: Test load/discharge limits
	//
	// TODO: Test none cargoes

}
