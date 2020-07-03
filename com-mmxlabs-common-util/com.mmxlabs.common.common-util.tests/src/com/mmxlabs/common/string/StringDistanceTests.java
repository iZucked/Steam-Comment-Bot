/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.strings.StringDistance;

public class StringDistanceTests {

	@Test
	public void testStringDistance() {
		Assertions.assertEquals(3, StringDistance.defaultInstance().distance("relevant", "elephant"));
		Assertions.assertEquals(2, StringDistance.defaultInstance().distance("a cat", "a abct"));
	}

}
