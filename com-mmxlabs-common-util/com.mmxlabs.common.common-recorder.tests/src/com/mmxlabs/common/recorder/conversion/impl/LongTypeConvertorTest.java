/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.Assert;
import org.junit.Test;

public class LongTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final LongTypeConvertor convertor = new LongTypeConvertor();
		Assert.assertSame(Long.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		final String in = "1";

		final Long out = convertor.toObject(in);

		Assert.assertEquals(Long.valueOf(1), out);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject2() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		final String in = "string";

		convertor.toObject(in);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject3() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		convertor.toObject(null);
	}

	@Test
	public void testToStringObject() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		final Long in = 1l;

		final String out = convertor.toString(in);

		Assert.assertEquals("1", out);
	}

}
