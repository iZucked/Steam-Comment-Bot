/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringListParserTest {

	@Test
	public void testStringListParserStringString() {
		final String separator = "z";
		final String defaultValue = "x";
		final StringListParser parser = new StringListParser(separator, defaultValue);

		Assert.assertTrue(parser.hasDefaultValue());

		final Object result = parser.getDefaultValue();
		Assert.assertTrue(result instanceof List);
		final List<?> resultList = (List<?>) result;
		Assert.assertEquals(1, resultList.size());
		Assert.assertEquals(defaultValue, resultList.get(0));
	}

	@Test
	public void testStringListParserString() {
		final String separator = "z";
		final StringListParser parser = new StringListParser(separator);

		Assert.assertFalse(parser.hasDefaultValue());
		Assert.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {

		final String separator = "z";
		final StringListParser parser = new StringListParser(separator);

		final List<String> strings = new ArrayList<String>(2);
		final String str1 = "abczdef ghi";
		strings.add(str1);

		final Object result = parser.parse(null, strings.iterator());
		Assert.assertTrue(result instanceof List);
		final List<?> resultList = (List<?>) result;
		Assert.assertEquals(2, resultList.size());
		Assert.assertEquals("abc", resultList.get(0));
		Assert.assertEquals("def ghi", resultList.get(1));

	}
}
