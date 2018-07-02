/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NothingParserTest {

	@Test
	public void test() throws InvalidArgumentException {
		final NothingParser parser = new NothingParser();

		Assertions.assertTrue(parser.hasDefaultValue());
		Assertions.assertEquals(false, parser.getDefaultValue());

		Assertions.assertEquals(true, parser.parse(null, null));
	}
}
