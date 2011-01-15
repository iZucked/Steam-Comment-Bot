/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.impl.NullSequencesManipulator;

@RunWith(JMock.class)
public class NullSequencesManipulatorTest {

	Mockery context = new JUnit4Mockery();
	
	@SuppressWarnings("unchecked")
	@Test
	public void testManipulate() {
		final IModifiableSequences<Object> sequences = context.mock(IModifiableSequences.class);
		
		final NullSequencesManipulator<Object> manipulator = new NullSequencesManipulator<Object>();
		
		context.checking(new Expectations() {
			{
				// Expect nothing to happen
			}
		});

		manipulator.manipulate(sequences);
		
		context.assertIsSatisfied();
	}
}
