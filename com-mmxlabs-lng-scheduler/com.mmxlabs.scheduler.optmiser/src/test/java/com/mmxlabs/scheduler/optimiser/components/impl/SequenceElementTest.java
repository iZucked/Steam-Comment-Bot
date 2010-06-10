package com.mmxlabs.scheduler.optimiser.components.impl;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@RunWith(JMock.class)
public class SequenceElementTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testSequenceElementStringIPortICargo() {

		final String name = "name";
		final IPort port = context.mock(IPort.class);
		final ICargo cargo = context.mock(ICargo.class);
		final SequenceElement element = new SequenceElement(name, port, cargo);

		Assert.assertSame(name, element.getName());
		Assert.assertSame(port, element.getPort());
		Assert.assertSame(cargo, element.getCargo());
	}

	@Test
	public void testGetSetPort() {

		final SequenceElement element = new SequenceElement();
		Assert.assertNull(element.getPort());
		final IPort port = context.mock(IPort.class);
		element.setPort(port);
		Assert.assertSame(port, element.getPort());
	}

	@Test
	public void testGetSetName() {

		final SequenceElement element = new SequenceElement();
		Assert.assertNull(element.getName());
		final String name = "name";
		element.setName(name);
		Assert.assertSame(name, element.getName());
	}

	@Test
	public void testGetSetCargo() {

		final SequenceElement element = new SequenceElement();
		Assert.assertNull(element.getCargo());
		final ICargo cargo = context.mock(ICargo.class);
		element.setCargo(cargo);
		Assert.assertSame(cargo, element.getCargo());
	}
}
