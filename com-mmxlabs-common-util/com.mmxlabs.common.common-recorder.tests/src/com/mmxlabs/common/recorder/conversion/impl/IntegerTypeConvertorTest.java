/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.Assert;
import org.junit.Test;

public class IntegerTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();
		Assert.assertSame(Integer.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		final String in = "1";

		final Integer out = convertor.toObject(in);

		Assert.assertEquals(Integer.valueOf(1), out);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject2() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		final String in = "string";

		convertor.toObject(in);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject3() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		convertor.toObject(null);
	}

	@Test
	public void testToStringObject() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		final Integer in = 1;

		final String out = convertor.toString(in);

		Assert.assertEquals("1", out);
	}

}
