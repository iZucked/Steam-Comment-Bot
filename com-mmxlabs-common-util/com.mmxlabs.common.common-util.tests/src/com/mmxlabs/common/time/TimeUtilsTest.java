/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.NonNullPair;

public class TimeUtilsTest {

	@Test
	public void testEarliest() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 2, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 1, 1, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime e = ZonedDateTime.of(2015, 11, 1, 0, 1, 0, 0, ZoneId.of("UTC"));

		// All equal? return first arg
		Assert.assertEquals(a, TimeUtils.earliest(a, b));
		Assert.assertEquals(b, TimeUtils.earliest(b, a));

		// Proper early checks
		Assert.assertEquals(a, TimeUtils.earliest(a, c));
		Assert.assertEquals(a, TimeUtils.earliest(c, a));

		Assert.assertEquals(e, TimeUtils.earliest(d, e));
		Assert.assertEquals(e, TimeUtils.earliest(e, d));
	}

	@Test
	public void testLatest() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 2, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 1, 1, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime e = ZonedDateTime.of(2015, 11, 1, 0, 1, 0, 0, ZoneId.of("UTC"));

		// All equal? return first arg
		Assert.assertEquals(a, TimeUtils.latest(a, b));
		Assert.assertEquals(b, TimeUtils.latest(b, a));

		// Proper early checks
		Assert.assertEquals(c, TimeUtils.latest(a, c));
		Assert.assertEquals(c, TimeUtils.latest(c, a));

		Assert.assertEquals(d, TimeUtils.latest(d, e));
		Assert.assertEquals(d, TimeUtils.latest(e, d));
	}

	@Test
	public void testOverlaps1() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 2, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 11, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		Assert.assertTrue(TimeUtils.overlaps(ia, ib));
		Assert.assertTrue(TimeUtils.overlaps(ib, ia));

	}

	@Test
	public void testOverlaps2() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 11, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 20, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		Assert.assertFalse(TimeUtils.overlaps(ia, ib));
		Assert.assertFalse(TimeUtils.overlaps(ib, ia));

	}

	@Test
	public void testOverlaps3() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 11, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		Assert.assertFalse(TimeUtils.overlaps(ia, ib));
		Assert.assertFalse(TimeUtils.overlaps(ib, ia));
	}

	@Test
	public void testOverlaps4() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		Assert.assertTrue(TimeUtils.overlaps(ia, ib));
		Assert.assertTrue(TimeUtils.overlaps(ib, ia));
	}

	@Test
	public void testOverlaps5() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 30, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 2, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		Assert.assertTrue(TimeUtils.overlaps(ia, ib));
		Assert.assertTrue(TimeUtils.overlaps(ib, ia));
	}

	@Test
	public void testIntersect1() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 2, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 11, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		final NonNullPair<ZonedDateTime, ZonedDateTime> intersection = TimeUtils.intersect(ia, ib);
		Assert.assertEquals(c, intersection.getFirst());
		Assert.assertEquals(b, intersection.getSecond());
	}

	@Test
	public void testIntersect2() {

		final ZonedDateTime a = ZonedDateTime.of(2015, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime b = ZonedDateTime.of(2015, 11, 4, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime c = ZonedDateTime.of(2015, 11, 5, 0, 0, 0, 0, ZoneId.of("UTC"));
		final ZonedDateTime d = ZonedDateTime.of(2015, 11, 11, 0, 0, 0, 0, ZoneId.of("UTC"));

		final NonNullPair<ZonedDateTime, ZonedDateTime> ia = new NonNullPair<>(a, b);
		final NonNullPair<ZonedDateTime, ZonedDateTime> ib = new NonNullPair<>(c, d);

		final NonNullPair<ZonedDateTime, ZonedDateTime> intersection = TimeUtils.intersect(ia, ib);
		Assert.assertEquals(c, intersection.getFirst());
		Assert.assertEquals(b, intersection.getSecond());
	}

}
