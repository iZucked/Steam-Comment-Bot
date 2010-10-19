/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

	@Test
	public void testIsEqual() {

		final Object a = new Object();
		final Object b = new Object();

		Assert.assertTrue(Equality.isEqual(null, null));
		Assert.assertFalse(Equality.isEqual(a, null));
		Assert.assertFalse(Equality.isEqual(null, a));
		Assert.assertFalse(Equality.isEqual(a, b));

		Assert.assertTrue(Equality.isEqual(a, a));
		Assert.assertTrue(Equality.isEqual(b, b));
	}

}
