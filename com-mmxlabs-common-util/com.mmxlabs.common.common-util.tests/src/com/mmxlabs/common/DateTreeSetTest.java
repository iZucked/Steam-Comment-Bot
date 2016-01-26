/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.common.impl.MappingTransformer;

@Ignore("Skip until API is sorted out. See Case 188")
public class DateTreeSetTest {

	@Test
	public void testDateTreeSet() {

		final Object obj1 = "First";
		final Object obj2 = "Second";
		final Object obj3 = "Third";

		final Date date1 = createDate(2011, 01, 01);
		final Date date2 = createDate(2011, 01, 01);
		final Date date3 = createDate(2011, 01, 02);

		final Map<Object, Date> map = CollectionsUtil.makeHashMap(obj1, date1, obj2, date2, obj3, date3);

		final ITransformer<Object, Date> transformer = new MappingTransformer<Object, Date>(map);

		final DateTreeSet<Object> dts = new DateTreeSet<Object>(transformer);

		dts.add(obj1);

		Assert.assertTrue(dts.contains(obj1));
		Assert.assertTrue(dts.contains(date1));

		Assert.assertFalse(dts.contains(obj2));
		Assert.assertTrue(dts.contains(date2));

		Assert.assertFalse(dts.contains(obj3));
		Assert.assertFalse(dts.contains(date3));

		Assert.assertEquals(1, dts.size());

		fail("Not yet implemented");
	}

	@Test
	public void testCeilingT() {
		fail("Not yet implemented");
	}

	@Test
	public void testCeilingDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDescendingIterator() {
		fail("Not yet implemented");
	}

	@Test
	public void testDescendingSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testFloorT() {
		fail("Not yet implemented");
	}

	@Test
	public void testFloorDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testHeadSetT() {
		fail("Not yet implemented");
	}

	@Test
	public void testHeadSetDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testHeadSetTBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testHeadSetDateBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testHigherT() {
		fail("Not yet implemented");
	}

	@Test
	public void testHigherDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testIterator() {
		fail("Not yet implemented");
	}

	@Test
	public void testLowerT() {
		fail("Not yet implemented");
	}

	@Test
	public void testLowerDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testPollFirst() {
		fail("Not yet implemented");
	}

	@Test
	public void testPollLast() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubSetTT() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubSetDateDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubSetTBooleanTBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubSetDateBooleanDateBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testTailSetT() {
		fail("Not yet implemented");
	}

	@Test
	public void testTailSetDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testTailSetTBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testTailSetDateBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testComparator() {
		fail("Not yet implemented");
	}

	@Test
	public void testFirst() {
		fail("Not yet implemented");
	}

	@Test
	public void testLast() {
		fail("Not yet implemented");
	}

	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		fail("Not yet implemented");
	}

	@Test
	public void testContains() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEmpty() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetainAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testToArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testToArrayUArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testClone() {
		fail("Not yet implemented");
	}

	private Date createDate(final int year, final int month, final int day) {

		final Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}
}
