/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

@RunWith(JMock.class)
public class SchedulerBuilderTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testSchedulerBuilder() {

		/**
		 * How to test builder? -- No access to state until we get the finished
		 * product? Perhaps a second constructor passing in DCP objects and
		 * ensure they are called correctly.
		 */

		fail("Not yet implemented");
	}

	@Test
	public void testCreateLoadSlot() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLoadSlot2() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port = context.mock(IPort.class);
		final ITimeWindow window = builder.createTimeWindow(0, 0);

		final ILoadPriceCalculator contract = context.mock(ILoadPriceCalculator.class);
		
		builder.createLoadSlot("id", port, window, 0, 0, contract, 0, 0, false, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLoadSlot3() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port = builder.createPort("port", false, null);
		final ITimeWindow window = context.mock(ITimeWindow.class);

		final ILoadPriceCalculator contract = context.mock(ILoadPriceCalculator.class);

		builder.createLoadSlot("id", port, window, 0, 0, contract, 0, 0, false, false);
	}

	@Test
	public void testCreateDischargeSlot() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDischargeSlot2() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port = context.mock(IPort.class);
		final ITimeWindow window = builder.createTimeWindow(0, 0);

		final ICurve curve = context.mock(ICurve.class);

		builder.createDischargeSlot("id", port, window, 0, 0, curve, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDischargeSlot3() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port = builder.createPort("port", false, null);
		final ITimeWindow window = context.mock(ITimeWindow.class);

		final ICurve curve = context.mock(ICurve.class);

		builder.createDischargeSlot("id", port, window, 0, 0, curve, 0);
	}

	@Test
	public void testCreateCargo() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCargo2() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port = builder.createPort("port", false, null);
		final ITimeWindow window = builder.createTimeWindow(0, 0);

		final ICurve curve = context.mock(ICurve.class);

		final ILoadSlot loadSlot = context.mock(ILoadSlot.class);

		final IDischargeSlot dischargeSlot = builder.createDischargeSlot("id",
				port, window, 0, 0, curve, 0);

		builder.createCargo("id", loadSlot, dischargeSlot);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCargo3() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final IPort port = builder.createPort("port", false, null);
		final ITimeWindow window = builder.createTimeWindow(0, 0);
		final ILoadPriceCalculator contract = context.mock(ILoadPriceCalculator.class);

		final ILoadSlot loadSlot = builder.createLoadSlot("id", port, window,
				0, 0, contract, 0, 0, false, false);
		final IDischargeSlot dischargeSlot = context.mock(IDischargeSlot.class);

		builder.createCargo("id", loadSlot, dischargeSlot);
	}

	@Test
	public void testCreatePortString() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreatePortStringFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateTimeWindow() {

		final SchedulerBuilder builder = new SchedulerBuilder();
		final ITimeWindow window = builder.createTimeWindow(10, 20);

		Assert.assertEquals(10, window.getStart());
		Assert.assertEquals(20, window.getEnd());

		fail("Not yet implemented - Test internal state");
	}

	@Test
	public void testCreateVessel() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPortToPortDistance() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetElementDurations() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOptimisationData() {
		fail("Not yet implemented");
	}

	@Test
	public void testDispose() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateVesselClass() {

		final SchedulerBuilder builder = new SchedulerBuilder();

		final int minSpeed = 1;
		final int maxSpeed = 2;
		final long capacity = 3l;
		final int minHeel = 4;

		final IVesselClass vesselClass = builder.createVesselClass("name",
				minSpeed, maxSpeed, capacity, minHeel, 7000, 1000, 1234, 35353, 80808, 10101);
		// createVesselClass("name", minSpeed,
		// maxSpeed, capacity, minHeel, 700;

		Assert.assertEquals(minSpeed, vesselClass.getMinSpeed());
		Assert.assertEquals(maxSpeed, vesselClass.getMaxSpeed());
		Assert.assertEquals(capacity, vesselClass.getCargoCapacity());
		Assert.assertEquals(minHeel, vesselClass.getMinHeel());
		Assert.assertEquals(7000, vesselClass.getBaseFuelUnitPrice());
		Assert.assertEquals(1000, vesselClass.getBaseFuelConversionFactor());
		Assert.assertEquals(1234, vesselClass.getHourlyCharterInPrice());

		Assert.assertEquals(35353, vesselClass.getWarmupTime());
		Assert.assertEquals(80808, vesselClass.getCooldownTime());
		Assert.assertEquals(10101, vesselClass.getCooldownVolume());
		
		fail("Not yet implemented - Internal state checks");
	}

	@Test
	public void testSetVesselClassStateParamaters() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetVesselClassStateParamaters2() {
		final IVesselClass vc = context.mock(IVesselClass.class);

		final SchedulerBuilder builder = new SchedulerBuilder();

		builder.setVesselClassStateParamaters(vc, null, 0, 0, 0, null, 0);
	}

}
