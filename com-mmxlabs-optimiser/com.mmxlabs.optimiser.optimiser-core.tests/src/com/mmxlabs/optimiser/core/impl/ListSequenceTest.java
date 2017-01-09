/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
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

		Assert.assertSame(object1, sequence.get(0));
		Assert.assertSame(object2, sequence.get(1));

		final ListSequence sequence2 = new ListSequence(sequence);
		Assert.assertSame(object1, sequence2.get(0));
		Assert.assertSame(object2, sequence2.get(1));
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
		Assert.assertTrue(iterator.hasNext());
		final ISequenceElement iterObject1 = iterator.next();
		Assert.assertSame(object1, iterObject1);

		Assert.assertTrue(iterator.hasNext());
		final ISequenceElement iterObject2 = iterator.next();
		Assert.assertSame(object2, iterObject2);
	}

	/**
	 * Test sequence iterator remove method
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testIterator_2() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		final Iterator<ISequenceElement> iterator = sequence.iterator();
		Assert.assertTrue(iterator.hasNext());
		final Object iterObject1 = iterator.next();
		Assert.assertSame(object1, iterObject1);

		// Expect to fail here
		iterator.remove();
	}

	@Test()
	public void testGet() {

		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(element);
		final ListSequence sequence = new ListSequence(l);

		Assert.assertEquals(1, sequence.size());
		Assert.assertEquals(element, sequence.get(0));

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_2() {
		final List<@NonNull ISequenceElement> emptyList = Collections.emptyList();
		final ListSequence sequence = new ListSequence(emptyList);

		sequence.get(0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_3() {
		final List<@NonNull ISequenceElement> emptyList = Collections.emptyList();
		final ListSequence sequence = new ListSequence(emptyList);

		sequence.get(-1);
	}

	@Test
	public void testGetSegment() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

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

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		sequence.getSegment(-1, 2);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetSegment_3() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		sequence.getSegment(0, 3);
	}

	@Test
	public void testSize() {
		final ISequenceElement element = Mockito.mock(ISequenceElement.class, "1");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(element);
		final ListSequence sequence = new ListSequence(l);

		Assert.assertEquals(1, sequence.size());
	}

	@Test
	public void testToString() {
		final List<@NonNull ISequenceElement> l = Collections.emptyList();
		final ListSequence sequence = new ListSequence(l);
		Assert.assertNotNull(sequence.toString());
	}

	@Test
	public void testFirst1() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assert.assertSame(object1, sequence.first());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testFirst2() {

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList();
		final ListSequence sequence = new ListSequence(l);

		sequence.first();
	}

	@Test
	public void testLast1() {

		final ISequenceElement object1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement object2 = Mockito.mock(ISequenceElement.class, "2");

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList(object1, object2);
		final ListSequence sequence = new ListSequence(l);

		Assert.assertSame(object2, sequence.last());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testLast2() {

		final List<@NonNull ISequenceElement> l = CollectionsUtil.makeArrayList();
		final ListSequence sequence = new ListSequence(l);

		sequence.last();
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

		Assert.assertTrue(sequence0.equals(sequence0));
		Assert.assertTrue(sequence0.equals(sequence1));
		Assert.assertTrue(sequence0.equals(sequence2));
		Assert.assertFalse(sequence0.equals(sequence3));
		Assert.assertFalse(sequence0.equals(sequence4));

		Assert.assertTrue(sequence0.equals(sequence0));
		Assert.assertTrue(sequence1.equals(sequence0));
		Assert.assertTrue(sequence2.equals(sequence0));
		Assert.assertFalse(sequence3.equals(sequence0));
		Assert.assertFalse(sequence4.equals(sequence0));
	}

}
