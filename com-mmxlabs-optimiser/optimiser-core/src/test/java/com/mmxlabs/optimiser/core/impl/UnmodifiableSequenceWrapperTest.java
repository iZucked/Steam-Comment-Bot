/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.UnmodifiableSequenceWrapper;

/**
 * Ensure the {@link UnmodifiableSequenceWrapper} correctly invokes methods on the target object.
 */
@RunWith(JMock.class)
public class UnmodifiableSequenceWrapperTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGet() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(target);

		context.checking(new Expectations() {
			{
				oneOf(target).get(0);
			}
		});

		wrapped.get(0);

		context.assertIsSatisfied();

	}

	@Test
	public void testIterator() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(target);

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
	public void testIterator2() {

		final ISequenceElement element = context.mock(ISequenceElement.class);
		final Iterator<ISequenceElement> target = context.mock(Iterator.class);

		final ISequence sequence = new ISequence() {

			@Override
			public ISequenceElement get(int index) {
				return null;
			}

			@Override
			public ISegment getSegment(int start, int end) {
				return null;
			}

			@Override
			public int size() {
				return 0;
			}

			@Override
			public Iterator<ISequenceElement> iterator() {
				return target;
			}

			@Override
			public final ISequenceElement last() {
				return get(size() - 1);
			}

			@Override
			public ISequenceElement first() {
				return get(0);
			}
		};

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(sequence);

		Iterator<ISequenceElement> wrappedItr = wrapped.iterator();
		context.checking(new Expectations() {
			{
				oneOf(target).next();
				will(returnValue(element));
				
			}
		});

		wrappedItr.next();

		context.assertIsSatisfied();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator3() {

		final Iterator<ISequenceElement> target = context.mock(Iterator.class);

		final ISequence sequence = new ISequence() {

			@Override
			public ISequenceElement get(int index) {
				return null;
			}

			@Override
			public ISegment getSegment(int start, int end) {
				return null;
			}

			@Override
			public int size() {
				return 0;
			}

			@Override
			public Iterator<ISequenceElement> iterator() {
				return target;
			}

			@Override
			public final ISequenceElement last() {
				return get(size() - 1);
			}

			@Override
			public ISequenceElement first() {
				return get(0);
			}
		};

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(sequence);

		Iterator<ISequenceElement> wrappedItr = wrapped.iterator();
		context.checking(new Expectations() {
			{
				oneOf(target).hasNext();
			}
		});

		wrappedItr.hasNext();

		context.assertIsSatisfied();

	}

	@SuppressWarnings("unchecked")
	@Test(expected = UnsupportedOperationException.class)
	public void testIterator4() {

		final Iterator<ISequenceElement> target = context.mock(Iterator.class);

		final ISequence sequence = new ISequence() {

			@Override
			public ISequenceElement get(int index) {
				return null;
			}

			@Override
			public ISegment getSegment(int start, int end) {
				return null;
			}

			@Override
			public int size() {
				return 0;
			}

			@Override
			public Iterator<ISequenceElement> iterator() {
				return target;
			}

			@Override
			public final ISequenceElement last() {
				return get(size() - 1);
			}

			@Override
			public ISequenceElement first() {
				return get(0);
			}
		};

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(sequence);

		Iterator<ISequenceElement> wrappedItr = wrapped.iterator();
		context.checking(new Expectations() {
			{
				// Exception should be thrown
			}
		});

		wrappedItr.remove();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetSegment() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(target);

		final int start = 10;
		final int end = 15;

		context.checking(new Expectations() {
			{
				oneOf(target).getSegment(start, end);
			}
		});

		wrapped.getSegment(start, end);

		context.assertIsSatisfied();
	}
}