/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringEatingParserTest {

	@Test
	public void testStringEatingParserString() {
		final String defaultValue = new String();
		final StringEatingParser parser = new StringEatingParser(defaultValue);

		Assert.assertTrue(parser.hasDefaultValue());
		Assert.assertSame(defaultValue, parser.getDefaultValue());
	}

	@Test
	public void testStringEatingParser() {
		final StringEatingParser parser = new StringEatingParser();

		Assert.assertFalse(parser.hasDefaultValue());
		Assert.assertNull(parser.getDefaultValue());
	}

	@Test
	public void testParse() throws InvalidArgumentException {
		final StringEatingParser parser = new StringEatingParser();

		final List<String> strings = new ArrayList<String>(2);
		final String str1 = "abc";
		final String str2 = "def";
		strings.add(str1);
		strings.add(str2);

		Assert.assertEquals("abc def ", parser.parse(null, strings.iterator()));
	}

}
