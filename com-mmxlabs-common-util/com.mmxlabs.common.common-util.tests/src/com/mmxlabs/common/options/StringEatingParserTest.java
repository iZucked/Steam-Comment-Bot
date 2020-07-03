/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringEatingParserTest {

	@Test
	public void testStringEatingParserString() {
		final String defaultValue = new String();
		final StringEatingParser parser = new StringEatingParser(defaultValue);

		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertSame(defaultValue, parser.getDefaultValue());
	}

	@Test
	public void testStringEatingParser() {
		final StringEatingParser parser = new StringEatingParser();

		Assertions.assertFalse(parser.hasDefaultValue());
		Assertions.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {
		final StringEatingParser parser = new StringEatingParser();

		final List<String> strings = new ArrayList<>(2);
		final String str1 = "abc";
		final String str2 = "def";
		strings.add(str1);
		strings.add(str2);

		Assertions.assertEquals("abc def ", parser.parse(null, strings.iterator()));
	}

}
