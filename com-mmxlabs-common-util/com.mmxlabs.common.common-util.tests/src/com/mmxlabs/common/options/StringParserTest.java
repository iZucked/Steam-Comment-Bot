/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringParserTest {

	@Test
	public void testStringParser() {
		final StringParser parser = new StringParser();
		Assert.assertFalse(parser.hasDefaultValue());
		Assert.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testStringParserString() {
		final String str = new String();

		final StringParser parser = new StringParser(str);
		Assert.assertTrue(parser.hasDefaultValue());
		Assert.assertSame(str, parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {
		final StringParser parser = new StringParser();

		final List<String> strings = new ArrayList<String>(2);
		final String str1 = new String();
		final String str2 = new String();
		strings.add(str1);
		strings.add(str2);

		Assert.assertSame(str1, parser.parse(null, strings.iterator()));
	}
}
