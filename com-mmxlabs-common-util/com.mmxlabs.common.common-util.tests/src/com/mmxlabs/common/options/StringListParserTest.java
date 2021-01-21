/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringListParserTest {

	@Test
	public void testStringListParserStringString() {
		final String separator = "z";
		final String defaultValue = "x";
		final StringListParser parser = new StringListParser(separator, defaultValue);

		Assertions.assertTrue(parser.hasDefaultValue());

		final Object result = parser.getDefaultValue();
		Assertions.assertTrue(result instanceof List);
		final List<?> resultList = (List<?>) result;
		Assertions.assertEquals(1, resultList.size());
		Assertions.assertEquals(defaultValue, resultList.get(0));
	}

	@Test
	public void testStringListParserString() {
		final String separator = "z";
		final StringListParser parser = new StringListParser(separator);

		Assertions.assertFalse(parser.hasDefaultValue());
		Assertions.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {

		final String separator = "z";
		final StringListParser parser = new StringListParser(separator);

		final List<String> strings = new ArrayList<>(2);
		final String str1 = "abczdef ghi";
		strings.add(str1);

		final Object result = parser.parse(null, strings.iterator());
		Assertions.assertTrue(result instanceof List);
		final List<?> resultList = (List<?>) result;
		Assertions.assertEquals(2, resultList.size());
		Assertions.assertEquals("abc", resultList.get(0));
		Assertions.assertEquals("def ghi", resultList.get(1));

	}
}
