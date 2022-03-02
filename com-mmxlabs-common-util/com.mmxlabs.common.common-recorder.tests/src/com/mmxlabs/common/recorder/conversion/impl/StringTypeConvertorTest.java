/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringTypeConvertorTest {

	@Test
	public void testGetDataType() {

		final StringTypeConvertor convertor = new StringTypeConvertor();
		Assertions.assertSame(String.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {

		final StringTypeConvertor convertor = new StringTypeConvertor();

		final String str = "str";

		final String out = convertor.toObject(str);

		Assertions.assertEquals(str, out);
	}

	@Test
	public void testToStringObject() {
		final StringTypeConvertor convertor = new StringTypeConvertor();

		final String str = "str";

		final String out = convertor.toString(str);

		Assertions.assertEquals(str, out);
	}
}
