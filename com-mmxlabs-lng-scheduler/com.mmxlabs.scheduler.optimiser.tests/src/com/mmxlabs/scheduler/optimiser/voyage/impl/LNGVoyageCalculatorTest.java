/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import static org.junit.Assert.fail;

import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.NotionalEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

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
		options.setRoute(ERouteOption.DIRECT, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
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
		options.setRoute(ERouteOption.DIRECT, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(75 * 48) / 24, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
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
		options.setRoute(ERouteOption.DIRECT, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48) / 24L, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
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
		final ERouteOption route = ERouteOption.DIRECT;
		options.setRoute(route, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(50 * 72) / 24L, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(10 * 24) / 24L, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyageFuelRequirements5() {

		// In this test, we check that we use travel NBO and idle base
		// This implies we do not need a safety heel

		final VoyageOptions options = createSampleVoyageOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(120);
		final int expectedTravelTime = 48;
		final int expectedIdleTime = 72;

		assert expectedTravelTime + expectedIdleTime == options.getAvailableTime();
		options.setRoute(ERouteOption.DIRECT, 15 * expectedTravelTime, 0L);

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(expectedTravelTime, details.getTravelTime());
		Assert.assertEquals(expectedIdleTime, details.getIdleTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * expectedTravelTime) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(10 * expectedIdleTime) / 24L,
				details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
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
		options.setRoute(ERouteOption.DIRECT, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 36) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(50 * 36) / 24L, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
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
		options.setRoute(ERouteOption.DIRECT, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(25 * 36) / 24L,
				details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 36) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
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
		options.setRoute(ERouteOption.DIRECT, 15 * 48, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 36) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(50 * 36) / 24L, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
	}

	VoyageOptions createSampleVoyageOptions() {

		final IPortSlot from = Mockito.mock(IPortSlot.class, "from");
		final IPortSlot to = Mockito.mock(IPortSlot.class, "to");

		final VoyageOptions options = new VoyageOptions(from, to);

		options.setNBOSpeed(OptimiserUnitConvertor.convertToInternalSpeed(15));

		final VesselClass vesselClass = createSampleVesselClass();

		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		options.setVessel(vessel);
		options.setVesselState(VesselState.Laden);

		options.setCargoCVValue(OptimiserUnitConvertor.convertToInternalConversionFactor(22.8));

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
		options.setRoute(ERouteOption.DIRECT, 0, 0L);

		final VoyageDetails details = new VoyageDetails(options);

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
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 48) / 24L, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));
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
		final ERouteOption routeName = ERouteOption.SUEZ;
		final ILongCurve routeCost = new ConstantValueLongCurve(200000L);
		options.setRoute(routeName, 15 * 24, routeCost.getValueAtPoint(0));

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor();
		calc.setRouteCostDataComponentProvider(routeCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IVessel vessel = options.getVessel();

		routeCostProvider.setDefaultRouteCost(routeName, routeCost);
		routeCostProvider.setRouteTransitTime(routeName, vessel, 24);
		routeCostProvider.setRouteFuel(routeName, vessel, VesselState.Laden, 1000, 50000);
		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		/* standard consumption */
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(150 * 24) / 24L, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

		/* route cost consumption */
		Assert.assertEquals((50000 * 24) / 24L, details.getRouteAdditionalConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
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
		final ERouteOption routeName = ERouteOption.SUEZ;

		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor();
		calc.setRouteCostDataComponentProvider(routeCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IVessel vessel = options.getVessel();

		final ILongCurve routeCost = new ConstantValueLongCurve(200000L);
		options.setRoute(routeName, 15 * 24, routeCost.getValueAtPoint(0));

		routeCostProvider.setDefaultRouteCost(routeName, routeCost);
		routeCostProvider.setRouteTransitTime(routeName, vessel, 24);
		routeCostProvider.setRouteFuel(routeName, vessel, VesselState.Laden, 1000, 50000);

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalSpeed(15), details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		/* standard consumption */
		Assert.assertEquals(OptimiserUnitConvertor.convertToInternalVolume(75 * 24) / 24L, details.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

		/* route cost consumption */
		Assert.assertEquals((1000 * 24) / 24L, details.getRouteAdditionalConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
	}

	@Test
	public void testCalculateVoyagePlan1() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));
		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(-1));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(-1));

		final VoyageOptions options = new VoyageOptions(loadSlot, dischargeSlot);
		options.setVesselState(VesselState.Laden);
		options.setCargoCVValue(loadSlot.getCargoCVValue());
		final VoyageDetails details = new VoyageDetails(options);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, details, dischargeDetails };

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(Matchers.<@NonNull IPortSlot> any())).thenReturn(0);

		calc.calculateVoyagePlan(plan, vessel, new long[] { 0L, 0L }, 0, portTimesRecord, sequence);

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
		vesselClass.setBaseFuel(new BaseFuel("test"));
		final ICurve baseFuelCurve = getMockedCurve(2);
		final IVesselBaseFuelCalculator baseFuelCalculator = createVesselBaseFuelCalculator(baseFuelCurve);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vessel.getCargoCapacity()).thenReturn(Long.MAX_VALUE);

		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		// vesselClass.setBaseFuelConversionFactor(OptimiserUnitConvertor.convertToInternalConversionFactor(1.0));
		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));
		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));

		loadSlot.setVolumeLimits(true, 0, 150000L);
		dischargeSlot.setVolumeLimits(true, 0, 30000L);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));
		loadSlot.setCargoCVValue(OptimiserUnitConvertor.convertToInternalConversionFactor(2));

		final VoyageOptions options = new VoyageOptions(loadSlot, dischargeSlot);
		options.setCargoCVValue(loadSlot.getCargoCVValue());
		options.setVesselState(VesselState.Laden);

		final VoyageDetails details = new VoyageDetails(options);

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

		final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, details, dischargeDetails };

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(Matchers.<@NonNull IPortSlot> any())).thenReturn(0);

		final int i = baseFuelCalculator.getBaseFuelPrice(vessel, 100);
		calc.calculateVoyagePlan(plan, vessel, new long[] { 0L, 0L }, baseFuelCalculator.getBaseFuelPrice(vessel, 100), portTimesRecord, sequence);

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

	private IVesselBaseFuelCalculator createVesselBaseFuelCalculator(final @NonNull ICurve curve) {
		final IVesselBaseFuelCalculator vbfc = new VesselBaseFuelCalculator();

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);
				final IBaseFuelCurveProvider b = Mockito.mock(IBaseFuelCurveProvider.class);
				Mockito.when(b.getBaseFuelCurve(Mockito.any(IBaseFuel.class))).thenReturn(curve);

				bind(IBaseFuelCurveProvider.class).toInstance(b);

			}
		});

		injector.injectMembers(vbfc);
		return vbfc;
	}

	private @NonNull ICurve getMockedCurve(final int mainInt) {
		final ICurve curve = Mockito.mock(ICurve.class);

		// create prices for different times (to test UTC)
		final int priceA = (int) OptimiserUnitConvertor.convertToInternalPrice(mainInt);
		final int priceB = (int) OptimiserUnitConvertor.convertToInternalFixedCost(0);
		final int timeA = 100;
		final int timeB = 0;

		// mock some return values
		Mockito.when(curve.getValueAtPoint(timeA)).thenReturn(priceA);
		Mockito.when(curve.getValueAtPoint(timeB)).thenReturn(priceB);

		return curve;
	}

	@Ignore("No longer works after LNGVoyageCalculator#init() removal")
	@Test(expected = RuntimeException.class)
	public void testCalculateVoyagePlan3() {

		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vessel.getCargoCapacity()).thenReturn(Long.MAX_VALUE);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);

		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 0, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));
		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));

		loadSlot.setVolumeLimits(true, 0, 119L);
		dischargeSlot.setVolumeLimits(true, 0, 30L);

		final VoyageOptions options = new VoyageOptions(loadSlot, dischargeSlot);
		final VoyageDetails details = new VoyageDetails(options);

		details.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 10);
		details.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20);
		details.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 30);
		details.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 40);
		details.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 50);
		details.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 60);
		details.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 70);
		details.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 80);

		final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, details, dischargeDetails };

		// Expect to throw a RuntimeException here for a capacity violation
		fail("Better to return object, recording the error");
		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(Matchers.<@NonNull IPortSlot> any())).thenReturn(0);

		final VoyagePlan plan = new VoyagePlan();
		final LNGVoyageCalculator calc = new LNGVoyageCalculator();
		calc.calculateVoyagePlan(plan, vessel, new long[] { 0L, 0L }, 0, portTimesRecord, sequence);

	}

	@Test
	public void testCalculateVoyagePlan4() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = Mockito.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setBaseFuel(new BaseFuel("test"));
		final ICurve baseFuelCurve = getMockedCurve(2);
		final IVesselBaseFuelCalculator baseFuelCalculator = createVesselBaseFuelCalculator(baseFuelCurve);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		Mockito.when(vessel.getCargoCapacity()).thenReturn(Long.MAX_VALUE);

		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);
		IHeelOptionConsumer heelOptions = new HeelOptionConsumer(0L, 0L, VesselTankState.EITHER, new ConstantHeelPriceCalculator(0));
		final IPortSlot otherSlot = new NotionalEndPortSlot("notional-end", null, null, heelOptions);

		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));
		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));
		final PortDetails otherDetails = new PortDetails(new PortOptions(otherSlot));

		loadSlot.setVolumeLimits(true, 0, 150000000L);
		dischargeSlot.setVolumeLimits(true, 0, 3000000L);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(1)));

		loadSlot.setCargoCVValue(OptimiserUnitConvertor.convertToInternalConversionFactor(2));

		final VoyageOptions options1 = new VoyageOptions(loadSlot, dischargeSlot);
		options1.setCargoCVValue(loadSlot.getCargoCVValue());
		options1.setVesselState(VesselState.Laden);
		final VoyageDetails details1 = new VoyageDetails(options1);

		details1.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 10000);
		details1.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20000);
		details1.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 30000);
		details1.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 40000);
		details1.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 50000);
		details1.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 60000);
		details1.setFuelConsumption(FuelComponent.PilotLight, FuelComponent.PilotLight.getDefaultFuelUnit(), 70000);
		details1.setFuelConsumption(FuelComponent.IdlePilotLight, FuelComponent.IdlePilotLight.getDefaultFuelUnit(), 80000);

		final VoyageOptions options2 = new VoyageOptions(dischargeSlot, otherSlot);
		options2.setCargoCVValue(loadSlot.getCargoCVValue());
		options2.setVesselState(VesselState.Ballast);

		final VoyageDetails details2 = new VoyageDetails(options2);
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

		final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, details1, dischargeDetails, details2, otherDetails };

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(Matchers.<@NonNull IPortSlot> any())).thenReturn(0);

		calc.calculateVoyagePlan(plan, vessel, new long[] { 0L, 0L }, baseFuelCalculator.getBaseFuelPrice(vessel, 100), portTimesRecord, sequence);

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

		final PortSlot otherSlot = new StartPortSlot("start", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), null);
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final PortDetails otherDetails = new PortDetails(new PortOptions(otherSlot));
		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));
		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));

		loadSlot.setVolumeLimits(true, 0, 150L);
		dischargeSlot.setVolumeLimits(true, 0, 30L);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(1000));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(1000));

		final VoyageOptions options1 = new VoyageOptions(otherSlot, loadSlot);
		options1.setVesselState(VesselState.Ballast);
		final VoyageDetails details1 = new VoyageDetails(options1);

		final VoyageOptions options2 = new VoyageOptions(loadSlot, dischargeSlot);
		options2.setVesselState(VesselState.Laden);
		final VoyageDetails details2 = new VoyageDetails(options2);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { otherDetails, details1, loadDetails, details2, dischargeDetails };

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(Matchers.<@NonNull IPortSlot> any())).thenReturn(0);
		Mockito.when(portTimesRecord.getFirstSlot()).thenReturn(otherSlot);

		calc.calculateVoyagePlan(plan, vessel, new long[] { 0L, 0L }, 0, portTimesRecord, sequence);

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
		final int baseFuelPrice = 2;
		final ICurve baseFuelCurve = getMockedCurve(2);
		final IVesselBaseFuelCalculator baseFuelCalculator = createVesselBaseFuelCalculator(baseFuelCurve);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		
		IHeelPriceCalculator heelPriceCalculator = Mockito.mock(IHeelPriceCalculator.class);
		IHeelOptionSupplier supplier = Mockito.mock(IHeelOptionSupplier.class);
		Mockito.when(supplier.getHeelPriceCalculator()).thenReturn(heelPriceCalculator);
		
		final PortSlot otherSlot = new StartPortSlot("start", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), supplier);
		final LoadSlot loadSlot = new LoadSlot("load", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);
		final DischargeSlot dischargeSlot = new DischargeSlot("discharge", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), true, 0L, 0L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final PortDetails otherDetails = new PortDetails(new PortOptions(otherSlot));
		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));
		loadDetails.getOptions().setVisitDuration(48);
		loadDetails.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, 13000);

		// NOTE: this discharge slot is the tail of the sequence, and
		// should not be evaluated in the VoyagePlan (to avoid double-counting
		// when it occurs as the head of the next sequence)
		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));
		dischargeDetails.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, 5000);

		loadSlot.setVolumeLimits(true, 0, 150L);
		dischargeSlot.setVolumeLimits(true, 0, 30L);

		loadSlot.setLoadPriceCalculator(new FixedPriceContract(1000));
		dischargeSlot.setDischargePriceCalculator(new FixedPriceContract(1000));

		final VoyageOptions options1 = new VoyageOptions(otherSlot, loadSlot);
		options1.setVesselState(VesselState.Ballast);
		final VoyageDetails details1 = new VoyageDetails(options1);

		final VoyageOptions options2 = new VoyageOptions(loadSlot, otherSlot);
		options2.setVesselState(VesselState.Laden);
		final VoyageDetails details2 = new VoyageDetails(options2);

		final LNGVoyageCalculator calc = new LNGVoyageCalculator();

		final IRouteCostProvider mockRouteCostProvider = Mockito.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);
		final IPortCVProvider mockPortCVProvider = Mockito.mock(IPortCVProvider.class);
		calc.setPortCVProvider(mockPortCVProvider);

		final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { otherDetails, details1, loadDetails, details2, dischargeDetails };

		final IPortTimesRecord portTimesRecord = Mockito.mock(IPortTimesRecord.class);
		Mockito.when(portTimesRecord.getSlotTime(Matchers.<@NonNull IPortSlot> any())).thenReturn(0);
		Mockito.when(portTimesRecord.getFirstSlot()).thenReturn(otherSlot);

		calc.calculateVoyagePlan(plan, vessel, new long[] { 0L, 0L }, baseFuelCalculator.getBaseFuelPrice(vessel, 100), portTimesRecord, sequence);

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

		final VesselClass vesselClass = (VesselClass) options.getVessel().getVesselClass();

		vesselClass.setInPortConsumptionRateInMTPerDay(portType, consumptionRate);

		Mockito.when(options.getPortSlot().getPortType()).thenReturn(portType);

		calc.calculatePortFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());

		Assert.assertEquals(target, details.getFuelConsumption(FuelComponent.Base, FuelUnit.MT));
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

		final IBaseFuel baseFuel = new BaseFuel("test");
		baseFuel.setEquivalenceFactor(OptimiserUnitConvertor.convertToInternalConversionFactor(45.6));
		vesselClass.setBaseFuel(baseFuel);

		// 2 days of boil off
		vesselClass.setSafetyHeel(OptimiserUnitConvertor.convertToInternalVolume(300 * 24));

		vesselClass.setInPortConsumptionRateInMTPerDay(PortType.Load, OptimiserUnitConvertor.convertToInternalDailyRate(35));
		vesselClass.setInPortConsumptionRateInMTPerDay(PortType.Discharge, OptimiserUnitConvertor.convertToInternalDailyRate(45));

		// Currently not used by test
		vesselClass.setCargoCapacity(0);

		return vesselClass;
	}

	public PortOptions createSamplePortOptions() {

		final IPortSlot slot = Mockito.mock(IPortSlot.class, "slot");

		final PortOptions options = new PortOptions(slot);

		options.setVisitDuration(0);

		final VesselClass vesselClass = createSampleVesselClass();

		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);

		options.setVessel(vessel);

		return options;
	}
}
