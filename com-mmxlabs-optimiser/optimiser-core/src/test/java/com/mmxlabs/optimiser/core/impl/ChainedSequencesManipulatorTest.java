/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;

@RunWith(JMock.class)
public class ChainedSequencesManipulatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testChainedSequencesManipulator() {

		final ChainedSequencesManipulator manipulator = new ChainedSequencesManipulator();

		final ISequencesManipulator mock1 = context.mock(ISequencesManipulator.class, "mock1");
		final ISequencesManipulator mock2 = context.mock(ISequencesManipulator.class, "mock2");

		manipulator.addDelegate(mock1);
		manipulator.addDelegate(mock2);

		final IModifiableSequences sequences = context.mock(IModifiableSequences.class);

		final Sequence sequence = context.sequence("sequence");
		context.checking(new Expectations() {
			{
				one(mock1).manipulate(sequences);
				inSequence(sequence);
				one(mock2).manipulate(sequences);
				inSequence(sequence);
			}
		});

		manipulator.manipulate(sequences);

		context.assertIsSatisfied();
	}

	@Test
	public void testDispose() {
		final ChainedSequencesManipulator manipulator = new ChainedSequencesManipulator();

		final ISequencesManipulator mock1 = context.mock(ISequencesManipulator.class, "mock1");
		final ISequencesManipulator mock2 = context.mock(ISequencesManipulator.class, "mock2");

		manipulator.addDelegate(mock1);
		manipulator.addDelegate(mock2);

		final IModifiableSequences sequences = context.mock(IModifiableSequences.class);

		context.checking(new Expectations() {
			{
				one(mock1).dispose();
				one(mock2).dispose();
			}
		});

		manipulator.dispose();
		manipulator.manipulate(sequences);

		context.assertIsSatisfied();
	}

}
