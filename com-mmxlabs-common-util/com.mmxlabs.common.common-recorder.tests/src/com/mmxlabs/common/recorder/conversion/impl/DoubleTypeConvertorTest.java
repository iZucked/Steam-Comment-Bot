/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.Assert;
import org.junit.Test;

public class DoubleTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();
		Assert.assertSame(Double.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		final String in = "1.0";

		final Double out = convertor.toObject(in);

		Assert.assertEquals(Double.valueOf(1), out);
	}

	@Test(expected = NumberFormatException.class)
	public void testToObject2() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		final String in = "string";

		convertor.toObject(in);
	}

	@Test(expected = NullPointerException.class)
	public void testToObject3() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		convertor.toObject(null);
	}

	@Test
	public void testToStringObject() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		final Double in = 1.0;

		final String out = convertor.toString(in);

		Assert.assertEquals("1.0", out);
	}

}
