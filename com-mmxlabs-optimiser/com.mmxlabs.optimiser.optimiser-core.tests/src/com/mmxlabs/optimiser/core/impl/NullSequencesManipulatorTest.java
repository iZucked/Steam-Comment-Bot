/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IModifiableSequences;

public class NullSequencesManipulatorTest {

	@Test
	public void testManipulate() {
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		final NullSequencesManipulator manipulator = new NullSequencesManipulator();

		manipulator.manipulate(sequences);

		Mockito.verifyNoMoreInteractions(sequences);
	}
}
