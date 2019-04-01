/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShortTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();
		Assertions.assertSame(Short.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		final String in = "1";

		final Short out = convertor.toObject(in);

		Assertions.assertEquals(Short.valueOf((short) 1), out);
	}

	@Test
	public void testToObject2() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		final String in = "string";

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(in));
	}

	@Test
	public void testToObject3() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(null));
	}

	@Test
	public void testToStringObject() {
		final ShortTypeConvertor convertor = new ShortTypeConvertor();

		final Short in = 1;

		final String out = convertor.toString(in);

		Assertions.assertEquals("1", out);
	}

}
