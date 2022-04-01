/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Test the {@link ListSequence} against the documented API. TODO: Use JMock to test internal calls against a List object.
 * 
 * @author Simon Goodall
 * 
 */
public class ListSequenceTest {

	@Test
	public void testConstructor() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertSame(object1, sequence.get(0));
		Assertions.assertSame(object2, sequence.get(1));

		final ListSequence sequence2 = new ListSequence(sequence);
		Assertions.assertSame(object1, sequence2.get(0));
		Assertions.assertSame(object2, sequence2.get(1));
	}

	/**
	 * Test sequence iterator method
	 */
	@Test
	public void testIterator() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		final Iterator<ISequenceElement> iterator = sequence.iterator();
		Assertions.assertTrue(iterator.hasNext());
		final ISequenceElement iterObject1 = iterator.next();
		Assertions.assertSame(object1, iterObject1);

		Assertions.assertTrue(iterator.hasNext());
		final ISequenceElement iterObject2 = iterator.next();
		Assertions.assertSame(object2, iterObject2);
	}

	/**
	 * Test sequence iterator remove method
	 */
	@Test
	public void testIterator_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		final Iterator<ISequenceElement> iterator = sequence.iterator();
		Assertions.assertTrue(iterator.hasNext());
		final Object iterObject1 = iterator.next();
		Assertions.assertSame(object1, iterObject1);

		// Expect to fail here
		Assertions.assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
	}

	@Test()
	public void testGet() {

		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(element);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertEquals(1, sequence.size());
		Assertions.assertEquals(element, sequence.get(0));

	}

	@Test
	public void testGet_2() {
		final List<@NonNull ISequenceElement> emptyList = Collections.emptyList();
		final ListSequence sequence = new ListSequence(emptyList);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.get(0));
	}

	@Test
	public void testGet_3() {
		final List<@NonNull ISequenceElement> emptyList = Collections.emptyList();
		final ListSequence sequence = new ListSequence(emptyList);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.get(-1));
	}

	@Test
	public void testGetSegment() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		final ISegment segment = sequence.getSegment(0, 2);

		Assertions.assertEquals(2, segment.size());

		Assertions.assertEquals(0, segment.getSequenceStart());
		Assertions.assertEquals(2, segment.getSequenceEnd());

		Assertions.assertSame(sequence, segment.getSequence());

		Assertions.assertSame(object1, segment.get(0));
		Assertions.assertSame(object2, segment.get(1));

		final Iterator<ISequenceElement> iterator = segment.iterator();

		Assertions.assertTrue(iterator.hasNext());
		final Object iterObject1 = iterator.next();
		Assertions.assertSame(object1, iterObject1);

		Assertions.assertTrue(iterator.hasNext());
		final Object iterObject2 = iterator.next();
		Assertions.assertSame(object2, iterObject2);

		iterator.remove();

		Assertions.assertEquals(1, segment.size());
		Assertions.assertEquals(2, sequence.size());

		Assertions.assertSame(object1, segment.get(0));

		Assertions.assertSame(object1, sequence.get(0));
		Assertions.assertSame(object2, sequence.get(1));
	}

	@Test
	public void testGetSegment_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.getSegment(-1, 2));
	}

	@Test
	public void testGetSegment_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.getSegment(0, 3));
	}

	@Test
	public void testSize() {
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(element);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertEquals(1, sequence.size());
	}

	@Test
	public void testToString() {
		final List<@NonNull ISequenceElement> l = Collections.emptyList();
		final ListSequence sequence = new ListSequence(l);
		Assertions.assertNotNull(sequence.toString());
	}

	@Test
	public void testFirst1() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertSame(object1, sequence.first());
	}

	@Test
	public void testFirst2() {

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList();
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.first());
	}

	@Test
	public void testLast1() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertSame(object2, sequence.last());
	}

	@Test
	public void testLast2() {

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList();
		final ListSequence sequence = new ListSequence(l);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.last());
	}

	@Test
	public void testEquals() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l1 = CollectionsUtil.makeArrayList(object1, object2);
		final List<@NonNull ISequenceElement> l2 = CollectionsUtil.makeArrayList(object1, object2);

		final List<@NonNull ISequenceElement> l3 = CollectionsUtil.makeArrayList(object1);
		final List<@NonNull ISequenceElement> l4 = CollectionsUtil.makeArrayList(object2);

		final ListSequence sequence0 = new ListSequence(l1);
		// Same list as sequence0
		final ListSequence sequence1 = new ListSequence(l1);
		// Same list contents as sequence0
		final ListSequence sequence2 = new ListSequence(l2);
		final ListSequence sequence3 = new ListSequence(l3);
		final ListSequence sequence4 = new ListSequence(l4);

		Assertions.assertTrue(sequence0.equals(sequence0));
		Assertions.assertTrue(sequence0.equals(sequence1));
		Assertions.assertTrue(sequence0.equals(sequence2));
		Assertions.assertFalse(sequence0.equals(sequence3));
		Assertions.assertFalse(sequence0.equals(sequence4));

		Assertions.assertTrue(sequence0.equals(sequence0));
		Assertions.assertTrue(sequence1.equals(sequence0));
		Assertions.assertTrue(sequence2.equals(sequence0));
		Assertions.assertFalse(sequence3.equals(sequence0));
		Assertions.assertFalse(sequence4.equals(sequence0));
	}

}
