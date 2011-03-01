/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import static org.junit.Assert.fail;

import java.util.TreeMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
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
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

@RunWith(JMock.class)
public class LNGVoyageCalculatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testCalculateVoyageFuelRequirements1() {

		// In this test, we check that we only use travel NBO.

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				allowing(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				150 * 48 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements2() {

		// In this test, we check that we only use travel Base.

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(75 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base,
						FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements3() {

		// In this test, we check that we use travel and idle NBO

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(96);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(48, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				150 * 48 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleNBO,
						FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements4() {

		// In this test, we check that we use travel and idle base

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(96);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(10 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(72, details.getTravelTime());
		Assert.assertEquals(24, details.getIdleTime());

		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(50 * 72 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base,
						FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));
		Assert.assertEquals(10 * 24 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleBase,
						FuelComponent.IdleBase.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements5() {

		// In this test, we check that we use travel NBO and idle base
		// This implies min heel run down

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(120);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(48, details.getTravelTime());
		Assert.assertEquals(72, details.getIdleTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				150 * 48 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));

		Assert.assertEquals(10 * 24 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleBase,
						FuelComponent.IdleBase.getDefaultFuelUnit()));

		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleNBO,
						FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements6() {

		// In this test, we check that we only use travel NBO - however due to
		// decreased time, expect FBO
		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(36);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(20 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				150 * 36 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				50 * 36 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements7() {

		// In this test, we check that we travel on NBO and base supplement
		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(36);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(20 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(25 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base_Supplemental,
						FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				150 * 36 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements8() {

		// In this test, there is not enough travel time, expect us to travel as
		// fast as possible to get to our destination.
		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(20);
		options.setDistance(15 * 48);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(20 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				150 * 36 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				50 * 36 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	VoyageOptions createSampleOptions() {

		final IPortSlot from = context.mock(IPortSlot.class, "from");
		final IPortSlot to = context.mock(IPortSlot.class, "to");

		final VoyageOptions options = new VoyageOptions();

		options.setFromPortSlot(from);
		options.setToPortSlot(to);

		options.setNBOSpeed(15 * Calculator.ScaleFactor);

		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();
		keypoints
				.put(10 * Calculator.ScaleFactor, 50l * Calculator.ScaleFactor);
		keypoints.put(20 * Calculator.ScaleFactor,
				100l * Calculator.ScaleFactor);

		final InterpolatingConsumptionRateCalculator calc = new InterpolatingConsumptionRateCalculator(
				keypoints);

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setConsumptionRate(VesselState.Laden, calc);
		vesselClass.setConsumptionRate(VesselState.Ballast, calc);
		vesselClass.setMinSpeed(10 * Calculator.ScaleFactor);
		vesselClass.setMaxSpeed(20 * Calculator.ScaleFactor);

		vesselClass.setNBORate(VesselState.Laden, 150 * Calculator.ScaleFactor);
		vesselClass.setNBORate(VesselState.Ballast,
				150 * Calculator.ScaleFactor);

		vesselClass.setIdleNBORate(VesselState.Laden,
				150 * Calculator.ScaleFactor);
		vesselClass.setIdleNBORate(VesselState.Ballast,
				150 * Calculator.ScaleFactor);

		vesselClass.setIdleConsumptionRate(VesselState.Ballast,
				10 * Calculator.ScaleFactor);
		vesselClass.setIdleConsumptionRate(VesselState.Laden,
				10 * Calculator.ScaleFactor);

		vesselClass.setName("class-1");

		vesselClass.setBaseFuelConversionFactor(500);

		// 2 days of boil off
		vesselClass.setMinHeel(300 * 24 * Calculator.ScaleFactor);

		// Currently not used by test
		vesselClass.setCargoCapacity(0);

		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final IVessel vessel = context.mock(IVessel.class);

		options.setVessel(vessel);
		options.setVesselState(VesselState.Laden);

		return options;
	}

	@Test
	public void testCalculateVoyageFuelRequirements9() {

		// In this test, we check zero distance is handled correctly

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(0);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
				one(mockRouteCostProvider).getRouteCost(null, vesselClass,
						VesselState.Laden);
				one(mockRouteCostProvider).getRouteFuelUsage(null, vesselClass);
				one(mockRouteCostProvider).getRouteTransitTime(null,
						vesselClass);

			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(0, details.getSpeed());
		Assert.assertEquals(48, details.getIdleTime());
		Assert.assertEquals(0, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleNBO,
						FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements10() {

		// In this test, we check that we only use travel NBO.
		// A canal route has been added and should be costed

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(true);
		options.setUseNBOForIdle(true);
		options.setUseFBOForSupplement(true);

		options.setAvailableTime(48);
		options.setDistance(15 * 24);
		final String routeName = "Canal";
		options.setRoute(routeName);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor(
				"Name", "Default");
		calc.setRouteCostDataComponentProvider(routeCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		routeCostProvider.setDefaultRouteCost(routeName, 200000);
		routeCostProvider.setRouteTimeAndFuel(routeName, vesselClass, 24, 1000);

		context.checking(new Expectations() {
			{
				allowing(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				/* route cost consumption */1000 * 24 * 2 +
				/* standard consumption */150 * 24 * Calculator.ScaleFactor,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyageFuelRequirements11() {

		// In this test, we check that we only use travel NBO.
		// A canal route has been added and should be costed

		final VoyageOptions options = createSampleOptions();

		// Populate options
		options.setUseNBOForTravel(false);
		options.setUseNBOForIdle(false);
		options.setUseFBOForSupplement(false);

		options.setAvailableTime(48);
		options.setDistance(15 * 24);
		final String routeName = "Canal";
		options.setRoute(routeName);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor(
				"Name", "Default");
		calc.setRouteCostDataComponentProvider(routeCostProvider);

		calc.init();

		// Need this additional bit to get vesselClass reference
		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});
		final IVesselClass vesselClass = options.getVessel().getVesselClass();

		routeCostProvider.setDefaultRouteCost(routeName, 200000);
		routeCostProvider.setRouteTimeAndFuel(routeName, vesselClass, 24, 1000);

		context.checking(new Expectations() {
			{
				allowing(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(/* route cost consumption */1000 * 24 +
		/* standard consumption */75 * 24 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base,
						FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.NBO,
						FuelComponent.NBO.getDefaultFuelUnit()));
		Assert.assertEquals(
				0,
				details.getFuelConsumption(FuelComponent.FBO,
						FuelComponent.FBO.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit()));
		Assert.assertEquals(0, details.getFuelConsumption(
				FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit()));

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyagePlan1() {

		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = context.mock(IVessel.class);

		final PortDetails loadDetails = new PortDetails();
		final PortDetails dischargeDetails = new PortDetails();

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		loadSlot.setPurchasePriceCurve(new ConstantValueCurve(-1.0));
		dischargeSlot.setSalesPriceCurve(new ConstantValueCurve(-1.0));

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		final VoyageOptions options = new VoyageOptions();
		details.setOptions(options);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		final Object[] sequence = new Object[] { loadDetails, details,
				dischargeDetails };

		context.checking(new Expectations() {
			{
				allowing(vessel).getVesselClass();

			}
		});

		final int[] arrivalTimes = new int[1 + sequence.length / 2];
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);
		for (final FuelComponent fuel : FuelComponent.values()) {
			expectedPlan.setFuelConsumption(fuel, 0);
			expectedPlan.setTotalFuelCost(fuel, 0);
		}

		Assert.assertEquals(expectedPlan, plan);

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyagePlan2() {

		final VoyagePlan plan = new VoyagePlan();

		final IVessel vessel = context.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setBaseFuelUnitPrice(2000);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);
		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final PortDetails loadDetails = new PortDetails();
		final PortDetails dischargeDetails = new PortDetails();

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(150000l);
		dischargeSlot.setMaxDischargeVolume(30000l);

		loadSlot.setPurchasePriceCurve(new ConstantValueCurve(1000.0));
		dischargeSlot.setSalesPriceCurve(new ConstantValueCurve(1000.0));
		loadSlot.setCargoCVValue(2000);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		final VoyageOptions options = new VoyageOptions();
		details.setOptions(options);

		details.setFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit(), 10000);
		details.setFuelConsumption(FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20000);
		details.setFuelConsumption(FuelComponent.NBO,
				FuelComponent.NBO.getDefaultFuelUnit(), 30000);
		details.setFuelConsumption(FuelComponent.FBO,
				FuelComponent.FBO.getDefaultFuelUnit(), 40000);
		details.setFuelConsumption(FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit(), 50000);
		details.setFuelConsumption(FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit(), 60000);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		final Object[] sequence = new Object[] { loadDetails, details,
				dischargeDetails };

		context.checking(new Expectations() {
			{
				allowing(vessel).getVesselClass();
			}
		});

		final int[] arrivalTimes = new int[1 + sequence.length / 2];
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);

		expectedPlan.setFuelConsumption(FuelComponent.Base, 10000);
		expectedPlan.setFuelConsumption(FuelComponent.Base_Supplemental, 20000);
		expectedPlan.setFuelConsumption(FuelComponent.NBO, 30000);
		expectedPlan.setFuelConsumption(FuelComponent.FBO, 40000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleNBO, 50000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleBase, 60000);

		expectedPlan.setTotalFuelCost(FuelComponent.Base, 20000);
		expectedPlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 40000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleBase, 120000);
		expectedPlan.setTotalFuelCost(FuelComponent.NBO, 60000);
		expectedPlan.setTotalFuelCost(FuelComponent.FBO, 80000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleNBO, 100000);

		// TODO: Check
		expectedPlan.setDischargeVolume(30000l);
		expectedPlan.setLoadVolume(150000);

		expectedPlan.setSalesRevenue(60000l);
		expectedPlan.setPurchaseCost(300000);

		Assert.assertEquals(expectedPlan, plan);

		context.assertIsSatisfied();
	}

	@Test(expected = RuntimeException.class)
	public void testCalculateVoyagePlan3() {

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = context.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		context.setDefaultResultForType(IVesselClass.class, vesselClass);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final PortDetails loadDetails = new PortDetails();
		final PortDetails dischargeDetails = new PortDetails();

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(119l);
		dischargeSlot.setMaxDischargeVolume(30l);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		final VoyageOptions options = new VoyageOptions();
		details.setOptions(options);

		details.setFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit(), 10);
		details.setFuelConsumption(FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20);
		details.setFuelConsumption(FuelComponent.NBO,
				FuelComponent.NBO.getDefaultFuelUnit(), 30);
		details.setFuelConsumption(FuelComponent.FBO,
				FuelComponent.FBO.getDefaultFuelUnit(), 40);
		details.setFuelConsumption(FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit(), 50);
		details.setFuelConsumption(FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit(), 60);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		calc.init();

		final Object[] sequence = new Object[] { loadDetails, details,
				dischargeDetails };

		context.checking(new Expectations() {
			{

				allowing(vessel).getVesselClass();
			}
		});

		// Expect to throw a RuntimeException here for a capacity violation
		fail("Better to return object, recording the error");
		final int[] arrivalTimes = new int[1 + sequence.length / 2];
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		context.assertIsSatisfied();

	}

	@Test
	public void testCalculateVoyagePlan4() {

		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = context.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		vesselClass.setBaseFuelUnitPrice(2000);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		context.setDefaultResultForType(IVesselClass.class, vesselClass);

		final PortDetails loadDetails = new PortDetails();
		final PortDetails dischargeDetails = new PortDetails();
		final PortDetails otherDetails = new PortDetails();

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();
		final IPortSlot otherSlot = new EndPortSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);
		otherDetails.setPortSlot(otherSlot);

		loadSlot.setMaxLoadVolume(150000000l);
		dischargeSlot.setMaxDischargeVolume(3000000l);

		loadSlot.setPurchasePriceCurve(new ConstantValueCurve(1000.0));
		dischargeSlot.setSalesPriceCurve(new ConstantValueCurve(1000.0));

		loadSlot.setCargoCVValue(2000);

		final VoyageDetails<Object> details1 = new VoyageDetails<Object>();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setVesselState(VesselState.Laden);
		details1.setOptions(options1);

		details1.setFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit(), 10000);
		details1.setFuelConsumption(FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 20000);
		details1.setFuelConsumption(FuelComponent.NBO,
				FuelComponent.NBO.getDefaultFuelUnit(), 30000);
		details1.setFuelConsumption(FuelComponent.FBO,
				FuelComponent.FBO.getDefaultFuelUnit(), 40000);
		details1.setFuelConsumption(FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit(), 50000);
		details1.setFuelConsumption(FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit(), 60000);

		final VoyageDetails<Object> details2 = new VoyageDetails<Object>();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setVesselState(VesselState.Ballast);
		details2.setOptions(options2);

		details2.setFuelConsumption(FuelComponent.Base,
				FuelComponent.Base.getDefaultFuelUnit(), 70000);
		details2.setFuelConsumption(FuelComponent.Base_Supplemental,
				FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 80000);
		details2.setFuelConsumption(FuelComponent.NBO,
				FuelComponent.NBO.getDefaultFuelUnit(), 90000);
		details2.setFuelConsumption(FuelComponent.FBO,
				FuelComponent.FBO.getDefaultFuelUnit(), 100000);
		details2.setFuelConsumption(FuelComponent.IdleNBO,
				FuelComponent.IdleNBO.getDefaultFuelUnit(), 110000);
		details2.setFuelConsumption(FuelComponent.IdleBase,
				FuelComponent.IdleBase.getDefaultFuelUnit(), 120000);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		final Object[] sequence = new Object[] { loadDetails, details1,
				dischargeDetails, details2, otherDetails };

		context.checking(new Expectations() {
			{
				allowing(vessel).getVesselClass();
			}
		});

		final int[] arrivalTimes = new int[1 + sequence.length / 2];
		calc.calculateVoyagePlan(plan, vessel, arrivalTimes, sequence);

		final VoyagePlan expectedPlan = new VoyagePlan();
		expectedPlan.setSequence(sequence);
		expectedPlan.setFuelConsumption(FuelComponent.Base, 80000);
		expectedPlan
				.setFuelConsumption(FuelComponent.Base_Supplemental, 100000);
		expectedPlan.setFuelConsumption(FuelComponent.NBO, 120000);
		expectedPlan.setFuelConsumption(FuelComponent.FBO, 140000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleNBO, 160000);
		expectedPlan.setFuelConsumption(FuelComponent.IdleBase, 180000);

		expectedPlan.setTotalFuelCost(FuelComponent.Base, 160000);
		expectedPlan.setTotalFuelCost(FuelComponent.Base_Supplemental, 200000);
		expectedPlan.setTotalFuelCost(FuelComponent.NBO, 240000);
		expectedPlan.setTotalFuelCost(FuelComponent.FBO, 280000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleNBO, 320000);
		expectedPlan.setTotalFuelCost(FuelComponent.IdleBase, 360000);

		expectedPlan.setDischargeVolume(3000000l);
		expectedPlan.setLoadVolume(3420000l);
		expectedPlan.setPurchaseCost(6840000l);
		expectedPlan.setSalesRevenue(6000000l);

		Assert.assertEquals(expectedPlan, plan);

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyagePlan5() {

		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final VoyagePlan plan = new VoyagePlan();
		final IVessel vessel = context.mock(IVessel.class);

		final PortDetails otherDetails = new PortDetails();
		final PortDetails loadDetails = new PortDetails();
		final PortDetails dischargeDetails = new PortDetails();

		final PortSlot otherSlot = new StartPortSlot();
		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		otherDetails.setPortSlot(otherSlot);
		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(150l);
		dischargeSlot.setMaxDischargeVolume(30l);

		loadSlot.setPurchasePriceCurve(new ConstantValueCurve(1000.0));
		dischargeSlot.setSalesPriceCurve(new ConstantValueCurve(1000.0));

		final VoyageDetails<Object> details1 = new VoyageDetails<Object>();
		final VoyageOptions options1 = new VoyageOptions();
		details1.setOptions(options1);

		final VoyageDetails<Object> details2 = new VoyageDetails<Object>();
		final VoyageOptions options2 = new VoyageOptions();
		details2.setOptions(options2);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final IRouteCostProvider mockRouteCostProvider = context
				.mock(IRouteCostProvider.class);
		calc.setRouteCostDataComponentProvider(mockRouteCostProvider);

		calc.init();

		final Object[] sequence = new Object[] { otherDetails, details1,
				loadDetails, details2, dischargeDetails };

		context.checking(new Expectations() {
			{
				allowing(vessel).getVesselClass();
			}
		});

		final int[] arrivalTimes = new int[1 + sequence.length / 2];
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

		expectedPlan.setLoadVolume(0);
		expectedPlan.setDischargeVolume(0);
		expectedPlan.setPurchaseCost(0);
		expectedPlan.setSalesRevenue(0);

		context.assertIsSatisfied();
	}

	//
	// TODO: Test load/discharge limits
	//
	// TODO: Test none cargos

}
