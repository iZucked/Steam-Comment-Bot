package com.mmxlabs.optimiser.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.impl.UnmodifiableSequenceWrapper;

/**
 * Ensure the {@link UnmodifiableSequenceWrapper} correctly invokes methods on
 * the target object.
 */
@RunWith(JMock.class)
public class UnmodifiableSequenceWrapperTest {

	Mockery context = new JUnit4Mockery();

	@SuppressWarnings("unchecked")
	@Test
	public void testGet() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				target);

		context.checking(new Expectations() {
			{
				oneOf(target).get(0);
			}
		});

		wrapped.get(0);

		context.assertIsSatisfied();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				target);

		context.checking(new Expectations() {
			{
				oneOf(target).iterator();
			}
		});

		wrapped.iterator();

		context.assertIsSatisfied();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetSegment() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				target);

		context.checking(new Expectations() {
			{
				oneOf(target).size();
			}
		});

		wrapped.size();

		context.assertIsSatisfied();
	}

}