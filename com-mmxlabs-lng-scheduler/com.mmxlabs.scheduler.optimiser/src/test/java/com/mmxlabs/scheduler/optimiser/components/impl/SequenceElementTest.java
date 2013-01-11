/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

@RunWith(JMock.class)
public class SequenceElementTest {
	final IIndexingContext index = new SimpleIndexingContext();
	Mockery context = new JUnit4Mockery();

	@Test
	public void testSequenceElementStringIPortICargo() {

		final String name = "name";
		final SequenceElement element = new SequenceElement(index, name);

		Assert.assertSame(name, element.getName());
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
