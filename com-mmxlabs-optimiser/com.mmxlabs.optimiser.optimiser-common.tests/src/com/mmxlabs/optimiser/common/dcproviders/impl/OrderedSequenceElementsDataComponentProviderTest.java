/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class OrderedSequenceElementsDataComponentProviderTest {

	@Test
	public void testElementOrder() {

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider();

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
}
