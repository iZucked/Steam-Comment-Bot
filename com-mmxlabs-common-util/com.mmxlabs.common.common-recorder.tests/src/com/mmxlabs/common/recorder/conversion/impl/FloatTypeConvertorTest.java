/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.Assert;
import org.junit.Test;

public class FloatTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();
		Assert.assertSame(Float.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		final String in = "1.0";

		final Float out = convertor.toObject(in);

		Assert.assertEquals(Float.valueOf(1), out);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject2() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		final String in = "string";

		convertor.toObject(in);
	}

	@Test(expected = NullPointerException.class)
	public void testToObject3() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		convertor.toObject(null);
	}

	@Test
	public void testToStringObject() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		final Float in = 1.0f;

		final String out = convertor.toString(in);

		Assert.assertEquals("1.0", out);
	}

}
