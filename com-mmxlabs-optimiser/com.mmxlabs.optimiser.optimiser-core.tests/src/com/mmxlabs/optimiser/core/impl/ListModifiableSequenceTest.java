/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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

	@Before
	public void setUp() throws Exception {
		sequence = new ListModifiableSequence(new ArrayList<ISequenceElement>(10));
	}

	@After
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
		Assert.assertTrue(iterator.hasNext());
		final Object iterObject1 = iterator.next();
		Assert.assertSame(object1, iterObject1);

		Assert.assertTrue(iterator.hasNext());
		final Object iterObject2 = iterator.next();
		Assert.assertSame(object2, iterObject2);

		iterator.remove();

		Assert.assertEquals(1, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
	}

	@Test
	public void testAdd() {

		Assert.assertEquals(0, sequence.size());
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");
		sequence.add(element);
		Assert.assertEquals(1, sequence.size());
		Assert.assertEquals(element, sequence.get(0));
	}

	@Test
	public void testInsertIntT() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");

		sequence.add(object1);
		sequence.add(object2);

		sequence.insert(1, object3);

		Assert.assertEquals(3, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object3, sequence.get(1));
		Assert.assertSame(object2, sequence.get(2));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertIntT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");

		sequence.add(object1);
		sequence.add(object2);

		sequence.insert(-1, object3);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertIntT_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");

		sequence.add(object1);
		sequence.add(object2);

		sequence.insert(3, object3);
	}

	@Test
	public void testInsertIntISegmentOfT() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object2);

		final List<ISequenceElement> segmentList = new LinkedList<ISequenceElement>();
		segmentList.add(object3);
		segmentList.add(object4);
		final ISegment segment = new ListSegment(segmentList, sequence, 0, 1);

		sequence.insert(1, segment);

		Assert.assertEquals(4, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object3, sequence.get(1));
		Assert.assertSame(object4, sequence.get(2));
		Assert.assertSame(object2, sequence.get(3));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertIntISegmentOfT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object2);

		final List<ISequenceElement> segmentList = new LinkedList<ISequenceElement>();
		segmentList.add(object3);
		segmentList.add(object4);
		final ISegment segment = new ListSegment(segmentList, sequence, 0, 1);

		sequence.insert(-1, segment);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertIntISegmentOfT_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object2);

		final List<ISequenceElement> segmentList = new LinkedList<ISequenceElement>();
		segmentList.add(object3);
		segmentList.add(object4);
		final ISegment segment = new ListSegment(segmentList, sequence, 0, 1);

		sequence.insert(3, segment);
	}

	@Test
	public void testRemoveInt() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0);

		Assert.assertEquals(1, sequence.size());
		Assert.assertSame(object2, sequence.get(0));
	}

	@Test
	public void testRemoveT() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		Assert.assertTrue(sequence.remove(object1));

		Assert.assertEquals(1, sequence.size());
		Assert.assertSame(object2, sequence.get(0));
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
		Assert.assertFalse(sequence.remove(object2));
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

		final List<ISequenceElement> segmentList = new LinkedList<ISequenceElement>();
		segmentList.add(object2);
		segmentList.add(object3);
		final ISegment segment = new ListSegment(segmentList, sequence, 1, 3);

		sequence.remove(segment);

		Assert.assertEquals(2, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object4, sequence.get(1));
	}

	@Ignore("Incomplete test")
	@Test
	public void testRemoveISegmentOfT_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final ISequenceElement object3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement object4 = Mockito.mock(ISequenceElement.class, "4");

		sequence.add(object1);
		sequence.add(object4);

		final List<ISequenceElement> segmentList = new LinkedList<ISequenceElement>();
		segmentList.add(object2);
		segmentList.add(object3);
		final ISegment segment = new ListSegment(segmentList, sequence, 1, 3);

		// TODO: Backing list implementation returns a boolean to indicate
		// success.
		// This information is currently lost and this method will silently
		// fail.
		sequence.remove(segment);

		fail("Ambigous API: What about (non)consecutive elements? What about missing elements?");
	}

	@Test
	public void testRemoveIntInt() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0, 1);

		Assert.assertEquals(1, sequence.size());
		Assert.assertSame(object2, sequence.get(0));

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveIntInt_2() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(-1, 1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveIntInt_3() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0, 3);
	}

	@Test
	public void testSet() {
		Assert.assertEquals(0, sequence.size());
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		sequence.add(element);
		Assert.assertEquals(1, sequence.size());
		Assert.assertEquals(element, sequence.get(0));

		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.set(0, element2);

		Assert.assertEquals(element2, sequence.get(0));
		Assert.assertNotSame(element, sequence.get(0));

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSet_2() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");

		sequence.set(1, object1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSet_3() {
		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");

		sequence.set(-1, object1);
	}

	@Test()
	public void testGet() {

		Assert.assertEquals(0, sequence.size());
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		sequence.add(element);
		Assert.assertEquals(1, sequence.size());
		Assert.assertEquals(element, sequence.get(0));

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_2() {
		sequence.get(0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_3() {
		sequence.get(-1);
	}

	@Test
	public void testGetSegment() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		final ISegment segment = sequence.getSegment(0, 2);

		Assert.assertEquals(2, segment.size());

		Assert.assertEquals(0, segment.getSequenceStart());
		Assert.assertEquals(2, segment.getSequenceEnd());

		Assert.assertSame(sequence, segment.getSequence());

		Assert.assertSame(object1, segment.get(0));
		Assert.assertSame(object2, segment.get(1));

		final Iterator<ISequenceElement> iterator = segment.iterator();

		Assert.assertTrue(iterator.hasNext());
		final Object iterObject1 = iterator.next();
		Assert.assertSame(object1, iterObject1);

		Assert.assertTrue(iterator.hasNext());
		final Object iterObject2 = iterator.next();
		Assert.assertSame(object2, iterObject2);

		iterator.remove();

		Assert.assertEquals(1, segment.size());
		Assert.assertEquals(2, sequence.size());

		Assert.assertSame(object1, segment.get(0));

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object2, sequence.get(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetSegment_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.getSegment(-1, 2);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetSegment_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		sequence.add(object1);
		sequence.add(object2);

		sequence.getSegment(0, 3);
	}

	@Test
	public void testSize() {
		Assert.assertEquals(0, sequence.size());

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");

		sequence.add(object1);

		Assert.assertEquals(1, sequence.size());
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

		Assert.assertEquals(3, sequence.size());

		Assert.assertSame(o1, sequence.get(0));
		Assert.assertSame(o2, sequence.get(1));
		Assert.assertSame(o3, sequence.get(2));

		final ListModifiableSequence sequence2 = new ListModifiableSequence(new ArrayList<ISequenceElement>(10));

		sequence2.add(o4);
		sequence2.add(o5);
		sequence2.add(o6);

		Assert.assertEquals(3, sequence2.size());

		Assert.assertSame(o4, sequence2.get(0));
		Assert.assertSame(o5, sequence2.get(1));
		Assert.assertSame(o6, sequence2.get(2));

		sequence.replaceAll(sequence2);

		Assert.assertEquals(3, sequence.size());

		Assert.assertSame(o4, sequence.get(0));
		Assert.assertSame(o5, sequence.get(1));
		Assert.assertSame(o6, sequence.get(2));
	}
}
