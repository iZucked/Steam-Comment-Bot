/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class OrderedSequenceElementsDataComponentProviderTest {

	@Test
	public void testElementOrder() {

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider();

		Assertions.assertNull(provider.getNextElement(obj1));
		Assertions.assertNull(provider.getPreviousElement(obj1));

		Assertions.assertNull(provider.getNextElement(obj2));
		Assertions.assertNull(provider.getPreviousElement(obj2));

		provider.setElementOrder(obj1, obj2);

		Assertions.assertSame(obj2, provider.getNextElement(obj1));
		Assertions.assertNull(provider.getPreviousElement(obj1));

		Assertions.assertNull(provider.getNextElement(obj2));
		Assertions.assertSame(obj1, provider.getPreviousElement(obj2));
	}
}
