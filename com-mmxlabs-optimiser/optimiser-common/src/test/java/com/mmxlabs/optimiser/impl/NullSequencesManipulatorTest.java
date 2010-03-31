package com.mmxlabs.optimiser.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import com.mmxlabs.optimiser.IModifiableSequences;


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
