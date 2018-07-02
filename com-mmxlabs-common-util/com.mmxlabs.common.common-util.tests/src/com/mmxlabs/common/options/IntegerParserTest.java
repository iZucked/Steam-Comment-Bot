/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerParserTest {

	@Test
	public void testIntegerParserInteger() {
		final Integer i = new Integer(0);
		final IntegerParser parser = new IntegerParser(i);
		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertSame(i, parser.getDefaultValue());
	}

	@Test
	public void testIntegerParserString() {
		final String str = "0";
		final IntegerParser parser = new IntegerParser(str);
		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertEquals(Integer.valueOf(0), parser.getDefaultValue());
	}

	@Test
	public void testIntegerParser() {

		final IntegerParser parser = new IntegerParser();
		Assertions.assertFalse(parser.hasDefaultValue());
		Assertions.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {

		final IntegerParser parser = new IntegerParser();

		final List<String> strings = new ArrayList<>(2);
		final String str1 = "0";
		final String str2 = "string";
		strings.add(str1);
		strings.add(str2);

		Assertions.assertEquals(Integer.valueOf(0), parser.parse(null, strings.iterator()));
	}

}
