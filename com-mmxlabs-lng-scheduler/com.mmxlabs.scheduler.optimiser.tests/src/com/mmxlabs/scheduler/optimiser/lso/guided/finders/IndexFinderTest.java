/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequence;

public class IndexFinderTest {

	@Test
	public void testIndexFinder() {

		final ISequence sequenceA = Mockito.mock(ISequence.class);

		Assertions.assertEquals(1, new IndexFinder(1).findInsertionIndex(sequenceA));
	}

}
