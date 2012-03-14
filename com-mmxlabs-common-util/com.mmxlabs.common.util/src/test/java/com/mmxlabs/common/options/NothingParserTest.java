package com.mmxlabs.common.options;

import org.junit.Assert;
import org.junit.Test;

public class NothingParserTest {

	@Test
	public void test() throws InvalidArgumentException {
		final NothingParser parser = new NothingParser();

		Assert.assertFalse(parser.hasDefaultValue());
		Assert.assertNull(parser.getDefaultValue());

		parser.parse(null, null);
	}
}
