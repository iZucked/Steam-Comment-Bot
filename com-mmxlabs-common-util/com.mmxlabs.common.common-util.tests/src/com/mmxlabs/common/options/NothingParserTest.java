/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import org.junit.Assert;
import org.junit.Test;

public class NothingParserTest {

	@Test
	public void test() throws InvalidArgumentException {
		final NothingParser parser = new NothingParser();

		Assert.assertTrue(parser.hasDefaultValue());
		Assert.assertEquals(false, parser.getDefaultValue());

		Assert.assertEquals(true, parser.parse(null, null));
	}
}
