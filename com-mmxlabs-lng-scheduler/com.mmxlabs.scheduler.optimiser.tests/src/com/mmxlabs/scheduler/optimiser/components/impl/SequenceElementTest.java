/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class SequenceElementTest {
	final IIndexingContext index = new SimpleIndexingContext();

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
