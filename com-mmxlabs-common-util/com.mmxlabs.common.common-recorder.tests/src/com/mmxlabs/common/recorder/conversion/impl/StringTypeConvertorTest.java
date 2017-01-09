/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import org.junit.Assert;
import org.junit.Test;

public class StringTypeConvertorTest {

	@Test
	public void testGetDataType() {

		final StringTypeConvertor convertor = new StringTypeConvertor();
		Assert.assertSame(String.class, convertor.getDataType());
	}

	@Test
	public void testToObject() {

		final StringTypeConvertor convertor = new StringTypeConvertor();

		final String str = "str";

		final String out = convertor.toObject(str);

		Assert.assertEquals(str, out);
	}

	@Test
	public void testToStringObject() {
		final StringTypeConvertor convertor = new StringTypeConvertor();

		final String str = "str";

		final String out = convertor.toString(str);

		Assert.assertEquals(str, out);
	}
}
