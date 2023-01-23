/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;

public class BeforeElementFinderTest {

	@Test
	public void testBeforeElementFinder() {

		final ISequenceElement elementResourceAStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementResourceAEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementResourceAStart, elementA, elementB, elementC, elementD, elementResourceAEnd));

		Assertions.assertEquals(0, new BeforeElementFinder(elementResourceAStart).findInsertionIndex(sequenceA));
		Assertions.assertEquals(1, new BeforeElementFinder(elementA).findInsertionIndex(sequenceA));
		Assertions.assertEquals(2, new BeforeElementFinder(elementB).findInsertionIndex(sequenceA));
		Assertions.assertEquals(3, new BeforeElementFinder(elementC).findInsertionIndex(sequenceA));
		Assertions.assertEquals(4, new BeforeElementFinder(elementD).findInsertionIndex(sequenceA));
		Assertions.assertEquals(5, new BeforeElementFinder(elementResourceAEnd).findInsertionIndex(sequenceA));

		Assertions.assertEquals(-1, new BeforeElementFinder(elementE).findInsertionIndex(sequenceA));
	}

}
