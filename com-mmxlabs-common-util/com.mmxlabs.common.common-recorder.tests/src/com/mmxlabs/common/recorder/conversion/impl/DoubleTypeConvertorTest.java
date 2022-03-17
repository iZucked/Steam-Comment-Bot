/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DoubleTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();
		Assertions.assertSame(Double.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		final String in = "1.0";

		final Double out = convertor.toObject(in);

		Assertions.assertEquals(Double.valueOf(1), out);
	}

	@Test
	public void testToObject2() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		final String in = "string";

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(in));
	}

	@Test
	public void testToObject3() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		Assertions.assertThrows(NullPointerException.class, () -> convertor.toObject(null));
	}

	@Test
	public void testToStringObject() {
		final DoubleTypeConvertor convertor = new DoubleTypeConvertor();

		final Double in = 1.0;

		final String out = convertor.toString(in);

		Assertions.assertEquals("1.0", out);
	}

}
