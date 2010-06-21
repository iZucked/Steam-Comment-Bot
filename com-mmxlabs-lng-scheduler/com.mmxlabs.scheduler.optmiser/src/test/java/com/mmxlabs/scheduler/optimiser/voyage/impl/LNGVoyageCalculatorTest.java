package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.TreeMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(48, details.getIdleTime());
		Assert.assertEquals(48, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(10 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(72, details.getTravelTime());
		Assert.assertEquals(24, details.getIdleTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(100 * 72 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleNBO));
		Assert.assertEquals(10 * 24 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleBase));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(15 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(48, details.getTravelTime());
		Assert.assertEquals(72, details.getIdleTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO));

		Assert.assertEquals(10 * 24 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleBase));

		Assert.assertEquals(150 * 48 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(20 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(150 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(50 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(20 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(50 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(150 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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

		final IVoyageDetails<Object> details = new VoyageDetails<Object>();

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		context.checking(new Expectations() {
			{
				one(options.getVessel()).getVesselClass();
			}
		});

		calc.calculateVoyageFuelRequirements(options, details);

		// Check results
		Assert.assertSame(options, details.getOptions());
		Assert.assertEquals(20 * Calculator.ScaleFactor, details.getSpeed());
		Assert.assertEquals(0, details.getIdleTime());
		Assert.assertEquals(36, details.getTravelTime());

		Assert.assertEquals(0, details.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(150 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(50 * 36 * Calculator.ScaleFactor, details
				.getFuelConsumption(FuelComponent.FBO));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(0, details
				.getFuelConsumption(FuelComponent.IdleNBO));

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
		keypoints.put(10 * Calculator.ScaleFactor,
				100l * Calculator.ScaleFactor);
		keypoints.put(20 * Calculator.ScaleFactor,
				200l * Calculator.ScaleFactor);

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

}
