/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();
		Assertions.assertSame(Integer.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		final String in = "1";

		final Integer out = convertor.toObject(in);

		Assertions.assertEquals(Integer.valueOf(1), out);
	}

	@Test
	public void testToObject2() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		final String in = "string";

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(in));
	}

	@Test
	public void testToObject3() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(null));
	}

	@Test
	public void testToStringObject() {
		final IntegerTypeConvertor convertor = new IntegerTypeConvertor();

		final Integer in = 1;

		final String out = convertor.toString(in);

		Assertions.assertEquals("1", out);
	}

}
