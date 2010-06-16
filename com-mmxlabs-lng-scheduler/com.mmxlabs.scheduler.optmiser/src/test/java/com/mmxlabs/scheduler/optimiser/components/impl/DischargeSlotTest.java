package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class DischargeSlotTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetDischargePort() {

		final IPort port = context.mock(IPort.class);

		final DischargeSlot slot = new DischargeSlot();
		Assert.assertNull(slot.getDischargePort());
		slot.setDischargePort(port);
		Assert.assertSame(port, slot.getDischargePort());
	}

	@Test
	public void testGetSetDischargeWindow() {
		final ITimeWindow window = context.mock(ITimeWindow.class);
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertNull(slot.getDischargeWindow());
		slot.setDischargeWindow(window);
		Assert.assertSame(window, slot.getDischargeWindow());
	}

	@Test
	public void testGetSetDischargeID() {
		final String id = "id";
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertNull(slot.getDischargeID());
		slot.setDischargeID(id);
		Assert.assertSame(id, slot.getDischargeID());
	}

	@Test
	public void testGetSetMinDischargeVolume() {
		final long value = 10;
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertEquals(0, slot.getMinDischargeVolume());
		slot.setMinDischargeVolume(value);
		Assert.assertEquals(value, slot.getMinDischargeVolume());
	}

	@Test
	public void testGetSetMaxDischargeVolume() {
		final long value = 10;
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertEquals(0, slot.getMaxDischargeVolume());
		slot.setMaxDischargeVolume(value);
		Assert.assertEquals(value, slot.getMaxDischargeVolume());
	}

	@Test
	public void testGetSetSalesPrice() {
		final long value = 10;
		final DischargeSlot slot = new DischargeSlot();
		Assert.assertEquals(0, slot.getSalesPrice());
		slot.setSalesPrice(value);
		Assert.assertEquals(value, slot.getSalesPrice());
	}

}
