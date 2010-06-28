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

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

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

	@Test
	public void testCalculateVoyagePlan1() {

		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final IVoyagePlan plan = context.mock(IVoyagePlan.class);
		final IVessel vessel = context.mock(IVessel.class);

		final IPortDetails loadDetails = new PortDetails();
		final IPortDetails dischargeDetails = new PortDetails();

		final ILoadSlot loadSlot = new LoadSlot();
		final IDischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		final IVoyageOptions options = context.mock(IVoyageOptions.class);
		details.setOptions(options);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final Object[] sequence = new Object[] { loadDetails, details,
				dischargeDetails };

		context.checking(new Expectations() {
			{
				allowing(options).getVesselState();
				one(vessel).getVesselClass();

				one(plan).setSequence(sequence);

				one(plan).setFuelConsumption(FuelComponent.Base, 0);
				one(plan)
						.setFuelConsumption(FuelComponent.Base_Supplemental, 0);
				one(plan).setFuelConsumption(FuelComponent.NBO, 0);
				one(plan).setFuelConsumption(FuelComponent.FBO, 0);
				one(plan).setFuelConsumption(FuelComponent.IdleNBO, 0);
				one(plan).setFuelConsumption(FuelComponent.IdleBase, 0);

				one(plan).setFuelCost(FuelComponent.Base, 0);
				one(plan).setFuelCost(FuelComponent.Base_Supplemental, 0);
				one(plan).setFuelCost(FuelComponent.NBO, 0);
				one(plan).setFuelCost(FuelComponent.FBO, 0);
				one(plan).setFuelCost(FuelComponent.IdleNBO, 0);
				one(plan).setFuelCost(FuelComponent.IdleBase, 0);

				one(plan).setLoadVolume(0);
				one(plan).setDischargeVolume(0);
				one(plan).setPurchaseCost(0);
				one(plan).setSalesRevenue(0);
			}
		});

		calc.calculateVoyagePlan(plan, vessel, sequence);

		context.assertIsSatisfied();
	}

	@Test
	public void testCalculateVoyagePlan2() {

		final IVoyagePlan plan = context.mock(IVoyagePlan.class);
		final IVessel vessel = context.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		context.setDefaultResultForType(IVesselClass.class, vesselClass);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final IPortDetails loadDetails = new PortDetails();
		final IPortDetails dischargeDetails = new PortDetails();

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(150l);
		dischargeSlot.setMaxDischargeVolume(30l);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		final IVoyageOptions options = context.mock(IVoyageOptions.class);
		details.setOptions(options);

		details.setFuelConsumption(FuelComponent.Base, 10);
		details.setFuelConsumption(FuelComponent.Base_Supplemental, 20);
		details.setFuelConsumption(FuelComponent.NBO, 30);
		details.setFuelConsumption(FuelComponent.FBO, 40);
		details.setFuelConsumption(FuelComponent.IdleNBO, 50);
		details.setFuelConsumption(FuelComponent.IdleBase, 60);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final Object[] sequence = new Object[] { loadDetails, details,
				dischargeDetails };

		context.checking(new Expectations() {
			{

				allowing(options).getVesselState();
				one(vessel).getVesselClass();

				one(plan).setSequence(sequence);

				one(plan).setFuelConsumption(FuelComponent.Base, 10);
				one(plan).setFuelConsumption(FuelComponent.Base_Supplemental,
						20);
				one(plan).setFuelConsumption(FuelComponent.NBO, 30);
				one(plan).setFuelConsumption(FuelComponent.FBO, 40);
				one(plan).setFuelConsumption(FuelComponent.IdleNBO, 50);
				one(plan).setFuelConsumption(FuelComponent.IdleBase, 60);

				one(plan).setFuelCost(FuelComponent.Base, 10);
				one(plan).setFuelCost(FuelComponent.Base_Supplemental, 20);
				one(plan).setFuelCost(FuelComponent.NBO, 30);
				one(plan).setFuelCost(FuelComponent.FBO, 40);
				one(plan).setFuelCost(FuelComponent.IdleNBO, 50);
				one(plan).setFuelCost(FuelComponent.IdleBase, 60);

				one(plan).setLoadVolume(150);
				one(plan).setDischargeVolume(30);
				one(plan).setPurchaseCost(150);
				one(plan).setSalesRevenue(30);
			}
		});

		calc.calculateVoyagePlan(plan, vessel, sequence);

		context.assertIsSatisfied();
	}

	@Test(expected = RuntimeException.class)
	public void testCalculateVoyagePlan3() {

		final IVoyagePlan plan = context.mock(IVoyagePlan.class);
		final IVessel vessel = context.mock(IVessel.class);
		final VesselClass vesselClass = new VesselClass();
		context.setDefaultResultForType(IVesselClass.class, vesselClass);
		vesselClass.setCargoCapacity(Long.MAX_VALUE);
		context.setDefaultResultForType(VesselState.class, VesselState.Laden);

		final IPortDetails loadDetails = new PortDetails();
		final IPortDetails dischargeDetails = new PortDetails();

		final LoadSlot loadSlot = new LoadSlot();
		final DischargeSlot dischargeSlot = new DischargeSlot();

		loadDetails.setPortSlot(loadSlot);
		dischargeDetails.setPortSlot(dischargeSlot);

		loadSlot.setMaxLoadVolume(119l);
		dischargeSlot.setMaxDischargeVolume(30l);

		final VoyageDetails<Object> details = new VoyageDetails<Object>();
		final IVoyageOptions options = context.mock(IVoyageOptions.class);
		details.setOptions(options);

		details.setFuelConsumption(FuelComponent.Base, 10);
		details.setFuelConsumption(FuelComponent.Base_Supplemental, 20);
		details.setFuelConsumption(FuelComponent.NBO, 30);
		details.setFuelConsumption(FuelComponent.FBO, 40);
		details.setFuelConsumption(FuelComponent.IdleNBO, 50);
		details.setFuelConsumption(FuelComponent.IdleBase, 60);

		final LNGVoyageCalculator<Object> calc = new LNGVoyageCalculator<Object>();

		final Object[] sequence = new Object[] { loadDetails, details,
				dischargeDetails };

		context.checking(new Expectations() {
			{

				allowing(options).getVesselState();
				one(vessel).getVesselClass();

				// one(plan).setSequence(sequence);
				//
				// one(plan).setFuelConsumption(FuelComponent.Base, 10);
				// one(plan).setFuelConsumption(FuelComponent.Base_Supplemental,
				// 20);
				// one(plan).setFuelConsumption(FuelComponent.NBO, 30);
				// one(plan).setFuelConsumption(FuelComponent.FBO, 40);
				// one(plan).setFuelConsumption(FuelComponent.IdleNBO, 50);
				// one(plan).setFuelConsumption(FuelComponent.IdleBase, 60);
				//
				// one(plan).setFuelCost(FuelComponent.Base, 10);
				// one(plan).setFuelCost(FuelComponent.Base_Supplemental, 20);
				// one(plan).setFuelCost(FuelComponent.NBO, 30);
				// one(plan).setFuelCost(FuelComponent.FBO, 40);
				// one(plan).setFuelCost(FuelComponent.IdleNBO, 50);
				// one(plan).setFuelCost(FuelComponent.IdleBase, 60);
				//
				// one(plan).setLoadVolume(150);
				// one(plan).setDischargeVolume(30);
				// one(plan).setPurchaseCost(150);
				// one(plan).setSalesRevenue(30);
			}
		});

		calc.calculateVoyagePlan(plan, vessel, sequence);

		context.assertIsSatisfied();

		fail("Better to return object, recording the error");

	}

	//	
	// TODO: Test load/discharge limits
	//	 
	// TODO: Test none cargos

}
