package com.mmxlabs.common.recorder.conversion.impl;

import junit.framework.Assert;

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
