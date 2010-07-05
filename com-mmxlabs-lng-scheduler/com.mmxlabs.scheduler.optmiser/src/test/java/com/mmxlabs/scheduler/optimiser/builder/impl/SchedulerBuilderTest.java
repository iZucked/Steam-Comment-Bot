package com.mmxlabs.scheduler.optimiser.builder.impl;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

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

		SchedulerBuilder builder = new SchedulerBuilder();

		IPort port = context.mock(IPort.class);
		ITimeWindow window = builder.createTimeWindow(0, 0);

		builder.createLoadSlot("id", port, window, 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLoadSlot3() {

		SchedulerBuilder builder = new SchedulerBuilder();

		IPort port = builder.createPort("port");
		ITimeWindow window = context.mock(ITimeWindow.class);

		builder.createLoadSlot("id", port, window, 0, 0, 0);
	}

	@Test
	public void testCreateDischargeSlot() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDischargeSlot2() {

		SchedulerBuilder builder = new SchedulerBuilder();

		IPort port = context.mock(IPort.class);
		ITimeWindow window = builder.createTimeWindow(0, 0);

		builder.createDischargeSlot("id", port, window, 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDischargeSlot3() {

		SchedulerBuilder builder = new SchedulerBuilder();

		IPort port = builder.createPort("port");
		ITimeWindow window = context.mock(ITimeWindow.class);

		builder.createDischargeSlot("id", port, window, 0, 0, 0);
	}

	@Test
	public void testCreateCargo() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCargo2() {

		SchedulerBuilder builder = new SchedulerBuilder();

		IPort port = builder.createPort("port");
		ITimeWindow window = builder.createTimeWindow(0, 0);

		ILoadSlot loadSlot = context.mock(ILoadSlot.class);

		IDischargeSlot dischargeSlot = builder.createDischargeSlot("id", port,
				window, 0, 0, 0);

		builder.createCargo("id", loadSlot, dischargeSlot);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCargo3() {

		SchedulerBuilder builder = new SchedulerBuilder();

		IPort port = builder.createPort("port");
		ITimeWindow window = builder.createTimeWindow(0, 0);

		ILoadSlot loadSlot = builder
				.createLoadSlot("id", port, window, 0, 0, 0);
		IDischargeSlot dischargeSlot = context.mock(IDischargeSlot.class);

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

		SchedulerBuilder builder = new SchedulerBuilder();
		ITimeWindow window = builder.createTimeWindow(10, 20);

		Assert.assertEquals(10, window.getStart());
		Assert.assertEquals(20, window.getEnd());
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
		fail("Not yet implemented");
	}

	@Test
	public void testSetVesselClassStateParamaters() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetVesselClassStateParamaters2() {
		IVesselClass vc = context.mock(IVesselClass.class);

		SchedulerBuilder builder = new SchedulerBuilder();

		builder.setVesselClassStateParamaters(vc, null, 0, 0, 0, null, 0);
	}

}
