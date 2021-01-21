/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;

public class OrderedSequenceElementsDataComponentProviderTest {

	@Test
	public void testElementOrder() {

		final MockSequenceElement obj1 = new MockSequenceElement(1);
		final MockSequenceElement obj2 = new MockSequenceElement(2);

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
