package com.acme.optimiser.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acme.optimiser.ISegment;

public class ListModifiableSequenceTest {

	private ListModifiableSequence<Object> sequence;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sequence = new ListModifiableSequence<Object>(new ArrayList<Object>(10));
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test sequence iterator method
	 */
	@Test
	public void testIterator() {

		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		Iterator<Object> iterator = sequence.iterator();
		Assert.assertTrue(iterator.hasNext());
		Object iterObject1 = iterator.next();
		Assert.assertSame(object1, iterObject1);

		Assert.assertTrue(iterator.hasNext());
		Object iterObject2 = iterator.next();
		Assert.assertSame(object2, iterObject2);

		iterator.remove();

		Assert.assertEquals(1, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
	}

	@Test
	public void testAdd() {

		Assert.assertEquals(0, sequence.size());
		Object element = new Object();
		sequence.add(element);
		Assert.assertEquals(1, sequence.size());
		Assert.assertEquals(element, sequence.get(0));
	}

	@Test
	public void testInsertIntT() {

		Object object1 = new Object();
		Object object2 = new Object();

		Object object3 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		sequence.insert(1, object3);

		Assert.assertEquals(3, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object3, sequence.get(1));
		Assert.assertSame(object2, sequence.get(2));
	}

	@Test
	public void testInsertIntISegmentOfT() {
		Object object1 = new Object();
		Object object2 = new Object();

		Object object3 = new Object();
		Object object4 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		List<Object> segmentList = new LinkedList<Object>();
		segmentList.add(object3);
		segmentList.add(object4);
		ISegment<Object> segment = new ListSegment<Object>(
				segmentList, sequence, 0, 1);
		
		sequence.insert(1, segment);

		Assert.assertEquals(4, sequence.size());

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object3, sequence.get(1));
		Assert.assertSame(object4, sequence.get(2));
		Assert.assertSame(object2, sequence.get(3));
	}

	@Test
	public void testRemoveInt() {

		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0);

		Assert.assertEquals(1, sequence.size());
		Assert.assertSame(object2, sequence.get(0));
	}

	@Test
	public void testRemoveT() {
		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(object1);

		Assert.assertEquals(1, sequence.size());
		Assert.assertSame(object2, sequence.get(0));
	}

	@Test
	public void testRemoveISegmentOfT() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveIntInt() {
		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		sequence.remove(0, 1);

		Assert.assertEquals(1, sequence.size());
		Assert.assertSame(object2, sequence.get(0));

	}

	@Test
	public void testSet() {
		Assert.assertEquals(0, sequence.size());
		Object element = new Object();
		sequence.add(element);
		Assert.assertEquals(1, sequence.size());
		Assert.assertEquals(element, sequence.get(0));

		Object element2 = new Object();
		sequence.set(0, element2);

		Assert.assertEquals(element2, sequence.get(0));
		Assert.assertNotSame(element, sequence.get(0));

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSet_2() {
		sequence.set(1, new Object());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSet_3() {
		sequence.set(-1, new Object());
	}

	@Test()
	public void testGet() {

		Assert.assertEquals(0, sequence.size());
		Object element = new Object();
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

		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		ISegment<Object> segment = sequence.getSegment(0, 2);

		Assert.assertEquals(2, segment.size());

		Assert.assertEquals(0, segment.getSequenceStart());
		Assert.assertEquals(2, segment.getSequenceEnd());

		Assert.assertSame(sequence, segment.getSequence());

		Assert.assertSame(object1, segment.get(0));
		Assert.assertSame(object2, segment.get(1));

		Iterator<Object> iterator = segment.iterator();

		Assert.assertTrue(iterator.hasNext());
		Object iterObject1 = iterator.next();
		Assert.assertSame(object1, iterObject1);

		Assert.assertTrue(iterator.hasNext());
		Object iterObject2 = iterator.next();
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

		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		ISegment<Object> segment = sequence.getSegment(-1, 2);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetSegment_3() {

		Object object1 = new Object();
		Object object2 = new Object();

		sequence.add(object1);
		sequence.add(object2);

		ISegment<Object> segment = sequence.getSegment(0, 3);
	}

	@Test
	public void testSize() {
		Assert.assertEquals(0, sequence.size());

		sequence.add(new Object());

		Assert.assertEquals(1, sequence.size());

	}

}
