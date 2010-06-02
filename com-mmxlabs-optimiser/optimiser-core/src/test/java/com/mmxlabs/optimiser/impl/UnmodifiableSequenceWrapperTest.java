package com.mmxlabs.optimiser.impl;

import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.ISegment;
import com.mmxlabs.optimiser.ISequence;

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
	public void testIterator2() {

		final Iterator target = context.mock(Iterator.class);

		final ISequence sequence = new ISequence() {

			@Override
			public Object get(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ISegment getSegment(int start, int end) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Iterator iterator() {
				return target;
			}
		};

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				sequence);

		Iterator wrappedItr = wrapped.iterator();
		context.checking(new Expectations() {
			{
				oneOf(target).next();
			}
		});

		wrappedItr.next();

		context.assertIsSatisfied();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator3() {

		final Iterator target = context.mock(Iterator.class);

		final ISequence sequence = new ISequence() {

			@Override
			public Object get(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ISegment getSegment(int start, int end) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Iterator iterator() {
				return target;
			}
		};

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				sequence);

		Iterator wrappedItr = wrapped.iterator();
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

		final Iterator target = context.mock(Iterator.class);

		final ISequence sequence = new ISequence() {

			@Override
			public Object get(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ISegment getSegment(int start, int end) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Iterator iterator() {
				return target;
			}
		};

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				sequence);

		Iterator wrappedItr = wrapped.iterator();
		context.checking(new Expectations() {
			{
				// Exception should be thrown
			}
		});

		wrappedItr.remove();

		context.assertIsSatisfied();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetSegment() {

		final ISequence target = context.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(
				target);

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