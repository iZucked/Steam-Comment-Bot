/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.CollectionsUtil;

public class MappingTransformerTest {

	@Test
	public void testMappingTransformer() {

		final Object obj1 = new Object();
		final Object obj2 = new Object();
		final Object obj3 = new Object();
		final Object obj4 = new Object();

		final String s1 = "s1";
		final String s2 = "s2";
		final String s3 = "s3";

		final Map<Object, String> map = CollectionsUtil.makeHashMap(obj1, s1, obj2, s2, obj3, s3);

		final MappingTransformer<Object, String> transformer = new MappingTransformer<>(map);

		Assertions.assertSame(map, transformer.getMapping());

		Assertions.assertSame(s1, transformer.transform(obj1));
		Assertions.assertSame(s2, transformer.transform(obj2));
		Assertions.assertSame(s3, transformer.transform(obj3));
		Assertions.assertNull(transformer.transform(obj4));
	}
}
