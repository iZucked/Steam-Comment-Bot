/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringParserTest {

	@Test
	public void testStringParser() {
		final StringParser parser = new StringParser();
		Assertions.assertFalse(parser.hasDefaultValue());
		Assertions.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testStringParserString() {
		final String str = new String();

		final StringParser parser = new StringParser(str);
		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertSame(str, parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {
		final StringParser parser = new StringParser();

		final List<String> strings = new ArrayList<>(2);
		final String str1 = new String();
		final String str2 = new String();
		strings.add(str1);
		strings.add(str2);

		Assertions.assertSame(str1, parser.parse(null, strings.iterator()));
	}
}
