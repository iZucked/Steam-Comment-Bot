/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;

@SuppressWarnings("null")
public class ChainedSequencesManipulatorTest {

	@Test
	public void testChainedSequencesManipulator() {

		final ChainedSequencesManipulator manipulator = new ChainedSequencesManipulator();

		final ISequencesManipulator mock1 = Mockito.mock(ISequencesManipulator.class, "mock1");
		final ISequencesManipulator mock2 = Mockito.mock(ISequencesManipulator.class, "mock2");

		manipulator.addDelegate(mock1);
		manipulator.addDelegate(mock2);

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		manipulator.manipulate(sequences);

		Mockito.verify(mock1).manipulate(sequences);
		Mockito.verify(mock2).manipulate(sequences);
	}
}
