/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssociationTest {

	@Test
	public void testAssociation() {

		final Association<String, Object> assoc = new Association<>();

		final String s1 = "s1";
		final String s2 = "s2";

		final Object o1 = new Object();
		final Object o2 = new Object();

		assoc.add(s1, o1);

		Assertions.assertSame(o1, assoc.lookup(s1));
		Assertions.assertNull(assoc.lookup(s2));

		Assertions.assertSame(s1, assoc.reverseLookup(o1));
		Assertions.assertNull(assoc.reverseLookup(o2));

		assoc.add(s2, o2);

		Assertions.assertSame(o1, assoc.lookup(s1));
		Assertions.assertSame(o2, assoc.lookup(s2));

		Assertions.assertSame(s1, assoc.reverseLookup(o1));
		Assertions.assertSame(s2, assoc.reverseLookup(o2));

		assoc.clear();

		Assertions.assertNull(assoc.lookup(s1));
		Assertions.assertNull(assoc.lookup(s2));

		Assertions.assertNull(assoc.reverseLookup(o1));
		Assertions.assertNull(assoc.reverseLookup(o2));
	}
}
