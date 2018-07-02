/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DateTreeSetComparatorTest {

	@Test
	public void testDateTreeSetComparator() {

		final long time = System.currentTimeMillis();
		final long timePlusADay = time + TimeUnit.DAYS.toMillis(1);

		// Get the current time as a date
		final Date d1 = new Date(time);
		// Get a different time (current time + a day) as a date.
		final Date d2 = new Date(timePlusADay);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final Map<Object, Date> datemap = new HashMap<>();

		datemap.put(obj1, d1);
		datemap.put(obj2, d2);

		final ITransformer<Object, Date> transformer = datemap::get;

		final DateTreeSetComparator<Object> cmp = new DateTreeSetComparator<>(transformer);

		//
		Assertions.assertEquals(cmp.compare(d1, d2), cmp.compare(obj1, obj2));
		Assertions.assertEquals(cmp.compare(d1, obj2), cmp.compare(obj1, d2));
		Assertions.assertEquals(cmp.compare(d2, d1), cmp.compare(obj2, obj1));
		Assertions.assertEquals(cmp.compare(d2, obj1), cmp.compare(obj2, d1));

		//
		Assertions.assertEquals(cmp.compare(d1, d2), -cmp.compare(d2, d1));
		Assertions.assertEquals(cmp.compare(d1, obj1), -cmp.compare(obj1, d1));
		Assertions.assertEquals(cmp.compare(d1, obj2), -cmp.compare(obj2, d1));
		// ....

		// Test equality
		Assertions.assertEquals(0, cmp.compare(d1, d1));
		Assertions.assertEquals(0, cmp.compare(d2, d2));
		Assertions.assertEquals(0, cmp.compare(obj1, obj1));
		Assertions.assertEquals(0, cmp.compare(obj2, obj2));
	}

	@Disabled("See code review 165 https://mmxlabs.fogbugz.com/default.asp?165")
	@Test
	public void testDateTreeSetComparatorCompareDateFromObject() {

		final long time = System.currentTimeMillis();
		final long timePlusADay = time + TimeUnit.DAYS.toMillis(1);

		// Get the current time as a date
		final Date d1 = new Date(time);
		// Get a different time (current time + a day) as a date.
		final Date d2 = new Date(timePlusADay);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final Map<Object, Date> datemap = new HashMap<>();

		datemap.put(obj1, d1);
		datemap.put(obj2, d2);

		final ITransformer<Object, Date> transformer = t -> datemap.get(t);

		final DateTreeSetComparator<Object> cmp = new DateTreeSetComparator<>(transformer);

		// Check obj1 retrieves d1, therefore obj1 equals d1
		Assertions.assertEquals(0, cmp.compare(d1, obj1));
		Assertions.assertEquals(0, cmp.compare(d2, obj2));
	}
}
