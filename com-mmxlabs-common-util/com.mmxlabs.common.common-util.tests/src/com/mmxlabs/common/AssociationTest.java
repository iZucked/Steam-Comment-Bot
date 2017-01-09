/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Test;

public class AssociationTest {

	@Test
	public void testAssociation() {

		final Association<String, Object> assoc = new Association<String, Object>();

		final String s1 = "s1";
		final String s2 = "s2";

		final Object o1 = new Object();
		final Object o2 = new Object();

		assoc.add(s1, o1);

		Assert.assertSame(o1, assoc.lookup(s1));
		Assert.assertNull(assoc.lookup(s2));

		Assert.assertSame(s1, assoc.reverseLookup(o1));
		Assert.assertNull(assoc.reverseLookup(o2));

		assoc.add(s2, o2);

		Assert.assertSame(o1, assoc.lookup(s1));
		Assert.assertSame(o2, assoc.lookup(s2));

		Assert.assertSame(s1, assoc.reverseLookup(o1));
		Assert.assertSame(s2, assoc.reverseLookup(o2));

		assoc.clear();

		Assert.assertNull(assoc.lookup(s1));
		Assert.assertNull(assoc.lookup(s2));

		Assert.assertNull(assoc.reverseLookup(o1));
		Assert.assertNull(assoc.reverseLookup(o2));
	}
}
