/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Ensure the {@link UnmodifiableSequenceWrapper} correctly invokes methods on the target object.
 */
public class UnmodifiableSequenceWrapperTest {

	@Test
	public void testGet() {

		final ISequence target = Mockito.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(target);

		wrapped.get(0);

		Mockito.verify(target).get(0);

	}

	@Test
	public void testIterator() {

		final ISequence target = Mockito.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(target);

		wrapped.iterator();
		Mockito.verify(target).iterator();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator2() {

		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final Iterator<ISequenceElement> target = Mockito.mock(Iterator.class);
		final ISequence sequence = Mockito.mock(ISequence.class);
		Mockito.when(sequence.iterator()).thenReturn(target);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(sequence);

		final Iterator<ISequenceElement> wrappedItr = wrapped.iterator();

		Mockito.when(target.next()).thenReturn(element);
		final ISequenceElement next = wrappedItr.next();
		Assert.assertEquals(element, next);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator3() {

		final Iterator<ISequenceElement> target = Mockito.mock(Iterator.class);
		final ISequence sequence = Mockito.mock(ISequence.class);
		Mockito.when(sequence.iterator()).thenReturn(target);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(sequence);

		final Iterator<ISequenceElement> wrappedItr = wrapped.iterator();

		wrappedItr.hasNext();
		Mockito.verify(target).hasNext();

	}

	@SuppressWarnings("unchecked")
	@Test(expected = UnsupportedOperationException.class)
	public void testIterator4() {

		final Iterator<ISequenceElement> target = Mockito.mock(Iterator.class);

		final ISequence sequence = Mockito.mock(ISequence.class);
		Mockito.when(sequence.iterator()).thenReturn(target);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(sequence);

		final Iterator<ISequenceElement> wrappedItr = wrapped.iterator();
		wrappedItr.remove();
	}

	@Test
	public void testGetSegment() {

		final ISequence target = Mockito.mock(ISequence.class);

		final UnmodifiableSequenceWrapper wrapped = new UnmodifiableSequenceWrapper(target);

		final int start = 10;
		final int end = 15;

		wrapped.getSegment(start, end);
		Mockito.verify(target).getSegment(start, end);
	}
}