/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.Assert;
import org.junit.Test;

public class ShortTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();
		Assert.assertSame(Short.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		final String in = "1";

		final Short out = convertor.toObject(in);

		Assert.assertEquals(Short.valueOf((short) 1), out);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject2() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		final String in = "string";

		convertor.toObject(in);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject3() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		convertor.toObject(null);
	}

	@Test
	public void testToStringObject() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		final Short in = 1;

		final String out = convertor.toString(in);

		Assert.assertEquals("1", out);
	}

}
