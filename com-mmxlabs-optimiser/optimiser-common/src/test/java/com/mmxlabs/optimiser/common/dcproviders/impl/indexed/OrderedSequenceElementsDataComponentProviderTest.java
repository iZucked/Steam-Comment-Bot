/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;

public class OrderedSequenceElementsDataComponentProviderTest {

	@Test
	public void testOrderedSequenceElementsDataComponentProvider() {

		final String name = "name";
		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider(name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testElementOrder() {

		final MockSequenceElement obj1 = new MockSequenceElement(1);
		final MockSequenceElement obj2 = new MockSequenceElement(2);

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("name");

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

		final MockSequenceElement obj1 = new MockSequenceElement(1);
		final MockSequenceElement obj2 = new MockSequenceElement(2);

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("name");

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
