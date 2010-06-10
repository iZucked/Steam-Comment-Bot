package com.mmxlabs.scheduler.optimiser.components.impl;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class CargoTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetId() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getId());
		final String id = "id";
		cargo.setId(id);
		Assert.assertSame(id, cargo.getId());
	}

	@Test
	public void testGetSetDischargePort() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getDischargePort());
		final IPort port = context.mock(IPort.class);
		cargo.setDischargePort(port);
		Assert.assertSame(port, cargo.getDischargePort());
	}

	@Test
	public void testGetSetLoadPort() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getLoadPort());
		final IPort port = context.mock(IPort.class);
		cargo.setLoadPort(port);
		Assert.assertSame(port, cargo.getLoadPort());
	}

	@Test
	public void testGetSetDischargeWindow() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getDischargeWindow());
		final ITimeWindow window = context.mock(ITimeWindow.class);
		cargo.setDischargeWindow(window);
		Assert.assertSame(window, cargo.getDischargeWindow());
	}

	@Test
	public void testGetSetLoadWindow() {

		final Cargo cargo = new Cargo();
		Assert.assertNull(cargo.getLoadWindow());
		final ITimeWindow window = context.mock(ITimeWindow.class);
		cargo.setLoadWindow(window);
		Assert.assertSame(window, cargo.getLoadWindow());
	}
}
