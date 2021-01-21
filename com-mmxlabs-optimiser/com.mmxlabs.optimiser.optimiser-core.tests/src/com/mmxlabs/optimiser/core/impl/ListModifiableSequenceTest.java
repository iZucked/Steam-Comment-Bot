/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Test the {@link ListModifiableSequence} against the documented API. Most of this test case should be reusable against other {@link IModifiableSequence} implementations.
 * 
 * @author Simon Goodall
 * 
 */
@SuppressWarnings("null")
public class ListModifiableSequenceTest {

	private ListModifiableSequence sequence;

	@BeforeEach
	public void setUp() {
		sequence = new ListModifiableSequence(new ArrayList<>(10));
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	/**
	 * Test sequence iterator method
	 */
	@Test
	public void testIterator() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		final Iterator<ISequenceElement> iterator = sequence.iterator();
		Assertions.assertTrue(iterator.hasNext());
		final Object iterObject1 = iterator.next();
		Assertions.assertSame(object1, iterObject1);

		Assertions.assertTrue(iterator.hasNext());
		final Object iterObject2 = iterator.next();
		Assertions.assertSame(object2, iterObject2);

		iterator.remove();

		Assertions.assertEquals(1, sequence.size());

		Assertions.assertSame(object1, sequence.get(0));
	}

	@Test
	public void testAdd() {

		Assertions.assertEquals(0, sequence.size());
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");
		sequence.add(element);
		Assertions.assertEquals(1, sequence.size());
		Assertions.assertEquals(element, sequence.get(0));
	}

	@Test
	public void testInsertIntT() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");

		sequence.add(object1);
		sequence.add(object2);

		sequence.insert(1, object3);

		Assertions.assertEquals(3, sequence.size());

		Assertions.assertSame(object1, sequence.get(0));
		Assertions.assertSame(object3, sequence.get(1));
		Assertions.assertSame(object2, sequence.get(2));
	}

	@Test
	public void testInsertIntT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.insert(-1, object3));
	}

	@Test
	public void testInsertIntT_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.insert(3, object3));
	}

	@Test
	public void testInsertIntISegmentOfT() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object2);

		final List<ISequenceElement> segmentList = new LinkedList<>();
		segmentList.add(object3);
		segmentList.add(object4);
		final ISegment segment = new ListSegment(segmentList, sequence, 0, 1);

		sequence.insert(1, segment);

		Assertions.assertEquals(4, sequence.size());

		Assertions.assertSame(object1, sequence.get(0));
		Assertions.assertSame(object3, sequence.get(1));
		Assertions.assertSame(object4, sequence.get(2));
		Assertions.assertSame(object2, sequence.get(3));
	}

	@Test
	public void testInsertIntISegmentOfT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object2);

		final List<ISequenceElement> segmentList = new LinkedList<>();
		segmentList.add(object3);
		segmentList.add(object4);
		final ISegment segment = new ListSegment(segmentList, sequence, 0, 1);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.insert(-1, segment));
	}

	@Test
	public void testInsertIntISegmentOfT_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object2);

		final List<ISequenceElement> segmentList = new LinkedList<>();
		segmentList.add(object3);
		segmentList.add(object4);
		final ISegment segment = new ListSegment(segmentList, sequence, 0, 1);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.insert(3, segment));
	}

	@Test
	public void testRemoveInt() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0);

		Assertions.assertEquals(1, sequence.size());
		Assertions.assertSame(object2, sequence.get(0));
	}

	@Test
	public void testRemoveT() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertTrue(sequence.remove(object1));

		Assertions.assertEquals(1, sequence.size());
		Assertions.assertSame(object2, sequence.get(0));
	}

	@Test
	public void testRemoveT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);

		// TODO: Backing list implementation returns a boolean to indicate
		// success.
		// This information is currently lost and this method will silently
		// fail.
		Assertions.assertFalse(sequence.remove(object2));
	}

	@Test
	public void testRemoveISegmentOfT() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");
		sequence.add(object1);
		sequence.add(object2);
		sequence.add(object3);
		sequence.add(object4);

		final List<ISequenceElement> segmentList = new LinkedList<>();
		segmentList.add(object2);
		segmentList.add(object3);
		final ISegment segment = new ListSegment(segmentList, sequence, 1, 3);

		sequence.remove(segment);

		Assertions.assertEquals(2, sequence.size());

		Assertions.assertSame(object1, sequence.get(0));
		Assertions.assertSame(object4, sequence.get(1));
	}

	@Disabled("Incomplete test")
	@Test
	public void testRemoveISegmentOfT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object4);

		final List<ISequenceElement> segmentList = new LinkedList<>();
		segmentList.add(object2);
		segmentList.add(object3);
		final ISegment segment = new ListSegment(segmentList, sequence, 1, 3);

		// TODO: Backing list implementation returns a boolean to indicate
		// success.
		// This information is currently lost and this method will silently
		// fail.
		sequence.remove(segment);

		Assertions.fail("Ambigous API: What about (non)consecutive elements? What about missing elements?");
	}

	@Test
	public void testRemoveIntInt() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0, 1);

		Assertions.assertEquals(1, sequence.size());
		Assertions.assertSame(object2, sequence.get(0));

	}

	@Test
	public void testRemoveIntInt_2() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.remove(-1, 1));
	}

	@Test
	public void testRemoveIntInt_3() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.remove(0, 3));
	}

	@Test
	public void testSet() {
		Assertions.assertEquals(0, sequence.size());
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		sequence.add(element);
		Assertions.assertEquals(1, sequence.size());
		Assertions.assertEquals(element, sequence.get(0));

		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.set(0, element2);

		Assertions.assertEquals(element2, sequence.get(0));
		Assertions.assertNotSame(element, sequence.get(0));

	}

	@Test
	public void testSet_2() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.set(1, object1));
	}

	@Test
	public void testSet_3() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.set(-1, object1));
	}

	@Test()
	public void testGet() {

		Assertions.assertEquals(0, sequence.size());
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		sequence.add(element);
		Assertions.assertEquals(1, sequence.size());
		Assertions.assertEquals(element, sequence.get(0));

	}

	@Test
	public void testGet_2() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.get(0));
	}

	@Test
	public void testGet_3() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.get(-1));
	}

	@Test
	public void testGetSegment() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

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

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.getSegment(-1, 2));
	}

	@Test
	public void testGetSegment_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence.getSegment(0, 3));
	}

	@Test
	public void testSize() {
		Assertions.assertEquals(0, sequence.size());

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");

		sequence.add(object1);

		Assertions.assertEquals(1, sequence.size());
	}

	@Test
	public void testReplaceAll() {

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		sequence.add(o1);
		sequence.add(o2);
		sequence.add(o3);

		Assertions.assertEquals(3, sequence.size());

		Assertions.assertSame(o1, sequence.get(0));
		Assertions.assertSame(o2, sequence.get(1));
		Assertions.assertSame(o3, sequence.get(2));

		final ListModifiableSequence sequence2 = new ListModifiableSequence(new ArrayList<ISequenceElement>(10));

		sequence2.add(o4);
		sequence2.add(o5);
		sequence2.add(o6);

		Assertions.assertEquals(3, sequence2.size());

		Assertions.assertSame(o4, sequence2.get(0));
		Assertions.assertSame(o5, sequence2.get(1));
		Assertions.assertSame(o6, sequence2.get(2));

		sequence.replaceAll(sequence2);

		Assertions.assertEquals(3, sequence.size());

		Assertions.assertSame(o4, sequence.get(0));
		Assertions.assertSame(o5, sequence.get(1));
		Assertions.assertSame(o6, sequence.get(2));
	}
}
