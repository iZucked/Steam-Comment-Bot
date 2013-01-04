package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

@RunWith(JMock.class)
public class PortOptionsTest {
	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetVisitDuration() {

		final int value = 100;
		final PortOptions options = new PortOptions();		
		Assert.assertEquals(0, options.getVisitDuration());
		options.setVisitDuration(value);
		Assert.assertEquals(value, options.getVisitDuration());
	}
	
	@Test
	public void testGetSetPortSlot() {
		final IPortSlot slot = context.mock(IPortSlot.class);

		final PortOptions options = new PortOptions();
		Assert.assertNull(options.getPortSlot());
		options.setPortSlot(slot);
		Assert.assertSame(slot, options.getPortSlot());
	}


	@Test
	public void testGetSetVessel() {
		final IVessel vessel = context.mock(IVessel.class);

		final PortOptions options = new PortOptions();
		Assert.assertNull(options.getVessel());
		options.setVessel(vessel);
		Assert.assertSame(vessel, options.getVessel());
	}

}
