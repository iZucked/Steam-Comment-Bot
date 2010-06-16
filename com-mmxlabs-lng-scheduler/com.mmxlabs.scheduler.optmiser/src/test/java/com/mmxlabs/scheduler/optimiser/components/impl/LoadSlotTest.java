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
public class LoadSlotTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetLoadPort() {

		final IPort port = context.mock(IPort.class);

		final LoadSlot slot = new LoadSlot();
		Assert.assertNull(slot.getLoadPort());
		slot.setLoadPort(port);
		Assert.assertSame(port, slot.getLoadPort());
	}

	@Test
	public void testGetSetLoadWindow() {
		final ITimeWindow window = context.mock(ITimeWindow.class);
		final LoadSlot slot = new LoadSlot();
		Assert.assertNull(slot.getLoadWindow());
		slot.setLoadWindow(window);
		Assert.assertSame(window, slot.getLoadWindow());
	}

	@Test
	public void testGetSetLoadID() {
		final String id = "id";
		final LoadSlot slot = new LoadSlot();
		Assert.assertNull(slot.getLoadID());
		slot.setLoadID(id);
		Assert.assertSame(id, slot.getLoadID());

	}

	@Test
	public void testGetSetMinLoadVolume() {
		final long value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getMinLoadVolume());
		slot.setMinLoadVolume(value);
		Assert.assertEquals(value, slot.getMinLoadVolume());
	}

	@Test
	public void testGetSetMaxLoadVolume() {
		final long value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getMaxLoadVolume());
		slot.setMaxLoadVolume(value);
		Assert.assertEquals(value, slot.getMaxLoadVolume());
	}


	@Test
	public void testGetSetPurchasePrice() {
		final long value = 10;
		final LoadSlot slot = new LoadSlot();
		Assert.assertEquals(0, slot.getPurchasePrice());
		slot.setPurchasePrice(value);
		Assert.assertEquals(value, slot.getPurchasePrice());
	}
}
