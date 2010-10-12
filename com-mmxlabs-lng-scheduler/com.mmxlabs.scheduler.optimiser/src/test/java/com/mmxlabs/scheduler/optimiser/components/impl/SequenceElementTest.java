package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.SimpleIndexingContext;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@RunWith(JMock.class)
public class SequenceElementTest {
	final IIndexingContext index = new SimpleIndexingContext();
	Mockery context = new JUnit4Mockery();

	@Test
	public void testSequenceElementStringIPortICargo() {

		final String name = "name";
		final IPortSlot slot = context.mock(IPortSlot.class);
		final SequenceElement element = new SequenceElement(index, name, slot);

		Assert.assertSame(name, element.getName());
		Assert.assertSame(slot, element.getPortSlot());
	}

	@Test
	public void testGetSetPortSlot() {

		final SequenceElement element = new SequenceElement(index);
		Assert.assertNull(element.getPortSlot());
		final IPortSlot slot = context.mock(IPortSlot.class);
		element.setPortSlot(slot);
		Assert.assertSame(slot, element.getPortSlot());
	}

	@Test
	public void testGetSetName() {

		final SequenceElement element = new SequenceElement(index);
		Assert.assertNull(element.getName());
		final String name = "name";
		element.setName(name);
		Assert.assertSame(name, element.getName());
	}
}
