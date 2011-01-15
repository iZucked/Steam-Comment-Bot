/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;

public class OrderedSequenceElementsDataComponentProviderTest {

	@Test
	public void testOrderedSequenceElementsDataComponentProvider() {

		final String name = "name";
		final OrderedSequenceElementsDataComponentProvider<Object> provider = new OrderedSequenceElementsDataComponentProvider<Object>(
				name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testElementOrder() {

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final OrderedSequenceElementsDataComponentProvider<Object> provider = new OrderedSequenceElementsDataComponentProvider<Object>(
				"name");

		Assert.assertNull(provider.getNextElement(obj1));
		Assert.assertNull(provider.getPreviousElement(obj1));

		Assert.assertNull(provider.getNextElement(obj2));
		Assert.assertNull(provider.getPreviousElement(obj2));

		provider.setElementOrder(obj1, obj2);

		Assert.assertSame(obj2, provider.getNextElement(obj1));
		Assert.assertNull(provider.getPreviousElement(obj1));

		Assert.assertNull(provider.getNextElement(obj2));
		Assert.assertSame(obj1, provider.getPreviousElement(obj2));
	}

	@Test
	public void testDispose() {

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final OrderedSequenceElementsDataComponentProvider<Object> provider = new OrderedSequenceElementsDataComponentProvider<Object>(
				"name");

		Assert.assertNull(provider.getNextElement(obj1));
		Assert.assertNull(provider.getPreviousElement(obj1));

		Assert.assertNull(provider.getNextElement(obj2));
		Assert.assertNull(provider.getPreviousElement(obj2));

		provider.setElementOrder(obj1, obj2);

		Assert.assertSame(obj2, provider.getNextElement(obj1));
		Assert.assertNull(provider.getPreviousElement(obj1));

		Assert.assertNull(provider.getNextElement(obj2));
		Assert.assertSame(obj1, provider.getPreviousElement(obj2));

		provider.dispose();

		Assert.assertNull(provider.getNextElement(obj1));
		Assert.assertNull(provider.getPreviousElement(obj1));

		Assert.assertNull(provider.getNextElement(obj2));
		Assert.assertNull(provider.getPreviousElement(obj2));

	}
}
