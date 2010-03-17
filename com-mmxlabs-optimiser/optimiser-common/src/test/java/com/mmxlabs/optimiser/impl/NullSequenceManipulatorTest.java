package com.mmxlabs.optimiser.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import com.mmxlabs.optimiser.IModifiableSequence;


public class NullSequenceManipulatorTest {

	Mockery context = new JUnit4Mockery();
	
	@SuppressWarnings("unchecked")
	@Test
	public void testManipulate() {
		final IModifiableSequence<Object> sequence = context.mock(IModifiableSequence.class);
		
		final NullSequenceManipulator<Object> manipulator = new NullSequenceManipulator<Object>();
		
		context.checking(new Expectations() {
			{
				
			}
		});

		manipulator.manipulate(sequence);
		
		context.assertIsSatisfied();
	}
}
