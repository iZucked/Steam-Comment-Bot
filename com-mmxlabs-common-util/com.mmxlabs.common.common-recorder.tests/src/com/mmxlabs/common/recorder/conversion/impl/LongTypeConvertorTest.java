/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final LongTypeConvertor convertor = new LongTypeConvertor();
		Assertions.assertSame(Long.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		final String in = "1";

		final Long out = convertor.toObject(in);

		Assertions.assertEquals(Long.valueOf(1), out);
	}

	@Test
	public void testToObject2() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		final String in = "string";

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(in));
	}

	@Test
	public void testToObject3() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(null));
	}

	@Test
	public void testToStringObject() {
		final LongTypeConvertor convertor = new LongTypeConvertor();

		final Long in = 1l;

		final String out = convertor.toString(in);

		Assertions.assertEquals("1", out);
	}

}
