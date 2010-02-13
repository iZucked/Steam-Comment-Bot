package com.acme.optimiser.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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


	@Test
	public void testIterator() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testInsertIntISegmentOfT() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveT() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveISegmentOfT() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveIntInt() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		Assert.assertEquals(0, sequence.size());

		sequence.add(new Object());

		Assert.assertEquals(1, sequence.size());

	}

}
