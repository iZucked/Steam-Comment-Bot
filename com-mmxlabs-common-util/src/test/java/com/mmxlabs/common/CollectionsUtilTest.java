/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.common;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class CollectionsUtilTest {

	@Test
	public void makeArrayListTest() {

		// TODO: We should test the actual object instances rather than rely
		// upon equals as different instances could be equal.

		final List<Integer> list = CollectionsUtil.makeArrayList(5, 4, 3, 2, 1);

		Assert.assertEquals(5, list.size());

		Assert.assertEquals(Integer.valueOf(5), list.get(0));
		Assert.assertEquals(Integer.valueOf(4), list.get(1));
		Assert.assertEquals(Integer.valueOf(3), list.get(2));
		Assert.assertEquals(Integer.valueOf(2), list.get(3));
		Assert.assertEquals(Integer.valueOf(1), list.get(4));
	}

	@Test
	public void makeHashMapTest() {

		// TODO: We should test the actual object instances rather than rely
		// upon equals as different instances could be equal.

		final Map<String, String> map = CollectionsUtil.makeHashMap("key1",
				"value1", "key2", "value2");

		Assert.assertEquals(2, map.size());

		Assert.assertTrue(map.containsKey("key1"));
		Assert.assertTrue(map.containsKey("key2"));

		Assert.assertTrue(map.containsValue("value1"));
		Assert.assertTrue(map.containsValue("value2"));

		Assert.assertEquals("value1", map.get("key1"));
		Assert.assertEquals("value2", map.get("key2"));
	}

}
