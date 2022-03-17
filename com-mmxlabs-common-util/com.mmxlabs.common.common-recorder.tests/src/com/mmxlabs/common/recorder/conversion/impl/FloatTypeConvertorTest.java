/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FloatTypeConvertorTest {

	@Test
	public void testGetDataType() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();
		Assertions.assertSame(Float.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		final String in = "1.0";

		final Float out = convertor.toObject(in);

		Assertions.assertEquals(Float.valueOf(1), out);
	}

	@Test
	public void testToObject2() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		final String in = "string";

		Assertions.assertThrows(NumberFormatException.class, () -> convertor.toObject(in));
	}

	@Test
	public void testToObject3() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		Assertions.assertThrows(NullPointerException.class, () -> convertor.toObject(null));
	}

	@Test
	public void testToStringObject() {
		final FloatTypeConvertor convertor = new FloatTypeConvertor();

		final Float in = 1.0f;

		final String out = convertor.toString(in);

		Assertions.assertEquals("1.0", out);
	}

}
