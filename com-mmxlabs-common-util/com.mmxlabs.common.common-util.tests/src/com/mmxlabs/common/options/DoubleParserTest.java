/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DoubleParserTest {

	@Test
	public void testDoubleParserDouble() {
		final double i = 0.0;
		final DoubleParser parser = new DoubleParser(i);
		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertEquals(i, parser.getDefaultValue());
	}

	@Test
	public void testDoubleParserString() {
		final String str = "0";
		final DoubleParser parser = new DoubleParser(str);
		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertEquals(Double.valueOf(0), parser.getDefaultValue());
	}

	@Test
	public void testDoubleParser() {

		final DoubleParser parser = new DoubleParser();
		Assertions.assertFalse(parser.hasDefaultValue());
		Assertions.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {

		final DoubleParser parser = new DoubleParser();

		final List<String> strings = new ArrayList<>(2);
		final String str1 = "0.0";
		final String str2 = "string";
		strings.add(str1);
		strings.add(str2);

		Assertions.assertEquals(Double.valueOf(0.0), parser.parse(null, strings.iterator()));
	}

}
